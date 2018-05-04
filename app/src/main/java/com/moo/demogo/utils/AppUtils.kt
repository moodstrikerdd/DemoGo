package com.moo.demogo.utils

import android.annotation.SuppressLint
import android.content.Context

/**
 * @author moodstrikerdd
 * @date 2018/4/2
 * @label
 */
object AppUtils {

    /**
     * 获取状态栏高度
     */
    @SuppressLint("PrivateApi")
    fun getStatusHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(`object`).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    /**
     * 获取手机屏幕宽
     */
    fun getScreenWidth(context: Context) = context.resources.displayMetrics.widthPixels

    /**
     * 获取手机屏幕高
     */
    fun getScreenHeigth(context: Context) = context.resources.displayMetrics.heightPixels
}