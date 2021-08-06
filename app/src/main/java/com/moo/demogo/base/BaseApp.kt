package com.moo.demogo.base

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.text.TextUtils
import com.moo.demogo.mainframe.exception.ExceptionCatcher
import com.moo.demogo.utils.SPUtils
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlin.properties.Delegates


/**
 * @author moodstrikerdd
 * @date 2018/3/20
 * @lable BaseApp
 */

class BaseApp : Application() {
    private lateinit var refWatcher: RefWatcher

    companion object {
        var instance: BaseApp by Delegates.notNull()

        fun getRefWatcher(context: Context): RefWatcher {
            val leakApplication = context.applicationContext as BaseApp
            return leakApplication.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        val pid = android.os.Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (process in activityManager.runningAppProcesses) {
            if (process.pid == pid) {
                val currentProcessName = process.processName
                if (!TextUtils.isEmpty(currentProcessName) && !currentProcessName.contains(":")) {
                    //避免多次初始化APP
                    instance = this
                    SPUtils.init(this)
                    Thread.setDefaultUncaughtExceptionHandler(ExceptionCatcher)
                }
            }
        }
        refWatcher = setupLeakCanary()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else LeakCanary.install(this)
    }

}
