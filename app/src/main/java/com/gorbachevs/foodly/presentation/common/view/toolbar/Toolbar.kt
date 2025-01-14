package com.gorbachevs.foodly.presentation.common.view.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorbachevs.foodly.presentation.common.drawable.FoodlyIcons
import com.gorbachevs.foodly.presentation.common.preview.ThemePreviewParameter
import com.gorbachevs.foodly.presentation.common.theme.FoodlyTheme

@Composable
fun Toolbar(
    title: String,
    modifier: Modifier = Modifier
) {
    Toolbar(
        title = title,
        modifier = modifier,
        onNavigationIconClick = {}
    )
}

@Composable
fun Toolbar(
    title: String,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: Painter? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        if (navigationIcon != null) {
            NavigationIcon(
                navigationIcon = navigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun NavigationIcon(
    navigationIcon: Painter,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = navigationIcon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier
            .size(40.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onNavigationIconClick() }
            .padding(10.dp)
    )
}

@Composable
@Preview
private fun Preview(@PreviewParameter(ThemePreviewParameter::class) useDarkTheme: Boolean) {
    FoodlyTheme(
        darkTheme = useDarkTheme
    ) {
        Toolbar(
            title = "Title",
            navigationIcon = FoodlyIcons.Back,
            onNavigationIconClick = {}
        )
    }
}
