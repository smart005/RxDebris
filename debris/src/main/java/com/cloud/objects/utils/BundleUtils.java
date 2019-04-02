package com.cloud.objects.utils;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import com.cloud.objects.ObjectJudge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/4/18
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BundleUtils {

    /**
     * bundle对象转为json
     *
     * @param bundle bundle
     * @return json
     */
    public static String toJson(Bundle bundle) {
        if (bundle == null) return null;
        JSONObject jsonObject = new JSONObject();
        for (String key : bundle.keySet()) {
            Object obj = bundle.get(key);
            try {
                jsonObject.put(key, wrap(bundle.get(key)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    private static Object wrap(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        if (o.equals(JSONObject.NULL)) {
            return o;
        }
        if (o instanceof Collection) {
            return new JSONArray((Collection) o);
        } else if (o.getClass().isArray()) {
            return toJSONArray(o);
        }
        if (o instanceof Map) {
            return new JSONObject((Map) o);
        }
        if (o instanceof Boolean ||
                o instanceof Byte ||
                o instanceof Character ||
                o instanceof Double ||
                o instanceof Float ||
                o instanceof Integer ||
                o instanceof Long ||
                o instanceof Short ||
                o instanceof String) {
            return o;
        }
        if (o.getClass().getPackage().getName().startsWith("java.")) {
            return o.toString();
        }
        return JSONObject.NULL;
    }

    private static JSONArray toJSONArray(Object array) {
        JSONArray result = new JSONArray();
        if (!array.getClass().isArray()) {
            return result;
        }
        final int length = Array.getLength(array);
        for (int i = 0; i < length; ++i) {
            result.put(wrap(Array.get(array, i)));
        }
        return result;
    }

    /**
     * json转换成Bundle对象
     *
     * @param jsonString json字符串
     * @return bundle
     */
    public static Bundle toBundle(String jsonString) {
        try {
            JSONObject jsonObject = toJsonObject(jsonString);
            return jsonToBundle(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Bundle();
    }

    private static JSONObject toJsonObject(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }

    private static Bundle jsonToBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object value = jsonObject.get(key);
            setBundleValue(bundle, key, value);
        }
        return bundle;
    }

    /**
     * 设置bundle值
     *
     * @param bundle bundle
     * @param key    键
     * @param value  值
     */
    public static void setBundleValue(Bundle bundle, String key, Object value) {
        if (value instanceof Boolean) {
            //boolean
            bundle.putBoolean(key, ObjectJudge.isTrue(value));
        } else if (value instanceof boolean[]) {
            //boolean[]
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof Byte) {
            //byte
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof Character) {
            //character
            bundle.putChar(key, (char) value);
        } else if (value instanceof Double) {
            //double
            bundle.putDouble(key, (Double) value);
        } else if (value instanceof double[]) {
            //double[]
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof Float) {
            //float
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof float[]) {
            //float[]
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof Integer) {
            //integer
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof int[]) {
            //Integer[]
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof Long) {
            //long
            bundle.putLong(key, (Long) value);
        } else if (value instanceof long[]) {
            //long[]
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof Short) {
            //short
            bundle.putShort(key, (Short) value);
        } else if (value instanceof short[]) {
            //short
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof String) {
            //string
            bundle.putString(key, (String) value);
        } else if (value instanceof String[]) {
            //string[]
            bundle.putStringArray(key, (String[]) value);
        } else if (value instanceof ArrayList) {
            //ArrayList
            ArrayList lst = (ArrayList) value;
            if (ObjectJudge.isNullOrEmpty(lst)) {
                return;
            }
            Object o = lst.get(0);
            if (o instanceof Integer) {
                bundle.putIntegerArrayList(key, lst);
            } else if (o instanceof String) {
                bundle.putStringArrayList(key, lst);
            } else if (o instanceof Parcelable) {
                bundle.putParcelableArrayList(key, lst);
            } else if (o instanceof CharSequence) {
                bundle.putCharSequenceArrayList(key, lst);
            }
        } else if (value instanceof Parcelable[]) {
            //Parcelable[]
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof Parcelable) {
            //Parcelable
            bundle.putParcelable(key, (Parcelable) value);
//            bundle.putBinder();
        } else if (value instanceof char[]) {
            //Character[]
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof CharSequence) {
            //CharSequence
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof CharSequence[]) {
            //CharSequence[]
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof Serializable) {
            //Serializable
            bundle.putSerializable(key, (Serializable) value);
        } else if (value instanceof byte[]) {
            //byte[]
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof IBinder) {
            //IBinder
            if (Build.VERSION.SDK_INT >= 18) {
                bundle.putBinder(key, (IBinder) value);
            }
        } else if (value instanceof SparseArray) {
            //SparseArray
            bundle.putSparseParcelableArray(key, (SparseArray) value);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                if (value instanceof Size) {
                    bundle.putSize(key, (Size) value);
                } else if (value instanceof SizeF) {
                    bundle.putSizeF(key, (SizeF) value);
                }
            }
        }
    }
}
