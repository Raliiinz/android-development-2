package com.example.androiddevelopment2.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class RecipeDetailsResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("instructions")
    val instructions: String?,
//    @SerializedName("extendedIngredients")
//    val ingredients: List<Ingredient?>
)

data class Ingredient(
    @SerializedName("name")
    val name: String?,
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("unit")
    val unit: String?
)