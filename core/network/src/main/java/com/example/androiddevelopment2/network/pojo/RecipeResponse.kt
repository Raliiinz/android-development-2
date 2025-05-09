package com.example.androiddevelopment2.network.pojo

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("usedIngredientCount")
    val usedIngredients: Int?,
    @SerializedName("missedIngredientCount")
    val missedIngredients: Int?
)
