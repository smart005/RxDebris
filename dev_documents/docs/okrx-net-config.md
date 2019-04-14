[返回目录](/docs/okrx-net-document-dir.md)
# OkRx网络(一)------配置
#### 初始化配置
><font color=gray size=3>全局初始化最好在Application中完成,具体注释已在代码中描述.</font>

```java
//网络框架初始化
OkRx.getInstance().initialize(this)
//true-对于请求失败跟踪日志带有设备相关配置信息;反之则不带;
.setHasFirmwareConfigInformationForTraceLog(false)
//全局参数配置监听
.setOnConfigParamsListener(new OnConfigParamsListener() {
    @Override
    public OkRxConfigParams onConfigParamsCall(OkRxConfigParams configParams) {
        //true-网络请求成功后内部处理相关状态;false-网络请求success后直接返回;
        configParams.setProcessNetResults(false);
        //是否进行网络状态码拦截(默认为false)
        configParams.setNetStatusCodeIntercept(false);
        //链接超时时间
        configParams.setConnectTimeout(10000);
        //读取超时时间
        configParams.setReadTimeOut(10000);
        //写入超时时间
        configParams.setWriteTimeOut(10000);
        //链接失败后重试次数
        configParams.setRetryCount(3);
        //全局请求头信息
        configParams.setHeaders(HashMap<String, String> headers);
        //用户token携带的位置
        //TokenProperties:
        //{
        //    location:cookie或header,
        //    tokenName:token名称
        //}
        configParams.setTokenConfig(TokenProperties tokenConfig);
        return configParams;
    }
})
//接口或缓存数据返回后对象解析监听
.setOnBeanParsingJsonListener(new OnBeanParsingJsonListener() {
    @Override
    public Object onBeanParsingJson(String response, Class dataClass, 
    boolean isCollectionDataType) {
        //如果有父类嵌套子类的情况这里可自行处理
        if (isCollectionDataType) {
            //解析成集合
            return JsonUtils.parseArray(response, dataClass);
        } else {
            //解析成对象
            return JsonUtils.parseT(response, dataClass);
        }
    }
})
//全局请求头参数监听,也可以通过上面的参数配置
.setOnGlobalReuqestHeaderListener(new OnGlobalReuqestHeaderListener() {
    @Override
    public HashMap<String, String> onHeaderParams() {
        return null;
    }
})
.setOnRequestErrorListener(new OnRequestErrorListener() {
    @Override
    public void onFailure(RequestErrorInfo errorInfo) {
        //网络请求失败回调
    }
})
//请求头Cookie参数监听
.setOnHeaderCookiesListener(new OnHeaderCookiesListener() {
    @Override
    public Map<String, String> onCookiesCall() {
        Map<String, String> cookies = new HashMap<String, String>();
        return cookies;
    }
})
.setOnAuthListener(new OnAuthListener() {
    @Override
    public void onLoginCall(String requestMethodName) {
        //当前接口请求需要用户登录时但未登录,会回调到这里统一处理;
    }
})
.build();
```
基础Url配置
--
><font color=gray size=3>新建UrlsProvider并实现OnRequestApiUrl接口,如下示例返回BaseUrl</font>

```java
public class UrlsProvider implements OnRequestApiUrl {
    @Override
    public String onBaseUrl(Integer apiUrlTypeName) {
        //参数apiUrlTypeName用于区分不同的基础地址
        //如在接口注解类中有这样一个注解
        //@BaseUrlTypeName(value = ServiceAPI.Normal),
        //其中的ServiceAPI的值会作为参数apiUrlTypeName传入用于区分
        //www.AA.com、www.BB.com等;
        //最终将拼接好的协议头、域名、端口、[统一路径]一起返回
        return "http://www.AA.com/";
    }
}
```
接口定义与服务类创建
--
```java
@BaseUrlTypeName(value = ServiceAPI.xxx, contentType = RequestContentType.Json)
public interface IGetAPI {
    //在这里定义接口
}
```
```java
@APIUrlInterfaceClass(UrlsProvider.class)
public class GetService extends BaseOkrxService {
    //这里创建接口请求
}
```