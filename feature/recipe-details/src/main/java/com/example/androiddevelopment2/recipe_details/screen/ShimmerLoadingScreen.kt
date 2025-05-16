package com.example.androiddevelopment2.recipe_details.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.androiddevelopment2.recipe_details.utils.Dimens

@Composable
fun ShimmerLoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = Dimens.LargeSpacer + Dimens.SmallSpacer,
                start = Dimens.SmallSpacer,
                end = Dimens.SmallSpacer
            )
    ) {
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.RecipeImageHeight)
                .clip(RoundedCornerShape(Dimens.RecipeImageCornerRadius))
        )

        Spacer(modifier = Modifier.height(Dimens.SmallSpacer))

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ShimmerTitleHeight)
        )

        Spacer(modifier = Modifier.height(Dimens.SmallSpacer))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .height(Dimens.ShimmerMetaHeight)
                    .padding(end = Dimens.ExtraSmallSpacer))
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .height(Dimens.ShimmerMetaHeight))
        }

        Spacer(modifier = Modifier.height(Dimens.MediumSpacer))

        repeat(2) { section ->
            ShimmerBox(
                modifier = Modifier
                    .width(150.dp)
                    .height(Dimens.ShimmerSectionTitleHeight)
            )

            Spacer(Modifier.height(Dimens.ExtraSmallSpacer))

            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ShimmerSectionContentHeight)
            )

            if (section == 0) Spacer(Modifier.height(Dimens.MediumSpacer))
        }
    }
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(Dimens.ShimmerCornerRadius)
) {
    val shimmerColors = remember {
        listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f)
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
