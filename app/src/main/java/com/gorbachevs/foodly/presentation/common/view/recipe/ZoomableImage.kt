package com.gorbachevs.foodly.presentation.common.view.recipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

@Suppress("ReusedModifierInstance", "LongMethod")
@ExperimentalFoundationApi
@Composable
fun ZoomableImage(
    painter: Painter,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    imageAlign: Alignment = Alignment.Center,
    shape: Shape = RectangleShape,
    maxScale: Float = 1f,
    minScale: Float = 3f,
    contentScale: ContentScale = ContentScale.Fit,
    isRotation: Boolean = false,
    isZoomable: Boolean = true,
    scrollState: ScrollableState? = null
) {
    val coroutineScope = rememberCoroutineScope()

    var scale by remember { mutableFloatStateOf(1f) }
    var rotationState by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(1f) }
    var offsetY by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = Modifier
            .clip(shape)
            .background(backgroundColor)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { },
                onDoubleClick = {
                    if (scale >= 2f) {
                        scale = 1f
                        offsetX = 1f
                        offsetY = 1f
                    } else scale = 3f
                },
            )
            .pointerInput(Unit) {
                if (isZoomable) {
                    awaitEachGesture {
                        do {
                            val event = awaitPointerEvent()
                            scale *= event.calculateZoom()
                            if (scale > 1) {
                                scrollState?.run {
                                    coroutineScope.launch {
                                        setScrolling(false)
                                    }
                                }
                                val offset = event.calculatePan()
                                offsetX += offset.x
                                offsetY += offset.y
                                rotationState += event.calculateRotation()
                                scrollState?.run {
                                    coroutineScope.launch {
                                        setScrolling(true)
                                    }
                                }
                            } else {
                                scale = 1f
                                offsetX = 1f
                                offsetY = 1f
                            }
                        } while (event.changes.any { it.pressed })
                        scale = 1f
                        offsetX = 1f
                        offsetY = 1f
                    }
                }
            }

    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
                .align(imageAlign)
                .graphicsLayer {
                    if (isZoomable) {
                        scaleX = maxOf(maxScale, minOf(minScale, scale))
                        scaleY = maxOf(maxScale, minOf(minScale, scale))
                        if (isRotation) {
                            rotationZ = rotationState
                        }
                        translationX = offsetX
                        translationY = offsetY
                    }
                }
        )
    }
}

suspend fun ScrollableState.setScrolling(isScrolling: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        if (!isScrolling) {
            awaitCancellation()
        }
    }
}
