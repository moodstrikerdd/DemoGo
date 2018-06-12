package com.moo.demogo.mainframe.diffutil

import android.support.v7.widget.LinearLayoutManager
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.DiffUtilCommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.bean.HomeBean
import com.moo.demogo.http.CallBack
import com.moo.demogo.http.RetrofitHelper
import com.moo.demogo.mainframe.webview.WebViewActivity
import com.moo.demogo.utils.doHttp
import kotlinx.android.synthetic.main.activity_diff_util.*

class DiffUtilActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_diff_util
    private var currentPage = 0

    private val data = arrayListOf<HomeBean.HomeListBean>()
    private val adapter: DiffUtilCommonAdapter<HomeBean.HomeListBean> by lazy {
        object : DiffUtilCommonAdapter<HomeBean.HomeListBean>(this, R.layout.item_diff, data) {
            override fun getChange(oldData: HomeBean.HomeListBean, newData: HomeBean.HomeListBean): HomeBean.HomeListBean {
                return newData
            }

            override fun areItemsSame(oldData: HomeBean.HomeListBean, newData: HomeBean.HomeListBean) = oldData.id == newData.id

            override fun areContentsSame(oldData: HomeBean.HomeListBean, newData: HomeBean.HomeListBean) = oldData.id == newData.id

            override fun convert(holder: ViewHolder, t: HomeBean.HomeListBean) {
                holder.setText(R.id.tvTitle, t.title)
                holder.setText(R.id.tvTips, t.desc)
                holder.setText(R.id.tvAuthor, t.author)
                holder.setText(R.id.tvDate, t.niceDate)
                holder.itemView.setOnClickListener { WebViewActivity.intentStart(this@DiffUtilActivity, t.link, t.title) }
            }
        }
    }

    override fun initView() {
        rvContent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvContent.adapter = adapter
        refresh.setOnRefreshListener { initData() }
    }

    override fun initData() {
        doHttp({
            RetrofitHelper.api.getHomeBean(currentPage)
        }, object : CallBack<HomeBean>() {
            override fun onSuccess(t: HomeBean) {
                adapter.setData(t.datas)
                refresh.isRefreshing = false
            }

            override fun onFailed(throwable: Throwable) {
                super.onFailed(throwable)
                refresh.isRefreshing = false
            }
        })

    }


}
