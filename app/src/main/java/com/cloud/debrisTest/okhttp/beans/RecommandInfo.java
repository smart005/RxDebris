package com.cloud.debrisTest.okhttp.beans;

import com.cloud.nets.beans.BaseBean;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/2
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class RecommandInfo extends BaseBean {

    private List<Slides> Slides;
    private List<Recommends> Recommends;

    public List<Slides> getSlides() {
        return Slides;
    }

    public void setSlides(List<Slides> Slides) {
        this.Slides = Slides;
    }

    public List<Recommends> getRecommends() {
        return Recommends;
    }

    public void setRecommends(List<Recommends> Recommends) {
        this.Recommends = Recommends;
    }

    public static class Slides {
        /**
         * Type : 4
         * Id : 43517
         * NewImgUrl : http://images.108sq.cn//Files/BuinessAd/2019/02/17/e9acc792-e3b4-40b1-a1a5-7ba47b181517.gif
         * Title : 鲜肉福袋，接福气！
         * InfoIdentity : http://anji.108sq.cn/shuo/detail/36883047?fromapp108sq=slide&fromapp108sqdata=3
         * ImgUrl : http://images.108sq.cn//Files/BuinessAd/2019/02/17/e9acc792-e3b4-40b1-a1a5-7ba47b181517.gif
         */

        private int Type;
        private int Id;
        private String NewImgUrl;
        private String Title;
        private String InfoIdentity;
        private String ImgUrl;

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getNewImgUrl() {
            return NewImgUrl;
        }

        public void setNewImgUrl(String NewImgUrl) {
            this.NewImgUrl = NewImgUrl;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getInfoIdentity() {
            return InfoIdentity;
        }

        public void setInfoIdentity(String InfoIdentity) {
            this.InfoIdentity = InfoIdentity;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String ImgUrl) {
            this.ImgUrl = ImgUrl;
        }
    }

    public static class Recommends {
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
    }
}
