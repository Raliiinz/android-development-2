package com.example.androiddevelopment2.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import com.example.androiddevelopment2.data.local.datasource.UserPreferencesDataSource
import com.example.androiddevelopment2.domain.repository.UserPreferencesRepository

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : UserPreferencesRepository {

    override suspend fun saveLoginState(isLoggedIn: Boolean, phone: String?) {
        dataSource.saveLoginState(isLoggedIn, phone)
    }

    override suspend fun clearAuthData() {
        dataSource.clearAuthData()
    }

    override val authState: Flow<Pair<Boolean, String?>>
        get() = dataSource.authState
}
