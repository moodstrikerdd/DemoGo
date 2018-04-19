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
    val api: ApiService by lazy {
        getService(ApiService::class.java)
    }
    private val gson: Gson by lazy {
        GsonBuilder().setPrettyPrinting().create()
    }
    private val mRetrofit: Retrofit by lazy {
        Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(OkHttpClient().newBuilder().apply {
                connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    loge(TAG, if (it.startsWith("{")) {
                        val fromJson = gson.fromJson(it, Any::class.java)
                        DATA_FRONT + gson.toJson(fromJson)
                    } else {
                        DATA_FRONT + it
                    })
                }))
                addInterceptor {
                    val request = it.request()
                    val newBuilder = request.newBuilder()
                    it.proceed(newBuilder.build())
                }
            }.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build()
    }

    private fun <T> getService(service: Class<T>): T {
        return mRetrofit.create(service)
    }

    fun <T> handleResult(baseBean: BaseBean<T>, callback: CallBack<T>) {
        if (baseBean.errorCode != 0 || baseBean.data == null) {
            throw ServiceException(baseBean.errorCode, baseBean.errorMsg)
        } else {
            callback.onSuccess(baseBean.data)
        }
    }
}
