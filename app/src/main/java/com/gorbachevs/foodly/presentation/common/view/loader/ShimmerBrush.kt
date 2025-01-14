package com.gorbachevs.foodly.presentation.common.view.loader

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.gorbachevs.foodly.presentation.common.theme.FoodlyColors.ShimmerCenterDark
import com.gorbachevs.foodly.presentation.common.theme.FoodlyColors.ShimmerCenterLight
import com.gorbachevs.foodly.presentation.common.theme.FoodlyColors.ShimmerEndDark
import com.gorbachevs.foodly.presentation.common.theme.FoodlyColors.ShimmerEndLight
import com.gorbachevs.foodly.presentation.common.theme.FoodlyColors.ShimmerStartDark
import com.gorbachevs.foodly.presentation.common.theme.FoodlyColors.ShimmerStartLight

const val START_OFFSET = 10f

@Composable
fun getShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0F,
        targetValue = 1000F,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
            label = ""
    )

    val colors = if (isSystemInDarkTheme()) {
        listOf(ShimmerStartDark, ShimmerCenterDark, ShimmerEndDark)
    } else {
        listOf(ShimmerStartLight, ShimmerCenterLight, ShimmerEndLight)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(START_OFFSET, START_OFFSET),
        end = Offset(translateAnim, translateAnim)
    )
}
