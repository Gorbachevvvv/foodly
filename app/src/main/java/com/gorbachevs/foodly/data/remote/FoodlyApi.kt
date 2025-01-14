package com.gorbachevs.foodly.data.remote

import com.gorbachevs.foodly.data.remote.model.RecipeEnvelopeResponse
import com.gorbachevs.foodly.data.remote.model.RecipeResponse
import com.gorbachevs.foodly.utils.Constants.DEFAULT_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodlyApi {

    @GET("/recipes/search")
    suspend fun getRecipesByQuery(
        @Query("q") query: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE
    ): RecipeEnvelopeResponse

    @GET("/recipes/{id}")
    suspend fun getRecipeById(
        @Path("id") id: Int,
    ): RecipeResponse?

    @GET("/recipes/tags")
    suspend fun getRecipesTags(): List<String>
}
