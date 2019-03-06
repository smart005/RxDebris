package com.cloud.mixed.h5.beans;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/6
 * @Description:h5初始化完成相关属性
 * @Modifier:
 * @ModifyContent:
 */
public class H5BuildProperties {
    /**
     * url
     */
    private String url = "";
    /**
     * html内容
     */
    private String htmlContent = "";
    /**
     * 是否隐藏分享
     */
    private boolean isHideShare = false;
    /**
     * 对加载的h5页面是否层级返回
     */
    private boolean isLevelReturn = false;
    /**
     * 标题
     */
    private String title = "";
    /**
     * 分享key或路径
     */
    private String shareKey = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public boolean isHideShare() {
        return isHideShare;
    }

    public void setHideShare(boolean hideShare) {
        isHideShare = hideShare;
    }

    public boolean isLevelReturn() {
        return isLevelReturn;
    }

    public void setLevelReturn(boolean levelReturn) {
        isLevelReturn = levelReturn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }
}
