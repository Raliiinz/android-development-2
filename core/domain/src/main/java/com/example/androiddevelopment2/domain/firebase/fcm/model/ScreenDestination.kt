package com.example.androiddevelopment2.domain.firebase.fcm.model

sealed class ScreenDestination(val route: String) {
    object Search : ScreenDestination("search")
    object Graph : ScreenDestination("graph")
    object Auth : ScreenDestination("auth")
    object Register : ScreenDestination("register")
    data class RecipeDetails(val recipeId: Int) : ScreenDestination("recipe_details") {
        companion object {
            private const val ID_ARG = "recipeId"
            fun createRoute(recipeId: Int) = "recipe_details?$ID_ARG=$recipeId"
        }
    }

    companion object {
        fun fromString(screen: String): ScreenDestination? {
            return when {
                screen.startsWith("search") -> Search
                screen.startsWith("graph") -> Graph
                screen.startsWith("auth") -> Auth
                screen.startsWith("register") -> Register
                screen.startsWith("recipe_details") -> {
                    val recipeId = screen.extractIntArg("recipeId")
                    if (recipeId != null) RecipeDetails(recipeId) else null
                }
                else -> null
            }
        }

        private fun String.extractIntArg(argName: String): Int? {
            return try {
                substringAfter("$argName=", "")
                    .substringBefore("&")
                    .takeIf { it.isNotEmpty() }
                    ?.toInt()
            } catch (e: Exception) {
                null
            }
        }
    }
}