package com.cloud.nets.filters;

import com.cloud.nets.BaseService;
import com.cloud.nets.annotations.ReturnCodeFilter;
import com.cloud.objects.ObjectJudge;

import java.lang.annotation.Annotation;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:返回码检测
 * Modifier:
 * ModifyContent:
 */
public class ReturnCodeCheck {

    private ReturnCodeFilter codeFilter = null;

    public  <S extends BaseService> ReturnCodeFilter getCodeFilter(S server) {
        if (codeFilter == null) {
            return getAnnonCodeFilter(server);
        } else {
            return codeFilter;
        }
    }

    private void getReturnCodeFilterAnnon(Class<?> cls, ReturnCodeFilter[] returnCodeFilters) {
        Annotation[] annotations = cls.getDeclaredAnnotations();
        if (ObjectJudge.isNullOrEmpty(annotations)) {
            Class<?> superclass = cls.getSuperclass();
            if (superclass != null) {
                getReturnCodeFilterAnnon(superclass, returnCodeFilters);
            }
        } else {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == ReturnCodeFilter.class) {
                    returnCodeFilters[0] = (ReturnCodeFilter) annotation;
                    break;
                }
            }
            if (returnCodeFilters[0] == null) {
                Class<?> superclass = cls.getSuperclass();
                if (superclass != null) {
                    getReturnCodeFilterAnnon(superclass, returnCodeFilters);
                }
            }
        }
    }

    private <S extends BaseService> ReturnCodeFilter getAnnonCodeFilter(S server) {
        Class<? extends BaseService> cls = server.getClass();
        ReturnCodeFilter[] returnCodeFilters = new ReturnCodeFilter[1];
        getReturnCodeFilterAnnon(cls, returnCodeFilters);
        if (returnCodeFilters[0] != null) {
            codeFilter = returnCodeFilters[0];
        }
        return returnCodeFilters[0];
    }
}
