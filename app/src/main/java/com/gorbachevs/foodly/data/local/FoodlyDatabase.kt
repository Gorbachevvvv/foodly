package com.gorbachevs.foodly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gorbachevs.foodly.data.local.converters.StringListTypeConverter
import com.gorbachevs.foodly.data.local.model.RecipeEntity

@TypeConverters(StringListTypeConverter::class)
@Database(
    entities = [RecipeEntity::class],
    version = FoodlyDatabase.VERSION
)
abstract class FoodlyDatabase : RoomDatabase() {
    abstract val dao: FoodlyDao

    companion object {
        const val VERSION = 1
        const val NAME = "foodly_db"
    }
}
