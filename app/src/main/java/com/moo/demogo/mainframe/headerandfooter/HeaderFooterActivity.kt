package com.moo.demogo.mainframe.headerandfooter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.adapter.recyclerview.HeaderFooterAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_header_footer.*

class HeaderFooterActivity : BaseActivity() {
    private val data = arrayListOf<String>()
    private var adapter: HeaderFooterAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_header_footer
    }

    override fun initView() {
        adapter = object : HeaderFooterAdapter(this, object : CommonAdapter<String>(this, R.layout.item_side_slip, data) {
            override fun convert(holder: ViewHolder, t: String) {
                holder.setText(R.id.tvItemName, t)
            }
        }) {
            override fun convertFooterViews(footerHolders: SparseArrayCompat<ViewHolder>) {
                for (index in 0 until footerHolders.size()) {
                    footerHolders.valueAt(index).setText(R.id.tvName, "footer---${index + 1}")
                }
            }

            override fun convertHeaderViews(headerHolders: SparseArrayCompat<ViewHolder>) {
                for (index in 0 until headerHolders.size()) {
                    headerHolders.valueAt(index).setText(R.id.tvName, "header---${index + 1}")
                }
            }
        }
        adapter?.addHeaderView(R.layout.layout_header_footer)
        adapter?.addHeaderView(R.layout.layout_header_footer)
        adapter?.addHeaderView(R.layout.layout_header_footer)
        adapter?.addFooterView(R.layout.layout_header_footer)
        adapter?.addFooterView(R.layout.layout_header_footer)
        adapter?.addFooterView(R.layout.layout_header_footer)

        rvContent.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvContent.adapter = adapter
    }

    override fun initData() {
        for (i in 0..20) {
            data.add("itemView" + (i + 1))
        }
        adapter?.notifyDataSetChanged()
    }

}