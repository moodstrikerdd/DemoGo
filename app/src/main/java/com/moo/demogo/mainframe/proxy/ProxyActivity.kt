package com.moo.demogo.mainframe.proxy

import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import retrofit2.Retrofit
import java.lang.reflect.Proxy

class ProxyActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_proxy

    override fun initData() {
        val netRequestProxy = NetRequestProxy(NetRequestImpl())
        val newProxyInstance = Proxy.newProxyInstance(NetRequest::class.java.classLoader,
                arrayOf<Class<*>>(NetRequest::class.java)
        ) { _, p1, p ->
            if (p1.declaringClass == Any::class.java) {
                p1.invoke(this, *p)
            } else {
                p1.invoke(netRequestProxy, *p)
            }
        } as NetRequest
        val bean = newProxyInstance.getBean()
        val toString = newProxyInstance.toString()
    }

    override fun initView() {
    }
}
