package com.moo.demogo.mainframe.snaphelper

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.View

/**
 * @ClassName: TopSnapHelper
 * @Author: moodstrikerdd
 * @CreateDate: 2020/1/2 18:13
 * @Description: java类作用描述
 */
class TopSnapHelper : SnapHelper() {

    private var mVerticalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, view: View): IntArray? {
        val out = IntArray(2)
        out[0] = 0
        if (layoutManager.canScrollVertically()) {
            out[1] = getVerticalHelper(layoutManager).getDecoratedStart(view)
        } else {
            out[1] = 0
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return if (layoutManager.canScrollVertically()) {
            findTopView(layoutManager, getVerticalHelper(layoutManager))
        } else null
    }

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
        val itemCount = layoutManager.itemCount
        if (itemCount == 0) {
            return -1
        } else {
            var mStartMostChildView: View? = null
            if (layoutManager.canScrollVertically()) {
                mStartMostChildView = this.findStartView(layoutManager, this.getVerticalHelper(layoutManager))
            }

            if (mStartMostChildView == null) {
                return -1
            } else {
                val centerPosition = layoutManager.getPosition(mStartMostChildView)
                return if (centerPosition == -1) {
                    -1
                } else {
                    val forwardDirection = if (layoutManager.canScrollHorizontally()) {
                        velocityX > 0
                    } else {
                        velocityY > 0
                    }

                    if (forwardDirection) centerPosition + 1 else centerPosition
                }
            }
        }
    }

    private fun findTopView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        } else {
            val manager = layoutManager as LinearLayoutManager
            val firstPosition = manager.findFirstVisibleItemPosition()
            val firstView = manager.findViewByPosition(firstPosition) ?: return null
            val lastPosition = manager.findLastCompletelyVisibleItemPosition()
            //滚动到最后不用对齐
            if (lastPosition == manager.itemCount) return null
            val start = Math.abs(helper.getDecoratedStart(firstView))
            return if (start >= helper.getDecoratedMeasurement(firstView) / 2) {
                manager.findViewByPosition(firstPosition + 1)
            } else firstView
        }
    }

    private fun findStartView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        } else {
            var closestChild: View? = null
            var startest = Integer.MAX_VALUE
            for (i in 0 until childCount) {
                val child = layoutManager.getChildAt(i)
                val childStart = helper.getDecoratedStart(child)
                if (childStart < startest) {
                    startest = childStart
                    closestChild = child
                }
            }

            return closestChild
        }
    }


    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (this.mVerticalHelper == null) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }

        return this.mVerticalHelper!!
    }
}
