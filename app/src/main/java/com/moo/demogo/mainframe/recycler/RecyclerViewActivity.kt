package com.moo.demogo.mainframe.recycler

import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.mainframe.snaphelper.GallerySnapHelper
import com.moo.demogo.mainframe.snaphelper.GravitySnapHelper
import com.moo.demogo.utils.AppUtils
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.android.synthetic.main.item_recyclerview.view.*

/**
 * @ClassName:      RecyclerViewActivity
 * @Author:         moodstrikerdd
 * @CreateDate:     2020/1/2 17:36
 * @Description:    java类作用描述
 */
class RecyclerViewActivity : BaseActivity() {
    private val data1 = arrayListOf<String>()
    private val data2 = arrayListOf<List<String>>()
    private val scrollerViews = arrayListOf<NestedScrollView>()
    private var isFirstScroll = true
    private val listenerScroller = NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        Log.e("tag", "scrollX:$scrollX  scrollY:$scrollY  oldScrollX:$oldScrollX  oldScrollY:$oldScrollY  ")
        if (isFirstScroll) {
            isFirstScroll = false
            for (i in 0 until scrollerViews.size) {
                if (scrollerViews[i] != v) {
                    scrollerViews[i].scrollBy(0, scrollY - oldScrollY)
                }
            }
            isFirstScroll = true
        }
    }

    override fun initData() {
        for (j in 0..10) {
            val data = arrayListOf<String>()
            for (i in 1..30) {
                data.add("data:$i")
            }
            if (j == 0) {
                data1.addAll(data)
            }
            data2.add(data)
        }

        rv1.adapter.notifyDataSetChanged()
        rv2.adapter.notifyDataSetChanged()
    }

    override fun initView() {
        rv1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv1.adapter = object : CommonAdapter<String>(this, R.layout.item_text, data1) {
            override fun convert(holder: ViewHolder, t: String) {
                val text = holder.getView<TextView>(R.id.text1)
                text.gravity = Gravity.CENTER
                text.text = t
            }
        }
        rv1.isNestedScrollingEnabled = false
        addRecyclerViews(nsv1)

        GravitySnapHelper(Gravity.START).attachToRecyclerView(rv2)
        rv2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isFirstScroll = false
                for (i in 0 until rv2.childCount) {
                    val child = rv2.getChildAt(i)
                    if (child != null && child.nsv3.scrollY != nsv1.scrollY) {
                        child.nsv3.scrollTo(0, nsv1.scrollY)
                    }
                }
                isFirstScroll = true
            }
        })
        rv2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv2.adapter = object : CommonAdapter<List<String>>(this, R.layout.item_recyclerview, data2) {
            override fun convert(holder: ViewHolder, t: List<String>) {
                holder.setText(R.id.tvTitle, t[0])
                holder.itemView.layoutParams.width = AppUtils.getScreenWidth(this@RecyclerViewActivity) / 4
                val rv3 = holder.getView<RecyclerView>(R.id.rv3)
                rv3.isNestedScrollingEnabled = false
                val nsv3 = holder.getView<NestedScrollView>(R.id.nsv3)
                rv3.layoutManager = LinearLayoutManager(this@RecyclerViewActivity, LinearLayoutManager.VERTICAL, false)
                rv3.adapter = object : CommonAdapter<String>(this@RecyclerViewActivity, R.layout.item_text, t.subList(1, t.size)) {
                    override fun convert(holder: ViewHolder, t: String) {
                        val text = holder.getView<TextView>(R.id.text1)
                        text.gravity = Gravity.CENTER
                        text.text = t
                    }
                }
                addRecyclerViews(nsv3)
            }
        }
    }

    private fun addRecyclerViews(nestedScrollView: NestedScrollView) {
        if (scrollerViews.contains(nestedScrollView)) {
            return
        }
        scrollerViews.add(nestedScrollView)
        nestedScrollView.setOnScrollChangeListener(listenerScroller)
    }

    override fun getLayoutId() = R.layout.activity_recyclerview
}