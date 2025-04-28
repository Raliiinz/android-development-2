package com.example.androiddevelopment2.data.repository

import com.example.androiddevelopment2.data.mapper.RecipeResponseMapper
import com.example.androiddevelopment2.data.remote.RecipeApi
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import com.example.androiddevelopment2.domain.util.ErrorHandler
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
//    private val recipeApi: RecipeApi,
//    private val mapper: RecipeResponseMapper
    private val recipeApi: RecipeApi,
    private val mapper: RecipeResponseMapper,
    private val errorHandler: ErrorHandler
): RecipesRepository {

    override suspend fun searchRecipes(ingredients: String): Result<List<RecipeModel>> {
        return try {
            val response = recipeApi.searchRecipesByIngredients(ingredients)

            if (response.isSuccessful) {
                response.body()?.let { recipeResponses ->
                    val recipes = recipeResponses.mapNotNull { recipeResponse ->
                        mapper.map(recipeResponse)
                    }
                    Result.success(recipes)
                } ?: Result.failure(errorHandler.handleError(response.code()))
            } else {
                Result.failure(errorHandler.handleError(response.code()))
            }
        } catch (e: Exception) {
            Result.failure(errorHandler.handleException(e))
        }
    }
}
//        return try {
//            val response = recipeApi.searchRecipesByIngredients(ingredients)
//
//            val recipes = response.mapNotNull { recipeResponse ->
//                mapper.map(recipeResponse)
//            }
//
//            Result.Success(recipes)
//        } catch (e: Exception) {
//            Result.Error(e.parseError())
//        }
//    }
//    override suspend fun searchRecipes(ingredients: String): Result<List<RecipeModel>> {
//        return recipeApi.searchRecipesByIngredients(ingredients = ingredients)
//            .map { recipeResponse -> mapper.map(recipeResponse) }
//    }
