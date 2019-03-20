package com.cloud.mixed.h5;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.mixed.h5.beans.APIReturnResult;
import com.cloud.mixed.h5.beans.ArgFieldItem;
import com.cloud.mixed.h5.beans.H5GetAPIMethodArgsBean;
import com.cloud.mixed.h5.enums.APIRequestState;
import com.cloud.nets.OkRxManager;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RequestContentType;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action4;
import com.cloud.objects.events.Func2;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.PathsUtils;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/1/4
 * Description:H5交互API工具类
 * Modifier:
 * ModifyContent:
 */
public class H5InteractionAPIUtils {

    /**
     * H5请求api接口方法
     *
     * @param context
     * @param baseUrl 请求api的基地址
     * @param extras  {"apiName":"login",
     *                "target":"返回时带回去",
     *                "reqType":"put/patch/get/post/delete",
     *                "args":[{"fieldName":"userName","fieldValue":"test"},
     *                {"fieldName":"password","fieldValue":"123456"}]}
     * @param headers 请求头
     */
    public static void getAPIMethod(Context context,
                                    String baseUrl,
                                    String extras,
                                    HashMap<String, String> headers,
                                    RequestContentType requestContentType,
                                    final Func2<Object, APIRequestState, APIReturnResult> callback) {
        try {
            if (context == null || TextUtils.isEmpty(extras)) {
                return;
            }
            final H5GetAPIMethodArgsBean h5GetAPIMethodArgsBean = JsonUtils.parseT(extras, H5GetAPIMethodArgsBean.class);
            if (h5GetAPIMethodArgsBean == null || TextUtils.isEmpty(h5GetAPIMethodArgsBean.getApiName())) {
                return;
            }
            String url = PathsUtils.combine(baseUrl, h5GetAPIMethodArgsBean.getApiName());
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!ObjectJudge.isNullOrEmpty(h5GetAPIMethodArgsBean.getArgs())) {
                for (ArgFieldItem argFieldItem : h5GetAPIMethodArgsBean.getArgs()) {
                    params.put(argFieldItem.getFieldName(), argFieldItem.getFieldValue());
                }
            }
            if (TextUtils.isEmpty(h5GetAPIMethodArgsBean.getReqType())) {
                return;
            }
            String target = h5GetAPIMethodArgsBean.getTarget();
            switch (h5GetAPIMethodArgsBean.getReqType().trim().toLowerCase()) {
                case "get":
                    getRequest(context, url, headers, params, target, callback);
                    break;
                case "post":
                    postRequest(context, url, headers, params, target, requestContentType, callback);
                    break;
                case "put":
                    putRequest(context, url, headers, params, target, requestContentType, callback);
                    break;
                case "patch":
                    patchRequest(context, url, headers, params, target, requestContentType, callback);
                    break;
                case "delete":
                    deleteRequest(context, url, headers, params, target, requestContentType, callback);
                    break;
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static void deleteRequest(Context context,
                                      String url,
                                      HashMap<String, String> headers,
                                      HashMap<String, Object> params,
                                      final String target,
                                      RequestContentType requestContentType,
                                      final Func2<Object, APIRequestState, APIReturnResult> callback) {
        RetrofitParams retrofitParams = new RetrofitParams();
        TreeMap<String, Object> requestParams = retrofitParams.getParams();
        requestParams.putAll(params);
        retrofitParams.setCallStatus(CallStatus.OnlyNet);
        retrofitParams.setRequestContentType(requestContentType);
        OkRxManager.getInstance().delete(url,
                headers,
                retrofitParams,
                requestContentType,
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        if (callback != null) {
                            APIReturnResult apiReturnResult = new APIReturnResult();
                            apiReturnResult.setResponse(response);
                            apiReturnResult.setTarget(target);
                            callback.call(APIRequestState.Success, apiReturnResult);
                        }
                    }
                },
                "",
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (callback != null && requestState == RequestState.Completed) {
                            callback.call(APIRequestState.Complate, null);
                        }
                    }
                },
                null,
                "", null);
    }

    private static void patchRequest(Context context,
                                     String url,
                                     HashMap<String, String> headers,
                                     HashMap<String, Object> params,
                                     final String target,
                                     RequestContentType requestContentType,
                                     final Func2<Object, APIRequestState, APIReturnResult> callback) {
        RetrofitParams retrofitParams = new RetrofitParams();
        TreeMap<String, Object> requestParams = retrofitParams.getParams();
        requestParams.putAll(params);
        retrofitParams.setCallStatus(CallStatus.OnlyNet);
        OkRxManager.getInstance().patch(url,
                headers,
                retrofitParams,
                requestContentType,
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        if (callback != null) {
                            APIReturnResult apiReturnResult = new APIReturnResult();
                            apiReturnResult.setResponse(response);
                            apiReturnResult.setTarget(target);
                            callback.call(APIRequestState.Success, apiReturnResult);
                        }
                    }
                }, "",
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (callback != null && requestState == RequestState.Completed) {
                            callback.call(APIRequestState.Complate, null);
                        }
                    }
                },
                null,
                "", null);
    }

    private static void putRequest(Context context,
                                   String url,
                                   HashMap<String, String> headers,
                                   HashMap<String, Object> params,
                                   final String target,
                                   RequestContentType requestContentType,
                                   final Func2<Object, APIRequestState, APIReturnResult> callback) {
        RetrofitParams retrofitParams = new RetrofitParams();
        TreeMap<String, Object> requestParams = retrofitParams.getParams();
        requestParams.putAll(params);
        retrofitParams.setCallStatus(CallStatus.OnlyNet);
        OkRxManager.getInstance().put(url,
                headers,
                retrofitParams,
                requestContentType,
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        if (callback != null) {
                            APIReturnResult apiReturnResult = new APIReturnResult();
                            apiReturnResult.setResponse(response);
                            apiReturnResult.setTarget(target);
                            callback.call(APIRequestState.Success, apiReturnResult);
                        }
                    }
                },
                "",
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (callback != null && requestState == RequestState.Completed) {
                            callback.call(APIRequestState.Complate, null);
                        }
                    }
                },
                null,
                "",
                null);
    }

    private static void postRequest(Context context,
                                    String url,
                                    HashMap<String, String> headers,
                                    HashMap<String, Object> params,
                                    final String target,
                                    RequestContentType requestContentType,
                                    final Func2<Object, APIRequestState, APIReturnResult> callback) {
        RetrofitParams retrofitParams = new RetrofitParams();
        TreeMap<String, Object> requestParams = retrofitParams.getParams();
        requestParams.putAll(params);
        retrofitParams.setCallStatus(CallStatus.OnlyNet);
        OkRxManager.getInstance().post(url,
                headers,
                retrofitParams,
                requestContentType,
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        if (callback != null) {
                            APIReturnResult apiReturnResult = new APIReturnResult();
                            apiReturnResult.setResponse(response);
                            apiReturnResult.setTarget(target);
                            callback.call(APIRequestState.Success, apiReturnResult);
                        }
                    }
                },
                "",
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (callback != null && requestState == RequestState.Completed) {
                            callback.call(APIRequestState.Complate, null);
                        }
                    }
                },
                null,
                "",
                null);
    }

    private static void getRequest(Context context,
                                   String url,
                                   HashMap<String, String> headers,
                                   HashMap<String, Object> params,
                                   final String target,
                                   final Func2<Object, APIRequestState, APIReturnResult> callback) {
        RetrofitParams retrofitParams = new RetrofitParams();
        TreeMap<String, Object> requestParams = retrofitParams.getParams();
        requestParams.putAll(params);
        retrofitParams.setCallStatus(CallStatus.OnlyNet);
        OkRxManager.getInstance().get(url,
                headers,
                retrofitParams,
                new Action4<String, String, HashMap<String, ReqQueueItem>, DataType>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, DataType dataType) {
                        if (callback != null) {
                            APIReturnResult apiReturnResult = new APIReturnResult();
                            apiReturnResult.setResponse(response);
                            apiReturnResult.setTarget(target);
                            callback.call(APIRequestState.Success, apiReturnResult);
                        }
                    }
                },
                "",
                new Action2<RequestState, ErrorType>() {
                    @Override
                    public void call(RequestState requestState, ErrorType errorType) {
                        if (callback != null && requestState == RequestState.Completed) {
                            callback.call(APIRequestState.Complate, null);
                        }
                    }
                }, null, "", null);
    }
}
