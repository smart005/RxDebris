package com.cloud.images.figureset;

import java.util.TreeMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/16
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnUploadCompletedListener {
    public void onUploadCompleted(TreeMap<Integer, String> uploadedUrls);
}
