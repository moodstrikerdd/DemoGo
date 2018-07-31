package com.moo.demogo.mainframe.ioc

/**
 * @author moodstrikerdd
 * @date 2018/7/30
 * @label 限制重复点击
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LimitRepeatClick(val value: Long = 1000)