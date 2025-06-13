package com.example.androiddevelopment2.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


//class PieChartView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : View(context, attrs, defStyleAttr) {
//
//    private var sectorCount = 3
//    private var sectorColors: List<Int> = listOf(Color.RED, Color.YELLOW, Color.BLUE)
//    private var selectedIndex: Int = -1
//    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//
//    private val path = Path()
//    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = Color.WHITE
//    }
//    private val sectorPath = Path()
//
//    init {
//        // Optional: parse attributes here if needed
//        context.theme.obtainStyledAttributes(
//            attrs,
//            R.styleable.CustomView,
//            0, 0
//        ).apply {
//            try {
//                // parse custom attributes if needed
//            } finally {
//                recycle()
//            }
//        }
//
//        setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                handleTouch(event.x, event.y)
//                return@setOnTouchListener true
//            }
//            false
//        }
//    }
//
////    override fun onDraw(canvas: Canvas) {
////        super.onDraw(canvas)
////
////        val centerX = width / 2f
////        val centerY = height / 2f
////        val outerRadius = min(centerX, centerY) * 0.9f
////        val thickness = outerRadius * 0.4f // делаем толще
////        val innerRadius = outerRadius - thickness
////
////        val outerRect = RectF(centerX - outerRadius, centerY - outerRadius, centerX + outerRadius, centerY + outerRadius)
////        val innerRect = RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius)
////
////        val sweepAngle = 360f / sectorCount
////
////        for (i in 0 until sectorCount) {
////            val path = Path()
////            val color = getSectorColor(i)
////            paint.color = color
////
////            val startAngle = 270f + i * sweepAngle
////
////            // Внешняя дуга
////            path.arcTo(outerRect, startAngle, sweepAngle)
////
////            // Закруглённый конец (внешний радиус)
////            val endAngleRad = Math.toRadians((startAngle + sweepAngle).toDouble())
////            val endX = centerX + outerRadius * cos(endAngleRad).toFloat()
////            val endY = centerY + outerRadius * sin(endAngleRad).toFloat()
////            path.addCircle(endX, endY, thickness / 2f, Path.Direction.CW)
////
////            // Внутренняя дуга (обратно)
////            path.arcTo(innerRect, startAngle + sweepAngle, -sweepAngle)
////
////            // Закруглённое начало
////            val startAngleRad = Math.toRadians(startAngle.toDouble())
////            val startX = centerX + outerRadius * cos(startAngleRad).toFloat()
////            val startY = centerY + outerRadius * sin(startAngleRad).toFloat()
////            path.addCircle(startX, startY, thickness / 2f, Path.Direction.CW)
////
////            path.close()
////
////            canvas.drawPath(path, paint)
////        }
////
////        // Текст в центре
////        paint.color = Color.BLACK
////        paint.textSize = outerRadius * 0.3f
////        paint.textAlign = Paint.Align.CENTER
////        canvas.drawText(sectorCount.toString(), centerX, centerY + paint.textSize / 3, paint)
////    }
//
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val centerX = width / 2f
//        val centerY = height / 2f
//        val radius = min(centerX, centerY) * 0.9f
//        val strokeWidth = radius * 0.5f
//        val innerRadius = radius - strokeWidth
//        val capRadius = strokeWidth / 2f
//
//        val outerRect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
//        val innerRect = RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius)
//
//        val sweepAngle = 360f / sectorCount
//        val overlapAngle = 5f // Угол перекрытия между секторами
//
//        for (i in 0 until sectorCount) {
//            val startAngle = 270f + i * sweepAngle
//            val endAngle = startAngle + sweepAngle
//
//            paint.color = getSectorColor(i)
//            path.reset()
//
//            // 1. Начинаем с острого конца (внутренний радиус)
//            val startInnerX = centerX + innerRadius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
//            val startInnerY = centerY + innerRadius * sin(Math.toRadians(startAngle.toDouble())).toFloat()
//            path.moveTo(startInnerX, startInnerY)
//
//            // 2. Линия к внешнему радиусу (острый край)
//            val startOuterX = centerX + radius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
//            val startOuterY = centerY + radius * sin(Math.toRadians(startAngle.toDouble())).toFloat()
//            path.lineTo(startOuterX, startOuterY)
//
//            // 3. Внешняя дуга (немного не доходя до конца для скругления)
//            path.arcTo(outerRect, startAngle, sweepAngle - overlapAngle, false)
//
//            // 4. Скругленный конец
//            val capAngle = endAngle - overlapAngle/2
//            val capCenterX = centerX + (radius - capRadius) * cos(Math.toRadians(capAngle.toDouble())).toFloat()
//            val capCenterY = centerY + (radius - capRadius) * sin(Math.toRadians(capAngle.toDouble())).toFloat()
//            path.arcTo(
//                RectF(
//                    capCenterX - capRadius,
//                    capCenterY - capRadius,
//                    capCenterX + capRadius,
//                    capCenterY + capRadius
//                ),
//                capAngle - 90,
//                180f,
//                false
//            )
//
//            // 5. Внутренняя дуга (обратно)
//            path.arcTo(innerRect, endAngle, -sweepAngle, false)
//
//            path.close()
//            canvas.drawPath(path, paint)
//        }
//
//        // Центральный круг
//        canvas.drawCircle(centerX, centerY, innerRadius * 0.8f, centerPaint)
//
//        // Текст с количеством секторов
//        paint.color = Color.BLACK
//        paint.textSize = radius / 3
//        paint.textAlign = Paint.Align.CENTER
//        canvas.drawText(sectorCount.toString(), centerX, centerY + paint.textSize / 3, paint)
//    }
//
//
//
////    override fun onDraw(canvas: Canvas) {
////        super.onDraw(canvas)
////
////        val centerX = width / 2f
////        val centerY = height / 2f
////        val radius = min(centerX, centerY) * 0.9f
////        val strokeWidth = radius * 0.5f
////        val innerRadius = radius - strokeWidth
////        val capRadius = strokeWidth / 2f
////
////        val sweepAngle = 360f / sectorCount
////
////        for (i in 0 until sectorCount) {
////            val startAngle = 270f + i * sweepAngle
////            val endAngle = startAngle + sweepAngle
////
////            paint.color = getSectorColor(i)
////
////            val path = Path()
////
////            // 1. Начало сектора (острое)
////            val startOuterX = centerX + radius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
////            val startOuterY = centerY + radius * sin(Math.toRadians(startAngle.toDouble())).toFloat()
////            path.moveTo(startOuterX, startOuterY)
////
////            // 2. Внешняя дуга
////            path.arcTo(
////                RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
////                startAngle,
////                sweepAngle - 15f, // чуть меньше полного угла, чтобы оставить место для скругления
////                false
////            )
////
////            // 3. Скруглённый конец (cap)
////            val capAngle = endAngle - 7.5f // центр скругления
////            val capCenterX = centerX + (innerRadius + capRadius) * cos(Math.toRadians(capAngle.toDouble())).toFloat()
////            val capCenterY = centerY + (innerRadius + capRadius) * sin(Math.toRadians(capAngle.toDouble())).toFloat()
////            path.arcTo(
////                RectF(
////                    capCenterX - capRadius,
////                    capCenterY - capRadius,
////                    capCenterX + capRadius,
////                    capCenterY + capRadius
////                ),
////                capAngle - 90f,
////                180f,
////                false
////            )
////
////            // 4. Внутренняя дуга (обратно)
////            path.arcTo(
////                RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius),
////                endAngle,
////                -(sweepAngle - 15f),
////                false
////            )
////
////            path.close()
////            canvas.drawPath(path, paint)
////        }
////
////        // Центр круга: число
////        paint.color = Color.BLACK
////        paint.textSize = radius / 3
////        paint.textAlign = Paint.Align.CENTER
////        canvas.drawText(sectorCount.toString(), centerX, centerY + paint.textSize / 3, paint)
////    }
//
//
////    override fun onDraw(canvas: Canvas) {
////        super.onDraw(canvas)
////
////        val centerX = width / 2f
////        val centerY = height / 2f
////        val radius = min(centerX, centerY) * 0.9f
////        val strokeWidth = radius * 0.5f
////        val innerRadius = radius - strokeWidth
////
////        val sweepAngle = 360f / sectorCount
////
////        for (i in 0 until sectorCount) {
////            val startAngle = 270f + i * sweepAngle
////            val endAngle = startAngle + sweepAngle
////
////            paint.color = getSectorColor(i)
////
////            val path = Path()
////
////            // Точка старта на внешнем радиусе
////            val startOuterX = centerX + radius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
////            val startOuterY = centerY + radius * sin(Math.toRadians(startAngle.toDouble())).toFloat()
////
////            // Точка конца на внешнем радиусе
////            val endOuterX = centerX + radius * cos(Math.toRadians(endAngle.toDouble())).toFloat()
////            val endOuterY = centerY + radius * sin(Math.toRadians(endAngle.toDouble())).toFloat()
////
////            // Точка конца на внутреннем радиусе
////            val endInnerX = centerX + innerRadius * cos(Math.toRadians(endAngle.toDouble())).toFloat()
////            val endInnerY = centerY + innerRadius * sin(Math.toRadians(endAngle.toDouble())).toFloat()
////
////            // Точка старта на внутреннем радиусе
////            val startInnerX = centerX + innerRadius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
////            val startInnerY = centerY + innerRadius * sin(Math.toRadians(startAngle.toDouble())).toFloat()
////
////            // 1. Дуга по внешнему радиусу
////            path.moveTo(startOuterX, startOuterY)
////            path.arcTo(
////                RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
////                startAngle,
////                sweepAngle,
////                false
////            )
////
////            // 2. Скругленный конец (дуга радиусом = половина толщины сектора)
////            val capRadius = (radius - innerRadius) / 2f
////            val capCenterX = centerX + (innerRadius + capRadius) * cos(Math.toRadians(endAngle.toDouble())).toFloat()
////            val capCenterY = centerY + (innerRadius + capRadius) * sin(Math.toRadians(endAngle.toDouble())).toFloat()
////            path.arcTo(
////                RectF(
////                    capCenterX - capRadius,
////                    capCenterY - capRadius,
////                    capCenterX + capRadius,
////                    capCenterY + capRadius
////                ),
////                endAngle - 90f,
////                180f,
////                false
////            )
////
////            // 3. Дуга по внутреннему радиусу (обратно)
////            path.arcTo(
////                RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius),
////                endAngle,
////                -sweepAngle,
////                false
////            )
////
////            // 4. Замыкаем путь
////            path.close()
////
////            canvas.drawPath(path, paint)
////        }
////
////        // Центр круга: число
////        paint.color = Color.BLACK
////        paint.textSize = radius / 3
////        paint.textAlign = Paint.Align.CENTER
////        canvas.drawText(sectorCount.toString(), centerX, centerY + paint.textSize / 3, paint)
////    }
//
//
////    override fun onDraw(canvas: Canvas) {
////        super.onDraw(canvas)
////
////        val centerX = width / 2f
////        val centerY = height / 2f
////        val radius = min(centerX, centerY) * 0.9f
////        val strokeWidth = radius * 0.5f
////        val innerRadius = radius - strokeWidth
////
////
////        val outerRect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
////        val innerRect = RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius)
////
////        val sweepAngle = 360f / sectorCount
////
////        for (i in 0 until sectorCount) {
////            val startAngle = 270f + i * sweepAngle
////            paint.color = getSectorColor(i)
////
////            val path = Path()
////
////            // Внешний круг
//////            path.arcTo(outerRect, startAngle, sweepAngle)
//////
//////            // Закругление на конце
//////            path.arcTo(innerRect, startAngle + sweepAngle, -sweepAngle)
//////            path.close()
//////
//////            canvas.drawPath(path, paint)
//////        }
////
////        // Центр круга: число
////        paint.color = Color.BLACK
////        paint.textSize = radius / 3
////        paint.textAlign = Paint.Align.CENTER
////        canvas.drawText(sectorCount.toString(), centerX, centerY + paint.textSize / 3, paint)
////    }
//
//
//
//    private fun getSectorColor(index: Int): Int {
//        val color = sectorColors[index % sectorColors.size]
//        return if (index == selectedIndex) lightenColor(color, 0.3f) else color
//    }
//
////    private fun handleTouch(x: Float, y: Float) {
////        val dx = x - width / 2f
////        val dy = y - height / 2f
////        val distance = sqrt(dx * dx + dy * dy)
////        val outerRadius = min(width, height) * 0.9f / 2f
////        val innerRadius = outerRadius - outerRadius * 0.4f
////
////        if (distance !in innerRadius..outerRadius) {
////            selectedIndex = -1
////        } else {
////            var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
////            angle = (angle + 360 + 90) % 360
////            selectedIndex = (angle / (360f / sectorCount)).toInt()
////        }
////
////        invalidate()
////    }
//
//    private fun handleTouch(x: Float, y: Float) {
//        val dx = x - width / 2f
//        val dy = y - height / 2f
//        val distance = sqrt(dx * dx + dy * dy)
//        val outerRadius = min(width, height) * 0.9f / 2f
//        val innerRadius = outerRadius * 0.5f
//
//        if (distance !in innerRadius..outerRadius) {
//            selectedIndex = -1
//        } else {
//            var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
//            angle = (angle + 360 + 90) % 360
//            selectedIndex = (angle / (360f / sectorCount)).toInt()
//        }
//
//        invalidate()
//    }
//
//
//
//    private fun lightenColor(color: Int, factor: Float): Int {
//        val r = Color.red(color)
//        val g = Color.green(color)
//        val b = Color.blue(color)
//        return Color.rgb(
//            (r + (255 - r) * factor).toInt(),
//            (g + (255 - g) * factor).toInt(),
//            (b + (255 - b) * factor).toInt()
//        )
//    }
//
//    fun setSectorCount(count: Int) {
//        sectorCount = count
//        invalidate()
//    }
//
//    fun setSectorColors(colors: List<Int>) {
//        require(colors.distinct().size >= min(colors.size, sectorCount)) {
//            "Цвета секторов должны быть разными и уникальными"
//        }
//        sectorColors = colors
//        invalidate()
//    }
//}





