# Picasso，Glide，Fresco对比与分析
### 基本对比
Github地址:
[Picasso](https://github.com/square/picasso)
[Glide](https://github.com/bumptech/glide)
[Fresco](https://github.com/facebook/fresco)

| 对比项 | Picasso | Glide | Fresco |
| --- | --- | --- | --- |
| 发布时间 | 2013-5 | 2014-9 | 2015-5 |
| 是否支持gif | false | true | true |
| 是否支持webP | true | true | true |
| 视频缩略图 | false | true | true |
| 大小 | 100k | 500KB | 2~3M |
| 加载速度 | 一般 | 快 | 快 |
| Disk+Men Cache | true | true | true |
| Easy of use | low | mediun | difficult |
| star | 16k+ | 25k+ | 15k+ |
| 开发者 | Square主导 | Google主导 | Facebook主导 |
### 加载图片耗时及内存对比
><font color=gray size=3>没有动画</font>

| 对比项 | Picasso | Glide | Fresco |
| --- | --- | --- | --- |
| max java heap | 12.6MB | 11.1MB | 13.9MB |
| max native heap | 43.8MB | 43.8MB | 43.8MB |
| avg wait time | 241ms | 34ms | 44ms |
><font color=gray size=3>有动画</font>

| 对比项 | Picasso | Glide | Fresco |
| --- | --- | --- | --- |
| max java heap | 6.8 | 74.8MB | 36.1MB |
| max native heap | 18.2MB | 66.8MB | 545.3MB |
| avg wait time | 1707ms | 33910ms | 15142ms |
1. 从上面的加载静态图片可以看出三大主流框架性能都不错，不过用数据说话整体而言Glide更胜一筹;
2. 基本上可以忽略Picasso,因为它根本不支持gif，那么Glide和Fresco可以看出Fresco的java heap基本保持较低平稳状态，而Glide的java heap基本为Fresco的一倍，所以OOM的风险也比fresco大一倍；
3. 从时间上glide是有一定差距，不过fresco有两张图片没加载完成，所以时间不是完全可靠的数据，从native heap可以看出Fresco最高545MB，这个有点恐怖，下面我们看个知识点
    1. Java Heap是对于Java 虚拟机而说的，一般的大小上限是 16M 24M 48M 76M 具体视手机而定;
    2. Native Heap是对于C/C++直接操纵的系统堆内存，所以它的上限一般是具体RAM的2/3左右;
    3. 所以对于2G的手机而言，Java Heap 大概76M，而Native Heap是760M左右，相差10倍。

# Picasso从各方面都比这两个弱,接下来只详细对比Fresco和Glide;

| 对比项 | Glide | Fresco |
| --- | --- | --- |
| 初始化 | 直接使用 | Fresco.initialize(this) |
| layout | 普通ImageView | 独有的SimpleDraweeView |
| 圆角、圆形 | 需要自己实现圆角，继承自BitmapTransformation操作bitmap对象实现 | 通过RoundingParams设置参数 |
| 缓存 | Glide内存和磁盘缓存 | 三级缓存、分别是Bitmap缓存，未解码图片缓存，文件缓存 |
| 缓存图像大小 | Glide则根据ImageView控件尺寸获得对应大小的bitmap来展示，从而缓存也可以针对不同的对象：原始图像(source)，结果图像(result) | 缓存原始图像 |
| 加载策略 | Glide只有点位图 | 先加载小尺寸图片，再加载大尺寸图片 |
| 加载进度 | false | true |
# 结论
Fresco虽然很强大，但是包很大，依赖很多，使用复杂，而且还要在布局使用SimpleDraweeView控件加载图片。相对而言Glide会轻好多，上手快，使用简单，配置方便，而且从加载速度和性能方面不相上下。对于一般的APP来说Glide是一个不错的选择，如果是专业的图片APP那么Fresco还是必要的
