package com.gorbachevs.foodly.presentation.common.view.loader

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshIndicator(
    isRefreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier
) {
    PullRefreshIndicator(
        refreshing = isRefreshing,
        state = state,
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}
