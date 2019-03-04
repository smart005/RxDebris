package com.cloud.objects.beans;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/4
 * @Description:配置contentProvider时uriMatcher属性
 * @Modifier:
 * @ModifyContent:
 */
public class UriMatcherItem {
    /**
     * 跳转的uri
     */
    private String uri = "";
    /**
     * 调用uri对应的方法(action)
     */
    private String action = "";
    /**
     * 调用action里匹配code部分
     */
    private int code = 0;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
