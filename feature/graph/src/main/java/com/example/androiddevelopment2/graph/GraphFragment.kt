package com.example.androiddevelopment2.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androiddevelopment2.base.theme.AppTheme
import com.example.androiddevelopment2.base.theme.Theme
import com.example.androiddevelopment2.graph.state.GraphEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraphFragment : Fragment() {
    private val viewModel: GraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        GraphScreen(viewModel)
                    }
                }
            }
        }
    }
}

//@Composable
//fun GraphScreen(viewModel: GraphViewModel) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(
//                top = Theme.spacing.extraLarge + Theme.spacing.small,
//                start = Theme.spacing.medium,
//                end = Theme.spacing.medium,
//                bottom = Theme.spacing.medium
//            ),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Поле ввода количества точек
//        OutlinedTextField(
//            value = state.pointsCountInput,
//            onValueChange = { viewModel.onEvent(GraphEvent.PointsCountChanged(it)) },
//            label = { Text(stringResource(R.string.number_of_points)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(),
//            isError = state.pointsCountError != null
//        )
//
//        if (state.pointsCountError != null) {
//            Text(
//                text = state.pointsCountError!!,
//                color = MaterialTheme.colorScheme.error,
//                modifier = Modifier.align(Alignment.Start)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(Theme.spacing.small))
//
//        // Поле ввода значений точек
//        OutlinedTextField(
//            value = state.pointsValuesInput,
//            onValueChange = { viewModel.onEvent(GraphEvent.PointsValuesChanged(it)) },
//            label = { Text(stringResource(R.string.values_of_dots_separated_by_commas)) },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(),
//            isError = state.pointsValuesError != null
//        )
//
//        if (state.pointsValuesError != null) {
//            Text(
//                text = state.pointsValuesError!!,
//                color = MaterialTheme.colorScheme.error,
//                modifier = Modifier.align(Alignment.Start)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(Theme.spacing.medium))
//
//        // Кнопка построения графика
//        Button(
//            onClick = { viewModel.onEvent(GraphEvent.DrawGraph) },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = state.canDrawGraph
//        ) {
//            Text(stringResource(R.string.draw_graph))
//        }
//
//        Spacer(modifier = Modifier.height(Theme.spacing.medium))
//
//        // Область для графика
//        if (state.showGraph) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(Theme.spacing.graphHeight)
//                    .background(Color.White)
//                    .padding(Theme.spacing.small)
//            ) {
//                GraphPlotter(
//                    points = state.graphPoints,
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun GraphPlotter(
//    points: List<Float>,
//    modifier: Modifier = Modifier,
//    lineColor: Color = Color.Blue,
//    gradientColors: List<Color> = listOf(Color.Blue.copy(alpha = 0.3f), Color.Blue.copy(alpha = 0f))
//) {
//    Canvas(modifier = modifier) {
//        if (points.isEmpty()) return@Canvas
//
//        val width = size.width
//        val height = size.height
//        val maxValue = points.maxOrNull() ?: 0f
//        val minValue = points.minOrNull() ?: 0f
//        val valueRange = maxValue - minValue
//
//        // Рассчитываем координаты точек
//        val xStep = width / (points.size - 1)
//        val coordinates = points.mapIndexed { index, value ->
//            val x = index * xStep
//            val y = if (valueRange == 0f) {
//                height / 2f
//            } else {
//                height - ((value - minValue) / valueRange) * height
//            }
//            Offset(x, y)
//        }
//
//        // Рисуем оси
//        drawLine(
//            color = Color.Black,
//            start = Offset(0f, height),
//            end = Offset(width, height),
//            strokeWidth = 2f
//        )
//
//        drawLine(
//            color = Color.Black,
//            start = Offset(0f, 0f),
//            end = Offset(0f, height),
//            strokeWidth = 2f
//        )
//
//        // Рисуем область под графиком с градиентом
//        val path = Path().apply {
//            moveTo(0f, height)
//            coordinates.forEach { point ->
//                lineTo(point.x, point.y)
//            }
//            lineTo(width, height)
//            close()
//        }
//
//        drawPath(
//            path = path,
//            brush = Brush.verticalGradient(
//                colors = gradientColors,
//                startY = coordinates.minByOrNull { it.y }?.y ?: 0f,
//                endY = height
//            )
//        )
//
//        // Рисуем линию графика
//        if (coordinates.size > 1) {
//            for (i in 0 until coordinates.size - 1) {
//                drawLine(
//                    color = lineColor,
//                    start = coordinates[i],
//                    end = coordinates[i + 1],
//                    strokeWidth = 3f
//                )
//            }
//        }
//
//        // Рисуем точки
//        coordinates.forEach { point ->
//            drawCircle(
//                color = lineColor,
//                radius = 5f,
//                center = point
//            )
//        }
//    }
//}
//
