package com.zx.permission_core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @descripe
 * @author zhouxu
 * @e-mail 374952705@qq.com
 * @time 2020/4/15
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionDeniedAndNoLongerAsk {
    int requestCode();
}
