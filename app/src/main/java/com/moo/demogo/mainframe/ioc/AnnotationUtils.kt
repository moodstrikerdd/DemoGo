package com.moo.demogo.mainframe.ioc

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.View
import com.moo.demogo.utils.AppUtils

/**
 * @author moodstrikerdd
 * @date 2018/7/30
 * @label ioc入口
 */
class AnnotationUtils {
    private val offsetTimes = hashMapOf<Int, Long>()

    private fun injectOnClick(any: Any, contentView: View?) {
        val javaClass = any.javaClass
        val declaredMethods = javaClass.declaredMethods
        if (declaredMethods != null && declaredMethods.isNotEmpty()) {
            declaredMethods.forEach { method ->
                val onClick = method.getAnnotation(OnClick::class.java)
                val checkNet = method.getAnnotation(CheckNet::class.java)
                val limitRepeatClick = method.getAnnotation(LimitRepeatClick::class.java)
                if (onClick != null) {
                    val values = onClick.value
                    var offsetTime = 0L
                    if (limitRepeatClick != null) {
                        offsetTime = limitRepeatClick.value
                    }
                    values.forEach { viewId ->
                        val view = (if (any is Activity) {
                            any.findViewById(viewId)
                        } else {
                            contentView?.findViewById<View>(viewId)
                        }) ?: throw RuntimeException("OnClick注解中存在无效id")
                        view.setOnClickListener {
                            val lastTimeMillis = offsetTimes[viewId] ?: 0L
                            val currentTimeMillis = System.currentTimeMillis()
                            if (offsetTime == 0L || currentTimeMillis - lastTimeMillis >= offsetTime) {
                                offsetTimes[viewId] = currentTimeMillis
                                if (checkNet == null || checkNet.value.isEmpty() || !checkNet.value.contains(viewId) || AppUtils.hasNet()) {
                                    method.isAccessible = true
                                    method.invoke(any, view)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun injectOnClick(activity: Activity) {
        injectOnClick(activity, null)
    }

    fun injectOnClick(fragment: Fragment, contentView: View) {
        injectOnClick(any = fragment, contentView = contentView)
    }
}