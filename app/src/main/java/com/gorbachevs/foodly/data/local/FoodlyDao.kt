package com.gorbachevs.foodly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gorbachevs.foodly.data.local.model.RecipeEntity

@Dao
interface FoodlyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(recipe: RecipeEntity)

    @Query("SELECT * FROM recipe LIMIT :pageSize OFFSET :offset")
    fun getPhotos(offset: Int, pageSize: Int,): List<RecipeEntity>

    @Query("DELETE FROM recipe WHERE id = :id")
    fun deletePhoto(id: Int)

    @Query("SELECT isBookmarked FROM recipe WHERE id = :id")
    fun isBookmarked(id: Int): Boolean
}
