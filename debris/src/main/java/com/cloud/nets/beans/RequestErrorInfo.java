package com.cloud.nets.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:网络请求错误信息
 * Modifier:
 * ModifyContent:
 */
public class RequestErrorInfo {
    /**
     * 请求url
     */
    private String url = "";
    /**
     * 请求参数
     */
    private Map<String, Object> params = null;
    /**
     * 请求状态码
     */
    private int status = 0;
    /**
     * 请求错误消息
     */
    private String message = "";
    /**
     * 请求堆栈集合信息
     * (请求请求过程中所有线程不同堆栈信息)
     */
    private Map<String, String> stacks = null;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getStacks() {
        if (stacks == null) {
            stacks = new HashMap<String, String>();
        }
        return stacks;
    }

    public void setStacks(Map<String, String> stacks) {
        this.stacks = stacks;
    }
}
