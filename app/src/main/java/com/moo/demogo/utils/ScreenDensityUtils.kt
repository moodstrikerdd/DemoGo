package com.moo.demogo.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

/**
 * @ClassName:      ScreenDensityUtils
 * @Author:         moodstrikerdd
 * @CreateDate:     2021/5/17 18:15
 * @Description:    屏幕适配
 */
object ScreenDensityUtils {
    var systemDensity = 0f
    var systemScaleDensity = 0f

    fun setCustomeDensity(application: Application, activity: Activity) {
        val displayMetrics = application.resources.displayMetrics
        if (systemDensity == 0f) {
            systemDensity = displayMetrics.density
            systemScaleDensity = displayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {
                }

                override fun onConfigurationChanged(newConfig: Configuration?) {
                    if (newConfig != null) {
                        systemScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

            })
        }
        val targetDensity = displayMetrics.widthPixels / 360f
        val targetScaleDensity = targetDensity * (systemScaleDensity / systemDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()

        displayMetrics.density = targetDensity
        displayMetrics.scaledDensity = targetScaleDensity
        displayMetrics.densityDpi = targetDensityDpi

        activity.resources.displayMetrics.density = targetDensity
        activity.resources.displayMetrics.scaledDensity = targetScaleDensity
        activity.resources.displayMetrics.densityDpi = targetDensityDpi

    }

}