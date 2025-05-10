package com.example.androiddevelopment2.authorization.state

sealed class AuthorizationUiState {
    object Idle : AuthorizationUiState()
    object Loading : AuthorizationUiState()
}