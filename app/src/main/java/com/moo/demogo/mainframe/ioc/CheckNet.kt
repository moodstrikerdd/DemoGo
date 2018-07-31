package com.moo.demogo.mainframe.ioc

/**
 * @author moodstrikerdd
 * @date 2018/7/30
 * @label 检查网络
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CheckNet(vararg val value: Int)