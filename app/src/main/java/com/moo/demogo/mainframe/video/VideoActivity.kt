package com.moo.demogo.mainframe.video

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.Button
import android.widget.MediaController
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.utils.UriUtils
import com.moo.demogo.utils.runtimepermission.Permission
import com.moo.demogo.utils.runtimepermission.RuntimePermissionHelper
import com.moo.demogo.utils.toast
import kotlinx.android.synthetic.main.activity_video.*
import java.io.File
import java.io.IOException


class VideoActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_video

    companion object {
        const val REQUEST_CODE_CAPTURE_VIDEO_ACTIVITY = 200
        const val REQUEST_CODE_ALBUM_VIDEO_ACTIVITY = 100
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
            RuntimePermissionHelper.permissions.clear()
            RuntimePermissionHelper.permissions.addAll(Permission.CAMERA)
            RuntimePermissionHelper.permissions.addAll(Permission.STORAGE)
            RuntimePermissionHelper.permissions.addAll(Permission.PHONE)
            if (RuntimePermissionHelper.checkPermissions(this)) {
                toCamera()
            } else {
                RuntimePermissionHelper.requestPermissions(this)
            }
        }
        button2.setOnClickListener {
            if ((it as Button).text == "播放视频") {
                videoView.start()
                it.text = "显示图片"
                image.visibility = View.GONE
            } else {
                videoView.pause()
                it.text = "播放视频"
                image.visibility = View.VISIBLE
            }
        }

        button3.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CODE_ALBUM_VIDEO_ACTIVITY)
        }
    }

    private fun toCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        try {
            videoFileUri = Uri.fromFile(videoFile)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                videoFileUri = FileProvider.getUriForFile(this, "com.moo.demogo.fileprovider", videoFile)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri)
        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        //限制录制时间10秒
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10)
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_VIDEO_ACTIVITY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        RuntimePermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults) {
            toCamera()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        if (requestCode == REQUEST_CODE_CAPTURE_VIDEO_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                showVideo()
            }
        } else if (requestCode == REQUEST_CODE_ALBUM_VIDEO_ACTIVITY) {
            videoFileUri = data.data
            val path = UriUtils.getPath(this, videoFileUri)
            videoFile = File(path)
            showVideo()
        }
    }

    private fun showVideo() {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(videoFile.absolutePath)
        val frameAtTime = mediaMetadataRetriever.frameAtTime
        image.setImageBitmap(frameAtTime)
        videoView.setVideoURI(videoFileUri)
        videoView.requestFocus()
    }


    override fun initData() {
    }
}
