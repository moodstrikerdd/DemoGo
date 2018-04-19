package com.moo.demogo.http

/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label 自定义通用异常
 */
class ServiceException(code: Int, message: String = "") : Exception() {
    var code = 0
    var msg = ""

    init {
        this.code = code
        this.msg = message
    }
}