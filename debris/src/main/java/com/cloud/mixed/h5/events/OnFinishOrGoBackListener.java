package com.cloud.mixed.h5.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/11/10
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */

public interface OnFinishOrGoBackListener {
    public void onFinishOrGoBack(boolean isH5GoBack, String url);
}
