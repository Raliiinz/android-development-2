package com.example.androiddevelopment2.data.mapper

import com.example.androiddevelopment2.data.remote.pojo.RecipeDetailsResponse
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel

class RecipeDetailsResponseMapper {
    fun map(input: RecipeDetailsResponse?) : RecipeDetailsModel {
        return input?.let {
            RecipeDetailsModel(
                id = it.id ?: 0,
                title = it.title ?: "",
                imageUrl = it.image ?: "",
                summary = it.summary ?: "",
                instructions = it.instructions ?: ""
            )
        } ?: RecipeDetailsModel(
            id = 0,
            title = "",
            imageUrl = "",
            summary = "",
            instructions = "",
        )
    }
}
