package com.example.androiddevelopment2.data.repository

import com.example.androiddevelopment2.data.mapper.RecipeDetailsResponseMapper
import com.example.androiddevelopment2.data.remote.RecipeApi
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.domain.repository.RecipeDetailsRepository
import com.example.androiddevelopment2.domain.util.ErrorHandler
import javax.inject.Inject

class RecipeDetailsRepositoryImpl @Inject constructor(
    private val recipeApi: RecipeApi,
    private val mapper: RecipeDetailsResponseMapper,
    private val errorHandler: ErrorHandler
): RecipeDetailsRepository {

    override suspend fun getRecipeDetails(id: Int): Result<RecipeDetailsModel> {
        return try {
            val response = recipeApi.getRecipeDetails(id)
            if (response.isSuccessful) {
                response.body()?.let { recipeDetailsResponse ->
                    Result.success(mapper.map(recipeDetailsResponse))
                } ?: Result.failure(errorHandler.handleError(response.code()))
            } else {
                Result.failure(errorHandler.handleError(response.code()))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(errorHandler.handleException(e))
        }
    }

//    override suspend fun getRecipeDetails(id: Int): Result<RecipeDetailsModel> {
//        return try {
//            val response = recipeApi.getRecipeDetails(id)
//            if (response.isSuccessful) {
//                response.body()?.let { recipeDetailsResponse ->
//                    Result.success(mapper.map(recipeDetailsResponse))
//                } ?: Result.failure(errorHandler.handleError(response.code()))
//            } else {
//                Result.failure(errorHandler.handleError(response.code()))
//            }
//        } catch (e: Exception) {
//            Result.failure(errorHandler.handleException(e))
//        }
////        return recipeApi.getRecipeDetails(id = id).let(mapper::map)
//    }
}
