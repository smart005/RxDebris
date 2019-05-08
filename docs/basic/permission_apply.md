# 权限申请与判断
><font color=gray size=3>
对于6.0以上部分敏感权限需要动态获取;在当前框架中可通过PermissionManager来申请与判断;
</font>

#### SDK版本兼容
```gradle
//使用权限管理需要将libsdk升级到1.0.8及以上
api 'com.cloud:libsdk:1.0.8'
```
#### 权限申请
><font color=gray size=3>
申请权限第一个参数只支持fragmentActivity和fragment
***下面以fragmentActivity和Manifest.permission.CAMERA为例***
</font>

```java
PermissionManager.getInstance().apply(this, new OnGrantedConsumer() {
    @Override
    public void accept(boolean granted) {
        if (granted) {
            //申请成功
        }else {
            //申请失败
        }
    }
}, Manifest.permission.CAMERA);
```
#### 权限判断
```java
//判断权限是否被注销
boolean revoked = PermissionManager.getInstance().isRevoked(this,
Manifest.permission.CAMERA);
```
```java
//判断权限是否已开启
boolean granted = PermissionManager.getInstance().isGranted(this,
Manifest.permission.CAMERA);
```