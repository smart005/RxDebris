package com.cloud.nets;

import com.cloud.cache.RxStacks;
import com.cloud.objects.ObjectJudge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/29
 * Description:http重连拦截器
 * Modifier:
 * ModifyContent:
 */
public class RequestRetryIntercepter implements Interceptor {
    private int maxRetry;//最大重试次数
    private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
    private HashMap<String, String> headers = null;

    public RequestRetryIntercepter(int maxRetry, HashMap<String, String> headers) {
        this.maxRetry = maxRetry;
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = null;
        if (ObjectJudge.isNullOrEmpty(headers)) {
            request = chain.request();
        } else {
            Request.Builder builder = chain.request().newBuilder();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.removeHeader(entry.getKey());
                builder.addHeader(entry.getKey(), entry.getValue());
            }
            request = builder.build();
        }
        Object tag = request.tag();
        RxStacks.setRequestChainInfo(tag, request.method(), request.url().toString(), headers);
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNum < maxRetry) {
            retryNum++;
            response = chain.proceed(request);
        }
        //记录状态和协议
        RxStacks.setRequestStateProtocol(tag, response.code(), response.protocol().toString());
        return response;
    }
}
