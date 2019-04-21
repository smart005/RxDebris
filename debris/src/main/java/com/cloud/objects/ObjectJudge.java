/**
 * Title: ObjectJudge.java
 * Description:
 * author: lijinghuan
 * data: 2015年5月4日 上午7:16:34
 */
package com.cloud.objects;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.widget.EditText;

import com.cloud.objects.events.Action1;
import com.cloud.objects.storage.FilenameUtils;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.ValidUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class ObjectJudge {
    /**
     * 判断列表是否为空
     * <p>
     * param list 需要检测的列表集合
     * return
     */
    public static <T> Boolean isNullOrEmpty(T[] list) {
        if (list != null && list.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     *
     * @param list 需要检测的列表集合
     * @return true-空;false-非空;
     */
    public static Boolean isNullOrEmpty(SparseArray list) {
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 需要检测的列表集合
     * return
     */
    public static <K, V> Boolean isNullOrEmpty(TreeMap<K, V> list) {
        if (list != null && !list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param haslist 需要检测的列表集合
     * return
     */
    public static Boolean isNullOrEmpty(HashSet<?> haslist) {
        if (haslist != null && !haslist.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param haslist 需要检测的列表集合
     * return
     */
    public static Boolean isNullOrEmpty(HashMap<?, ?> haslist) {
        if (haslist != null && !haslist.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param map 需要检测的列表集合
     * return
     */
    public static Boolean isNullOrEmpty(Map<?, ?> map) {
        if (map != null && !map.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param haslist 需要检测的列表集合
     * return
     */
    public static Boolean isNullOrEmpty(Hashtable<?, ?> haslist) {
        if (haslist != null && !haslist.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 整型数据集合
     * return
     */
    public static Boolean isNullOrEmpty(int[] list) {
        if (list != null && list.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param longlist 长整型集合
     * return
     */
    public static Boolean isNullOrEmpty(long[] longlist) {
        if (longlist != null && longlist.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 列表集合
     * return
     */
    public static Boolean isNullOrEmpty(List<?> list) {
        if (list != null && !list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 数据集合
     * return
     */
    public static Boolean isNullOrEmpty(Collection<?> list) {
        if (list != null && !list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 数据列表
     * return
     */
    public static Boolean isNullOrEmpty(String[][] list) {
        if (list != null && list.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断服务是否正在运行
     * <p>
     * param context
     * param className
     * return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(100000);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 是否在后台运行
     * <p>
     * param context
     * return
     */
    public static boolean isBackgroundRunning(Context context) {
        try {
            String processName = context.getPackageName();
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager == null)
                return false;
            List<ActivityManager.RunningAppProcessInfo> processList = activityManager
                    .getRunningAppProcesses();
            if (ObjectJudge.isNullOrEmpty(processList)) {
                return false;
            }
            Boolean flag = false;
            for (ActivityManager.RunningAppProcessInfo process : processList) {
                if (process.processName.startsWith(processName)) {
                    boolean isBackground = process.importance != IMPORTANCE_FOREGROUND
                            && process.importance != IMPORTANCE_VISIBLE;
                    flag = isBackground;
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            return true;
        }
    }

    /***
     * 判断字符是否为中文
     * @param ch 需要判断的字符
     * @return 中文返回true，非中文返回false
     */
    public static boolean isChinese(char ch) {
        //获取此字符的UniCodeBlock
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        //  GENERAL_PUNCTUATION 判断中文的“号
        //  CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
        //  HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 是否为真
     * <p>
     * param object true或大于0都为真,否则为假
     * return
     */
    public static boolean isTrue(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Boolean) {
            return (boolean) object;
        } else {
            String flag = object.toString().trim().toLowerCase();
            if (TextUtils.equals(flag, "true")) {
                return true;
            } else if (ConvertUtils.toInt(flag) > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断是否银行卡卡号
     *
     * @param bankCard 卡号
     * @return
     */
    public static boolean isBankCard(String bankCard) {
        if (bankCard == null) {
            return false;
        }
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0 || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断字符串是否为空
     *
     * @param content
     * @return
     */
    public static boolean isEmptyString(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        } else {
            content = content.toLowerCase();
            if (TextUtils.equals(content, "null") ||
                    TextUtils.equals(content, "(null)")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断activity是否正在运行
     *
     * @param context          上下文
     * @param className        短类名或长类名
     * @param isShortClassName true-表示className为短类名;false-表示className为长类名;
     * @return
     */
    public static boolean isRunningActivity(Context context, String className, boolean isShortClassName) {
        if (TextUtils.isEmpty(className)) {
            return false;
        }
        Context applicationContext = context.getApplicationContext();
        String packageName = applicationContext.getPackageName();
        if (!isShortClassName) {
            className = FilenameUtils.getName(className);
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100000);
        boolean isRunning = false;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (TextUtils.equals(info.topActivity.getShortClassName(), className) ||
                    TextUtils.equals(info.baseActivity.getShortClassName(), className)) {
                if (info.topActivity.getPackageName().equals(packageName) || info.baseActivity.getPackageName().equals(packageName)) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }

    /**
     * 是否包含sdk卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断广播是否已经注册
     *
     * @param context
     * @param action  广播action
     * @return
     */
    public static boolean isRegisterBroadcast(Context context, String action) {
        if (context == null || TextUtils.isEmpty(action)) {
            return false;
        }
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(action);
        List<ResolveInfo> resolveInfos = manager.queryBroadcastReceivers(intent, 0);
        return !ObjectJudge.isNullOrEmpty(resolveInfos);
    }

    /**
     * 是否匹配字段和属性值
     *
     * @param t
     * @param field         属性字段
     * @param propertyValue 属性值
     * @param <T>
     * @return
     */
    public static <T> boolean isMatchPropertyValue(T t, String field, Object propertyValue) {
        if (t == null || TextUtils.isEmpty(field) || propertyValue == null) {
            return false;
        }
        Object value = GlobalUtils.getPropertiesValue(t, field);
        if (value == null) {
            return false;
        }
        if (value instanceof String ||
                value instanceof Integer ||
                value instanceof Double ||
                value instanceof Float) {
            String old = String.valueOf(value);
            String now = String.valueOf(propertyValue);
            return TextUtils.equals(String.valueOf(value), String.valueOf(propertyValue));
        } else {
            return value == propertyValue;
        }
    }

    /**
     * 实体对象是否匹配所有的字段和属性值
     *
     * @param t
     * @param fieldValues 字段和属性值
     * @param <T>
     * @return
     */
    public static <T> boolean isMatchPropertyValues(T t, LinkedHashMap<String, String> fieldValues) {
        if (ObjectJudge.isNullOrEmpty(fieldValues)) {
            return false;
        }
        int count = 0;
        for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
            boolean isMatch = isMatchPropertyValue(t, entry.getKey(), entry.getValue());
            if (isMatch) {
                count++;
            }
        }
        return count == fieldValues.size();
    }

    /**
     * 监听所有编辑文本是否非空并在回调中返回
     *
     * @param action    检测完成后回调
     * @param editTexts 编辑文本集合
     */
    public static void checkAllEditTextNotEmpty(Action1<Boolean> action, EditText... editTexts) {
        if (action == null || ObjectJudge.isNullOrEmpty(editTexts)) {
            return;
        }
        HashMap<Integer, String> maps = new HashMap<Integer, String>();
        class InputTextWatcher implements TextWatcher {

            private int position = 0;
            private HashMap<Integer, String> maps = null;
            private Action1<Boolean> action = null;

            public InputTextWatcher(int position, HashMap<Integer, String> maps, Action1<Boolean> action) {
                this.position = position;
                this.maps = maps;
                this.action = action;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (maps == null) {
                    return;
                }
                maps.put(position, text == null ? "" : text.toString());
                boolean flag = true;
                for (Map.Entry<Integer, String> entry : maps.entrySet()) {
                    if (TextUtils.isEmpty(entry.getValue())) {
                        flag = false;
                        break;
                    }
                }
                action.call(flag);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
        for (int i = 0; i < editTexts.length; i++) {
            EditText editText = editTexts[i];
            maps.put(i, "");
            editText.addTextChangedListener(new InputTextWatcher(i, maps, action));
        }
    }

    /**
     * 判断当前执行的方法或线程是否属于主线程
     *
     * @return true-主线程;false-非主线程;
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 判断某项条件是否被枚举集合所包含
     *
     * @param condition   条件
     * @param collections 枚举集合
     * @return true-包含;false-不包含;
     */
    public static boolean isContains(String condition, String... collections) {
        if (TextUtils.isEmpty(condition) || isNullOrEmpty(collections)) {
            return false;
        }
        List<String> lst = Arrays.asList(collections);
        boolean contains = lst.contains(condition);
        return contains;
    }

    /**
     * 判断某项条件是否被枚举集合所包含
     *
     * @param condition   条件
     * @param collections 枚举集合
     * @return true-包含;false-不包含;
     */
    public static boolean isContains(int condition, Integer... collections) {
        if (isNullOrEmpty(collections)) {
            return false;
        }
        List<Integer> lst = Arrays.asList(collections);
        return lst.contains(condition);
    }

    /**
     * 检测json字符串是否符合
     *
     * @param json json字符串
     * @return true-json;false-非json;
     */
    public static boolean isJson(String json) {
        if (ObjectJudge.isEmptyString(json)) {
            return false;
        }
        //判断是否为json格式{...}或[...]
        String regex = "^(\\{(.+)*\\})$|^(\\[(.+)*\\])$";
        if (ValidUtils.valid(regex, json)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测json字符串是否为空
     *
     * @param json json字符串
     * @return true-空;false-非空;
     */
    public static boolean isEmptyJson(String json) {
        if (ObjectJudge.isEmptyString(json)) {
            return true;
        }
        //判断是否为json格式{...}或[...]
        String regex = "^(\\{(.+)*\\})$|^(\\[(.+)*\\])$";
        if (ValidUtils.valid(regex, json)) {
            //如果对象直接包含数组如{["id",3,"name":"名称"]}
            regex = "^(\\{\\[)(.+)(\\]\\})$";
            if (ValidUtils.valid(regex, json)) {
                return false;
            } else {
                //如果数组中包含对象
                regex = "^(\\[\\{)(.+)(\\}\\])$";
                if (ValidUtils.valid(regex, json)) {
                    return false;
                } else {
                    //如果只对象
                    HashMap map = JsonUtils.parseT(json, HashMap.class);
                    if (map != null && map.size() > 0) {
                        return false;
                    } else {
                        int length = json.replace(" ", "").trim().length();
                        if (length > 2) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } else {
            return true;
        }
    }

    /**
     * 在json中是否包含指定的键
     *
     * @param keyName    键名称
     * @param jsonString json
     * @return true-包含;false-不包含;
     */
    public static boolean isContainerJsonKey(String keyName, String jsonString) {
        return JsonUtils.containerKey(keyName, jsonString);
    }

    /**
     * 对应值是否在匹配列表中
     *
     * @param array      匹配列表
     * @param matchValue 被匹配的值
     * @return true-在匹配项中,反之不在;
     */
    public static boolean isMatch(int[] array, int matchValue) {
        if (isNullOrEmpty(array)) {
            return false;
        }
        //不用Arrays.asList处理
        for (int i : array) {
            if (i == matchValue) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对应值是否在匹配列表中
     *
     * @param array      匹配列表
     * @param matchValue 被匹配的值
     * @return true-在匹配项中,反之不在;
     */
    public static boolean isMatch(String[] array, String matchValue) {
        if (isNullOrEmpty(array)) {
            return false;
        }
        //不用Arrays.asList处理
        for (String s : array) {
            if (TextUtils.equals(s, matchValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数组中的字符串是否包含有空值
     *
     * @param values 字符串数组
     * @return true-所有元素均不为空,反之false;
     */
    public static boolean isEmpty(String... values) {
        if (isNullOrEmpty(values)) {
            return true;
        }
        for (String value : values) {
            if (TextUtils.isEmpty(value)) {
                return true;
            }
        }
        return false;
    }
}
