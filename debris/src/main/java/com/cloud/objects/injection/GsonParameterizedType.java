package com.cloud.objects.injection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/12
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class GsonParameterizedType implements ParameterizedType {
    Class clazz;

    public GsonParameterizedType(Class clz) {
        clazz = clz;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{clazz};
    }

    @Override
    public Type getRawType() {
        return List.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
