package com.moo.demogo.mainframe.encryp

import android.text.TextUtils
import android.util.Base64
import android.view.View
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.mainframe.ioc.AnnotationUtils
import com.moo.demogo.mainframe.ioc.OnClick
import com.moo.demogo.utils.encryp.RSAUtils
import com.moo.demogo.utils.encryp.RsaEncryptUtils
import com.moo.demogo.utils.loge
import kotlinx.android.synthetic.main.activity_encryp.*
import org.jetbrains.anko.toast

class EncrypActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_encryp
    override fun initData() {
        AnnotationUtils().injectOnClick(this)
    }

    override fun initView() {
        val a = "@"
        val b = "Man"

        loge(message = String(Base64.encode(a.toByteArray(), Base64.DEFAULT)))


        loge(message = String(Base64.encode(b.toByteArray(), Base64.DEFAULT)))
        loge(message = String(Base64.encode(b.toByteArray(), Base64.CRLF)))
        loge(message = String(Base64.encode(b.toByteArray(), Base64.NO_CLOSE)))
        loge(message = String(Base64.encode(b.toByteArray(), Base64.NO_PADDING)))
        loge(message = String(Base64.encode(b.toByteArray(), Base64.NO_WRAP)))


        val message = String(Base64.encode(b.toByteArray(), Base64.DEFAULT))

        loge(message = String(Base64.decode(message, Base64.DEFAULT)))
        loge(message = String(Base64.decode(message, Base64.CRLF)))
        loge(message = String(Base64.decode(message, Base64.NO_CLOSE)))
        loge(message = String(Base64.decode(message, Base64.NO_PADDING)))
        loge(message = String(Base64.decode(message, Base64.NO_WRAP)))
    }

    fun intToByteArray(a: Int): ByteArray {
        return byteArrayOf((a shr 24 and 0xFF).toByte(), (a shr 16 and 0xFF).toByte(), (a shr 8 and 0xFF).toByte(), (a and 0xFF).toByte())
    }

    private var maps: Map<String, String>? = null
    private var rsaEncryptByPublicKey: String? = null
    @OnClick(R.id.btnInit, R.id.btnEncode, R.id.btnDecode)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btnInit -> {
                maps = RsaEncryptUtils.initKey()
                tvContent.append("公钥：${maps!![RsaEncryptUtils.PUBLIC_KEY]}\r\n")
                tvContent.append("私钥：${maps!![RsaEncryptUtils.PRIVATE_KEY]}\r\n")
            }
            R.id.btnEncode -> {
                val str = etString.text.toString().trim()
                if (maps == null) {
                    toast("请先初始化秘钥")
                } else if (TextUtils.isEmpty(str)) {
                    toast("请先输入需要加密文字")
                } else {
                    rsaEncryptByPublicKey = RsaEncryptUtils.RsaEncryptByPublicKey(str, maps!![RsaEncryptUtils.PUBLIC_KEY])
                    tvContent.append("加密后：$rsaEncryptByPublicKey\r\n")
                }
            }
            R.id.btnDecode -> {
                when {
                    maps == null -> toast("请先初始化秘钥")
                    rsaEncryptByPublicKey == null -> toast("请先加密字符串")
                    else -> {
                        val privateKey = RSAUtils.loadPrivateKey(maps!![RsaEncryptUtils.PRIVATE_KEY])
                        val rsaEncryptByPublicKey = RSAUtils.decryptData(Base64.decode(rsaEncryptByPublicKey, Base64.DEFAULT), privateKey)
                        tvContent.append("解密后：${String(rsaEncryptByPublicKey)}\r\n")
                        loge(message = tvContent.text.toString().trim())
                    }
                }
            }
        }
    }
}