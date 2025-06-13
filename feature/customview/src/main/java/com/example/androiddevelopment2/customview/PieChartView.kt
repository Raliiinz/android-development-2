package com.example.androiddevelopment2.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import androidx.core.graphics.toColorInt

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SECTOR_COUNT = 3
        const val NO_SELECTION = -1
        private const val RADIUS_RATIO = 0.8f
        private const val STROKE_WIDTH_RATIO = 0.6f
        private const val CENTER_BORDER_WIDTH = 2f
        private const val TEXT_SIZE_RATIO = 0.6f
        private const val TEXT_VERTICAL_OFFSET = 3f
        private const val LIGHTEN_FACTOR = 0.3f
        private const val START_ANGLE = 270f
        private const val DARK_GRAY_COLOR = "#333333"
        private const val LIGHT_GRAY_COLOR = "#E0E0E0"
        private const val SHADOW_COLOR = "#20000000"
    }

    private var sectorCount = DEFAULT_SECTOR_COUNT
    private var sectorColors: List<Int> = emptyList()
    private var selectedIndex = NO_SELECTION

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = DARK_GRAY_COLOR.toColorInt()
    }

    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = SHADOW_COLOR.toColorInt()
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val centerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = LIGHT_GRAY_COLOR.toColorInt()
        strokeWidth = CENTER_BORDER_WIDTH
    }

    init {
        isClickable = true

        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.PieChartView,
                defStyleAttr,
                0
            )

            try {
                val count =
                    typedArray.getInt(R.styleable.PieChartView_sectorsCount, DEFAULT_SECTOR_COUNT)
                val colorsResId = typedArray.getResourceId(R.styleable.PieChartView_sectorColors, 0)

                val colors = if (colorsResId != 0) {
                    context.resources.getIntArray(colorsResId).toList()
                } else {
                    emptyList()
                }

                updateData(count, colors)
            } finally {
                typedArray.recycle()
            }
        }
    }

    fun updateData(count: Int, colors: List<Int>, selectedIndex: Int = NO_SELECTION) {
        if (count < 0 || colors.isEmpty()) return

        this.sectorCount = count
        this.sectorColors = colors
        this.selectedIndex = selectedIndex.coerceIn(NO_SELECTION, count - 1)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (sectorCount <= 0 || sectorColors.isEmpty()) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) * RADIUS_RATIO
        val strokeWidth = radius * STROKE_WIDTH_RATIO
        val innerRadius = radius - strokeWidth

        drawShadow(canvas, centerX, centerY, radius + 4f, innerRadius - 4f)

        for (i in sectorCount - 1 downTo 0) {
            drawSectorBody(canvas, centerX, centerY, radius, innerRadius, strokeWidth, i)
        }

        for (i in sectorCount - 1 downTo 0) {
            drawSectorCap(canvas, centerX, centerY, radius, innerRadius, strokeWidth, i)
        }

        drawCenter(canvas, centerX, centerY, innerRadius)
    }

    private fun drawSectorBody(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float,
        strokeWidth: Float,
        sectorIndex: Int
    ) {
        val sweepAngle = 360f / sectorCount
        val startAngle = START_ANGLE + sectorIndex * sweepAngle
        val endAngle = startAngle + sweepAngle

        paint.color = getSectorColor(sectorIndex)

        val path = Path()

        val startAngleRad = Math.toRadians(startAngle.toDouble()).toFloat()
        val endAngleRad = Math.toRadians(endAngle.toDouble()).toFloat()

        val startOuterX = centerX + outerRadius * cos(startAngleRad)
        val startOuterY = centerY + outerRadius * sin(startAngleRad)
        val startInnerX = centerX + innerRadius * cos(startAngleRad)
        val startInnerY = centerY + innerRadius * sin(startAngleRad)
        val endInnerX = centerX + innerRadius * cos(endAngleRad)
        val endInnerY = centerY + innerRadius * sin(endAngleRad)

        path.moveTo(startInnerX, startInnerY)
        path.lineTo(startOuterX, startOuterY)
        path.arcTo(
            RectF(
                centerX - outerRadius,
                centerY - outerRadius,
                centerX + outerRadius,
                centerY + outerRadius
            ),
            startAngle,
            sweepAngle,
            false
        )
        path.lineTo(endInnerX, endInnerY)
        path.arcTo(
            RectF(
                centerX - innerRadius,
                centerY - innerRadius,
                centerX + innerRadius,
                centerY + innerRadius
            ),
            endAngle,
            -sweepAngle,
            false
        )
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawSectorCap(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float,
        strokeWidth: Float,
        sectorIndex: Int
    ) {
        val sweepAngle = 360f / sectorCount
        val startAngle = 270f + sectorIndex * sweepAngle
        val endAngle = startAngle + sweepAngle

        val endAngleRad = Math.toRadians(endAngle.toDouble()).toFloat()

        val capRadius = strokeWidth / 2f
        val capMidRadius = innerRadius + capRadius

        val capCenterX = centerX + capMidRadius * cos(endAngleRad)
        val capCenterY = centerY + capMidRadius * sin(endAngleRad)

        paint.color = getSectorColor(sectorIndex)
        canvas.drawCircle(capCenterX, capCenterY, capRadius, paint)
    }

    private fun drawShadow(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float
    ) {
        Path().apply {
            addCircle(centerX, centerY, outerRadius, Path.Direction.CW)
            addCircle(centerX, centerY, innerRadius, Path.Direction.CCW)
            fillType = Path.FillType.EVEN_ODD
            canvas.drawPath(this, shadowPaint)
        }
    }

    private fun drawCenter(canvas: Canvas, centerX: Float, centerY: Float, innerRadius: Float) {
        canvas.drawCircle(centerX, centerY, innerRadius, centerPaint)
        canvas.drawCircle(centerX, centerY, innerRadius, centerBorderPaint)

        textPaint.textSize = innerRadius * TEXT_SIZE_RATIO
        canvas.drawText(
            sectorCount.toString(),
            centerX,
            centerY + textPaint.textSize / TEXT_VERTICAL_OFFSET,
            textPaint
        )
    }

    var onSelectionChanged: ((Int) -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchedSector = getTouchedSector(event.x, event.y)
                selectedIndex = touchedSector
                invalidate()
                onSelectionChanged?.invoke(touchedSector)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getTouchedSector(x: Float, y: Float): Int {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) * RADIUS_RATIO
        val strokeWidth = radius * STROKE_WIDTH_RATIO
        val innerRadius = radius - strokeWidth

        val distance = sqrt((x - centerX).pow(2) + (y - centerY).pow(2))

        if (distance < innerRadius || distance > radius) {
            return NO_SELECTION
        }

        var angle = Math.toDegrees(atan2(y - centerY, x - centerX).toDouble()).toFloat()
        angle = (angle + 360 + 90) % 360
        return (angle / (360f / sectorCount)).toInt().coerceIn(0, sectorCount - 1)
    }

    private fun getSectorColor(index: Int): Int {
        val color = sectorColors[index % sectorColors.size]
        return if (index == selectedIndex) lightenColor(color) else color
    }

    private fun lightenColor(color: Int): Int {
        return Color.rgb(
            (Color.red(color) + (255 - Color.red(color)) * LIGHTEN_FACTOR).toInt(),
            (Color.green(color) + (255 - Color.green(color)) * LIGHTEN_FACTOR).toInt(),
            (Color.blue(color) + (255 - Color.blue(color)) * LIGHTEN_FACTOR).toInt()
        )
    }
}