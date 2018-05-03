package com.moo.demogo.mainframe.snaphelper

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_snaphelper.*

class SnaphelperActivity : BaseActivity() {
    private val data = arrayListOf<Int>()
    private val adapter: CommonAdapter<Int> by lazy {
        object : CommonAdapter<Int>(this, R.layout.layout_snap, data) {
            override fun convert(holder: ViewHolder, t: Int) {
                holder.setImageResource(R.id.ivContent, t)
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_snaphelper

    override fun initData() {
        for (index in 0..3) {
            data.add(R.mipmap.page1)
            data.add(R.mipmap.page2)
            data.add(R.mipmap.page3)
            data.add(R.mipmap.page4)
        }
        adapter.notifyDataSetChanged()
    }

    override fun initView() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvContent)
        rvContent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvContent.adapter = adapter
    }

}
