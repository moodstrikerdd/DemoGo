package com.moo.demogo.mainframe.camera

import android.graphics.PixelFormat
import android.hardware.Camera
import android.hardware.Camera.Size
import android.media.MediaRecorder
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import com.moo.demogo.R
import com.moo.demogo.base.BaseActivity
import com.moo.demogo.mainframe.ioc.AnnotationUtils
import com.moo.demogo.mainframe.ioc.OnClick
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

/**
 * @author moodstrikedd
 * @date 2018/10/30
 * @label
 */
class CameraActivity : BaseActivity(), SurfaceHolder.Callback {

    private lateinit var mCamera: Camera
    private var mSurfaceHolder: SurfaceHolder? = null
    private lateinit var mMediaRecorder: MediaRecorder


    override fun getLayoutId() = R.layout.activity_camera

    override fun initData() {

    }

    override fun initView() {
        AnnotationUtils().injectOnClick(this)
        val layoutParams = rlLayout.layoutParams
        layoutParams.height = (resources.displayMetrics.heightPixels * 0.6).toInt()
        rlLayout.layoutParams = layoutParams

        surfaceView.holder.setFormat(PixelFormat.TRANSPARENT)
        surfaceView.holder.setKeepScreenOn(true)
        surfaceView.holder.addCallback(this)
    }


    override fun onResume() {
        super.onResume()
        var numberOfCameras = Camera.getNumberOfCameras()
        mCamera = if (numberOfCameras == 2) {
            Camera.open(0)
        } else {
            Camera.open()
        }
        mCamera.setDisplayOrientation(90)
    }

    override fun onPause() {
        super.onPause()
        if (mCamera != null) {
            mCamera.release()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        mSurfaceHolder = holder
        if (mSurfaceHolder == null || mSurfaceHolder?.surface == null) {
            return
        }
        try {
            mCamera.setPreviewDisplay(holder)

            val parameters = mCamera.parameters
            val s = getCloselyPreSize(width, height, parameters.supportedPreviewSizes)
            parameters.setPreviewSize(s.width, s.height)

            //增加对聚焦模式的判断
            if (parameters.supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
            } else if (parameters.supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
            }

            mCamera.parameters = parameters
            mCamera.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth
     * 需要被进行对比的原宽
     * @param surfaceHeight
     * 需要被进行对比的原高
     * @param preSizeList
     * 需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected fun getCloselyPreSize(surfaceWidth: Int, surfaceHeight: Int,
                                    preSizeList: List<Size>): Size {
        val ReqTmpWidth = surfaceHeight
        val ReqTmpHeight = surfaceWidth
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (size in preSizeList) {
            if (size.width == ReqTmpWidth && size.height == ReqTmpHeight) {
                return size
            }
        }
        // 得到与传入的宽高比最接近的size
        val reqRatio = ReqTmpWidth.toFloat() / ReqTmpHeight
        var curRatio: Float
        var deltaRatio: Float
        var deltaRatioMin = java.lang.Float.MAX_VALUE
        var retSize: Camera.Size? = null
        for (size in preSizeList) {
            curRatio = size.width.toFloat() / size.height
            deltaRatio = Math.abs(reqRatio - curRatio)
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio
                retSize = size
            }
        }
        return retSize!!
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mSurfaceHolder = holder
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mSurfaceHolder = holder
    }

    @OnClick(R.id.start)
    public fun onClick(view: View) {
        val text = start.text
        if (text == "开始") {
            start()
            start.text = "暂停"
        } else if (text == "暂停") {
            mMediaRecorder.pause()
            start.text = "继续"
        } else if(text == "继续"){
            mMediaRecorder.resume()
            start.text = "结束"
        }else {
            stop()
            start.text = "开始"
        }

    }

    fun start() {
        mCamera.unlock()
        mMediaRecorder = MediaRecorder()
        mMediaRecorder.setCamera(mCamera)
        mMediaRecorder
                .setVideoSource(MediaRecorder.VideoSource.CAMERA)
        mMediaRecorder
                .setAudioSource(MediaRecorder.AudioSource.MIC)
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mMediaRecorder.setVideoSize(640, 480)
        mMediaRecorder.setVideoEncodingBitRate(600 * 1000)
        mMediaRecorder.setOrientationHint(90)
        val path = "${Environment.getExternalStorageDirectory()}/chedai/Video"
        val directory = File(path)
        if (!directory.exists())
            directory.mkdirs()
        try {
            val name = "Sign${System.currentTimeMillis()}.mp4"
            val mRecordPath = path + File.separator + name
            val mRecAudioFile = File(mRecordPath)
            mMediaRecorder.setOutputFile(mRecAudioFile
                    .absolutePath)
            mMediaRecorder.prepare()
            mMediaRecorder.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        mMediaRecorder.stop()
        mMediaRecorder.release()
    }
}
