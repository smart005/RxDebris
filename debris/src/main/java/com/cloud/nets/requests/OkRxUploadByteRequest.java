package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.nets.OkRx;
import com.cloud.nets.events.OnRequestErrorListener;
import com.cloud.nets.properties.ByteRequestItem;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action3;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class OkRxUploadByteRequest {

    private String responseString = "";

    public void call(String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     List<ByteRequestItem> byteRequestItems,
                     final Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                     final Action1<RequestState> completeAction,
                     final Action2<String, String> printLogAction,
                     final String apiRequestKey,
                     final HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        try {
            if (TextUtils.isEmpty(url)) {
                if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                    reqQueueItemHashMap.remove(apiRequestKey);
                }
                if (completeAction != null) {
                    completeAction.call(RequestState.Completed);
                }
                return;
            }
            OkHttpClient client = OkRx.getInstance().getOkHttpClient();
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (!ObjectJudge.isNullOrEmpty(byteRequestItems)) {
                for (ByteRequestItem byteRequestItem : byteRequestItems) {
                    if (byteRequestItem.getBs() == null) {
                        continue;
                    }
                    RequestBody body = RequestBody.create(MediaType.parse(byteRequestItem.getMediaTypeValue()), byteRequestItem.getBs());
                    String filename = String.format("%s.rxtiny", GlobalUtils.getGuidNoConnect());
                    requestBody.addFormDataPart(byteRequestItem.getFieldName(), filename, body);
                }
            }
            if (!ObjectJudge.isNullOrEmpty(params)) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() instanceof Byte[]) {
                        continue;
                    }
                    if (entry.getValue() instanceof List) {
                        requestBody.addFormDataPart(entry.getKey(), JsonUtils.toStr(entry.getValue()));
                    } else {
                        requestBody.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
            }
            Request.Builder builder = new Request.Builder().url(url).post(requestBody.build());
            if (!ObjectJudge.isNullOrEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            Request request = builder.build();
            client.newBuilder().readTimeout(60000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (completeAction != null) {
                        completeAction.call(RequestState.Error);
                    }
                    if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                        reqQueueItemHashMap.remove(apiRequestKey);
                    }
                    //抛出失败回调到全局监听
                    OnRequestErrorListener errorListener = OkRx.getInstance().getOnRequestErrorListener();
                    if (errorListener != null) {
                        errorListener.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) {
                    try {
                        if (successAction != null && response != null) {
                            ResponseBody body = response.body();
                            responseString = body.string();
                            successAction.call(responseString, apiRequestKey, reqQueueItemHashMap);
                        }
                    } catch (Exception e) {
                        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            reqQueueItemHashMap.remove(apiRequestKey);
                        }
                        if (completeAction != null) {
                            completeAction.call(RequestState.Completed);
                        }
                        Logger.error(e);
                    }
                }
            });
        } catch (Exception e) {
            completeAction.call(RequestState.Completed);
            if (printLogAction != null) {
                printLogAction.call(apiRequestKey, "");
            }
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            Logger.error(e);
        }
    }
}
