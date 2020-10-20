package com.moo.demogo.bean

import android.graphics.Rect

/**
 * @ClassName:      GraphBean
 * @Author:         moodstrikerdd
 * @CreateDate:     2019/12/17 11:36
 * @Description:    java类作用描述
 */
data class GraphBean(var title: String = "",
                     var rect: Rect = Rect(),
                     var itemBeans: List<GraphItemBean>)

data class GraphItemBean(var item : List<GraphItemDataBean>)

data class GraphItemDataBean(var data : Float,
                             var dataHeight : Int)