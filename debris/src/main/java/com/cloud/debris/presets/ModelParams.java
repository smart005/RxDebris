package com.cloud.debris.presets;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-09
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ModelParams<T> {

    private Object extras;

    private T value;
    /**
     * true-成功逻辑;false-失败逻辑;
     */
    private boolean success;

    public Object getExtras() {
        return extras;
    }

    public void setExtras(Object extras) {
        this.extras = extras;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
