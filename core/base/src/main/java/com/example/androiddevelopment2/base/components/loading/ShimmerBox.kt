package com.example.androiddevelopment2.base.components.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.androiddevelopment2.base.theme.Theme

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = Theme.shape.extraSmall,
    baseColor: Color = Color.LightGray.copy(alpha = 0.6f),
    highlightColor: Color = Color.LightGray.copy(alpha = 0.2f)
) {
    val shimmerColors = remember {
        listOf(
            baseColor,
            highlightColor,
            baseColor
        )
    }

    val infiniteTransition = rememberInfiniteTransition()
    val xShimmer = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = remember(xShimmer.value) {
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(xShimmer.value - 500f, xShimmer.value - 500f),
            end = Offset(xShimmer.value, xShimmer.value)
        )
    }

    Box(
        modifier = modifier
            .background(brush = brush, shape = shape)
            .clip(shape)
    )
}