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
}
