package com.zx.simplepermission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zx.permission_core.annotation.CheckPermission
import com.zx.permission_core.annotation.PermissionDenied
import com.zx.permission_core.annotation.PermissionDeniedAndNoLongerAsk
import kotlinx.android.synthetic.main.activity_test_permission.*


/**
 * 测试权限,同一个页面多组不同的权限，根据requestCode来区分；
 * 支持在Activity、Fragment、Dialog、PopupWindow或者其他View中使用
 */
class TestPermissionActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TestPermissionActivity"
        const val REQUEST_CODE_EXTERNAL_STORAGE = 1
        const val REQUEST_CODE_CALL_PHONE = 2
        const val permissionDescribe = "应用需要获取相机权限，若不允许，你将无法使用拍照上传附件、拍照上传头像和扫描二维码的功能。"
        const val permissionDescribe2 = "应用需要获取拨号权限，若不允许，你将无法使用拨打电话的功能。"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_permission)

        btn1.setOnClickListener {
            doInExternalStorage()
        }
        btn2.setOnClickListener {
            callPhone("123")
        }
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            TestPermissionFragment()
        ).commit()
    }

    @CheckPermission(
        value = [Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE],
        requestCode = REQUEST_CODE_EXTERNAL_STORAGE,
        describe = permissionDescribe
    )
    fun doInExternalStorage() {
        //在外部存储上操作
        Log.e(TAG, "doInExternalStorage: 在外部存储上做一些读写操作")
        Toast.makeText(this, "在外部存储上做一些读写操作", Toast.LENGTH_SHORT).show()
    }


    @PermissionDenied(requestCode = 1)
    private fun deny2() {
        Log.e(TAG, "deny2:--- 我就是不小心写重复了的 ")
    }

    @PermissionDenied(requestCode = REQUEST_CODE_EXTERNAL_STORAGE)
    private fun deny() {
        Log.e(TAG, "deny:--- @PermissionDenied ")
        Toast.makeText(this, "开发者自己处理【@PermissionDenied】逻辑", Toast.LENGTH_SHORT).show()
    }

    @PermissionDeniedAndNoLongerAsk(requestCode = REQUEST_CODE_EXTERNAL_STORAGE)
    private fun noLongerAsk() {
        Log.e(TAG, "noLongerAsk:--- @PermissionDeniedAndNoLongerAsk ")
        Toast.makeText(this, "开发者自己处理【@PermissionDeniedAndNoLongerAsk】逻辑", Toast.LENGTH_SHORT)
            .show()
    }


    @SuppressLint("MissingPermission")
    @CheckPermission(
        value = [Manifest.permission.CALL_PHONE],
        requestCode = REQUEST_CODE_CALL_PHONE,
        describe = permissionDescribe2
    )
    private fun callPhone(phoneNum: String) {
        // 拨打电话（直接拨打电话）
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        startActivity(intent)
    }

    @PermissionDenied(requestCode = REQUEST_CODE_CALL_PHONE)
    private fun denyCallPhone() {
        Log.e(TAG, "denyCallPhone:--- @PermissionDenied ")
        Toast.makeText(this, "开发者自己处理【@PermissionDenied】逻辑", Toast.LENGTH_SHORT).show()
    }


    @PermissionDeniedAndNoLongerAsk(requestCode = REQUEST_CODE_CALL_PHONE)
    private fun noLongerAsk2() {
        Log.e(TAG, "noLongerAsk2:--- @PermissionDeniedAndNoLongerAsk ")
        Toast.makeText(this, "开发者自己处理【@PermissionDeniedAndNoLongerAsk】逻辑", Toast.LENGTH_SHORT)
            .show()
    }


}
