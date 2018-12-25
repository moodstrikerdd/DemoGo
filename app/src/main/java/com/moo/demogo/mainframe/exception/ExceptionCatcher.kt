package com.moo.demogo.mainframe.exception

import com.moo.demogo.utils.loge

object ExceptionCatcher : Thread.UncaughtExceptionHandler {
    private val TAG = "ExceptionCatcher"

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        loge(TAG, thread.name)
        throwable.printStackTrace()
    }
}