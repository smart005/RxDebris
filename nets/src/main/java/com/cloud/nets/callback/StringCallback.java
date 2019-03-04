package com.cloud.nets.callback;

import android.text.TextUtils;

import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action4;
import com.cloud.objects.logs.Logger;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/30
 * Description:
 * Modifier:
 * ModifyContent:
 */
public abstract class StringCallback implements Callback {

    //处理成功回调
    private Action4<String, String, HashMap<String, ReqQueueItem>, Boolean> successAction = null;
    //请求完成时回调(成功或失败)
    private Action1<RequestState> completeAction = null;
    //请求完成时输出日志
    private Action2<String, String> printLogAction = null;
    //请求标识队列
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = null;
    //请求标识
    private String apiRequestKey = "";
    //数据返回内容
    private String responseString = "";
    //api唯一标识
    private String apiUnique = "";
    //header回调
    private Action2<String, HashMap<String, String>> headersAction = null;

    protected abstract void onSuccessCall(String responseString);

    public StringCallback(Action4<String, String, HashMap<String, ReqQueueItem>, Boolean> successAction,
                          Action1<RequestState> completeAction,
                          Action2<String, String> printLogAction,
                          HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                          String apiRequestKey,
                          String apiUnique,
                          Action2<String, HashMap<String, String>> headersAction) {
        this.successAction = successAction;
        this.completeAction = completeAction;
        this.printLogAction = printLogAction;
        this.reqQueueItemHashMap = reqQueueItemHashMap;
        this.apiRequestKey = apiRequestKey;
        this.apiUnique = apiUnique;
        this.headersAction = headersAction;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
            queueItem.setReqNetCompleted(true);
        }
        if (completeAction != null) {
            completeAction.call(RequestState.Error);
        }
    }

    private void headerDealWith(Response response) {
        if (TextUtils.isEmpty(apiUnique)) {
            return;
        }
        Headers headers = response.headers();
        if (headers == null) {
            return;
        }
        if (headersAction == null) {
            return;
        }
        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        String[] headerParamNames = builder.getHttpHeaderParamNames();
        if (ObjectJudge.isNullOrEmpty(headerParamNames)) {
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        for (String paramName : headerParamNames) {
            map.put(paramName, headers.get(paramName));
        }
        headersAction.call(apiUnique, map);
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            if (response == null || !response.isSuccessful()) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
            }
            headerDealWith(response);
            ResponseBody body = response.body();
            if (body == null) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
                return;
            }
            if (successAction != null) {
                responseString = body.string();
                successAction.call(responseString, apiRequestKey, reqQueueItemHashMap, true);

                onSuccessCall(responseString);

                //输出日志
                if (printLogAction != null) {
                    printLogAction.call(apiRequestKey, responseString);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                queueItem.setReqNetCompleted(true);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
        }
    }
}
