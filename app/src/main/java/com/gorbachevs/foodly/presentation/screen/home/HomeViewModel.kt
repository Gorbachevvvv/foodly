package com.gorbachevs.foodly.presentation.screen.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.gorbachevs.foodly.domain.lce.lceFlow
import com.gorbachevs.foodly.domain.lce.mapLceContent
import com.gorbachevs.foodly.domain.lce.onEachContent
import com.gorbachevs.foodly.domain.repository.RecipeRepository
import com.gorbachevs.foodly.presentation.common.viewmodel.StatefulViewModel
import com.gorbachevs.foodly.presentation.model.ChipUi
import com.gorbachevs.foodly.presentation.model.RecipeUi
import com.gorbachevs.foodly.presentation.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recipesRepository: RecipeRepository
) : StatefulViewModel<HomeState>(HomeState()), HomeController {
    private var recipesJob: Job? = null
    private var collectionsJob: Job? = null
    private var searchJob: Job? = null

    val recipesFlow = MutableStateFlow<PagingData<RecipeUi>>(PagingData.empty())
    private val searchQueryStateFlow = MutableStateFlow("")

    init {
        subscribeToSearchQueryFlow()
        getFeaturedCollections()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun subscribeToSearchQueryFlow() {
        searchJob?.cancel()
        searchJob = searchQueryStateFlow
            .debounce(SEARCH_DEBOUNCE)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                lceFlow {
                    emit(loadRecipes(query))
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadRecipes(query: String = "") {
        recipesJob?.cancel()
        recipesJob = recipesRepository.getRecipes(query)
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { recipe ->
                    recipe.toUi()
                }
            }
            .onEach(recipesFlow::emit)
            .launchIn(viewModelScope)
    }

    override fun saveScrollPosition(scrollPosition: Int) {
        updateState { copy(savedScrollPosition = scrollPosition) }
    }

    override fun onSearchQueryChanged(query: String) {
        saveScrollPosition(0)
        updateState { copy(query = query) }
        searchQueryStateFlow.update { query }
        checkCollections(query)
    }

    override fun onSearchResetClicked() = onSearchQueryChanged("")
    private fun getFeaturedCollections() {
        collectionsJob?.cancel()
        collectionsJob = lceFlow {
            emit(recipesRepository.getRecipesTags())
        }
            .mapLceContent { collections ->
                collections.map { title ->
                    ChipUi(text = title, isSelected = false)
                }
            }
            .onEachContent { collections ->
                updateState { copy(collections = collections, originalCollections = collections) }
            }
            .launchIn(viewModelScope)
    }

    override fun onCollectionClicked(collection: ChipUi) {
        updateState { copy(query = collection.text) }
        searchQueryStateFlow.update { collection.text }

        val collections = state.originalCollections.map {
            if (collection.text == it.text) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }

        updateState { copy(collections = collections) }
        changeSelectedCollectionPosition()
    }

    override fun onExploreClicked() {
        subscribeToSearchQueryFlow()
        if (state.collections.isEmpty()) {
            getFeaturedCollections()
        }
    }

    override fun onRetryClicked() {
        if (state.collections.isEmpty()) {
            getFeaturedCollections()
        }
    }

    private fun checkCollections(query: String) {
        val collections = state.collections.map { collection ->
            if (collection.text == query) {
                collection.copy(isSelected = true)
            } else {
                collection.copy(isSelected = false)
            }
        }

        updateState { copy(collections = collections) }
        changeSelectedCollectionPosition()
    }

    private fun changeSelectedCollectionPosition() {
        var collections = state.collections.toMutableList()
        val selectedCollection = state.collections.find { it.isSelected }
        if (selectedCollection != null) {
            collections.remove(selectedCollection)
            collections.add(0, selectedCollection)
        } else {
            collections = state.originalCollections.toMutableList()
        }
        updateState { copy(collections = collections) }
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 300L
    }
}

data class HomeState(
    val query: String = "",
    val collections: List<ChipUi> = emptyList(),
    val originalCollections: List<ChipUi> = emptyList(),
    val savedScrollPosition: Int = 0
)
