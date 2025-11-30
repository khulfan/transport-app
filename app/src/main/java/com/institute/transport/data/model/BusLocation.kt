package com.institute.transport.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Real-time bus location data
 */
data class BusLocation(
    @DocumentId
    val locationId: String = "",
    val routeId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val speed: Float = 0f,  // Speed in m/s
    val bearing: Float = 0f,  // Direction in degrees
    @ServerTimestamp
    val timestamp: Date? = null
) {
    fun toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
}
