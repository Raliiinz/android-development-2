package com.example.androiddevelopment2.customview

import androidx.lifecycle.ViewModel
import com.example.androiddevelopment2.customview.state.PieChartEvent
import com.example.androiddevelopment2.customview.state.PieChartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.core.graphics.toColorInt

@HiltViewModel
class PieChartViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<PieChartState>(PieChartState.Initial)
    val state: StateFlow<PieChartState> = _state.asStateFlow()

    private val availableColors = listOf(
        "#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA726",
        "#66BB6A", "#AB47BC", "#E91E63", "#9C27B0"
    ).map { it.toColorInt() }

    init {
        generateInitialData()
    }

    fun reduce(event: PieChartEvent) {
        when (event) {
            is PieChartEvent.OnSectorCountChanged -> generateSectorData(event.count)
            is PieChartEvent.OnSectorSelected -> updateSelection(event.index)
            PieChartEvent.OnClearSelection -> clearSelection()
        }
    }

    private fun generateInitialData() {
        generateSectorData(4)
    }

    private fun generateSectorData(count: Int) {
        val colors = generateDistinctColors(count)
        _state.value = PieChartState.Success(count, colors)
    }

    private fun generateDistinctColors(count: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val shuffledColors = availableColors.shuffled()

        for (i in 0 until count) {
            var color = shuffledColors[i % shuffledColors.size]
            if (colors.isNotEmpty() && color == colors.last()) {
                color = shuffledColors.first { it != colors.last() }
            }
            colors.add(color)
        }

        if (colors.size > 1 && colors.first() == colors.last()) {
            colors[colors.lastIndex] = shuffledColors.first { it != colors.first() }
        }

        return colors
    }

    private fun updateSelection(index: Int) {
        val currentState = _state.value as? PieChartState.Success ?: return
        _state.value = currentState.copy(selectedSector = index)
    }

    private fun clearSelection() {
        val currentState = _state.value as? PieChartState.Success ?: return
        _state.value = currentState.copy(selectedSector = -1)
    }
}
