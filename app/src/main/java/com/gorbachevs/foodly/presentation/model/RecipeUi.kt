package com.gorbachevs.foodly.presentation.model

import android.os.Parcelable
import com.gorbachevs.foodly.domain.model.Recipe
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeUi(
    val id: Int,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: String,
    val cuisine: String,
    val caloriesPerServing: Int,
    val tags: List<String>,
    val userId: Int,
    val image: String,
    val rating: Double,
    val reviewCount: Int,
    val mealType: List<String>,
    val isBookmarked: Boolean = false
): Parcelable {
    companion object {
        fun empty() = RecipeUi(
            id = 1,
            name = "Recipe",
            ingredients = emptyList(),
            instructions = emptyList(),
            prepTimeMinutes = 0,
            cookTimeMinutes = 0,
            servings = 0,
            difficulty = "",
            cuisine = "",
            caloriesPerServing = 0,
            tags = emptyList(),
            userId = 1,
            image = "",
            rating = 1.0,
            reviewCount = 1,
            mealType = emptyList(),
            isBookmarked = false
        )
    }
}

fun RecipeUi.toDomain() = Recipe(
    id = id,
    name = name,
    ingredients = ingredients,
    instructions = instructions,
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = servings,
    difficulty = difficulty,
    cuisine = cuisine,
    caloriesPerServing = caloriesPerServing,
    tags = tags,
    userId = userId,
    image = image,
    rating = rating,
    reviewCount = reviewCount,
    mealType = mealType,
    isBookmarked = isBookmarked
)

fun Recipe.toUi() = RecipeUi(
    id = id,
    name = name,
    ingredients = ingredients,
    instructions = instructions,
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = servings,
    difficulty = difficulty,
    cuisine = cuisine,
    caloriesPerServing = caloriesPerServing,
    tags = tags,
    userId = userId,
    image = image,
    rating = rating,
    reviewCount = reviewCount,
    mealType = mealType,
    isBookmarked = isBookmarked
)