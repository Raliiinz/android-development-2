package com.example.androiddevelopment2.customview.state

sealed class PieChartEvent {
    data class OnSectorCountChanged(val count: Int) : PieChartEvent()
    data class OnSectorSelected(val index: Int) : PieChartEvent()
    object OnClearSelection : PieChartEvent()
}