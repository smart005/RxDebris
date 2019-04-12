package com.cloud.objects.utils;

import com.cloud.objects.ObjectJudge;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    /**
     * 将对象转为json
     *
     * @param object 要转换的对象
     * @return json
     */
    public static String toStr(Object object) {
        if (object == null) {
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * 解析json数据至对象
     *
     * @param json  json数据
     * @param clazz 类对象
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T parseT(String json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return newNull(clazz);
        }
    }

    /**
     * 解析json数据至对象
     *
     * @param json  json数据
     * @param clazz 类对象
     * @param <T>   泛型
     * @return 对象
     */
    @Deprecated
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 将json数据解析成列表
     *
     * @param json  json数据
     * @param clazz 类class对象
     * @param <T>   泛型
     * @return 集合
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            Type type = new JsonParameterizedType(clazz);
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            return new ArrayList<T>(0);
        }
    }

    /**
     * 根据class实例化对象
     *
     * @param clazz 类class对象
     * @param <T>   泛型
     * @return 对象
     */
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

    private static class JsonParameterizedType implements ParameterizedType {
        Class clazz;

        public JsonParameterizedType(Class clz) {
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
}
