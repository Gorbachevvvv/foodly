package com.gorbachevs.foodly.presentation.common.drawable

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.gorbachevs.foodly.R

object FoodlyIcons {
    val HomeFilled: Painter @Composable get() = painterResource(R.drawable.ic_home_filled)
    val HomeOutline: Painter @Composable get() = painterResource(R.drawable.ic_home_outline)
    val BookmarkFilled: Painter @Composable get() = painterResource(R.drawable.ic_bookmark_filled)
    val BookmarkOutline: Painter @Composable get() = painterResource(R.drawable.ic_bookmark_outline)
    val Search: Painter @Composable get() = painterResource(R.drawable.ic_search)
    val Close: Painter @Composable get() = painterResource(R.drawable.ic_close)
    val NoNetwork: Painter @Composable get() = painterResource(R.drawable.ic_no_network)
    val Back: Painter @Composable get() = painterResource(R.drawable.ic_back)
    val Download: Painter @Composable get() = painterResource(R.drawable.ic_download)
}
