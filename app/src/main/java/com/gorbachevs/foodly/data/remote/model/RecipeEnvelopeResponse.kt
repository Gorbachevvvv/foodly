package com.gorbachevs.foodly.data.remote.model


import com.google.gson.annotations.SerializedName

data class RecipeEnvelopeResponse(
    @SerializedName("recipes")
    val recipes: List<RecipeResponse>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("limit")
    val limit: Int
)