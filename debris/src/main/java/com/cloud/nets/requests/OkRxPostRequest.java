package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.cache.RxCache;
import com.cloud.nets.OkRx;
import com.cloud.nets.callback.StringCallback;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.enums.RequestContentType;
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
public class OkRxPostRequest extends BaseRequest {

    private String responseString = "";
    private Action2<String, HashMap<String, String>> headersAction = null;

    public OkRxPostRequest(RequestContentType requestContentType) {
        super.setRequestContentType(requestContentType);
    }

    @Override
    public void call(String url, final HashMap<String, String> headers, final HashMap<String, Object> params, final boolean isCache, final String cacheKey, final long cacheTime, Action4<String, String, HashMap<String, ReqQueueItem>, Boolean> successAction, Action1<RequestState> completeAction, Action2<String, String> printLogAction, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiUnique, Action2<String, HashMap<String, String>> headersAction) {
        this.headersAction = headersAction;
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
        setRequestType(RequestType.POST);
        Request.Builder builder = getBuilder(url, headers, params);
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
