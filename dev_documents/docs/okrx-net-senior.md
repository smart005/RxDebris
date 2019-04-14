[返回目录](docs/okrx-net-document-dir.md)
# OkRx网络(三)------高级使用
##### 完整请求地址配置
><font color=gray size=3>如果是完整url必须设置isFullUrl=true，那么在验证请求地址时不拼接baseUrl处理;</font>

```java
@GET(value = "http://www.xxx.com/rest/version", isFullUrl = true)
@DataParam(value = VersionBean.class)
RetrofitParams requestOutsideUrl(
        @Param("versionName") String versionName,
        @Param("deviceNumber") String deviceNumber
);
```
##### 对请求参数类似且数据返回类型相同的接口，可采用地址组合的方式
><font color=gray size=3>只要输入urlKey的值是对应key1 key2 key3中的一个那么请求将以相应的url地址来请求返回数据；</font>

```java
@GET(values = {
        @UrlItem(value = "/rest/order/paid", key = "key1"),
        @UrlItem(value = "/rest/order/canceled", key = "key2"),
        @UrlItem(value = "/rest/order/completed", key = "key3")
})
@DataParam(value = OrderListBean.class)
RetrofitParams requestOrderList(
        @UrlItemKey() String urlKey
);
```
##### 注解属性isRemoveEmptyValueField的作用
><font color=gray size=3>1.若加在GET POST DELETE PATCH PUT上面则对此接口下所有提交的参数都生效；
2.若对参数@Param注解设置isRemoveEmptyValueField=true,则表示该字段值为空时；接口请求提交的参数将不会包含该字段；</font>

```java
@GET(value = "/rest/searchList")
@DataParam(xxxx.class)
RetrofitParams requestxxxx(
        @Param(value = "desc", isRemoveEmptyValueField = true) Boolean desc,
        @Param("pageNum") int pageNum,
        @Param("pageSize") int pageSize
);
```
##### 注解属性isFailureRetry的作用
><font color=gray size=3>对设置isFailureRetry=true后，在接口请求失败时将除了全局设置的尝试请求次数外，将一直尝试请求且每次请求延迟时间在上次请求的基础上增加5秒直到请求成功为止;【最大请求次数为100次】</font>

```java
@GET(value = "/rest/searchList", isFailureRetry = true)
@DataParam(xxxx.class)
RetrofitParams requestxxxx();
```
##### 注解属性isAssociatedAssignment的作用
><font color=gray size=3>对返回数据对象字段属性标有OriginalField，返回时将对应的json数据自动复制到该字段上；</font>

```java
@GET(value = "/rest/searchList", isAssociatedAssignment = true)
@DataParam(xxxx.class)
RetrofitParams requestxxxx();
```