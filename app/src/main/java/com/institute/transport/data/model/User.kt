package com.institute.transport.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * User data model representing both drivers and passengers
 */
data class User(
    @DocumentId
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.PASSENGER,
    val routeId: String? = null,  // For passengers - the route they're subscribed to
    val assignedRouteIds: List<String> = emptyList(),  // For drivers - routes they manage
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null
)

enum class UserRole {
    DRIVER,
    PASSENGER,
    ADMIN
}
