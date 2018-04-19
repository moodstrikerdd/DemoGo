package com.moo.demogo.http

import com.moo.demogo.utils.toast

/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label 回调
 */
abstract class CallBack<in T> {
    abstract fun onSuccess(t: T)

    open fun onFailed(throwable: Throwable) {
        val handleException = ExceptionHandle.handleException(throwable)
        toast(handleException.msg)
    }
}