package com.moo.demogo.commonView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.moo.demogo.utils.GraphUtils


/**
 * @ClassName:      DoubleBarGraphView
 * @Author:         moodstrikerdd
 * @CreateDate:     2019/12/4 16:01
 * @Description:    java类作用描述
 */
class DoubleBarGraphView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var isMeasured = false
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var mode = MeasureSpec.getMode(heightMeasureSpec)
        if (mode != MeasureSpec.EXACTLY) {
            mode = MeasureSpec.EXACTLY
            val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180f, context.resources.displayMetrics).toInt()
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, mode))
        }
        isMeasured = true
        if (isDataSet) {
            calcPoint()
        }
    }

    private val data1Colors = arrayListOf(Color.parseColor("#B3458CF5"), Color.parseColor("#80458CF5"), Color.parseColor("#4D458CF5"), Color.parseColor("#33458CF5"))
    private val data2Colors = arrayListOf(Color.parseColor("#B3FF424A"), Color.parseColor("#80FF424A"), Color.parseColor("#4DFF424A"), Color.parseColor("#33FF424A"))

    private lateinit var bottomTag: List<String>
    private val bottomTagX = arrayListOf<Int>()
    private lateinit var data1: List<List<Float>>
    private val data1Height = arrayListOf<List<Int>>()
    private lateinit var data2: List<List<Float>>
    private val data2Height = arrayListOf<List<Int>>()
    private var bottomLine = 0
    private lateinit var brokenLineData: List<Float>
    private val brokenLineDataY = arrayListOf<Int>()
    private lateinit var mainRect: Rect
    private val contentRect = Rect()
    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    private fun clear() {
        bottomTagX.clear()
        data1Height.clear()
        data2Height.clear()
        brokenLineDataY.clear()
    }


    private var isDataSet = false
    fun setData(bottomTag: List<String>, data1: List<List<Float>>, data2: List<List<Float>>, data3: List<Float>) {
        this.bottomTag = bottomTag
        this.data1 = data1
        this.data2 = data2
        this.brokenLineData = data3
        if (isMeasured) {
            calcPoint()
            postInvalidate()
        }
    }


    private fun calcPoint() {
        clear()
        var max = 0f
        var min = 0f
        data1.forEach {
            var tempMax = 0f
            var tempMin = 0f
            it.forEach { innerData ->
                if (innerData > 0) {
                    tempMax += innerData
                } else {
                    tempMin += innerData
                }
            }
            if (max < tempMax) {
                max = tempMax
            }
            if (tempMin == 0f) {
                tempMin = tempMax
            }
            if (min > tempMin) {
                min = tempMin
            }
        }
        data2.forEach {
            var tempMax = 0f
            var tempMin = 0f
            it.forEach { innerData ->
                if (innerData > 0) {
                    tempMax += innerData
                } else {
                    tempMin += innerData
                }
            }
            if (max < tempMax) {
                max = tempMax
            }
            if (tempMin == 0f) {
                tempMin = tempMax
            }
            if (min > tempMin) {
                min = tempMin
            }
        }

        mainRect = Rect(0, dp2px(20).toInt(), measuredWidth, (measuredHeight - dp2px(20)).toInt())
        bottomLine = mainRect.bottom
        val contentHeight = mainRect.bottom - mainRect.top

        data1.forEach {
            val list = arrayListOf<Int>()
            it.forEach { innerData ->
                list.add(((innerData / max) * contentHeight).toInt())
            }
            data1Height.add(list)
        }
        data2.forEach {
            val list = arrayListOf<Int>()
            it.forEach { innerData ->
                list.add(((innerData / max) * contentHeight).toInt())
            }
            data2Height.add(list)
        }

        val itemWidth = dp2px(46)
        val offset = ((mainRect.right - mainRect.left - (dp2px(8) * 2)) - dp2px(14) * 2 - (itemWidth * data1.size)) / (data1.size - 1)
        for (i in bottomTag.indices) {
            bottomTagX.add((dp2px(22) + (offset + itemWidth) * i + (itemWidth / 2)).toInt())
        }

        data1.forEachIndexed { index, list ->
            var totalData1 = 0f
            var totalData2 = 0f
            list.forEachIndexed { innerIndex, innerData ->
                totalData1 += innerData
                totalData2 += data2[index][innerIndex]
            }
        }
        val brokenMax = brokenLineData.max() ?: 0f
        brokenLineData.forEach {
            brokenLineDataY.add((mainRect.height() * it / brokenMax).toInt())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bottomTagX.isEmpty()) {
            return
        }
        drawBottomLine(canvas)
        drawGraph(canvas)
    }

    private fun drawGraph(canvas: Canvas) {
        data1.forEachIndexed { index, data ->
            //画蓝色柱状图
            var currentTop = 0
            var totalMoney = 0f
            data.forEachIndexed { innerIndex, innerData ->
                totalMoney += innerData
                paint.color = data1Colors[innerIndex]
                contentRect.apply {
                    left = (bottomTagX[index] - (dp2px(46) / 2)).toInt()
                    right = (bottomTagX[index] - dp2px(3)).toInt()
                    bottom = bottomLine - currentTop
                    top = bottom - data1Height[index][innerIndex]
                    currentTop += data1Height[index][innerIndex]
                }
                canvas.drawRect(contentRect, paint)
            }

            //画蓝色柱状图上的文字
            var drawText = GraphUtils.getMoneyString(totalMoney)
            if (totalMoney != 0f) {
                paint.apply {
                    color = data1Colors[0]
                    textSize = sp2px(8)
                    getTextBounds(drawText, 0, drawText.length, contentRect)
                }
                canvas.drawText(drawText, (bottomTagX[index] - (dp2px(46) / 2) + dp2px(10)) - (contentRect.width() / 2),
                        bottomLine - currentTop - dp2px(5)
                        , paint)

            }
            //画横坐标下的下标
            drawText = bottomTag[index]
            paint.apply {
                color = Color.parseColor("#9999A3")
                textSize = sp2px(10)
                getTextBounds(drawText, 0, drawText.length, contentRect)
            }
            canvas.drawText(drawText, (bottomTagX[index] - contentRect.width() / 2).toFloat(),
                    measuredHeight - dp2px(6), paint)
        }

        data2.forEachIndexed { index, data ->
            //画红色柱状图
            var currentTop = 0
            var totalMoney = 0f
            data.forEachIndexed { innerIndex, innerData ->
                totalMoney += innerData
                paint.color = data2Colors[innerIndex]
                contentRect.apply {
                    left = (bottomTagX[index] + dp2px(3)).toInt()
                    right = (contentRect.left + dp2px(20)).toInt()
                    bottom = bottomLine - currentTop
                    top = bottom - data2Height[index][innerIndex]
                    currentTop += data2Height[index][innerIndex]
                }
                canvas.drawRect(contentRect, paint)
            }

            //画红色柱状图上的文字
            if (totalMoney != 0f) {
                val drawText = GraphUtils.getMoneyString(totalMoney)
                paint.apply {
                    color = data2Colors[0]
                    textSize = sp2px(8)
                    getTextBounds(drawText, 0, drawText.length, contentRect)
                }
                canvas.drawText(drawText, (bottomTagX[index] + dp2px(3) + dp2px(10)) - (contentRect.width() / 2),
                        bottomLine - currentTop - dp2px(2)
                        , paint)
            }
            //画折线图
            brokenLineDataY.forEachIndexed { index, data ->
                paint.apply {
                    color = Color.parseColor("#FBB000")
                    style = Paint.Style.STROKE
                }
                canvas.drawCircle(bottomTagX[index] + dp2px(3) + dp2px(10), bottomLine - data.toFloat(), dp2px(3), paint)
                if (index < brokenLineDataY.size - 1) {
                    canvas.drawLine(bottomTagX[index] + dp2px(3) + dp2px(10), bottomLine - data.toFloat(),
                            bottomTagX[index + 1] + dp2px(3) + dp2px(10), bottomLine - brokenLineDataY[index + 1].toFloat(), paint)
                }
                paint.apply {
                    color = Color.WHITE
                    style = Paint.Style.FILL
                }
                canvas.drawCircle(bottomTagX[index] + dp2px(3) + dp2px(10), bottomLine - data.toFloat(), dp2px(3) - paint.strokeWidth / 2, paint)
            }
        }
    }

    //画基准线
    private fun drawBottomLine(canvas: Canvas) {
        paint.apply {
            color = Color.parseColor("#E8E8E8")
            strokeWidth = dp2px(1)
        }
        canvas.drawLine(mainRect.left + dp2px(14), bottomLine - (dp2px(1) / 2), mainRect.right - dp2px(14), bottomLine + (dp2px(1) / 2), paint)
    }


    private fun dp2px(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    private fun sp2px(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp.toFloat(), context.resources.displayMetrics)

}