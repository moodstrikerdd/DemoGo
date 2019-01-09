package com.moo.demogo.mainframe.proxy

import android.util.Log
import com.moo.demogo.bean.Person

class NetRequestProxy<T : NetRequest>(val t: T) : NetRequest {
    override fun getBean(): Person {
        Log.e("proxy", "开始预处理")
        return t.getBean()
    }

    override fun getList(): List<Person> {
        Log.e("proxy", "开始预处理")
        return t.getList()
    }
}