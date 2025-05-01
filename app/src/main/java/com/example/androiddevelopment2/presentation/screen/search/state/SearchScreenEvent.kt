package com.example.androiddevelopment2.presentation.screen.search.state

sealed interface SearchScreenEvent {
    data class OnSearchButtonClicked(val query: String) : SearchScreenEvent
    data class OnListItemClick(val recipeId: Int) : SearchScreenEvent
}
