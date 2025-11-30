package com.institute.transport.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

/**
 * Utility functions for location calculations
 */
object LocationUtils {

    /**
     * Calculate distance between two points in meters
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    /**
     * Calculate distance between two LatLng points
     */
    fun calculateDistance(point1: LatLng, point2: LatLng): Float {
        return calculateDistance(
            point1.latitude,
            point1.longitude,
            point2.latitude,
            point2.longitude
        )
    }

    /**
     * Check if location is within radius of a point
     */
    fun isWithinRadius(
        centerLat: Double,
        centerLon: Double,
        pointLat: Double,
        pointLon: Double,
        radiusMeters: Float
    ): Boolean {
        val distance = calculateDistance(centerLat, centerLon, pointLat, pointLon)
        return distance <= radiusMeters
    }

    /**
     * Calculate bearing between two points in degrees
     */
    fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val lonDiff = Math.toRadians(lon2 - lon1)

        val y = sin(lonDiff) * cos(lat2Rad)
        val x = cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(lonDiff)
        
        val bearing = Math.toDegrees(atan2(y, x))
        return ((bearing + 360) % 360).toFloat()
    }

    /**
     * Format distance for display
     */
    fun formatDistance(meters: Float): String {
        return if (meters < 1000) {
            "${meters.toInt()} m"
        } else {
            String.format("%.1f km", meters / 1000)
        }
    }

    /**
     * Estimate time to arrival in minutes
     */
    fun estimateTimeToArrival(distanceMeters: Float, speedMetersPerSecond: Float): Int {
        if (speedMetersPerSecond <= 0) return -1
        val timeSeconds = distanceMeters / speedMetersPerSecond
        return (timeSeconds / 60).toInt()
    }
}
