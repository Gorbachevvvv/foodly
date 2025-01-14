package com.gorbachevs.foodly.data.local.converters

import android.util.Log
import androidx.room.TypeConverter

class StringListTypeConverter {
    @TypeConverter
    fun toEntity(list: List<String>) = list.toString()

    @TypeConverter
    fun fromEntity(joinedList: String): List<String> {
        val result = ArrayList<String>()
        val split = joinedList
            .replace("[","")
            .replace("]","")
            .replace(" ","")
            .split(",")

        for (n in split) {
            try {
                result.add(n)
            } catch (e: Exception) {
                Log.e("RoomConverter", "List of strings", e)
            }
        }
        return result
    }
}