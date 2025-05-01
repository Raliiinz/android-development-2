package com.example.androiddevelopment2.data.remote

import com.example.androiddevelopment2.data.remote.pojo.RecipeDetailsResponse
import com.example.androiddevelopment2.data.remote.pojo.RecipeResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("findByIngredients")
    suspend fun searchRecipesByIngredients(
        @Query("ingredients") ingredients: String
    ): Response<List<RecipeResponse?>>

    @GET("{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: Int
    ): Response<RecipeDetailsResponse?>
}
