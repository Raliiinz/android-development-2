package com.example.androiddevelopment2.data.local.datasource

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class UserPreferencesDataSource @Inject constructor(
    private val context: Context
) {

    private val Context.authDataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "auth_prefs")
    private val isLoggedIn = booleanPreferencesKey("is_logged_in")
    private val userPhone = stringPreferencesKey("user_phone")

    suspend fun saveLoginState(isLoggedInState: Boolean, phone: String?) {
        context.authDataStore.edit { prefs ->
            prefs[isLoggedIn] = isLoggedInState
            phone?.let { prefs[userPhone] = it }
        }
    }

    val authState: Flow<Pair<Boolean, String?>> = context.authDataStore.data
        .map { prefs ->
            Pair(
                prefs[isLoggedIn] ?: false,
                prefs[userPhone]
            )
        }

    suspend fun clearAuthData() {
        context.authDataStore.edit { prefs ->
            prefs.remove(isLoggedIn)
            prefs.remove(userPhone)
        }
    }
}
