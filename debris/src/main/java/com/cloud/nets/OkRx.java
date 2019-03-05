package com.cloud.nets;

import android.content.Context;

import com.cloud.cache.MemoryCache;
import com.cloud.cache.RxCache;
import com.cloud.nets.cookie.CookieJarImpl;
import com.cloud.nets.cookie.store.SPCookieStore;
import com.cloud.nets.events.OnBeanParsingJsonListener;
import com.cloud.nets.events.OnConfigParamsListener;
import com.cloud.nets.events.OnGlobalReuqestHeaderListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.utils.JsonUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    public static OkRx getInstance() {
        if (okRx == null) {
            okRx = new OkRx();
        }
        return okRx;
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
        if (onConfigParamsListener != null) {
            okRxConfigParams = onConfigParamsListener.onConfigParamsCall(this);
        }
        if (okRxConfigParams == null) {
            okRxConfigParams = new OkRxConfigParams();
        }
        //缓存okRxConfigParams参数
        OkHttpClient client = newHttpClient(okRxConfigParams);
        MemoryCache.getInstance().setSoftCache(OkRxKeys.okhttpClientKey, client);
        return this;
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
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        Object objectValue = MemoryCache.getInstance().getSoftCache(OkRxKeys.okhttpClientKey);
        if ((objectValue instanceof OkHttpClient)) {
            OkHttpClient httpClient = (OkHttpClient) objectValue;
            return httpClient;
        }
        OkRxConfigParams configParams = getOkRxConfigParams();
        OkHttpClient client = newHttpClient(configParams);
        MemoryCache.getInstance().setSoftCache(OkRxKeys.okhttpClientKey, client);
        return client;
    }

    /**
     * 获取okrx全局配置参数
     *
     * @return
     */
    public OkRxConfigParams getOkRxConfigParams() {
        if (okRxConfigParams == null) {
            if (onConfigParamsListener != null) {
                okRxConfigParams = onConfigParamsListener.onConfigParamsCall(this);
            }
        }
        //再次判断若全局参数为空则重新创建参数
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
    public <T> OnBeanParsingJsonListener<T> getOnBeanParsingJsonListener() {
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
        String json = ObjectJudge.isNullOrEmpty(headers) ? "" : JsonUtils.toStr(headers);
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
}