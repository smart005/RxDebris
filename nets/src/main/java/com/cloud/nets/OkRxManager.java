package com.cloud.nets;

import android.graphics.Bitmap;

import com.cloud.nets.properties.ByteRequestItem;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.nets.requests.OkRxBitmapRequest;
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
                    HashMap<String, Object> params,
                    boolean isCache,
                    String cacheKey,
                    long cacheTime,
                    boolean isCallNCData,
                    Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                    String apiUnique,
                    Action2<String, HashMap<String, String>> headersAction,
                    Action1<RequestState> completeAction,
                    Action2<String, String> printLogAction,
                    String apiRequestKey,
                    HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxGetRequest request = new OkRxGetRequest();
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void post(String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     boolean isCache,
                     String cacheKey,
                     long cacheTime,
                     boolean isCallNCData,
                     RequestContentType requestContentType,
                     Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction,
                     Action1<RequestState> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPostRequest request = new OkRxPostRequest(requestContentType);
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void delete(String url,
                       HashMap<String, String> headers,
                       HashMap<String, Object> params,
                       boolean isCache,
                       String cacheKey,
                       long cacheTime,
                       boolean isCallNCData,
                       RequestContentType requestContentType,
                       Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                       String apiUnique,
                       Action2<String, HashMap<String, String>> headersAction,
                       Action1<RequestState> completeAction,
                       Action2<String, String> printLogAction,
                       String apiRequestKey,
                       HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxDeleteRequest request = new OkRxDeleteRequest(requestContentType);
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void put(String url,
                    HashMap<String, String> headers,
                    HashMap<String, Object> params,
                    boolean isCache,
                    String cacheKey,
                    long cacheTime,
                    boolean isCallNCData,
                    RequestContentType requestContentType,
                    Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                    String apiUnique,
                    Action2<String, HashMap<String, String>> headersAction,
                    Action1<RequestState> completeAction,
                    Action2<String, String> printLogAction,
                    String apiRequestKey,
                    HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPutRequest request = new OkRxPutRequest(requestContentType);
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void patch(String url,
                      HashMap<String, String> headers,
                      HashMap<String, Object> params,
                      boolean isCache,
                      String cacheKey,
                      long cacheTime,
                      boolean isCallNCData,
                      RequestContentType requestContentType,
                      Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                      String apiUnique,
                      Action2<String, HashMap<String, String>> headersAction,
                      Action1<RequestState> completeAction,
                      Action2<String, String> printLogAction,
                      String apiRequestKey,
                      HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPatchRequest request = new OkRxPatchRequest(requestContentType);
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void head(String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     boolean isCache,
                     String cacheKey,
                     long cacheTime,
                     boolean isCallNCData,
                     Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction,
                     Action1<RequestState> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxHeadRequest request = new OkRxHeadRequest();
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void options(String url,
                        HashMap<String, String> headers,
                        HashMap<String, Object> params,
                        boolean isCache,
                        String cacheKey,
                        long cacheTime,
                        boolean isCallNCData,
                        RequestContentType requestContentType,
                        Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                        String apiUnique,
                        Action2<String, HashMap<String, String>> headersAction,
                        Action1<RequestState> completeAction,
                        Action2<String, String> printLogAction,
                        String apiRequestKey,
                        HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxOptionsRequest request = new OkRxOptionsRequest(requestContentType);
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void trace(String url,
                      HashMap<String, String> headers,
                      HashMap<String, Object> params,
                      boolean isCache,
                      String cacheKey,
                      long cacheTime,
                      boolean isCallNCData,
                      RequestContentType requestContentType,
                      Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                      String apiUnique,
                      Action2<String, HashMap<String, String>> headersAction,
                      Action1<RequestState> completeAction,
                      Action2<String, String> printLogAction,
                      String apiRequestKey,
                      final HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxTraceRequest request = new OkRxTraceRequest(requestContentType);
        request.setCallNCData(isCallNCData);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void download(String url,
                         HashMap<String, String> headers,
                         HashMap<String, Object> params,
                         File downFile,
                         Action1<Float> progressAction,
                         Action1<File> successAction,
                         Action1<RequestState> completeAction,
                         String apiRequestKey,
                         HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxDownloadFileRequest request = new OkRxDownloadFileRequest();
        request.call(url, headers, params, downFile, progressAction, successAction, completeAction, apiRequestKey, reqQueueItemHashMap);
    }

    public void getBitmap(String url,
                          HashMap<String, String> headers,
                          HashMap<String, Object> params,
                          Action3<Bitmap, String, HashMap<String, ReqQueueItem>> successAction,
                          Action1<RequestState> completeAction,
                          String apiRequestKey,
                          HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxBitmapRequest request = new OkRxBitmapRequest();
        request.call(url, headers, params, successAction, completeAction, apiRequestKey, reqQueueItemHashMap, "", null);
    }

    public void uploadFile(String url,
                           HashMap<String, String> headers,
                           HashMap<String, Object> params,
                           boolean isCache,
                           String cacheKey,
                           long cacheTime,
                           RequestContentType requestContentType,
                           Action4<String, String, HashMap<String, ReqQueueItem>,Boolean> successAction,
                           Action1<RequestState> completeAction,
                           Action2<String, String> printLogAction,
                           String apiRequestKey,
                           HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPostRequest request = new OkRxPostRequest(requestContentType);
        request.call(url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, "", null);
    }

    public void uploadBytes(String url,
                            HashMap<String, String> httpHeaders,
                            HashMap<String, Object> httpParams,
                            List<ByteRequestItem> byteRequestItems,
                            Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                            Action1<RequestState> completeAction,
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
                           Action1<RequestState> completeAction,
                           Action2<String, String> printLogAction,
                           String apiRequestKey,
                           HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        List<ByteRequestItem> byteRequestItems = new ArrayList<ByteRequestItem>();
        byteRequestItems.add(byteRequestItem);
        uploadBytes(url, httpHeaders, httpParams, byteRequestItems, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap);
    }
}
