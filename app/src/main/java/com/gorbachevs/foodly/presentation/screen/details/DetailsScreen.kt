package com.gorbachevs.foodly.presentation.screen.details

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gorbachevs.foodly.R
import com.gorbachevs.foodly.presentation.common.drawable.FoodlyIcons
import com.gorbachevs.foodly.presentation.common.preview.ThemePreviewParameter
import com.gorbachevs.foodly.presentation.common.theme.FoodlyTheme
import com.gorbachevs.foodly.presentation.common.view.button.IconButton
import com.gorbachevs.foodly.presentation.common.view.button.LargeButton
import com.gorbachevs.foodly.presentation.common.view.button.TextButton
import com.gorbachevs.foodly.presentation.common.view.recipe.ZoomablePhoto
import com.gorbachevs.foodly.presentation.common.view.toolbar.Toolbar
import com.gorbachevs.foodly.presentation.model.RecipeUi
import com.gorbachevs.foodly.presentation.screen.destinations.HomeScreenDestination
import com.gorbachevs.foodly.utils.extension.hasNetwork
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Composable
@Destination(navArgsDelegate = DetailsNavArgs::class)
fun DetailsScreen(
    destinationsNavigator: DestinationsNavigator,
    refreshResultBackNavigator: ResultBackNavigator<Boolean>,
    detailsNavArgs: DetailsNavArgs,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(detailsNavArgs) {
        viewModel.setPhoto(
            recipe = detailsNavArgs.recipe,
            isFromHomeTab = detailsNavArgs.isFromHomeTab
        )
    }

    val state by viewModel.stateFlow.collectAsState()

    BackHandler {
        if (detailsNavArgs.isFromHomeTab) {
            destinationsNavigator.navigateUp()
        } else {
            refreshResultBackNavigator.navigateBack(result = state.shouldRefreshBookmarks)
        }
    }

    DetailsLayout(
        state = state,
        onBackClick = {
            if (detailsNavArgs.isFromHomeTab) {
                destinationsNavigator.navigateUp()
            } else {
                refreshResultBackNavigator.navigateBack(result = state.shouldRefreshBookmarks)
            }
        },
        onExploreClick = {
            destinationsNavigator.navigate(HomeScreenDestination) {
                popUpTo(route = HomeScreenDestination.route)
            }
        },
        controller = viewModel
    )
}

@Composable
private fun DetailsLayout(
    state: DetailsState,
    onBackClick: () -> Unit,
    onExploreClick: () -> Unit,
    controller: DetailsController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        Toolbar(
            title = state.recipe.name,
            navigationIcon = FoodlyIcons.Back,
            onNavigationIconClick = onBackClick
        )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка избранного
                BookmarkButton(
                    isBookmarked = state.recipe.isBookmarked,
                    isLoading = state.isBookmarkLoading,
                    onClick = controller::onBookmarkClicked
                )

                Spacer(modifier = Modifier.width(8.dp)) // Отступ между кнопкой и изображением

                // Картинка рецепта
                this@Column.ZoomablePhoto(
                    recipe = state.recipe,
                    modifier = Modifier
                        .weight(1f) // Заполнение оставшегося пространства
                        .wrapContentSize()
                        .align(Alignment.CenterVertically),
                    onError = controller::onPhotoError
                )
            }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.servings),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                state.recipe.ingredients.forEach { servings ->
                    Text(
                        text = "- $servings",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.calories_per_serving),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                state.recipe.ingredients.forEach { caloriesPerServing ->
                    Text(
                        text = "- $caloriesPerServing",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.prepare_time),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                state.recipe.ingredients.forEach { prepTimeMinutes ->
                    Text(
                        text = "- $prepTimeMinutes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.cooking_time),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                state.recipe.ingredients.forEach {cookTimeMinutes ->
                    Text(
                        text = "- $cookTimeMinutes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.ingredients),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                state.recipe.ingredients.forEach { ingredient ->
                    Text(
                        text = "- $ingredient",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.instructions),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                state.recipe.instructions.forEach { step ->
                    Text(
                        text = "- $step",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }
    }


@Composable
private fun ErrorStub(
    onExploreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.details_error))
        Spacer(Modifier.height(12.dp))
        TextButton(text = stringResource(R.string.explore), onClick = onExploreClick)
    }
}

@Composable
private fun BookmarkButton(isBookmarked: Boolean, isLoading: Boolean, onClick: () -> Unit) {
    val bookmarkIcon = if (isBookmarked) {
        FoodlyIcons.BookmarkFilled
    } else {
        FoodlyIcons.BookmarkOutline
    }

    val tint = if (isBookmarked) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    IconButton(
        icon = bookmarkIcon,
        loading = isLoading,
        onClick = onClick,
        tint = tint
    )
}

@Composable
private fun PhotoButtons(
    state: DetailsState,
    controller: DetailsController
) {
    val context = LocalContext.current
    val hasNetwork by remember(context) { mutableStateOf(context.hasNetwork()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = hasNetwork) {
            LargeButton(
                title = stringResource(R.string.download),
                leadingIcon = FoodlyIcons.Download,
                loading = state.isDownloading,
                onClick = { controller.onDownloadClicked(context) }
            )
        }

        Spacer(Modifier.weight(1f))

        BookmarkButton(
            isBookmarked = state.recipe.isBookmarked,
            isLoading = state.isBookmarkLoading,
            onClick = controller::onBookmarkClicked
        )
    }
}

@Preview
@Composable
private fun Preview(@PreviewParameter(ThemePreviewParameter::class) useDarkTheme: Boolean) {
    val controller = object : DetailsController {
        override fun setPhoto(recipe: RecipeUi, isFromHomeTab: Boolean) = Unit
        override fun onBookmarkClicked() = Unit
        override fun onDownloadClicked(context: Context) = Unit
        override fun onPhotoError(message: String) = Unit
    }
    val state = DetailsState(recipe = RecipeUi.empty())

    FoodlyTheme(darkTheme = useDarkTheme) {
        DetailsLayout(
            state = state,
            onBackClick = { },
            onExploreClick = { },
            controller = controller
        )
    }
}
