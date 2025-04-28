package com.example.androiddevelopment2.domain.repository

import com.example.androiddevelopment2.domain.model.RecipeModel

interface RecipesRepository {
    suspend fun searchRecipes(ingredients: String): Result<List<RecipeModel>>
}