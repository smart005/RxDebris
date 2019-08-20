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
}
