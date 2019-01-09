package com.moo.demogo.commonView

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.moo.demogo.R
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class CommTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var ctvTitle: TextView

    lateinit var ctvContent: TextView


    var ctvTitleText by Delegates.observable("") { _, _, newValue ->
        ctvTitle.text = newValue
    }

    var ctvTitleTextSize by Delegates.observable(0f) { _, _, newValue ->
        ctvTitle.textSize = newValue
    }

    var ctvTitleTextColor by Delegates.observable(0) { _, _, newValue ->
        ctvTitle.setTextColor(newValue)
    }

    /**
     *  right 0  left 1
     */
    var ctvRightGravity by Delegates.observable(0) { _, _, newValue ->
        ctvContent.gravity = if (newValue == 0) {
            Gravity.CENTER_VERTICAL or Gravity.END
        } else {
            Gravity.CENTER_VERTICAL or Gravity.START
        }
    }

    var ctvRightSrc by Delegates.observable(R.mipmap.ic_launcher) { _, _, newValue ->
        val drawable = ContextCompat.getDrawable(context, newValue)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        ctvContent.setCompoundDrawables(null, null, drawable, null)
    }

    var ctvRightHint by Delegates.observable("") { property, _, newValue ->
        isHintShow = !TextUtils.isEmpty(newValue) && TextUtils.isEmpty(ctvRightText)
        showTextOrHint(property, newValue)
    }

    var ctvRightHintColor by Delegates.observable(Color.parseColor("#999999")) { _, _, newValue ->
        if (isHintShow) {
            ctvContent.setTextColor(newValue)
        }
    }

    var isHintShow = false
    var ctvRightText by Delegates.observable("") { property, _, newValue ->
        isHintShow = TextUtils.isEmpty(newValue)
        showTextOrHint(property, newValue)
    }

    private fun showTextOrHint(property: KProperty<*>, newValue: String) {
        if (isHintShow) {
            ctvContent.text = if (property.name == ctvRightHint) {
                newValue
            } else {
                ctvRightHint
            }
            ctvContent.setTextColor(ctvRightHintColor)
        } else {
            ctvContent.text = if (property.name == ctvRightText) {
                newValue
            } else {
                ctvRightText
            }
            ctvContent.setTextColor(ctvRightTextColor)
        }
    }


    var ctvRightTextSize by Delegates.observable(16f) { _, _, newValue ->
        ctvContent.textSize = newValue
    }

    var ctvRightTextColor by Delegates.observable(Color.parseColor("#333333")) { _, _, newValue ->
        if (!isHintShow) {
            ctvContent.setTextColor(newValue)
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_comm_textview, this, true)
        ctvTitle = findViewById(R.id.ctvTitle)
        ctvContent = findViewById(R.id.ctvContent)
        val osa = context.obtainStyledAttributes(attrs, R.styleable.CommTextView)
        initAttrs(osa)
        osa.recycle()
    }

    private fun initAttrs(typedArray: TypedArray) {
        for (i in 0 until typedArray.indexCount) {
            val type = typedArray.getIndex(i)
            when (type) {
                R.styleable.CommTextView_ctvTitleText -> ctvTitleText = typedArray.getString(type)
                R.styleable.CommTextView_ctvTitleTextSize -> ctvTitleTextSize = typedArray.getDimension(type, 17f)
                R.styleable.CommTextView_ctvTitleTextColor -> ctvTitleTextColor = typedArray.getColor(type, Color.parseColor("#333333"))
                R.styleable.CommTextView_ctvRightGravity -> ctvRightGravity = typedArray.getInteger(type, 0)
                R.styleable.CommTextView_ctvRightSrc -> ctvRightSrc = typedArray.getResourceId(type, R.mipmap.ic_launcher)
                R.styleable.CommTextView_ctvRightHint -> ctvRightHint = typedArray.getString(type)
                R.styleable.CommTextView_ctvRightHintColor -> ctvRightHintColor = typedArray.getColor(type, Color.parseColor("#999999"))
                R.styleable.CommTextView_ctvRightText -> ctvRightText = typedArray.getString(type)
                R.styleable.CommTextView_ctvRightTextSize -> ctvRightTextSize = typedArray.getDimension(type, 17f)
                R.styleable.CommTextView_ctvRightTextColor -> ctvRightTextColor = typedArray.getColor(type, Color.parseColor("#333333"))
            }
        }
    }
}