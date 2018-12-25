package com.moo.demogo.mainframe.proxy

import com.moo.demogo.bean.Person

class NetRequestImpl : NetRequest{
    override fun getBean() = Person("张三", "Moo")

    override fun getList() = arrayListOf(
            Person("张三", "Moo"),
            Person("李四", "Noo"),
            Person("王五", "Ooo"))
}