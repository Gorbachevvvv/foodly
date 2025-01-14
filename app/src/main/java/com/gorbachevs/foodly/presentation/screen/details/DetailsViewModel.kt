package com.gorbachevs.foodly.presentation.screen.details

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.gorbachevs.foodly.domain.lce.LceState
import com.gorbachevs.foodly.domain.lce.lceFlow
import com.gorbachevs.foodly.domain.lce.onEachContent
import com.gorbachevs.foodly.domain.repository.BookmarkRepository
import com.gorbachevs.foodly.presentation.common.viewmodel.StatefulViewModel
import com.gorbachevs.foodly.presentation.model.RecipeUi
import com.gorbachevs.foodly.presentation.model.toDomain
import com.gorbachevs.foodly.utils.extension.save
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : StatefulViewModel<DetailsState>(DetailsState()), DetailsController {
    private var downloadJob: Job? = null
    private var bookmarkJob: Job? = null

    override fun setPhoto(recipe: RecipeUi, isFromHomeTab: Boolean) {
        updateState { copy(recipe = recipe) }

        if (isFromHomeTab) {
            checkIfBookmarked()
        } else {
            updateState { copy(bookmarkState = LceState.Content) }
        }
    }

    private fun checkIfBookmarked() {
        bookmarkJob?.cancel()
        bookmarkJob = lceFlow {
            emit(bookmarkRepository.isBookmarked(state.recipe.id))
        }
            .onEach { updateState { copy(bookmarkState = it) } }
            .onEachContent { isBookmarked ->
                updateState { copy(recipe = recipe.copy(isBookmarked = isBookmarked)) }
            }
            .launchIn(viewModelScope)
    }

    override fun onBookmarkClicked() {
        val newPhoto = state.recipe.copy(isBookmarked = state.recipe.isBookmarked.not())
        bookmarkJob?.cancel()
        bookmarkJob = lceFlow {
            emit(bookmarkRepository.updateBookmark(newPhoto.toDomain()))
        }
            .onEach { updateState { copy(bookmarkState = it) } }
            .onEachContent {
                updateState { copy(recipe = newPhoto, shouldRefreshBookmarks = true) }
            }
            .launchIn(viewModelScope)
    }

    override fun onDownloadClicked(context: Context) {
        TODO("Not yet implemented")
    }

    override fun onPhotoError(message: String) {
        updateState { copy(isPhotoError = true) }
    }
}

data class DetailsState(
    val recipe: RecipeUi = RecipeUi.empty(),
    val isPhotoError: Boolean = false,
    val shouldRefreshBookmarks: Boolean = false,
    val recipeBitmap: Bitmap? = null,
    val recipeState: LceState = LceState.Loading,
    val bookmarkState: LceState = LceState.Loading,
    val downloadState: LceState = LceState.Content
) {
    val isDownloading get() = downloadState.isLoading
    val isBookmarkLoading get() = bookmarkState.isLoading
}
