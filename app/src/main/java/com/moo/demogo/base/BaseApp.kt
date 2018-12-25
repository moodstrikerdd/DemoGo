package com.moo.demogo.base

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.moo.demogo.mainframe.exception.ExceptionCatcher
import com.moo.demogo.utils.SPUtils
import kotlin.properties.Delegates

/**
 * @author moodstrikerdd
 * @date 2018/3/20
 * @lable BaseApp
 */

class BaseApp : Application() {
    companion object {
        var instance: BaseApp by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SPUtils.init(this)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionCatcher)
    }
}
