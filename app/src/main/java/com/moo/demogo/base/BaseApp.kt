package com.moo.demogo.base

import android.app.Application
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
    }
}
