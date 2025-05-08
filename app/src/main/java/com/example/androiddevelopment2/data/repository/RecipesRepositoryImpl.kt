package com.example.androiddevelopment2.data.repository

// data/repository/CachedRecipesRepositoryImpl.kt

import android.util.Log
import com.example.androiddevelopment2.data.local.cache.RecipeCache
import com.example.androiddevelopment2.data.mapper.RecipeResponseMapper
import com.example.androiddevelopment2.data.remote.RecipeApi
import com.example.androiddevelopment2.domain.exception.NetworkException
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.domain.model.RecipeResult
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import com.example.androiddevelopment2.domain.util.ErrorHandler
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
//import com.example.androiddevelopment2.data.local.database.AppDatabase
//import com.example.androiddevelopment2.data.local.entity.CachedRecipe
//import com.example.androiddevelopment2.data.mapper.RecipeResponseMapper
//import com.example.androiddevelopment2.data.remote.RecipeApi
//import com.example.androiddevelopment2.domain.exception.NetworkException
//import com.example.androiddevelopment2.domain.model.RecipeModel
//import com.example.androiddevelopment2.domain.repository.RecipesRepository
//import com.example.androiddevelopment2.domain.util.ErrorHandler
//import retrofit2.HttpException
//import java.io.IOException
//import javax.inject.Inject
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.withContext
//
//class RecipesRepositoryImpl @Inject constructor(
//    private val recipeApi: RecipeApi,
//    private val mapper: RecipeResponseMapper,
//    private val errorHandler: ErrorHandler,
//    private val database: AppDatabase,
//    private val dispatcher: CoroutineDispatcher
//) : RecipesRepository {
//
//    private val recentQueries = mutableListOf<String>()
//    private val cacheTimeoutMinutes = 5
//    private val maxQueriesBetweenSame = 3
//
//    override suspend fun searchRecipes(ingredients: String): List<RecipeModel> {
//        return withContext(dispatcher) {
//            // Проверяем кэш
//            val cached = checkCache(ingredients)
//            if (cached != null) {
//                return@withContext cached
//            }
//
//            // Если кэш невалиден, идем в API
//            try {
//                val response = recipeApi.searchRecipesByIngredients(ingredients)
//
//                if (response.isSuccessful) {
//                    val result = response.body()?.mapNotNull { mapper.map(it) } ?: emptyList()
//
//                    // Обновляем кэш
//                    updateCache(ingredients, result)
//                    result
//                } else {
//                    throw errorHandler.handleHttpException(response.code())
//                }
//            } catch (ioe: IOException) {
//                throw NetworkException("Ошибка сети: ${ioe.message ?: "неизвестная ошибка"}").apply {
//                    initCause(ioe)
//                }
//            } catch (httpException: HttpException) {
//                throw errorHandler.handleHttpException(httpException.code()).apply {
//                    initCause(httpException)
//                }
//            }
//        }
//    }
//
//    private suspend fun checkCache(query: String): List<RecipeModel>? {
//        val cached = database.cachedRecipeDao().getByQuery(query) ?: return null
//
//        // Проверяем время кэша
//        val currentTime = System.currentTimeMillis()
//        val cacheAgeMinutes = (currentTime - cached.timestamp) / (60 * 1000)
//
//        if (cacheAgeMinutes > cacheTimeoutMinutes) {
//            return null
//        }
//
//        // Проверяем количество запросов между одинаковыми
//        val lastIndex = recentQueries.indexOfLast { it == query }
//        if (lastIndex != -1) {
//            val queriesBetween = recentQueries.size - lastIndex - 1
//            if (queriesBetween >= maxQueriesBetweenSame) {
//                return null
//            }
//        }
//
//        // Обновляем счетчик запросов и сохраняем
//        recentQueries.add(query)
//        if (recentQueries.size > maxQueriesBetweenSame * 2) {
//            recentQueries.removeFirst()
//        }
//
//        return cached.recipes
//    }
//
//    private suspend fun updateCache(query: String, recipes: List<RecipeModel>) {
//        val cachedRecipe = CachedRecipe(
//            query = query,
//            recipes = recipes,
//            timestamp = System.currentTimeMillis()
//        )
//        database.cachedRecipeDao().insert(cachedRecipe)
//
//        // Обновляем список последних запросов
//        recentQueries.add(query)
//        if (recentQueries.size > maxQueriesBetweenSame * 2) {
//            recentQueries.removeFirst()
//        }
//    }
//}

//class RecipesRepositoryImpl @Inject constructor(
//    private val recipeApi: RecipeApi,
//    private val mapper: RecipeResponseMapper,
//    private val errorHandler: ErrorHandler
//): RecipesRepository {
//
//    override suspend fun searchRecipes(ingredients: String): List<RecipeModel> {
//        return try {
//            val response = recipeApi.searchRecipesByIngredients(ingredients)
//
//            if (response.isSuccessful) {
//                response.body()?.mapNotNull { mapper.map(it) } ?: emptyList()
//            } else {
//                throw errorHandler.handleHttpException(response.code())
//            }
//        } catch (ioe: IOException) {
//            throw NetworkException("Ошибка сети: ${ioe.message ?: "неизвестная ошибка"}").apply {
//                initCause(ioe)
//            }
//        } catch (httpException: HttpException) {
//            throw errorHandler.handleHttpException(httpException.code()).apply {
//                initCause(httpException)
//            }
//        }
//    }
//}
//
//
