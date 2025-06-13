package com.example.androiddevelopment2.customview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddevelopment2.customview.databinding.FragmentPieChartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PieChartFragment : Fragment(R.layout.fragment_pie_chart) {
    private val viewBinding: FragmentPieChartBinding by viewBinding(FragmentPieChartBinding::bind)


    private lateinit var pieChartView: PieChartView
    private lateinit var statusText: TextView
    private lateinit var btn3Sectors: Button
    private lateinit var btn4Sectors: Button
    private lateinit var btn5Sectors: Button
    private lateinit var btn6Sectors: Button
    private lateinit var btnClearSelection: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupClickListeners()
        setupPieChart()

//        val colors = listOf(
//            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
//            Color.MAGENTA, Color.CYAN, Color.GRAY, Color.DKGRAY
//        )
//
//        viewBinding.pieChartView.apply {
//            setSectorCount(5)
//            setSectorColors(colors)
//        }
    }

    private fun initViews() {

        pieChartView = viewBinding.pieChartView
        statusText = viewBinding.statusText
        btn3Sectors = viewBinding.btn3Sectors
        btn4Sectors = viewBinding.btn4Sectors
        btn5Sectors = viewBinding.btn5Sectors
        btn6Sectors = viewBinding.btn6Sectors
        btnClearSelection = viewBinding.btnClearSelection
    }

    private fun setupClickListeners() {
        btn3Sectors.setOnClickListener {
            setupSectors(3)
        }

        btn4Sectors.setOnClickListener {
            setupSectors(4)
        }

        btn5Sectors.setOnClickListener {
            setupSectors(5)
        }

        btn6Sectors.setOnClickListener {
            setupSectors(6)
        }

        btnClearSelection.setOnClickListener {
            pieChartView.clearSelection()
            updateStatusText()
        }
    }

    private fun setupPieChart() {
        // Set initial configuration
        pieChartView.setSectorCount(4)

        // Set custom colors
        val colors = listOf(
            Color.parseColor("#FF6B6B"), // Red
            Color.parseColor("#4ECDC4"), // Teal
            Color.parseColor("#45B7D1"), // Blue
            Color.parseColor("#FFA726")  // Orange
        )
        pieChartView.setSectorColors(colors)

        // Listen for selection changes
        setupSelectionListener()
        updateStatusText()
    }

    private fun setupSelectionListener() {
        // Since we don't have a built-in selection listener, we'll check periodically
        // In a real implementation, you might want to add a proper callback mechanism
        pieChartView.setOnTouchListener { _, _ ->
            // Post delayed to ensure the touch handling is complete
            pieChartView.post {
                updateStatusText()
            }
            false // Let the view handle the touch event
        }
    }

    private fun setupSectors(count: Int) {
        pieChartView.setSectorCount(count)

        val colors = when (count) {
            3 -> listOf(
                Color.parseColor("#FF6B6B"), // Red
                Color.parseColor("#4ECDC4"), // Teal
                Color.parseColor("#FFA726")  // Orange
            )
            4 -> listOf(
                Color.parseColor("#FF6B6B"), // Red
                Color.parseColor("#4ECDC4"), // Teal
                Color.parseColor("#45B7D1"), // Blue
                Color.parseColor("#FFA726")  // Orange
            )
            5 -> listOf(
                Color.parseColor("#FF6B6B"), // Red
                Color.parseColor("#4ECDC4"), // Teal
                Color.parseColor("#45B7D1"), // Blue
                Color.parseColor("#FFA726"), // Orange
                Color.parseColor("#66BB6A")  // Green
            )
            6 -> listOf(
                Color.parseColor("#FF6B6B"), // Red
                Color.parseColor("#4ECDC4"), // Teal
                Color.parseColor("#45B7D1"), // Blue
                Color.parseColor("#FFA726"), // Orange
                Color.parseColor("#66BB6A"), // Green
                Color.parseColor("#AB47BC")  // Purple
            )
            else -> listOf(Color.GRAY)
        }

        pieChartView.setSectorColors(colors)
        updateStatusText()

        Toast.makeText(requireContext(), "Switched to $count sectors", Toast.LENGTH_SHORT).show()
    }

    private fun updateStatusText() {
        val selectedSector = pieChartView.getSelectedSector()
        statusText.text = if (selectedSector >= 0) {
            "Selected sector: ${selectedSector + 1}"
        } else {
            "No sector selected"
        }
    }
}