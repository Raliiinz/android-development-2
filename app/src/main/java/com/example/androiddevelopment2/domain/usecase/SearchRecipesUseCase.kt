package com.example.androiddevelopment2.domain.usecase

import com.example.androiddevelopment2.di.qualifies.IoDispatchers
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(ingredients: String): List<RecipeModel> {
        return withContext(dispatcher) {
            recipesRepository.searchRecipes(ingredients)
        }
    }
}
