package com.example.androiddevelopment2.base.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class Sizes(
    val axisStrokeWidth: Float = 2f,
    val gridStrokeWidth: Float = 1f,
    val graphLineWidth: Float = 3f,
    val pointRadius: Float = 15f,
)

val LocalSizes = staticCompositionLocalOf { Sizes() }