class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var sectorCount = 3
    private var sectorColors: List<Int> = listOf(
        Color.parseColor("#FF6B6B"), // Red
        Color.parseColor("#4ECDC4"), // Teal
        Color.parseColor("#FFA726")  // Orange
    )
    private var selectedIndex: Int = -1

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setupPaints()
    }

    private fun setupPaints() {
        paint.style = Paint.Style.FILL

        textPaint.apply {
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = Color.parseColor("#333333")
        }

        shadowPaint.apply {
            color = Color.parseColor("#20000000")
            maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (sectorCount <= 0) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) * 0.8f
        val strokeWidth = radius * 0.6f
        val innerRadius = radius - strokeWidth

        // Draw shadow
        drawShadow(canvas, centerX, centerY, radius + 4f, innerRadius - 4f)

        // Draw sectors (in reverse order so overlapping works correctly)
        for (i in sectorCount - 1 downTo 0) {
            drawSector(canvas, centerX, centerY, radius, innerRadius, strokeWidth, i)
        }

        // Draw center circle and text
        drawCenter(canvas, centerX, centerY, innerRadius)
    }

    private fun drawShadow(canvas: Canvas, centerX: Float, centerY: Float, outerRadius: Float, innerRadius: Float) {
        val shadowPath = Path().apply {
            addCircle(centerX, centerY, outerRadius, Path.Direction.CW)
            addCircle(centerX, centerY, innerRadius, Path.Direction.CCW)
            fillType = Path.FillType.EVEN_ODD
        }
        canvas.drawPath(shadowPath, shadowPaint)
    }

    private fun drawSector(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float,
        strokeWidth: Float,
        sectorIndex: Int
    ) {
        val sweepAngle = 360f / sectorCount
        val startAngle = 270f + sectorIndex * sweepAngle // Начало с 9 часов (270°)
        val endAngle = startAngle + sweepAngle

        // Устанавливаем цвет для основного сектора
        paint.color = getSectorColor(sectorIndex)

        val path = Path()

        val startAngleRad = Math.toRadians(startAngle.toDouble()).toFloat()
        val endAngleRad = Math.toRadians(endAngle.toDouble()).toFloat()

        val startOuterX = centerX + outerRadius * cos(startAngleRad)
        val startOuterY = centerY + outerRadius * sin(startAngleRad)
        val startInnerX = centerX + innerRadius * cos(startAngleRad)
        val startInnerY = centerY + innerRadius * sin(startAngleRad)
        val endOuterX = centerX + outerRadius * cos(endAngleRad)
        val endOuterY = centerY + outerRadius * sin(endAngleRad)
        val endInnerX = centerX + innerRadius * cos(endAngleRad)
        val endInnerY = centerY + innerRadius * sin(endAngleRad)

        // 1. Построение основного контура сектора (без закругления через arcTo на конце)
        path.moveTo(startInnerX, startInnerY)
        path.lineTo(startOuterX, startOuterY)
        path.arcTo(
            RectF(centerX - outerRadius, centerY - outerRadius, centerX + outerRadius, centerY + outerRadius),
            startAngle,
            sweepAngle,
            false
        )
        // Линия от внешней конечной точки к внутренней конечной точке (прямая, не дуга)
        path.lineTo(endInnerX, endInnerY)
        path.arcTo(
            RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius),
            endAngle,
            -sweepAngle, // Обратное направление
            false
        )
        path.close()
        canvas.drawPath(path, paint)

        // --- 2. Отдельное рисование закругленного конца ---
        // Этот круг будет нарисован поверх основной формы
        val capRadius = strokeWidth / 2f

        // Центр круга закругления должен быть в середине толщины линии
        // на конце сектора.
        val capMidRadius = innerRadius + capRadius

        val capCenterX = centerX + capMidRadius * cos(endAngleRad)
        val capCenterY = centerY + capMidRadius * sin(endAngleRad)

        // Используем тот же paint, который уже настроен на цвет сектора
        // Стиль paint уже установлен на FILL
        canvas.drawCircle(capCenterX, capCenterY, capRadius, paint)
    }

    private fun drawCenter(canvas: Canvas, centerX: Float, centerY: Float, innerRadius: Float) {
        // Draw center circle background
        paint.color = Color.WHITE
        canvas.drawCircle(centerX, centerY, innerRadius, paint)

        // Draw center circle border
        paint.apply {
            color = Color.parseColor("#E0E0E0")
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawCircle(centerX, centerY, innerRadius, paint)
        paint.style = Paint.Style.FILL

        // Draw center text
        textPaint.textSize = innerRadius * 0.6f
        val textY = centerY + textPaint.textSize / 3f
        canvas.drawText(sectorCount.toString(), centerX, textY, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouch(event.x, event.y)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleTouch(x: Float, y: Float) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) * 0.8f
        val strokeWidth = radius * 0.6f
        val innerRadius = radius - strokeWidth  // Синхронизируем с onDraw
//        val innerRadius = radius * 0.6f

        val dx = x - centerX
        val dy = y - centerY
        val distance = sqrt(dx * dx + dy * dy)

        // Check if touch is within the ring area
        if (distance in innerRadius..radius) {
            // Calculate angle from center
            var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
            // Adjust angle to start from 9 o'clock (270°) and go clockwise
            angle = (angle + 360 + 90) % 360

            // Determine which sector was touched
            val sectorAngle = 360f / sectorCount
            selectedIndex = (angle / sectorAngle).toInt()

            // Ensure selectedIndex is within bounds
            if (selectedIndex >= sectorCount) {
                selectedIndex = sectorCount - 1
            }
        } else {
            // Touch is outside the ring, clear selection
            selectedIndex = -1
        }

        invalidate()
    }

    private fun getSectorColor(index: Int): Int {
        val color = sectorColors[index % sectorColors.size]
        return if (index == selectedIndex) {
            lightenColor(color, 0.3f)
        } else {
            color
        }
    }

    private fun lightenColor(color: Int, factor: Float): Int {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return Color.rgb(
            (r + (255 - r) * factor).toInt(),
            (g + (255 - g) * factor).toInt(),
            (b + (255 - b) * factor).toInt()
        )
    }

    // Public API methods
    fun setSectorCount(count: Int) {
        require(count > 0) { "Sector count must be positive" }
        sectorCount = count
        selectedIndex = -1 // Reset selection
        invalidate()
    }

    fun setSectorColors(colors: List<Int>) {
        require(colors.isNotEmpty()) { "Colors list cannot be empty" }
        require(colors.size == colors.distinct().size) { "All colors must be unique" }
        sectorColors = colors
        invalidate()
    }

    fun getSelectedSector(): Int = selectedIndex

    fun setSelectedSector(index: Int) {
        selectedIndex = if (index in 0 until sectorCount) index else -1
        invalidate()
    }

    fun clearSelection() {
        selectedIndex = -1
        invalidate()
    }
}
