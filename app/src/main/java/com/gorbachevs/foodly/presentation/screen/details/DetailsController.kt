package com.gorbachevs.foodly.presentation.screen.details

import android.content.Context
import androidx.compose.runtime.Stable
import com.gorbachevs.foodly.presentation.model.RecipeUi

@Stable
interface DetailsController {
    fun setPhoto(recipe: RecipeUi, isFromHomeTab: Boolean)
    fun onBookmarkClicked()
    fun onDownloadClicked(context: Context)

    fun onPhotoError(message: String)
}
