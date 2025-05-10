package com.example.androiddevelopment2.authorization.state

data class AuthState(
    val isLoggedIn: Boolean = false,
    val userPhone: String? = null
)