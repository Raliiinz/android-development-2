package com.example.androiddevelopment2.base.components.info

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.base.theme.Theme

@Composable
fun InfoRow(
    readyInMinutes: Int,
    servings: Int,
    spacing: Dp = Theme.spacing.medium,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InfoItem(
            iconRes = R.drawable.ic_time,
            text = stringResource(R.string.ready_in_minutes_format, readyInMinutes),
            textStyle = Theme.typography.meta,
            spacing = spacing
        )

        InfoItem(
            iconRes = R.drawable.ic_group,
            text = stringResource(R.string.servings_format, servings),
            textStyle = Theme.typography.meta
        )
    }
}