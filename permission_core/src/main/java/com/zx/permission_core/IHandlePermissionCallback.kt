package com.zx.permission_core

/**
 * @descripe 处理拒绝权限的回调
 *
 * @author zhouxu
 * @e-mail 374952705@qq.com
 * @time   2020/4/15
 */


interface IHandlePermissionCallback {
    fun handlePermissionsDenied()

    fun handlePermissionsDeniedAndNoLongerPrompt()
}