[返回目录](docs/okrx-net-document-dir.md)
# OkRx网络(二)------restFul Api接口定义
##### 普通请求【GET POST PUT DELETE HEAD PATCH】
```java
//请求方式和接口相对路径
@GET(value = "/rest/version")
//[可选]
//当定义的接口与当前接口类请求的base url、tokenName或contentType不一致时需要添加注解
@BaseUrlTypeName(value = "base url",contentType = RequestContentType.Form)
//数据返回类型
@DataParam(value = VersionBean.class)
//定义请求方法和参数
RetrofitParams requestOutsideUrl(
        @Param("versionName") String versionName,
        @Param("deviceNumber") String deviceNumber
);
```
##### DELETE请求
><font color=gray size=3>DELETE请求时参数拼接与GET一致,传入注解使用@DelQuery;</font>

```java
@DELETE(value = "/rest/order/{id}?status={status}")
@DataParam(value = xxxxx.class)
RetrofitParams requestOrderDetail(
        @Path("id") String id,
        @DelQuery("status") String status
);
```
##### Url地址动态替换【@Path注解使用】
><font color=gray size=3>部分url替换</font>

```java
@GET(value = "/rest/order/{id}")
@DataParam(value = xxxxx.class)
RetrofitParams requestOrderDetail(
        @Path("id") String id
);
```
><font color=gray size=3>全部url替换,此时也可配合isFullUrl=true来使用,以此判断传入的url相对路径还是绝对路径；以便在请求时是否要拼接base url处理;</font>

```java
@GET(value = "{url}")
@DataParam(value = xxxxx.class)
RetrofitParams requestOrderDetail(
        @Path("url") String url
);
```
##### 请求头Header、Headers【@HeaderPart使用】
```java
@POST(value = "相对url")
@Header(name = "token", value = "xxxxx")
@Headers({"key1:value1", "key2:{value2}"})
@DataParam(value = BaseBean.class)
RetrofitParams payCode(
    @HeaderPart("value2") String value2
);
```
##### 请求返回字节、流、文件类型
><font color=gray size=3>1.返回类型File或byte[],则responseDataType = ResponseDataType.byteData;
2.返回类型InputStream,则responseDataType = ResponseDataType.stream;
3.对于目标文件参数@Param需要设置属性isTargetFile = true;</font>

```java
@GET(value = "url地址")
@DataParam(value = File.class, responseDataType = ResponseDataType.byteData)
RetrofitParams requestXXXX(
        @Param("username") String username,
        @Param(isTargetFile = true) File file
);
```
><font color=gray size=3>其中目标文件可通过以下方式传入</font>

```java
@ApiCheckAnnotation
public void requestXXXX(OnSuccessfulListener<File> successfulListener) {
    BaseSubscriber baseSubscriber = new BaseSubscriber<File, GetService>(this);
    baseSubscriber.setOnSuccessfulListener(successfulListener);
    requestObject(IGetAPI.class, this, baseSubscriber,
    new Func2<RetrofitParams, IGetAPI, HashMap<String, Object>>() {
        @Override
        public RetrofitParams call(IGetAPI getAPI,
        HashMap<String, Object> params) {
            //【保存的目标文件】
            File file = StorageManager.createImageFile("test_image.jpg");
            return getAPI.requestXXXX(file);
        }
    });
}
```
##### 接口请求定义
```java
//若不需要token验证则加注解即可
//@ApiCheckAnnotation
-------
//网络请求-不做缓存
//@ApiCheckAnnotation(isTokenValid = true)
-------
//网络缓存会根据设置的cacheKey+请求条件作缓存处理;
//(如[recommand_info_siteId_42_pageSize_1_pageNumber_20])
//网络请求-在缓存未失效时网络数据与缓存只会返回其中一个,
//缓存失效后先请求网络->再缓存->最后返回;
//@ApiCheckAnnotation(callStatus = CallStatus.OnlyCache, 
//cacheTime = 1, cacheTimeUnit = TimeUnit.MINUTES,
//cacheKey = "recommand_info")
-------
//网络请求-在缓存未失败时获取到网络数据和缓存数据均会回调,
//缓存失效后先请求网络->再缓存->最后返回(即此时只作网络数据的回调);
//@ApiCheckAnnotation(callStatus = CallStatus.PersistentIntervalCache,
//cacheIntervalTime = 10000, cacheTime = 1, cacheTimeUnit = TimeUnit.MINUTES,
//cacheKey = "recommand_info")
-------
//请求时间限制;可在请求方法或接口定义上添加;接口定义上设置的值优先于请求方法设置的值;
@ApiCheckAnnotation(callStatus = CallStatus.PersistentIntervalCache)
@RequestTimeLimit(totalTime = "2", unit = TimeUnit.SECONDS)
public void requestXXX(final int siteId,
OnSuccessfulListener<ReturnBean> successfulListener) {
    //请求订阅对象
    BaseSubscriber baseSubscriber = 
    new BaseSubscriber<ReturnBean, GetService>(this);
    //设置成功监听器
baseSubscriber.setOnSuccessfulListener(successfulListener);
    //设置扩展数据(将在回调中返回)
    //baseSubscriber.setExtra("");
    //请求api
    requestObject(IGetAPI.class, this, baseSubscriber,
    new Func2<RetrofitParams, IGetAPI, HashMap<String, Object>>() {
        @Override
        public RetrofitParams call(IGetAPI getAPI,
        HashMap<String, Object> params) {
            HashMap<String, String> map = new HashMap<>();
            map.put("parame1", "123");
            return getAPI.requestXXX(siteId, map, 3, 
            CallStatus.PersistentIntervalCache);
        }
    });
}
```
><font color=gray size=3>其中OnSuccessfulListener<ReturnBean> successfulListener监听如下示例:</font>

```java
private OnSuccessfulListener<RecommandInfo> recommandListener = 
new OnSuccessfulListener<RecommandInfo>() {
    @Override
    public void onSuccessful(RecommandInfo recommandInfo, DataType dataType,
    Object... extras) {
        //如果dataType==DataType.EmptyForOnlyCache则recommandInfo==null
        //具体接口请求成功回调;
        //如果有缓存且存在缓存和网络均会回调时则isLastCall==true表示最后一次回调
    }

    @Override
    public void onError(RecommandInfo recommandInfo, ErrorType errorType,
    Object... extras) {
        //【可选】具体接口请求失败回调
    }

    @Override
    public void onCompleted(Object... extras) {
        //【可选】具体接口请求完成回调
    }
};
```

