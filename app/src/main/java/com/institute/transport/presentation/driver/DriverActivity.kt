package com.institute.transport.presentation.driver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.institute.transport.databinding.ActivityDriverBinding

/**
 * Main activity for driver role
 * Manages routes, starts trips, and tracks location
 */
class DriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Initialize driver UI
        // - Display routes list
        // - Add route button
        // - Start/Stop trip functionality
        // - Permission handling
    }
}
