package com.example.androiddevelopment2.domain.model

data class RecipeResult(
    val data: List<RecipeModel>,
    val source: Source
) {
    enum class Source {
        CACHE, API
    }
}
