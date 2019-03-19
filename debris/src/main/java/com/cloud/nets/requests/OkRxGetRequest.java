package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.cache.CacheDataItem;
import com.cloud.cache.RxCache;
import com.cloud.cache.RxStacks;
import com.cloud.nets.OkRx;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.callback.StringCallback;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action4;
import com.cloud.objects.events.OnNetworkConnectListener;

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
     * @param successAction       成功回调
     * @param completeAction      完成回调
     * @param printLogAction      日志输出回调
     * @param apiRequestKey       api请求标识
     * @param reqQueueItemHashMap 请求标识队列
     */
    @Override
    public void call(String url,
                     final HashMap<String, String> headers,
                     final Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                     final Action2<RequestState, ErrorType> completeAction,
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
                completeAction.call(RequestState.Completed, ErrorType.none);
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
        setCancelIntervalCacheCall(false);
        RetrofitParams retrofitParams = getRetrofitParams();
        CallStatus callStatus = retrofitParams.getCallStatus();
        if (callStatus != CallStatus.OnlyNet) {
            String ckey = String.format("%s%s", retrofitParams.getCacheKey(), getAllParamsJoin(headers, retrofitParams.getParams()));
            CacheDataItem dataItem = RxCache.getBaseCacheData(ckey, true);
            if (successAction != null && dataItem != null && !TextUtils.isEmpty(dataItem.getValue())) {
                responseString = dataItem.getValue();
                successAction.call(responseString, apiRequestKey, reqQueueItemHashMap, DataType.CacheData);
                //1.有缓存时先回调缓存数据再请求网络数据然后[缓存+回调];
                //2.无缓存时不作缓存回调直接请求网络数据后[缓存+回调];
                //3.有缓存时先回调缓存数据再请求网络数据然后[缓存]不作网络回调;
                //4.无缓存时不作缓存回调直接请求网络数据后[缓存]不作网络回调;
                //首次请求时缓存失效的情况会走网络,否则每次只取缓存数据;
                //具体类型参考{@link }
                if (callStatus == CallStatus.OnlyCache) {
                    return;
                } else if (callStatus == CallStatus.PersistentIntervalCache) {
                    long intervalCacheTime = dataItem.getIntervalCacheTime();
                    long stime = dataItem.getStartTime() + intervalCacheTime;
                    if (stime > System.currentTimeMillis()) {
                        //如果stime大于当前时间表示在间隔时间的有效范围内不作网络请求
                        setCancelIntervalCacheCall(true);
                        return;
                    }
                }
            }
        }
        //如果网络未连接则不作请求
        OnNetworkConnectListener networkConnectListener = RxAndroid.getInstance().getOnNetworkConnectListener();
        if (networkConnectListener != null && !networkConnectListener.isConnected()) {
            if (completeAction != null) {
                completeAction.call(RequestState.Error, ErrorType.netRequest);
                completeAction.call(RequestState.Completed, ErrorType.none);
            }
            return;
        }

        setRequestType(RequestType.GET);
        Request.Builder builder = getBuilder(url, headers, retrofitParams.getParams(), retrofitParams.getFileSuffixParams()).get();
        Request request = builder.tag(retrofitParams.getInvokeMethodName()).build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient();
        StringCallback callback = new StringCallback(successAction, completeAction, printLogAction, reqQueueItemHashMap, apiRequestKey, apiUnique, headersAction) {
            @Override
            protected void onSuccessCall(String responseString) {
                RetrofitParams retrofitParams = getRetrofitParams();
                CallStatus callStatus = retrofitParams.getCallStatus();
                if (callStatus != CallStatus.OnlyNet && !TextUtils.isEmpty(retrofitParams.getCacheKey())) {
                    String ckey = String.format("%s%s", retrofitParams.getCacheKey(), getAllParamsJoin(headers, retrofitParams.getParams()));
                    RxCache.setBaseCacheData(ckey, responseString, retrofitParams.getCacheTime(), TimeUnit.MILLISECONDS, retrofitParams.getIntervalCacheTime());
                }
            }
        };
        callback.setHeaders(retrofitParams.getHeadParams());
        callback.setParams(retrofitParams.getParams());
        callback.setRequestMethodName(retrofitParams.getInvokeMethodName());
        callback.setCancelIntervalCacheCall(isCancelIntervalCacheCall());
        //数据类型
        callback.setDataClass(retrofitParams.getDataClass());
        callback.setCallStatus(callStatus);
        //绑定cookies
        bindCookies(client, request.url());
        //请求网络
        client.newCall(request).enqueue(callback);
        //记录请求时的堆栈信息
        RxStacks.setStack(retrofitParams.getInvokeMethodName(), new Exception());
    }
}
