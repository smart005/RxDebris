package com.cloud.nets.properties;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/27
 * @Description:请求数据项
 * @Modifier:
 * @ModifyContent:
 */
public class ReqQueueItem {
    /**
     * 是否网络请求完成
     */
    private boolean isReqNetCompleted = false;
    /**
     * 一般指请求是否真正进入成功回调
     */
    private boolean isSuccess = false;

    public boolean isReqNetCompleted() {
        return isReqNetCompleted;
    }

    public void setReqNetCompleted(boolean reqNetCompleted) {
        isReqNetCompleted = reqNetCompleted;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
