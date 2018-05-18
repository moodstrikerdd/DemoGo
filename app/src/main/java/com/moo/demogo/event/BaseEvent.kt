package com.moo.demogo.event

/**
 * @author moodstrikerdd
 * @date 2018/5/11
 * @label
 */
data class BaseEvent<T>(var code: Int = 0,
                        var date: T? = null,
                        var message: String? = null)