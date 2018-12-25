package com.moo.demogo.mainframe.proxy

import android.util.Log
import com.moo.demogo.bean.Person

class NetRequestProxy<T : NetRequest>(val t: T) : NetRequest {
    override fun getBean(): Person {
//        Log.e("proxy", "静态代理")
        return t.getBean()
    }

    override fun getList() = t.getList()
}