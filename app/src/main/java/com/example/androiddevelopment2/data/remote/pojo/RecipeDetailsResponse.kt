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
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("servings")
    val servings: Int?,
)
