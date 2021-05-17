package com.moo.demogo.utils

import android.util.Log
import android.widget.Toast
import com.moo.demogo.base.BaseApp
import com.moo.demogo.bean.BaseBean
import com.moo.demogo.http.CallBack
import com.moo.demogo.http.ExceptionHandle
import com.moo.demogo.http.RetrofitHelper
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.JobCancellationException
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label 扩展函数
 */
fun loge(tag: String = "DemoGo", message: String?) {
    Log.e(tag, message ?: tag)
}

fun loge(message: String?) {
    Log.e("DemoGo", message ?: "DemoGo")
}

fun toast(message: String?) {
    Toast.makeText(BaseApp.instance, message ?: "", Toast.LENGTH_LONG).show()
}

fun tryCatch(catchBlock: (Throwable) -> Unit = { it.printStackTrace() }, tryBlock: () -> Unit) {
    try {
        tryBlock()
    } catch (_: JobCancellationException) {

    } catch (t: Throwable) {
        catchBlock(t)
    }
}

fun <T> doHttp(createApi: () -> Deferred<BaseBean<T>>, callBack: CallBack<T>) {
    launch(UI) {
        try {
            val deferred = createApi()
            val await = deferred.await()
            RetrofitHelper.handleResult(await, callBack)
        } catch (t: Throwable) {
            callBack.onFailed(ExceptionHandle.handleException(t))
        }
    }
}
