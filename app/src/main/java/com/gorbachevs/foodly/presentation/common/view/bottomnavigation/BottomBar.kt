package com.gorbachevs.foodly.presentation.common.view.bottomnavigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gorbachevs.foodly.presentation.common.animation.IconAnimatedVisibility
import com.gorbachevs.foodly.presentation.screen.NavGraphs
import com.gorbachevs.foodly.presentation.screen.appCurrentDestinationAsState
import com.gorbachevs.foodly.presentation.screen.destinations.Destination
import com.gorbachevs.foodly.presentation.screen.startAppDestination

@Composable
fun BottomBar(
    navController: NavController,
) {
    val navigationTabs = navigationTabs(LocalContext.current)

    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    val isVisible = navigationTabs.map { it.direction as Destination }.contains(currentDestination)

    BottomNavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Color.Transparent,
        isVisible = isVisible
    ) {
        Column {
            Row(Modifier.fillMaxWidth()) {
                ActiveTabIndication(
                    position = navigationTabs.indexOfFirst { it.direction == currentDestination },
                    tabsCount = navigationTabs.size,
                    isVisible = isVisible
                )
            }
            Row(Modifier.fillMaxWidth()) {
                navigationTabs.forEach { navigationTab ->
                    BottomNavigationTab(
                        navController = navController,
                        tab = navigationTab,
                        destinations = navigationTabs,
                        selected = currentDestination == navigationTab.direction,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun ActiveTabIndication(
    position: Int,
    tabsCount: Int,
    isVisible: Boolean
) {
    IconAnimatedVisibility(position != -1 && isVisible) {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val offsetValue = with(LocalDensity.current) {
            val tabOffset = screenWidth / tabsCount / 2
            val offsetDp = screenWidth / tabsCount * position + tabOffset - 12
            offsetDp.dp.roundToPx()
        }

        val offset by animateIntOffsetAsState(
            targetValue = IntOffset(offsetValue, 0),
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            ),
            label = "indication_offset"
        )

        Spacer(
            Modifier
                .offset { offset }
                .width(24.dp)
                .height(2.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
        )
    }
}
