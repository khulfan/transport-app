package com.institute.transport.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Extension functions for easier UI operations
 */

/**
 * Show a short toast message
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Show a short toast message from string resource
 */
fun Context.showToast(@StringRes messageRes: Int) {
    Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
}

/**
 * Show a long toast message
 */
fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Validate email format
 */
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Validate phone number format (basic validation)
 */
fun String.isValidPhone(): Boolean {
    return this.length >= 10 && this.all { it.isDigit() || it == '+' || it == '-' || it == ' ' }
}

/**
 * Check if string is not empty
 */
fun String.isNotEmpty(): Boolean {
    return this.trim().isNotEmpty()
}
