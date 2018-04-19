package com.moo.demogo.http

import android.util.Log
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException


/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label 统一处理error
 */
class ExceptionHandle {
    companion object {

        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        fun handleException(e: Throwable): ServiceException {
            val ex: ServiceException
            Log.i("tag", "e.toString = " + e.toString())
            if (e is HttpException) {
                ex = ServiceException(Error.HTTP_ERROR)
                when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE ->
                        ex.msg = "网络错误"
                    else -> ex.msg = "网络错误"
                }
                return ex
            } else if (e is ServiceException) {
                return e
            } else if (e is JsonParseException || e is JSONException) {
                ex = ServiceException(Error.PARSE_ERROR)
                ex.msg = "解析错误"
                return ex
            } else if (e is ConnectException) {
                ex = ServiceException(Error.NETWORD_ERROR)
                ex.msg = "连接失败"
                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                ex = ServiceException(Error.SSL_ERROR)
                ex.msg = "证书验证失败"
                return ex
            } else {
                ex = ServiceException(Error.UNKNOWN)
                ex.msg = "未知错误"
                return ex
            }
        }
    }
}