package com.example.androiddevelopment2.authorization.state

sealed class AuthorizationEvent {
    data class ShowError(val message: AuthorizationError) : AuthorizationEvent()
}

enum class AuthorizationError {
    InvalidCredentials,
    Unknown
}