package com.gorbachevs.foodly.data.repository

import androidx.paging.PagingData
import com.gorbachevs.foodly.data.local.FoodlyDao
import com.gorbachevs.foodly.data.local.model.toDomain
import com.gorbachevs.foodly.data.local.model.toEntity
import com.gorbachevs.foodly.data.paging.pagingFlow
import com.gorbachevs.foodly.domain.model.Recipe
import com.gorbachevs.foodly.domain.repository.BookmarkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BookmarkRepositoryImpl(
    private val foodlyDao: FoodlyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookmarkRepository {
    override fun getBookmarks(): Flow<PagingData<Recipe>> =
        pagingFlow { pageNumber, pageSize ->
            withContext(ioDispatcher) {
                foodlyDao.getPhotos(
                    offset = pageNumber * pageSize,
                    pageSize = pageSize
                ).map { it.toDomain().copy(isBookmarked = true) }
            }
        }

    override suspend fun updateBookmark(recipe: Recipe) = withContext(ioDispatcher) {
        if (recipe.isBookmarked) {
            foodlyDao.insertPhoto(recipe.toEntity())
        } else {
            foodlyDao.deletePhoto(recipe.id)
        }
    }

    override suspend fun isBookmarked(id: Int): Boolean = withContext(ioDispatcher) {
        return@withContext foodlyDao.isBookmarked(id)
    }
}
