# SimplePermission
一行注解完成所有权限操作。

## 一、项目介绍
这是一款基于AOP思想的运行时权限库。帮助开发者免去繁琐的权限请求流程，一行注解就可以完成权限请求，大大降低了权限请求对业务代码对入侵。

## 二、功能与优势
- 支持单权限／多权限请求
- 一行注解申请／处理权限
- 支持私有化的调用方法
- 支持权限拒绝／不再询问的自定义处理
- 支持Activity、Fragment、Dialog、PopupWindow或者其他View中使用
- 支持kotlin、androidx
- 非侵入式

## 三、添加依赖和配置
1.在project的`build.gradle`中添加
```
buildscript {
    
    dependencies {
        ...
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10'
    }
}
```
1.在app/libary的`build.gradle`中添加
```
apply plugin: 'android-aspectjx'

dependencies {
    //implementation 'com.github.Liberuman:PermissionManager:0.31'
}
```

## 四、使用

 **Basic**
```
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
```
这种方式及其简单，只需要申明权限／权限组、requestCode、请求拒绝的描述就可以了。如果用户同意权限，就直接执行方法；
如果用户拒绝，会用框架默认的Dialog去提示权限作用或者引导用户手动授权。


**自定义权限拒绝的逻辑**
```
@PermissionDenied(requestCode = REQUEST_CODE_CAMERA)
    private fun deny() {
        Log.e(TAG, "deny:--- @PermissionDenied ")
        Toast.makeText(context, "开发者自己处理【@PermissionDenied】逻辑", Toast.LENGTH_SHORT).show()
    }

    @PermissionDeniedAndNoLongerAsk(requestCode = REQUEST_CODE_CAMERA)
    private fun noLongerAsk() {
        Log.e(TAG, "noLongerAsk:--- @PermissionDeniedAndNoLongerAsk ")
        Toast.makeText(context, "开发者自己处理【@PermissionDeniedAndNoLongerAsk】逻辑", Toast.LENGTH_SHORT)
            .show()
    }
```
