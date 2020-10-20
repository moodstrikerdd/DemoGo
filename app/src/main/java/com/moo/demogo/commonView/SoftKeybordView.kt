package com.moo.demogo.commonView

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.moo.demogo.R
import kotlinx.android.synthetic.main.layout_soft_key_board.view.*

/**
 * @ClassName:      SoftKeybordView
 * @Author:         moodstrikerdd
 * @CreateDate:     2019/11/9 17:25
 * @Description:    系统输入法、自定义输入无缝切换View
 */
class SoftKeybordView @JvmOverloads constructor(context: Context,
                                                attributeSet: AttributeSet? = null,
                                                defStyleAttr: Int = 0) : FrameLayout(context, attributeSet, defStyleAttr) {
    companion object {
        const val KEY_SOFT_KEYBOARD_HEIGHT = "KEY_SOFT_KEYBOARD_HEIGHT"
        const val SOFT_KEYBOARD_HEIGHT_DEFAULT = 0
    }

    private var im: InputMethodManager
    private var sp: SharedPreferences
    private var mActivity: Activity
    private var lastChangeTime = 0L
    private var shouldIntercept = false

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_soft_key_board, this, true)
        im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        sp = context.getSharedPreferences("app", Context.MODE_PRIVATE)
        sp.edit().putInt(KEY_SOFT_KEYBOARD_HEIGHT, SOFT_KEYBOARD_HEIGHT_DEFAULT).apply()
        mActivity = context as Activity
        etInput.setOnTouchListener { _, motionEvent ->
            val currentTimeMillis = System.currentTimeMillis()
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                if (currentTimeMillis - lastChangeTime <= 500) {
                    shouldIntercept = true
                } else {
                    shouldIntercept = false
                    lastChangeTime = currentTimeMillis
                }
            }

            if (motionEvent.action == MotionEvent.ACTION_POINTER_UP ||
                    motionEvent.action == MotionEvent.ACTION_CANCEL) {
                if (shouldIntercept) {
                    shouldIntercept = false
                    return@setOnTouchListener true
                } else {
                    if (flContainer.isShown) {
                        changeLockView {
                            showSystemKeyBoard(true)
                            showOwnMenu(0, false)
                        }
                    }
                }
            }
            return@setOnTouchListener shouldIntercept
        }

        btnMenu.setOnClickListener{
            if(flContainer.isShown){
                
            }
        }
    }


    fun changeLockView(change: () -> Unit) {
        mActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        change()
        handler.postDelayed({
            mActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }, 200)
    }


    fun showOwnMenu(index: Int, show: Boolean) {
        if (show) {
            val systemKeyBoardHeight = getSystemKeyBoardHeight()
            flContainer.layoutParams.height = systemKeyBoardHeight
            flContainer.visibility = View.VISIBLE
            showIndex(index)
        } else {
            handler.postDelayed({ flContainer.visibility = GONE }, 200)
        }
    }

    //显示哪一个表情 自定义
    private fun showIndex(index: Int) {
    }

    fun showSystemKeyBoard(show: Boolean) {
        if (show) {
            im.hideSoftInputFromWindow(etInput.windowToken, 0)
        } else {
            im.showSoftInput(etInput, 0)
        }
    }

    fun getSystemKeyBoardHeight(): Int {
        val localHeight = sp.getInt(KEY_SOFT_KEYBOARD_HEIGHT, SOFT_KEYBOARD_HEIGHT_DEFAULT)
        if (localHeight == 0) {

        }
        return localHeight
    }

}