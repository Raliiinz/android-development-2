package com.example.androiddevelopment2.graph.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import com.example.androiddevelopment2.base.theme.GraphTheme
import com.example.androiddevelopment2.graph.state.GraphUiState

@Composable
fun GraphCanvas(
    uiState: GraphUiState,
    modifier: Modifier = Modifier,
) {
    val (points, lineColor, gradientColors) = remember(uiState) {
        Triple(uiState.points, uiState.lineColor, uiState.gradientColors)
    }

    var hoveredPointIndex by remember { mutableStateOf<Int?>(null) }
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    hoveredPointIndex = findNearestPointIndex(offset, points, size, GraphTheme.hoverDistanceThreshold)
                    val wasConsumed = tryAwaitRelease()
                    if (!wasConsumed) {
                        hoveredPointIndex = null
                    }
                }
            )
        }
    ) {
        if (points.isEmpty()) return@Canvas

        drawGraph(
            points = points,
            lineColor = lineColor,
            gradientColors = gradientColors,
            hoveredPointIndex = hoveredPointIndex,
            textMeasurer = textMeasurer
        )
    }
}

private fun findNearestPointIndex(
    offset: Offset,
    points: List<Float>,
    size: IntSize,
    hoverThreshold: Float = GraphTheme.hoverDistanceThreshold
): Int? {
    if (points.isEmpty()) return null

    val width = size.width.toFloat()
    val height = size.height.toFloat()
    val maxValue = points.maxOrNull() ?: 0f
    val minValue = 0f
    val valueRange = maxValue

    val xStep = width / (points.size - 1)

    return points.mapIndexed { index, value ->
        val x = index * xStep
        val y = if (valueRange == 0f) {
            height
        } else {
            height - ((value - minValue) / valueRange) * height
        }
        val pointOffset = Offset(x, y)
        index to pointOffset
    }.minByOrNull { (_, pointOffset) ->
        (offset - pointOffset).getDistance()
    }?.takeIf { (_, pointOffset) ->
        (offset - pointOffset).getDistance() < hoverThreshold
    }?.first
}


private fun DrawScope.drawGraph(
    points: List<Float>,
    lineColor: Color,
    gradientColors: List<Color>,
    hoveredPointIndex: Int?,
    textMeasurer: TextMeasurer
) {
    val width = size.width
    val height = size.height
    val maxValue = points.maxOrNull() ?: 0f
    val minValue = 0f
    val valueRange = maxValue

    val xStep = width / (points.size - 1)
    val coordinates = points.mapIndexed { index, value ->
        val x = index * xStep
        val y = if (valueRange == 0f) {
            height
        } else {
            height - ((value - minValue) / valueRange) * height
        }
        Offset(x, y)
    }

    drawGrid(width, height, maxValue, coordinates, textMeasurer)

    drawAxes(width, height)

    drawFilledArea(coordinates, width, height, gradientColors)

    drawGraphLine(coordinates, lineColor)

    drawPointsAndHints(points, coordinates, lineColor, hoveredPointIndex, textMeasurer)
}

private fun DrawScope.drawGrid(
    width: Float,
    height: Float,
    maxValue: Float,
    coordinates: List<Offset>,
    textMeasurer: TextMeasurer
) {
    for (i in 0..GraphTheme.yGridLinesCount) {
        val yPos = height - (height / GraphTheme.yGridLinesCount) * i
        drawLine(
            color = GraphTheme.gridColor,
            start = Offset(0f, yPos),
            end = Offset(width, yPos),
            strokeWidth = GraphTheme.gridStrokeWidth
        )

        if (maxValue > 0) {
            val value = (maxValue / GraphTheme.yGridLinesCount) * i
            val text = "%.1f".format(value)
            val textLayout = textMeasurer.measure(text, GraphTheme.axisTextStyle)
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    -textLayout.size.width - GraphTheme.textPadding,
                    yPos - textLayout.size.height / 2
                )
            )
        }
    }

    val xGridLines = minOf(GraphTheme.maxXGridLines, coordinates.size)
    for (i in 0 until xGridLines) {
        val xPos = (width / (xGridLines - 1)) * i
        drawLine(
            color = GraphTheme.gridColor,
            start = Offset(xPos, 0f),
            end = Offset(xPos, height),
            strokeWidth = GraphTheme.gridStrokeWidth
        )

        if (coordinates.size > 1) {
            val text = "${i + 1}"
            val textLayout = textMeasurer.measure(text, GraphTheme.axisTextStyle)
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    xPos - textLayout.size.width / 2,
                    height + GraphTheme.textPadding
                )
            )
        }
    }
}

private fun DrawScope.drawAxes(width: Float, height: Float) {
    drawLine(
        color = GraphTheme.axisColor,
        start = Offset(0f, height),
        end = Offset(width, height),
        strokeWidth = GraphTheme.axisStrokeWidth
    )

    drawLine(
        color = GraphTheme.axisColor,
        start = Offset(0f, 0f),
        end = Offset(0f, height),
        strokeWidth = GraphTheme.axisStrokeWidth
    )
}

private fun DrawScope.drawFilledArea(
    coordinates: List<Offset>,
    width: Float,
    height: Float,
    gradientColors: List<Color>
) {
    val path = Path().apply {
        moveTo(0f, height)
        coordinates.forEach { point -> lineTo(point.x, point.y) }
        lineTo(width, height)
        close()
    }

    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = gradientColors,
            startY = coordinates.minByOrNull { it.y }?.y ?: 0f,
            endY = height
        )
    )
}

private fun DrawScope.drawGraphLine(
    coordinates: List<Offset>,
    lineColor: Color
) {
    if (coordinates.size > 1) {
        for (i in 0 until coordinates.size - 1) {
            drawLine(
                color = lineColor,
                start = coordinates[i],
                end = coordinates[i + 1],
                strokeWidth = GraphTheme.graphLineWidth
            )
        }
    }
}

private fun DrawScope.drawPointsAndHints(
    points: List<Float>,
    coordinates: List<Offset>,
    lineColor: Color,
    hoveredPointIndex: Int?,
    textMeasurer: TextMeasurer
) {
    coordinates.forEachIndexed { index, point ->
        val isHovered = index == hoveredPointIndex
        drawCircle(
            color = if (isHovered) lineColor else GraphTheme.defaultPointColor,
            radius = GraphTheme.pointRadius,
            center = point
        )

        if (isHovered) {
            val text = "(${index + 1}, ${points[index]})"
            val textLayout = textMeasurer.measure(text, GraphTheme.hoverTextStyle)
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    x = point.x.coerceIn(
                        minimumValue = 0f,
                        maximumValue = size.width - textLayout.size.width
                    ),
                    y = point.y - textLayout.size.height - GraphTheme.hoverTextOffset
                )
            )
        }
    }
}
