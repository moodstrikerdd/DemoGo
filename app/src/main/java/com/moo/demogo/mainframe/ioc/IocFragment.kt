package com.moo.demogo.mainframe.ioc

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.moo.demogo.R
import com.moo.demogo.base.BaseFragment
import com.moo.demogo.utils.toast

/**
 * @author moodstrikerdd
 * @date 2018/7/31
 * @label
 */
class IocFragment : BaseFragment() {

    companion object {
        fun newInstance(): IocFragment {
            val fragment = IocFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutId() = R.layout.fragment_ioc

    override fun initView() {
        AnnotationUtils().injectOnClick(this, contentView)
    }

    override fun initData() {
    }


    @OnClick(R.id.btnClick1, R.id.btnClick2)
    @CheckNet(R.id.btnClick2)
    @LimitRepeatClick
    fun onClick(view: View) {
        toast("${(view as Button).text}被点击了！")
    }
}