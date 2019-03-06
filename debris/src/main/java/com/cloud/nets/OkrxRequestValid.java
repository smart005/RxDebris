package com.cloud.nets;

import android.text.TextUtils;

import com.cloud.cache.MemoryCache;
import com.cloud.nets.annotations.ApiCheckAnnotation;
import com.cloud.nets.annotations.RequestTimeLimit;
import com.cloud.nets.events.OnAuthListener;
import com.cloud.nets.events.OnRequestNetCheckListener;
import com.cloud.nets.properties.OkRxValidParam;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.utils.ConvertUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
        netValid(validParam, apiCheckAnnotation.isTokenValid());
    }

    private void netValid(OkRxValidParam validParam, boolean isTokenValid) {
        //从缓存列表中获取监听对象,如果对象为null或类型不匹配再继续请求
        Object o = MemoryCache.getInstance().get(Keys.netConnectListenerKey);
        if (o == null || !(o instanceof OnRequestNetCheckListener)) {
            validParam.setFlag(true);
            return;
        }
        OnRequestNetCheckListener listener = (OnRequestNetCheckListener) o;
        if (listener.onNetConnectCheck()) {
            //token校验
            if (isTokenValid) {
                //获取token,如果监听null或不匹配再继续请求由返回结果来校验
                Object tokenListener = MemoryCache.getInstance().get(Keys.tokenRequestListenerKey);
                if (tokenListener != null && (tokenListener instanceof OnAuthListener)) {
                    OnAuthListener authListener = (OnAuthListener) tokenListener;
                    if (TextUtils.isEmpty(authListener.getAuthToken())) {
                        validParam.setFlag(false);
                        validParam.setNeedLogin(true);
                    } else {
                        validParam.setNeedLogin(false);
                        validParam.setFlag(true);
                    }
                } else {
                    validParam.setFlag(true);
                }
            } else {
                validParam.setFlag(true);
            }
        } else {
            //无网络调用缓存数据;
            validParam.setFlag(true);
            validParam.setLoadCacheData(true);
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
