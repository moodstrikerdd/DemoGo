package com.moo.demogo.mainframe.ioc

import android.view.View
import android.widget.Button
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import org.jetbrains.anko.toast

/**
 * @author moodstrikerdd
 * @date 2018/7/30
 * @label 自定义ioc
 */
class IocActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_ioc

    override fun initData() {
    }

    override fun initView() {
        AnnotationUtils().injectOnClick(this)
        supportFragmentManager.beginTransaction().add(R.id.flContain, IocFragment.newInstance()).commit()
    }

    @OnClick(R.id.btnClick1, R.id.btnClick2)
    @CheckNet(R.id.btnClick2)
    @LimitRepeatClick
    private fun onClick(view: View) {
        toast("${(view as Button).text}被点击了！")
    }
}
