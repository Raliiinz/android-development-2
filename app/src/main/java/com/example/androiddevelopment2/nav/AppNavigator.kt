package com.example.androiddevelopment2.nav

import android.os.Bundle
import com.example.androiddevelopment2.app.R
import com.example.androiddevelopment2.domain.firebase.fcm.model.ScreenDestination
import com.example.androiddevelopment2.navigation.Navigator
import javax.inject.Inject

class AppNavigator @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate
) : Navigator {

    override fun navigateTo(screen: String, extras: Map<String, String>?) {
        val destination = ScreenDestination.fromString(screen) ?: run {
            throw IllegalArgumentException("Unknown screen: $screen")
        }

        when (destination) {
            is ScreenDestination.Search -> navigatorDelegate.navigate(R.id.action_global_search)
            is ScreenDestination.Graph -> navigatorDelegate.navigate(R.id.action_global_graph)
            is ScreenDestination.Auth -> navigatorDelegate.navigate(R.id.action_global_auth)
            is ScreenDestination.Register -> navigatorDelegate.navigate(R.id.action_global_register)
            is ScreenDestination.RecipeDetails -> {
                val bundle = Bundle().apply {
                    putInt("recipeId", destination.recipeId)
                    extras?.forEach { (key, value) ->
                        putString(key, value)
                    }
                }
                navigatorDelegate.navigate(R.id.action_global_details, bundle)
            }
        }
    }
}