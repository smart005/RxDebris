package com.cloud.nets;

import android.text.TextUtils;

import com.cloud.nets.annotations.ApiCheckAnnotation;
import com.cloud.nets.annotations.RequestTimeLimit;
import com.cloud.nets.beans.TokenProperties;
import com.cloud.nets.enums.TokenLocation;
import com.cloud.nets.events.OnHeaderCookiesListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.nets.properties.OkRxValidParam;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.utils.ConvertUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/7
 * Description:okgo请求验证
 * Modifier:
 * ModifyContent:
 */
public class OkrxRequestValid {

    /**
     * @param t
     * @param invokeMethodName
     * @param <T>
     * @return
     */
    public <T extends BaseService> OkRxValidParam check(T t, String invokeMethodName) {
        OkRxValidParam validParam = new OkRxValidParam();
        validParam.setInvokeMethodName(invokeMethodName);
        if (t == null) {
            validParam.setFlag(false);
            return validParam;
        }
        if (TextUtils.isEmpty(invokeMethodName)) {
            validParam.setFlag(false);
            return validParam;
        }
        //请求开始时间
        validParam.setCurrentRequestTime(System.currentTimeMillis());
        Method method = null;
        Method[] methods = t.getClass().getMethods();
        for (Method m : methods) {
            if (!TextUtils.equals(m.getName(), invokeMethodName)) {
                continue;
            }
            method = m;
            break;
        }
        if (method != null) {
            if (!method.isAnnotationPresent(ApiCheckAnnotation.class)) {
                validParam.setFlag(false);
                return validParam;
            }
            methodValid(method, validParam, t);
            //如果有请求注解则获取
            if (method.isAnnotationPresent(RequestTimeLimit.class)) {
                bindRequestTime(method, validParam);
            }
            return validParam;
        } else {
            validParam.setFlag(false);
        }
        return validParam;
    }

    private void bindRequestTime(Method method, OkRxValidParam validParam) {
        RequestTimeLimit annotation = method.getAnnotation(RequestTimeLimit.class);
        if (annotation == null) {
            return;
        }
        if (!TextUtils.isDigitsOnly(annotation.totalTime())) {
            return;
        }
        long time = Long.parseLong(annotation.totalTime());
        long milliseconds = ConvertUtils.toMilliseconds(time, annotation.unit());
        validParam.setRequestTotalTime(milliseconds);
    }

    private <T extends BaseService> void methodValid(Method method, OkRxValidParam validParam, T t) {
        ApiCheckAnnotation apiCheckAnnotation = method.getAnnotation(ApiCheckAnnotation.class);
        BaseSubscriber subscriber = t.getBaseSubscriber();
        if (subscriber == null) {
            //若未注册请求定阅则结束请求
            validParam.setFlag(false);
            return;
        }
        validParam.setApiCheckAnnotation(apiCheckAnnotation);
        //检查网络
        tokenValid(validParam, apiCheckAnnotation.isTokenValid());
    }

    private String getTokenValue(OkRxValidParam validParam, OkRxConfigParams configParams, TokenProperties tokenConfig, TokenLocation location) {
        String token = "";
        if (location == TokenLocation.header) {
            HashMap<String, String> headerParams = OkRx.getInstance().getHeaderParams();
            //参数为null或不包含token则从全局头信息取
            if (headerParams == null || !headerParams.containsKey(tokenConfig.getTokenName())) {
                HashMap<String, String> headers = configParams.getHeaders();
                if (headers != null && headers.containsKey(tokenConfig.getTokenName())) {
                    token = headers.get(tokenConfig.getTokenName());
                }
            } else {
                token = headerParams.get(tokenConfig.getTokenName());
                //如果此时为空也从全局头信息取
                if (TextUtils.isEmpty(token)) {
                    HashMap<String, String> headers = configParams.getHeaders();
                    if (headers != null && headers.containsKey(tokenConfig.getTokenName())) {
                        token = headers.get(tokenConfig.getTokenName());
                    }
                }
            }
        } else if (location == TokenLocation.cookie) {
            //从cookie中获取token信息
            OnHeaderCookiesListener cookiesListener = OkRx.getInstance().getOnHeaderCookiesListener();
            if (cookiesListener != null) {
                Map<String, String> map = cookiesListener.onCookiesCall();
                if (map != null && map.containsKey(tokenConfig.getTokenName())) {
                    token = map.get(tokenConfig.getTokenName());
                }
            }
        } else {
            validParam.setFlag(true);
        }
        return token;
    }

    private void tokenValid(OkRxValidParam validParam, boolean isTokenValid) {
        //token校验
        if (isTokenValid) {
            //获取配置参数
            OkRxConfigParams configParams = OkRx.getInstance().getOkRxConfigParams();
            //获取token配置
            TokenProperties tokenConfig = configParams.getTokenConfig();
            TokenLocation location = tokenConfig.getLocation();
            String tokenValue = getTokenValue(validParam, configParams, tokenConfig, location);
            if (TextUtils.isEmpty(tokenValue)) {
                validParam.setFlag(false);
                validParam.setNeedLogin(true);
            } else {
                validParam.setNeedLogin(false);
                validParam.setFlag(true);
            }
        } else {
            validParam.setFlag(true);
        }
    }

    /**
     * 获取调用方法名
     * <p>
     * return
     */
    public static String getInvokingMethodName() {
        Thread currentThread = Thread.currentThread();
        StackTraceElement[] stacks = currentThread.getStackTrace();
        if (ObjectJudge.isNullOrEmpty(stacks)) {
            return "";
        }
        String[] fms = {"getThreadStackTrace", "getStackTrace", "getInvokingMethodName", "check", "requestObject"};
        List<String> fmslst = Arrays.asList(fms);
        String methodName = "";
        for (StackTraceElement stack : stacks) {
            if (!fmslst.contains(stack.getMethodName())) {
                methodName = stack.getMethodName();
                break;
            }
        }
        return methodName;
    }
}
