package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.cache.RxCache;
import com.cloud.nets.OkRx;
import com.cloud.nets.beans.ResponseData;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.callback.StringCallback;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.enums.ResponseDataType;
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
                     final Action4<ResponseData, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                     final Action2<RequestState, ErrorType> completeAction,
                     final Action2<String, String> printLogAction,
                     final String apiRequestKey,
                     final HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique) {
        if (TextUtils.isEmpty(url)) {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed, ErrorType.none);
            }
            return;
        }
        setCancelIntervalCacheCall(false);
        RetrofitParams retrofitParams = getRetrofitParams();
        CallStatus callStatus = retrofitParams.getCallStatus();
        if (!cacheDealWith(callStatus, headers, successAction, apiRequestKey, reqQueueItemHashMap)) {
            //此时结束处理
            return;
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
        if (builder == null) {
            completeAction.call(RequestState.Completed, ErrorType.businessProcess);
            return;
        }
        Request request = builder.build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient();
        StringCallback callback = new StringCallback(successAction, completeAction, printLogAction, reqQueueItemHashMap, apiRequestKey, apiUnique) {
            @Override
            protected void onSuccessCall(ResponseData responseData) {
                ResponseDataType responseDataType = responseData.getResponseDataType();
                if (responseDataType != ResponseDataType.object) {
                    return;
                }
                RetrofitParams retrofitParams = getRetrofitParams();
                CallStatus callStatus = retrofitParams.getCallStatus();
                if (callStatus != CallStatus.OnlyNet && !TextUtils.isEmpty(retrofitParams.getCacheKey())) {
                    String ckey = String.format("%s%s", retrofitParams.getCacheKey(), getAllParamsJoin(headers, retrofitParams.getParams()));
                    RxCache.setBaseCacheData(String.valueOf(ckey.hashCode()), responseData.getResponse(), retrofitParams.getCacheTime(), TimeUnit.MILLISECONDS, retrofitParams.getIntervalCacheTime());
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
        callback.setResponseDataType(retrofitParams.getResponseDataType());
        //请求失败后是否重试
        callback.setFailureRetry(retrofitParams.isFailureRetry());
        //绑定cookies
        bindCookies(client, request.url());
        //请求网络
        client.newCall(request).enqueue(callback);
    }
}
