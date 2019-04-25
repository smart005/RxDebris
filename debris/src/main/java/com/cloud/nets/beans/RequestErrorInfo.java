package com.cloud.nets.beans;

import java.util.TreeSet;

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
     * 公共头
     */
    private String commonHeaders = "";
    /**
     * params headers
     */
    private String headers = "";
    /**
     * 请求参数
     */
    private String params = "";
    /**
     * 请求错误消息
     */
    private String message = "";
    /**
     * 请求堆栈集合信息
     * (请求请求过程中所有线程不同堆栈信息)
     */
    private TreeSet<String> stacks = null;
    /**
     * 本次请求类型
     */
    private String requestType = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommonHeaders() {
        return commonHeaders;
    }

    public void setCommonHeaders(String commonHeaders) {
        this.commonHeaders = commonHeaders;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TreeSet<String> getStacks() {
        if (stacks == null) {
            stacks = new TreeSet<String>();
        }
        return stacks;
    }

    public void setStacks(TreeSet<String> stacks) {
        this.stacks = stacks;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
