package com.example.androiddevelopment2.graph.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class GraphState(
    val pointsCountInput: String = "",
    val pointsValuesInput: String = "",
    val canDrawGraph: Boolean = false,
    val uiState: GraphUiState = GraphUiState(),
    val failureReason: FailureReason? = null
) {
    val showGraph: Boolean
        @Composable get() = uiState.points.isNotEmpty()

    sealed interface FailureReason {
        data object InvalidPointsCount : FailureReason
        data object InvalidPoints : FailureReason
        data object PointsCountMismatch : FailureReason
    }
}