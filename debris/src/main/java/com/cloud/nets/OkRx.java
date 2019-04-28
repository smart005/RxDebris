package com.cloud.nets;

import android.content.Context;

import com.cloud.cache.MemoryCache;
import com.cloud.cache.RxCache;
import com.cloud.nets.cookie.CookieJarImpl;
import com.cloud.nets.cookie.store.CookieStore;
import com.cloud.nets.cookie.store.SPCookieStore;
import com.cloud.nets.events.OnAuthListener;
import com.cloud.nets.events.OnBeanParsingJsonListener;
import com.cloud.nets.events.OnConfigParamsListener;
import com.cloud.nets.events.OnGlobalReuqestHeaderListener;
import com.cloud.nets.events.OnHeaderCookiesListener;
import com.cloud.nets.events.OnRequestErrorListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.utils.JsonUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Author Gs
 * Email:gs_12@foxmail.com
 * CreateTime:2017/6/1
 * Description: OkGo基础
 * Modifier:
 * ModifyContent:
 */
public class OkRx {

    private static OkRx okRx = null;
    //是否要重新获取配置参数
    private boolean isUpdateConfig = false;
    private OkRxConfigParams okRxConfigParams = null;
    private OnConfigParamsListener onConfigParamsListener = null;
    //application context 在cookies持久化时使用(为空时cookies使用内存持久化)
    private Context applicationContext = null;
    //接口请求结果json解析处理监听
    private OnBeanParsingJsonListener parsingJsonListener = null;
    //请求头监听
    private OnGlobalReuqestHeaderListener globalReuqestHeaderListener = null;
    //监听请求头参数
    private HashMap headers = null;
    //网络请求失败监听
    private OnRequestErrorListener onRequestErrorListener = null;
    //网络请求时用户授权相关回调监听
    private OnAuthListener onAuthListener = null;
    //用于http socket connect fail处理
    private Set<String> failDomainList = new HashSet<String>();
    //头部cookies监听
    private OnHeaderCookiesListener onHeaderCookiesListener = null;
    //跟踪日志是否带固件配置信息(默认false)
    private boolean isHasFirmwareConfigInformationForTraceLog = false;
    //是否输出网络日志(只有在debug模式下才生效,默认false)
    private boolean isPrintDebugNetLog = false;

    public static OkRx getInstance() {
        if (okRx == null) {
            okRx = new OkRx();
        }
        return okRx;
    }

    //在连接失败判断用,外面无须调用;
    public Set<String> getFailDomainList() {
        return failDomainList;
    }

    /**
     * 设置全局配置参数监听
     *
     * @param listener 全局配置参数监听
     */
    public OkRx setOnConfigParamsListener(OnConfigParamsListener listener) {
        this.onConfigParamsListener = listener;
        return this;
    }

    /**
     * 获取请求头回调监听
     *
     * @return OnGlobalReuqestHeaderListener
     */
    public OnGlobalReuqestHeaderListener getOnGlobalReuqestHeaderListener() {
        if (globalReuqestHeaderListener == null) {
            Object listener = MemoryCache.getInstance().getSoftCache("GlobalReuqestHeaderListener");
            if (listener instanceof OnGlobalReuqestHeaderListener) {
                globalReuqestHeaderListener = (OnGlobalReuqestHeaderListener) listener;
            }
        }
        return globalReuqestHeaderListener;
    }

    /**
     * 设置全局请求头回调监听
     *
     * @param listener OnGlobalReuqestHeaderListener
     * @return
     */
    public OkRx setOnGlobalReuqestHeaderListener(OnGlobalReuqestHeaderListener listener) {
        this.globalReuqestHeaderListener = listener;
        MemoryCache.getInstance().setSoftCache("GlobalReuqestHeaderListener", listener);
        return this;
    }

    /**
     * okrx初始化
     * (一般在Application初始化时调用)
     *
     * @param context 上下文
     */
    public OkRx initialize(Context context) {
        this.applicationContext = context;
        this.isUpdateConfig = true;
        if (onConfigParamsListener != null) {
            okRxConfigParams = onConfigParamsListener.onConfigParamsCall(getDefaultConfigParams());
        }
        if (okRxConfigParams == null) {
            okRxConfigParams = new OkRxConfigParams();
        }
        return this;
    }

    //构建相关配置
    public void build() {
        //缓存okRxConfigParams参数
        OkHttpClient client = newHttpClient(okRxConfigParams);
        MemoryCache.getInstance().setSoftCache(OkRxKeys.okhttpClientKey, client);
    }

