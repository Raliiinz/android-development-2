package com.example.androiddevelopment2.customview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddevelopment2.customview.PieChartView.Companion.NO_SELECTION
import com.example.androiddevelopment2.customview.databinding.FragmentPieChartBinding
import com.example.androiddevelopment2.customview.state.PieChartEvent
import com.example.androiddevelopment2.customview.state.PieChartState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PieChartFragment : Fragment(R.layout.fragment_pie_chart) {
    private val viewBinding: FragmentPieChartBinding by viewBinding(FragmentPieChartBinding::bind)

    private val viewModel: PieChartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPieChart()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupPieChart() {
        viewBinding.pieChartView.apply {
            onSelectionChanged = { index ->
                if (index == NO_SELECTION) {
                    viewModel.reduce(PieChartEvent.OnClearSelection)
                } else {
                    viewModel.reduce(PieChartEvent.OnSectorSelected(index))
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickListeners() {
        viewBinding.apply {
            root.setOnClickListener {
                viewModel.reduce(PieChartEvent.OnClearSelection)
            }
            btn3Sectors.setOnClickListener {
                viewModel.reduce(PieChartEvent.OnSectorCountChanged(3))
            }
            btn4Sectors.setOnClickListener {
                viewModel.reduce(PieChartEvent.OnSectorCountChanged(4))
            }
            btn5Sectors.setOnClickListener {
                viewModel.reduce(PieChartEvent.OnSectorCountChanged(5))
            }
            btn6Sectors.setOnClickListener {
                viewModel.reduce(PieChartEvent.OnSectorCountChanged(6))
            }
            btnClearSelection.setOnClickListener { viewModel.reduce(PieChartEvent.OnClearSelection) }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is PieChartState.Initial -> {
                    }
                    is PieChartState.Success -> {
                        viewBinding.pieChartView.updateData(
                            count = state.sectorCount,
                            colors = state.colors,
                            selectedIndex = state.selectedSector
                        )
                    }
                }
            }
        }
    }
}
