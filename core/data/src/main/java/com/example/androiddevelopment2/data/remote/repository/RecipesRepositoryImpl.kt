package com.example.androiddevelopment2.data.remote.repository

import com.example.androiddevelopment2.data.local.cache.RecipeCache
import com.example.androiddevelopment2.data.mapper.RecipeResponseMapper
import com.example.androiddevelopment2.domain.exception.NetworkException
import com.example.androiddevelopment2.domain.model.RecipeResult
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import com.example.androiddevelopment2.domain.util.ErrorHandler
import com.example.androiddevelopment2.network.RecipeApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val recipeApi: RecipeApi,
    private val mapper: RecipeResponseMapper,
    private val errorHandler: ErrorHandler,
    private val recipeCache: RecipeCache
) : RecipesRepository {

    override suspend fun searchRecipes(ingredients: String): RecipeResult {

        recipeCache.get(ingredients)?.let { cachedData ->
           return RecipeResult(cachedData, RecipeResult.Source.CACHE)
        }

        return try {
            val response = recipeApi.searchRecipesByIngredients(ingredients)

            if (response.isSuccessful) {
                val result = response.body()?.mapNotNull { mapper.map(it) } ?: emptyList()

                recipeCache.updateCache(ingredients, result)

                RecipeResult(result, RecipeResult.Source.API)
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
