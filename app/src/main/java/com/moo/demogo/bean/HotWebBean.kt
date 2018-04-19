package com.moo.demogo.bean

/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label 热门网站
 */

data class HotWebBean(var icon: String? = null,
                      var id: Long = 0,
                      var link: String? = null,
                      var name: String? = null,
                      var order: Int = 0,
                      var visible: Int = 0)

/**
 * icon :
 * id : 24
 * link : https://www.chuangkit.com/designtools/startdesign
 * name : 素材设计-创客
 * order : 18
 * visible : 1
 */
