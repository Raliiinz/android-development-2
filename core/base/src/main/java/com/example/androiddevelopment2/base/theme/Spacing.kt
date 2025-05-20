package com.example.androiddevelopment2.base.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val recipeImageHeight: Dp = 256.dp,
    val graphHeight: Dp = 300.dp,
    val iconSize: Dp = 24.dp,
    val shimmerCornerRadius: Dp = 4.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }