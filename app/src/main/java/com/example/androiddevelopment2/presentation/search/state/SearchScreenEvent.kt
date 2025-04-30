package com.example.androiddevelopment2.presentation.search.state

import com.example.androiddevelopment2.domain.model.RecipeModel

sealed interface SearchScreenEvent {
    data class OnSearchQueryChanged(val query: String) : SearchScreenEvent
    data class OnListItemClick(val recipeId: Int) : SearchScreenEvent
}