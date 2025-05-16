package com.example.androiddevelopment2.recipe_details.screen

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.recipe_details.state.DetailsErrorEvent

@Composable
fun ErrorDialog(
    errorEvent: DetailsErrorEvent.Error,
    onDismiss: () -> Unit
) {
    val (titleRes, messageRes) = when (errorEvent.reason) {
        DetailsErrorEvent.FailureReason.Unauthorized ->
            R.string.error_title_auth to R.string.error_unauthorized
        DetailsErrorEvent.FailureReason.Forbidden ->
            R.string.error_title_auth to R.string.error_forbidden
        DetailsErrorEvent.FailureReason.NotFound ->
            R.string.error_title_server to R.string.error_not_found
        DetailsErrorEvent.FailureReason.BadRequest ->
            R.string.error_title_validation to R.string.error_bad_request
        DetailsErrorEvent.FailureReason.Server ->
            R.string.error_title_server to R.string.error_server
        DetailsErrorEvent.FailureReason.Network ->
            R.string.error_title_network to R.string.error_network
        DetailsErrorEvent.FailureReason.Unknown ->
            R.string.error_title_server to R.string.error_unknown
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(titleRes)) },
        text = { Text(stringResource(messageRes)) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.OK))
            }
        }
    )
}