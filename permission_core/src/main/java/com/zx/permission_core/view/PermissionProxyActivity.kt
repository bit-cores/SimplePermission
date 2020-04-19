package com.zx.permission_core.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.zx.permission_core.PermissionManager

/**
 * @descripe 通过启动一个透明Activity的方式，来申请权限／处理权限请求回调
 *
 * @author zx
 * @e-mail 374952705@qq.com
 * @time   2020/4/13
 */


class PermissionProxyActivity : Activity() {
    val TAG = "PermissionProxyActivity"

    companion object {
        private var activity: Activity? = null
        var mRequestCode: Int = 0
        lateinit var mPermissionCallbacks: PermissionManager.PermissionCallbacks
        lateinit var mPermissions: Array<String>
        lateinit var mDescription: String

        fun requestPermission(
            context: Context, permissions: Array<String>,
            requestCode: Int, description: String,
            callbacks: PermissionManager.PermissionCallbacks
        ) {
            mRequestCode = requestCode
            mPermissionCallbacks = callbacks
            mPermissions = permissions
            mDescription = description

            var intent = Intent(context, PermissionProxyActivity::class.java)
            context.startActivity(intent)
        }

        fun handlePermissionsDenied() {
            PermissionManager.explainPermission(
                activity!!,
                mPermissions,
                mRequestCode,
                mDescription
            )
        }

        fun handlePermissionsDeniedAndNoLongerPrompt() {
            PermissionManager.guidePermissionSetting(
                activity!!,
                mDescription
            )
        }

        fun finishSelf() {
            activity?.finish()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = this
        //设置背景透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //申请权限
        PermissionManager.requestPermission(
            this,
            mPermissions,
            mRequestCode
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        //防止静态变量造成的内存泄漏
        activity = null
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (mRequestCode == requestCode && PermissionManager.verifyPermissions(
                permissions,
                grantResults
            )
        ) {
            //用户同意授权，拿到这个组的全部权限
            mPermissionCallbacks.onPermissionsGranted()
            finish()
        } else {
            /**
             * 检查APP是否应该向用于展示申请权限的解释
             *
             * 1、APP申请指定权限时被用户拒绝，但未勾选【不再提示】，返回true
             * 2、当APP从未申请过指定的权限或申请了指定权限，但被用户拒绝，且勾选了【不再提示】，返回false；
             *
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                mPermissionCallbacks.onPermissionsDenied(requestCode, permissions)
            } else {
                //用户选择了拒绝的时候勾选不再提醒
                mPermissionCallbacks.onPermissionsDeniedAndNoLongerPrompt()
            }
        }
    }
}