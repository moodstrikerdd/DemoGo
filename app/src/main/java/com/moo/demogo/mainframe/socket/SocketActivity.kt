package com.moo.demogo.mainframe.socket

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.os.StrictMode
import android.text.TextUtils
import android.view.View
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.mainframe.ioc.AnnotationUtils
import com.moo.demogo.mainframe.ioc.OnClick
import com.moo.demogo.utils.loge
import com.moo.demogo.utils.toast
import com.moo.demogo.utils.tryCatch
import kotlinx.android.synthetic.main.activity_socket.*
import okhttp3.WebSocket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executors


class SocketActivity : BaseActivity() {
    override fun doInBeforeSetContent() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build())
    }

    override fun getLayoutId() = R.layout.activity_socket

    lateinit var socket: Socket
    lateinit var bufferedReader: BufferedReader
    lateinit var printWriter: PrintWriter

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val toString = msg.obj.toString()
            tvMessage.append("$toString\n")
        }
    }

    override fun initData() {
    }

    fun start() {
        tryCatch {
            while (true) {
                loge(message = "开始读取信息")
                if (socket.isConnected && !socket.isInputShutdown) {
                    val readLine = bufferedReader.readLine()
                    loge(message = "读取信息：" + readLine)
                    if (!TextUtils.isEmpty(readLine)) {
                        val message = handler.obtainMessage()
                        message.obj = readLine
                        handler.sendMessage(message)
                    }
                }
            }
        }
    }

    override fun initView() {
        AnnotationUtils().injectOnClick(this)
    }

    @OnClick(R.id.btnConnect, R.id.btnSend)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btnConnect -> {
                Executors.newSingleThreadExecutor().execute {
                    tryCatch {
                        socket = Socket(etIp.text.toString(), etPort.text.toString().toInt())
                        socket.tcpNoDelay = true
                        bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                        printWriter = PrintWriter(OutputStreamWriter(socket.getOutputStream()))
                        start()
                    }
                }
            }
            R.id.btnSend -> {
                Executors.newSingleThreadExecutor().execute {
                    tryCatch {
                        val message = etMessage.text.toString()
                        if (TextUtils.isEmpty(message)) {
                            toast(message = "请输入内容")
                        } else {
                            if (socket.isConnected && !socket.isOutputShutdown) {
                                printWriter.println(message)
                                printWriter.flush()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        Executors.newSingleThreadExecutor().execute {
            tryCatch {
                val message = "own exit"
                if (socket.isConnected && !socket.isOutputShutdown) {
                    printWriter.println(message)
                    printWriter.flush()
                }
                bufferedReader.close()
                printWriter.close()
                socket.close()
            }
        }
        super.onDestroy()
    }
}