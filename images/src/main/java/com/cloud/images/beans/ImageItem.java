package com.cloud.images.beans;

import android.text.TextUtils;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class ImageItem {
    public String path;
    public String name;
    public long time;

    public ImageItem(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return TextUtils.equals(this.path, other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
