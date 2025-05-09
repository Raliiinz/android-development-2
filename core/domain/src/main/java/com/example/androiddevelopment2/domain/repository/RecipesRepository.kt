package com.example.androiddevelopment2.domain.repository

import com.example.androiddevelopment2.domain.model.RecipeResult

interface RecipesRepository {
    suspend fun searchRecipes(ingredients: String): RecipeResult
}
