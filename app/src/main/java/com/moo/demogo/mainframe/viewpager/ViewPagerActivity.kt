package com.moo.demogo.mainframe.viewpager

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.moo.adapter.ViewHolder
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.utils.AppUtils
import kotlinx.android.synthetic.main.activity_view_pager.*

class ViewPagerActivity : BaseActivity() {
    private val data = arrayListOf<Int>()
    private val views = arrayListOf<ViewHolder>()

    override fun getLayoutId() = R.layout.activity_view_pager

    override fun initData() {

    }

    override fun initView() {
        data.add(R.mipmap.page1)
        views.add(ViewHolder.get(this@ViewPagerActivity, R.layout.layout_view_pager, null, false))
        data.add(R.mipmap.page2)
        views.add(ViewHolder.get(this@ViewPagerActivity, R.layout.layout_view_pager, null, false))
        data.add(R.mipmap.page3)
        views.add(ViewHolder.get(this@ViewPagerActivity, R.layout.layout_view_pager, null, false))
        data.add(R.mipmap.page4)
        views.add(ViewHolder.get(this@ViewPagerActivity, R.layout.layout_view_pager, null, false))

        vpContent.apply {
            //设置预加载的数量是3，这个值默认是1
            offscreenPageLimit = 3
            val screenWidth = AppUtils.getScreenWidth(this@ViewPagerActivity)
            setPadding(screenWidth / 3, 0, screenWidth / 3, 0)
            clipToPadding = false
//            pageMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
            adapter = object : PagerAdapter() {
                override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                    return if (data.size == 0) {
                        super.instantiateItem(container, position)
                    } else {
                        val newPosition = position % data.size
                        val viewHolder = views[newPosition]
                        viewHolder.setImageResource(R.id.ivContent, data[newPosition])
                        container?.addView(viewHolder.convertView)
                        viewHolder.convertView
                    }
                }

                override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                    container?.removeView(views[position].convertView)
                }

                override fun isViewFromObject(view: View?, `object`: Any?) = view == `object`

                override fun getCount(): Int = data.size
            }

            currentItem = 1

            setPageTransformer(false) { page, position ->
                val newPosition = position - vpContent.paddingLeft.toFloat() / (vpContent.width - vpContent.paddingLeft * 2).toFloat()
                when {
                    newPosition < -1f -> {
                        page.scaleX = 0.50f
//                        page.rotationY = 30f
                    }
                    newPosition > -1 && newPosition <= 0 -> {
                        val scaleX = 1 + 0.5f * newPosition
                        page.scaleX = scaleX
//                        page.rotationY = 30 * Math.abs(newPosition)
                    }
                    newPosition > 0 && newPosition <= 1f -> {
                        val scaleX = 1 - 0.5f * newPosition
                        page.scaleX = scaleX
//                        page.rotationY = -30 * Math.abs(newPosition)
                    }
                    newPosition > 1f -> {
//                        page.rotationY = -30f
                        page.scaleX = 0.50f
                    }
                }
            }
        }
    }
}
