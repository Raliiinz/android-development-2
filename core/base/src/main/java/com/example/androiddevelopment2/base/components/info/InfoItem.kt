package com.example.androiddevelopment2.base.components.info

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androiddevelopment2.base.theme.Theme

@Composable
fun InfoItem(
    @DrawableRes iconRes: Int,
    text: String,
    textStyle: TextStyle,
    iconSize: Dp = 24.dp,
    iconTint: Color = Theme.colors.onBackground,
    spacing: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(spacing))
        Text(
            text = text,
            style = textStyle
        )
    }
}