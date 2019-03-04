package com.cloud.nets.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/2
 * Description:接口服务类方法调用时的请求参数
 * Modifier:
 * ModifyContent:
 */
public class RequestParam<Type> {
    /**
     * 字段顺序
     */
    private int order;
    /**
     * 字段值
     */
    private Type fieldValue;

    public RequestParam(int order, Type fieldValue) {
        this.order = order;
        this.fieldValue = fieldValue;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Type getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Type fieldValue) {
        this.fieldValue = fieldValue;
    }
}
