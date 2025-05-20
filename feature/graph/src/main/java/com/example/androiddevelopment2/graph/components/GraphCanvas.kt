package com.example.androiddevelopment2.graph.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
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
                    hoveredPointIndex = findNearestPointIndex(offset, points, size, hoverDistanceThreshold)
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
    hoverThreshold: Float = hoverDistanceThreshold
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

    // Рисуем оси
    drawAxes(width, height)

    // Рисуем заполненную область
    drawFilledArea(coordinates, width, height, gradientColors)

    // Рисуем линию графика
    drawGraphLine(coordinates, lineColor)

    // Рисуем точки и подсказки
    drawPointsAndHints(points, coordinates, lineColor, hoveredPointIndex, textMeasurer)
}



//    val gridColor = Color.LightGray.copy(alpha = 0.5f)
//    val gridStrokeWidth = 1f
//
//    val yGridLines = 5
//    for (i in 0..yGridLines) {
//        val yPos = height - (height / yGridLines) * i
//        drawLine(
//            color = gridColor,
//            start = Offset(0f, yPos),
//            end = Offset(width, yPos),
//            strokeWidth = gridStrokeWidth
//        )
//
//        if (valueRange > 0) {
//            val value = minValue + (valueRange / yGridLines) * i
//            val text = "%.1f".format(value)
//            val textLayout = textMeasurer.measure(
//                text = text,
//                style = TextStyle(
//                    color = Color.Black,
//                    fontSize = 10.sp
//                )
//            )
//            drawText(
//                textLayoutResult = textLayout,
//                topLeft = Offset(-textLayout.size.width - 5f, yPos - textLayout.size.height / 2)
//            )
//        }
//    }
//
//    val xGridLines = minOf(10, points.size)
//    for (i in 0 until xGridLines) {
//        val xPos = (width / (xGridLines - 1)) * i
//        drawLine(
//            color = gridColor,
//            start = Offset(xPos, 0f),
//            end = Offset(xPos, height),
//            strokeWidth = gridStrokeWidth
//        )
//
//        if (points.size > 1) {
//            val text = "${i + 1}"
//            val textLayout = textMeasurer.measure(
//                text = text,
//                style = TextStyle(
//                    color = Color.Black,
//                    fontSize = 10.sp
//                )
//            )
//            drawText(
//                textLayoutResult = textLayout,
//                topLeft = Offset(xPos - textLayout.size.width / 2, height + 5f)
//            )
//        }
//    }
//
//    drawLine(
//        color = Color.Black,
//        start = Offset(0f, height),
//        end = Offset(width, height),
//        strokeWidth = 2f
//    )
//
//    drawLine(
//        color = Color.Black,
//        start = Offset(0f, 0f),
//        end = Offset(0f, height),
//        strokeWidth = 2f
//    )
//
//    val path = Path().apply {
//        moveTo(0f, height)
//        coordinates.forEach { point -> lineTo(point.x, point.y) }
//        lineTo(width, height)
//        close()
//    }
//
//    drawPath(
//        path = path,
//        brush = Brush.verticalGradient(
//            colors = gradientColors,
//            startY = coordinates.minByOrNull { it.y }?.y ?: 0f,
//            endY = height
//        )
//    )
//
//    if (coordinates.size > 1) {
//        for (i in 0 until coordinates.size - 1) {
//            drawLine(
//                color = lineColor,
//                start = coordinates[i],
//                end = coordinates[i + 1],
//                strokeWidth = 3f
//            )
//        }
//    }
//
//    coordinates.forEachIndexed { index, point ->
//        val isHovered = index == hoveredPointIndex
//        val pointColor = if (isHovered) lineColor else Color.Gray.copy(alpha = 0.7f)
//        val pointRadius = 15f
//
//        drawCircle(
//            color = pointColor,
//            radius = pointRadius,
//            center = point
//        )
//
//        if (isHovered) {
//            val text = "(${index + 1}, ${points[index]})"
//            val textLayoutResult = textMeasurer.measure(
//                text = text,
//                style = TextStyle(
//                    color = Color.Black,
//                    fontSize = 12.sp,
//                    background = Color.White.copy(alpha = 0.8f)
//                )
//            )
//
//            // Position the text above the point
//            val textOffset = Offset(
//                x = point.x.coerceIn(
//                    minimumValue = 0f,
//                    maximumValue = width - textLayoutResult.size.width
//                ),
//                y = point.y - textLayoutResult.size.height - 10f
//            )
//
//            drawText(
//                textLayoutResult = textLayoutResult,
//                topLeft = textOffset
//            )
//        }
//    }
//}


