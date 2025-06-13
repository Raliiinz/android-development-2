package com.example.androiddevelopment2.domain.usecase

import com.example.androiddevelopment2.domain.di.qualifies.IoDispatchers
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.domain.repository.RecipeDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(
    private val recipesRepository: RecipeDetailsRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(recipeId: Int): RecipeDetailsModel {
        return withContext(dispatcher) {
            recipesRepository.getRecipeDetails(recipeId)
        }
    }
}
