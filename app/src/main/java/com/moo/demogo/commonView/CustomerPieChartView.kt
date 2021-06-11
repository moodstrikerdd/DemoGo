package com.moo.demogo.commonView

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * @ClassName:      CustomerPieChartView
 * @Author:         moodstrikerdd
 * @CreateDate:     2021/6/4 10:21
 * @Description:    java类作用描述
 */
class CustomerPieChartView
@JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defInt: Int = 0)
    : View(context, attr, defInt) {
    private val Int.dp
        get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics).toInt()

    private val Int.sp
        get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                this.toFloat(),
                Resources.getSystem().displayMetrics)

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private val mData: MutableList<FormData> = arrayListOf()
    private val textRect: MutableList<Rect> = arrayListOf()
    private val linePath: MutableList<Path> = arrayListOf()

    private val colors = arrayListOf(
            Color.parseColor("#1F853D"),
            Color.parseColor("#2CB956"),
            Color.parseColor("#FB5446"),
            Color.parseColor("#FE0306")
    )


    private val pieChartRect = RectF()
    private val drawPieChartRect = RectF()
    private val chartTotalWidth = 140.dp
    private val chartWidth = 28.dp

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    private val textMaxWidth by lazy {
        var max = 0f
        mPaint.textSize = 14.sp
        for (data in mData) {
            max = maxOf(max, mPaint.measureText(data.getNumberDisplayText()))
            max = maxOf(max, mPaint.measureText(data.getTitle()))
        }
        max
    }
    private val textTotalHeight by lazy {
        var height = 0
        mPaint.textSize = 14.sp
        mPaint.isFakeBoldText = true
        height += (mPaint.fontMetrics.bottom - mPaint.fontMetrics.top).toInt()
        mPaint.textSize = 12.sp
        mPaint.isFakeBoldText = false
        height += (mPaint.fontMetrics.bottom - mPaint.fontMetrics.top).toInt() * 2
        height
    }

    private val leftAndRightPadding = 30.dp
    private var leftBorder = 0f
    private var rightBorder = 0f

    private val topBorder = textTotalHeight / 2

    private var bottomBorder = 0

    private val firstLineLength = 15.dp
    private val lineTextMargin = 9.dp


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = textTotalHeight + chartTotalWidth
        setMeasuredDimension(widthMeasureSpec, height)
        calc()
    }

    fun setData(data: List<FormData>) {
        mData.clear()
        mData.addAll(data)
        calc()
        invalidate()
    }

    private fun calc() {
        textRect.clear()
        linePath.clear()
        //中间进度范围
        pieChartRect.left = measuredWidth.toFloat() / 2 - chartTotalWidth.toFloat() / 2
        pieChartRect.top = measuredHeight.toFloat() / 2 - chartTotalWidth.toFloat() / 2
        pieChartRect.right = measuredWidth.toFloat() / 2 + chartTotalWidth.toFloat() / 2
        pieChartRect.bottom = measuredHeight.toFloat() / 2 + chartTotalWidth.toFloat() / 2

        drawPieChartRect.left = pieChartRect.left + chartWidth / 2
        drawPieChartRect.top = pieChartRect.top + chartWidth / 2
        drawPieChartRect.right = pieChartRect.right - chartWidth / 2
        drawPieChartRect.bottom = pieChartRect.bottom - chartWidth / 2

        if (mData.isEmpty()) {
            return
        }

        bottomBorder = measuredHeight - textTotalHeight / 2
        leftBorder = leftAndRightPadding + textMaxWidth + lineTextMargin
        rightBorder = measuredWidth - leftAndRightPadding - textMaxWidth - lineTextMargin

        for (i in mData.indices) {
            textRect.add(Rect())
            linePath.add(Path())
        }
        calcTextAndPath()
    }

    /**
     * 计算文字的rect和指引线path
     */
    private fun calcTextAndPath() {
        var startAngle = -90f
        for (i in mData.indices) {
            val data = mData[i]
            val curSweepAngle = 360 * data.getPercent()
            val curCenterAngle = (curSweepAngle / 2 + startAngle + 1)
            startAngle += curSweepAngle

            val path = linePath[i]
            val curCos = cos(Math.toRadians(curCenterAngle.toDouble()))
            val curSin = sin(Math.toRadians(curCenterAngle.toDouble()))
            val curTan = tan(Math.toRadians(curCenterAngle.toDouble()))

            val pathStartX = pieChartRect.centerX() +
                    (curCos * chartTotalWidth / 2).toInt()
            val pathStartY = pieChartRect.centerY() +
                    (curSin * chartTotalWidth / 2).toInt()
            path.moveTo(pathStartX, pathStartY)

            var secondPointY = (pathStartY + firstLineLength * curSin).toFloat()
            if (secondPointY < topBorder) {
                secondPointY = topBorder.toFloat()
            }
            if (secondPointY > bottomBorder) {
                secondPointY = bottomBorder.toFloat()
            }
            var secondPointX = (pathStartX + (secondPointY - pathStartY) / curTan).toFloat()

            if (secondPointX < leftBorder) {
                secondPointX = leftBorder
                secondPointY  =  (pathStartY + (secondPointX - pathStartX) * curTan).toFloat()
            }
            if (secondPointX > rightBorder) {
                secondPointX = rightBorder
                secondPointY =  (pathStartY + (secondPointX - pathStartX) * curTan).toFloat()
            }

            path.lineTo(secondPointX, secondPointY)

            val rect = textRect[i]
            var thirdPoint: Float
            if (secondPointX > pieChartRect.centerX()) {
                thirdPoint = rightBorder
                rect.left = (thirdPoint + lineTextMargin).toInt()
            } else {
                thirdPoint = leftBorder
                rect.left = (thirdPoint - textMaxWidth - lineTextMargin).toInt()
            }
            rect.right = (rect.left + textMaxWidth).toInt()
            rect.top = (secondPointY - textTotalHeight / 2).toInt()
            rect.bottom = (secondPointY + textTotalHeight / 2).toInt()

            path.lineTo(thirdPoint, secondPointY)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mData.isEmpty()) {
            drawCenterText(canvas, "加载中...")
            return
        }
        drawCenterText(canvas, "今日资金")
        drawChart(canvas)
        drawPath(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        for (i in textRect.indices) {
            if (mData[i].getPercent() == 0f) {
                continue
            }
            mPaint.apply {
                if (i in textRect.indices) {
                    color = colors[i]
                }
                strokeWidth = 1.dp.toFloat()
                style = Paint.Style.FILL
            }

            val rect = textRect[i]

            if (rect.left < pieChartRect.centerX()) {
                mPaint.textAlign = Paint.Align.RIGHT
            } else {
                mPaint.textAlign = Paint.Align.LEFT
            }
            val textArray = arrayOf(
                    "${(mData[i].getPercent() * 100).toInt()}%",
                    mData[i].getTitle(),
                    mData[i].getNumberDisplayText())

            var curY = rect.top.toFloat()

            for (i in textArray.indices) {
                val text = textArray[i]
                mPaint.isFakeBoldText = i == 0
                val fontMetrics = mPaint.fontMetrics
                val curTextHeight = fontMetrics.bottom - fontMetrics.top
                canvas.drawText(text,
                        if (rect.left > pieChartRect.centerX()) {
                            rect.left.toFloat()
                        } else {
                            rect.right.toFloat()
                        },
                        curY + curTextHeight - fontMetrics.bottom,
                        mPaint)
                curY += curTextHeight
            }

        }
    }

    private fun drawPath(canvas: Canvas) {
        for (i in linePath.indices) {
            if (mData[i].getPercent() == 0f) {
                continue
            }
            mPaint.apply {
                if (i in linePath.indices) {
                    color = colors[i]
                }
                strokeWidth = 1.dp.toFloat()
                style = Paint.Style.STROKE
            }
            canvas.drawPath(linePath[i], mPaint)
        }
    }

    private fun drawChart(canvas: Canvas) {
        var startAngle = -90f
        for (i in mData.indices) {
            mPaint.apply {
                if (i in colors.indices) {
                    color = colors[i]
                }
                style = Paint.Style.STROKE
                strokeWidth = chartWidth.toFloat()
            }

            val data = mData[i]
            if (data.getPercent() == 0f) {
                continue
            }
            val currentAngle = 360 * data.getPercent()
            canvas.drawArc(drawPieChartRect,
                    startAngle + 1,
                    currentAngle - 1,
                    false, mPaint)
            startAngle += (currentAngle)
        }
    }

    private fun drawCenterText(canvas: Canvas, centerText: String) {
        mPaint.apply {
            textAlign = Paint.Align.CENTER
            color = Color.parseColor("#66687F")
            textSize = 12.sp
        }
        val x = pieChartRect.centerX()
        val fontMetrics = mPaint.fontMetrics
        val y = pieChartRect.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2
        canvas.drawText(centerText, x, y, mPaint)
    }


    interface FormData {
        fun getTitle(): String
        fun getPercent(): Float
        fun getNumberDisplayText(): String

    }
}