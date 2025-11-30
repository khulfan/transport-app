package com.institute.transport.util

import android.os.Build

/**
 * Utility object for handling Android version compatibility
 * Provides safe version checks and compatibility helpers
 */
object VersionCompatibility {
    
    /**
     * Check if device is running Android 14 (API 34) or higher
     */
    fun isAndroid14OrHigher(): Boolean = Build.VERSION.SDK_INT >= 34
    
    /**
     * Check if device is running Android 13 (API 33) or higher
     * Required for: POST_NOTIFICATIONS permission
     */
    fun isAndroid13OrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    
    /**
     * Check if device is running Android 12 (API 31) or higher
     * Required for: Splash Screen API, Exact alarm permissions
     */
    fun isAndroid12OrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    
    /**
     * Check if device is running Android 11 (API 30) or higher
     * Required for: Scoped storage enforcement
     */
    fun isAndroid11OrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    
    /**
     * Check if device is running Android 10 (API 29) or higher
     * Required for: Background location permission
     */
    fun isAndroid10OrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    
    /**
     * Check if device is running Android 9 (API 28) or higher
     * Required for: Foreground service restrictions
     */
    fun isAndroid9OrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    
    /**
     * Check if device is running Android 8 (API 26) or higher
     * Required for: Notification channels
     */
    fun isAndroid8OrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    
    /**
     * Get human-readable Android version name
     */
    fun getAndroidVersionName(): String {
        return when {
            Build.VERSION.SDK_INT >= 34 -> "Android 14"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> "Android 13"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> "Android 12"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> "Android 11"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> "Android 10"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> "Android 9"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> "Android 8"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> "Android 7"
            else -> "Android ${Build.VERSION.SDK_INT}"
        }
    }
    
    /**
     * Get device information for debugging
     */
    fun getDeviceInfo(): String {
        return buildString {
            append("Device: ${Build.MANUFACTURER} ${Build.MODEL}\n")
            append("Android Version: ${getAndroidVersionName()} (API ${Build.VERSION.SDK_INT})\n")
            append("SDK Int: ${Build.VERSION.SDK_INT}\n")
            append("Release: ${Build.VERSION.RELEASE}\n")
        }
    }
    
    /**
     * Check if background location permission is required
     * Only needed on Android 10+
     */
    fun requiresBackgroundLocationPermission(): Boolean = isAndroid10OrHigher()
    
    /**
     * Check if notification permission is required
     * Only needed on Android 13+
     */
    fun requiresNotificationPermission(): Boolean = isAndroid13OrHigher()
    
    /**
     * Check if foreground service location type is required
     * Only needed on Android 10+
     */
    fun requiresForegroundServiceType(): Boolean = isAndroid10OrHigher()
}
