package com.moo.demogo.mainframe.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*


@SuppressLint("SetJavaScriptEnabled")
class WebViewActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_web_view

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
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    return dealWithUrl(view, url)
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }

        }

        webView.setDownloadListener { url, _, _, _, _ ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


    }

    private fun dealWithUrl(view: WebView, url: String): Boolean {
        return false
    }

    override fun initData() {
        webView.loadUrl("https://www.panda.tv/all?pdt=1.18.pheader-n.1.1sijfa2v86j")
    }

}