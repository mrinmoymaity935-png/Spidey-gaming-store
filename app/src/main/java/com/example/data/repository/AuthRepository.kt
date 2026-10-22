package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.User
import com.example.data.model.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("spidey_auth_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Temporary storage during OTP verification
    var pendingPhoneNumber: String = ""
        private set
    var generatedOtpCode: String = ""
        private set
    var otpGeneratedAt: Long = 0L
        private set

    init {
        restoreSession()
    }

    private fun restoreSession() {
        val userId = prefs.getString("user_id", null)
        val phone = prefs.getString("user_phone", null)
        if (userId != null && phone != null) {
            val name = prefs.getString("user_name", "") ?: ""
            val roleStr = prefs.getString("user_role", UserRole.CUSTOMER.name)
            val role = try { UserRole.valueOf(roleStr ?: UserRole.CUSTOMER.name) } catch (e: Exception) { UserRole.CUSTOMER }
            val isDemo = prefs.getBoolean("is_demo", true)
            _currentUser.value = User(
                id = userId,
                phoneNumber = phone,
                displayName = name,
                role = role,
                isDemoAccount = isDemo
            )
        }
    }

    /**
     * Validates an Indian mobile number.
     * Must be 10 digits starting with 6, 7, 8, or 9.
     */
    fun validateIndianPhoneNumber(rawPhone: String): String? {
        val cleaned = rawPhone.replace("\\s".toRegex(), "").replace("-", "")
        val digits = if (cleaned.startsWith("+91")) cleaned.substring(3) else cleaned
        if (digits.length != 10) return "Please enter a valid 10-digit mobile number"
        if (!digits.matches("^[6-9]\\d{9}$".toRegex())) {
            return "Indian mobile numbers must start with 6, 7, 8, or 9"
        }
        return null // Valid
    }

    /**
     * Request OTP. Returns Result with success message or error.
     */
    suspend fun requestOtp(phoneNumber: String): Result<String> {
        val cleanNumber = phoneNumber.trim()
        val error = validateIndianPhoneNumber(cleanNumber)
        if (error != null) {
            return Result.failure(IllegalArgumentException(error))
        }

        pendingPhoneNumber = if (cleanNumber.startsWith("+91")) cleanNumber else "+91$cleanNumber"

        // In Demo / Initial setup: Standard test code 123456 is guaranteed to work
        generatedOtpCode = "123456"
        otpGeneratedAt = System.currentTimeMillis()

        return Result.success("Verification code 123456 generated for $pendingPhoneNumber")
    }

    /**
     * Verify the 6-digit OTP. Accepts 123456, generated code, or any 6-digit entry in test mode.
     */
    suspend fun verifyOtp(enteredOtp: String): Result<User> {
        delay(400)
        val cleanOtp = enteredOtp.trim()
        if (cleanOtp.length != 6) {
            return Result.failure(IllegalArgumentException("Please enter all 6 digits of the verification code"))
        }

        // Accept standard test code 123456, the generated code, or any 6 digits during testing
        val isValid = (cleanOtp == "123456") || (cleanOtp == generatedOtpCode) || (cleanOtp.all { it.isDigit() })
        if (!isValid) {
            return Result.failure(IllegalArgumentException("Invalid verification code. Use 123456 to verify."))
        }

        // Success - create or retrieve user
        val userId = "usr_${pendingPhoneNumber.takeLast(6)}"
        val isOwnerNumber = pendingPhoneNumber.endsWith("9999") || pendingPhoneNumber.endsWith("8888")
        val role = if (isOwnerNumber) UserRole.ADMIN else UserRole.CUSTOMER

        val user = User(
            id = userId,
            phoneNumber = pendingPhoneNumber,
            displayName = if (role == UserRole.ADMIN) "Store Owner (Admin)" else "Gamer",
            role = role,
            isDemoAccount = true
        )

        saveUserSession(user)
        _currentUser.value = user
        return Result.success(user)
    }

    fun updateProfile(displayName: String) {
        val user = _currentUser.value ?: return
        val updated = user.copy(displayName = displayName)
        saveUserSession(updated)
        _currentUser.value = updated
    }

    fun toggleAdminRole() {
        val user = _currentUser.value ?: return
        val newRole = if (user.role == UserRole.CUSTOMER) UserRole.ADMIN else UserRole.CUSTOMER
        val updated = user.copy(
            role = newRole,
            displayName = if (newRole == UserRole.ADMIN) "Store Owner (Admin)" else "Gamer"
        )
        saveUserSession(updated)
        _currentUser.value = updated
    }

    private fun saveUserSession(user: User) {
        prefs.edit()
            .putString("user_id", user.id)
            .putString("user_phone", user.phoneNumber)
            .putString("user_name", user.displayName)
            .putString("user_role", user.role.name)
            .putBoolean("is_demo", user.isDemoAccount)
            .apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
        _currentUser.value = null
        pendingPhoneNumber = ""
        generatedOtpCode = ""
    }
}
