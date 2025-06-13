package com.example.androiddevelopment2.base.components.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Composable
fun TextSection(
    title: String,
    content: String,
    titleStyle: TextStyle,
    contentStyle: TextStyle,
    spacing: Dp,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = titleStyle
        )

        Spacer(modifier = Modifier.height(spacing))

        Text(
            text = content,
            style = contentStyle
        )
    }
}