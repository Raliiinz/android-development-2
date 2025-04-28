package com.example.androiddevelopment2.domain.usecase

import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.domain.repository.RecipeDetailsRepository
import javax.inject.Inject

//class GetRecipeDetailsUseCase @Inject constructor(
//    private val repository: RecipeDetailsRepository
//) {
//    suspend operator fun invoke(id: Int): Result<RecipeDetailsModel> {
//        return repository.getRecipeDetails(id)
//    }
//}

class GetRecipeDetailsUseCase @Inject constructor(
    private val recipesRepository: RecipeDetailsRepository
) {
    suspend operator fun invoke(recipeId: Int): Result<RecipeDetailsModel> {
        return recipesRepository.getRecipeDetails(recipeId)
    }
}