    /**
     * 重新构建http client
     *
     * @param okRxConfigParams 全局配置参数
     * @return OkHttpClient
     */
    public OkHttpClient newHttpClient(OkRxConfigParams okRxConfigParams) {
        //创建http client对象
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //连接超时
        builder.connectTimeout(okRxConfigParams.getConnectTimeout(), TimeUnit.MILLISECONDS);
        //读取超时
        builder.readTimeout(okRxConfigParams.getReadTimeOut(), TimeUnit.MILLISECONDS);
        //写入超时
        builder.writeTimeout(okRxConfigParams.getWriteTimeOut(), TimeUnit.MILLISECONDS);
        //设置失败时重连次数,请求头信息
        builder.addInterceptor(new RequestRetryIntercepter(okRxConfigParams.getRetryCount(), okRxConfigParams.getHeaders()));
        //cookies持久化
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(applicationContext)));
        //添加证书信任
        SslSocketManager.SSLParams sslParams1 = SslSocketManager.getSslSocketFactory();
        if (sslParams1 != null) {
            builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        }
        //全局请求参数设置
        return builder.build();
    }

    /**
     * 获取http client对象
     *
     * @param isNewConnect 是否重新创建连接
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpClient(boolean isNewConnect) {
        if (!isNewConnect) {
            Object objectValue = MemoryCache.getInstance().getSoftCache(OkRxKeys.okhttpClientKey);
            if ((objectValue instanceof OkHttpClient)) {
                OkHttpClient httpClient = (OkHttpClient) objectValue;
                return httpClient;
            }
        }
        OkRxConfigParams configParams = getOkRxConfigParams();
        OkHttpClient client = newHttpClient(configParams);
        MemoryCache.getInstance().setSoftCache(OkRxKeys.okhttpClientKey, client);
        return client;
    }

    /**
     * 获取http client对象
     *
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        return getOkHttpClient(false);
    }

    /**
     * 获取okrx全局配置参数
     * (不要在application初始化时调用)
     *
     * @return
     */
    public OkRxConfigParams getOkRxConfigParams() {
        if (okRxConfigParams == null || isUpdateConfig) {
            if (onConfigParamsListener != null) {
                okRxConfigParams = onConfigParamsListener.onConfigParamsCall(getDefaultConfigParams());
            }
            isUpdateConfig = false;
        }
        //再次判断若全局参数为空则重新创建参数
        if (okRxConfigParams == null) {
            okRxConfigParams = new OkRxConfigParams();
        }
        return okRxConfigParams;
    }

    /**
     * 获取默认参数配置
     *
     * @return OkRxConfigParams
     */
    public OkRxConfigParams getDefaultConfigParams() {
        if (okRxConfigParams == null) {
            okRxConfigParams = new OkRxConfigParams();
        }
        return okRxConfigParams;
    }

    /**
     * 获取json解析监听对象
     *
     * @return json解析监听对象
     */
    public OnBeanParsingJsonListener getOnBeanParsingJsonListener() {
        if (parsingJsonListener == null) {
            Object object = MemoryCache.getInstance().getSoftCache("BeanParsingJsonListener");
            if (object instanceof OnBeanParsingJsonListener) {
                parsingJsonListener = (OnBeanParsingJsonListener) object;
            }
        }
        return parsingJsonListener;
    }

    /**
     * 接口返回结果json解析需要自行处理的须实现此监听
     *
     * @param parsingJsonListener json解析监听对象
     * @return OkRx
     */
    public OkRx setOnBeanParsingJsonListener(OnBeanParsingJsonListener parsingJsonListener) {
        this.parsingJsonListener = parsingJsonListener;
        MemoryCache.getInstance().setSoftCache("BeanParsingJsonListener", parsingJsonListener);
        return this;
    }

    /**
     * 设置http请求时header参数(全局)
     * (持久存储)
     * 根据业务场景也可以在setOnGlobalReuqestHeaderListener()设置的监听中回调
     *
     * @param headers header params
     * @return OkRx
     */
    public OkRx setHeaderParams(HashMap<String, String> headers) {
        this.headers = headers;
        String json = ObjectJudge.isNullOrEmpty(headers) ? "" : JsonUtils.toJson(headers);
        RxCache.setCacheData("NetRequestHttpHeaderParams", json);
        return this;
    }

    /**
     * 获取请求头参数
     *
     * @return key-value header params
     */
    public HashMap<String, String> getHeaderParams() {
        if (ObjectJudge.isNullOrEmpty(headers)) {
            String params = RxCache.getCacheData("NetRequestHttpHeaderParams");
            headers = JsonUtils.parseT(params, HashMap.class);
        }
        return headers;
    }

    /**
     * 获取http请求失败回调监听
     *
     * @return OnRequestErrorListener
     */
    public OnRequestErrorListener getOnRequestErrorListener() {
        if (onRequestErrorListener == null) {
            Object errorListener = MemoryCache.getInstance().getSoftCache("NetRequestErrorListener");
            if (errorListener instanceof OnRequestErrorListener) {
                onRequestErrorListener = (OnRequestErrorListener) errorListener;
            }
        }
        return onRequestErrorListener;
    }

    /**
     * 设置http请求失败回调监听
     *
     * @param listener http失败回调监听
     */
    public OkRx setOnRequestErrorListener(OnRequestErrorListener listener) {
        this.onRequestErrorListener = listener;
        MemoryCache.getInstance().setSoftCache("NetRequestErrorListener", listener);
        return this;
    }

    /**
     * 获取授权相关监听
     *
     * @return OnAuthListener
     */
    public OnAuthListener getOnAuthListener() {
        if (onAuthListener == null) {
            Object authListener = MemoryCache.getInstance().getSoftCache("$_NetAuthListener");
            if (authListener instanceof OnAuthListener) {
                onAuthListener = (OnAuthListener) authListener;
            }
        }
        return onAuthListener;
    }

    /**
     * 设置授权相关监听
     *
     * @param listener 授权相关监听
     */
    public OkRx setOnAuthListener(OnAuthListener listener) {
        this.onAuthListener = listener;
        MemoryCache.getInstance().setSoftCache("$_NetAuthListener", listener);
        return this;
    }

    /**
     * 获取http cookies追加监听
     *
     * @return OnHeaderCookiesListener
     */
    public OnHeaderCookiesListener getOnHeaderCookiesListener() {
        if (onHeaderCookiesListener == null) {
            Object cookiesListener = MemoryCache.getInstance().getSoftCache("$_OnHeaderCookiesListener");
            if (cookiesListener instanceof OnHeaderCookiesListener) {
                onHeaderCookiesListener = (OnHeaderCookiesListener) cookiesListener;
            }
        }
        return onHeaderCookiesListener;
    }

    /**
     * 设置http cookies追加监听
     *
     * @param listener OnHeaderCookiesListener
     * @return OkRx
     */
    public OkRx setOnHeaderCookiesListener(OnHeaderCookiesListener listener) {
        this.onHeaderCookiesListener = listener;
        MemoryCache.getInstance().setSoftCache("$_OnHeaderCookiesListener", listener);
        return this;
    }

    /**
     * 清除token信息
     * 在用户退出登录时调用
     */
    public void clearToken() {
        OkHttpClient client = getOkHttpClient();
        if (client == null) {
            return;
        }
        CookieJar cookieJar = client.cookieJar();
        if (!(cookieJar instanceof CookieJarImpl)) {
            return;
        }
        CookieJarImpl cookieImpl = (CookieJarImpl) cookieJar;
        CookieStore cookieStore = cookieImpl.getCookieStore();
        if (cookieStore == null) {
            return;
        }
        cookieStore.removeAllCookie();
    }

    /**
     * 清除接口请求的网络缓存
     *
     * @param cacheKey 请求接口时设置的缓存key
     */
    public void clearCache(String cacheKey) {
        RxCache.clearContainerKey(cacheKey);
    }

    /**
     * 设置跟踪日志是否带固件配置信息(默认false)
     *
     * @param isHasFirmwareConfigInformationForTraceLog true-对于请求失败跟踪日志带有设备相关配置信息;反之则不带;
     * @return OkRx
     */
    public OkRx setHasFirmwareConfigInformationForTraceLog(boolean isHasFirmwareConfigInformationForTraceLog) {
        this.isHasFirmwareConfigInformationForTraceLog = isHasFirmwareConfigInformationForTraceLog;
        MemoryCache.getInstance().setSoftCache("$_HasFirmwareConfigInformationForTraceLog", isHasFirmwareConfigInformationForTraceLog);
        return this;
    }

    /**
     * 获取跟踪日志是否带固件配置信息(默认false)
     *
     * @return true-对于请求失败跟踪日志带有设备相关配置信息;反之则不带;
     */
    public boolean isHasFirmwareConfigInformationForTraceLog() {
        if (!this.isHasFirmwareConfigInformationForTraceLog) {
            Object o = MemoryCache.getInstance().getSoftCache("$_HasFirmwareConfigInformationForTraceLog");
            this.isHasFirmwareConfigInformationForTraceLog = ObjectJudge.isTrue(o);
        }
        return this.isHasFirmwareConfigInformationForTraceLog;
    }

    /**
     * 是否输出网络日志(只有在debug模式下才生效,默认false)
     *
     * @return true-输出debug模式下网络请求日志,false-不输出;
     */
    public boolean isPrintDebugNetLog() {
        if (!isPrintDebugNetLog) {
            Object printDebugNetLog = MemoryCache.getInstance().getSoftCache("$_PrintDebugNetLog");
            isPrintDebugNetLog = ObjectJudge.isTrue(printDebugNetLog);
        }
        return isPrintDebugNetLog;
    }

    /**
     * 设置debug模式网络日志是否输出
     *
     * @param printDebugNetLog true-输出debug模式下网络请求日志,false-不输出;
     * @return OkRx
     */
    public OkRx setPrintDebugNetLog(boolean printDebugNetLog) {
        isPrintDebugNetLog = printDebugNetLog;
        MemoryCache.getInstance().setSoftCache("$_PrintDebugNetLog", printDebugNetLog);
        return this;
    }
}