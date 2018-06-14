package com.moo.demogo.mainframe.diffutil

import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.ViewConfiguration
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.DiffUtilCommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.bean.HomeBean
import com.moo.demogo.http.CallBack
import com.moo.demogo.http.RetrofitHelper
import com.moo.demogo.mainframe.webview.WebViewActivity
import com.moo.demogo.utils.doHttp
import com.moo.demogo.utils.loge
import kotlinx.android.synthetic.main.activity_diff_util.*

class DiffUtilActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_diff_util
    private var currentPage = 0

    private val data = arrayListOf<HomeBean.HomeListBean>()
    private val adapter: DiffUtilCommonAdapter<HomeBean.HomeListBean> by lazy {
        object : DiffUtilCommonAdapter<HomeBean.HomeListBean>(this, R.layout.item_diff, data) {
            override fun getChange(oldData: HomeBean.HomeListBean, newData: HomeBean.HomeListBean): HomeBean.HomeListBean? {
                return null
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

    private val mTouchSlop: Int by lazy {
        ViewConfiguration.get(this).scaledTouchSlop
    }
    private var isShowLoading = false
    private var startLoading = false
    private var isLoadingMode = true;
    var startX = 0f
    var startY = 0f
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (startLoading) {
            return true
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = (ev.x - startX).toInt()
                var offsetY = (ev.y - startY).toInt()
                loge(message = "offsetY：$offsetY")
                if (isShowLoading) {
                    if (Math.abs(offsetX) < Math.abs(offsetY)) {
                        if (offsetY < -tvName.measuredHeight * 2) {
                            offsetY = -tvName.measuredHeight * 2
                            startLoading = true
                        }
                        if (offsetY > 0) {
                            offsetY = 0
                            isShowLoading = false
                            startX = 0f
                            startY = 0f
                        }
                        llContent.scrollTo(0, -offsetY / 2)
                    }
                    return true
                } else {
                    if (isLoadingMode && !rvContent.canScrollVertically(1)) {
                        if (!isShowLoading) {
                            startX = ev.x
                            startY = ev.y
                            isShowLoading = true
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isShowLoading) {
                    if (startLoading) {

                    } else {
                        llContent.scrollTo(0, 0)
                    }
                    isShowLoading = false
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun initData() {
        doHttp({
            RetrofitHelper.api.getHomeBean(currentPage++)
        }, object : CallBack<HomeBean>() {
            override fun onSuccess(t: HomeBean) {
                adapter.addData(t.datas)
                rvContent.adapter.notifyDataSetChanged()
                refresh.isRefreshing = false
            }

            override fun onFailed(throwable: Throwable) {
                super.onFailed(throwable)
                refresh.isRefreshing = false
            }
        })
    }


}
