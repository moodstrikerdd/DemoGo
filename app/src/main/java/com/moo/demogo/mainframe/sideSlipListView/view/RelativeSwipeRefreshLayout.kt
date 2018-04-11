package com.moo.demogo.mainframe.sideSlipListView.view

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration


/**
 * @author moodstrikerdd
 * @date 2018/4/10
 * @label
 */

class RelativeSwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(context, attrs) {


    /**
     * 是否让子view处理touch事件
     */
    private var letChildDealTouchEvent: Boolean = false
    private var startX: Float = 0.toFloat()
    private var startY: Float = 0.toFloat()
    private val mTouchSlop: Int = ViewConfiguration.get(getContext()).scaledTouchSlop

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置
                startY = ev.y
                startX = ev.x
                // 初始化标记
                letChildDealTouchEvent = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果子view正在拖拽中，那么不拦截它的事件，直接return false；
                if (letChildDealTouchEvent) {
                    return false
                }

                // 获取当前手指位置
                val endY = ev.y
                val endX = ev.x
                val distanceX = Math.abs(endX - startX)
                val distanceY = Math.abs(endY - startY)
                // 如果X轴位移大于Y轴位移，那么将事件交给子View处理
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    letChildDealTouchEvent = true
                    return false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                // 初始化标记
                letChildDealTouchEvent = false
            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}
