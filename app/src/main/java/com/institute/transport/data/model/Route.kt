package com.institute.transport.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Route data model containing route information and stops
 */
data class Route(
    @DocumentId
    val routeId: String = "",
    val routeName: String = "",
    val driverId: String = "",
    val driverName: String = "",
    val activeTrip: Boolean = false,
    val stops: List<Stop> = emptyList(),
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null,
    val tripStartTime: Date? = null,
    val tripEndTime: Date? = null
)

/**
 * Stop model representing each stop on a route
 */
data class Stop(
    val stopIndex: Int = 0,
    val stopName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: StopStatus = StopStatus.PENDING,
    val arrivalTime: Date? = null,
    val geofenceRadius: Float = 150f  // Radius in meters
) {
    fun toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
}

enum class StopStatus {
    PENDING,
    ARRIVED,
    DEPARTED
}
