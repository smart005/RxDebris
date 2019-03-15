package com.cloud.nets.callback;

import com.cloud.nets.OkRx;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.nets.requests.ErrorWith;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.events.Action1;
import com.cloud.objects.logs.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
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
public class FileCallback implements Callback {

    //下载文件
    private File downFile = null;
    //下载进度
    private Action1<Float> progressAction = null;
    //处理成功回调
    private Action1<File> successAction = null;
    //请求完成时回调(成功或失败)
    private Action1<RequestState> completeAction = null;
    //请求标识队列
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = null;
    //请求标识
    private String apiRequestKey = "";

    public FileCallback(File downFile,
                        Action1<Float> progressAction,
                        Action1<File> successAction,
                        Action1<RequestState> completeAction,
                        HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                        String apiRequestKey) {
        this.downFile = downFile;
        this.progressAction = progressAction;
        this.successAction = successAction;
        this.completeAction = completeAction;
        this.reqQueueItemHashMap = reqQueueItemHashMap;
        this.apiRequestKey = apiRequestKey;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (call.isCanceled()) {
            Request request = call.request();
            HttpUrl url = request.url();
            String host = url.host();
            Set<String> domainList = OkRx.getInstance().getFailDomainList();
            if (domainList.contains(host)) {
                //如果域名已在失败列表在新创建连接并重新请求仍失败,服务器地址有问题或当前网络异常;
                //此时直接返回即可
                return;
            }
            domainList.add(host);
            //如果连接已经被取消时则重新建立
            OkHttpClient client = OkRx.getInstance().getOkHttpClient(true);
            //创建新请求
            Call clone = call.clone();
            client.newCall(clone.request()).enqueue(this);
            return;
        }
        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
            reqQueueItemHashMap.remove(apiRequestKey);
        }
        if (completeAction != null) {
            completeAction.call(RequestState.Error);
        }
        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
            queueItem.setReqNetCompleted(true);
        }
        //抛出失败回调到全局监听
        ErrorWith errorWith = new ErrorWith();
        errorWith.call(call, e);
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
            if (response == null) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
            }
            ResponseBody body = response.body();
            if (body == null) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
                return;
            }
            if (successAction != null) {
                InputStream stream = body.byteStream();
                //获取字节流总长度
                long total = body.contentLength();
                FileOutputStream fos = new FileOutputStream(downFile);
                long sum = 0;
                int len = 0;
                byte[] buf = new byte[2048];
                DecimalFormat df = new DecimalFormat("0.00");
                while ((len = stream.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    float progress = sum * 1.0f / total;
                    if (progressAction != null) {
                        Number number = NumberFormat.getNumberInstance().parse(df.format(progress));
                        progressAction.call(number.floatValue());
                    }
                }
                fos.flush();
                successAction.call(downFile);
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                queueItem.setReqNetCompleted(true);
            }
        }
    }
}
