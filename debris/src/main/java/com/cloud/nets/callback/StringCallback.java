package com.cloud.nets.callback;

import android.text.TextUtils;

import com.cloud.nets.OkRx;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.nets.requests.ErrorWith;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action4;
import com.cloud.objects.logs.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    private Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction = null;
    //请求完成时回调(成功或失败)
    private Action2<RequestState, ErrorType> completeAction = null;
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
    //请求回调状态
    private CallStatus callStatus = CallStatus.OnlyNet;
    //是否取消间隔缓存回调
    private boolean isCancelIntervalCacheCall = false;
    //返回数据类型
    private Class dataClass = null;

    public boolean isCancelIntervalCacheCall() {
        return isCancelIntervalCacheCall;
    }

    public void setCancelIntervalCacheCall(boolean cancelIntervalCacheCall) {
        isCancelIntervalCacheCall = cancelIntervalCacheCall;
    }

    public void setCallStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public void setDataClass(Class dataClass) {
        this.dataClass = dataClass;
    }

    protected abstract void onSuccessCall(String responseString);

    public StringCallback(Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                          Action2<RequestState, ErrorType> completeAction,
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
            if (queueItem != null) {
                queueItem.setReqNetCompleted(true);
            }
        }
        if (call.isCanceled()) {
            if (completeAction != null) {
                completeAction.call(RequestState.Error, ErrorType.netRequest);
            }
            return;
        }
        String message = e.getMessage() == null ? "" : e.getMessage();
        if (message.contains("Unable to resolve host") ||
                message.contains("Failed to connect")) {
            //这里做dns处理
            if (completeAction != null) {
                completeAction.call(RequestState.Error, ErrorType.netRequest);
            }
            return;
        }
        if (!call.isExecuted()) {
            if (!failReConnect(call)) {
                //抛出失败回调到全局监听
                ErrorWith errorWith = new ErrorWith();
                errorWith.call(call, e);
                if (completeAction != null) {
                    completeAction.call(RequestState.Error, ErrorType.netRequest);
                }
            }
            return;
        }
        //抛出失败回调到全局监听
        ErrorWith errorWith = new ErrorWith();
        errorWith.call(call, e);
        if (completeAction != null) {
            completeAction.call(RequestState.Error, ErrorType.netRequest);
        }
    }

    private boolean failReConnect(Call call) {
        Request request = call.request();
        HttpUrl url = request.url();
        String host = url.host();
        Set<String> domainList = OkRx.getInstance().getFailDomainList();
        if (domainList.contains(host)) {
            //如果域名已在失败列表在新创建连接并重新请求仍失败,服务器地址有问题或当前网络异常;
            //此时直接返回即可
            return false;
        }
        domainList.add(host);
        //如果连接已经被取消时则重新建立
        OkHttpClient client = OkRx.getInstance().getOkHttpClient(true);
        //创建新请求
        Call clone = call.clone();
        client.newCall(clone.request()).enqueue(this);
        return true;
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
            //请求成功后将连接从缓存列表移除
            Request request = call.request();
            HttpUrl url = request.url();
            String host = url.host();
            Set<String> domainList = OkRx.getInstance().getFailDomainList();
            if (domainList.contains(host)) {
                domainList.remove(host);
            }
            if (response == null || !response.isSuccessful()) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error, ErrorType.businessProcess);
                }
            } else {
                headerDealWith(response);
                ResponseBody body = response.body();
                if (body == null) {
                    if (completeAction != null) {
                        completeAction.call(RequestState.Error, ErrorType.businessProcess);
                    }
                } else {
                    if (successAction != null) {
                        responseString = body.string();
                        //如果不是json且请求的数据类型不是基础数据类型则回调error
                        if (dataClass == String.class ||
                                dataClass == Integer.class ||
                                dataClass == Double.class ||
                                dataClass == Float.class ||
                                dataClass == Long.class ||
                                ObjectJudge.isJson(responseString)) {
                            if (callStatus != CallStatus.WeakCache && !isCancelIntervalCacheCall()) {
                                //此状态下不做网络回调但做缓存
                                successAction.call(responseString, apiRequestKey, reqQueueItemHashMap, DataType.NetData);
                            }
                            onSuccessCall(responseString);
                        } else {
                            if (completeAction != null) {
                                completeAction.call(RequestState.Error, ErrorType.businessProcess);
                            }
                        }
                    }
                }
            }
            //输出日志
            if (printLogAction != null) {
                printLogAction.call(apiRequestKey, responseString);
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                if (queueItem != null) {
                    queueItem.setReqNetCompleted(true);
                }
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed, ErrorType.none);
            }
        }
    }
}
