package com.example.androiddevelopment2.data.repository

import com.example.androiddevelopment2.data.mapper.RecipeDetailsResponseMapper
import com.example.androiddevelopment2.data.remote.RecipeApi
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.domain.repository.RecipeDetailsRepository
import com.example.androiddevelopment2.domain.util.ErrorHandler
import java.io.IOException
import javax.inject.Inject

class RecipeDetailsRepositoryImpl @Inject constructor(
    private val recipeApi: RecipeApi,
    private val mapper: RecipeDetailsResponseMapper,
    private val errorHandler: ErrorHandler
): RecipeDetailsRepository {

    override suspend fun getRecipeDetails(id: Int): RecipeDetailsModel {
        return try {
            val response = recipeApi.getRecipeDetails(id)

            if (response.isSuccessful) {
                response.body().let { recipe ->
                    mapper.map(recipe)
                }
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


