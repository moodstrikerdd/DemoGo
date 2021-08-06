package com.moo.demogo.utils

import android.content.res.Resources
import android.provider.Contacts
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import com.moo.demogo.base.BaseApp
import com.moo.demogo.bean.BaseBean
import com.moo.demogo.http.CallBack
import com.moo.demogo.http.ExceptionHandle
import com.moo.demogo.http.RetrofitHelper
import kotlinx.coroutines.*
import java.lang.Exception

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
    } catch (_: Exception) {

    } catch (t: Throwable) {
        catchBlock(t)
    }
}

fun <T> doHttp(createApi: () -> Deferred<BaseBean<T>>, callBack: CallBack<T>) {
    GlobalScope.launch(Dispatchers.Main) {
        try {
            val deferred = createApi()
            val await = deferred.await()
            RetrofitHelper.handleResult(await, callBack)
        } catch (t: Throwable) {
            callBack.onFailed(ExceptionHandle.handleException(t))
        }
    }
}

