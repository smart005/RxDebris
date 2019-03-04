package com.cloud.objects.utils;

import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Func4;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/23
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class StringUtils {

    private static void intervalCharsWith(StringBuffer sb, String rawString, int start, int rlen, int tinum, int[] intervalNums, String intervalCharacter, Boolean isReverse) {
        for (int num : intervalNums) {
            if (num <= 0 || num >= rlen) {
                continue;
            }
            tinum += num;
            if (isReverse) {
                start += 1;
                if (start - num > 0) {
                    sb.insert(0, intervalCharacter + rawString.substring(start - num, start));
                } else {
                    sb.insert(0, rawString.substring(0, start));
                }
                start -= num;
            } else {
                if (start + num < rlen) {
                    sb.append(rawString.substring(start, start + num) + intervalCharacter);
                } else {
                    sb.append(rawString.substring(start, rawString.length()));
                }
                start += num;
            }
        }
        if (isReverse) {
            if (start > 0) {
                sb.insert(0, rawString.substring(0, start));
            }
        } else {
            if (start < rlen) {
                sb.append(rawString.substring(start));
            }
        }
    }

    private static void intervalCharsWith(StringBuffer sb, int rlen, int start, String rawString, Integer intervalNum, String intervalCharacter, Boolean isReverse) {
        if (isReverse) {
            start += 1;
            for (int i = rlen - intervalNum; i >= 0; i -= intervalNum) {
                if (i > 0) {
                    sb.insert(0, intervalCharacter + rawString.substring(i, start));
                } else {
                    break;
                }
                start = i;
            }
            if (start >= 0) {
                sb.insert(0, rawString.substring(0, start));
            }
        } else {
            for (int i = intervalNum; i <= rlen; i += intervalNum) {
                if (i < rlen) {
                    sb.append(rawString.substring(start, i) + intervalCharacter);
                } else {
                    break;
                }
                start = i;
            }
            if (start < rlen) {
                sb.append(rawString.substring(start));
            }
        }
    }

    /**
     * 在原字符串中每隔指定的位数中间插入固定的字符
     *
     * @param rawString         原字符串
     * @param intervalNums      间隔数数组(如果intervalNums.lenght>1则对intervalNums循环添加指定字符，如果intervalNums.lenght==1则对原字符串循环添加指定字符)
     * @param intervalCharacter 插入的特殊字符
     * @param isReverse         true-反向添加；false-从顺序添加;
     * @param startString       开始处理字符串
     * @return
     */
    public static String insertIntervalCharacter(String rawString, int[] intervalNums, String intervalCharacter, boolean isReverse, String startString) {
        if (TextUtils.isEmpty(rawString) || intervalNums == null || intervalNums.length == 0 || TextUtils.isEmpty(intervalCharacter)) {
            return rawString;
        }

        Func4<String, String, int[], String, Boolean> arrayWithFunc = new Func4<String, String, int[], String, Boolean>() {
            @Override
            public String call(String rawString, int[] intervalNums, String intervalCharacter, Boolean isReverse) {
                StringBuffer sb = new StringBuffer();
                int rlen = rawString.length();
                int tinum = 0;
                //开始处理索引
                int start = isReverse ? (rlen - 1) : 0;
                intervalCharsWith(sb, rawString, start, rlen, tinum, intervalNums, intervalCharacter, isReverse);
                return sb.toString();
            }
        };

        Func4<String, String, Integer, String, Boolean> chatWithFunc = new Func4<String, String, Integer, String, Boolean>() {
            @Override
            public String call(String rawString, Integer intervalNum, String intervalCharacter, Boolean isReverse) {
                rawString = rawString.trim();
                int rlen = rawString.length();
                if (rlen <= intervalNum) {
                    return rawString;
                }
                int start = isReverse ? (rlen - 1) : 0;
                StringBuffer sb = new StringBuffer();
                intervalCharsWith(sb, rlen, start, rawString, intervalNum, intervalCharacter, isReverse);
                return sb.toString();
            }
        };

        //去掉前后空格
        rawString = rawString.trim();
        StringBuffer sb = new StringBuffer();

        //根据开始字符和处理顺序重置原字符串
        if (!TextUtils.isEmpty(startString)) {
            //取开始索引在原字符串的索引
            int index = isReverse ? rawString.lastIndexOf(startString) : rawString.indexOf(startString);
            if (index >= 0) {
                sb.append(isReverse ? rawString.substring(index, rawString.length()) : rawString.substring(0, index));
                rawString = isReverse ? rawString.substring(0, index) : rawString.substring(index + 1, rawString.length());
            }
        }
        String withResult = "";
        if (intervalNums.length > 1) {
            withResult = arrayWithFunc.call(rawString, intervalNums, intervalCharacter, isReverse);
        } else {
            withResult = chatWithFunc.call(rawString, intervalNums[0], intervalCharacter, isReverse);
        }

        if (isReverse) {
            sb.insert(0, withResult);
        } else {
            sb.append(withResult);
        }

        return sb.toString();
    }

    /**
     * 在原字符串中每隔指定的位数中间插入固定的字符
     *
     * @param rawString         原字符串
     * @param intervalNum       间隔数
     * @param intervalCharacter 插入的特殊字符
     * @param isReverse         true-从顺序添加；false-反向添加;
     * @param startString       开始处理字符串
     * @return
     */
    public static String insertIntervalCharacter(String rawString, int intervalNum, String intervalCharacter, boolean isReverse, String startString) {
        int[] intervalNums = {intervalNum};
        return insertIntervalCharacter(rawString, intervalNums, intervalCharacter, isReverse, startString);
    }

    /**
     * 对金额每3位添加指定的间隔符
     *
     * @param rawMoney          金额
     * @param intervalCharacter 间隔符
     * @return 已处理金额
     */
    public static String moneyIntervalWith(String rawMoney, String intervalCharacter) {
        return insertIntervalCharacter(rawMoney, 3, intervalCharacter, true, ".");
    }

    /**
     * 对金额每3位添加指定的间隔符
     *
     * @param rawMoney          金额
     * @param intervalCharacter 间隔符
     * @return 已处理金额
     */
    public static String moneyIntervalWith(double rawMoney, String intervalCharacter) {
        return moneyIntervalWith(String.valueOf(rawMoney), intervalCharacter);
    }

    /**
     * 对金额每3位添加指定的间隔符（间隔符默认短号）
     *
     * @param rawMoney 金额
     * @return 已处理金额
     */
    public static String moneyIntervalWith(String rawMoney) {
        return moneyIntervalWith(rawMoney, ",");
    }

    /**
     * 对金额每3位添加指定的间隔符（间隔符默认短号）
     *
     * @param rawMoney 金额
     * @return 已处理金额
     */
    public static String moneyIntervalWith(double rawMoney) {
        return moneyIntervalWith(rawMoney, ",");
    }

    /**
     * 判断字符串中是否包含特定字符
     *
     * @param content    原字符串
     * @param characters 指定字符
     * @return true-包含;false-不包含;
     */
    public static boolean isContains(String content, String characters) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(characters)) {
            return false;
        }
        return content.contains(characters);
    }

    /**
     * 统计字符数
     *
     * @param text 统计字符串
     * @return 字符数(中文算两个字符)
     */
    public static int charaterCount(String text) {
        int count = 0;
        if (TextUtils.isEmpty(text)) {
            return count;
        }
        text = text.trim();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            if (ObjectJudge.isChinese(text.charAt(i))) {
                count += 2;
            } else {
                count++;
            }
        }
        return count;
    }

    /**
     * 截断字符
     *
     * @param text     处理文体
     * @param length   最大长度(以字符计)
     * @param endChars 末尾显示字符
     * @return
     */
    public static String ellipsize(String text, int length, String endChars) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        text = text.trim();
        int len = text.length();
        int count = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (ObjectJudge.isChinese(c)) {
                count += 2;
            } else {
                count++;
            }
            if (count <= length) {
                builder.append(c);
            } else {
                break;
            }
        }
        builder.append(endChars);
        return builder.toString();
    }
}
