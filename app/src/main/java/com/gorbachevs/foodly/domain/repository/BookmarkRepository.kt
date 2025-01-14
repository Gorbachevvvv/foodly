package com.gorbachevs.foodly.domain.repository

import androidx.paging.PagingData
import com.gorbachevs.foodly.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarks(): Flow<PagingData<Recipe>>
    suspend fun updateBookmark(recipe: Recipe)
    suspend fun isBookmarked(id: Int): Boolean
}
