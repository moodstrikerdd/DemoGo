package com.moo.demogo.mainframe.share

import android.content.ComponentName
import android.content.Intent
import android.view.View
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.utils.runtimepermission.Permission
import com.moo.demogo.utils.runtimepermission.RuntimePermissionHelper

class ShareActivity : BaseActivity() {

    companion object {
        const val REQ_PICK_PICTURE_FROM_GALLERY = 1000
    }

    override fun getLayoutId() = R.layout.activity_share

    fun shareAll(view: View) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "标题：标题\n副标题：副标题\n链接：http://dist.cheguo.com/#/ws/index")
        startActivity(Intent.createChooser(intent, "分享"))
    }

    fun shareImg(view: View) {
        RuntimePermissionHelper.permissions.clear()
        RuntimePermissionHelper.permissions.addAll(Permission.STORAGE)
        RuntimePermissionHelper.requestPermissions2(this)
    }

    fun shareWeChat(view: View) {
        val intent = Intent()
//        intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
        intent.setPackage("com.tencent.mm")
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
        intent.putExtra(Intent.EXTRA_TEXT, "标题：标题\n副标题：副标题\n链接：http://dist.cheguo.com/#/ws/index")
//        startActivity(Intent.createChooser(intent, "分享"))
        startActivity(intent)
    }

    fun shareQq(view: View) {
        val intent = Intent()
        intent.component = ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "标题：标题\n副标题：副标题\n链接：http://dist.cheguo.com/#/ws/index")
        startActivity(Intent.createChooser(intent, "分享"))
    }

    override fun initData() {
    }

    override fun initView() {
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        RuntimePermissionHelper.onRequestPermissionsResult2(this, requestCode, permissions, grantResults) {
            toAlbum()
        }
    }

    private fun toAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQ_PICK_PICTURE_FROM_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_PICK_PICTURE_FROM_GALLERY) {
            if (data == null || data.data == null) {
                return
            }
            val uri = data.data
            var intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "image/jpg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
            intent.putExtra(Intent.EXTRA_TEXT, "HI 推荐您使用一款软件")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent = Intent.createChooser(intent, "分享")
            startActivity(intent)
        }
    }
}
