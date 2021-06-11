package com.moo.app2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.moo.moouidservice.UidAidlInterface
import com.moo.moouidservice.bean.AidlBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mUidAidlInterface: UidAidlInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val conn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mUidAidlInterface = UidAidlInterface.Stub.asInterface(service)
                text.text = "链接服成功！"
                groupMore.visibility = View.VISIBLE
            }
        }
        text.setOnClickListener {
            val intent = Intent()
            intent.action = "com.moo.moouidservice.MyAidlService"
            intent.`package` = "com.moo.demogo"
            bindService(intent, conn, Context.BIND_AUTO_CREATE)
        }
        text1.setOnClickListener {
            val aidlBean = AidlBean()
            aidlBean.uid = 100
            Log.e("aidl", "客户端发送：" + aidlBean.uid)
            mUidAidlInterface?.setClientUid(aidlBean)
        }
        text2.setOnClickListener {
            val aidlUid = mUidAidlInterface?.aidlUid
            Log.e("aidl", "客户端获取：" + aidlUid?.uid)
        }
        text3.setOnClickListener {
            val aidlUids = mUidAidlInterface?.aidlUids
            val sb = StringBuilder()
            if (aidlUids != null) {
                for (uid in aidlUids) {
                    sb.append("${uid.uid}  ")
                }
            }

            Log.e("aidl", "客户端获取：$sb")
        }
    }
}
