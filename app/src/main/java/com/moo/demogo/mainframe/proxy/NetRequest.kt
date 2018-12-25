package com.moo.demogo.mainframe.proxy

import com.moo.demogo.bean.Person

interface NetRequest {

    fun getBean(): Person

    fun getList(): List<Person>
}