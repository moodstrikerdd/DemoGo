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
 * @ClassName:      SingleBarGraphView
 * @Author:         moodstrikerdd
 * @CreateDate:     2019/12/16 15:58
 * @Description:    java类作用描述
 */
class SingleBarGraphView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, res: Int = 0)
    : View(context, attributeSet, res) {
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

    private var showRight = true
    private lateinit var data: List<List<Float>>
    private val dataHeight = arrayListOf<List<Int>>()
    private lateinit var bottomTag: List<String>
    private val bottomTagX = arrayListOf<Int>()
    private lateinit var rightData: List<Float>
    private val rightDataY = arrayListOf<Int>()
    private val rightTagText = arrayListOf<String>()
    private var bottomLine = 0

    private val rectList = arrayListOf<Rect>()


    private var pnProportion = 0f
    private var graphMax = 0f
    private var brokenLineMax = 0f

    private val colors = arrayListOf(Color.parseColor("#458CF5"),
            Color.parseColor("#CC458CF5"),
            Color.parseColor("#99458CF5"),
            Color.parseColor("#4D458CF5"))

    private val itemWidth = dp2px(34).toInt()

    private lateinit var mainRect: Rect
    private val contentRect = Rect()
    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            textSize = sp2px(10)
        }
    }


    private var isDataSet = false
    fun setData(data: List<List<Float>>, bottomTag: List<String>, rightData: List<Float>, showRight: Boolean = true) {
        this.data = data
        this.bottomTag = bottomTag
        this.rightData = rightData
        this.showRight = showRight
        if (isMeasured) {
            calcPoint()
            postInvalidate()
        }
    }

    fun calcPoint() {
        var max = 0f
        var min = 0f
        data.forEach {
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
        val rightMax = rightData.max() ?: 0f
        val rightMin = rightData.min() ?: 0f
        //定右边坐标
        val rightMaxTag = when {
            rightMax > 0 -> ((rightMax.toInt() / 40) + 1) * 40
            rightMax < 0 -> ((rightMax.toInt() / 40) - 1) * 40
            else -> 0
        }

        val rightMinTag = when {
            rightMin > 0 -> ((rightMin.toInt() / 40) + 1) * 40
            rightMin < 0 -> ((rightMin.toInt() / 40) - 1) * 40
            else -> 0
        }

        var tempMax = rightMaxTag
        var tempMin = rightMinTag
        if (rightMaxTag < 0) {
            tempMax = 0
            tempMin = rightMinTag
            brokenLineMax = -tempMin.toFloat()
        } else {
            if (rightMinTag >= 0) {
                tempMax = rightMaxTag
                tempMin = 0
                brokenLineMax = tempMax.toFloat()
            } else {
                val p = tempMax.toFloat() / -tempMin.toFloat()
                when {
                    p > 3 -> {
                        while (tempMax % 3 != 0) {
                            tempMax += 10
                        }
                        tempMin = -tempMax / 3
                        brokenLineMax = -tempMin * 4f
                    }
                    p > 1 -> {
                        tempMin = -tempMax
                        brokenLineMax = tempMax * 2f
                    }
                    p == 1f -> {
                        brokenLineMax = tempMax * 2f
                    }
                    p <= 1 / 3 -> {
                        while (tempMin % 3 != 0) {
                            tempMin -= 10
                        }
                        tempMax = -tempMin / 3
                        brokenLineMax = tempMax * 4f
                    }
                    p < 1 -> {
                        tempMax = -tempMin
                        brokenLineMax = tempMax * 2f
                    }
                }
            }
        }
        dealWithType(max, min, tempMax.toFloat(), tempMin.toFloat())

        val rightOffset = (tempMax - tempMin) / 4
        tempMax += rightOffset
        while (tempMax != tempMin) {
            rightTagText.add("${tempMax - rightOffset}%")
            tempMax -= rightOffset
        }


        val rightContentWeight = if (rightData.isNotEmpty() && showRight) {
            paint.measureText("10%") + dp2px(5) + dp2px(14)
        } else {
            dp2px(14)
        }
        when (pnProportion) {
            0f -> {
                mainRect = Rect(0,
                        0,
                        measuredWidth - rightContentWeight.toInt(),
                        measuredHeight - dp2px(40).toInt())
                bottomLine = mainRect.top
            }
            1f -> {
                mainRect = Rect(0,
                        dp2px(20).toInt(),
                        measuredWidth - rightContentWeight.toInt(),
                        measuredHeight - dp2px(20).toInt())
                bottomLine = mainRect.bottom
            }
            else -> {
                mainRect = Rect(0,
                        dp2px(20).toInt(),
                        measuredWidth - rightContentWeight.toInt(),
                        measuredHeight - dp2px(20).toInt())
                bottomLine = (mainRect.height() * pnProportion + dp2px(20)).toInt()
            }
        }
        data.forEach {
            val list = arrayListOf<Int>()
            it.forEach { innerData ->
                list.add((mainRect.height() * innerData / graphMax).toInt())
            }
            dataHeight.add(list)
        }
        val offset = (mainRect.width() - dp2px(14) - dp2px(8) * 2 - itemWidth * data.size) / (data.size - 1)

        bottomTag.forEachIndexed { index, _ ->
            bottomTagX.add((dp2px(8) + dp2px(14) + (itemWidth + offset) * index + (itemWidth / 2)).toInt())
        }
        rightData.forEach {
            rightDataY.add((mainRect.height() * it / brokenLineMax).toInt())
        }
        postInvalidate()
    }

    private fun dealWithType(max: Float, min: Float, rightMax: Float, rightMin: Float) {
        //0全负数，1全正数，2正负数
        var type = 0
        if (max > 0f) {
            type = if (min >= 0f) {
                1
            } else {
                2
            }
        }
        var type2 = 0
        if (rightMax > 0f) {
            type2 = if (rightMin >= 0f) {
                1
            } else {
                2
            }
        }
        when (type) {
            0 -> {
                when (type2) {
                    0 -> {
                        pnProportion = 0f
                        graphMax = -min
                    }
                    1 -> {
                        pnProportion = 0.5f
                        graphMax = -min * 2
                        brokenLineMax *= 2
                    }
                    2 -> {
                        pnProportion = rightMax / (rightMax - rightMin)
                        graphMax = -min / pnProportion
                    }
                }
            }
            1 -> {
                when (type2) {
                    0 -> {
                        pnProportion = 0.5f
                        graphMax = max * 2
                        brokenLineMax *= 2
                    }
                    1 -> {
                        pnProportion = 1f
                        graphMax = max
                    }
                    2 -> {
                        pnProportion = rightMax / (rightMax - rightMin)
                        graphMax = max / pnProportion
                    }
                }
            }
            2 -> {
                when (type2) {
                    0 -> {
                        pnProportion = max / (max - min)
                        graphMax = max - min
                        brokenLineMax /= pnProportion
                    }
                    1 -> {
                        pnProportion = max / (max - min)
                        graphMax = max - min
                        brokenLineMax /= pnProportion
                    }
                    2 -> {
                        pnProportion = 0.5f
                        graphMax = max * 2
                        brokenLineMax *= 2
                    }
                }
            }
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

    //画柱状图
    private fun drawGraph(canvas: Canvas) {
        rectList.clear()
        data.forEachIndexed { index, list ->
            var positive = 0
            var negative = 0
            var totalMoney = 0f
            list.forEachIndexed { innerIndex, innerData ->
                totalMoney += innerData
                paint.color = if (list.size == 1) {
                    colors[2]
                } else {
                    colors[innerIndex]
                }
                contentRect.apply {
                    left = bottomTagX[index] - itemWidth / 2
                    right = bottomTagX[index] + itemWidth / 2
                    bottom = if (innerData >= 0) {
                        bottomLine - positive
                    } else {
                        bottomLine - negative
                    }
                    val currentHeight = dataHeight[index][innerIndex]
                    top = bottom - currentHeight
                    if (currentHeight >= 0) {
                        positive += currentHeight
                    } else {
                        negative += currentHeight
                    }
                }
                canvas.drawRect(contentRect, paint)
            }
            rectList.add(Rect(bottomTagX[index] - itemWidth / 2, bottomLine - positive, bottomTagX[index] + itemWidth / 2, bottomLine - negative))
            //画顶上的数字
            var drawText = GraphUtils.getMoneyString(totalMoney)
            if (positive != 0 || negative != 0) {
                paint.apply {
                    textSize = sp2px(8)
                    color = colors[0]
                    getTextBounds(drawText, 0, drawText.length, contentRect)
                }
                canvas.drawText(drawText, (bottomTagX[index] - (contentRect.width() / 2)).toFloat(),
                        bottomLine - positive - dp2px(5)
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
        //画点
        rightDataY.forEachIndexed { index, data ->
            paint.apply {
                color = Color.parseColor("#FBB000")
                style = Paint.Style.STROKE
            }
            canvas.drawCircle(bottomTagX[index].toFloat(), bottomLine - data.toFloat(), dp2px(3), paint)
            if (index < rightDataY.size - 1) {
                canvas.drawLine(bottomTagX[index].toFloat(), bottomLine - data.toFloat(),
                        bottomTagX[index + 1].toFloat(), bottomLine - rightDataY[index + 1].toFloat(), paint)
            }
            paint.apply {
                style = Paint.Style.FILL
                color = Color.WHITE
            }
            canvas.drawCircle(bottomTagX[index].toFloat(), bottomLine - data.toFloat(), dp2px(3) - paint.strokeWidth / 2, paint)
        }
        //画右边坐标
        if (showRight) {
            paint.apply {
                color = Color.parseColor("#9999A3")
                textSize = sp2px(10)
                getTextBounds("10%", 0, "10%".length, contentRect)
            }
            val index = rightTagText.indexOf("0%")
            val offset = if (index == 0) {
                bottomLine - mainRect.top
            } else {
                (bottomLine - mainRect.top) / (index)
            }
            rightTagText.forEachIndexed { index, str ->
                canvas.drawText(str, mainRect.right + dp2px(4), (mainRect.top + (index * offset)).toFloat(), paint)
            }
        }
    }

    //画基准线
    private fun drawBottomLine(canvas: Canvas) {
        paint.color = Color.parseColor("#E8E8E8")
        paint.strokeWidth = dp2px(1)
        canvas.drawLine(mainRect.left + dp2px(14), bottomLine - (dp2px(1) / 2), mainRect.right.toFloat(), bottomLine + (dp2px(1) / 2), paint)
    }


    private fun dp2px(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    private fun sp2px(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp.toFloat(), context.resources.displayMetrics)

}