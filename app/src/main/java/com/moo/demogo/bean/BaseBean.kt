package com.moo.demogo.bean

/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label
 */


/**
 * data :
 * errorCode : 0
 * errorMsg :
 */
data class BaseBean<T>(var data: T? = null,
                       var errorCode: Int = 0,
                       var errorMsg: String = "")



