package com.example.androiddevelopment2.customview.state

sealed class PieChartState {
    object Initial : PieChartState()
    data class Success(
        val sectorCount: Int,
        val colors: List<Int>,
        val selectedSector: Int = -1
    ) : PieChartState()
}