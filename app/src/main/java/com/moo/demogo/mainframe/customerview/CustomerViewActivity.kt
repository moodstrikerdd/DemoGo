package com.moo.demogo.mainframe.customerview

import android.os.Handler
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
        Handler().postDelayed({cpcData.setData(data)},1000)
    }

    override fun initView() {
    }
}