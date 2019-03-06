package com.moo.demogo.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.webkit.WebView

object ViewShotUtils {
    /**
     * 截取View
     */
    fun viewShot(view: View): Bitmap {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return view.drawingCache
    }

    /**
     * 截取webview可视区域截图（需要调用webView.setDrawingCacheEnabled(true)）
     */
    fun webViewShot(webView: WebView) = webView.drawingCache


    fun webViewAllShot(webView: WebView, scale: Float): Bitmap {
        val width = webView.width
        val height = (webView.height * scale).toInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        webView.draw(canvas)
        return bitmap
    }

}