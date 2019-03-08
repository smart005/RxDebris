package com.cloud.nets.requests;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.cloud.nets.OkRx;
import com.cloud.nets.OkRxKeys;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.events.OnHeaderCookiesListener;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RequestContentType;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action3;
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
                     Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                     Action1<RequestState> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction) {
        //子类重载方法
    }

    public void call(String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     Action3<Bitmap, String, HashMap<String, ReqQueueItem>> successAction,
                     Action1<RequestState> completeAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction) {
        //子类重载方法
    }

    public void setRequestContentType(RequestContentType requestContentType) {
        this.requestContentType = requestContentType;
    }

    protected Request.Builder getBuilder(String url,
                                         HashMap<String, String> headers,
                                         HashMap<String, Object> params) {
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
                addRequestParams(builder, params);
            }
        }
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
    private void addRequestParams(Request.Builder builder, HashMap<String, Object> params) {
        ValidResult validResult = validParams(params);
        if (!ObjectJudge.isNullOrEmpty(validResult.streamParamKeys) ||
                !ObjectJudge.isNullOrEmpty(validResult.fileParamKeys)) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (validResult.streamParamKeys.contains(entry.getKey())) {
                    //以字节流的形式上传文件
                    MediaType mediaType = MediaType.parse("application/octet-stream");
                    RequestBody body = RequestBody.create(mediaType, (byte[]) entry.getValue());
                    String filename = String.format("%s.rxtiny", GlobalUtils.getGuidNoConnect());
                    requestBody.addFormDataPart(entry.getKey(), filename, body);
                } else if (validResult.fileParamKeys.contains(entry.getKey())) {
                    //以文件的形式上传文件
                    MediaType mediaType = MediaType.parse("multipart/form-data");
                    RequestBody body = RequestBody.create(mediaType, (File) entry.getValue());
                    String filename = String.format("%s.rxtiny", GlobalUtils.getGuidNoConnect());
                    requestBody.addFormDataPart(entry.getKey(), filename, body);
                } else if ((entry.getValue() instanceof List) || (entry.getValue() instanceof Map)) {
                    requestBody.addFormDataPart(entry.getKey(), JsonUtils.toStr(entry.getValue()));
                } else {
                    requestBody.addFormDataPart(entry.getKey(), entry.getValue() + "");
                }
            }
            MultipartBody body = requestBody.build();
            submitMultipartParams(builder, body);
        } else {
            RequestBody requestBody = addJsonRequestParams(validResult.ignoreParamContainsKeys, params);
            submitRequestParams(builder, requestBody);
        }
    }

    private RequestBody addJsonRequestParams(List<String> ignoreParamKeys, HashMap<String, Object> params) {
        if (requestContentType == RequestContentType.Form) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (!ObjectJudge.isNullOrEmpty(params)) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() instanceof List) {
                        bodyBuilder.add(entry.getKey(), JsonUtils.toStr(entry.getValue()));
                    } else {
                        bodyBuilder.add(entry.getKey(), entry.getValue() + "");
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
                String body = JsonUtils.toStr(params);
                return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
            } else {
                //如果包含有忽略参数将忽略其它参数提交
                if (ignoreParamKeys.size() == 1) {
                    String key = ignoreParamKeys.get(0);
                    Object value = params.get(key);
                    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value + "");
                } else {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    for (String paramKey : ignoreParamKeys) {
                        map.put(paramKey, params.get(paramKey));
                    }
                    String value = JsonUtils.toStr(map);
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

    private ValidResult validParams(HashMap<String, Object> params) {
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

    private String addGetRequestParams(String url, HashMap<String, Object> params) {
        if (ObjectJudge.isNullOrEmpty(params)) {
            return url;
        }
        StringBuilder builder = new StringBuilder();
        int index = 0;
        int count = params.size();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue() + "";
            builder.append(entry.getKey() + "=" + value.trim() + ((index + 1) < count ? "&" : ""));
            index++;
        }
        //判断原url中是否包含?
        if (StringUtils.isContains(url, "?")) {
            return String.format("%s&time=%s&%s", url, System.currentTimeMillis(), builder.toString());
        } else {
            return String.format("%s?time=%s&%s", url, System.currentTimeMillis(), builder.toString());
        }
    }

    protected String getAllParamsJoin(HashMap<String, String> headers, HashMap<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        //拼接参数
        if (!ObjectJudge.isNullOrEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.append(String.format("_%s_%s", entry.getKey(), entry.getValue()));
            }
        }
        //拼接headers
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.append(String.format("_%s_%s", entry.getKey(), entry.getValue()));
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
}
