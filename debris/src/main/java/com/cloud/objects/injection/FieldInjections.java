package com.cloud.objects.injection;

import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.annotations.OriginalField;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.ValidUtils;

import java.lang.reflect.Field;
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

    public <T> void injection(T entity, String json) {
        if (entity == null || TextUtils.isEmpty(json)) {
            return;
        }
        injectionOriginalFieldValue(entity, false, -1, json);
        Class<?> aClass = entity.getClass().getSuperclass();
        injectionSupperObjectValues(entity, aClass, json);
    }

    private <T> void injectionOriginalFieldValue(T entity, boolean isArray, int position, String json) {
        Class<?> entityClass = entity.getClass();
        //对象定义字段
        Field[] fields = entityClass.getDeclaredFields();
        bindFieldValues(entity, fields, isArray, position, json);
    }

    private <T> void bindFieldValues(T entity, Field[] fields, boolean isArray, int position, String json) {
        for (Field field : fields) {
            String typeName = field.getType().getSimpleName();
            if (TextUtils.equals(typeName, "List") || TextUtils.equals(typeName, "ArrayList")) {
                //列表
                injectionListObject(GlobalUtils.getPropertiesValue(entity, field.getName()), json);
            } else {
                if (!field.isAnnotationPresent(OriginalField.class)) {
                    continue;
                }
                OriginalField annotation = field.getAnnotation(OriginalField.class);
                String partJson = JsonUtils.getValue(annotation.value(), json);
                if (isArray) {
                    List<String> matches = ValidUtils.matches("\\{(.*?)\\}", partJson);
                    if (!ObjectJudge.isNullOrEmpty(matches) && matches.size() > position) {
                        partJson = matches.get(position);
                    }
                }
                GlobalUtils.setPropertiesValue(entity, field.getName(), partJson);
            }
        }
    }

    private void injectionListObject(Object value, String json) {
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
            injectionListFieldValue(list.get(i), i, json);
        }
    }

    private <T> void injectionListFieldValue(T entity, int position, String json) {
        if (entity == null) {
            return;
        }
        injectionOriginalFieldValue(entity, true, position, json);
    }

    private <T> void injectionSupperObjectValues(T entity, Class cls, String json) {
        if (!isTreatability(cls)) {
            return;
        }
        Field[] fields = cls.getDeclaredFields();
        bindFieldValues(entity, fields, false, -1, json);
        Class superclass = cls.getSuperclass();
        String simpleName = superclass.getSimpleName();
        if (simpleName != null && !TextUtils.equals(simpleName, "Object")) {
            injectionSupperObjectValues(entity, superclass, json);
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
