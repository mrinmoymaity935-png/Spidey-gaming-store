package com.example.data.model

enum class UserRole {
    CUSTOMER,
    ADMIN
}

data class User(
    val id: String,
    val phoneNumber: String,
    val displayName: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val isDemoAccount: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
