package com.example.androiddevelopment2.data.mapper

import com.example.androiddevelopment2.data.remote.pojo.RecipeDetailsResponse
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.presentation.extensions.formatRecipeInstructions
import javax.inject.Inject

class RecipeDetailsResponseMapper @Inject constructor() {
    fun map(input: RecipeDetailsResponse?) : RecipeDetailsModel {
        return input?.let {
            RecipeDetailsModel(
                id = it.id ?: 0,
                title = it.title ?: "",
                imageUrl = it.image ?: "",
                summary = it.summary?.formatRecipeInstructions() ?: "",
                instructions = it.instructions?.formatRecipeInstructions() ?: "",
                readyInMinutes = it.readyInMinutes ?: 0,
                servings = it.servings ?: 0
            )
        } ?: RecipeDetailsModel(
            id = 0,
            title = "",
            imageUrl = "",
            summary = "",
            instructions = "",
            readyInMinutes = 0,
            servings = 0
        )
    }
}
