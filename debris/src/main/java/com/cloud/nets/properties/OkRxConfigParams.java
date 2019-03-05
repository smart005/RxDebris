package com.cloud.nets.properties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/8
 * Description:OkRx配置参数
 * Modifier:
 * ModifyContent:
 */
public class OkRxConfigParams {

    //连接超时时间(毫秒)
    private long connectTimeout = 10000;
    //读取超时时间(毫秒)
    private long readTimeOut = 10000;
    //写入超时时间(毫秒)
    private long writeTimeOut = 10000;
    //重连次数
    private int retryCount = 3;
    //公共头参数
    private HashMap<String, String> headers = null;
    //api成功返回码.接口返回时code对应码只有在集合中才能成功返回
    //set中的value保证唯一
    private Set<String> apiSuccessRetCodes = null;
    //api名称验证过虑,对于此集合中api名称不作code验证
    private Set<String> apiNameCodeValidFilter = null;
    //未授权返回码,是否登录可通过此状态判断
    private Set<String> unauthorizedRet = null;
    //返回码在此集合中则过虑掉toast消息提醒
    private Set<String> messageFilterRetCodes = null;
    /**
     * 是否处理请求结果,即请求成功后是否进行各状态验证(默认为false)
     */
    private boolean isProcessNetResults = false;
    /**
     * 是否进行网络状态码拦截(默认为false)
     */
    private boolean isNetStatusCodeIntercept = false;

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public long getWriteTimeOut() {
        return writeTimeOut;
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public HashMap<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public Set<String> getApiSuccessRetCodes() {
        if (apiSuccessRetCodes == null) {
            apiSuccessRetCodes = new HashSet<String>();
        }
        return apiSuccessRetCodes;
    }

    public void setApiSuccessRetCodes(Set<String> apiSuccessRetCodes) {
        this.apiSuccessRetCodes = apiSuccessRetCodes;
    }

    public Set<String> getApiNameCodeValidFilter() {
        if (apiNameCodeValidFilter == null) {
            apiNameCodeValidFilter = new HashSet<String>();
        }
        return apiNameCodeValidFilter;
    }

    public void setApiNameCodeValidFilter(Set<String> apiNameCodeValidFilter) {
        this.apiNameCodeValidFilter = apiNameCodeValidFilter;
    }

    public Set<String> getUnauthorizedRet() {
        if (unauthorizedRet == null) {
            unauthorizedRet = new HashSet<String>();
        }
        return unauthorizedRet;
    }

    public void setUnauthorizedRet(Set<String> unauthorizedRet) {
        this.unauthorizedRet = unauthorizedRet;
    }

    public Set<String> getMessageFilterRetCodes() {
        if (messageFilterRetCodes == null) {
            messageFilterRetCodes = new HashSet<String>();
        }
        return messageFilterRetCodes;
    }

    public void setMessageFilterRetCodes(Set<String> messageFilterRetCodes) {
        this.messageFilterRetCodes = messageFilterRetCodes;
    }

    public boolean isProcessNetResults() {
        return isProcessNetResults;
    }

    public void setProcessNetResults(boolean processNetResults) {
        isProcessNetResults = processNetResults;
    }

    public boolean isNetStatusCodeIntercept() {
        return isNetStatusCodeIntercept;
    }

    public void setNetStatusCodeIntercept(boolean netStatusCodeIntercept) {
        isNetStatusCodeIntercept = netStatusCodeIntercept;
    }
}
