package com.example.androiddevelopment2.graph.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.base.theme.Theme
import com.example.androiddevelopment2.graph.state.GraphEvent
import com.example.androiddevelopment2.graph.state.GraphState

@Composable
fun GraphInputs(
    state: GraphState,
    onEvent: (GraphEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.small)
    ) {
        PointsCountInput(
            value = state.pointsCountInput,
            isError = state.failureReason is GraphState.FailureReason.InvalidPointsCount,
            onValueChange = { onEvent(GraphEvent.PointsCountChanged(it)) },
            errorMessage = state.failureReason
        )

        PointsValuesInput(
            value = state.pointsValuesInput,
            isError = state.failureReason is GraphState.FailureReason.InvalidPoints
                    || state.failureReason is GraphState.FailureReason.PointsCountMismatch,
            onValueChange = { onEvent(GraphEvent.PointsValuesChanged(it)) },
            pointsCount = state.pointsCountInput,
            errorMessage = state.failureReason
        )

        Button(
            onClick = { onEvent(GraphEvent.DrawGraph) },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canDrawGraph
        ) {
            Text(stringResource(R.string.draw_graph))
        }
    }
}

@Composable
private fun PointsCountInput(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    errorMessage: GraphState.FailureReason?,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.number_of_points)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )

        if (isError && errorMessage == GraphState.FailureReason.InvalidPointsCount) {
            Text(
                text = stringResource(R.string.error_invalid_points_count),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}

@Composable
private fun PointsValuesInput(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    pointsCount: String,
    errorMessage: GraphState.FailureReason?,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.values_of_dots_separated_by_commas)) },
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage == GraphState.FailureReason.InvalidPoints) {
            Text(
                text = stringResource(R.string.error_invalid_points),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        if (errorMessage == GraphState.FailureReason.PointsCountMismatch) {
            Text(
                text = stringResource(
                    R.string.error_points_mismatch,
                    pointsCount.toIntOrNull() ?: 0
                ),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}