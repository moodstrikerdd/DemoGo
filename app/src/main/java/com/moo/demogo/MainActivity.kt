package com.moo.demogo

import android.content.Intent
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.TypedValue
import android.widget.TextView
import com.moo.adapter.ViewHolder
import com.moo.adapter.recyclerview.CommonAdapter
import com.moo.adapter.recyclerview.RecycleViewDivider
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.bean.ActivityNameBean
import com.moo.demogo.commonView.CenterImageSpan
import com.moo.demogo.mainframe.camera.CameraActivity
import com.moo.demogo.mainframe.coordinate.CoordinatorLayoutDemoActivity
import com.moo.demogo.mainframe.coroutines.CoroutinesActivity
import com.moo.demogo.mainframe.diffutil.DiffUtilActivity
import com.moo.demogo.mainframe.dir.DirActivity
import com.moo.demogo.mainframe.encryp.EncrypActivity
import com.moo.demogo.mainframe.headerandfooter.HeaderFooterActivity
import com.moo.demogo.mainframe.imagescale.ImageScaleActivity
import com.moo.demogo.mainframe.ioc.IocActivity
import com.moo.demogo.mainframe.leakcanary.LeakCanaryActivity
import com.moo.demogo.mainframe.proxy.ProxyActivity
import com.moo.demogo.mainframe.recycler.RecyclerViewActivity
import com.moo.demogo.mainframe.service.ServiceActivity
import com.moo.demogo.mainframe.share.ShareActivity
import com.moo.demogo.mainframe.sidesliplistview.SideSlipActivity
import com.moo.demogo.mainframe.socket.SocketActivity
import com.moo.demogo.mainframe.video.VideoActivity
import com.moo.demogo.mainframe.webview.WebViewActivity
import com.moo.demogo.utils.encryp.AESUtils
import com.moo.demogo.utils.encryp.EncrypUtils
import com.moo.demogo.utils.encryp.RsaEncryptUtils_
import com.moo.demogo.utils.loge
import com.moo.demogo.utils.runtimepermission.Permission
import com.moo.demogo.utils.runtimepermission.RuntimePermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    private val list = arrayListOf(
            ActivityNameBean("WebViewActivity", "WebView相关设置，入宽高自适应，shouldOverrideUrlLoa。", WebViewActivity::class.java),
            ActivityNameBean("SideSlipActivity", "侧滑ListView\nSwipeRefreshLayout冲突，NestedScrolling实现。", SideSlipActivity::class.java),
            ActivityNameBean("HeaderFooterActivity", "RecyclerView添加多头和尾，使用装饰者模式，将正常Adapter包装，使其可以addHeaderView和addFooterView。", HeaderFooterActivity::class.java),
            ActivityNameBean("CoroutinesActivity", "kotlin重点---协程\n线程间调度，是异步任务能线性调用。", CoroutinesActivity::class.java),
            ActivityNameBean("ServiceActivity", "ServiceActivity\nstart bind Service生命周期。", ServiceActivity::class.java),
            ActivityNameBean("LeakCanaryActivity", "LeakCanaryActivity\n内存泄露分析工具LeakCanary。", LeakCanaryActivity::class.java),
            ActivityNameBean("VideoActivity", "VideoActivity\n选择拍摄视频。", VideoActivity::class.java),
            ActivityNameBean("DiffUtilActivity", "DiffUtilActivity\nDiffUtil封装通用adapter。", DiffUtilActivity::class.java),
            ActivityNameBean("ShareActivity", "ShareActivity\n调用系统分享。", ShareActivity::class.java),
            ActivityNameBean("IocActivity", "IocActivity\n自定义ioc框架实现点击事件，网络判断，禁止重复点击。", IocActivity::class.java),
            ActivityNameBean("CameraActivity", "CameraActivity\n自定义相机。", CameraActivity::class.java),
            ActivityNameBean("DirActivity", "DirActivity\nandroid文件目录获取。", DirActivity::class.java),
            ActivityNameBean("SocketActivity", "SocketActivity\nsocket聊天客户端。", SocketActivity::class.java),
            ActivityNameBean("ProxyActivity", "ProxyActivity\njava动态代理。", ProxyActivity::class.java),
            ActivityNameBean("EncrypActivity", "EncrypActivity\nandroid 加密。", EncrypActivity::class.java),
            ActivityNameBean("RecyclerViewActivity", "RecyclerViewActivity\nRecyclerView联动。", RecyclerViewActivity::class.java),
            ActivityNameBean("CoordinatorLayoutDemoActivity", "CoordinatorLayoutDemoActivity\nCoordinatorLayout自定义behavior。", CoordinatorLayoutDemoActivity::class.java),
            ActivityNameBean("ImageScaleActivity", "ImageScaleActivity\nImageView scaleType 区别。", ImageScaleActivity::class.java)
    )

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initData() {
//        loge(message = NdkTest.get())
//        loge(message = NdkTest2.get())

//        Flowable.create(FlowableOnSubscribe<Any> {
//            Thread.sleep(2000)
//            it.onNext("crash")
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Subscriber<Any> {
//                    override fun onComplete() {
//                    }
//
//                    override fun onSubscribe(s: Subscription?) {
//                    }
//
//                    override fun onNext(t: Any?) {
//                        if (t == "crash") {
//                            throw RuntimeException("test crash")
//                        }
//                    }
//
//                    override fun onError(t: Throwable?) {
//                    }
//
//                })
//        Flowable.interval(1, TimeUnit.SECONDS, Schedulers.io())
//                .flatMap {
//                    loge(message = "flatMap$it")
//                    Flowable.create(FlowableOnSubscribe<Any> { inner ->
//                        val sleep = Random().nextInt(3)
//                        loge(message = "Flowable sleep $sleep s")
//                        Thread.sleep(sleep * 1000L)
//                        loge(message = "inner start")
//                        inner.onNext("$it")
//                    }, BackpressureStrategy.BUFFER)
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Subscriber<Any> {
//                    override fun onComplete() {
//                    }
//
//                    override fun onSubscribe(s: Subscription?) {
//                        s?.request(Long.MAX_VALUE)
//                    }
//
//                    override fun onNext(t: Any?) {
//                        val sleep = Random().nextInt(3)
//                        loge(message = "onNext sleep $sleep s")
//                        Thread.sleep(sleep * 1000L)
//                        loge(message = "onNext $t")
//                    }
//
//                    override fun onError(t: Throwable?) {
//                    }
//                })
//        Observable.create<String> {
//            Thread.sleep(2000)
//            it.onNext("crash")
//        }.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<String> {
//                    override fun onComplete() {
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                    }
//
//                    override fun onNext(t: String) {
//                        if (t == "crash") {
//                            throw RuntimeException("test crash")
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//                    }
//                })

//        launch(UI) {
//            launch(CommonPool) {
//                Thread.sleep(2000)
//            }.join()
//            throw RuntimeException("test crash")
//        }

        val decodeString = EncrypUtils.decodeRSA("h35XnGCgifJtl+XTwl1jSZMBtzAm35Dknsg0zmhfvqRnxcDa6GQj0HNm3LK/QOco1Ed7FWtWt6yK6B9JaTnHYRoMJoorO39YBwD3vYs5k/sjzx1J/qJIxVhzClZMMdS/60/Rhg0evlQIKqPzT5gEKxMPmLVNtOi+5rjH1DRzKyw=")
        loge(message = "decodeString = $decodeString")

        val decodeString2 = AESUtils.decode("05a526126eb32e1480e75ca029a0aebc", "mvh5WxwwN2DGg7ognEx/Txwr71DEsM60xPzNV87iKu8=")
        loge(message = "decodeString2 = $decodeString2")

//        AesEncryptUtils.test()
        RsaEncryptUtils_.test()
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = object : CommonAdapter<ActivityNameBean>(this, R.layout.item_main_menu, list) {
            val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()

            val drawable1 = ActivityCompat.getDrawable(this@MainActivity, R.mipmap.find_topic_audio).apply {
                setBounds(0, 0, size, size)
            }
            val drawable2 = ActivityCompat.getDrawable(this@MainActivity, R.mipmap.find_topic_video).apply {
                setBounds(0, 0, size, size)
            }

            override fun convert(holder: ViewHolder, t: ActivityNameBean) {
                holder.setText(R.id.name, t.name)


                val titleBuilder = StringBuilder(t.describe)
                titleBuilder.append("aaaa")
                val spannableString = SpannableStringBuilder(titleBuilder)
                spannableString.setSpan(ForegroundColorSpan(Color.WHITE),t.describe!!.length,t.describe!!.length+4,Spanned.SPAN_INCLUSIVE_INCLUSIVE)

                spannableString.setSpan(ImageSpan(drawable1, 0),
                        t.describe!!.length,
                        t.describe!!.length + 1,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE)

                spannableString.setSpan(ImageSpan(drawable2, 0),
                        t.describe!!.length + 2,
                        t.describe!!.length + 3,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE)

                holder.getView<TextView>(R.id.message).setText(spannableString,TextView.BufferType.SPANNABLE)

                holder.convertView.setOnClickListener {
                    try {
                        val intent = Intent(this@MainActivity, t.className)
                        startActivity(intent)
                    } catch (e: Exception) {
                        toast("跳转失败！")
                    }
                }
            }
        }
        recyclerView.addItemDecoration(RecycleViewDivider(this, RecycleViewDivider.VERTICAL, 10, resources.getColor(R.color.dividerGray)))

        RuntimePermissionHelper.permissions.clear()
        RuntimePermissionHelper.permissions.addAll(Permission.CAMERA)
        RuntimePermissionHelper.permissions.addAll(Permission.STORAGE)
        RuntimePermissionHelper.permissions.addAll(Permission.MICROPHONE)
        RuntimePermissionHelper.permissions.add(Permission.PHONE[0])
        RuntimePermissionHelper.requestPermissions2(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        RuntimePermissionHelper.onRequestPermissionsResult2(this, requestCode, permissions, grantResults, null)
    }
}
