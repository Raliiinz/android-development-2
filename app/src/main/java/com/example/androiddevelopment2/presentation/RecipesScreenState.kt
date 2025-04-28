package com.example.androiddevelopment2.presentation

import com.example.androiddevelopment2.domain.model.RecipeModel

sealed class RecipesScreenState {
    data class Success(
        val recipes: List<RecipeModel>
    ) : RecipesScreenState()

    data object Loading : RecipesScreenState()

    data class Failure(val reason: FailureReason) : RecipesScreenState()

    sealed interface FailureReason {
        data object InvalidApiKey : FailureReason
        data object NoInternet : FailureReason
    }
}
