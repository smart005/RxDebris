package com.cloud.debrisTest;

import com.google.gson.annotations.SerializedName;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/17
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ADItem {
    /**
     * AdId : 5508
     * SiteId : 1
     * Title : 绍兴-今日有料信息流广告-链接
     * ImagesField : 120547,/user/2018/0426/1059183550841535757074215.jpg,0,500*331
     * Tag : 福利
     * Link : http://sx.108sq.org:920/shuo/detail/264852?fromapp108sq=news&fromapp108sqdata=2
     * Style : 3
     * Weight : 2
     * Position : 2
     * ID : 0
     * AdUnionIos : 9040030277394669
     * AdUnionAndroid : 8070433135332699
     */

    /**
     * 广告id
     */
    private int AdId;
    /**
     * 站点id
     */
    @SerializedName(value = "SiteId", alternate = {"SiteID"})
    private int SiteId;
    /**
     * 标题
     */
    private String Title;
    /**
     * 图片字段
     */
    private String ImagesField;
    /**
     * 标签
     */
    private String Tag;
    /**
     * 广告跳转的url
     */
    private String Link;
    /**
     * 5=广告联盟 6=穿山甲联盟
     */
    private int Style;
    /**
     * 权重
     */
    private int Weight;
    /**
     * 广告所在位置
     */
    private int Position;
    /**
     * 不为0时同普通内容
     */
    private int ID;
    /**
     * 广告联盟IOS (4.8.0新增)
     */
    private String AdUnionIos;
    /**
     * 广告联盟Android(4.8.0新增)
     */
    private String AdUnionAndroid;

    public int getAdId() {
        return AdId;
    }

    public void setAdId(int adId) {
        AdId = adId;
    }

    public int getSiteId() {
        return SiteId;
    }

    public void setSiteId(int siteId) {
        SiteId = siteId;
    }

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImagesField() {
        return ImagesField == null ? "" : ImagesField;
    }

    public void setImagesField(String imagesField) {
        ImagesField = imagesField;
    }

    public String getTag() {
        return Tag == null ? "" : Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getLink() {
        return Link == null ? "" : Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getStyle() {
        return Style;
    }

    public void setStyle(int style) {
        Style = style;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAdUnionIos() {
        return AdUnionIos == null ? "" : AdUnionIos;
    }

    public void setAdUnionIos(String adUnionIos) {
        AdUnionIos = adUnionIos;
    }

    public String getAdUnionAndroid() {
        return AdUnionAndroid == null ? "" : AdUnionAndroid;
    }

    public void setAdUnionAndroid(String adUnionAndroid) {
        AdUnionAndroid = adUnionAndroid;
    }
}
