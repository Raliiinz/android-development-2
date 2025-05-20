package com.example.androiddevelopment2.graph.state

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class GraphUiState(
    val points: List<Float> = emptyList(),
    val lineColor: Color = Color.Blue,
    val gradientColors: List<Color> = listOf(
        Color.Blue.copy(alpha = 0.5f),
        Color.Blue.copy(alpha = 0f)
    )
)