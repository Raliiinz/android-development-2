package com.example.androiddevelopment2.navigation

interface Navigator {
    fun navigateTo(screen: String, extras: Map<String, String>? = null)
}