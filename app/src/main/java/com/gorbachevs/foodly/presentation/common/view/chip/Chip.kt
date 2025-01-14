package com.gorbachevs.foodly.presentation.common.view.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.gorbachevs.foodly.presentation.common.preview.ThemePreviewParameter
import com.gorbachevs.foodly.presentation.common.theme.FoodlyTheme
import com.gorbachevs.foodly.presentation.model.ChipUi

@Composable
fun Chip(
    chip: ChipUi,
    onClick: () -> Unit
) {
    val chipColor = if (chip.isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor = if (chip.isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    val fontWeight = if (chip.isSelected) FontWeight.Bold else FontWeight.Normal

    Text(
        text = chip.text,
        color = textColor,
        fontWeight = fontWeight,
        modifier = Modifier
            .background(color = chipColor, shape = RoundedCornerShape(100.dp))
            .clip(RoundedCornerShape(100.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    )
}

@Composable
@Preview
private fun Preview(@PreviewParameter(ThemePreviewParameter::class) useDarkTheme: Boolean) {
    FoodlyTheme(darkTheme = useDarkTheme) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
        ) {
            Chip(ChipUi(text = "Ice", isSelected = true), onClick = {})
            Spacer(Modifier.padding(12.dp))
            Chip(ChipUi(text = "Watches", isSelected = false), onClick = {})
        }
    }
}
