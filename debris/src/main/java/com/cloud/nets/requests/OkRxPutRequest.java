package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.cache.RxCache;
import com.cloud.nets.OkRx;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.callback.StringCallback;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
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
public class OkRxPutRequest extends BaseRequest {

    private String responseString = "";

    public OkRxPutRequest(RequestContentType requestContentType) {
        super.setRequestContentType(requestContentType);
    }

    @Override
    public void call(String url, final HashMap<String, String> headers, Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction, Action1<RequestState> completeAction, Action2<String, String> printLogAction, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiUnique, Action2<String, HashMap<String, String>> headersAction) {
        if (TextUtils.isEmpty(url)) {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            return;
        }

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
        RetrofitParams retrofitParams = getRetrofitParams();
        CallStatus callStatus = retrofitParams.getCallStatus();
        if (callStatus != CallStatus.OnlyNet) {
            String ckey = String.format("%s%s", retrofitParams.getCacheKey(), getAllParamsJoin(headers, retrofitParams.getParams()));
            String cache = RxCache.getCacheData(ckey);
            if (successAction != null && !TextUtils.isEmpty(cache)) {
                responseString = cache;
                successAction.call(responseString, apiRequestKey, reqQueueItemHashMap, DataType.CacheData);
                //1.有缓存时先回调缓存数据再请求网络数据然后[缓存+回调];
                //2.无缓存时不作缓存回调直接请求网络数据后[缓存+回调];
                //3.有缓存时先回调缓存数据再请求网络数据然后[缓存]不作网络回调;
                //4.无缓存时不作缓存回调直接请求网络数据后[缓存]不作网络回调;
                //首次请求时缓存失效的情况会走网络,否则每次只取缓存数据;
                //具体类型参考{@link }
                if (callStatus == CallStatus.OnlyCache) {
                    return;
                }
            }
        }
        setRequestType(RequestType.PUT);
        Request.Builder builder = getBuilder(url, headers, retrofitParams.getParams());
        if (builder == null) {
            completeAction.call(RequestState.Completed);
            return;
        }
        Request request = builder.build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient();
        StringCallback callback = new StringCallback(successAction, completeAction, printLogAction, reqQueueItemHashMap, apiRequestKey, apiUnique, headersAction) {
            @Override
            protected void onSuccessCall(String responseString) {
                RetrofitParams retrofitParams = getRetrofitParams();
                CallStatus callStatus = retrofitParams.getCallStatus();
                if (callStatus != CallStatus.OnlyNet && !TextUtils.isEmpty(retrofitParams.getCacheKey())) {
                    String ckey = String.format("%s%s", retrofitParams.getCacheKey(), getAllParamsJoin(headers, retrofitParams.getParams()));
                    RxCache.setCacheData(ckey, responseString, retrofitParams.getCacheTime(), TimeUnit.MILLISECONDS);
                }
            }
        };
        callback.setCallStatus(callStatus);
        client.newCall(request).enqueue(callback);
    }
}
