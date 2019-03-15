package com.cloud.cache;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:堆栈信息记录
 * Modifier:
 * ModifyContent:
 */
@Entity(nameInDb = "rx_cache_stack_info")
public class StackInfoItem {

    @Id()
    @Property(nameInDb = "key")
    @Index(unique = true)
    private String key = "";
    /**
     * 堆栈信息
     */
    @Property(nameInDb = "stack")
    private String stack = "";
    /**
     * 请求状态码
     */
    @Property(nameInDb = "code")
    private int code = 0;
    /**
     * 请求协议
     */
    @Property(nameInDb = "protocol")
    private String protocol = "";
    /**
     * 头信息
     */
    private String headers = null;
    /**
     * 参数
     */
    private String params = null;
    /**
     * 请求类型
     */
    private String requestType = "";
    /**
     * url
     */
    private String url = "";
    /**
     * 错误消息
     */
    private String message = "";

    @Generated(hash = 748866985)
    public StackInfoItem(String key, String stack, int code, String protocol, String headers,
                         String params, String requestType, String url, String message) {
        this.key = key;
        this.stack = stack;
        this.code = code;
        this.protocol = protocol;
        this.headers = headers;
        this.params = params;
        this.requestType = requestType;
        this.url = url;
        this.message = message;
    }

    @Generated(hash = 461612283)
    public StackInfoItem() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
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

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
