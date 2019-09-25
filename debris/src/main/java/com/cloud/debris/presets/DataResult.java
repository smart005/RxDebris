package com.cloud.debris.presets;

import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.objects.enums.RequestState;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-09-02
 * Description:监听返回数据
 * Modifier:
 * ModifyContent:
 */
public class DataResult<T> {
    /**
     * 预设数据
     */
    private T preset;
    /**
     * 数据类型
     */
    private DataType dataType;
    /**
     * 错误类型
     */
    private ErrorType errorType;
    /**
     * 扩展数据
     */
    private Object[] extras;
    /**
     * 请求状态
     */
    private RequestState requestState;

    public T getPreset() {
        return preset;
    }

    public void setPreset(T preset) {
        this.preset = preset;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public Object[] getExtras() {
        return extras;
    }

    public void setExtras(Object[] extras) {
        this.extras = extras;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }
}
