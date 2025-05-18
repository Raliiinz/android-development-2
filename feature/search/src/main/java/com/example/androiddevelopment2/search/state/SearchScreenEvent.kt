package com.example.androiddevelopment2.search.state

sealed interface SearchScreenEvent {
    data class OnSearchButtonClicked(val query: String) : SearchScreenEvent
    data class OnListItemClick(val recipeId: Int) : SearchScreenEvent
    data object OnGraphButtonClicked : SearchScreenEvent
}
