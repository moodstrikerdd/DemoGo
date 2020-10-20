package com.moo.demogo.mainframe.sidesliplistview.view

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Scroller

/**
 * @author moodstrikerdd
 * @date 2018/4/10
 * @label
 */

class SideSlipListView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : ListView(context, attrs, defStyle) {
    /**
     * 屏幕宽度
     */
    private val mScreenWidth: Int
    /**
     * 按下点的x值
     */
    private var mDownX: Int = 0
    /**
     * 按下点的y值
     */
    private var mDownY: Int = 0
    /**
     * 删除按钮的宽度
     */
    private var mDeleteBtnWidth: Int = 0
    /**
     * 当前处理的item
     */
    private var mPointChild: ViewGroup? = null
    /**
     * 当前处理上一个item
     */
    private var mLastPointChild: ViewGroup? = null
    /**
     * 当前处理的item的LayoutParams
     */
    private var mLayoutParams: LinearLayout.LayoutParams? = null


    private val mScroller: Scroller

    private val mTouchSlop: Int

    /**
     * 是否拦截down up时间
     */
    private var shouldOnlyTouchDown: Boolean = false
    /**
     * 是否正在左划
     */
    private var isLeftScrolling: Boolean = false
    /**
     * 是否正在垂直滑动
     */
    private var startScrolling: Boolean = false
    private var isVerticalScrolling: Boolean = false

    init {
        // 获取屏幕宽度
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        mScreenWidth = dm.widthPixels
        mScroller = Scroller(context)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.e("tag", "ACTION_DOWN")
                performActionDown(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e("tag", "ACTION_MOVE")
                return performActionMove(ev)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.e("tag", "ACTION_UP")
                return performActionUp(ev)
            }
            else -> {
            }
        }
        return super.onTouchEvent(ev)
    }

    /**
     * 处理action_down事件
     */
    private fun performActionDown(ev: MotionEvent) {
        isLeftScrolling = false
        isVerticalScrolling = false
        startScrolling = false
        if (shouldTurnToNormal()) {
            turnToNormal()
            shouldOnlyTouchDown = true
            return
        }

        mDownX = ev.x.toInt()
        mDownY = ev.y.toInt()

        //有头时 头不能左划
        if (headerViewsCount == 1 && pointToPosition(mDownX, mDownY) == 0) {
            return
        }
        //有尾时 尾不能左划
        if (footerViewsCount == 1 && pointToPosition(mDownX, mDownY) == count - 1) {
            return
        }
        // 获取当前点的item
        mPointChild = getChildAt(pointToPosition(mDownX, mDownY) - firstVisiblePosition) as ViewGroup?

        if (mPointChild == null) {
            return
        }
        // 获取删除按钮的宽度（itemView的固定格式，左右两块左边为内容 宽度全屏 右边为菜单）
        mDeleteBtnWidth = mPointChild!!.getChildAt(1).measuredWidth
        mLayoutParams = mPointChild!!.getChildAt(0)
                .layoutParams as LinearLayout.LayoutParams
        mLayoutParams!!.width = mScreenWidth
        mPointChild!!.getChildAt(0).layoutParams = mLayoutParams
    }

    /**
     * 处理action_move事件
     */
    private fun performActionMove(ev: MotionEvent): Boolean {
        //有头时 头不能左划
        if (headerViewsCount == 1 && pointToPosition(mDownX, mDownY) == 0) {
            return super.onTouchEvent(ev)
        }
        //有尾时 尾不能左划
        if (footerViewsCount == 1 && pointToPosition(mDownX, mDownY) == count - 1) {
            return super.onTouchEvent(ev)
        }
        if (shouldOnlyTouchDown) {
            return true
        }
        if (mPointChild == null) {
            return super.onTouchEvent(ev)
        }
        val nowX = ev.x.toInt()
        val nowY = ev.y.toInt()

        val offsetX = Math.abs(nowX - mDownX)
        val offsetY = Math.abs(nowY - mDownY)
        if (!startScrolling) {
            startScrolling = offsetX > mTouchSlop || offsetY > mTouchSlop
        } else {
            if (!isVerticalScrolling && offsetX > offsetY) {
                // 如果向左滑动
                if (nowX < mDownX) {
                    isLeftScrolling = true
                    // 计算要偏移的距离
                    var scroll = nowX - mDownX
                    // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                    if (-scroll >= mDeleteBtnWidth) {
                        scroll = -mDeleteBtnWidth
                    }
                    mPointChild!!.scrollTo(-scroll, 0)
                }
            } else {
                isVerticalScrolling = true
            }
        }
        return isLeftScrolling || super.onTouchEvent(ev)
    }

    /**
     * 处理action_up事件
     */
    private fun performActionUp(ev: MotionEvent): Boolean {
        isLeftScrolling = false
        isVerticalScrolling = false
        startScrolling = false

        if (shouldOnlyTouchDown) {
            shouldOnlyTouchDown = false
            return true
        }

        //有头时 头不能左划
        if (headerViewsCount == 1 && pointToPosition(mDownX, mDownY) == 0) {
            return super.onTouchEvent(ev)
        }
        //有尾时 头不能左划
        if (footerViewsCount == 1 && pointToPosition(mDownX, mDownY) == count - 1) {
            return super.onTouchEvent(ev)
        }

        if (mPointChild == null) {
            return super.onTouchEvent(ev)
        }

        mLastPointChild = mPointChild
        // 偏移量大于button的一半，则显示button
        // 否则恢复默认
        val scrollX = mPointChild!!.scrollX

        if (scrollX >= mDeleteBtnWidth / 2) {
            mScroller.startScroll(scrollX, 0, mDeleteBtnWidth - scrollX, 0)
            invalidate()
        } else {
            turnToNormal()
        }
        return super.onTouchEvent(ev)
    }

    /**
     * 变为正常状态
     */
    fun turnToNormal() {
        if (mLastPointChild == null) {
            return
        }
        val scrollX = mLastPointChild!!.scrollX
        Log.e("tag", "scrollX   $scrollX")
        mScroller.startScroll(scrollX, 0, -scrollX, 0)
        invalidate()
    }

    private fun shouldTurnToNormal(): Boolean {
        return mLastPointChild != null && mLastPointChild!!.scrollX != 0
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mLastPointChild!!.scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }


}
