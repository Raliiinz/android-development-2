package com.example.androiddevelopment2.base.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

object Theme {
    val spacing: Spacing
        @Composable
        get() = LocalSpacing.current

    val shape: Shape
        @Composable
        get() = LocalShape.current

    val typography: Typography
        @Composable
        get() = LocalTypography.current

    val colors: ColorScheme
        @Composable
        get() = LocalColorScheme.current
}