package com.gorbachevs.foodly.presentation.common.view.bottomnavigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.gorbachevs.foodly.R
import com.gorbachevs.foodly.presentation.common.drawable.FoodlyIcons
import com.gorbachevs.foodly.presentation.screen.destinations.BookmarksScreenDestination
import com.gorbachevs.foodly.presentation.screen.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

data class NavigationTab(
    val direction: DirectionDestinationSpec,
    val iconSelected: Painter,
    val iconUnselected: Painter,
    val name: String,
)

@Composable
fun navigationTabs(context: Context) = listOf(
    NavigationTab(
        direction = HomeScreenDestination,
        iconSelected = FoodlyIcons.HomeFilled,
        iconUnselected = FoodlyIcons.HomeOutline,
        name = context.getString(R.string.tab_home)
    ),
    NavigationTab(
        direction = BookmarksScreenDestination,
        iconSelected = FoodlyIcons.BookmarkFilled,
        iconUnselected = FoodlyIcons.BookmarkOutline,
        name = context.getString(R.string.tab_favorites)
    )
)
