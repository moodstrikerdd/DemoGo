package com.moo.demogo.mainframe.coroutines

import android.util.Log
import com.moo.adapter.ViewHolder
import com.moo.adapter.abslistview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coroutines.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.concurrent.TimeUnit

class CoroutinesActivity : BaseActivity() {
    private val TAG = "CoroutinesActivity"

    private var adapter: CommonAdapter<String>? = null
    private val data = arrayListOf<String>()

    override fun getLayoutId(): Int = R.layout.activity_coroutines

    override fun initView() {
        adapter = object : CommonAdapter<String>(this, R.layout.item_side_slip, data) {
            override fun convert(holder: ViewHolder, t: String) {
                holder.setText(R.id.tvItemName, t)
            }
        }
        lvContent.adapter = adapter
    }

    override fun initData() {
        test5()
    }

    private suspend fun getData(): List<String> = runBlocking {
        val list = arrayListOf<String>()
        for (i in 0..20) {
            list.add("item===${i + 1}")
        }
        delay(3000)
        list
    }

    fun test5() = async(UI) {
        val job1 = async(CommonPool) {
            Log.e(TAG, "job1 start")
            delay(3000, TimeUnit.MILLISECONDS)
            Log.e(TAG, "job1 end")
        }
        Log.e(TAG, "1")
        val job2 = async(CommonPool) {
            Log.e(TAG, "job2 start,wait job1 end")
            job1.join()
            Log.e(TAG, "job2 end")
        }
        Log.e(TAG, "2")
        job2.join()
        Log.e(TAG, "3")

    }

    fun test3() = async {  }


    fun test2() = launch(UI) {
        refresh.isRefreshing = true
        val job = launch(CommonPool) {
            data.addAll(getData())
        }
        job.join()
        refresh.isRefreshing = false
        adapter?.notifyDataSetChanged()
    }


    fun test() = runBlocking {
        val job = launch(CommonPool) {
            data.addAll(getData())
        }

        val job2 = launch {
            Log.e("coroutines", "job1 loading")
            job.join()
            Log.e("coroutines", "job1 stop")
            adapter?.notifyDataSetChanged()
        }
        Log.e("coroutines", "job2 loading")
        job2.join()
        Log.e("coroutines", "job2 stop")
    }

}