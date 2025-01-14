package com.gorbachevs.foodly.presentation.screen.home

import androidx.compose.runtime.Stable
import com.gorbachevs.foodly.presentation.model.ChipUi

@Stable
interface HomeController {
    fun saveScrollPosition(scrollPosition: Int)
    fun onSearchQueryChanged(query: String)
    fun onSearchResetClicked()
    fun onCollectionClicked(collection: ChipUi)
    fun onExploreClicked()
    fun onRetryClicked()
}
