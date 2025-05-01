package com.example.androiddevelopment2.presentation.screen.details.state

import com.example.androiddevelopment2.domain.model.RecipeDetailsModel

sealed interface DetailsScreenState {
    data object Initial : DetailsScreenState
    data object Loading : DetailsScreenState
    data class DetailsResult(val result: RecipeDetailsModel) : DetailsScreenState
}
