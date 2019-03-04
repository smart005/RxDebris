package com.cloud.nets.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/21
 * @Description:错误监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnResponseErrorListener {
    /**
     * 回调失败监听
     *
     * @param code    接口返回码
     * @param message 返回错误消息
     */
    public void onResponseError(String code, String message);
}
