package com.moo.demogo.mainframe.proxy

import android.view.View
import com.moo.demogo.utils.AppUtils

class OnClickListenerNetProxy(private val onClickListener: View.OnClickListener?) : View.OnClickListener {

    override fun onClick(v: View) {
        if (AppUtils.hasNet()) {
            onClickListener?.onClick(v)
        }
    }
}
