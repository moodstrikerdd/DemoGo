package com.moo.demogo.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @ClassName: GridSpacingItemDecoration
 * @Author: moodstrikerdd
 * @CreateDate: 2019/12/19 10:45
 * @Description: java类作用描述
 */
class GridSpacingItemDecoration(//列数
        private val spanCount: Int, //间隔
        private val spacing: Int, //是否包含边缘
        private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {

        //这里是关键，需要根据你有几列来判断
        // item position
        val position = parent.getChildAdapterPosition(view)
        // item column
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}
