package com.example.androiddevelopment2.domain.firebase.remoteconfig

interface FirebaseRemoteConfigDataSource {
    suspend fun fetchConfig()
    fun getBoolean(key: String): Boolean
}