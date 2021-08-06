package com.moo.demogo.mainframe.customerview

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.commonView.CustomerPieChartView
import kotlinx.android.synthetic.main.activity_customer_view.*

/**
 * @ClassName:      CustomerViewActivity
 * @Author:         moodstrikerdd
 * @CreateDate:     2021/6/4 15:46
 * @Description:    java类作用描述
 */
class CustomerViewActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_customer_view

    private val intData = arrayListOf<Int>()
    private val smallData = arrayListOf(
            R.mipmap.market_chart_open_menu,
            R.mipmap.market_chart_enlarge)
    private val largeData = arrayListOf(
            R.mipmap.market_chart_close_menu,
            R.mipmap.market_chart_subtract,
            R.mipmap.market_chart_plus,
            R.mipmap.market_chart_left,
            R.mipmap.market_chart_right,
            R.mipmap.market_chart_exit_full)

    class Data(val p: Float, val t: String, val numberDisplay: String) : CustomerPieChartView.FormData {
        override fun getTitle() = t

        override fun getPercent() = p

        override fun getNumberDisplayText() = numberDisplay

    }

    private val data = arrayListOf<CustomerPieChartView.FormData>()
    override fun initData() {
        data.add(Data(0.5f, "主力流出", "2494.12"))
        data.add(Data(0f, "散户流出", "2494.12"))
        data.add(Data(0.33f, "散户流入", "2494.12"))
        data.add(Data(0.17f, "主力流入", "2494.12"))
        Handler().postDelayed({ cpcData.setData(data) }, 1000)

        rvContent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvContent.adapter = object : CommonAdapter<Int>(this,
                R.layout.item_rv_img,
                intData
        ) {
            override fun convert(holder: ViewHolder, t: Int) {
                holder.setImageResource(R.id.image, t)
                holder.setOnClickListener(R.id.image) {
                    when(t) {
                        R.mipmap.market_chart_open_menu -> {
                            if (rvContent.isAnimating) {
                                return@setOnClickListener
                            }
                            intData.clear()
                            intData.addAll(largeData)
                            rvContent.adapter?.notifyItemChanged(0)
                            rvContent.adapter?.notifyItemRangeInserted(1, intData.size - 2)
                        }
                        R.mipmap.market_chart_close_menu->{
                            if (rvContent.isAnimating) {
                                return@setOnClickListener
                            }
                            val oldSize = intData.size
                            intData.clear()
                            intData.addAll(smallData)
                            rvContent.adapter?.notifyItemChanged(0)
                            rvContent.adapter?.notifyItemRangeRemoved(1,oldSize - 2)
                        }
                    }
                }
            }
        }

        intData.addAll(smallData)
        rvContent.adapter?.notifyDataSetChanged()
    }

    override fun initView() {
    }
}