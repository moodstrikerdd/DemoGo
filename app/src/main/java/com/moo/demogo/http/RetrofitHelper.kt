package com.moo.demogo.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.moo.demogo.bean.BaseBean
import com.moo.demogo.utils.loge
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label 单例Retrofit Retrofit帮助类
 */
object RetrofitHelper {
    private const val BASE_URL = "http://www.wanandroid.com"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L
    private const val TAG = "Retrofit:"
    private const val DATA_FRONT = "Data:"
    //实例化api
    val api: ApiService by lazy {
        getService(ApiService::class.java)
    }
    //实例化Gson，用于控制台格式化输出json
    private val gson: Gson by lazy {
        GsonBuilder().setPrettyPrinting().create()
    }
    //实例化Retrofit
    private val mRetrofit: Retrofit by lazy {
        Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(OkHttpClient().newBuilder().apply {
                connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                addInterceptor {
                    val request = it.request()
                    val newBuilder = request.newBuilder()
                    //请求预处理，此处没做处理
                    it.proceed(newBuilder.build())
                }
                //log拦截器一定要最后添加，否则后面添加的拦截器修改的内容不会打印
                addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    loge(TAG, if (it.startsWith("{")) {
                        //控制台格式化输出json
                        val fromJson = gson.fromJson(it, Any::class.java)
                        DATA_FRONT + gson.toJson(fromJson)
                    } else {
                        DATA_FRONT + it
                    })
                }))
            }.build())
                    //添加gson转换器
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加Kotlin 协程适配器
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build()
    }

    private fun <T> getService(service: Class<T>): T {
        return mRetrofit.create(service)
    }

    fun <T> handleResult(baseBean: BaseBean<T>, callback: CallBack<T>) {
        //以http://www.wanandroid.com api例子，BaseBean根据自己实际情况定义，处理逻辑也
        if (baseBean.errorCode != 0 || baseBean.data == null) {
            //自定义异常
            throw ServiceException(baseBean.errorCode, baseBean.errorMsg)
        } else {
            callback.onSuccess(baseBean.data!!)
        }
    }
}
