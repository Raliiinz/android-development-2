package com.example.androiddevelopment2.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveLoginState(isLoggedIn: Boolean, phone: String?)
    suspend fun clearAuthData()
    val authState: Flow<Pair<Boolean, String?>>
}
