package com.institute.transport.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.institute.transport.R
import com.institute.transport.data.model.UserRole
import com.institute.transport.databinding.FragmentRegisterBinding
import com.institute.transport.utils.isValidEmail
import com.institute.transport.utils.isValidPhone
import com.institute.transport.utils.showToast

/**
 * Register fragment
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRoleSpinner()
        setupClickListeners()
    }

    private fun setupRoleSpinner() {
        val roles = listOf("Passenger", "Driver")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRole.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            attemptRegister()
        }
    }

    private fun attemptRegister() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val role = if (binding.spinnerRole.selectedItemPosition == 0) {
            UserRole.PASSENGER
        } else {
            UserRole.DRIVER
        }

        // Validate input
        when {
            name.isEmpty() -> {
                binding.etName.error = getString(R.string.error_empty_field)
                return
            }
            email.isEmpty() -> {
                binding.etEmail.error = getString(R.string.error_empty_field)
                return
            }
            !email.isValidEmail() -> {
                binding.etEmail.error = getString(R.string.error_invalid_email)
                return
            }
            phone.isEmpty() -> {
                binding.etPhone.error = getString(R.string.error_empty_field)
                return
            }
            !phone.isValidPhone() -> {
                binding.etPhone.error = "Invalid phone number"
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
            password != confirmPassword -> {
                binding.etConfirmPassword.error = getString(R.string.error_passwords_dont_match)
                return
            }
        }

        // Perform registration
        viewModel.register(email, password, name, phone, role)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
