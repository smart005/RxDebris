package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.cache.RxCache;
import com.cloud.nets.OkRx;
import com.cloud.nets.callback.StringCallback;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action4;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class OkRxGetRequest extends BaseRequest {

    private String responseString = "";

    /**
     * get请求回调
     *
     * @param url                 请求完整路径
     * @param headers             请求头信息
     * @param params              请求参数
     * @param isCache             true-缓存;false-不缓存;
     * @param cacheKey            缓存键值
     * @param cacheTime           缓存时间
     * @param successAction       成功回调
     * @param completeAction      完成回调
     * @param printLogAction      日志输出回调
     * @param apiRequestKey       api请求标识
     * @param reqQueueItemHashMap 请求标识队列
     */
    @Override
    public void call(String url,
                     final HashMap<String, String> headers,
                     final HashMap<String, Object> params,
                     final boolean isCache,
                     final String cacheKey,
                     final long cacheTime,
                     final Action4<String, String, HashMap<String, ReqQueueItem>, Boolean> successAction,
                     final Action1<RequestState> completeAction,
                     final Action2<String, String> printLogAction,
                     final String apiRequestKey,
                     final HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction) {
        if (TextUtils.isEmpty(url)) {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            return;
        }
        if (isCache) {
            String ckey = String.format("%s%s", cacheKey, getAllParamsJoin(headers, params));
            String cache = RxCache.getCacheData(ckey);
            if (successAction != null && !TextUtils.isEmpty(cache)) {
                responseString = cache;
                successAction.call(responseString, apiRequestKey, reqQueueItemHashMap, !isCallNCData());
                //如果isCallNCData==false且符合缓存时间等条件时则只取缓存数据
                if (!isCallNCData()) {
                    return;
                }
            }
        }
        setRequestType(RequestType.GET);
        Request.Builder builder = getBuilder(url, headers, params).get();
        Request request = builder.build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient();
        client.newCall(request).enqueue(new StringCallback(successAction, completeAction, printLogAction, reqQueueItemHashMap, apiRequestKey, apiUnique, headersAction) {
            @Override
            protected void onSuccessCall(String responseString) {
                if (isCache && !TextUtils.isEmpty(cacheKey)) {
                    String ckey = String.format("%s%s", cacheKey, getAllParamsJoin(headers, params));
                    RxCache.setCacheData(ckey, responseString, cacheTime, TimeUnit.MILLISECONDS);
                }
            }
        });
    }
}
