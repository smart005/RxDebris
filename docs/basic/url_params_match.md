# Url参数匹配
><font color=gray size=3>
示例url:
http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=图片&hs=0&pn=1&spn=0&di=176660&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=234634259%2C4236876085&os=54892700%2C159557102&simid=3579428015%2C308375459&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fpic15.nipic.com%2F20110628%2F1369025_192645024000_2.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bgtrtv_z%26e3Bv54AzdH3Ffi5oAzdH3F90088lb_z%26e3Bip4s&gsm=0&islist=&querylist=
</font>

##### 使用场景
```doc
传统url中的参数匹配需要有一堆判断和截取才能匹配到相应的参数，这会使代码变得很冗余;
下面通过UrlParamsEntry来解析和匹配参数;
```
###### 1.初始化
```java
UrlParamsEntry urlParamsEntry = new UrlParamsEntry();
urlParamsEntry.mapper("url 地址");
```
###### 2.方法使用
```java
1.获取原url地址
  String urlParamsEntry.getUrl();
2.匹配参数
  String urlParamsEntry.getParams(String paramName);
3.匹配int参数
  int urlParamsEntry.getIntParams(String paramName);
4.获取参数集合
  HashMap<String, String> urlParamsEntry.getParamsMap();
5.检测是否包含特定的参数
  boolean urlParamsEntry.containsKey(String key);
```