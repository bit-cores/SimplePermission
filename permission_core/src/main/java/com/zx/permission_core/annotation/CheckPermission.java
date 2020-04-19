package com.zx.permission_core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouxu
 * @descripe 检查请求权限
 * @e-mail 374952705@qq.com
 * @time 2020/4/13
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
    /**
     * 权限数组
     *
     * @return
     */
    String[] value();

    /**
     * 请求码
     */
    int requestCode();

    /**
     * 用户拒绝时，向用户解释权限的说明
     * @return
     */
    String describe();
}
