package com.moo.demogo

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.moo.demogo.mainframe.sideSlipListView.SideSlipActivity
import com.moo.demogo.base.BaseActivity
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.adapter.recyclerview.RecycleViewDivider
import com.moo.demogo.bean.ActivityNameBean
import com.moo.demogo.mainframe.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {
    private val list = arrayListOf(
            ActivityNameBean("WebViewActivity", "WebView相关设置，入宽高自适应，shouldOverrideUrlLoading过时替代等", WebViewActivity::class.java),
            ActivityNameBean("SideSlipActivity", "侧滑ListView\nSwipeRefreshLayout冲突，NestedScrolling实现", SideSlipActivity::class.java)
    )

    private val newList = arrayListOf<ActivityNameBean>()

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initData() {
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = object : CommonAdapter<ActivityNameBean>(this, R.layout.item_main_menu, list) {
            override fun convert(holder: ViewHolder, t: ActivityNameBean) {
                holder.setText(R.id.name, t.name)
                holder.setText(R.id.message, t.describe)
                holder.convertView.setOnClickListener {
                    try {
                        val intent = Intent(this@MainActivity, t.className)
                        startActivity(intent)
                    } catch (e: Exception) {
                        toast("跳转失败！")
                    }
                }
            }
        }
        recyclerView.addItemDecoration(RecycleViewDivider(this, RecycleViewDivider.VERTICAL, 10, resources.getColor(R.color.dividerGray)))
    }
}
