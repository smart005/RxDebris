package com.cloud.debrisTest;

import com.cloud.objects.annotations.OriginalField;

public class Info {
    private String Id;
    private int SiteId;
    private float Distance;
    private String Address;
    private String Point;
    private String Title;
    private String Content;
    private String ImagesField;
    private String SourceName;
    private int TitularType;
    private long TitularId;
    private String TitularName;
    /**
     * 精华 1 预精华 3 版主精华 6 管理员精华
     */
    private int Essence;
    private int OperateType;
    private int BrowserCount;
    private int PublishUnixTime;
    private int CommentCount;
    private int TopCount;
    private boolean IsCloseComment;
    private String ShareImage;
    private long RefreshUnixTime;
    private long RecommendTime;
    private String RecId;//大家在聊推荐信息的推荐ID(暂用于埋点)
    @OriginalField(values = {"List", "InfoList", "Result"})
    private String origMsg;
    private boolean IsRec;
    private int InfoStatus;
    private int UserSex = -1;
    private int UserAge = -1;
    private boolean IsContentProcess;
    private int UseVideoStyle;

    public String getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public String getImagesField() {
        return ImagesField;
    }

    public String getSourceName() {
        return SourceName;
    }

    public int getTitularType() {
        return TitularType;
    }

    public long getTitularID() {
        return TitularId;
    }

    public String getTitularName() {
        return TitularName;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public int getBrowserCount() {
        return BrowserCount;
    }

    public int getTopCount() {
        return TopCount;
    }

    public int getPublishUnixTime() {
        return PublishUnixTime;
    }

    public int getEssence() {
        return Essence;
    }


    public int getSiteID() {
        return SiteId;
    }

    public int getOperateType() {
        return OperateType;
    }

    public boolean getIsCloseComment() {
        return IsCloseComment;
    }


    public String getPoint() {
        return Point;
    }

    public String getAddress() {
        return Address;
    }

    public float getDistance() {
        return Distance;
    }

    public String getShareImage() {
        return ShareImage;
    }

    public long getRecommendTime() {
        return RecommendTime;
    }

    public void setRecId(String recId) {
        this.RecId = recId;
    }

    public String getRecId() {
        return RecId;
    }

    public String getOrigMsg() {
        return origMsg;
    }

    public void setOrigMsg(String origMsg) {
        this.origMsg = origMsg;
    }

    public boolean isRec() {
        return IsRec;
    }

    public int getInfoStatus() {
        return InfoStatus;
    }

    public int getUserSex() {
        return UserSex;
    }

    public int getUserAge() {
        return UserAge;
    }

    public boolean getIsContentProcess() {
        return IsContentProcess;
    }

    public int getUseVideoStyle() {
        return UseVideoStyle;
    }
}
