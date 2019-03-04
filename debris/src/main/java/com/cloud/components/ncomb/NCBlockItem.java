package com.cloud.components.ncomb;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/27
 * @Description:当前视图块属性
 * @Modifier:
 * @ModifyContent:
 */
public class NCBlockItem {
    /**
     * 图片地址
     */
    private String imgUrl = "";
    /**
     * 背影
     */
    private String bg = "";

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }
}
