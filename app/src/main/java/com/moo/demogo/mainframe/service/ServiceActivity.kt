package com.moo.demogo.mainframe.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.view.View
import android.widget.Button
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.event.BaseEvent
import com.moo.demogo.utils.loge
import kotlinx.android.synthetic.main.activity_service.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ServiceActivity : BaseActivity() {
    private val eventBus: BaseEvent<String> by lazy {
        BaseEvent<String>()
    }
    private val menus = arrayListOf<String>()

    private val conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            eventBus.date = "Service ServiceConnection"
            loge(message = eventBus.date)
            EventBus.getDefault().post(eventBus)
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            eventBus.date = "Service onServiceConnected"
            loge(message = eventBus.date)
            EventBus.getDefault().post(eventBus)
        }
    }

    override fun getLayoutId() = R.layout.activity_service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    override fun initData() {
        menus.add("startService")
        menus.add("bindService")
        menus.add("unBindService")
        menus.add("stopService")
        rvContent.adapter.notifyDataSetChanged()
    }

    override fun initView() {
        rvContent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvContent.adapter = object : CommonAdapter<String>(this, R.layout.item_service_manu_button, menus) {
            override fun convert(holder: ViewHolder, t: String) {
                val btnService = holder.getView<Button>(R.id.btnService)
                btnService.text = t
                btnService.setOnClickListener(onClickListener)
            }
        }
        LinearSnapHelper().attachToRecyclerView(rvContent)
    }

    private val onClickListener = View.OnClickListener {
        it as Button
        when (it.text) {
            menus[0] -> {
                val intent = Intent(this, CustomService::class.java)
                startService(intent)
            }
            menus[1] -> {
                val intent = Intent(this, CustomService::class.java)
                bindService(intent, conn, Service.BIND_AUTO_CREATE)
            }
            menus[2] -> {
                unbindService(conn)
            }
            menus[3] -> {
                val intent = Intent(this, CustomService::class.java)
                stopService(intent)
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BaseEvent<String>) {
        if (event.date != null) {
            tvContent.append(event.date + "\n")
        }
    }
}
