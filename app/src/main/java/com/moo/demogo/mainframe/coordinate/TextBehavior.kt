package com.moo.demogo.mainframe.coordinate

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlin.math.abs

/**
 * @ClassName:      TextBehavior
 * @Author:         moodstrikerdd
 * @CreateDate:     2020/10/20 11:46
 * @Description:    java类作用描述
 */

class TextBehavior : CoordinatorLayout.Behavior<TextView> {
    constructor() : super()

    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(params)
    }

    override fun onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams()
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: TextView?, dependency: View?): Boolean {
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout?, child: TextView?, dependency: View?) {
        super.onDependentViewRemoved(parent, child, dependency)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: TextView, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: TextView, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        val layoutManager = (target as RecyclerView).layoutManager as LinearLayoutManager
        val first = layoutManager.findFirstCompletelyVisibleItemPosition()

        if ((dy < 0 && first == 0 && child.translationY < 0) ||
                (dy > 0)) {
            var float = child.translationY - dy.toFloat()
            if (float > 0) {
                float = 0f
            }
            Log.e("Behavior", "TextBehavior : dx:${dx}  dy:${dy} child.translationY${child.translationY} float${float}")
            if (-float <= child.measuredHeight) {
                child.translationY = float
                consumed[1] = dy
            } else {
                child.translationY = -child.measuredHeight.toFloat()
            }
        }
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: TextView, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

    }
}