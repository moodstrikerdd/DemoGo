package com.moo.demogo.mainframe.dir

import android.os.Environment
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_dir.*
import java.lang.StringBuilder

/**
 * @author moodstrikedd
 * @date 2018/11/8
 * @label
 */
class DirActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_dir

    override fun initData() {
        val sb = StringBuilder()
        sb.append("context.getFilesDir---${filesDir.absoluteFile}\n")
        sb.append("context.getExternalFilesDir---${getExternalFilesDir("file").absoluteFile}\n")
        sb.append("context.getCacheDir---${cacheDir.absoluteFile}\n")
        sb.append("context.getExternalCacheDir---${externalCacheDir.absoluteFile}\n")


        sb.append("Environment.getExternalStorageDirectory()---${Environment.getExternalStorageDirectory().absoluteFile}\n")
        sb.append("Environment.getExternalStoragePublicDirectory()---${Environment.getExternalStoragePublicDirectory("file").absoluteFile}\n")
        text.text = sb.toString()
    }

    override fun initView() {
    }

}