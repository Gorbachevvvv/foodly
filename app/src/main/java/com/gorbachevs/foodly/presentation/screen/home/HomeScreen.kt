package com.gorbachevs.foodly.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gorbachevs.foodly.R
import com.gorbachevs.foodly.presentation.common.preview.ThemePreviewParameter
import com.gorbachevs.foodly.presentation.common.theme.FoodlyTheme
import com.gorbachevs.foodly.presentation.common.view.chip.Chip
import com.gorbachevs.foodly.presentation.common.view.loader.HorizontalLoader
import com.gorbachevs.foodly.presentation.common.view.recipe.RecipesGrid
import com.gorbachevs.foodly.presentation.common.view.toolbar.Search
import com.gorbachevs.foodly.presentation.model.ChipUi
import com.gorbachevs.foodly.presentation.model.RecipeUi
import com.gorbachevs.foodly.presentation.screen.destinations.DetailsScreenDestination
import com.gorbachevs.foodly.utils.extension.hasNetwork
import com.gorbachevs.foodly.utils.extension.showMessage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
@RootNavGraph(start = true)
@Destination
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()
    val recipes = viewModel.recipesFlow.collectAsLazyPagingItems()

    val context = LocalContext.current
    LaunchedEffect(context) {
        if (context.hasNetwork().not()) {
            context.showMessage(R.string.no_connection)
        }
    }

    HomeLayout(
        state = state,
        recipes = recipes,
        controller = viewModel,
        onPhotoClick = { recipe, position ->
            viewModel.saveScrollPosition(position)
            navigator.navigate(DetailsScreenDestination(recipe))
        }
    )
}

@Composable
private fun HomeLayout(
    state: HomeState,
    recipes: LazyPagingItems<RecipeUi>,
    controller: HomeController,
    onPhotoClick: (RecipeUi, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        Search(
            value = state.query,
            onValueChange = controller::onSearchQueryChanged,
            onValueResetClick = controller::onSearchResetClicked,
            placeholderText = stringResource(R.string.search),
        )

        ChipsContent(state.collections, controller)

        HorizontalLoader(
            loading = recipes.loadState.refresh is LoadState.Loading,
            modifier = Modifier.fillMaxWidth()
        )

        RecipesGrid(
            recipes = recipes,
            onClick = onPhotoClick,
            savedScrollPosition = state.savedScrollPosition,
            onRetryClick = {
                controller.onRetryClicked()
                recipes.retry()
            },
            onExploreClick = controller::onExploreClicked,
        )
    }
}

@Composable
private fun ChipsContent(collections: List<ChipUi>, controller: HomeController) {
    val listState = rememberLazyListState()

    LaunchedEffect(collections) {
        listState.animateScrollToItem(0)
    }

    LazyRow(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 12.dp),
        state = listState
    ) {
        items(collections.size) { position ->
            Chip(
                chip = collections[position],
                onClick = { controller.onCollectionClicked(collections[position]) }
            )
            if (position != collections.lastIndex) {
                Spacer(Modifier.width(12.dp))
            }
        }
    }
}

@Composable
@Preview
private fun Preview(@PreviewParameter(ThemePreviewParameter::class) useDarkTheme: Boolean) {
    val controller = object : HomeController {
        override fun saveScrollPosition(scrollPosition: Int) = Unit
        override fun onSearchQueryChanged(query: String) = Unit
        override fun onSearchResetClicked() = Unit
        override fun onCollectionClicked(collection: ChipUi) = Unit
        override fun onExploreClicked() = Unit
        override fun onRetryClicked() = Unit
    }

    val state = HomeState()

    val pagingData = PagingData.empty<RecipeUi>(
        sourceLoadStates = LoadStates(
            refresh = LoadState.Loading,
            append = LoadState.NotLoading(false),
            prepend = LoadState.NotLoading(false)
        )
    )
    val recipes = MutableStateFlow(pagingData).collectAsLazyPagingItems()

    FoodlyTheme(darkTheme = useDarkTheme) {
        HomeLayout(
            state = state,
            recipes = recipes,
            controller = controller,
            onPhotoClick = { _, _ -> }
        )
    }
}
