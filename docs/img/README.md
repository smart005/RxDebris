# 图片渲染
* 项目开发中图片为资源加载比较重要部分.一般Android应用程序在不同的设备上内存分配的大小是固定的；加载文件过大或尺寸大小偏大都会导致资源的浪费;
* 在Android客户端目前常用的图片工具有Glide、Picasso、Volley、Luban等,而Volley实际上是一个网络通信框架;
* 本框架中以将以Glide为例,选择此第三方的原因：
    1. Glide是Google强力推荐，且在加载过程带有相关的生命周期管理;
    2. [Picasso，Glide，Fresco对比与分析](pgf-analysis.md);
    3. 多种图片格式的缓存，适用于更多的内容表现形式（如Gif、WebP、缩略图、Video）;
    4. 生命周期集成（根据Activity或者Fragment的生命周期管理图片加载请求）;
    5. 高效处理Bitmap（bitmap的复用和主动回收，减少系统回收压力）;
    6. 高效的缓存策略，灵活（Picasso只会缓存原始尺寸的图片，Glide缓存的是多种规格），加载速度快且内存开销小（默认Bitmap格式的不同，使得内存开销是Picasso的一半）;

*[下面我们介绍一下本框架中的图片使用]*
# GlideOptimize使用
><font color=gray size=3>
1. 常规的视图图片、gif、本地图片、资源图片加载;
2. 与第三方规则配合使用;
3. 代码中避免直接使用三方库减少侵入性与耦合度;

**示例中有详细注释这里不一一列出了**
</font>

```java
GlideOptimize.with(this)
        .load(url)
        //可自定义宽度和高度，不设置默认取控件的宽高;
        //.setWidth(100)
        //.setHeight(200)
        //glide图片加载完成之前的占位图片
        //未设置时取RxImage.setDefImage()设置的默认图片
        .setPlaceholder(R.drawable.def_image)
        //图片加载过程中优化级(一般用于比较重要位置或优化要显示的图片)
        .setPriority(Priority.HIGH)
        //true-将图片转为正圆再渲染;false-不作转换处理;
        //对于网络图片若尺寸或文件大小太大会导致不能转成圆形
        .setRound(false)
        //先加载相对于原图的缩放比例(按宽高比)的缩略图
        .setThumbnailScale(0.5f)
        //具体说明看{@link ScaleType}
        .setScaleType(ScaleType.centerCrop)
        //图片圆角弧度(由第三方规则处理)
        .setRoundCorners(20)
        //图片规则(根据第三方文档设定,如阿里、七牛)
        .setImageRule(ImgRuleType.GeometricForWidth.getRule())
        //缓存模式
        .setCacheMode(CacheMode.onlyMemory)
        //散列key,适用于请求url不变但图片已更新情况
        //.setHashKey(GlobalUtils.getNewGuid())
        //gif图片需要设置此属性
        //.asGif()
        .into(binding.testIv0);
```
><font color=gray size=3>
加载带有bitmap回调的方式
</font>

```java
GlideOptimize.with(this)
    .load(url2)
    .into(new GBitmapCallback() {
        @Override
        public void call(Bitmap bitmap) {
            binding.testIv1.setImageBitmap(bitmap);
        }
    });
```
><font color=gray size=3>
加载带有File回调的方式
</font>

```java
GlideOptimize.with(this)
    .load(url)
    //将文件移动至此目录下,如果不设置则为glide缓存默认路径
    .toMove(DirectoryNames.forum.name())
    .into(new GFileCallback<File>() {
        @Override
        public void call(File file) {
            //这里做移动文件、加载等操作
        }
    });
```

下一将介绍[本地图片、相册等相关操作](operation.md)