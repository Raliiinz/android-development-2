package com.example.androiddevelopment2.domain.repository

import com.example.androiddevelopment2.domain.model.RecipeDetailsModel

interface RecipeDetailsRepository {
    suspend fun getRecipeDetails(id: Int): RecipeDetailsModel
}
