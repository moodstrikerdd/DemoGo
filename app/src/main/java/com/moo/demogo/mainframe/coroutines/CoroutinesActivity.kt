package com.moo.demogo.mainframe.coroutines

import com.moo.adapter.ViewHolder
import com.moo.adapter.abslistview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.bean.HotWebBean
import com.moo.demogo.http.CallBack
import com.moo.demogo.http.RetrofitHelper
import com.moo.demogo.mainframe.webview.WebViewActivity
import com.moo.demogo.utils.doHttp
import kotlinx.android.synthetic.main.activity_coroutines.*
import org.jetbrains.anko.toast

class CoroutinesActivity : BaseActivity() {

    private var adapter: CommonAdapter<HotWebBean>? = null
    private val data = arrayListOf<HotWebBean>()

    override fun getLayoutId(): Int = R.layout.activity_coroutines

    override fun initView() {
        adapter = object : CommonAdapter<HotWebBean>(this, R.layout.item_side_slip, data) {
            override fun convert(holder: ViewHolder, t: HotWebBean) {
                holder.setText(R.id.tvItemName, t.name)
                holder.setOnClickListener(R.id.tvItemName) {
                    if (t.link == null) {
                        toast("跳转链接不存在")
                    } else {
                        WebViewActivity.intentStart(this@CoroutinesActivity, t.link!!, t.name)
                    }
                }
            }
        }
        lvContent.adapter = adapter
        refresh.setOnRefreshListener {
            initData()
        }
    }

    override fun initData() {
        refresh.isRefreshing = true
        doHttp({
            RetrofitHelper.api.getList()
        },
                object : CallBack<List<HotWebBean>>() {
                    override fun onSuccess(t: List<HotWebBean>) {
                        data.apply {
                            clear()
                            addAll(t)
                        }
                        refresh.isRefreshing = false
                        adapter?.notifyDataSetChanged()
                    }

                    override fun onFailed(throwable: Throwable) {
                        super.onFailed(throwable)
                        refresh.isRefreshing = false
                    }
                })
    }

}