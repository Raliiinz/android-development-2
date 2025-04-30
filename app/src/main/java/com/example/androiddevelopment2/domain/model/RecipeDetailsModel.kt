package com.example.androiddevelopment2.domain.model

data class RecipeDetailsModel(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val summary: String,
    val instructions: String,
    val readyInMinutes: Int,
    val servings: Int,
)