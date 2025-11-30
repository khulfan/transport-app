package com.institute.transport.presentation.passenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.institute.transport.databinding.ActivityPassengerBinding

/**
 * Main activity for passenger role
 * Views live bus location and receives notifications
 */
class PassengerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPassengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassengerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Initialize passenger UI
        // - Route selection
        // - Live map view
        // - Stop list display
    }
}
