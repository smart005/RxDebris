package com.cloud.images.figureset;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnReviewImageListener {

    public void onReview(List<String> images, int position);
}
