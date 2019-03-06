package com.moo.demogo.mainframe.sidesliplistview

import android.view.View
import android.widget.TextView
import com.moo.adapter.ViewHolder
import com.moo.adapter.abslistview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_side_slip.*
import org.jetbrains.anko.toast

class SideSlipActivity : BaseActivity() {
    private var adapter: CommonAdapter<String>? = null
    private val data = arrayListOf<String>()

    override fun getLayoutId(): Int = R.layout.activity_side_slip

    override fun initView() {
        adapter = object : CommonAdapter<String>(this, R.layout.item_side_slip, data) {
            override fun convert(holder: ViewHolder, t: String) {
                holder.setText(R.id.tvItemName, t)
                val onClickListener = View.OnClickListener {
                    toast((it as TextView).text).show()
                    lvContent.turnToNormal()
                }
                holder.setOnClickListener(R.id.tvDelete, onClickListener)
                holder.setOnClickListener(R.id.tvPay, onClickListener)
            }

        }
        lvContent.adapter = adapter

        refresh.setOnRefreshListener { initData() }
        val t = object :ThreadLocal<String>(){
            override fun initialValue(): String {
                return "abc"
            }
        }
    }

    override fun initData() {
        data.clear()
        for (i in 0..20) {
            data.add("item" + (i + 1))
        }
        refresh.isRefreshing = false
        adapter?.notifyDataSetChanged()
    }

}