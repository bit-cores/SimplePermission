package com.zx.permission_core

import android.content.Context
import android.util.Log
import com.zx.permission_core.annotation.CheckPermission
import com.zx.permission_core.annotation.PermissionDenied
import com.zx.permission_core.annotation.PermissionDeniedAndNoLongerAsk
import com.zx.permission_core.view.PermissionProxyActivity
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.lang.Exception
import java.lang.reflect.Method

/**
 * @author zhouxu
 * @descripe 检查请求权限
 * @e-mail 374952705@qq.com
 * @time 2020/4/14
 */

@Aspect
class AndroidPermissionAspect {

    val TAG = "AndroidPermissionAspect"
    private lateinit var obj: Any
    private var mRequestCode: Int = 0

    @Around("execution(@com.zx.permission_core.annotation.CheckPermission  * *(..))")
    fun check(proceedingJoinPoint: ProceedingJoinPoint) {
        var signature: MethodSignature = proceedingJoinPoint.signature as MethodSignature
        var annotation: CheckPermission = signature.method.getAnnotation(
            CheckPermission::class.java)
        var perms: Array<String> = annotation.value
        mRequestCode = annotation.requestCode
        var descibetion = annotation.describe
        Log.e(TAG, "perms---: $perms")
        Log.e(TAG, "requestCode---: $mRequestCode")

        obj = proceedingJoinPoint.`this` //当前正在执行的对象,也就是方法所在的类对象
        Log.e(TAG, "this--: $obj")


        var context: Context? = PermissionManager.getContext(proceedingJoinPoint.`this`)
        if (PermissionManager.hasPermissions(context!!, perms)) {
            proceedingJoinPoint.proceed()
        } else {
            PermissionProxyActivity.requestPermission(context, perms, mRequestCode, descibetion
                , object : PermissionManager.PermissionCallbacks {
                    override fun onPermissionsGranted() {
                        proceedingJoinPoint.proceed()
                    }

                    override fun onPermissionsDenied(requestCode: Int, perms: Array<String>) {
                        handlePermissionCallBack(PermissionDenied::class.java)
                    }

                    override fun onPermissionsDeniedAndNoLongerPrompt() {
                        handlePermissionCallBack(PermissionDeniedAndNoLongerAsk::class.java)
                    }
                })
        }
    }

    /**
     * 处理权限回调
     */
    private fun handlePermissionCallBack(permissionType: Class<out Annotation>) {
        var invokeMethod = findMethod(permissionType)
        /**
         * 没有使用@PermissionDenied、@PermissionDeniedAndNoLongerPrompt注解的方法，
         * 则使用框架默认的拒绝处理逻辑
         */
        if (invokeMethod == null) {
            when (permissionType) {
                PermissionDenied::class.java -> {
                    PermissionProxyActivity.handlePermissionsDenied()
                }
                PermissionDeniedAndNoLongerAsk::class.java -> {
                    PermissionProxyActivity.handlePermissionsDeniedAndNoLongerPrompt()
                }
            }
        } else {
            PermissionProxyActivity.finishSelf()
            //调用拒绝权限的注解标记的相关方法
            invokeMethod?.invoke(obj)
        }
    }

    /**
     * 查找切面类中权限注解标记的方法
     * 反射遍历类的方法是无序的，如果一组权限重写了2次相同注解的方法，只有其中一个有效
     */
    private fun findMethod(permissionType: Class<out Annotation>): Method? {
        var invokeMethod: Method? = null
        var declaredMethods: Array<Method> = obj.javaClass.declaredMethods
        for (method: Method in declaredMethods) {
            if (method.isAnnotationPresent(permissionType)) {
                //为了解决一个页面内，有多次申请权限的情况
                var requestCode = getAnnotationValue(method.getAnnotation(permissionType))
                if (mRequestCode == requestCode) {
                    method.isAccessible = true
                    invokeMethod = method
                    break
                }
            }
        }
        return invokeMethod
    }

    private fun getAnnotationValue(annotation: Annotation?): Int {
        return when (annotation) {
            is PermissionDenied -> annotation.requestCode
            is PermissionDeniedAndNoLongerAsk -> annotation.requestCode
            else -> throw Exception("not found method with @PermissionDenied、@PermissionDeniedAndNoLongerPrompt")
        }
    }


}