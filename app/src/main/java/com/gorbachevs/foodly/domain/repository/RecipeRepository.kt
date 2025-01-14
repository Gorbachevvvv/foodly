package com.gorbachevs.foodly.domain.repository

import androidx.paging.PagingData
import com.gorbachevs.foodly.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes(query: String): Flow<PagingData<Recipe>>
    suspend fun getRecipesTags(): List<String>
    suspend fun getRecipeById(id: Int): Recipe?
}
