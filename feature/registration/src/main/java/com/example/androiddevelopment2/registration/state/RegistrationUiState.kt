package com.example.androiddevelopment2.registration.state

sealed class RegistrationUiState {
    data object Idle : RegistrationUiState()
    data object Loading : RegistrationUiState()
    data object Success : RegistrationUiState()
}