package com.cloud.nets;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cloud.cache.RxStacks;
import com.cloud.nets.annotations.ApiCheckAnnotation;
import com.cloud.nets.annotations.ApiHeadersCall;
import com.cloud.nets.annotations.ReturnCodeFilter;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.events.OnApiRetCodesFilterListener;
import com.cloud.nets.events.OnAuthListener;
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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


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

    protected void onRequestCompleted() {
        //请求完成(结束)
    }

    protected void onRequestError() {
        //请求错误
    }

    protected void baseConfig(final BaseService baseService,
                              final RetrofitParams retrofitParams,
                              OkRxValidParam validParam,
                              final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                              Action1<ErrorType> errorAction,
                              Action0 completedAction,
                              String apiRequestKey) {
        final Class dataClass = retrofitParams.getDataClass();
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
                //记录当前线程调用的堆栈信息
                RxStacks.setStack(validParam.getInvokeMethodName(), new Exception());
                retrofitParams.setInvokeMethodName(validParam.getInvokeMethodName());
                //请求api
                reqQueueItemHashMap.put(apiRequestKey, new ReqQueueItem());
                if (retrofitParams.getRequestType() == RequestType.BYTES) {
                    HashMap<String, Object> updateByteParams = getUploadByteParams(retrofitParams);
                    List<ByteRequestItem> uploadByteItems = getUploadByteItems(retrofitParams);
                    subBytes(requestUrl, mHeaders, updateByteParams, retrofitParams, uploadByteItems, baseService, dataClass, successAction, errorAction, completedAction, apiRequestKey);
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
                    } else if (retrofitParams.getRequestType() == RequestType.OPTIONS) {
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

    private void successDealWith(Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                                 final Action1<ErrorType> errorAction,
                                 Class dataClass,
                                 boolean isCollectionDataType,
                                 BaseService baseService,
                                 String response,
                                 String apiRequestKey,
                                 HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                                 DataType dataType,
                                 long requestStartTime,
                                 long requestTotalTime,
                                 final Action0 completedAction) {
        if (successAction == null) {
            finishedRequest(baseService, completedAction);
        } else {
            boolean isBasicData = false;
            Object data = null;
            //如果dataClass为基础数据类型则不进行解决
            if (dataClass == String.class ||
                    dataClass == Integer.class ||
                    dataClass == Double.class ||
                    dataClass == Float.class ||
                    dataClass == Long.class) {
                isBasicData = true;
                data = response;
            } else {
                OnBeanParsingJsonListener jsonListener = OkRx.getInstance().getOnBeanParsingJsonListener();
                if (jsonListener == null) {
                    if (isCollectionDataType) {
                        data = JsonUtils.parseArray(response, dataClass);
                    } else {
                        data = JsonUtils.parseT(response, dataClass);
                    }
                } else {
                    data = jsonListener.onBeanParsingJson(response, dataClass, isCollectionDataType);
                }
            }
            //如果空则回调错误
            //如果从缓存过来的且对象为空则不处理
            if (data == null) {
                if (dataType == DataType.CacheData) {
                    return;
                }
                sendErrorAction(errorAction, baseService, ErrorType.businessProcess);
                return;
            }
            //如果是集合则取消拦截
            if (isCollectionDataType || isBasicData) {
                //成功回调
                successAction.call(data, apiRequestKey, reqQueueItemHashMap, dataType, requestStartTime, requestTotalTime);
            } else {
                //开启拦截且拦截符合的返回码
                OkRxConfigParams okRxConfigParams = OkRx.getInstance().getOkRxConfigParams();
                if (okRxConfigParams.isNetStatusCodeIntercept()) {
                    if (!filterMatchRetCodes(data)) {
                        //成功回调
                        successAction.call(data, apiRequestKey, reqQueueItemHashMap, dataType, requestStartTime, requestTotalTime);
                    }
                } else {
                    //成功回调
                    successAction.call(data, apiRequestKey, reqQueueItemHashMap, dataType, requestStartTime, requestTotalTime);
                }
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

    private void subBytes(String requestUrl,
                          HashMap<String, String> httpHeaders,
                          HashMap<String, Object> httpParams,
                          final RetrofitParams retrofitParams,
                          List<ByteRequestItem> byteRequestItems,
                          final BaseService baseService,
                          final Class dataClass,
                          final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                          final Action1<ErrorType> errorAction,
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
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, DataType.NetData, 0, 0, completedAction);
                    }
                },
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    private void sendErrorAction(final Action1<ErrorType> errorAction, final BaseService baseService, final ErrorType errorType) {
        if (ObjectJudge.isMainThread()) {
            if (errorAction != null) {
                errorAction.call(errorType);
            }
            baseService.onRequestError();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    if (errorAction != null) {
                        errorAction.call(errorType);
                    }
                    baseService.onRequestError();
                }
            });
        }
    }

    private void trace(String requestUrl,
                       HashMap<String, String> headers,
                       final RetrofitParams retrofitParams,
                       final BaseService baseService,
                       final Class dataClass,
                       final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                       final Action1<ErrorType> errorAction,
                       final Action0 completedAction,
                       final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().trace(
                requestUrl,
                headers,
                retrofitParams,
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    private void options(String requestUrl,
                         HashMap<String, String> headers,
                         final RetrofitParams retrofitParams,
                         final BaseService baseService,
                         final Class dataClass,
                         final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                         final Action1<ErrorType> errorAction,
                         final Action0 completedAction,
                         final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().options(
                requestUrl,
                headers,
                retrofitParams,
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    private void head(String requestUrl,
                      HashMap<String, String> headers,
                      final RetrofitParams retrofitParams,
                      final BaseService baseService,
                      final Class dataClass,
                      final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                      final Action1<ErrorType> errorAction,
                      final Action0 completedAction,
                      final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().head(
                requestUrl,
                headers,
                retrofitParams,
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    private void patch(String requestUrl,
                       HashMap<String, String> headers,
                       final RetrofitParams retrofitParams,
                       final BaseService baseService,
                       final Class dataClass,
                       final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                       final Action1<ErrorType> errorAction,
                       final Action0 completedAction,
                       final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().patch(
                requestUrl,
                headers,
                retrofitParams,
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    private void put(String requestUrl,
                     HashMap<String, String> headers,
                     final RetrofitParams retrofitParams,
                     final BaseService baseService,
                     final Class dataClass,
                     final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                     final Action1<ErrorType> errorAction,
                     final Action0 completedAction,
                     final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().put(
                requestUrl,
                headers,
                retrofitParams,
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    private void delete(String requestUrl,
                        HashMap<String, String> headers,
                        final RetrofitParams retrofitParams,
                        final BaseService baseService,
                        final Class dataClass,
                        final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                        final Action1<ErrorType> errorAction,
                        final Action0 completedAction,
                        final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().delete(
                requestUrl,
                headers,
                retrofitParams,
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    private void post(String requestUrl,
                      HashMap<String, String> headers,
                      final RetrofitParams retrofitParams,
                      final BaseService baseService,
                      final Class dataClass,
                      final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                      final Action1 errorAction,
                      final Action0 completedAction,
                      final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().post(
                requestUrl,
                headers,
                retrofitParams,
                retrofitParams.getRequestContentType(),
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem != null && queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
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

    private void get(String requestUrl,
                     HashMap<String, String> headers,
                     final RetrofitParams retrofitParams,
                     final BaseService baseService,
                     final Class dataClass,
                     final Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long> successAction,
                     final Action1<ErrorType> errorAction,
                     final Action0 completedAction,
                     final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().get(
                requestUrl,
                headers,
                retrofitParams,
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        successDealWith(successAction, errorAction, dataClass, retrofitParams.isCollectionDataType(), baseService, response, apiRequestKey, reqQueueItemHashMap, dataType, retrofitParams.getCurrentRequestTime(), retrofitParams.getRequestTotalTime(), completedAction);
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
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (requestState == RequestState.Error) {
                            sendErrorAction(errorAction, baseService, errorType);
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

    protected <I, S extends BaseService> void requestObject(Class<I> apiClass,
                                                            S server,
                                                            final BaseSubscriber<Object, S> baseSubscriber,
                                                            OkRxValidParam validParam,
                                                            Func2<String, S, Integer> urlAction,
                                                            Func2<RetrofitParams, I, HashMap<String, Object>> decApiAction,
                                                            HashMap<String, Object> params) {
        try {
            //若需要登录验证则打开登录页面
            if (validParam.isNeedLogin()) {
                OnAuthListener authListener = OkRx.getInstance().getOnAuthListener();
                if (authListener != null) {
                    authListener.onLoginCall(validParam.getInvokeMethodName());
                }
                finishedRequest(baseSubscriber);
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
            if (!retrofitParams.getFlag()) {
                finishedRequest(baseSubscriber);
                return;
            }
            //若api类未指定base url类型名称则不作请求处理
            if (retrofitParams.getIsJoinUrl() && retrofitParams.getUrlTypeName() == null) {
                finishedRequest(baseSubscriber);
                return;
            }
            if (retrofitParams.getIsJoinUrl() && retrofitParams.getUrlTypeName().value() == 0) {
                finishedRequest(baseSubscriber);
                return;
            }
            ScheduledThreadPoolExecutor executor = ThreadPoolUtils.getInstance().getMultiTaskExecutor();
            ApiRequestRunnable<I, S> runnable = new ApiRequestRunnable<>(apiClass, server, baseSubscriber, validParam, retrofitParams, urlAction, new Exception());
            executor.schedule(runnable, 0, TimeUnit.SECONDS);
        } catch (Exception e) {
            finishedRequest(baseSubscriber);
        }
    }

    private class ApiRequestRunnable<I, S extends BaseService> implements Runnable {

        private Class<I> apiClass;
        private S server;
        private BaseSubscriber<Object, S> baseSubscriber;
        private OkRxValidParam validParam;
        private RetrofitParams retrofitParams;
        private Func2<String, S, Integer> urlAction;
        private Exception exception = null;

        public ApiRequestRunnable(Class<I> apiClass,
                                  S server,
                                  final BaseSubscriber<Object, S> baseSubscriber,
                                  OkRxValidParam validParam,
                                  RetrofitParams retrofitParams,
                                  Func2<String, S, Integer> urlAction,
                                  Exception exception) {
            this.apiClass = apiClass;
            this.server = server;
            this.baseSubscriber = baseSubscriber;
            this.validParam = validParam;
            this.retrofitParams = retrofitParams;
            this.urlAction = urlAction;
            this.exception = exception;
        }

        @Override
        public void run() {
            //记录之前main线程堆栈信息
            RxStacks.setStack(validParam.getInvokeMethodName(), exception);
            apiRequest(apiClass, server, baseSubscriber, validParam, retrofitParams, urlAction);
        }
    }

    private <I, S extends BaseService> void apiRequest(Class<I> apiClass,
                                                       S server,
                                                       final BaseSubscriber<Object, S> baseSubscriber,
                                                       OkRxValidParam validParam,
                                                       RetrofitParams retrofitParams,
                                                       Func2<String, S, Integer> urlAction) {
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
        retrofitParams.setCallStatus(apiCheckAnnotation.callStatus());
        retrofitParams.setCacheKey(apiCheckAnnotation.cacheKey());
        retrofitParams.setIntervalCacheTime(apiCheckAnnotation.cacheIntervalTime());
        //设置缓存时间
        CallStatus status = retrofitParams.getCallStatus();
        if (status != CallStatus.OnlyNet) {
            long milliseconds = ConvertUtils.toMilliseconds(apiCheckAnnotation.cacheTime(), apiCheckAnnotation.cacheTimeUnit());
            retrofitParams.setCacheTime(milliseconds);
        }
        //拼接完整的url
        //del请求看delQuery参数是不是为空
        if (!ObjectJudge.isNullOrEmpty(retrofitParams.getDelQueryParams())) {
            StringBuffer querysb = new StringBuffer();
            for (Map.Entry<String, String> entry : retrofitParams.getDelQueryParams().entrySet()) {
                querysb.append(MessageFormat.format("{0}={1},", entry.getKey(), entry.getValue()));
            }
            if (querysb.length() > 0) {
                if (!retrofitParams.getRequestUrl().contains("?")) {
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
        String apiRequestKey = GlobalUtils.getNewGuid();
        server.baseConfig(server, retrofitParams, validParam,
                new Action6<Object, String, HashMap<String, ReqQueueItem>, DataType, Long, Long>() {
                    @Override
                    public void call(Object t, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType, Long requestStartTime, Long requestTotalTime) {
                        //成功回调
                        baseSubscriber.onNext(t, reqQueueItemHashMap, apiRequestKey, dataType, requestStartTime, requestTotalTime);
                    }
                },
                new Action1<ErrorType>() {
                    @Override
                    public void call(ErrorType errorType) {
                        //失败回调
                        OnSuccessfulListener successfulListener = baseSubscriber.getOnSuccessfulListener();
                        if (successfulListener != null) {
                            successfulListener.onError(null, errorType, baseSubscriber.getExtra());
                            successfulListener.onError(errorType, baseSubscriber.getExtra());
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
                },
                apiRequestKey);

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
                logsb.append(String.format("缓存信息:callStatus=%s,cacheKey=%s,cacheTime=%s\n",
                        retrofitParams.getCallStatus().name(),
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
