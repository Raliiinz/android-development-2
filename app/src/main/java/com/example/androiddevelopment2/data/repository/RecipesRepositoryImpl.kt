package com.example.androiddevelopment2.data.repository

import android.net.http.HttpException
import com.example.androiddevelopment2.data.mapper.RecipeResponseMapper
import com.example.androiddevelopment2.data.remote.RecipeApi
import com.example.androiddevelopment2.domain.exception.ForbiddenException
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import com.example.androiddevelopment2.domain.util.ErrorHandler
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
                response.body()?.let { recipeResponses ->
                    recipeResponses.mapNotNull { recipeResponse ->
                        mapper.map(recipeResponse)
                    }
                } ?: emptyList()
            } else {
                throw errorHandler.handleHttpException(response.code())
            }
        } catch (_: IOException) {
            throw Exception()
        }
//            throw NetworkException(null)
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val httpError = parseHttpError(errorBody)
//
//            when (e.code()) {
//                403 -> throw ForbiddenException(httpError?.error?.message)
//                else -> throw ServerException(httpError?.error?.message)
//            }
//        }
    }
}

