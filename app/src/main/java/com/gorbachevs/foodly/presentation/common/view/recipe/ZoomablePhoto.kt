package com.gorbachevs.foodly.presentation.common.view.recipe

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.gorbachevs.foodly.presentation.common.view.loader.getShimmerBrush
import com.gorbachevs.foodly.presentation.model.RecipeUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.ZoomablePhoto(
    recipe: RecipeUi,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()

            .clip(RoundedCornerShape(16.dp))
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(recipe.image)
                .scale(Scale.FIT)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        )

        ZoomableImage(
            painter = painter,
            imageAlign = Alignment.Center,
            contentScale = ContentScale.Inside, // Сохраняет естественный размер изображения
            scrollState = scrollState,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.wrapContentSize() // Устанавливает размер равным содержимому
        )
    }
}
