package com.zx.permission_core.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.zx.permission_core.R
import kotlinx.android.synthetic.main.dialog_explain_permission.*
import java.lang.Exception

/**
 * @descripe
 *
 * @author zhouxu
 * @e-mail 374952705@qq.com
 * @time   2020/4/14
 */

open class ExplainPermissionDialog(context: Context, themeResId: Int = R.style.custom_dialog, content: String) :
    Dialog(context, themeResId) {


    init {
        //指定布局
        setContentView(R.layout.dialog_explain_permission)
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
        describe_tv.text = content

        cancel_btn.setOnClickListener {
            cancelAction()
        }
        ok_btn.setOnClickListener {
            authorize()
        }
    }

    open fun cancelAction() {
        dismiss()
    }

    /**
     * 去授权
     */
    open fun authorize() {
        dismiss()
    }

}