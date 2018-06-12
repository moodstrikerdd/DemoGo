package com.moo.demogo.commonView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.moo.adapter.recyclerview.HeaderFooterAdapter

/**
 * @author moodstrikerdd
 * @date 2018/6/7
 * @label
 */
class LoadRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {


    private lateinit var adapter: HeaderFooterAdapter

    override fun setAdapter(adapter: Adapter<*>?) {
        this.adapter = HeaderFooterAdapter(context, adapter)
        super.setAdapter(this.adapter)
    }
}