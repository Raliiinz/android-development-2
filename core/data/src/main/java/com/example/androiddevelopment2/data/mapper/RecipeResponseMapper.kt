package com.example.androiddevelopment2.data.mapper

import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.network.pojo.RecipeResponse
import javax.inject.Inject

class RecipeResponseMapper @Inject constructor() {
    fun map(input: RecipeResponse?) : RecipeModel {
        return input?.let {
            RecipeModel(
                id = it.id ?: 0,
                title = it.title ?: "",
                imageUrl = it.image ?: "",
                usedIngredients = it.usedIngredients ?: 0,
                missedIngredients = it.missedIngredients ?: 0
            )
        } ?: RecipeModel(
            id = 0,
            title = "",
            imageUrl = "",
            usedIngredients = 0,
            missedIngredients = 0
        )
    }
}
