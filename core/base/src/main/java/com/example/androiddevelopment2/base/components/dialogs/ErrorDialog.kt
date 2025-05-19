package com.example.androiddevelopment2.base.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.base.theme.Theme

@Composable
fun ErrorDialog(
    titleRes: Int,
    messageRes: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Theme.colors.surface,
        title = {
            Text(
                text = stringResource(titleRes),
                style = Theme.typography.title
            )
        },
        text = {
            Text(
                text = stringResource(messageRes),
                style = Theme.typography.body
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.OK),
                    style = Theme.typography.meta
                )
            }
        }
    )
}