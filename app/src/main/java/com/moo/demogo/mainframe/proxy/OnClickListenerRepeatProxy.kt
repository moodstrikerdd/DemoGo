package com.moo.demogo.mainframe.proxy

import android.view.View

class OnClickListenerRepeatProxy(private val onClickListener: View.OnClickListener?, private val second: Long = 1000) : View.OnClickListener {
    private var lastClickTimeMills = 0L

    override fun onClick(v: View) {
        if (System.currentTimeMillis() - lastClickTimeMills >= second) {
            lastClickTimeMills = System.currentTimeMillis()
            onClickListener?.onClick(v)
        }
    }
}
