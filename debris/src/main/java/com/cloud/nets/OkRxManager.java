package com.cloud.nets;

import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.properties.ByteRequestItem;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.nets.requests.OkRxDeleteRequest;
import com.cloud.nets.requests.OkRxDownloadFileRequest;
import com.cloud.nets.requests.OkRxGetRequest;
import com.cloud.nets.requests.OkRxHeadRequest;
import com.cloud.nets.requests.OkRxOptionsRequest;
import com.cloud.nets.requests.OkRxPatchRequest;
import com.cloud.nets.requests.OkRxPostRequest;
import com.cloud.nets.requests.OkRxPutRequest;
import com.cloud.nets.requests.OkRxTraceRequest;
import com.cloud.nets.requests.OkRxUploadByteRequest;
import com.cloud.objects.enums.RequestContentType;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action3;
import com.cloud.objects.events.Action4;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/15
 * Description:网络请求类
 * Modifier:
 * ModifyContent:
 */
public class OkRxManager {

    private static OkRxManager okRxManager = null;

    public static OkRxManager getInstance() {
        return okRxManager == null ? okRxManager = new OkRxManager() : okRxManager;
    }

    public void get(String url,
                    HashMap<String, String> headers,
                    RetrofitParams retrofitParams,
                    Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                    String apiUnique,
                    Action2<String, HashMap<String, String>> headersAction,
                    Action2<RequestState,ErrorType> completeAction,
                    Action2<String, String> printLogAction,
                    String apiRequestKey,
                    HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxGetRequest request = new OkRxGetRequest();
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void post(String url,
                     HashMap<String, String> headers,
                     RetrofitParams retrofitParams,
                     RequestContentType requestContentType,
                     Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction,
                     Action2<RequestState,ErrorType> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPostRequest request = new OkRxPostRequest(requestContentType);
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void delete(String url,
                       HashMap<String, String> headers,
                       RetrofitParams retrofitParams,
                       RequestContentType requestContentType,
                       Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                       String apiUnique,
                       Action2<String, HashMap<String, String>> headersAction,
                       Action2<RequestState,ErrorType> completeAction,
                       Action2<String, String> printLogAction,
                       String apiRequestKey,
                       HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxDeleteRequest request = new OkRxDeleteRequest(requestContentType);
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void put(String url,
                    HashMap<String, String> headers,
                    RetrofitParams retrofitParams,
                    RequestContentType requestContentType,
                    Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                    String apiUnique,
                    Action2<String, HashMap<String, String>> headersAction,
                    Action2<RequestState,ErrorType> completeAction,
                    Action2<String, String> printLogAction,
                    String apiRequestKey,
                    HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPutRequest request = new OkRxPutRequest(requestContentType);
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void patch(String url,
                      HashMap<String, String> headers,
                      RetrofitParams retrofitParams,
                      RequestContentType requestContentType,
                      Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                      String apiUnique,
                      Action2<String, HashMap<String, String>> headersAction,
                      Action2<RequestState,ErrorType> completeAction,
                      Action2<String, String> printLogAction,
                      String apiRequestKey,
                      HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPatchRequest request = new OkRxPatchRequest(requestContentType);
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void head(String url,
                     HashMap<String, String> headers,
                     RetrofitParams retrofitParams,
                     Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction,
                     Action2<RequestState,ErrorType> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxHeadRequest request = new OkRxHeadRequest();
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void options(String url,
                        HashMap<String, String> headers,
                        RetrofitParams retrofitParams,
                        RequestContentType requestContentType,
                        Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                        String apiUnique,
                        Action2<String, HashMap<String, String>> headersAction,
                        Action2<RequestState,ErrorType> completeAction,
                        Action2<String, String> printLogAction,
                        String apiRequestKey,
                        HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxOptionsRequest request = new OkRxOptionsRequest(requestContentType);
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void trace(String url,
                      HashMap<String, String> headers,
                      RetrofitParams retrofitParams,
                      RequestContentType requestContentType,
                      Action4<String, String, HashMap<String, ReqQueueItem>, DataType> successAction,
                      String apiUnique,
                      Action2<String, HashMap<String, String>> headersAction,
                      Action2<RequestState, ErrorType> completeAction,
                      Action2<String, String> printLogAction,
                      String apiRequestKey,
                      final HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxTraceRequest request = new OkRxTraceRequest(requestContentType);
        request.setRetrofitParams(retrofitParams);
        request.call(url, headers, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void download(String url,
                         HashMap<String, String> headers,
                         TreeMap<String, Object> params,
                         File downFile,
                         Action1<Float> progressAction,
                         Action1<File> successAction,
                         Action1<RequestState> completeAction,
                         String apiRequestKey,
                         HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxDownloadFileRequest request = new OkRxDownloadFileRequest();
        request.call(url, headers, params, downFile, progressAction, successAction, completeAction, apiRequestKey, reqQueueItemHashMap);
    }

    public void uploadBytes(String url,
                            HashMap<String, String> httpHeaders,
                            HashMap<String, Object> httpParams,
                            List<ByteRequestItem> byteRequestItems,
                            Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                            Action2<RequestState,ErrorType> completeAction,
                            Action2<String, String> printLogAction,
                            String apiRequestKey,
                            HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxUploadByteRequest request = new OkRxUploadByteRequest();
        request.call(url, httpHeaders, httpParams, byteRequestItems, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap);
    }

    public void uploadByte(String url,
                           HashMap<String, String> httpHeaders,
                           HashMap<String, Object> httpParams,
                           ByteRequestItem byteRequestItem,
                           Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                           Action2<RequestState,ErrorType> completeAction,
                           Action2<String, String> printLogAction,
                           String apiRequestKey,
                           HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        List<ByteRequestItem> byteRequestItems = new ArrayList<ByteRequestItem>();
        byteRequestItems.add(byteRequestItem);
        uploadBytes(url, httpHeaders, httpParams, byteRequestItems, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap);
    }
}
