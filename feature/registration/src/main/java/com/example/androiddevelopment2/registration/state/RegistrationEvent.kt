package com.example.androiddevelopment2.registration.state

sealed class RegistrationEvent {
    data class ShowError(val error: RegistrationError) : RegistrationEvent()
}

enum class RegistrationError {
    UserAlreadyExists,
    Unknown
}