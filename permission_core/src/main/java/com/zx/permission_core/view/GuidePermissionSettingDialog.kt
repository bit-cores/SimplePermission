package com.zx.permission_core.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.zx.permission_core.PackageUtils
import com.zx.permission_core.R
import kotlinx.android.synthetic.main.dialog_guide_permission_setting.*
import java.lang.Exception

/**
 * @descripe 引导权限设置
 *
 * @author zhouxu
 * @e-mail 374952705@qq.com
 * @time   2020/4/15
 */


open class GuidePermissionSettingDialog(context: Context, themeResId: Int = R.style.custom_dialog, content: String) :
    Dialog(context, themeResId) {

    init {
        //指定布局
        setContentView(R.layout.dialog_guide_permission_setting)
        setCancelable(false)

        var win: Window? = window
        win?.decorView?.setPadding(0, 0, 0, 0)
        var lp = win!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER //设置对话框位置
        win.attributes = lp


        if (content.isEmpty()) {
            throw Exception("permission describe not be null")
        }
        var appName = PackageUtils.getAppName(context)
        describe_tv.text = content+"请在设置->应用->"+appName+"->权限中开启相关权限。"

        cancel_btn.setOnClickListener {
            cancelAction()
        }
        setting_btn.setOnClickListener {
            toSetting()
        }
    }

    open fun cancelAction() {
        dismiss()
    }


    /**
     * 打开系统设置页面
     */
    open fun toSetting() {
        dismiss()
    }

}