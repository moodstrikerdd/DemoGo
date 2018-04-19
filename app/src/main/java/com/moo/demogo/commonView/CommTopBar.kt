package com.moo.demogo.commonView

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.moo.adapter.ViewHolder
import com.moo.demogo.R
import com.moo.demogo.utils.AppUtils
import kotlin.properties.Delegates

/**
 * @author moodstrikerdd
 * @date 2018/4/3
 * @label
 */
class CommTopBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mViewHolder: ViewHolder? = null

    var needPadding by Delegates.observable(true) { _, _, new ->
        val height = if (new) {
            AppUtils.getStatusHeight(context)
        } else {
            0
        }
        mViewHolder?.getView<RelativeLayout>(R.id.container)?.setPadding(0, height, 0, 0)
    }

    var background by Delegates.observable(Any()) { _, _, new ->
        if (new is Drawable) {
            mViewHolder?.getView<RelativeLayout>(R.id.container)?.background = new
        }
    }

    var ivLeftSrc by Delegates.observable(R.mipmap.ic_launcher) { _, _, new ->
        val view = mViewHolder?.getView<ImageView>(R.id.ivLeft)
        view?.setImageResource(new)
        view?.setOnClickListener {
            if (getContext() is Activity) {
                (getContext() as Activity).onBackPressed()
            }
        }
    }
    var tvLeftText by Delegates.observable("") { _, _, new ->
        mViewHolder?.setText(R.id.tvLeft, new)
    }
    var tvLeftTextColor by Delegates.observable(R.mipmap.ic_launcher) { _, _, new ->
        mViewHolder?.setTextColor(R.id.tvLeft, new)
    }
    var tvLeftTextSize by Delegates.observable(14f) { _, _, new ->
        val textView = mViewHolder?.getView<TextView>(R.id.tvLeft)
        textView?.textSize = new
    }
    var tvTitleText by Delegates.observable("") { _, _, new ->
        mViewHolder?.setText(R.id.tvTitle, new)
    }
    var tvTitleTextColor by Delegates.observable(R.mipmap.ic_launcher) { _, _, new ->
        mViewHolder?.setTextColor(R.id.tvTitle, new)
    }
    var tvTitleTextSize by Delegates.observable(14f) { _, _, new ->
        val textView = mViewHolder?.getView<TextView>(R.id.tvTitle)
        textView?.textSize = new
    }
    var tvRightText by Delegates.observable("") { _, _, new ->
        mViewHolder?.setText(R.id.tvRight, new)
    }
    var tvRightTextColor by Delegates.observable(R.mipmap.ic_launcher) { _, _, new ->
        mViewHolder?.setTextColor(R.id.tvRight, new)
    }
    var tvRightTextSize by Delegates.observable(14f) { _, _, new ->
        val textView = mViewHolder?.getView<TextView>(R.id.tvRight)
        textView?.textSize = new
    }
    var ivRightSrc by Delegates.observable(R.mipmap.ic_launcher) { _, _, new ->
        mViewHolder?.setImageResource(R.id.ivRight, new)
    }


    init {
        mViewHolder = ViewHolder.get(context, R.layout.layout_comm_top_bar, this, true)
        val osa = context.obtainStyledAttributes(attrs, R.styleable.CommTopBar)
        initAttrs(osa)
        osa.recycle()
    }

    private fun initAttrs(osa: TypedArray) {
        for (index in 0 until osa.indexCount) {
            val indexInfo = osa.getIndex(index)
            when (indexInfo) {
                R.styleable.CommTopBar_needPadding -> {
                    needPadding = osa.getBoolean(indexInfo, true)
                }
                R.styleable.CommTopBar_topBackground -> {
                    background = osa.getDrawable(indexInfo)
                }
                R.styleable.CommTopBar_ivLeftSrc -> {
                    ivLeftSrc = osa.getResourceId(indexInfo, R.mipmap.ic_launcher)
                }
                R.styleable.CommTopBar_tvLeftText -> {
                    tvLeftText = osa.getString(indexInfo)
                }
                R.styleable.CommTopBar_tvLeftTextColor -> {
                    tvLeftTextColor = osa.getColor(indexInfo, Color.WHITE)
                }
                R.styleable.CommTopBar_tvLeftTextSize -> {
                    tvLeftTextSize = osa.getDimension(indexInfo, 14f)
                }
                R.styleable.CommTopBar_tvTitleText -> {
                    tvTitleText = osa.getString(indexInfo)
                }
                R.styleable.CommTopBar_tvTitleTextColor -> {
                    tvTitleTextColor = osa.getColor(indexInfo, Color.WHITE)
                }
                R.styleable.CommTopBar_tvTitleTextSize -> {
                    tvTitleTextSize = osa.getDimension(indexInfo, 14f)
                }
                R.styleable.CommTopBar_tvRightText -> {
                    tvRightText = osa.getString(indexInfo)
                }
                R.styleable.CommTopBar_tvRightTextColor -> {
                    tvRightTextColor = osa.getColor(indexInfo, Color.WHITE)
                }
                R.styleable.CommTopBar_tvRightTextSize -> {
                    tvRightTextSize = osa.getDimension(indexInfo, 14f)
                }
                R.styleable.CommTopBar_ivRightSrc -> {
                    ivRightSrc = osa.getResourceId(indexInfo, R.mipmap.ic_launcher)
                }
            }
        }
    }
}