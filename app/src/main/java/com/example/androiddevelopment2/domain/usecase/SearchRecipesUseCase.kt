package com.example.androiddevelopment2.domain.usecase

import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import javax.inject.Inject

class SearchRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    suspend operator fun invoke(ingredients: String): List<RecipeModel> {
//        if (ingredients.isBlank()) {
//            return Result.failure(EmptyInputException())
//        }
        return recipesRepository.searchRecipes(ingredients)
    }
}