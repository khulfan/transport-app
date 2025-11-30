package com.institute.transport.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.institute.transport.data.model.BusLocation
import com.institute.transport.data.model.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository for bus location tracking
 */
class LocationRepository {
    
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    /**
     * Update bus location
     */
    suspend fun updateBusLocation(busLocation: BusLocation): Result<Unit> {
        return try {
            firestore.collection("busLocations")
                .document(busLocation.routeId)
                .set(busLocation)
                .await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Get latest bus location for a route
     */
    suspend fun getBusLocation(routeId: String): Result<BusLocation> {
        return try {
            val doc = firestore.collection("busLocations")
                .document(routeId)
                .get()
                .await()
            
            val location = doc.toObject(BusLocation::class.java) 
                ?: throw Exception("Location not found")
            
            Result.Success(location)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Observe bus location changes in real-time
     */
    fun observeBusLocation(routeId: String): Flow<Result<BusLocation>> = callbackFlow {
        var listener: ListenerRegistration? = null
        
        try {
            listener = firestore.collection("busLocations")
                .document(routeId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.Error(Exception(error)))
                        return@addSnapshotListener
                    }
                    
                    snapshot?.toObject(BusLocation::class.java)?.let { location ->
                        trySend(Result.Success(location))
                    }
                }
        } catch (e: Exception) {
            trySend(Result.Error(e))
        }
        
        awaitClose { listener?.remove() }
    }
}
