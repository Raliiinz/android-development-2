package com.example.androiddevelopment2.graph.state

data class GraphState(
    val pointsCountInput: String = "",
    val pointsValuesInput: String = "",
    val pointsCountError: String? = null,
    val pointsValuesError: String? = null,
    val showGraph: Boolean = false,
    val graphPoints: List<Float> = emptyList(),
    val canDrawGraph: Boolean = false
)
