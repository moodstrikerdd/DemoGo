package com.moo.demogo.mainframe.proxy

import android.os.Handler
import android.view.View
import android.view.Window
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.utils.loge
import kotlinx.android.synthetic.main.activity_proxy.*
import org.jetbrains.anko.toast
import java.lang.reflect.Proxy

class ProxyActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_proxy

    override fun initData() {
        val netRequestProxy = NetRequestImpl()
        val newProxyInstance = Proxy.newProxyInstance(NetRequest::class.java.classLoader,
                arrayOf<Class<*>>(NetRequest::class.java)
        ) { //此处为InvocationHandler接口的实现
            _, p1, p ->
            if (p1.declaringClass == Any::class.java) {
                //Object的方法一般不处理
                p1.invoke(this, *p)
            } else {
                //接口中需要被代理的方法
                loge(message = "${p1.name} start:${System.currentTimeMillis()}")
                //使用反射调用实现类的具体实现方法
                val invoke = p1.invoke(netRequestProxy, *p)
                loge(message = "${p1.name} end:${System.currentTimeMillis()}")
                //返回对应代理方法的返回值
                invoke
            }
        } as NetRequest
        val bean = newProxyInstance.getBean()
        val toString = newProxyInstance.getList()

        text1.setOnClickListener(OnClickListenerRepeatProxy(OnClickListenerNetProxy(View.OnClickListener {
            toast("控件被点击了")
        })))
    }

    override fun doInBeforeSetContent() {
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
    }


    override fun initView() {
        Handler().postDelayed({
            text1.ctvRightText = ""
            text2.ctvRightText = ""
            text3.ctvRightText = ""
            text4.ctvRightText = ""
        }, 2000)

        Handler().postDelayed({
            text1.ctvRightText = "1"
            text2.ctvRightText = "1"
            text3.ctvRightText = "1"
            text4.ctvRightText = "1"
        }, 4000)
    }
}
