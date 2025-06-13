package com.example.androiddevelopment2.base.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

object GraphTheme {

    val axisColor = Color.Black
    val gridColor = Color.LightGray.copy(alpha = 0.5f)
    val hoverTextBackground = Color.White.copy(alpha = 0.8f)
    val defaultPointColor = Color.Gray.copy(alpha = 0.7f)

    val axisStrokeWidth = 2f
    val gridStrokeWidth = 1f
    val graphLineWidth = 3f
    val pointRadius = 15f

    val axisTextStyle = TextStyle(color = Color.Black, fontSize = 10.sp)
    val hoverTextStyle = TextStyle(
        color = Color.Black,
        fontSize = 12.sp,
        background = hoverTextBackground
    )

    const val yGridLinesCount = 5
    const val maxXGridLines = 10
    const val hoverDistanceThreshold = 50f
    const val hoverTextOffset = 10f
    const val textPadding = 5f
}