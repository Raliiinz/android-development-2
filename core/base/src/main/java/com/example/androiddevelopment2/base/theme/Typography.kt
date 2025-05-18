package com.example.androiddevelopment2.base.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Typography(
    val title: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    val sectionTitle: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    val meta: TextStyle = TextStyle(
        fontSize = 16.sp
    ),
    val body: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    val shimmerTitleHeight: Dp = 32.dp,
    val shimmerMetaHeight: Dp = 24.dp,
    val shimmerSectionTitleHeight: Dp = 28.dp,
    val shimmerSectionContentHeight: Dp = 250.dp
)

val LocalTypography = staticCompositionLocalOf { Typography() }