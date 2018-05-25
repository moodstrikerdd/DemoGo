package com.moo.demogo.mainframe.video

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.widget.MediaController
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.utils.AppUtils
import com.moo.demogo.utils.SPUtils
import com.moo.demogo.utils.toast
import kotlinx.android.synthetic.main.activity_video.*
import java.io.File
import java.io.IOException


class VideoActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_video

    companion object {
        const val REQUEST_CODE_CAMERA_PERMISSION = 100
        const val REQUEST_CODE_CAPTURE_VIDEO_ACTIVITY = 200
    }


    private lateinit var videoFile: File
    private lateinit var videoFileUri: Uri

    override fun initView() {
        videoFile = File(getExternalFilesDir("video"), "demo.mp4")
        videoView.setMediaController(MediaController(this))
        videoView.setOnCompletionListener {
            toast("播放完成")
        }
        button.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                toCamera()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                        || SPUtils.get(Manifest.permission.CAMERA, false) as Boolean) {
                    AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("当前应用缺少读写权限。\n请点击\"设置\",\"权限\"打开相应权限")
                            .setPositiveButton("设置") { _, _ -> AppUtils.getAppDetailSettingIntent(this@VideoActivity) }
                            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                            .create()
                            .show()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA_PERMISSION)
                    SPUtils.put(Manifest.permission.CAMERA, true)
                }
            }
        }
    }


    private fun toCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        try {
            videoFileUri = Uri.fromFile(videoFile)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                videoFileUri = FileProvider.getUriForFile(this, "com.moo.demogo.fileprovider", videoFile)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri)  // set the image file name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1) // set the video image quality to high
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_VIDEO_ACTIVITY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            grantResults.filter { it != PackageManager.PERMISSION_GRANTED }
                    .forEach {
                        toast(message = "必须同意所有该类权限才能使用本程序")
                        return
                    }
            toCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE_CAPTURE_VIDEO_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                toast(message = "Video saved to:\n" + data.data!!)
                //Display the video
                videoView.setVideoURI(videoFileUri)
                videoView.requestFocus()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }


    override fun initData() {
    }
}
