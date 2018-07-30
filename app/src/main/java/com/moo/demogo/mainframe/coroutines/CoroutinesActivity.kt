package com.moo.demogo.mainframe.coroutines

import com.moo.adapter.ViewHolder
import com.moo.adapter.abslistview.CommonAdapter
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.bean.HotWebBean
import com.moo.demogo.http.CallBack
import com.moo.demogo.http.RetrofitHelper
import com.moo.demogo.mainframe.webview.WebViewActivity
import com.moo.demogo.utils.doHttp
import kotlinx.android.synthetic.main.activity_coroutines.*
import org.jetbrains.anko.toast

class CoroutinesActivity : BaseActivity() {

    private var adapter: CommonAdapter<HotWebBean>? = null
    private val data = arrayListOf<HotWebBean>()

    override fun getLayoutId(): Int = R.layout.activity_coroutines

    override fun initView() {
        adapter = object : CommonAdapter<HotWebBean>(this, R.layout.item_side_slip, data) {
            override fun convert(holder: ViewHolder, t: HotWebBean) {
                holder.setText(R.id.tvItemName, t.name)
                holder.setOnClickListener(R.id.tvItemName) {
                    if (t.link == null) {
                        toast("跳转链接不存在")
                    } else {
                        WebViewActivity.intentStart(this@CoroutinesActivity, t.link!!, t.name)
//                        val formUrl = "https://218.19.164.167:30043/accountManage/personsignClient.do";
//                        val commonval = "P2P00001|FfjjZheOPkNvo06p6cJWelv0tcrv6O3zsFOVfRGBdOiaRZlQ1ARDhb3R8jhrSirT3qFzYsb+MdpNW9qHeXPnEKnjK8HdRUoBoeW+chT95yMNCgouClr4ZH9aeG/Zn03aUvl4vILsQqwLzlCgFSNiiVCj4PoYKKrclW3UdzK4tmU=|eIYUplUcuU6qmxYkqq+kvG+DPVwO3lY1qphm6EbH0nBdBYf1VYGwPVFVVkPn21s8J/6V6H5DpbYlmNLFhs9J2E8Fr55mcqksPSY3pxWHuygeChggEO0cQbawr9iXyhpoPb1qVWWB185ie4uw0LdeYnBWsZp25nOqOgLTPCO4YJE=|mPZtokLK7TvT0QsLh3iosKFSx1uc4znZ1AcYlDzH5SOYFoPPmxqswbQHJCwJt3QGzHrb5KyvQk03BgWgDvSCNDwv10mTifv1Oh2Sbkfp/n12vURWvB5hTWDEImEy2Tzc40GzsAU/Emvkfvj2o6vZNr1Y8uLCM9jJxcbe0tZ0dqdy29HZFsn7Wwwfnd2p2+xs0Gr40gad45eEMj1ZZ04eapleOF84mQdzO1mUt7MltRxEsUZbWKPoYrkmq/FfOoza+W2QxyyaHOxiaYNQOne5v7MY99/NlK3Ux53dKkezyMadvBgMN1hs9pdcmMIr06l5p+16FjnfUUms8X178exblZRd0HxFAS+z576gTB6WOYhYexCraKclgnmrVrfqgS5gmBnc+oAqZU2XJqqPeHYXC8ws+P8iS0aK7foOe9deX2anurP/jkSMweKoHkBPdr5pi8ppTNMYr1xBELUAjThtvQ1ARAmBaW/0PWWixhSYPE+uVsBWUCSVPr4ZkLmE4w5YyV4yjYvxJ4DWcuZoALXqglNaPJwHlUweyiEFFs6M1WNZAKRh5J8XC6KdagIRwhwsI3cR7Bxq/5ujgcUnZPVMhH5rmoGR/hqxAW/Od3r1gOC0io5PPx8RB7pw0fegySQxvSFkwUGYXz4ixDPuKpntKX3y7TTbLQsiN6+gp7rKO/Vx5l5LiveS2qtEi6XMAKxYvTxOH63vhjMuouUtlbb2gDQaU9M2g5vJaZ1WyiRhACHGJnspIT8X50xZOhor+FweUv5taU2n6xXsah7PH96NEA==";
//                        val encode = Base64.encode("abcdef".toByteArray(), Base64.DEFAULT)
//                        val toString = encode.toString(Charsets.UTF_8)
//                        loge(message = encode.toString(Charsets.UTF_8))
//                        loge(message = Base64.decode(toString, Base64.DEFAULT).toString(Charsets.UTF_8))
//                        val map = hashMapOf<String, String>()
//                        map["COMMONVAL"] = commonval
//                        WebViewActivity.intentStart(this@CoroutinesActivity, formUrl, t.name, map)
                    }
                }
            }
        }
        lvContent.adapter = adapter
        refresh.setOnRefreshListener {
            initData()
        }
    }

    override fun initData() {
        refresh.isRefreshing = true
        doHttp({
            RetrofitHelper.api.getList()
        },
                object : CallBack<List<HotWebBean>>() {
                    override fun onSuccess(t: List<HotWebBean>) {
                        data.apply {
                            clear()
                            addAll(t)
                        }
                        refresh.isRefreshing = false
                        adapter?.notifyDataSetChanged()
                    }

                    override fun onFailed(throwable: Throwable) {
                        super.onFailed(throwable)
                        refresh.isRefreshing = false
                    }
                })
    }

}