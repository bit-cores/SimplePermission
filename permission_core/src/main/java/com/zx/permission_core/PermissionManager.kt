package com.zx.permission_core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.zx.permission_core.view.ExplainPermissionDialog
import com.zx.permission_core.view.GuidePermissionSettingDialog
import org.aspectj.lang.ProceedingJoinPoint

/**
 * @descripe Permission Manage singTon
 *
 * @author zhouxu
 * @e-mail 374952705@qq.com
 * @time   2020/4/13
 */


object PermissionManager {

    val TAG = "PermissionManager"

    /**
     * Callback interface to receive the results of {@link PermissionProxyActivity#onRequestPermissionsResult}.
     * calls.
     */
    interface PermissionCallbacks {
        fun onPermissionsGranted()

        fun onPermissionsDenied(requestCode: Int, perms: Array<String>)

        /**
         * 拒绝且不再提示
         */
        fun onPermissionsDeniedAndNoLongerPrompt()
    }


    /**
     * 检查权限是否已经获取
     */
    fun hasPermissions(context: Context, perms: Array<String>): Boolean {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d(TAG, "hasPermissions: API version < M, returning true by default")
            return true
        }
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    perm
                ) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(
                    context,
                    perm
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


    /**
     * 申请权限
     */
    fun requestPermission(activity: Activity, perms: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, perms, requestCode)
        }
    }

    /**
     * 验证权限
     */
    fun verifyPermissions(permissions: Array<String>, grantResults: IntArray): Boolean {
        return permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 由于切点可能在Activity 、Fragment或者其他对象中，那么需要通过不同方式获取到context
     */
    fun getContext(obj: Any): Context? {
        return when (obj) {
            is Activity -> obj
            is Fragment -> obj.activity
            is Dialog -> obj.context
            is View -> obj.context
            else -> null
        }

    }

    /**
     * 打开APP设置，引导用户授权
     */
    fun openAppSetting(context: Context) {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", context.packageName, null)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * 如果用户拒绝，向用户解释权限的作用
     */
    fun explainPermission(
        activity: Activity, perms: Array<String>,
        requestCode: Int, describe: String
    ) {

        val dialog: ExplainPermissionDialog =
            object : ExplainPermissionDialog(activity, content = describe) {
                override fun authorize() {
                    super.authorize()
                    requestPermission(activity, perms, requestCode)
                }

                override fun cancelAction() {
                    super.cancelAction()
                    activity.finish()
                }
            }
        dialog.show()
    }

    /**
     * 如果用户拒绝且勾选了【不再提示】，引导用户打开设置去手动授权
     */
    fun guidePermissionSetting(
        activity: Activity, describe: String
    ) {
        val dialog: GuidePermissionSettingDialog =
            object : GuidePermissionSettingDialog(activity, content = describe) {
                override fun toSetting() {
                    super.toSetting()
                    openAppSetting(activity)
                }

                override fun cancelAction() {
                    super.cancelAction()
                    activity.finish()
                }
            }
        dialog.show()
    }
}