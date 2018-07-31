package com.moo.demogo.mainframe.ioc

/**
 * @author moodstrikerdd
 * @date 2018/7/30
 * @label 点击事件
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class OnClick(vararg val value: Int)
