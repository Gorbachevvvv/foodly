package com.gorbachevs.foodly.data.repository

import androidx.paging.PagingData
import com.gorbachevs.foodly.data.paging.pagingFlow
import com.gorbachevs.foodly.data.remote.FoodlyApi
import com.gorbachevs.foodly.data.remote.model.toDomain
import com.gorbachevs.foodly.domain.model.Recipe
import com.gorbachevs.foodly.domain.repository.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val foodlyApi: FoodlyApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {

    override fun getRecipes(query: String): Flow<PagingData<Recipe>> =
        pagingFlow { pageNumber, pageSize ->
            withContext(ioDispatcher) {
                val recipesEnvelope = foodlyApi.getRecipesByQuery(
                    query = query,
                    skip = (pageNumber - 1) * pageSize,
                    limit = pageSize
                )

                recipesEnvelope.recipes.map { recipeResponse ->
                    recipeResponse.toDomain()
                }
            }
        }

    override suspend fun getRecipesTags() = withContext(ioDispatcher) {
        foodlyApi.getRecipesTags()
    }

    override suspend fun getRecipeById(id: Int): Recipe? = withContext(ioDispatcher) {
        foodlyApi.getRecipeById(id)?.toDomain()
    }
}
