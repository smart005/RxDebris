package com.cloud.nets.requests;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.cloud.nets.OkRx;
import com.cloud.nets.callback.BitmapCallback;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action3;

import java.util.HashMap;

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
public class OkRxBitmapRequest extends BaseRequest {

    @Override
    public void call(String url, HashMap<String, String> headers, HashMap<String, Object> params, Action3<Bitmap, String, HashMap<String, ReqQueueItem>> successAction, Action1<RequestState> completeAction, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiUnique, Action2<String, HashMap<String, String>> headersAction) {
        if (TextUtils.isEmpty(url)) {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            return;
        }
        setRequestType(RequestType.GET);
        Request.Builder builder = getBuilder(url, headers, params).get();
        Request request = builder.build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient();
        //绑定cookies
        bindCookies(client, request.url());
        //请求网络
        client.newCall(request).enqueue(new BitmapCallback(successAction, completeAction, reqQueueItemHashMap, apiRequestKey, apiUnique, headersAction));
    }
}
