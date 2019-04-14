package com.cloud.objects.utils;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/14
 * @Description:属性对象映射
 * @Modifier:
 * @ModifyContent:
 */
public class MapperUtils {

    /**
     * 将对象属性转为任意一个java bean属性
     * (通过此方式转换只做映射)
     *
     * @param object 要转换的对象
     * @param cls    转换后java bean的class类
     * @param <T>    转换后的类型
     * @return
     */
    public static <T> T toEntity(Object object, Class<T> cls) {
        String json = JsonUtils.toJson(object);
        return JsonUtils.parseT(json, cls);
    }

    /**
     * 对象(列表)转换成任意一个java bean列表属性
     *
     * @param object 要转换的对象列表
     * @param cls    转换后java bean的class类
     * @param <T>    转换后的类型
     * @return
     */
    public static <T> List<T> toList(Object object, Class<T> cls) {
        String json = JsonUtils.toJson(object);
        return JsonUtils.parseArray(json, cls);
    }
}
