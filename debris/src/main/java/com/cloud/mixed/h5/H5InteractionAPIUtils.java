package com.cloud.mixed.h5;

import android.text.TextUtils;

import com.cloud.mixed.h5.beans.APIReturnResult;
import com.cloud.mixed.h5.beans.ArgFieldItem;
import com.cloud.mixed.h5.beans.H5GetAPIMethodArgsBean;
import com.cloud.mixed.h5.enums.APIRequestState;
import com.cloud.nets.OkRxManager;
import com.cloud.nets.beans.CompleteResponse;
import com.cloud.nets.beans.ResponseData;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.beans.SuccessResponse;
import com.cloud.nets.enums.CallStatus;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RequestContentType;
import com.cloud.objects.enums.RequestState;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.events.Action1;
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
     * @param baseUrl 请求api的基地址
     * @param extras  {"apiName":"login",
     *                "target":"返回时带回去",
     *                "reqType":"put/patch/get/post/delete",
     *                "args":[{"fieldName":"userName","fieldValue":"test"},
     *                {"fieldName":"password","fieldValue":"123456"}]}
     * @param headers 请求头
     */
    public static void getAPIMethod(String baseUrl,
                                    String extras,
                                    HashMap<String, String> headers,
                                    RequestContentType requestContentType,
                                    final Func2<Object, APIRequestState, APIReturnResult> callback,
                                    String userClass) {
        try {
            if (TextUtils.isEmpty(extras)) {
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
//            switch (h5GetAPIMethodArgsBean.getReqType().trim().toLowerCase()) {
//                case "get":
//                    request(url, headers, params, target, callback, userClass,RequestType.GET);
//                    break;
//                case "post":
//                    request(url, headers, params, target, requestContentType, callback, userClass);
//                    break;
//                case "put":
//                    request(url, headers, params, target, requestContentType, callback, userClass);
//                    break;
//                case "patch":
//                    request(url, headers, params, target, requestContentType, callback, userClass);
//                    break;
//                case "delete":
//                    request(url, headers, params, target, requestContentType, callback, userClass);
//                    break;
//            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static void request(String url,
                                HashMap<String, String> headers,
                                HashMap<String, Object> params,
                                final String target,
                                RequestContentType requestContentType,
                                final Func2<Object, APIRequestState, APIReturnResult> callback,
                                String userClass,
                                RequestType requestType) {
        RetrofitParams retrofitParams = new RetrofitParams();
        TreeMap<String, Object> requestParams = retrofitParams.getParams();
        requestParams.putAll(params);
        retrofitParams.setCallStatus(CallStatus.OnlyNet);
        retrofitParams.setRequestContentType(requestContentType);
        retrofitParams.setRequestType(requestType);
        OkRxManager.getInstance().request(url,
                headers,
                retrofitParams,
                requestContentType,
                userClass,
                new Action1<SuccessResponse>() {
                    @Override
                    public void call(SuccessResponse response) {
                        if (callback != null) {
                            ResponseData responseData = response.getResponseData();
                            APIReturnResult apiReturnResult = new APIReturnResult();
                            apiReturnResult.setResponse(responseData.getResponse());
                            apiReturnResult.setTarget(target);
                            callback.call(APIRequestState.Success, apiReturnResult);
                        }
                    }
                },
                new Action1<CompleteResponse>() {
                    @Override
                    public void call(CompleteResponse response) {
                        if (callback != null && response.getRequestState() == RequestState.Completed) {
                            callback.call(APIRequestState.Complate, null);
                        }
                    }
                });
    }
}
