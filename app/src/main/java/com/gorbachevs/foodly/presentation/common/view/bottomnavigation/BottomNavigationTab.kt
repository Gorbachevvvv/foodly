package com.gorbachevs.foodly.presentation.common.view.bottomnavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate

@Suppress("MagicNumber")
@Composable
internal fun RowScope.BottomNavigationTab(
    navController: NavController,
    tab: NavigationTab,
    destinations: List<NavigationTab>,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.weight(1f)) {
        this@BottomNavigationTab.NavigationBarItem(
            icon = {
                Box(modifier = Modifier.wrapContentWidth(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = if (selected) tab.iconSelected else tab.iconUnselected,
                        contentDescription = tab.name
                    )
                }
            },
            alwaysShowLabel = true,
            selected = selected,
            enabled = !selected,
            onClick = {
                navController.navigate(tab.direction) {
                    launchSingleTop = true

                    val firstBottomBarDestination = navController.currentBackStack.value
                        .firstOrNull { navBackStackEntry ->
                            checkForDestinations(
                                destinations,
                                navBackStackEntry
                            )
                        }
                        ?.destination
                    // remove all navigation items from the stack
                    // so only the currently selected screen remains in the stack
                    if (firstBottomBarDestination != null) {
                        popUpTo(firstBottomBarDestination.id) {
                            inclusive = true
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            colors = getNavigationBarColors(),
        )
    }
}

fun checkForDestinations(
    navigationRoutes: List<NavigationTab>,
    navBackStackEntry: NavBackStackEntry
): Boolean {
    navigationRoutes.forEach { tab ->
        if (tab.direction.route == navBackStackEntry.destination.route) {
            return true
        }
    }
    return false
}

@Composable
private fun getNavigationBarColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    unselectedIconColor = MaterialTheme.colorScheme.secondary,
    indicatorColor = MaterialTheme.colorScheme.background,
)
