package com.cloud.objects.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cloud.objects.ObjectJudge;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    public static String toStr(Object object) {
        if (object == null) {
            return "";
        }
        return JSON.toJSONString(object);
    }

    public static String toStr(String key, Object object) {
        JSONObject j = new JSONObject();
        j.put(key, object);
        return j.toJSONString();
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (JSONException e) {
            return newNull(clazz);
        }
    }

    public static <T> T parseT(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (JSONException e) {
            return newNull(clazz);
        }
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            return JSON.parseArray(json.trim(), clazz);
        } catch (JSONException e) {
            return new ArrayList<T>(0);
        }
    }

    public static <T> T newNull(Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            return t;
        } catch (InstantiationException e) {
            // json parase error
        } catch (IllegalAccessException e) {
            // json parase error
        }
        return null;
    }

    /**
     * json中是否包含指定的键
     *
     * @param keyName 键名称
     * @return true-包含;false-不包含;
     */
    public static boolean containerKey(String keyName, String jsonString) {
        if (ObjectJudge.isEmptyString(keyName)) {
            return false;
        }
        if (ObjectJudge.isEmptyJson(jsonString)) {
            return false;
        }
        String regex = "(((\"" + keyName + "\")|('" + keyName + "')):(.*?)((\\,|\\})(\\s\\S)*))";
        Matcher matcher = Pattern.compile(regex).matcher(jsonString);
        return matcher.find();
    }

    /**
     * 获取json指定key对应的值
     *
     * @param keyName    json键
     * @param jsonString json
     * @return 返回对应的值
     */
    public static String getValue(String keyName, String jsonString) {
        if (ObjectJudge.isEmptyString(keyName)) {
            return "";
        }
        String regex = "((\"" + keyName + "\")|('" + keyName + "')):(((\\[(.+)\\](\\,|\\}))|(\\{(.+)\\}(\\,|\\})))|((.*?)((\\,|\\})(\\s\\S)*)))";
        Matcher matcher = Pattern.compile(regex).matcher(jsonString);
        String value = "";
        while (matcher.find()) {
            //避免null出错
            value = (matcher.group() + "").trim();
            //根据:分隔
            int index = value.indexOf(":");
            if (index < 0 || (index + 1) == value.length()) {
                continue;
            }
            value = value.substring(index + 1).trim();
            int start = 0;
            if (value.startsWith("\"") || value.startsWith("'")) {
                start = 1;
            }
            //去掉前面引号
            //去掉最后一个字符包含的,或}
            value = value.substring(start, value.length() - 1);
            value = value.trim();
            //去掉后面引号
            int end = value.length();
            if (value.endsWith("\"") || value.endsWith("'")) {
                end -= 1;
            }
            value = value.substring(0, end);
            break;
        }
        return value;
    }

    /**
     * 获取json指定key对应的值
     *
     * @param relationKeys json键关系(如key或key->key1->key2),根据json关系指定
     * @param jsonString   json
     * @return 返回对应的值
     */
    public static String getAccurateValue(String relationKeys, String jsonString) {
        if (ObjectJudge.isEmptyString(relationKeys)) {
            return "";
        }
        if (ObjectJudge.isEmptyJson(jsonString)) {
            return "";
        }
        String[] split = relationKeys.split("->");
        if (ObjectJudge.isNullOrEmpty(split)) {
            return "";
        }
        String value = "";
        String subJson = jsonString;
        for (int i = 0; i < split.length; i++) {
            if ((i + 1) == split.length) {
                value = getValue(split[i], subJson);
            } else {
                subJson = getValue(split[i], subJson);
            }
        }
        return value;
    }
}
