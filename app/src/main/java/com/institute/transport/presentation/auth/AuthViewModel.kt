package com.institute.transport.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.institute.transport.data.model.Result
import com.institute.transport.data.model.User
import com.institute.transport.data.model.UserRole
import com.institute.transport.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication
 */
class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    /**
     * Login user
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    _authState.value = AuthState.Success(result.data)
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(
                        result.exception.message ?: "Login failed"
                    )
                }
                else -> {}
            }
        }
    }

    /**
     * Register new user
     */
    fun register(
        email: String,
        password: String,
        name: String,
        phone: String,
        role: UserRole
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            when (val result = authRepository.register(email, password, name, phone, role)) {
                is Result.Success -> {
                    _authState.value = AuthState.Success(result.data)
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(
                        result.exception.message ?: "Registration failed"
                    )
                }
                else -> {}
            }
        }
    }

    /**
     * Reset auth state
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    /**
     * Auth state sealed class
     */
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
