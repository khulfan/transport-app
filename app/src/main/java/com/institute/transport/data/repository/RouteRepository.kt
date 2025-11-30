package com.institute.transport.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.institute.transport.data.model.Result
import com.institute.transport.data.model.Route
import com.institute.transport.data.model.Stop
import com.institute.transport.data.model.StopStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * Repository for route management operations
 */
class RouteRepository {
    
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    /**
     * Create a new route
     */
    suspend fun createRoute(route: Route): Result<String> {
        return try {
            val docRef = firestore.collection("routes").document()
            val newRoute = route.copy(routeId = docRef.id)
            docRef.set(newRoute).await()
            Result.Success(docRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Update existing route
     */
    suspend fun updateRoute(route: Route): Result<Unit> {
        return try {
            firestore.collection("routes")
                .document(route.routeId)
                .set(route)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Get route by ID
     */
    suspend fun getRoute(routeId: String): Result<Route> {
        return try {
            val doc = firestore.collection("routes")
                .document(routeId)
                .get()
                .await()
            
            val route = doc.toObject(Route::class.java) 
                ?: throw Exception("Route not found")
            
            Result.Success(route)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Get all routes for a driver
     */
    suspend fun getDriverRoutes(driverId: String): Result<List<Route>> {
        return try {
            val snapshot = firestore.collection("routes")
                .whereEqualTo("driverId", driverId)
                .get()
                .await()
            
            val routes = snapshot.toObjects(Route::class.java)
            Result.Success(routes)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Get all available routes
     */
    suspend fun getAllRoutes(): Result<List<Route>> {
        return try {
            val snapshot = firestore.collection("routes")
                .get()
                .await()
            
            val routes = snapshot.toObjects(Route::class.java)
            Result.Success(routes)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Observe route changes in real-time
     */
    fun observeRoute(routeId: String): Flow<Result<Route>> = callbackFlow {
        var listener: ListenerRegistration? = null
        
        try {
            listener = firestore.collection("routes")
                .document(routeId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.Error(Exception(error)))
                        return@addSnapshotListener
                    }
                    
                    snapshot?.toObject(Route::class.java)?.let { route ->
                        trySend(Result.Success(route))
                    }
                }
        } catch (e: Exception) {
            trySend(Result.Error(e))
        }
        
        awaitClose { listener?.remove() }
    }
    
    /**
     * Start trip for a route
     */
    suspend fun startTrip(routeId: String): Result<Unit> {
        return try {
            firestore.collection("routes")
                .document(routeId)
                .update(
                    mapOf(
                        "activeTrip" to true,
                        "tripStartTime" to Date(),
                        "tripEndTime" to null,
                        // Reset all stops to pending
                        "stops" to firestore.collection("routes")
                            .document(routeId)
                            .get()
                            .await()
                            .toObject(Route::class.java)
                            ?.stops
                            ?.map { it.copy(status = StopStatus.PENDING, arrivalTime = null) }
                            ?: emptyList()
                    )
                ).await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * End trip for a route
     */
    suspend fun endTrip(routeId: String): Result<Unit> {
        return try {
            firestore.collection("routes")
                .document(routeId)
                .update(
                    mapOf(
                        "activeTrip" to false,
                        "tripEndTime" to Date()
                    )
                ).await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Update stop status when arrived
     */
    suspend fun updateStopStatus(
        routeId: String,
        stopIndex: Int,
        status: StopStatus
    ): Result<Unit> {
        return try {
            val route = firestore.collection("routes")
                .document(routeId)
                .get()
                .await()
                .toObject(Route::class.java) 
                ?: throw Exception("Route not found")
            
            val updatedStops = route.stops.mapIndexed { index, stop ->
                if (index == stopIndex) {
                    stop.copy(
                        status = status,
                        arrivalTime = if (status == StopStatus.ARRIVED) Date() else stop.arrivalTime
                    )
                } else {
                    stop
                }
            }
            
            firestore.collection("routes")
                .document(routeId)
                .update("stops", updatedStops)
                .await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Delete route
     */
    suspend fun deleteRoute(routeId: String): Result<Unit> {
        return try {
            firestore.collection("routes")
                .document(routeId)
                .delete()
                .await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
