package com.zx.simplepermission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.zx.permission_core.annotation.CheckPermission
import com.zx.permission_core.annotation.PermissionDenied
import com.zx.permission_core.annotation.PermissionDeniedAndNoLongerAsk

/**
 * Fragment中使用SimplePermission
 */
class TestPermissionFragment : Fragment() {

    companion object {
        const val TAG = "TestPermissionFragment"
        const val REQUEST_CODE_CAMERA = 3
        const val permissionDescribe = "应用需要获取相机权限，若不允许，你将无法使用拍照上传附件、拍照上传头像和扫描二维码的功能。"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_test_permission, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        view.findViewById<Button>(R.id.btn).setOnClickListener {
            openCamera()
        }
    }

    @CheckPermission(
        value = [Manifest.permission.CAMERA],
        requestCode = REQUEST_CODE_CAMERA,
        describe = permissionDescribe
    )
    fun openCamera() {
        //打开相机
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory("android.intent.category.DEFAULT")
        startActivityForResult(intent, 1)
    }


    @PermissionDenied(requestCode = REQUEST_CODE_CAMERA)
    private fun deny() {
        Log.e(TAG, "deny:--- @PermissionDenied ")
        Toast.makeText(context, "开发者自己处理【@PermissionDenied】逻辑", Toast.LENGTH_SHORT).show()
    }

    @PermissionDeniedAndNoLongerAsk(requestCode = REQUEST_CODE_CAMERA)
    private fun noLongerAsk() {
        Log.e(TAG, "noLongerAsk:--- @PermissionDeniedAndNoLongerPrompt ")
        Toast.makeText(context, "开发者自己处理【@PermissionDeniedAndNoLongerPrompt】逻辑", Toast.LENGTH_SHORT)
            .show()
    }
}
