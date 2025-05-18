package com.example.androiddevelopment2.graph.state

sealed class GraphEvent {
    data class PointsCountChanged(val input: String) : GraphEvent()
    data class PointsValuesChanged(val input: String) : GraphEvent()
    object DrawGraph : GraphEvent()
}