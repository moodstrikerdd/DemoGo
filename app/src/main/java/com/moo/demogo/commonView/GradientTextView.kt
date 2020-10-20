package com.moo.demogo.commonView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.TextView



/**
 * @ClassName:      GradientTextView
 * @Author:         moodstrikerdd
 * @CreateDate:     2020/1/3 11:19
 * @Description:    java类作用描述
 */
class GradientTextView @JvmOverloads
constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = android.R.attr.textViewStyle) :
        TextView(context, attrs, defStyleAttr) {

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            textSize = this@GradientTextView.textSize
            val linearGradient = LinearGradient(0f, 0f, measuredWidth.toFloat(), 0f, Color.parseColor("#9999A3"), Color.parseColor("#00f7f8fa"), Shader.TileMode.CLAMP)
            shader = linearGradient
        }
    }

    private val mTextBound = Rect()

    override fun onDraw(canvas: Canvas) {
        val mTipText = text.toString()
        mPaint.getTextBounds(mTipText, 0, mTipText.length, mTextBound)
        val fontMetrics = mPaint.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseline = mTextBound.centerY() + distance
        canvas.drawText(mTipText, measuredWidth.toFloat() / 2 - mTextBound.width() / 2, measuredHeight.toFloat() / 2 + mTextBound.height() / 2, mPaint)
}
}