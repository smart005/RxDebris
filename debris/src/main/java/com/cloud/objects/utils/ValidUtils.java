package com.cloud.objects.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2015-6-15 上午10:16:18
 * Description: 验证处理
 * Modifier:
 * ModifyContent:
 */
public class ValidUtils {

    public static boolean valid(String pattern, String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        boolean flag = false;
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(input);
        while (mat.find()) {
            flag = mat.groupCount() > 0;
            if (flag) {
                break;
            }
        }
        return flag;
    }

    public static List<String> matches(String pattern, String input) {
        List<String> lst = new ArrayList<String>();
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(input);
        while (mat.find()) {
            if (mat.groupCount() > 0) {
                String value = mat.group(0);
                if (!TextUtils.isEmpty(value) && !lst.contains(value)) {
                    lst.add(value);
                }
            } else {
                String value = mat.group();
                if (!TextUtils.isEmpty(value) && !lst.contains(value)) {
                    lst.add(value);
                }
            }
        }
        return lst;
    }

    public static String matche(String pattern, String input) {
        List<String> lst = matches(pattern, input);
        if (lst == null || lst.isEmpty()) {
            return "";
        } else {
            return lst.get(0);
        }
    }

    public static List<String> matcheValues(String pattern, String matchAttrName, String input) {
        List<String> lst = new ArrayList<String>();
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(input);
        while (mat.find()) {
            //取属性值因此要求匹配到的计数大于1
            int count = mat.groupCount();
            if (count > 1) {
                for (int i = 1; i < count; i++) {
                    String value = mat.group(matchAttrName);
                    if (!TextUtils.isEmpty(value) && !lst.contains(value)) {
                        lst.add(value);
                    }
                }
            }
        }
        return lst;
    }

    /**
     * 验证接口服务
     * <p>
     * param service
     * param <T>
     * return true:是接口服务且接口数大于1;false:不是接口服务或接口数等于0;
     */
    public static <T> boolean validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            return false;
        }
        // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
        // Android (http://b.android.com/58753) but it forces composition of API declarations which is
        // the recommended pattern.
        if (service.getInterfaces().length > 0) {
            //继承了其它接口
            return false;
        }
        return true;
    }
}
