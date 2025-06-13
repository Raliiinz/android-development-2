package com.example.androiddevelopment2.recipe_details.screen

import androidx.compose.runtime.Composable
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.base.components.dialogs.ErrorDialog
import com.example.androiddevelopment2.recipe_details.state.DetailsErrorEvent

object ErrorDialogFactory {
    @Composable
    fun Create(
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

        ErrorDialog(
            titleRes = titleRes,
            messageRes = messageRes,
            onDismiss = onDismiss
        )
    }
}