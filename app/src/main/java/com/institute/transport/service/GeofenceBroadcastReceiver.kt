package com.institute.transport.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.institute.transport.data.model.StopStatus
import com.institute.transport.data.repository.RouteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Broadcast receiver for geofence transitions
 * Automatically detects when bus arrives at a stop
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val routeRepository = RouteRepository()
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Geofencing error: ${geofencingEvent.errorCode}")
            return
        }

        // Check if bus entered a geofence (arrived at stop)
        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            handleStopArrival(geofencingEvent.triggeringGeofences ?: emptyList())
        }
    }

    /**
     * Handle stop arrival - update Firestore
     * This triggers Cloud Function to send WhatsApp messages
     */
    private fun handleStopArrival(triggeringGeofences: List<Geofence>) {
        triggeringGeofences.forEach { geofence ->
            val geofenceId = geofence.requestId
            parseGeofenceId(geofenceId)?.let { (routeId, stopIndex) ->
                Log.d(TAG, "Bus arrived at stop $stopIndex on route $routeId")
                
                // Update stop status in Firestore
                scope.launch {
                    routeRepository.updateStopStatus(
                        routeId = routeId,
                        stopIndex = stopIndex,
                        status = StopStatus.ARRIVED
                    )
                }
            }
        }
    }

    /**
     * Parse geofence ID to extract route ID and stop index
     * Format: "routeId_stop_stopIndex"
     */
    private fun parseGeofenceId(geofenceId: String): Pair<String, Int>? {
        return try {
            val parts = geofenceId.split("_stop_")
            if (parts.size == 2) {
                val routeId = parts[0]
                val stopIndex = parts[1].toInt()
                Pair(routeId, stopIndex)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing geofence ID: $geofenceId", e)
            null
        }
    }

    companion object {
        private const val TAG = "GeofenceBroadcast"
    }
}
