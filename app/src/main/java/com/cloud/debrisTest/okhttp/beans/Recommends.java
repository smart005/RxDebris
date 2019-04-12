package com.cloud.debrisTest.okhttp.beans;

import com.cloud.objects.annotations.OriginalField;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/12
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class Recommends {
    /**
     * SiteID : 0
     * Title : 女学生实名举报！与高校辅导员婚外情，还被正室殴打
     * ImagesField : 57002691,/user/2019/0228/1621195652572768640517232.jpg,0,533*300
     * CommentCount : 2
     * BrowserCount : 1824
     * Tag : 早知道
     * Link : http://common.108sq.cn/Html/Subect/Infos/8534.html
     * AdId : 0
     * InfoType : 1
     * IsMoreImages : false
     * ID : 37319977
     */

    private int SiteID;
    private String Title;
    private String ImagesField;
    private int CommentCount;
    private int BrowserCount;
    private String Tag;
    private String Link;
    private int AdId;
    private int InfoType;
    private boolean IsMoreImages;
    private int ID;
    @OriginalField(value = "Recommends")
    private String recommandJson;

    public int getSiteID() {
        return SiteID;
    }

    public void setSiteID(int SiteID) {
        this.SiteID = SiteID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getImagesField() {
        return ImagesField;
    }

    public void setImagesField(String ImagesField) {
        this.ImagesField = ImagesField;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int CommentCount) {
        this.CommentCount = CommentCount;
    }

    public int getBrowserCount() {
        return BrowserCount;
    }

    public void setBrowserCount(int BrowserCount) {
        this.BrowserCount = BrowserCount;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }

    public int getAdId() {
        return AdId;
    }

    public void setAdId(int AdId) {
        this.AdId = AdId;
    }

    public int getInfoType() {
        return InfoType;
    }

    public void setInfoType(int InfoType) {
        this.InfoType = InfoType;
    }

    public boolean isIsMoreImages() {
        return IsMoreImages;
    }

    public void setIsMoreImages(boolean IsMoreImages) {
        this.IsMoreImages = IsMoreImages;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRecommandJson() {
        return recommandJson == null ? "" : recommandJson;
    }

    public void setRecommandJson(String recommandJson) {
        this.recommandJson = recommandJson;
    }
}
