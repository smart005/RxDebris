package com.cloud.nets.events;

import java.io.IOException;

import okhttp3.Call;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/6
 * Description:网络请求失败监听
 * Modifier:
 * ModifyContent:
 */
public interface OnRequestErrorListener {

    /**
     * 网络请求失败
     *
     * @param call 请求回调
     * @param e    网络异常
     */
    public void onFailure(Call call, IOException e);
}
