package com.gorbachevs.foodly.presentation.screen.details

import com.gorbachevs.foodly.presentation.model.RecipeUi

data class DetailsNavArgs(
    val recipe: RecipeUi,
    val isFromHomeTab: Boolean = true
)
