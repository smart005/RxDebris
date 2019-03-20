package com.cloud.nets.requests;

import android.text.TextUtils;

import com.cloud.nets.OkRx;
import com.cloud.nets.callback.FileCallback;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action1;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

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
public class OkRxDownloadFileRequest extends BaseRequest {

    public void call(String url,
                     HashMap<String, String> headers,
                     TreeMap<String, Object> params,
                     File downFile,
                     Action1<Float> progressAction,
                     Action1<File> successAction,
                     Action1<RequestState> completeAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        if (TextUtils.isEmpty(url) || downFile == null || !downFile.exists()) {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            return;
        }
        setRequestType(RequestType.GET);
        Request.Builder builder = getBuilder(url, headers, params, null).get();
        Request request = builder.build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient();
        //绑定cookies
        bindCookies(client, request.url());
        //请求网络
        client.newCall(request).enqueue(new FileCallback(downFile, progressAction, successAction, completeAction, reqQueueItemHashMap, apiRequestKey));
    }
}
