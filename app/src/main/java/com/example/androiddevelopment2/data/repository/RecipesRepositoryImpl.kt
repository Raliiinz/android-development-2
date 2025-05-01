package com.example.androiddevelopment2.data.repository

import com.example.androiddevelopment2.data.mapper.RecipeResponseMapper
import com.example.androiddevelopment2.data.remote.RecipeApi
import com.example.androiddevelopment2.domain.exception.NetworkException
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import com.example.androiddevelopment2.domain.util.ErrorHandler
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val recipeApi: RecipeApi,
    private val mapper: RecipeResponseMapper,
    private val errorHandler: ErrorHandler
): RecipesRepository {

    override suspend fun searchRecipes(ingredients: String): List<RecipeModel> {
        return try {
            val response = recipeApi.searchRecipesByIngredients(ingredients)

            if (response.isSuccessful) {
                response.body()?.mapNotNull { mapper.map(it) } ?: emptyList()
            } else {
                throw errorHandler.handleHttpException(response.code())
            }
        } catch (ioe: IOException) {
            throw NetworkException("Ошибка сети: ${ioe.message ?: "неизвестная ошибка"}").apply {
                initCause(ioe)
            }
        } catch (httpException: HttpException) {
            throw errorHandler.handleHttpException(httpException.code()).apply {
                initCause(httpException)
            }
        }
    }
}
