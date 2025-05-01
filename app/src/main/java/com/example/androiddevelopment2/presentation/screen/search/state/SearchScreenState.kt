package com.example.androiddevelopment2.presentation.screen.search.state

import com.example.androiddevelopment2.domain.model.RecipeModel

sealed interface SearchScreenState {
    data object Initial : SearchScreenState
    data object Loading : SearchScreenState
    data class SearchResult(val result: List<RecipeModel>) : SearchScreenState
}
