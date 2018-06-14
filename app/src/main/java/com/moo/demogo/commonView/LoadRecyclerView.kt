package com.moo.demogo.commonView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.moo.adapter.recyclerview.HeaderFooterAdapter
import com.moo.demogo.R
import com.moo.demogo.utils.loge

/**
 * @author moodstrikerdd
 * @date 2018/6/7
 * @label
 */
class LoadRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        if (mHeaderFooterAdapter.footerHolders.size() > 0) {
            val itemView = mHeaderFooterAdapter.footerHolders.valueAt(0).itemView

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    itemView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT)
                    mHeaderFooterAdapter.notifyItemChanged(mHeaderFooterAdapter.itemCount - 1)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    itemView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,0)
                    mHeaderFooterAdapter.notifyItemChanged(mHeaderFooterAdapter.itemCount - 1)
                }
            }
            loge(message = "rect :${canScrollVertically(-1)}  rect :${canScrollVertically(1)}")
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return super.onTouchEvent(e)
    }

    private lateinit var mHeaderFooterAdapter: HeaderFooterAdapter

    override fun setAdapter(adapter: Adapter<*>?) {
        mHeaderFooterAdapter = HeaderFooterAdapter(context, adapter)
        mHeaderFooterAdapter.addFooterView(R.layout.layout_header_footer)
        super.setAdapter(mHeaderFooterAdapter)
    }


}