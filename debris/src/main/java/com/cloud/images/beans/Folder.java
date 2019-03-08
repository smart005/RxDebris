package com.cloud.images.beans;

import android.text.TextUtils;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class Folder {
    public String name;
    public String path;
    public ImageItem cover;
    public List<ImageItem> imageItems;

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return TextUtils.equals(other.path, path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
