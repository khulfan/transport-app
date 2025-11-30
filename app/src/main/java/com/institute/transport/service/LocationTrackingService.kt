package com.institute.transport.service

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import com.institute.transport.R
import com.institute.transport.TransportApplication
import com.institute.transport.data.model.BusLocation
import com.institute.transport.data.repository.LocationRepository
import com.institute.transport.presentation.driver.DriverActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Foreground service for continuous GPS tracking
 */
class LocationTrackingService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationRepository = LocationRepository()
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    
    private var currentRouteId: String? = null
    private var routeName: String = "Unknown Route"

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationCallback()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TRACKING -> {
                currentRouteId = intent.getStringExtra(EXTRA_ROUTE_ID)
                routeName = intent.getStringExtra(EXTRA_ROUTE_NAME) ?: "Unknown Route"
                startForegroundService()
                startLocationUpdates()
            }
            ACTION_STOP_TRACKING -> {
                stopLocationUpdates()
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, DriverActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, TransportApplication.LOCATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.tracking_notification_title))
            .setContentText(getString(R.string.tracking_notification_text, routeName))
            .setSmallIcon(R.drawable.ic_bus)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    updateBusLocation(location)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf()
            return
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        ).apply {
            setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL)
            setWaitForAccurateLocation(true)
            setMaxUpdateDelayMillis(MAX_UPDATE_DELAY)
        }.build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateBusLocation(location: Location) {
        val routeId = currentRouteId ?: return

        val busLocation = BusLocation(
            locationId = routeId,
            routeId = routeId,
            latitude = location.latitude,
            longitude = location.longitude,
            speed = location.speed,
            bearing = location.bearing
        )

        serviceScope.launch {
            locationRepository.updateBusLocation(busLocation)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    companion object {
        const val ACTION_START_TRACKING = "com.institute.transport.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.institute.transport.STOP_TRACKING"
        const val EXTRA_ROUTE_ID = "route_id"
        const val EXTRA_ROUTE_NAME = "route_name"
        
        private const val NOTIFICATION_ID = 1001
        private const val LOCATION_UPDATE_INTERVAL = 5000L  // 5 seconds
        private const val FASTEST_UPDATE_INTERVAL = 3000L   // 3 seconds
        private const val MAX_UPDATE_DELAY = 10000L         // 10 seconds
    }
}
