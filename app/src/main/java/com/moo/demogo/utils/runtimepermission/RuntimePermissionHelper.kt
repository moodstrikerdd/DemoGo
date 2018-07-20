package com.moo.demogo.utils.runtimepermission

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import com.moo.demogo.utils.AppUtils
import com.moo.demogo.utils.SPUtils
import com.moo.demogo.utils.toast

/**
 * @author moodstrikerdd
 * @date 2018/5/31
 * @label 运行时权限帮助类
 */
object RuntimePermissionHelper {
    val permissions = arrayListOf<String>()

    private val permissionNotAllowed = arrayListOf<String>()

    private const val REQUEST_CODE_PERMISSION = 1001

    private var requestCode = 0

    /**
     * 检查相应权限是否已经获取，没获取的权限放入permissionNotAllowed List中
     */
    fun checkPermissions(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            return true
        }
        permissionNotAllowed.clear()
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED) {
                permissionNotAllowed.add(it)
            }
        }
        return permissionNotAllowed.isEmpty()
    }

    /**
     * 方式一 请求权限
     * 先判断shouldShowRequestPermissionRationale，根据返回值决定是跳转设置界面还是请求权限
     */
    fun requestPermissions(activity: Activity, requestCode: Int = REQUEST_CODE_PERMISSION) {
        if (Build.VERSION.SDK_INT < 23) {
            return
        }
        for (i in 0 until permissionNotAllowed.size) {
            val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionNotAllowed[i])
            val requested = SPUtils.get(permissionNotAllowed[i], false) as Boolean
            if (requested && !shouldShow) {
                AlertDialog.Builder(activity)
                        .setTitle("提示")
                        .setMessage("当前应用缺少权限:${Permission.getPermissionName(permissionNotAllowed.toTypedArray())}。请点击\"设置\",\"权限\"打开相应权限")
                        .setPositiveButton("设置") { _, _ -> AppUtils.getAppDetailSettingIntent(activity) }
                        .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                return
            }
        }
        RuntimePermissionHelper.requestCode = requestCode
        permissionNotAllowed.forEach { SPUtils.put(it, true) }
        ActivityCompat.requestPermissions(activity, permissionNotAllowed.toTypedArray(), requestCode)
    }


    /**
     * 方式二 直接请求所有权限
     */
    fun requestPermissions2(activity: Activity, requestCode: Int = REQUEST_CODE_PERMISSION) {
        if (Build.VERSION.SDK_INT < 23) {
            return
        }
        RuntimePermissionHelper.requestCode = requestCode
        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
    }

    /**
     * 方式一 结果回调 只存在 获取权限失败和成功
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, success: (requestCode: Int) -> Unit) {
        if (requestCode == RuntimePermissionHelper.requestCode) {
            for (i in 0 until permissions.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    toast(message = "必须同意所有该类权限才能使用本程序")
                    return
                }
            }
            success(requestCode)
        }
    }

    /**
     * 方式二 结果回调
     * 在回调结果中判断 shouldShowRequestPermissionRationale
     * 三种结果
     * 1.提示获取权限失败
     * 2.跳转设置界面
     * 3.获取权限成功
     */
    fun onRequestPermissionsResult2(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray, success: (requestCode: Int) -> Unit) {
        if (requestCode == RuntimePermissionHelper.requestCode) {
            val permissionsNotAllowed = permissions.filterIndexed { index, _ ->
                grantResults[index] != PackageManager.PERMISSION_GRANTED
            }
            for (i in 0 until permissions.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        toast(message = "必须同意${Permission.getPermissionName(permissionsNotAllowed.toTypedArray())}权限才能使用本程序")
                    } else {
                        AlertDialog.Builder(activity)
                                .setTitle("提示")
                                .setMessage("当前应用缺少权限:${Permission.getPermissionName(permissionsNotAllowed.toTypedArray())}。请点击\"设置\",\"权限\"打开相应权限")
                                .setPositiveButton("设置") { _, _ -> AppUtils.getAppDetailSettingIntent(activity) }
                                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                                .create()
                                .show()
                    }
                    return
                }
            }
            success(requestCode)
        }
    }
}