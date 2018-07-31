package com.moo.demogo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build


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
     * 跳转到应用设置界面
     */
    @SuppressLint("ObsoleteSdkInt")
    fun getAppDetailSettingIntent(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(localIntent)
    }


    /**
     * 获取手机屏幕宽
     */
    fun getScreenWidth(context: Context) = context.resources.displayMetrics.widthPixels

    /**
     * 获取手机屏幕高
     */
    fun getScreenHeigth(context: Context) = context.resources.displayMetrics.heightPixels

    /**
     * 是否有网络
     */
    fun hasNet(context: Context): Boolean {
        var hasNet = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                connectivityManager.allNetworks.forEach {
                    val networkInfo = connectivityManager.getNetworkInfo(it)
                    if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                        hasNet = true
                    }
                }
            } else {
                connectivityManager.allNetworkInfo.forEach {
                    if (it.state == NetworkInfo.State.CONNECTED) {
                        hasNet = true
                    }
                }
            }
        }
        if (!hasNet) {
            toast("无网络连接，请检查网络！")
        }
        return hasNet
    }
}