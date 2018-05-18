package com.moo.demogo.mainframe.service

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.IBinder
import com.moo.demogo.event.BaseEvent
import com.moo.demogo.utils.loge
import org.greenrobot.eventbus.EventBus

/**
 * @author moodstrikerdd
 * @date 2018/5/11
 * @label
 */
class CustomService : Service() {
    private var binder: CustomBinder? = null

    private val eventBus: BaseEvent<String> by lazy {
        BaseEvent<String>()
    }

    override fun onCreate() {
        super.onCreate()
        eventBus.date = "Service onCreate"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
        binder = CustomBinder()
    }

    override fun onBind(intent: Intent?): IBinder? {
        eventBus.date = "Service onBind"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
        return binder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        eventBus.date = "Service onRebind"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        eventBus.date = "Service onUnbind"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        eventBus.date = "Service onStartCommand"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        eventBus.date = "Service onLowMemory"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        eventBus.date = "Service onTaskRemoved"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        eventBus.date = "Service onTrimMemory"
        loge(message = eventBus.date)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        eventBus.date = "Service onConfigurationChanged"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
    }

    override fun onDestroy() {
        super.onDestroy()
        eventBus.date = "Service onDestroy"
        loge(message = eventBus.date)
        EventBus.getDefault().post(eventBus)
    }

    class CustomBinder : Binder()
}