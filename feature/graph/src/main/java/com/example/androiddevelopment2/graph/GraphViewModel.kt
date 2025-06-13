package com.example.androiddevelopment2.graph

import androidx.lifecycle.ViewModel
import com.example.androiddevelopment2.graph.state.GraphEvent
import com.example.androiddevelopment2.graph.state.GraphState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(GraphState())
    val state: StateFlow<GraphState> = _state.asStateFlow()

    fun onEvent(event: GraphEvent) {
        when (event) {
            is GraphEvent.PointsCountChanged -> handlePointsCountChanged(event.input)
            is GraphEvent.PointsValuesChanged -> handlePointsValuesChanged(event.input)
            GraphEvent.DrawGraph -> drawGraph()
        }
    }

    private fun handlePointsCountChanged(input: String) {
        _state.update { current ->
            current.copy(
                pointsCountInput = input,
                failureReason = null,
                uiState = current.uiState.copy(points = emptyList())
            )
        }
        validateInputs()
    }

    private fun handlePointsValuesChanged(input: String) {
        _state.update { current ->
            current.copy(
                pointsValuesInput = input,
                failureReason = null,
                uiState = current.uiState.copy(points = emptyList())
            )
        }
        validateInputs()
    }

    private fun drawGraph() {
        val points = _state.value.pointsValuesInput.split(",")
            .mapNotNull { it.trim().toFloatOrNull() }
            .filter { it >= 0 }

        _state.update { current ->
            current.copy(
                uiState = current.uiState.copy(points = points)
            )
        }
    }

    private fun validateInputs() {
        val state = _state.value
        val pointsCount = state.pointsCountInput.toIntOrNull()
        val pointsValues = state.pointsValuesInput.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        _state.update { current ->
            when {
                state.pointsCountInput.isEmpty() -> current.copy(
                    canDrawGraph = false,
                    failureReason = null
                )
                pointsCount == null -> current.copy(
                    canDrawGraph = false,
                    failureReason = GraphState.FailureReason.InvalidPointsCount
                )
                pointsCount <= 0 -> current.copy(
                    canDrawGraph = false,
                    failureReason = GraphState.FailureReason.InvalidPointsCount
                )
                state.pointsValuesInput.isEmpty() -> current.copy(
                    canDrawGraph = false,
                    failureReason = null
                )
                pointsValues.any { it.toFloatOrNull() == null } -> current.copy(
                    canDrawGraph = false,
                    failureReason = GraphState.FailureReason.InvalidPoints
                )
                pointsValues.any { it.toFloat() < 0 } -> current.copy(
                    canDrawGraph = false,
                    failureReason = GraphState.FailureReason.InvalidPoints
                )
                pointsValues.size != pointsCount -> current.copy(
                    canDrawGraph = false,
                    failureReason = GraphState.FailureReason.PointsCountMismatch
                )
                else -> current.copy(
                    canDrawGraph = true,
                    failureReason = null
                )
            }
        }
    }
}
