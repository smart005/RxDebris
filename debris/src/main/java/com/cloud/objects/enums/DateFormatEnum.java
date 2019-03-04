package com.cloud.objects.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2015-6-19 上午8:44:24
 * Description: 日期格式
 * Modifier:
 * ModifyContent:
 */
public enum DateFormatEnum {

    /**
     * yyyy{0}MM
     */
    YYYYMM("yyyy{0}MM"),
    /**
     * yyyyMM
     */
    YYYYMMNC("yyyyMM"),
    /**
     * yy{0}MM{0}dd
     */
    YYMMDD("yy{0}MM{0}dd"),
    /**
     * yyyy{0}MM{0}dd
     */
    YYYYMMDD("yyyy{0}MM{0}dd"),
    /**
     * _年_月_日
     */
    YYYY年MM月DD日("yyyy年MM月dd日"),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    YYYYMMDDHHMMSS("yyyy-MM-dd HH:mm:ss"),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    YYMMDDHHMMSS("yy-MM-dd HH:mm:ss"),
    /**
     * _年_月_日 _时_分_秒
     */
    YYYY年MM月DD日HH时MM分SS秒("yyyy年MM月dd日 HH时mm分ss秒"),
    /**
     * 月{0}日
     */
    MMDD("MM{0}dd"),
    /**
     * 小时:分
     */
    HHmm("HH:mm"),
    /**
     * 月-日 时-分
     */
    MMDDHHMM("MM-dd HH:mm");

    private String value = "";

    private DateFormatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