private fun DrawScope.drawGrid(
    width: Float,
    height: Float,
    maxValue: Float,
    coordinates: List<Offset>,
    textMeasurer: TextMeasurer
) {
    // Горизонтальные линии
    for (i in 0..yGridLinesCount) {
        val yPos = height - (height / yGridLinesCount) * i
        drawLine(
            color = gridColor,
            start = Offset(0f, yPos),
            end = Offset(width, yPos),
            strokeWidth = gridStrokeWidth
        )

        if (maxValue > 0) {
            val value = (maxValue / yGridLinesCount) * i
            val text = "%.1f".format(value)
            val textLayout = textMeasurer.measure(text, axisTextStyle)
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    -textLayout.size.width - textPadding,
                    yPos - textLayout.size.height / 2
                )
            )
        }
    }

    // Вертикальные линии
    val xGridLines = minOf(maxXGridLines, coordinates.size)
    for (i in 0 until xGridLines) {
        val xPos = (width / (xGridLines - 1)) * i
        drawLine(
            color = gridColor,
            start = Offset(xPos, 0f),
            end = Offset(xPos, height),
            strokeWidth = gridStrokeWidth
        )

        if (coordinates.size > 1) {
            val text = "${i + 1}"
            val textLayout = textMeasurer.measure(text, axisTextStyle)
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    xPos - textLayout.size.width / 2,
                    height + textPadding
                )
            )
        }
    }
}

private fun DrawScope.drawAxes(width: Float, height: Float) {
    drawLine(
        color = axisColor,
        start = Offset(0f, height),
        end = Offset(width, height),
        strokeWidth = axisStrokeWidth
    )

    drawLine(
        color = axisColor,
        start = Offset(0f, 0f),
        end = Offset(0f, height),
        strokeWidth = axisStrokeWidth
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
                strokeWidth = graphLineWidth
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
            color = if (isHovered) lineColor else defaultPointColor,
            radius = pointRadius,
            center = point
        )

        if (isHovered) {
            val text = "(${index + 1}, ${points[index]})"
            val textLayout = textMeasurer.measure(text, hoverTextStyle)
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    x = point.x.coerceIn(
                        minimumValue = 0f,
                        maximumValue = size.width - textLayout.size.width
                    ),
                    y = point.y - textLayout.size.height - hoverTextOffset
                )
            )
        }
    }
}

val axisColor = Color.Black
val gridColor = Color.LightGray.copy(alpha = 0.5f)
val hoverTextBackground = Color.White.copy(alpha = 0.8f)
val defaultPointColor = Color.Gray.copy(alpha = 0.7f)

// Sizes
val axisStrokeWidth = 2f
val gridStrokeWidth = 1f
val graphLineWidth = 3f
val pointRadius = 15f

// Text
val axisTextStyle = TextStyle(color = Color.Black, fontSize = 10.sp)
val hoverTextStyle = TextStyle(
    color = Color.Black,
    fontSize = 12.sp,
    background = hoverTextBackground
)

const val yGridLinesCount = 5
const val maxXGridLines = 20
const val hoverDistanceThreshold = 50f
const val hoverTextOffset = 10f
const val textPadding = 5f
//data class GraphParams(
//    val color: Color,
//    val axesColor: Color,
//    val axesLineWidth: Dp,
//    val graphLineWidth: Dp,
//    val widthAxesCount: Int,
//    val heightAxesCount: Int,
//) {
//    companion object {
//        @Composable
//        fun default(): GraphParams {
//            return GraphParams(
//                color = TTheme.colorScheme.primary,
//                axesColor = TTheme.colorScheme.outline,
//                axesLineWidth = AxesLineWidth,
//                widthAxesCount = WidthAxesCount,
//                heightAxesCount = HeightAxesCount,
//                graphLineWidth = GraphLineWidth
//            )
//        }
//    }
//}


