package com.moo.demogo.utils.runtimepermission

import android.Manifest
import android.os.Build
import android.text.TextUtils


/**
 * @author moodstrikerdd
 * @date 2018/5/31
 * @label 6.0运行时权限
 */
object Permission {

    /**
     *读写日历。
     */
    val CALENDAR: Array<String>
    /**
     * 相机。
     */
    val CAMERA: Array<String>
    /**
     * 读写联系人。
     */
    val CONTACTS: Array<String>
    /**
     * 读位置信息。
     */
    val LOCATION: Array<String>
    /**
     * 使用麦克风。
     */
    val MICROPHONE: Array<String>
    /**
     * 读电话状态、打电话、读写电话记录。
     */
    val PHONE: Array<String>
    /**
     * 传感器。
     */
    val SENSORS: Array<String>
    /**
     * 读写短信、收发短信。
     */
    val SMS: Array<String>
    /**
     * 读写存储卡。
     */
    val STORAGE: Array<String>

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CALENDAR = arrayOf()
            CAMERA = arrayOf()
            CONTACTS = arrayOf()
            LOCATION = arrayOf()
            MICROPHONE = arrayOf()
            PHONE = arrayOf()
            SENSORS = arrayOf()
            SMS = arrayOf()
            STORAGE = arrayOf()
        } else {
            CALENDAR = arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR)

            CAMERA = arrayOf(
                    Manifest.permission.CAMERA)

            CONTACTS = arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS)

            LOCATION = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)

            MICROPHONE = arrayOf(
                    Manifest.permission.RECORD_AUDIO)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PHONE = arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_PHONE_NUMBERS,
                        Manifest.permission.ANSWER_PHONE_CALLS,
                        Manifest.permission.ADD_VOICEMAIL,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.PROCESS_OUTGOING_CALLS)
            } else {
                PHONE = arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ADD_VOICEMAIL,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.PROCESS_OUTGOING_CALLS)
            }

            SENSORS = arrayOf(
                    Manifest.permission.BODY_SENSORS)

            SMS = arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS)

            STORAGE = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    /**
     * 获取单个权限的名称
     */
    private fun getPermissionName(permission: String): String {
        var permissionName = ""
        when {
            CALENDAR.contains(permission) -> permissionName = "日历"
            CAMERA.contains(permission) -> permissionName = "相机"
            CONTACTS.contains(permission) -> permissionName = "联系人"
            LOCATION.contains(permission) -> permissionName = "定位"
            MICROPHONE.contains(permission) -> permissionName = "麦克风"
            PHONE.contains(permission) -> permissionName = "电话"
            SENSORS.contains(permission) -> permissionName = "传感器"
            SMS.contains(permission) -> permissionName = "短信"
            STORAGE.contains(permission) -> permissionName = "读写存储"
        }
        return permissionName
    }

    /**
     * 获取权限的拼接名称
     */
    fun getPermissionName(permission: Array<String>): String {
        val sb = StringBuilder()
        permission.forEach {
            val permissionName = getPermissionName(it)
            if (!TextUtils.isEmpty(permissionName) && !sb.contains(permissionName)) {
                sb.append("$permissionName、")
            }
        }
        if (sb.isNotEmpty()) {
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }
}