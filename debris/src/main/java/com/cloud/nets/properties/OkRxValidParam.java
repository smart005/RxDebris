package com.cloud.nets.properties;


import com.cloud.nets.annotations.ApiCheckAnnotation;
import com.cloud.nets.annotations.ReturnCodeFilter;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/8
 * Description:验证返回参数
 * Modifier:
 * ModifyContent:
 */
public class OkRxValidParam {
    private ApiCheckAnnotation apiCheckAnnotation = null;
    /**
     * 判断验证是否通过
     */
    private boolean flag = false;
    /**
     * 是否需要登录
     */
    private boolean isNeedLogin = false;
    /**
     * api返回过滤器
     */
    private ReturnCodeFilter returnCodeFilter = null;
    /**
     * 是否加载缓存数据
     */
    private boolean isLoadCacheData = false;
    /**
     * 请求总时间
     */
    private long requestTotalTime = 0;
    /**
     * 当前请求时间
     */
    private long currentRequestTime = 0;
    /**
     * 请求方法名
     */
    private String invokeMethodName = "";

    public ApiCheckAnnotation getApiCheckAnnotation() {
        return apiCheckAnnotation;
    }

    public void setApiCheckAnnotation(ApiCheckAnnotation apiCheckAnnotation) {
        this.apiCheckAnnotation = apiCheckAnnotation;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isNeedLogin() {
        return isNeedLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        isNeedLogin = needLogin;
    }

    public ReturnCodeFilter getReturnCodeFilter() {
        return returnCodeFilter;
    }

    public void setReturnCodeFilter(ReturnCodeFilter returnCodeFilter) {
        this.returnCodeFilter = returnCodeFilter;
    }

    public boolean isLoadCacheData() {
        return isLoadCacheData;
    }

    public void setLoadCacheData(boolean loadCacheData) {
        isLoadCacheData = loadCacheData;
    }

    public long getRequestTotalTime() {
        return requestTotalTime;
    }

    public void setRequestTotalTime(long requestTotalTime) {
        this.requestTotalTime = requestTotalTime;
    }

    public long getCurrentRequestTime() {
        return currentRequestTime;
    }

    public void setCurrentRequestTime(long currentRequestTime) {
        this.currentRequestTime = currentRequestTime;
    }

    public String getInvokeMethodName() {
        return invokeMethodName;
    }

    public void setInvokeMethodName(String invokeMethodName) {
        this.invokeMethodName = invokeMethodName;
    }
}
