package com.institute.transport.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.institute.transport.R
import com.institute.transport.databinding.FragmentLoginBinding
import com.institute.transport.utils.isValidEmail
import com.institute.transport.utils.showToast

/**
 * Login fragment
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        // Validate input
        when {
            email.isEmpty() -> {
                binding.etEmail.error = getString(R.string.error_empty_field)
                return
            }
            !email.isValidEmail() -> {
                binding.etEmail.error = getString(R.string.error_invalid_email)
                return
            }
            password.isEmpty() -> {
                binding.etPassword.error = getString(R.string.error_empty_field)
                return
            }
            password.length < 6 -> {
                binding.etPassword.error = getString(R.string.error_weak_password)
                return
            }
        }

        // Perform login
        viewModel.login(email, password)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
