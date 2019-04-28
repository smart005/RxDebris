package com.cloud.objects.injection;

import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.annotations.OriginalField;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.ValidUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/12
 * Description:数据字段注入{@link com.cloud.objects.annotations.OriginalField}
 * Modifier:
 * ModifyContent:
 */
public class FieldInjections {

    /**
     * 字段json注入
     *
     * @param entity 对象
     * @param json   原json字符串
     * @param <T>    对象类型
     */
    public <T> void injection(T entity, String json) {
        if (entity == null || TextUtils.isEmpty(json)) {
            return;
        }
        injectionOriginalFieldValue(entity, false, -1, json, "");
        Class<?> aClass = entity.getClass().getSuperclass();
        injectionSupperObjectValues(entity, aClass, json, "");
    }

    private <T> void injectionOriginalFieldValue(T entity, boolean isArray, int position, String json, String currFieldName) {
        Class<?> entityClass = entity.getClass();
        //对象定义字段
        Field[] fields = entityClass.getDeclaredFields();
        bindFieldValues(entity, fields, isArray, position, json, currFieldName);
    }

    private boolean isObject(Class fieldType) {
        if (fieldType == String.class ||
                fieldType == int.class ||
                fieldType == Integer.class ||
                fieldType == double.class ||
                fieldType == Double.class ||
                fieldType == long.class ||
                fieldType == Long.class ||
                "Object".equals(fieldType.getSimpleName()) ||
                fieldType == byte.class ||
                fieldType == byte[].class ||
                fieldType == File.class ||
                fieldType == HashMap.class) {
            return false;
        }
        return true;
    }

    private <T> void bindFieldValues(T entity, Field[] fields, boolean isArray, int position, String json, String currFieldName) {
        for (Field field : fields) {
            String typeName = field.getType().getSimpleName();
            if (TextUtils.equals(typeName, "List") || TextUtils.equals(typeName, "ArrayList")) {
                //列表
                injectionListObject(GlobalUtils.getPropertiesValue(entity, field.getName()), json, field.getName());
            } else if (isObject(field.getType())) {
                Object value = GlobalUtils.getPropertiesValue(entity, field.getName());
                if (value == null) {
                    continue;
                }
                Class<?> aClass = value.getClass();
                Field[] declaredFields = aClass.getDeclaredFields();
                bindFieldValues(value, declaredFields, false, -1, json, field.getName());
                //绑定当前值
                bindAnnotations(entity, field, isArray, position, json, currFieldName);
            } else {
                bindAnnotations(entity, field, isArray, position, json, currFieldName);
            }
        }
    }

    private <T> void bindAnnotations(T entity, Field field, boolean isArray, int position, String json, String currFieldName) {
        if (TextUtils.isEmpty(currFieldName)) {
            return;
        }
        if (!field.isAnnotationPresent(OriginalField.class)) {
            return;
        }
        OriginalField annotation = field.getAnnotation(OriginalField.class);
        String[] keys = annotation.values();
        if (ObjectJudge.isNullOrEmpty(keys)) {
            return;
        }
        String effectiveKey = "";
        for (String key : keys) {
            if (TextUtils.equals(currFieldName, key) &&
                    JsonUtils.containerKey(key, json)) {
                effectiveKey = key;
                break;
            }
        }
        if (TextUtils.isEmpty(effectiveKey)) {
            return;
        }
        String partJson = JsonUtils.getValue(effectiveKey, json);
        if (isArray) {
            List<String> matches = ValidUtils.matches("\\{(.*?)\\}", partJson);
            if (!ObjectJudge.isNullOrEmpty(matches) && matches.size() > position) {
                partJson = matches.get(position);
            }
        }
        GlobalUtils.setPropertiesValue(entity, field.getName(), partJson);
    }

    private void injectionListObject(Object value, String json, String currFieldName) {
        if (!(value instanceof List)) {
            return;
        }
        List list = (List) value;
        if (ObjectJudge.isNullOrEmpty(list)) {
            return;
        }
        Object o = list.get(0);
        Class<?> oClass = o.getClass();
        if (!isTreatability(oClass)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            injectionListFieldValue(list.get(i), i, json, currFieldName);
        }
    }

    private <T> void injectionListFieldValue(T entity, int position, String json, String currFieldName) {
        if (entity == null) {
            return;
        }
        injectionOriginalFieldValue(entity, true, position, json, currFieldName);
    }

    private <T> void injectionSupperObjectValues(T entity, Class cls, String json, String currFieldName) {
        if (!isTreatability(cls)) {
            return;
        }
        Field[] fields = cls.getDeclaredFields();
        bindFieldValues(entity, fields, false, -1, json, currFieldName);
        Class superclass = cls.getSuperclass();
        String simpleName = superclass.getSimpleName();
        if (simpleName != null && !TextUtils.equals(simpleName, "Object")) {
            injectionSupperObjectValues(entity, superclass, json, currFieldName);
        }
    }

    private boolean isTreatability(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        if (TextUtils.equals(cls.getSimpleName(), "Object") ||
                TextUtils.equals(cls.getSimpleName(), "String") ||
                TextUtils.equals(cls.getSimpleName(), "Integer") ||
                TextUtils.equals(cls.getSimpleName(), "Double") ||
                TextUtils.equals(cls.getSimpleName(), "Float") ||
                TextUtils.equals(cls.getSimpleName(), "Long")) {
            return false;
        }
        return true;
    }
}
