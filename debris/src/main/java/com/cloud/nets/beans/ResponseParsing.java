package com.cloud.nets.beans;

import com.cloud.nets.enums.ResponseDataType;

import java.io.InputStream;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/21
 * Description:响应解析
 * Modifier:
 * ModifyContent:
 */
public class ResponseParsing {

    /**
     * 数据类
     */
    private Class dataClass = null;
    /**
     * 响应数据类型
     */
    private ResponseDataType responseDataType = ResponseDataType.object;
    /**
     * 解析后的数据对象
     */
    private Object data = null;
    /**
     * 字节数据
     */
    private byte[] bytes = null;
    /**
     * 流数据
     */
    private InputStream stream = null;

    public Class getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class dataClass) {
        this.dataClass = dataClass;
    }

    public ResponseDataType getResponseDataType() {
        if (responseDataType == null) {
            responseDataType = ResponseDataType.object;
        }
        return responseDataType;
    }

    public void setResponseDataType(ResponseDataType responseDataType) {
        this.responseDataType = responseDataType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }
}
