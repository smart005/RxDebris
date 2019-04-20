# 图片操作
### 基本属性
```java
//选择后图片最大压缩大小
imageSelectDialog.setMaxFileSize(1024);
//最多选择图片数量
imageSelectDialog.setMaxSelectNumber(1);
//是否显示拍照选项
imageSelectDialog.setShowTakingPictures(true);
//选择后压缩图片最大宽度
imageSelectDialog.setMaxImageWidth(720);
//选择后压缩图片最大高度
imageSelectDialog.setMaxImageHeight(1920);
//选择图片后是否进行裁剪处理
imageSelectDialog.setTailoring(true);
//设置裁剪最大宽高
int screenWidth = ObjectManager.getScreenWidth(this) * 2;
int height = screenWidth * 83 / 345;
imageSelectDialog.withMaxSize(screenWidth, height);
//设置裁剪宽高比
imageSelectDialog.withAspect(345, 83);
//扩展数据，选择时传入可以回调中取出;
imageSelectDialog.setExtra(null);
```
### 其它属性和方法
```java
//删除已选择的图片
imageSelectDialog.deleteSelecteImages();
//获取扩展参数
imageSelectDialog.getExtra();
```
### 调用方法
><font color=gray size=3>相册[拍照]选择</font>

```java
imageSelectDialog.show(this);
```
><font color=gray size=3>直接调用拍照</font>

```java
imageSelectDialog.showTaking(this);
```
### 回调
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    imageSelectDialog.onActivityResult(this, requestCode, resultCode, data);
}
```
```java
private ImageSelectDialog imageSelectDialog = new ImageSelectDialog() {
    @Override
    protected void onSelectCompleted(List<SelectImageProperties> 
    selectImageProperties, Object extra) {

    }
};
```