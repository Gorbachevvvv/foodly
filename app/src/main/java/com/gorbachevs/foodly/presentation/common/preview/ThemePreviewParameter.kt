package com.gorbachevs.foodly.presentation.common.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ThemePreviewParameter : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(false, true)
}
