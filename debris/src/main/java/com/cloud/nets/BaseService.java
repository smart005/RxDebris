package com.cloud.nets;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cloud.nets.annotations.ApiCheckAnnotation;
import com.cloud.nets.annotations.ApiHeadersCall;
import com.cloud.nets.annotations.ReturnCodeFilter;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.events.OnApiRetCodesFilterListener;
import com.cloud.nets.events.OnAuthCallInfoListener;
import com.cloud.nets.events.OnBeanParsingJsonListener;
import com.cloud.nets.events.OnGlobalReuqestHeaderListener;
import com.cloud.nets.events.OnHttpRequestHeadersListener;
import com.cloud.nets.events.OnSuccessfulListener;
import com.cloud.nets.properties.ByteRequestItem;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.nets.properties.OkRxValidParam;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action0;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action3;
import com.cloud.objects.events.Action4;
import com.cloud.objects.events.Action6;
import com.cloud.objects.events.Func2;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.PathsUtils;
import com.cloud.objects.utils.ThreadPoolUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/6/14
 * Description:
 */
public class BaseService {

    /**
     * token值
     */
    private String token = "";
    /**
     * 接口名
     */
    private String apiName = "";

    private BaseSubscriber baseSubscriber = null;
    private HashMap<String, StringBuffer> logmaps = new HashMap<String, StringBuffer>();
    //请求队列
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = new HashMap<String, ReqQueueItem>();
    private Handler mhandler = new Handler(Looper.getMainLooper());
    private ReturnCodeFilter returnCodeFilter = null;

    /**
     * 获取token值
     */

    public String getToken() {
        if (token == null) {
            token = "";
        }
        return token;
    }

    /**
     * 设置token值
     * <p>
     * param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取接口名
     */
    public String getApiName() {
        if (apiName == null) {
            apiName = "";
        }
        return apiName;
    }

    /**
     * 设置接口名
     * <p>
     * param apiName
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public BaseSubscriber getBaseSubscriber() {
        return this.baseSubscriber;
    }

    public void setBaseSubscriber(BaseSubscriber baseSubscriber) {
        this.baseSubscriber = baseSubscriber;
    }

    /**
     * API请求完成(结束)
     */
    protected void onRequestCompleted() {

    }

    protected void onRequestError() {
        if (baseSubscriber == null) {
            return;
        }
        OnSuccessfulListener successfulListener = baseSubscriber.getOnSuccessfulListener();
        if (successfulListener == null) {
            return;
        }
        successfulListener.onError(null, baseSubscriber.getExtra());
    }

