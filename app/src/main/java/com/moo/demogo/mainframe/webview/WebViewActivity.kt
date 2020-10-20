package com.moo.demogo.mainframe.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.constant.DefineKey
import com.moo.demogo.utils.ViewShotUtils
import kotlinx.android.synthetic.main.activity_web_view.*
import java.net.URLEncoder


@SuppressLint("SetJavaScriptEnabled")
class WebViewActivity : BaseActivity() {
    private val titleMap: HashMap<String, String> = hashMapOf()

    private lateinit var url: String
    private lateinit var title: String
    private var usePost = false
    private lateinit var params: HashMap<String, String>

    private var webViewScale = 0f

    companion object {
        fun intentStart(context: Context, url: String, title: String?) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(DefineKey.URL, url)
            intent.putExtra(DefineKey.TITLE, title ?: "网页")
            context.startActivity(intent)
        }

        fun intentStart(context: Context, url: String, title: String?, params: HashMap<String, String>) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(DefineKey.USE_POST, true)
            intent.putExtra(DefineKey.URL, url)
            intent.putExtra(DefineKey.TITLE, title ?: "网页")
            intent.putExtra(DefineKey.PARAMS_MAP, params)
            context.startActivity(intent)
        }
    }

    override fun doInBeforeSetContent() {
//        WebView.enableSlowWholeDocumentDraw()
    }

    override fun getLayoutId(): Int = R.layout.activity_web_view

    override fun initIntentData(intent: Intent) {
        url = if (intent.hasExtra(DefineKey.URL)) {

            intent.getStringExtra(DefineKey.URL)
        } else {
            "https://h5.niuguwang.com/2019y/course/article-detail/index.html?bbsid=15332272&showpreview=0"
        }
        title = if (intent.hasExtra(DefineKey.TITLE)) {
            intent.getStringExtra(DefineKey.TITLE)
        } else {
            "网页详情"
        }
        usePost = intent.getBooleanExtra(DefineKey.USE_POST, false)
        if (intent.hasExtra(DefineKey.PARAMS_MAP)) {
            params = intent.getSerializableExtra(DefineKey.PARAMS_MAP) as HashMap<String, String>
        }
    }

    override fun initView() {
        initWebSetting()
    }

    private fun initWebSetting() {
        //CVE-2014-1939
        // WebView 中内置导出的 “searchBoxJavaBridge_” Java Object 可能被利用，实现远程任意代码；
        webView.removeJavascriptInterface("searchBoxJavaBridge_")
        //CVE-2014-7224，类似于 CVE-2014-1939 ，
        // WebView 内置导出 “accessibility” 和 “accessibilityTraversal” 两个 Java Object 接口，
        // 可被利用实现远程任意代码执行。
        webView.removeJavascriptInterface("accessibility")
        webView.removeJavascriptInterface("accessibilityTraversal")

        webView.isDrawingCacheEnabled = true

        val settings = webView.settings
        settings.javaScriptEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.useWideViewPort = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        } else {
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dealWithUrl(view, request.url.toString())
                } else {
                    super.shouldOverrideUrlLoading(view, request)
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    dealWithUrl(view, url)
                } else {
                    super.shouldOverrideUrlLoading(view, url)
                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                super.onScaleChanged(view, oldScale, newScale)
                webViewScale = newScale
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                if (titleMap[view.url] == null || titleMap[view.url] == "网页") {
                    titleMap[view.url] = title
                }
                topBar.tvTitleText = titleMap[view.url] ?: "网页"
            }
        }

        webView.setDownloadListener { url, _, _, _, _ ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        webView.setOnLongClickListener {
            //            CodeUtils.analyzeBitmap(BitmapFactory.decodeResource(resources, R.mipmap.code), object : CodeUtils.AnalyzeCallback {
//                override fun onAnalyzeSuccess(mBitmap: Bitmap?, result: String): Boolean {
//                    if (!TextUtils.isEmpty(result)) {
//                        toast(message = "二维码内容：$result")
//                    }
//                    return true
//                }
//
//                override fun onAnalyzeFailed() {
//
//                }
//            })
            val bitmap = ViewShotUtils.webViewAllShot(webView,webViewScale)
            if (bitmap != null) {
                image.setImageBitmap(bitmap)
                image.visibility = View.VISIBLE
            }
            true
        }

    }

    private fun dealWithUrl(view: WebView, url: String): Boolean {
        if (usePost) {
            WebViewActivity.intentStart(this, url, null)
            return true
        }
        return false
    }

    override fun initData() {
        if (TextUtils.isEmpty(title)) {
            title = "网页"
        }
        if (TextUtils.isEmpty(url)) {
            url = "http://www.moodstrikerdd.com"
        }
        titleMap[url] = title
        topBar.tvTitleText = title
        if (usePost) {
            val sb = StringBuilder()
            params.forEach { key, value ->
                sb.append("$key=${URLEncoder.encode(value, "UTF-8")}")
            }
            webView.postUrl(url, sb.toString().toByteArray())
        } else {
            webView.loadUrl(url)
        }
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

    override fun onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

}