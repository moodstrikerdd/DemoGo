package com.moo.demogo.utils.runtimepermission

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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

    fun checkPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            return true
        }
        permissionNotAllowed.clear()
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                permissionNotAllowed.add(it)
            }
        }
        return permissionNotAllowed.isEmpty()
    }

    fun requestPermissions(activity: Activity, requestCode: Int = REQUEST_CODE_PERMISSION) {
        if (Build.VERSION.SDK_INT < 23) {
            return
        }
        RuntimePermissionHelper.requestCode = requestCode
        ActivityCompat.requestPermissions(activity, permissionNotAllowed.toTypedArray(), requestCode)
    }

    fun requestPermissionsWithoutCheck(activity: Activity, requestCode: Int = REQUEST_CODE_PERMISSION) {
        if (Build.VERSION.SDK_INT < 23) {
            return
        }
        RuntimePermissionHelper.requestCode = requestCode
        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
    }


    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray, success: (requestCode: Int) -> Unit) {
        if (requestCode == RuntimePermissionHelper.requestCode) {
            val permissionsNotAllowed = permissions.filterIndexed { index, _ ->
                grantResults[index] != PackageManager.PERMISSION_GRANTED
            }
            for (i in 0 until permissions.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        toast(message = "必须同意所有该类权限才能使用本程序")
                    } else {
                        AlertDialog.Builder(activity)
                                .setTitle("提示")
                                .setMessage("当前应用缺少权限:${Permission.getPermissionName(permissionsNotAllowed.toTypedArray())}。请点击\"设置\",\"权限\"打开相应权限")
                                .setPositiveButton("设置") { _, _ -> AppUtils.getAppDetailSettingIntent(activity) }
                                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                                .create()
                                .show()
                        SPUtils.put(permissions[i], true)
                    }
                    return
                }
            }
            success(requestCode)
        }
    }
}