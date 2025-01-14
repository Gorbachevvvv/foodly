package com.gorbachevs.foodly.presentation.common.view.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.gorbachevs.foodly.presentation.common.animation.IconAnimatedVisibility
import com.gorbachevs.foodly.presentation.common.drawable.FoodlyIcons
import com.gorbachevs.foodly.presentation.common.preview.ThemePreviewParameter
import com.gorbachevs.foodly.presentation.common.theme.FoodlyTheme
import com.gorbachevs.foodly.presentation.common.view.loader.CircularLoader

@Suppress("ModifierHeightWithText")
@Composable
fun LargeButton(
    title: String,
    leadingIcon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(enabled = enabled && !loading) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularLoader(
                loading = loading,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            IconAnimatedVisibility(!loading) {
                Icon(
                    painter = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Spacer(Modifier.width(18.dp))

        Text(
            text = title,
            maxLines = 1
        )

        Spacer(Modifier.width(38.dp))
    }
}

@Preview
@Composable
private fun Preview(@PreviewParameter(ThemePreviewParameter::class) useDarkTheme: Boolean) {
    FoodlyTheme(darkTheme = useDarkTheme) {
        Column(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LargeButton(
                title = "Download",
                leadingIcon = FoodlyIcons.Download,
                onClick = { }
            )
        }
    }
}
