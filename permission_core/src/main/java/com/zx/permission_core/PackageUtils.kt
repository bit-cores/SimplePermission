package com.zx.permission_core

import android.content.Context
import android.content.pm.PackageManager

/**
 * @descripe
 * @author zhouxu
 * @e-mail 374952705@qq.com
 * @time 2020/4/15
 */
object PackageUtils {
    /**
     * 获取版本名称
     *
     * @param context 上下文
     *
     * @return 版本名称
     */
    fun getVersionName(context: Context): String? {
        val pm = context.packageManager
        //获取包信息
        try {
            val packageInfo =
                pm.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取版本号
     *
     * @param context
     *
     * @return 版本号
     */
    fun getVersionCode(context: Context): Int {
        val pm = context.packageManager
        try {
            val packageInfo =
                pm.getPackageInfo(context.packageName, 0)
            //返回版本号
            return packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取App的名称
     *
     * @param context
     *
     * @return 名称
     */
    fun getAppName(context: Context): String? {
        val pm = context.packageManager
        try {
            val packageInfo =
                pm.getPackageInfo(context.packageName, 0)
            //获取应用 信息
            val applicationInfo = packageInfo.applicationInfo
            //获取albelRes
            val labelRes = applicationInfo.labelRes
            //返回App的名称
            return context.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}