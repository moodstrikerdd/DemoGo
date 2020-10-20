package com.moo.demogo.mainframe.coordinate

import android.support.v7.widget.LinearLayoutManager
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coordinate.*

/**
 * @ClassName:      CoorDinatorLayoutDemoActivity
 * @Author:         moodstrikerdd
 * @CreateDate:     2020/10/20 11:23
 * @Description:    java类作用描述
 */

class CoordinatorLayoutDemoActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_coordinate
    override fun initData() {
    }

    override fun initView() {
        val data = arrayListOf<String>()
        for (i in 1..30) {
            data.add("item$i")
        }
        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.adapter = object : CommonAdapter<String>(this, R.layout.item_text, data) {
            override fun convert(holder: ViewHolder, t: String) {
                holder.setText(R.id.text1, t)
            }

        }
    }
}