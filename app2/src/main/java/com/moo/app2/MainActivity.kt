package com.moo.app2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.moo.demogo.IMyAidlInterface
import com.moo.demogo.bean.AidlBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val conn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
                val aidlBean = AidlBean()
                aidlBean.uid = 100
                iMyAidlInterface.getProcessUid(aidlBean)
                Log.e("app2", "uid=${aidlBean.uid}")
            }
        }
        text.setOnClickListener {
            val intent = Intent()
            intent.action = "com.moo.demogo.MyAidlService"
            intent.`package` = "com.moo.demogo"
            bindService(intent,conn, Context.BIND_AUTO_CREATE)
        }
    }
}
