package com.cloud.nets.requests;

import com.cloud.nets.OkRx;
import com.cloud.nets.beans.RequestErrorInfo;
import com.cloud.nets.events.OnRequestErrorListener;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.logs.CrashUtils;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.DeviceUtils;
import com.cloud.objects.utils.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:错误处理
 * Modifier:
 * ModifyContent:
 */
public class NetErrorWith {

    public void call(Call call, IOException e) {
        OnRequestErrorListener errorListener = OkRx.getInstance().getOnRequestErrorListener();
        if (errorListener == null) {
            return;
        }
        Request request = call.request();
        HashMap<String, String> requestParams = request.tag(HashMap.class);
        RequestErrorInfo errorInfos = getRequestErrorInfos(e, e.getMessage(), requestParams);
        errorListener.onFailure(errorInfos);
    }

    private static RequestErrorInfo getRequestErrorInfos(IOException e,
                                                         String message,
                                                         HashMap<String, String> requestParams) {
        RequestErrorInfo errorInfo = new RequestErrorInfo();
        TreeSet<String> stacks = errorInfo.getStacks();
        if (OkRx.getInstance().isHasFirmwareConfigInformationForTraceLog()) {
            //拦截公共头信息
            Map<String, Object> deviceInfo = DeviceUtils.getProgramDeviceInfo();
            String join = ConvertUtils.toJoin(deviceInfo, "\n");
            stacks.add(join);
        }
        //错误堆栈信息
        stacks.add(CrashUtils.getCrashInfo(e));
        //公共头信息
        errorInfo.setCommonHeaders(JsonUtils.toJson(OkRx.getInstance().getHeaderParams()));
        //错误消息
        errorInfo.setMessage(message);
        if (!ObjectJudge.isNullOrEmpty(requestParams)) {
            //请求类型
            if (requestParams.containsKey("requestType")) {
                errorInfo.setRequestType(requestParams.get("requestType"));
            }
            //请求url
            if (requestParams.containsKey("requestUrl")) {
                errorInfo.setUrl(requestParams.get("requestUrl"));
            }
            //请求信息
            if (requestParams.containsKey("requestHeaders")) {
                errorInfo.setHeaders(requestParams.get("requestHeaders"));
            }
            //请求参数
            if (requestParams.containsKey("requestParams")) {
                errorInfo.setParams(requestParams.get("requestParams"));
            }
        }
        return errorInfo;
    }
}
