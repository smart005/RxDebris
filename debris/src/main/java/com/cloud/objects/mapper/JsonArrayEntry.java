package com.cloud.objects.mapper;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.utils.ConvertUtils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-04-28
 * Description:json数组对象处理
 * Modifier:
 * ModifyContent:
 */
public class JsonArrayEntry {

    private JSONArray jsonArray;

    /**
     * JsonArrayEntry构造函数必须传入json数组
     *
     * @param jsonArrayString json数组
     */
    public JsonArrayEntry(String jsonArrayString) {
        jsonArray = ConvertUtils.toJSONArray(jsonArrayString);
    }

    /**
     * 是否空jsonArray
     *
     * @return true-空或长度==0,otherwise false;
     */
    public boolean isEmptyArray() {
        if (jsonArray == null) {
            return true;
        }
        return ObjectJudge.isNullOrEmpty(jsonArray);
    }

    /**
     * 获取数组长度
     *
     * @return json array length
     */
    public int length() {
        if (jsonArray == null) {
            return 0;
        }
        return jsonArray.length();
    }

    /**
     * 获取JSONArray数据项
     *
     * @param position 索引
     * @return JSONArray数据项对应的json
     */
    public String getItemJson(int position) {
        if (jsonArray == null) {
            return "";
        }
        try {
            return jsonArray.getString(position);
        } catch (JSONException e) {
            return "";
        }
    }
}
