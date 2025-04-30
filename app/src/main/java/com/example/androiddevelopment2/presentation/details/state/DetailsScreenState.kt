package com.example.androiddevelopment2.presentation.details.state

import com.example.androiddevelopment2.domain.model.RecipeDetailsModel

sealed interface DetailsScreenState {
    data object Initial : DetailsScreenState
    data object Loading : DetailsScreenState
    data class DetailsResult(val result: RecipeDetailsModel) : DetailsScreenState
    data class Error(val message: String?, val ex: Throwable) : DetailsScreenState
}