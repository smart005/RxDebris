package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.cache.CacheDataItem;
import com.cloud.cache.RxCache;
import com.cloud.nets.OkRx;
import com.cloud.nets.OkRxKeys;
import com.cloud.nets.beans.ResponseData;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.enums.ResponseDataType;
import com.cloud.nets.events.OnHeaderCookiesListener;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RequestContentType;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action4;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.StringUtils;
import com.cloud.objects.utils.ValidUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseRequest {

    private RequestType requestType = null;
    private RequestContentType requestContentType = null;
    private RetrofitParams retrofitParams;
    //是否取消间隔缓存回调
    private boolean isCancelIntervalCacheCall = false;
    //内部处理后的参数
    private HashMap<String, Object> internalProcessParams = new HashMap<String, Object>();

    public boolean isCancelIntervalCacheCall() {
        return isCancelIntervalCacheCall;
    }

    public void setCancelIntervalCacheCall(boolean cancelIntervalCacheCall) {
        isCancelIntervalCacheCall = cancelIntervalCacheCall;
    }

    public RetrofitParams getRetrofitParams() {
        if (retrofitParams == null) {
            retrofitParams = new RetrofitParams();
        }
        return retrofitParams;
    }

    public void setRetrofitParams(RetrofitParams retrofitParams) {
        this.retrofitParams = retrofitParams;
    }

    protected void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void call(String url,
                     HashMap<String, String> headers,
                     Action4<ResponseData, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                     Action2<RequestState, ErrorType> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique) {
        //子类重载方法
    }

    public void setRequestContentType(RequestContentType requestContentType) {
        this.requestContentType = requestContentType;
    }

    protected Request.Builder getBuilder(String url,
                                         HashMap<String, String> headers,
                                         TreeMap<String, Object> params,
                                         HashMap<String, String> suffixParams) {
        Request.Builder builder = new Request.Builder();
        if (requestType == RequestType.GET) {
            url = addGetRequestParams(url, params);
        }
        builder.url(url);
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.removeHeader(entry.getKey());
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        if (requestType != RequestType.GET) {
            if (requestType == RequestType.HEAD) {
                builder.head();
            } else {
                addRequestParams(builder, params, suffixParams);
            }
        }

        HashMap<String, String> map = new HashMap<>();
        //本次请求初始方法名
        map.put("requestMethodName", retrofitParams.getInvokeMethodName());
        //请求类型
        map.put("requestType", requestType.name());
        //请求url
        map.put("requestUrl", url);
        //请求头信息
        map.put("requestHeaders", JsonUtils.toJson(headers));
        //请求参数
        map.put("requestParams", JsonUtils.toJson(internalProcessParams));
        builder.tag(HashMap.class, map);

        return builder;
    }

    private void submitMultipartParams(Request.Builder builder, MultipartBody requestBody) {
        if (requestType == RequestType.POST) {
            builder.post(requestBody);
        } else if (requestType == RequestType.PUT) {
            builder.put(requestBody);
        } else if (requestType == RequestType.DELETE) {
            builder.delete(requestBody);
        } else if (requestType == RequestType.PATCH) {
            builder.patch(requestBody);
        } else if (requestType == RequestType.OPTIONS) {
            builder.method("OPTIONS", requestBody);
        } else if (requestType == RequestType.TRACE) {
            builder.method("TRACE", requestBody);
        }
    }

    private void submitRequestParams(Request.Builder builder, RequestBody requestBody) {
        if (requestType == RequestType.POST) {
            builder.post(requestBody);
        } else if (requestType == RequestType.PUT) {
            builder.put(requestBody);
        } else if (requestType == RequestType.DELETE) {
            builder.delete(requestBody);
        } else if (requestType == RequestType.PATCH) {
            builder.patch(requestBody);
        } else if (requestType == RequestType.OPTIONS) {
            builder.method("OPTIONS", requestBody);
        } else if (requestType == RequestType.TRACE) {
            builder.method("TRACE", requestBody);
        }
    }

    //如果参数集合包含file或byte[]则无论requestContentType是否为json均以Form方式提交
    private void addRequestParams(Request.Builder builder, TreeMap<String, Object> params, HashMap<String, String> suffixParams) {
        ValidResult validResult = validParams(params);
        if (!ObjectJudge.isNullOrEmpty(validResult.streamParamKeys) ||
                !ObjectJudge.isNullOrEmpty(validResult.fileParamKeys)) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            //文件后缀(若File类型默认取原后缀,若Byte默认以rxtiny为后缀)
            String suffix = "rxtiny";
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (validResult.streamParamKeys.contains(entry.getKey())) {
                    //以字节流的形式上传文件
                    MediaType mediaType = MediaType.parse("application/octet-stream");
                    RequestBody body = RequestBody.create(mediaType, (byte[]) entry.getValue());
                    if (suffixParams != null && suffixParams.containsKey(entry.getKey())) {
                        String mfx = suffixParams.get(entry.getKey());
                        if (!TextUtils.isEmpty(mfx)) {
                            suffix = mfx;
                        }
                    }
                    String filename = String.format("%s.%s", GlobalUtils.getGuidNoConnect(), suffix);
                    //添加参数
                    requestBody.addFormDataPart(entry.getKey(), filename, body);
                    //内部使用
                    internalProcessParams.put(entry.getKey(), "stream");
                } else if (validResult.fileParamKeys.contains(entry.getKey())) {
                    //以文件的形式上传文件
                    MediaType mediaType = MediaType.parse("multipart/form-data");
                    File file = (File) entry.getValue();
                    RequestBody body = RequestBody.create(mediaType, file);
                    //后缀若参数中有指定则优先取参数后缀，反之默认从文件中取
                    if (suffixParams != null && suffixParams.containsKey(entry.getKey())) {
                        String mfx = suffixParams.get(entry.getKey());
                        if (!TextUtils.isEmpty(mfx)) {
                            suffix = mfx;
                        } else {
                            suffix = GlobalUtils.getSuffixName(file.getName());
                        }
                    } else {
                        suffix = GlobalUtils.getSuffixName(file.getName());
                    }
                    String filename = String.format("%s.%s", GlobalUtils.getGuidNoConnect(), suffix);
                    requestBody.addFormDataPart(entry.getKey(), filename, body);
                    //内部使用
                    internalProcessParams.put(entry.getKey(), "file");
                } else if ((entry.getValue() instanceof List) || (entry.getValue() instanceof Map)) {
                    String json = JsonUtils.toJson(entry.getValue());
                    requestBody.addFormDataPart(entry.getKey(), json);
                    //内部使用
                    internalProcessParams.put(entry.getKey(), json);
                } else {
                    requestBody.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
                    //内部使用
                    internalProcessParams.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            MultipartBody body = requestBody.build();
            submitMultipartParams(builder, body);
        } else {
            RequestBody requestBody = addJsonRequestParams(validResult.ignoreParamContainsKeys, params);
            submitRequestParams(builder, requestBody);
        }
    }

    private RequestBody addJsonRequestParams(List<String> ignoreParamKeys, TreeMap<String, Object> params) {
        if (requestContentType == RequestContentType.Form) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (!ObjectJudge.isNullOrEmpty(params)) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() instanceof List) {
                        bodyBuilder.add(entry.getKey(), JsonUtils.toJson(entry.getValue()));
                    } else {
                        if (entry.getValue() instanceof Map) {
                            Map<String, Object> childMap = (Map<String, Object>) entry.getValue();
                            for (Map.Entry<String, Object> childEntry : childMap.entrySet()) {
                                bodyBuilder.add(childEntry.getKey(), String.valueOf(childEntry.getValue()));
                            }
                        } else {
                            bodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
                        }
                    }
                }
            }
            RequestBody requestBody = bodyBuilder.build();
            return requestBody;
        } else {
            if (ObjectJudge.isNullOrEmpty(params)) {
                return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}");
            }
            if (ObjectJudge.isNullOrEmpty(ignoreParamKeys)) {
                String body = JsonUtils.toJson(params);
                return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
            } else {
                //如果包含有忽略参数将忽略其它参数提交
                if (ignoreParamKeys.size() == 1) {
                    String key = ignoreParamKeys.get(0);
                    String value = String.valueOf(params.get(key));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value);
                    return requestBody;
                } else {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    for (String paramKey : ignoreParamKeys) {
                        map.put(paramKey, params.get(paramKey));
                    }
                    String value = JsonUtils.toJson(map);
                    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value);
                }
            }
        }
    }

    private class ValidResult {
        //被忽略参数key
        public List<String> ignoreParamContainsKeys = new ArrayList<String>();
        //流参数key
        public List<String> streamParamKeys = new ArrayList<String>();
        //文件参数key
        public List<String> fileParamKeys = new ArrayList<String>();
    }

    private ValidResult validParams(TreeMap<String, Object> params) {
        ValidResult result = new ValidResult();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().startsWith(OkRxKeys.ignoreParamContainsKey) && entry.getValue() instanceof String) {
                if (!result.ignoreParamContainsKeys.contains(entry.getKey())) {
                    result.ignoreParamContainsKeys.add(entry.getKey());
                }
            } else if (entry.getValue() instanceof byte[]) {
                if (!result.streamParamKeys.contains(entry.getKey())) {
                    result.streamParamKeys.add(entry.getKey());
                }
            } else if (entry.getValue() instanceof File) {
                if (!result.fileParamKeys.contains(entry.getKey())) {
                    result.fileParamKeys.add(entry.getKey());
                }
            }
        }
        return result;
    }

    private String addGetRequestParams(String url, TreeMap<String, Object> params) {
        if (ObjectJudge.isNullOrEmpty(params)) {
            return url;
        }
        StringBuilder builder = new StringBuilder();
        int index = 0;
        int count = params.size();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> childMap = (Map) entry.getValue();
                count += childMap.size() - 1;
                for (Map.Entry<String, Object> childEntry : childMap.entrySet()) {
                    joinSingleParamForGet(builder, childEntry, index, count);
                    index++;
                }
            } else {
                joinSingleParamForGet(builder, entry, index, count);
                index++;
            }
        }
        //判断原url中是否包含?
        if (StringUtils.isContains(url, "?")) {
            return String.format("%s&%s&time=%s", url, builder.toString(), System.currentTimeMillis());
        } else {
            return String.format("%s?%s&time=%s", url, builder.toString(), System.currentTimeMillis());
        }
    }

    private void joinSingleParamForGet(StringBuilder builder, Map.Entry<String, Object> entry, int index, int count) {
        String value = "";
        try {
            value = URLEncoder.encode(String.valueOf(entry.getValue()).trim(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
        }
        builder.append(entry.getKey());
        builder.append("=");
        builder.append(value);
        builder.append((index + 1) < count ? "&" : "");
        //内部使用
        internalProcessParams.put(entry.getKey(), value);
    }

    protected String getAllParamsJoin(HashMap<String, String> headers, TreeMap<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        //拼接参数
        if (!ObjectJudge.isNullOrEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.append(String.format("_%s_%s", entry.getKey(), entry.getValue()));
            }
        }
        RetrofitParams retrofitParams = getRetrofitParams();
        if (retrofitParams.isTokenValid()) {
            //只有需要用户登录的接口拼接身份数据
            //拼接headers
            if (!ObjectJudge.isNullOrEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.append(String.format("_%s_%s", entry.getKey(), entry.getValue()));
                }
            }
            //拼接cookies参数
            OnHeaderCookiesListener cookiesListener = OkRx.getInstance().getOnHeaderCookiesListener();
            Map<String, String> map = cookiesListener.onCookiesCall();
            if (!ObjectJudge.isNullOrEmpty(map)) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    builder.append(String.format("_%s_%s", entry.getKey(), entry.getValue()));
                }
            }
        }
        return builder.toString();
    }

    protected void bindCookies(OkHttpClient client, HttpUrl url) {
        try {
            //获取cookies列表
            OnHeaderCookiesListener cookiesListener = OkRx.getInstance().getOnHeaderCookiesListener();
            Map<String, String> map = cookiesListener.onCookiesCall();
            if (ObjectJudge.isNullOrEmpty(map)) {
                return;
            }
            //获取已有cookie对象
            List<Cookie> cookies = client.cookieJar().loadForRequest(url);
            //移除包含中文cookies
            Set<String> keys = removeContainChineseRepeatCookies(cookies, map);
            //添加cookie
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Cookie.Builder builder = new Cookie.Builder();
                String key = getCookieCode(entry.getKey());
                String value = getCookieCode(entry.getValue());
                Cookie cookie = builder.name(key).value(value).domain(url.host()).build();
                if (keys.contains(key)) {
                    removeHasCookie(cookies, key);
                }
                cookies.add(cookie);
            }
            //重新设置到http
            client.cookieJar().saveFromResponse(url, cookies);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private String getCookieCode(String value) {
        if (value == null) {
            return "";
        }
        String matche = ValidUtils.matche("[\\u4e00-\\u9fa5]", value);
        if (TextUtils.isEmpty(matche)) {
            return value;
        } else {
            try {
                return URLEncoder.encode(value, "utf-8");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    private Set<String> removeContainChineseRepeatCookies(List<Cookie> cookies, Map<String, String> map) {
        Set<String> keys = new HashSet<String>();
        Iterator<Cookie> iterator = cookies.iterator();
        while (iterator.hasNext()) {
            Cookie next = iterator.next();
            String matche = ValidUtils.matche("[\\u4e00-\\u9fa5]", next.value());
            //如果有包含中文或在新增队列中移除
            if (!TextUtils.isEmpty(matche) || map.containsKey(next.name())) {
                //包含中文
                iterator.remove();
            } else {
                //如果有重复移除
                if (keys.contains(next.name())) {
                    iterator.remove();
                } else {
                    keys.add(next.name());
                }
            }
        }
        return keys;
    }

    private void removeHasCookie(List<Cookie> cookies, String key) {
        Iterator<Cookie> iterator = cookies.iterator();
        while (iterator.hasNext()) {
            Cookie next = iterator.next();
            String name = next.name();
            if (TextUtils.equals(String.valueOf(name).trim(), String.valueOf(key))) {
                iterator.remove();
                break;
            }
        }
    }

    //缓存处理返回true继续之后处理,false不作之后网络处理
    protected boolean cacheDealWith(CallStatus callStatus,
                                    HashMap<String, String> headers,
                                    Action4<ResponseData, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                                    String apiRequestKey,
                                    HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        //网络请求-在缓存未失效时网络数据与缓存只会返回其中一个,缓存失效后先请求网络->再缓存->最后返回;
        //即首次请求或缓存失效的情况会走网络,否则每次只取缓存数据;
        //OnlyCache,
        //
        //每次只作网络请求;
        //OnlyNet,
        //
        //网络请求-在缓存未失败时获取到网络数据和缓存数据均会回调,缓存失效后先请求网络->再缓存->最后返回(即此时只作网络数据的回调);
        //1.有缓存时先回调缓存数据再请求网络数据然后[缓存+回调];
        //2.无缓存时不作缓存回调直接请求网络数据后[缓存+回调];
        //WeakCacheAccept,
        //
        //1.有缓存时先回调缓存数据再请求网络数据然后[缓存]不作网络回调;
        //2.无缓存时不作缓存回调直接请求网络数据后[缓存]不作网络回调;
        //WeakCache
        if (callStatus != CallStatus.OnlyNet) {
            String ckey = String.format("%s%s", retrofitParams.getCacheKey(), getAllParamsJoin(headers, retrofitParams.getParams()));
            CacheDataItem dataItem = RxCache.getBaseCacheData(String.valueOf(ckey.hashCode()), true);
            if (successAction != null && dataItem != null) {
                ResponseData responseData = new ResponseData();
                responseData.setResponseDataType(ResponseDataType.object);
                responseData.setResponse(dataItem.getValue());
                if (callStatus == CallStatus.TakNetwork) {
                    if (TextUtils.isEmpty(responseData.getResponse())) {
                        successAction.call(responseData, apiRequestKey, reqQueueItemHashMap, DataType.EmptyForOnlyCache);
                    } else {
                        successAction.call(responseData, apiRequestKey, reqQueueItemHashMap, DataType.CacheData);
                    }
                    //此状态下不作网络请求
                    return false;
                } else if (!TextUtils.isEmpty(dataItem.getValue())) {
                    successAction.call(responseData, apiRequestKey, reqQueueItemHashMap, DataType.CacheData);
                    //1.有缓存时先回调缓存数据再请求网络数据然后[缓存+回调];
                    //2.无缓存时不作缓存回调直接请求网络数据后[缓存+回调];
                    //3.有缓存时先回调缓存数据再请求网络数据然后[缓存]不作网络回调;
                    //4.无缓存时不作缓存回调直接请求网络数据后[缓存]不作网络回调;
                    //首次请求时缓存失效的情况会走网络,否则每次只取缓存数据;
                    //具体类型参考{@link CallStatus}
                    if (callStatus == CallStatus.OnlyCache) {
                        return false;
                    } else if (callStatus == CallStatus.PersistentIntervalCache) {
                        long intervalCacheTime = dataItem.getIntervalCacheTime();
                        long stime = dataItem.getStartTime() + intervalCacheTime;
                        if (stime > System.currentTimeMillis()) {
                            //如果stime大于当前时间表示在间隔时间的有效范围内不作网络请求
                            setCancelIntervalCacheCall(true);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
