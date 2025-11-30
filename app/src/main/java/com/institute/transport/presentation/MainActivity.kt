package com.institute.transport.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.institute.transport.R
import com.institute.transport.data.model.UserRole
import com.institute.transport.data.repository.AuthRepository
import com.institute.transport.presentation.auth.AuthActivity
import com.institute.transport.presentation.driver.DriverActivity
import com.institute.transport.presentation.passenger.PassengerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Main activity - Splash screen that routes to appropriate screen
 */
class MainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check authentication and route user
        lifecycleScope.launch {
            delay(1500) // Splash delay
            checkAuthAndRoute()
        }
    }

    private suspend fun checkAuthAndRoute() {
        val currentUser = authRepository.getCurrentUser()
        
        if (currentUser != null) {
            // User is logged in, get user data and route
            when (val result = authRepository.getUserData(currentUser.uid)) {
                is com.institute.transport.data.model.Result.Success -> {
                    val user = result.data
                    routeToApp(user.role)
                }
                else -> {
                    // Error getting user data, go to auth
                    routeToAuth()
                }
            }
        } else {
            // No user logged in
            routeToAuth()
        }
    }

    private fun routeToAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun routeToApp(role: UserRole) {
        val intent = when (role) {
            UserRole.DRIVER -> Intent(this, DriverActivity::class.java)
            UserRole.PASSENGER -> Intent(this, PassengerActivity::class.java)
            UserRole.ADMIN -> Intent(this, DriverActivity::class.java) // Admin uses driver UI for now
        }
        startActivity(intent)
        finish()
    }
}
