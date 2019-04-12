package com.cloud.nets.requests;

import com.cloud.nets.RequestStacksInfo;
import com.cloud.nets.OkRx;
import com.cloud.nets.beans.RequestErrorInfo;
import com.cloud.nets.events.OnRequestErrorListener;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:错误处理
 * Modifier:
 * ModifyContent:
 */
public class NetErrorWith {

    public void call(String requestMethodName, Call call, IOException e, Map<String, String> headers, Map<String, Object> params) {
        OnRequestErrorListener errorListener = OkRx.getInstance().getOnRequestErrorListener();
        if (errorListener == null) {
            RequestStacksInfo.clearBusStacks(requestMethodName);
            return;
        }
        //保存当前堆栈
        RequestStacksInfo.setStack(requestMethodName, e);
        //保存基本信息
        RequestStacksInfo.setRequestInfo(requestMethodName, headers, params, e.getMessage());
        //获取跟踪信息
        RequestErrorInfo errorInfos = RequestStacksInfo.getRequestErrorInfos(requestMethodName);
        errorListener.onFailure(errorInfos);
    }
}
