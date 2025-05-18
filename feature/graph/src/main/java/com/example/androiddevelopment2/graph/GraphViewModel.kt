package com.example.androiddevelopment2.graph

import androidx.lifecycle.ViewModel
import com.example.androiddevelopment2.graph.state.GraphEvent
import com.example.androiddevelopment2.graph.state.GraphState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(GraphState())
    val state: StateFlow<GraphState> = _state.asStateFlow()

    fun onEvent(event: GraphEvent) {
        when (event) {
            is GraphEvent.PointsCountChanged -> {
                val newState = _state.value.copy(
                    pointsCountInput = event.input,
                    pointsCountError = null,
                    showGraph = false
                )
                _state.update { newState }
                validateInputs(newState)
            }
            is GraphEvent.PointsValuesChanged -> {
                val newState = _state.value.copy(
                    pointsValuesInput = event.input,
                    pointsValuesError = null,
                    showGraph = false
                )
                _state.update { newState }
                validateInputs(newState)
            }
            GraphEvent.DrawGraph -> {
                val points = _state.value.pointsValuesInput.split(",")
                    .map { it.trim().toFloat() }
                _state.update {
                    it.copy(
                        showGraph = true,
                        graphPoints = points
                    )
                }
            }
        }
    }

    private fun validateInputs(state: GraphState) {
        val pointsCount = state.pointsCountInput.toIntOrNull()
        val pointsValues = state.pointsValuesInput.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        var pointsCountError: String? = null
        var pointsValuesError: String? = null
        var canDrawGraph = false

        if (state.pointsCountInput.isNotEmpty()) {
            if (pointsCount == null) {
                pointsCountError = "Введите целое число"
            } else if (pointsCount <= 0) {
                pointsCountError = "Количество точек должно быть положительным"
            }
        }

        if (state.pointsValuesInput.isNotEmpty()) {
            val invalidValues = pointsValues.any { it.toFloatOrNull() == null || it.toFloat() < 0 }
            if (invalidValues) {
                pointsValuesError = "Введите неотрицательные числа через запятую"
            } else if (pointsCount != null && pointsValues.size != pointsCount) {
                pointsValuesError = "Количество значений должно быть $pointsCount"
            }
        }

        if (pointsCount != null && pointsCount > 0 &&
            pointsValues.size == pointsCount &&
            pointsValues.all { it.toFloatOrNull() != null && it.toFloat() >= 0 }) {
            canDrawGraph = true
        }

        _state.update {
            it.copy(
                pointsCountError = pointsCountError,
                pointsValuesError = pointsValuesError,
                canDrawGraph = canDrawGraph
            )
        }
    }
}

//@HiltViewModel
//class GraphViewModel @Inject constructor() : ViewModel() {
//    private val _state = MutableStateFlow<GraphState>(GraphState.Initial)
//    val state: StateFlow<GraphState> = _state.asStateFlow()
//
//    private val _sideEffect = MutableSharedFlow<GraphSideEffect>()
//    val sideEffect: SharedFlow<GraphSideEffect> = _sideEffect.asSharedFlow()
//
//    init {
//        _state.value = GraphState.InputScreen()
//    }
//
//    fun onEvent(event: GraphEvent) {
//        when (event) {
//            is GraphEvent.PointsCountChanged -> {
//                _state.update { current ->
//                    (current as? GraphState.InputScreen)?.copy(
//                        pointsCount = event.value,
//                        pointsCountError = null
//                    ) ?: current
//                }
//            }
//
//            is GraphEvent.ValuesChanged -> {
//                _state.update { current ->
//                    (current as? GraphState.InputScreen)?.copy(
//                        values = event.value,
//                        valuesError = null
//                    ) ?: current
//                }
//            }
//
//            GraphEvent.DrawGraphClicked -> {
//                val current = _state.value as? GraphState.InputScreen ?: return
//
//                val pointsCount = current.pointsCount.toIntOrNull()
//                if (pointsCount == null || pointsCount <= 0) {
//                    _state.update { current ->
//                        when (current) {
//                            is GraphState.InputScreen -> current.copy(pointsCountError = "Invalid points count")
//                            else -> current
//                        }
//                    }
//                }
//
//                val values = current.values.split(",")
//                    .mapNotNull { it.trim().toFloatOrNull() }
//                    .filter { it >= 0 }
//
//                if (values.size != pointsCount) {
//                    _state.update { current ->
//                        when (current) {
//                            is GraphState.InputScreen -> current.copy(valuesError = "Values count must match points count")
//                            else -> current
//                        }
//                    }
//                }
//
//                _state.update { GraphState.GraphScreen(points = values) }
//            }
//
//            GraphEvent.BackClicked -> {
//                _state.update { GraphState.InputScreen() }
//            }
//        }
//    }
//}