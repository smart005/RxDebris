package com.cloud.objects.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/28
 * Description:string key类型 map item
 * Modifier:
 * ModifyContent:
 */
public class MapEntryItem<T> extends MapEntry<String, T> {
    public MapEntryItem(String key, T value) {
        super(key, value);
    }
}
