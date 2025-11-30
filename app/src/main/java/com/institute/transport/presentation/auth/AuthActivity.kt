package com.institute.transport.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.institute.transport.databinding.ActivityAuthBinding
import com.institute.transport.presentation.driver.DriverActivity
import com.institute.transport.presentation.passenger.PassengerActivity
import com.institute.transport.data.model.UserRole
import com.institute.transport.utils.showToast
import kotlinx.coroutines.launch

/**
 * Authentication activity with login/register tabs
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupViewPager()
        observeViewModel()
    }

    private fun setupViewPager() {
        val adapter = AuthPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Login"
                1 -> "Register"
                else -> ""
            }
        }.attach()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthState.Success -> {
                        routeToApp(state.user.role)
                    }
                    is AuthViewModel.AuthState.Error -> {
                        showToast(state.message)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun routeToApp(role: UserRole) {
        val intent = when (role) {
            UserRole.DRIVER -> Intent(this, DriverActivity::class.java)
            UserRole.PASSENGER -> Intent(this, PassengerActivity::class.java)
            UserRole.ADMIN -> Intent(this, DriverActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
