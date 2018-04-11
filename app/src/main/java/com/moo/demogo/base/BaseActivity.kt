package com.moo.demogo.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager


/**
 * @author moodstrikerdd
 * @date 2018/4/2
 * @label
 */

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initStatusBar()
        initView()
        initData()
    }

    private fun initStatusBar() {
        val sdkVersion = Build.VERSION.SDK_INT
        if (sdkVersion >= 16) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
        if (sdkVersion in 19..20) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (sdkVersion >= 21) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    abstract fun getLayoutId(): Int

    abstract fun initData()

    abstract fun initView()

}