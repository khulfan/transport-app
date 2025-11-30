package com.institute.transport.service

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.institute.transport.data.model.Stop
import kotlinx.coroutines.tasks.await

/**
 * Manager for geofencing operations
 */
class GeofenceManager(private val context: Context) {

    private val geofencingClient: GeofencingClient = 
        LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    /**
     * Add geofences for all stops in a route
     */
    suspend fun addGeofences(routeId: String, stops: List<Stop>): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        val geofences = stops.mapIndexed { index, stop ->
            createGeofence(routeId, index, stop)
        }

        val geofencingRequest = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofences)
        }.build()

        return try {
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Remove all geofences for a route
     */
    suspend fun removeGeofences(routeId: String, stopCount: Int): Boolean {
        val geofenceIds = (0 until stopCount).map { index ->
            getGeofenceId(routeId, index)
        }

        return try {
            geofencingClient.removeGeofences(geofenceIds).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Remove all geofences
     */
    suspend fun removeAllGeofences(): Boolean {
        return try {
            geofencingClient.removeGeofences(geofencePendingIntent).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Create a geofence for a stop
     */
    private fun createGeofence(routeId: String, stopIndex: Int, stop: Stop): Geofence {
        return Geofence.Builder()
            .setRequestId(getGeofenceId(routeId, stopIndex))
            .setCircularRegion(
                stop.latitude,
                stop.longitude,
                stop.geofenceRadius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()
    }

    /**
     * Generate unique ID for geofence
     */
    private fun getGeofenceId(routeId: String, stopIndex: Int): String {
        return "${routeId}_stop_${stopIndex}"
    }

    companion object {
        const val GEOFENCE_RADIUS_METERS = 150f
    }
}
