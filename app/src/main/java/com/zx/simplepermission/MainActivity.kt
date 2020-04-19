package com.zx.simplepermission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker


/**
 * 基于AOP的动态权限库
 * 关联Activity、Fragment生命周期、任意地方使用
 */
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    val OPEN_CAMERA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        check()
//        testPerm()
        startActivity(Intent(this,TestPermissionActivity::class.java))
        finish()
    }

//    @PermissionDenied()
//    private fun test1(){
//
//    }

//    @Permission(value = [Manifest.permission.CAMERA], requestCode = 1)
//    private fun testPerm() {
//    }

    // 拍照并显示图片
    private fun openCamera() {
        //打开相机
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory("android.intent.category.DEFAULT")
        startActivityForResult(intent, 1)
    }

    private fun check() {
        val permissions = arrayOf<String>(Manifest.permission.CAMERA)
        if (hasPermissions(this, permissions)) {
            Log.d(TAG, "check: hasPermissions true")
            openCamera()
        } else {
            //申请权限
            requestPermission(permissions, OPEN_CAMERA)
        }
    }

    /**
     * 申请权限
     */
    private fun requestPermission(perms: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, requestCode)
        }
    }


    /**
     * 检查权限是否已经获取
     */
    private fun hasPermissions(context: Context, perms: Array<String>): Boolean {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d(TAG, "hasPermissions: API version < M, returning true by default")
            return true
        }
        requireNotNull(context) { "Can't check permissions for null context" }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OPEN_CAMERA) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                openCamera()
            } else {
                /**
                 * 检查APP是否应该向用于展示申请权限的解释
                 *
                 * 1、APP申请指定权限时被用户拒绝，但未勾选【不再提示】，返回true
                 * 2、当APP从未申请过指定的权限或申请了指定权限，但被用户拒绝，且勾选了【不再提示】，返回false；
                 *
                 */
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    hintPermission()
                } else {
                    Log.e(TAG, "shouldShowRequestPermissionRationale false: ")
                    //用户选择了拒绝的时候勾选不再提醒
                    val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.data = Uri.fromParts("package", getPackageName(), null)
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent)
                    }
                }
            }
        }
    }

    /**
     * 用户不同意，向用户展示该权限作用
     */
    private fun hintPermission() {
        AlertDialog.Builder(this)
            .setMessage("需要相机权限")
            .setPositiveButton("OK", { dialog1, which ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    OPEN_CAMERA
                )
            })
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }


}
