package com.example.androiddevelopment2.search.state

import com.example.androiddevelopment2.domain.model.RecipeResult

sealed class SearchUiEvent {
    data class ShowDataSourceToast(val source: RecipeResult.Source) : SearchUiEvent()
}
