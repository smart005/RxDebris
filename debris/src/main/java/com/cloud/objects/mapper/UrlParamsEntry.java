package com.cloud.objects.mapper;

import android.text.TextUtils;

import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.ValidUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-04-29
 * Description:url参数映射
 * Modifier:
 * ModifyContent:
 */
public class UrlParamsEntry {

    private String url;
    private String relativeUrl;

    /**
     * url参数mapping
     *
     * @param url 当前需要处理参数的url
     */
    public void mapper(String url) {
        this.url = url;
        if (TextUtils.isEmpty(url)) {
            return;
        }
        int index = url.indexOf("?");
        if (index < 0) {
            return;
        }
        this.relativeUrl = url.substring(index + 1);
    }

    /**
     * 获取url
     *
     * @return url
     */
    public String getUrl() {
        return this.url == null ? "" : this.url;
    }

    /**
     * 获取url参数
     *
     * @param paramName 参数名
     * @return 参数值
     */
    public String getParams(String paramName) {
        if (TextUtils.isEmpty(paramName)) {
            return "";
        }
        String pattern = String.format("(^|)%s=([^&]*)(|$)", paramName);
        String matche = ValidUtils.matche(pattern, this.relativeUrl);
        String[] split = matche.split("=");
        if (split.length != 2) {
            return "";
        }
        return split[1];
    }

    /**
     * 获取int参数值
     *
     * @param paramName 参数名
     * @return 参数值
     */
    public int getIntParams(String paramName) {
        String params = getParams(paramName);
        if (TextUtils.isDigitsOnly(params)) {
            return ConvertUtils.toInt(params);
        }
        return 0;
    }

    /**
     * 获取参数map
     *
     * @return 参数集合
     */
    public HashMap<String, String> getParamsMap() {
        HashMap<String, String> map = new HashMap<>();
        String pattern = "(\\w+)=([^&]*)";
        List<String> matches = ValidUtils.matches(pattern, this.relativeUrl);
        for (String match : matches) {
            if (match == null) {
                continue;
            }
            String[] split = match.split("=");
            if (split.length != 2) {
                map.put(split[0], "");
            } else {
                map.put(split[0], split[1]);
            }
        }
        return map;
    }

    /**
     * 检测是否包含参数
     *
     * @param key 参数名
     * @return true-包含;false-不包含;
     */
    public boolean containsKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        String patten = String.format("(^|)%s=", key);
        return ValidUtils.valid(patten, this.relativeUrl);
    }
}
