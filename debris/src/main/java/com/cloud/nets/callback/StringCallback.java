package com.cloud.nets.callback;

import android.text.TextUtils;

import com.cloud.cache.RxStacks;
import com.cloud.nets.OkRx;
import com.cloud.nets.beans.ResponseData;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.enums.ResponseDataType;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.nets.requests.NetErrorWith;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.TaskManager;
import com.cloud.objects.beans.TaskEntry;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action4;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
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
    private Action4<ResponseData, String, HashMap<String, ReqQueueItem>, DataType> successAction = null;
    //请求完成时回调(成功或失败)
    private Action2<RequestState, ErrorType> completeAction = null;
    //请求完成时输出日志
    private Action2<String, String> printLogAction = null;
    //请求标识队列
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = null;
    //请求标识
    private String apiRequestKey = "";
    //数据返回内容
    private ResponseData responseData = new ResponseData();
    //api唯一标识
    private String apiUnique = "";
    //请求回调状态
    private CallStatus callStatus = CallStatus.OnlyNet;
    //是否取消间隔缓存回调
    private boolean isCancelIntervalCacheCall = false;
    //返回数据类型
    private Class dataClass = null;
    //请求方法名
    private String requestMethodName = "";
    //headers
    private Map<String, String> headers = null;
    //请求参数
    private Map<String, Object> params = null;
    //响应数据类型
    private ResponseDataType responseDataType = null;
    //请求失败后是否重试
    private boolean isFailureRetry = false;

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

    protected abstract void onSuccessCall(ResponseData responseData);

    public void setRequestMethodName(String requestMethodName) {
        this.requestMethodName = requestMethodName;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setResponseDataType(ResponseDataType responseDataType) {
        this.responseDataType = responseDataType;
    }

    public void setFailureRetry(boolean failureRetry) {
        isFailureRetry = failureRetry;
    }

    public StringCallback(Action4<ResponseData, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                          Action2<RequestState, ErrorType> completeAction,
                          Action2<String, String> printLogAction,
                          HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                          String apiRequestKey,
                          String apiUnique) {
        this.successAction = successAction;
        this.completeAction = completeAction;
        this.printLogAction = printLogAction;
        this.reqQueueItemHashMap = reqQueueItemHashMap;
        this.apiRequestKey = apiRequestKey;
        this.apiUnique = apiUnique;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
            if (queueItem != null) {
                queueItem.setReqNetCompleted(true);
            }
        }
        //输出debug模式下日志
        outputLogForDebug(call, e.getMessage());
        if (call.isCanceled()) {
            if (completeAction != null) {
                //请求失败后是否重试
                if (isFailureRetry) {
                    if (failureAutoCall(call)) {
                        return;
                    }
                }
                //回调错误
                completeAction.call(RequestState.Error, ErrorType.netRequest);
                //结束回调
                completeAction.call(RequestState.Completed, ErrorType.none);
            }
            return;
        }
        String message = e.getMessage() == null ? "" : e.getMessage();
        if (message.contains("Unable to resolve host") ||
                message.contains("Failed to connect")) {
            //这里做dns处理
            if (completeAction != null) {
                //错误回调
                completeAction.call(RequestState.Error, ErrorType.netRequest);
                //完成回调
                completeAction.call(RequestState.Completed, ErrorType.none);
            }
            NetErrorWith netErrorWith = new NetErrorWith();
            netErrorWith.call(requestMethodName, call, e, headers, params);
            return;
        }
        if (!call.isExecuted()) {
            if (!failReConnect(call)) {
                //抛出失败回调到全局监听
                if (completeAction != null) {
                    //错误回调
                    completeAction.call(RequestState.Error, ErrorType.netRequest);
                    //完成回调
                    completeAction.call(RequestState.Completed, ErrorType.none);
                }
                NetErrorWith netErrorWith = new NetErrorWith();
                netErrorWith.call(requestMethodName, call, e, headers, params);
            }
            return;
        }
        //抛出失败回调到全局监听
        if (completeAction != null) {
            //请求失败后是否重试
            if (isFailureRetry) {
                if (failureAutoCall(call)) {
                    return;
                }
            }
            //错误回调
            completeAction.call(RequestState.Error, ErrorType.netRequest);
            //完成回调
            completeAction.call(RequestState.Completed, ErrorType.none);
        }
        NetErrorWith netErrorWith = new NetErrorWith();
        netErrorWith.call(requestMethodName, call, e, headers, params);
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
                    //请求失败后是否重试
                    if (isFailureRetry) {
                        if (failureAutoCall(call)) {
                            return;
                        }
                    }
                    completeAction.call(RequestState.Error, ErrorType.businessProcess);
                }
                //输出debug模式下日志
                outputLogForDebug(call, String.format("protocol=%s;code=%s;message=%s;", response.protocol().toString(), response.code(), response.message()));
            } else {
                ResponseBody body = response.body();
                if (body == null) {
                    if (completeAction != null) {
                        //请求失败后是否重试
                        if (isFailureRetry) {
                            if (failureAutoCall(call)) {
                                return;
                            }
                        }
                        completeAction.call(RequestState.Error, ErrorType.businessProcess);
                    }
                    //输出debug模式下日志
                    outputLogForDebug(call, String.format("protocol=%s;code=%s;message=%s;", response.protocol().toString(), response.code(), response.message()));
                } else {
                    bindResponseData(call, body);
                }
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
            //清除本次请求堆栈信息
            RxStacks.clearBusStacks(requestMethodName);
        }
    }

    private void bindResponseData(Call call, ResponseBody body) throws IOException {
        responseData.setResponseDataType(responseDataType);
        if (responseDataType == ResponseDataType.object) {
            //object\int\double\float\long\string
            responseData.setResponse(body.string());
            //输出debug模式下日志
            outputLogForDebug(call, "");
            if (successAction == null) {
                return;
            }
            //如果不是json且请求的数据类型不是基础数据类型则回调error
            if (dataClass == String.class ||
                    dataClass == Integer.class ||
                    dataClass == Double.class ||
                    dataClass == Float.class ||
                    dataClass == Long.class ||
                    ObjectJudge.isJson(responseData.getResponse())) {
                if (callStatus != CallStatus.WeakCache && !isCancelIntervalCacheCall()) {
                    //此状态下不做网络回调但做缓存
                    successAction.call(responseData, apiRequestKey, reqQueueItemHashMap, DataType.NetData);
                }
                onSuccessCall(responseData);
            } else {
                if (completeAction != null) {
                    //请求失败后是否重试
                    if (isFailureRetry) {
                        if (failureAutoCall(call)) {
                            return;
                        }
                    }
                    completeAction.call(RequestState.Error, ErrorType.businessProcess);
                }
            }
        } else if (responseDataType == ResponseDataType.byteData) {
            responseData.setBytes(body.bytes());
            if (successAction == null) {
                return;
            }
            successAction.call(responseData, apiRequestKey, reqQueueItemHashMap, DataType.NetData);
        } else if (responseDataType == ResponseDataType.stream) {
            responseData.setStream(body.byteStream());
            if (successAction == null) {
                return;
            }
            successAction.call(responseData, apiRequestKey, reqQueueItemHashMap, DataType.NetData);
        }
    }

    //输出debug模式下日志
    private void outputLogForDebug(Call call, String message) {
        if (call == null) {
            return;
        }
        //如果debug模式下打印日志
        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        if (builder.isDebug()) {
            //获取url
            Request request = call.request();
            HttpUrl httpUrl = request.url();
            String url = httpUrl.toString();
            StringBuilder logbuilder = new StringBuilder();
            logbuilder.append(String.format("url:%s\n", url));
            logbuilder.append(String.format("header:%s\n", JsonUtils.toStr(headers)));
            logbuilder.append(String.format("params:%s\n", JsonUtils.toStr(params)));
            if (TextUtils.isEmpty(responseData.getResponse())) {
                logbuilder.append(String.format("message:%s", message));
            } else {
                logbuilder.append(String.format("result:%s", responseData.getResponse()));
            }
            HandlerManager.getInstance().post(new RunnableParamsN<StringBuilder>() {
                @Override
                public void run(StringBuilder... builders) {
                    if (ObjectJudge.isNullOrEmpty(builders)) {
                        return;
                    }
                    Logger.info("net", builders[0].toString());
                }
            }, logbuilder);
        }
    }

    //失败自动回调
    private boolean failureAutoCall(Call call) {
        //获取请求对象
        Request request = call.request();
        //获取请求url
        HttpUrl httpUrl = request.url();
        String url = httpUrl.toString();

        TaskManager taskManager = TaskManager.getInstance();
        TaskEntry<? extends Runnable> taskEntry = taskManager.getTask(url);
        if (taskEntry == null) {
            taskManager.addPerformTask(url, new TaskRunable(url, call.clone(), this, taskManager), 100, 5000);
        } else {
            if (taskEntry.getCount() > taskEntry.getPerformCounts()) {
                taskManager.removeTask(url);
                return false;
            } else {
                taskEntry.setCount(taskEntry.getCount() + 1);
                taskEntry.setDelayTime(taskEntry.getDelayTime() + 5000);
                taskManager.execute(taskEntry);
            }
        }
        return true;
    }

    private class TaskRunable implements Runnable {

        private String key;
        private Call call;
        private StringCallback callback;
        private TaskManager taskManager;

        public TaskRunable(String key, Call call, StringCallback callback, TaskManager taskManager) {
            this.key = key;
            this.call = call;
            this.callback = callback;
            this.taskManager = taskManager;
        }

        @Override
        public void run() {
            if (call == null || callback == null) {
                if (taskManager != null) {
                    taskManager.removeTask(key);
                }
                return;
            }
            OkHttpClient client = OkRx.getInstance().getOkHttpClient();
            //创建新请求
            client.newCall(call.request()).enqueue(callback);
        }
    }
}
