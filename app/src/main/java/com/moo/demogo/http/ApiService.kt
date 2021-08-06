package com.moo.demogo.http

import com.moo.demogo.bean.BaseBean
import com.moo.demogo.bean.HomeBean
import com.moo.demogo.bean.HotWebBean
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author moodstrikerdd
 * @date 2018/4/19
 * @label Retrofit Api
 */
interface ApiService {
    /**
     * http://www.wanandroid.com
     */

    @GET("/friend/json")
    fun getList(): Deferred<BaseBean<List<HotWebBean>>>

    @GET("/article/list/{page}/json")
    fun getHomeBean(@Path("page") page: Int): Deferred<BaseBean<HomeBean>>
}