    protected <T> void baseConfig(final BaseService baseService,
                                  final RetrofitParams retrofitParams,
                                  OkRxValidParam validParam,
                                  final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                                  Action0 errorAction,
                                  Action0 completedAction,
                                  String apiRequestKey) {
        final Class<T> dataClass = retrofitParams.getDataClass();
        try {
            if (!TextUtils.isEmpty(retrofitParams.getRequestUrl())) {
                String requestUrl = retrofitParams.getRequestUrl();
                //全局头设置的信息
                HashMap<String, String> mHeaders = bindGlobalHeaders();
                //接口头信息
                mHeaders.putAll(retrofitParams.getHeadParams());
                //检查头部是否已添加token，没有则添加
                if (!TextUtils.isEmpty(token)) {
                    if (validParam.getApiCheckAnnotation().isTokenValid()) {
                        String tokenName = retrofitParams.getTokenName();
                        if (!TextUtils.isEmpty(tokenName)) {
                            mHeaders.put(tokenName, token);
                        }
                    }
                }

                //设置返回码监听
                if (returnCodeFilter == null) {
                    returnCodeFilter = validParam.getReturnCodeFilter();
                }
                //请求api
                reqQueueItemHashMap.put(apiRequestKey, new ReqQueueItem());
                if (retrofitParams.getRequestType() == RequestType.BYTES) {
                    HashMap<String, Object> updateByteParams = getUploadByteParams(retrofitParams);
                    List<ByteRequestItem> uploadByteItems = getUploadByteItems(retrofitParams);
                    subBytes(requestUrl, mHeaders, updateByteParams, uploadByteItems, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                } else {
                    //请求参数
                    if (retrofitParams.getRequestType() == RequestType.POST) {
                        post(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.DELETE) {
                        delete(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.PUT) {
                        put(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.PATCH) {
                        patch(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.HEAD) {
                        head(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.POST) {
                        options(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.TRACE) {
                        trace(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    } else {
                        get(requestUrl, mHeaders, retrofitParams, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
                    }
                }
            } else {
                finishedRequest(baseService, completedAction);
            }
        } catch (Exception e) {
            finishedRequest(baseService, completedAction);
            Logger.error(e);
        }
    }

    //绑定全局头信息
    private HashMap<String, String> bindGlobalHeaders() {
        HashMap<String, String> headParams = new HashMap<String, String>();
        HashMap<String, String> defaultHeaderParams = OkRx.getInstance().getHeaderParams();
        if (!ObjectJudge.isNullOrEmpty(defaultHeaderParams)) {
            for (Map.Entry<String, String> entry : defaultHeaderParams.entrySet()) {
                headParams.put(entry.getKey(), entry.getValue());
            }
        }
        //从监听对象中获取
        OnGlobalReuqestHeaderListener headerListener = OkRx.getInstance().getOnGlobalReuqestHeaderListener();
        if (headerListener == null) {
            return headParams;
        }
        HashMap<String, String> globalHeaderParams = headerListener.onHeaderParams();
        if (ObjectJudge.isNullOrEmpty(globalHeaderParams)) {
            return headParams;
        }
        for (Map.Entry<String, String> entry : globalHeaderParams.entrySet()) {
            headParams.put(entry.getKey(), entry.getValue());
        }
        return headParams;
    }

    private void finishedRequest(final BaseService baseService, final Action0 completedAction) {
        if (ObjectJudge.isMainThread()) {
            if (completedAction != null) {
                completedAction.call();
            }
            baseService.onRequestCompleted();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    if (completedAction != null) {
                        completedAction.call();
                    }
                    baseService.onRequestCompleted();
                }
            });
        }
    }

    private <T> void successDealWith(Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                                     Class<T> dataClass,
                                     BaseService baseService,
                                     String response,
                                     String apiRequestKey,
                                     HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                                     boolean isLastCall,
                                     long requestStartTime,
                                     long requestTotalTime,
                                     final Action0 completedAction) {
        if (successAction == null) {
            finishedRequest(baseService, completedAction);
        } else {
            T data = null;
            OnBeanParsingJsonListener<T> jsonListener = OkRx.getInstance().getOnBeanParsingJsonListener();
            if (jsonListener == null) {
                data = JsonUtils.parseT(response, dataClass);
            } else {
                data = jsonListener.onBeanParsingJson(response, dataClass);
            }
            //如果data==null则赋初始值
            if (data == null) {
                data = JsonUtils.newNull(dataClass);
            }
            //开启拦截且拦截符合的返回码
            OkRxConfigParams okRxConfigParams = OkRx.getInstance().getOkRxConfigParams();
            if (okRxConfigParams.isNetStatusCodeIntercept()) {
                if (!filterMatchRetCodes(data)) {
                    //成功回调
                    successAction.call(data, apiRequestKey, reqQueueItemHashMap, isLastCall, requestStartTime, requestTotalTime);
                }
            } else {
                //成功回调
                successAction.call(data, apiRequestKey, reqQueueItemHashMap, isLastCall, requestStartTime, requestTotalTime);
            }
        }
    }

    private <T> boolean filterMatchRetCodes(T data) {
        Class<?> codesListeningClass = returnCodeFilter.retCodesListeningClass();
        if (returnCodeFilter == null || ObjectJudge.isNullOrEmpty(returnCodeFilter.retCodes()) || codesListeningClass == null) {
            return false;
        }
        List<String> codes = Arrays.asList(returnCodeFilter.retCodes());
        String code = String.valueOf(GlobalUtils.getPropertiesValue(data, "code"));
        if (!codes.contains(code)) {
            return false;
        }
        Object obj = JsonUtils.newNull(codesListeningClass);
        if (obj == null || !(obj instanceof OnApiRetCodesFilterListener)) {
            //若状态码拦截成功且只是未实现处理，也中断之后处理;
            return true;
        }
        OnApiRetCodesFilterListener filterListener = (OnApiRetCodesFilterListener) obj;
        filterListener.onApiRetCodesFilter(code, data);
        return true;
    }

    private OnHttpRequestHeadersListener getHeaderCall(ApiHeadersCall apiHeadersCall) {
        if (apiHeadersCall == null) {
            return null;
        }
        Class<?> headersCallClass = apiHeadersCall.requestHeadersCallClass();
        Object callObject = JsonUtils.newNull(headersCallClass);
        if (callObject == null || !(callObject instanceof OnHttpRequestHeadersListener)) {
            return null;
        }
        return (OnHttpRequestHeadersListener) callObject;
    }

    private <T> void subBytes(String requestUrl,
                              HashMap<String, String> httpHeaders,
                              HashMap<String, Object> httpParams,
                              List<ByteRequestItem> byteRequestItems,
                              final BaseService baseService,
                              final Class<T> dataClass,
                              final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                              final Action0 errorAction,
                              final Action0 completedAction,
                              final String apiRequestKey) {
        OkRxManager.getInstance().uploadBytes(
                requestUrl,
                httpHeaders,
                httpParams,
                byteRequestItems,
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, true, 0, 0, completedAction);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        finallPrintLog(apiRequestKey, responseString);
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private void sendErrorAction(final Action0 errorAction, final BaseService baseService) {
        if (ObjectJudge.isMainThread()) {
            if (errorAction != null) {
                errorAction.call();
            }
            baseService.onRequestError();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    if (errorAction != null) {
                        errorAction.call();
                    }
                    baseService.onRequestError();
                }
            });
        }
    }

    private <T> void trace(String requestUrl,
                           HashMap<String, String> headers,
                           final RetrofitParams retrofitParams,
                           final BaseService baseService,
                           final Class<T> dataClass,
                           final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                           final Action0 errorAction,
                           final Action0 completedAction,
                           final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().trace(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T> void options(String requestUrl,
                             HashMap<String, String> headers,
                             final RetrofitParams retrofitParams,
                             final BaseService baseService,
                             final Class<T> dataClass,
                             final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                             final Action0 errorAction,
                             final Action0 completedAction,
                             final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().options(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T> void head(String requestUrl,
                          HashMap<String, String> headers,
                          final RetrofitParams retrofitParams,
                          final BaseService baseService,
                          final Class<T> dataClass,
                          final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                          final Action0 errorAction,
                          final Action0 completedAction,
                          final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().head(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T> void patch(String requestUrl,
                           HashMap<String, String> headers,
                           final RetrofitParams retrofitParams,
                           final BaseService baseService,
                           final Class<T> dataClass,
                           final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                           final Action0 errorAction,
                           final Action0 completedAction,
                           final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().patch(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T> void put(String requestUrl,
                         HashMap<String, String> headers,
                         final RetrofitParams retrofitParams,
                         final BaseService baseService,
                         final Class<T> dataClass,
                         final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                         final Action0 errorAction,
                         final Action0 completedAction,
                         final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().put(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T> void delete(String requestUrl,
                            HashMap<String, String> headers,
                            final RetrofitParams retrofitParams,
                            final BaseService baseService,
                            final Class<T> dataClass,
                            final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                            final Action0 errorAction,
                            final Action0 completedAction,
                            final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().delete(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T> void post(String requestUrl,
                          HashMap<String, String> headers,
                          final RetrofitParams retrofitParams,
                          final BaseService baseService,
                          final Class<T> dataClass,
                          final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                          final Action0 errorAction,
                          final Action0 completedAction,
                          final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().post(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T> void get(String requestUrl,
                         HashMap<String, String> headers,
                         final RetrofitParams retrofitParams,
                         final BaseService baseService,
                         final Class<T> dataClass,
                         final Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long> successAction,
                         final Action0 errorAction,
                         final Action0 completedAction,
                         final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().get(
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.isCallNCData(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, Boolean>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap, isLastCall, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService, completedAction);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
                        if (builder.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private void finallPrintLog(String apiRequestKey, String responseString) {
        try {
            if (!logmaps.containsKey(apiRequestKey)) {
                return;
            }
            StringBuffer buffer = logmaps.get(apiRequestKey);
            if (buffer == null) {
                return;
            }
            if (!TextUtils.isEmpty(responseString)) {
                int length = responseString.length();
                for (int i = 0; i < length; i += 90) {
                    int endIndex = i + 90;
                    if (endIndex >= length) {
                        buffer.append(String.format("%s\n", responseString.substring(i)));
                    } else {
                        buffer.append(String.format("%s\n", responseString.substring(i, endIndex)));
                    }
                }
            }
            buffer.append(String.format("%s\n", responseString));
            buffer.append("===============================================================\n");
            Logger.info(buffer.toString());
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            if (logmaps.containsKey(apiRequestKey)) {
                logmaps.remove(apiRequestKey);
            }
        }
    }

    private List<ByteRequestItem> getUploadByteItems(RetrofitParams retrofitParams) {
        List<ByteRequestItem> lst = new ArrayList<ByteRequestItem>();
        HashMap<String, Object> params = retrofitParams.getParams();
        if (ObjectJudge.isNullOrEmpty(params)) {
            return lst;
        }
        for (HashMap.Entry<String, Object> entry : params.entrySet()) {
            //参数名
            String key = entry.getKey();
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            //参数值
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if ((value instanceof byte[]) || (value instanceof Byte[])) {
                ByteRequestItem requestItem = new ByteRequestItem();
                requestItem.setFieldName(key);
                requestItem.setBs((byte[]) value);
                lst.add(requestItem);
            }
        }
        return lst;
    }

    private HashMap<String, Object> getUploadByteParams(RetrofitParams retrofitParams) {
        HashMap<String, Object> params2 = new HashMap<String, Object>();
        if (ObjectJudge.isNullOrEmpty(retrofitParams.getParams())) {
            return params2;
        }
        HashMap<String, Object> params = retrofitParams.getParams();
        for (HashMap.Entry<String, Object> entry : params.entrySet()) {
            //参数名
            String key = entry.getKey();
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            //参数值
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof Integer) {
                params2.put(key, value);
            } else if (value instanceof Long) {
                params2.put(key, value);
            } else if (value instanceof String) {
                params2.put(key, value);
            } else if (value instanceof Double) {
                params2.put(key, value);
            } else if (value instanceof Float) {
                params2.put(key, value);
            } else if (value instanceof Boolean) {
                params2.put(key, value);
            } else if (value instanceof List) {
                params2.put(key, JsonUtils.toStr(value));
            }
        }
        return params2;
    }

    private <S extends BaseService> void openLogin(S s, OnAuthCallInfoListener listener) {
        //请求token api请求中的token清空
        s.setToken("");
        if (listener != null) {
            listener.onCallInfo(s.getApiName());
        }
    }

    private <T, S extends BaseService> void finishedRequest(final BaseSubscriber<T, S> baseSubscriber) {
        if (ObjectJudge.isMainThread()) {
            baseSubscriber.onCompleted();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    baseSubscriber.onCompleted();
                }
            });
        }
    }

    protected <T, I, S extends BaseService> void requestObject(Class<I> apiClass,
                                                               S server,
                                                               final BaseSubscriber<T, S> baseSubscriber,
                                                               OkRxValidParam validParam,
                                                               Func2<String, S, String> urlAction,
                                                               Func2<RetrofitParams, I, HashMap<String, Object>> decApiAction,
                                                               OnAuthCallInfoListener listener,
                                                               HashMap<String, Object> params) {
        try {
            //若需要登录验证则打开登录页面
            if (validParam.isNeedLogin()) {
                openLogin(server, listener);
                return;
            }
            //验证失败结束请求(需要判断当前请求的接口是否在线程中请求)
            if (!validParam.isFlag()) {
                finishedRequest(baseSubscriber);
                return;
            }
            if (urlAction == null || server == null) {
                finishedRequest(baseSubscriber);
                return;
            }
            OkRxParsing parsing = new OkRxParsing();
            I decApi = parsing.createAPI(apiClass);
            if (decApiAction == null || decApi == null || validParam.getApiCheckAnnotation() == null) {
                finishedRequest(baseSubscriber);
                return;
            }
            RetrofitParams retrofitParams = decApiAction.call(decApi, params);
            retrofitParams.setCurrentRequestTime(validParam.getCurrentRequestTime());
            if (retrofitParams == null || !retrofitParams.getFlag()) {
                finishedRequest(baseSubscriber);
                return;
            }
            //若api类未指定base url类型名称则不作请求处理
            if (retrofitParams.getIsJoinUrl() && retrofitParams.getUrlTypeName() == null) {
                finishedRequest(baseSubscriber);
                return;
            }
            if (retrofitParams.getIsJoinUrl() && TextUtils.isEmpty(retrofitParams.getUrlTypeName().value())) {
                finishedRequest(baseSubscriber);
                return;
            }
            ThreadPoolUtils.getInstance().singleTaskExecute(new ApiRequestRunnable<T, I, S>(apiClass, server, baseSubscriber, validParam, retrofitParams, urlAction, listener));
        } catch (Exception e) {
            finishedRequest(baseSubscriber);
        }
    }

    private class ApiRequestRunnable<T, I, S extends BaseService> implements Runnable {

        private Class<I> apiClass;
        private S server;
        private BaseSubscriber<T, S> baseSubscriber;
        private OkRxValidParam validParam;
        private RetrofitParams retrofitParams;
        private Func2<String, S, String> urlAction;
        private OnAuthCallInfoListener listener;

        public ApiRequestRunnable(Class<I> apiClass,
                                  S server,
                                  final BaseSubscriber<T, S> baseSubscriber,
                                  OkRxValidParam validParam,
                                  RetrofitParams retrofitParams,
                                  Func2<String, S, String> urlAction,
                                  OnAuthCallInfoListener listener) {
            this.apiClass = apiClass;
            this.server = server;
            this.baseSubscriber = baseSubscriber;
            this.validParam = validParam;
            this.retrofitParams = retrofitParams;
            this.urlAction = urlAction;
            this.listener = listener;
        }

        @Override
        public void run() {
            apiRequest(apiClass, server, baseSubscriber, validParam, retrofitParams, urlAction, listener);
        }
    }

    private <T, I, S extends BaseService> void apiRequest(Class<I> apiClass,
                                                          S server,
                                                          final BaseSubscriber<T, S> baseSubscriber,
                                                          OkRxValidParam validParam,
                                                          RetrofitParams retrofitParams,
                                                          Func2<String, S, String> urlAction,
                                                          OnAuthCallInfoListener listener) {
        //设置回调是否作验证
        baseSubscriber.setValidCallResult(retrofitParams.isValidCallResult());
        //设置此接口允许返回码
        if (!ObjectJudge.isNullOrEmpty(retrofitParams.getAllowRetCodes())) {
            List<String> allowRetCodes = baseSubscriber.getAllowRetCodes();
            allowRetCodes.addAll(retrofitParams.getAllowRetCodes());
        }
        //设置请求地址
        if (retrofitParams.getUrlTypeName() != null) {
            if (retrofitParams.getIsJoinUrl()) {
                String baseUrl = urlAction.call(server, retrofitParams.getUrlTypeName().value());
                retrofitParams.setRequestUrl(PathsUtils.combine(baseUrl, retrofitParams.getRequestUrl()));
                if (retrofitParams.isLastContainsPath() && !retrofitParams.getRequestUrl().endsWith("/")) {
                    retrofitParams.setRequestUrl(retrofitParams.getRequestUrl() + "/");
                }
            }
            //设置token名字
            retrofitParams.setTokenName(retrofitParams.getUrlTypeName().tokenName());
        }
        //NO_CACHE: 不使用缓存,该模式下,cacheKey,cacheTime 参数均无效
        //DEFAULT: 按照HTTP协议的默认缓存规则，例如有304响应头时缓存。
        //REQUEST_FAILED_READ_CACHE：先请求网络，如果请求网络失败，则读取缓存，如果读取缓存失败，本次请求失败。
        //IF_NONE_CACHE_REQUEST：如果缓存不存在才请求网络，否则使用缓存。
        //FIRST_CACHE_THEN_REQUEST：先使用缓存，不管是否存在，仍然请求网络。
        //缓存的过期时间,单位毫秒
        //为确保未设置缓存请求几乎不做缓存，此处默认缓存时间暂设为5秒
        ApiCheckAnnotation apiCheckAnnotation = validParam.getApiCheckAnnotation();
        retrofitParams.setCache(apiCheckAnnotation.isCache());
        retrofitParams.setCacheKey(apiCheckAnnotation.cacheKey());
        if (retrofitParams.isCache()) {
            long milliseconds = ConvertUtils.toMilliseconds(apiCheckAnnotation.cacheTime(), apiCheckAnnotation.cacheTimeUnit());
            retrofitParams.setCacheTime(milliseconds);
        } else {
            retrofitParams.setCacheTime(5000);
        }
        retrofitParams.setCallNCData(apiCheckAnnotation.isCallNCData());
        //拼接完整的url
        //del请求看delQuery参数是不是为空
        if (!ObjectJudge.isNullOrEmpty(retrofitParams.getDelQueryParams())) {
            StringBuffer querysb = new StringBuffer();
            for (Map.Entry<String, String> entry : retrofitParams.getDelQueryParams().entrySet()) {
                querysb.append(MessageFormat.format("{0}={1},", entry.getKey(), entry.getValue()));
            }
            if (querysb.length() > 0) {
                if (retrofitParams.getRequestUrl().indexOf("?") < 0) {
                    retrofitParams.setRequestUrl(String.format("%s?%s",
                            retrofitParams.getRequestUrl(),
                            querysb.substring(0, querysb.length() - 1)));
                } else {
                    retrofitParams.setRequestUrl(String.format("%s&%s",
                            retrofitParams.getRequestUrl(),
                            querysb.substring(0, querysb.length() - 1)));
                }
            }
        }
        baseSubscriber.setOnAuthCallInfoListener(listener);
        String apiRequestKey = GlobalUtils.getNewGuid();
        server.<T>baseConfig(server, retrofitParams, validParam,
                new Action6<T, String, HashMap<String, ReqQueueItem>, Boolean, Long, Long>() {
                    @Override
                    public void call(T t, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, Boolean isLastCall, Long requestStartTime, Long requestTotalTime) {
                        //成功回调
                        baseSubscriber.onNext(t, reqQueueItemHashMap, apiRequestKey, isLastCall, requestStartTime, requestTotalTime);
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        OnSuccessfulListener successfulListener = baseSubscriber.getOnSuccessfulListener();
                        if (successfulListener != null) {
                            successfulListener.onError(null, baseSubscriber.getExtra());
                        }
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        OnSuccessfulListener successfulListener = baseSubscriber.getOnSuccessfulListener();
                        if (successfulListener == null) {
                            return;
                        }
                        successfulListener.onCompleted(baseSubscriber.getExtra());
                    }
                }, apiRequestKey);

        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        if (builder.isDebug()) {
            printLog(apiRequestKey, retrofitParams);
        }
    }

    private void printLog(String apiRequestKey, RetrofitParams retrofitParams) {
        try {
            if (retrofitParams == null) {
                return;
            }
            if (retrofitParams.isPrintApiLog()) {
                StringBuffer logsb = new StringBuffer();
                logsb.append("===============================================================\n");
                logsb.append(String.format("接口名:%s\n", retrofitParams.getApiName()));
                logsb.append(String.format("请求类型:%s\n", retrofitParams.getRequestType().name()));
                logsb.append(String.format("请求token:%s=%s\n", retrofitParams.getTokenName(), token));
                logsb.append(String.format("请求地址:%s\n", retrofitParams.getRequestUrl()));
                logsb.append(String.format("Header信息:%s\n", JsonUtils.toStr(retrofitParams.getHeadParams())));
                if (retrofitParams.getRequestType() == RequestType.DELETE) {
                    logsb.append(String.format("请求参数:%s\n", JsonUtils.toStr(retrofitParams.getDelQueryParams())));
                } else {
                    logsb.append(String.format("请求参数:%s\n", JsonUtils.toStr(retrofitParams.getParams())));
                }
                logsb.append(String.format("数据提交方式:%s\n", retrofitParams.getRequestContentType().name()));
                logsb.append(String.format("缓存信息:isCache=%s,cacheKey=%s,cacheTime=%s\n",
                        retrofitParams.isCache(),
                        retrofitParams.getCacheKey(),
                        retrofitParams.getCacheTime()));
                logsb.append(String.format("返回数据类名:%s\n", retrofitParams.getDataClass().getSimpleName()));
                logsb.append(String.format("验证是否通过:%s\n", retrofitParams.getFlag()));
                logsb.append(String.format("允许接口返回码:%s\n", retrofitParams.getAllowRetCodes()));
                logmaps.put(apiRequestKey, logsb);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
