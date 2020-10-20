package com.moo.demogo.mainframe.leakcanary

import android.os.Handler
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_leakcanary.*

class LeakCanaryActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_leakcanary
    }

    override fun initData() {
        Handler().postDelayed({
            dv1.setData(arrayListOf("2017一季报", "2017二季报", "2017三季报", "2017四季报", "2017年报"),
                    arrayListOf(arrayListOf(200f, 300f, 400f, 500f),
                            arrayListOf(500f, 300f, 400f, 200f),
                            arrayListOf(300f, 200f, 600f, 500f),
                            arrayListOf(400f, 500f, 300f, 400f),
                            arrayListOf(100f, 200f, 300f, 400f)),
                    arrayListOf(arrayListOf(100f, 200f, 300f, 400f),
                            arrayListOf(100f, 200f, 300f, 400f),
                            arrayListOf(100f, 200f, 300f, 400f),
                            arrayListOf(100f, 200f, 300f, 400f),
                            arrayListOf(100f, 200f, 300f, 400f)),
                    arrayListOf(0f, 10f, 10f, 20f, 0f))

            sv1.setData(arrayListOf(
                    arrayListOf(-100f),
                    arrayListOf(-100f),
                    arrayListOf(-100f),
                    arrayListOf(-100f),
                    arrayListOf(-100f)),
                    arrayListOf("2014年报", "2015年报", "2016年报", "2017年报", "2018年报"),
                    arrayListOf(0f, 10f, -10f, 20f, 0f))
            sv2.setData(arrayListOf(arrayListOf(100f, 200f, 300f, 400f),
                    arrayListOf(-100f, 200f, 300f, -400f),
                    arrayListOf(100f, -200f, -300f, 400f),
                    arrayListOf(100f, -200f, -300f, 400f),
                    arrayListOf(-100f, 200f, 300f, -400f)),
                    arrayListOf("2014年报", "2015年报", "2016年报", "2017年报", "2018年报"),
                    arrayListOf(40f, 50f, 60f, 70f, 100f),
                    false)
        }, 500)

        Handler().postDelayed({
            dv1.setData(arrayListOf("2017一季报", "2017二季报", "2017三季报", "2017四季报", "2017年报"),
                    arrayListOf(arrayListOf(100f, 300f, 400f, 500f),
                            arrayListOf(100f, 300f, 400f, 200f),
                            arrayListOf(100f, 200f, 600f, 500f),
                            arrayListOf(100f, 500f, 300f, 400f),
                            arrayListOf(200f, 200f, 300f, 400f)),
                    arrayListOf(arrayListOf(100f, 200f, 300f, 400f),
                            arrayListOf(300f, 200f, 300f, 400f),
                            arrayListOf(200f, 200f, 300f, 400f),
                            arrayListOf(200f, 200f, 300f, 400f),
                            arrayListOf(300f, 200f, 300f, 400f)),
                    arrayListOf(10f, 0f, 0f, 20f, 10f))
        },1500)
    }

    override fun initView() {}
}
