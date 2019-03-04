package com.cloud.objects.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2015-6-15 上午10:08:16
 * Description:常用验证规则
 * Modifier:
 * ModifyContent:
 */
public enum RuleParams {

    /**
     * 手机号
     */
    Phone("^1\\d{10}$"),
    /**
     * email
     */
    Email("^[\\w]+[@][\\w]+[\\.][\\w]+$"),
    /**
     * 验证金额
     */
    ValidMoney("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$"),
    /**
     * 过滤金额
     */
    FilterMoney("(([1-9]\\d{0,9})|0)(\\.\\d{1,})?"),
    /**
     * url或file:///本地文件路径验证
     */
    Url("^(http|https|file|rtsp|mms)://[/]?(([\\w-]+\\.)+)?[\\w-]+(:[0-9]{2,})*(/[\\w-./?%&=,@!~`#$%^&*,./_+|!:,.;]*)?$"),
    /**
     * 匹配指定标记(开始结束标签必须配对)
     * {0}、{1}分别代表标签、属性名
     */
    MatchThisTagAttr(
            "<%1$s(\\S*?) [^>]*>*?[^>]*?%2$s=([^\"]*)\"[^>]*>([\\s\\S]*?)</%1$s>"),
    /**
     * 匹配指定标记及属性值(开始结束标签必须配对)
     * {0}、{1}分别代表标签、属性名
     * 取值:patter.group("属性名")
     * patter.group("text")
     */
    MatchThisTagValue("(?<%2$s>(?<=<%1$s %2$s ?= ?\\\"?)[^\\\">]+?(?=\\\"|>)).*(?<text>(?<=>)[^<]+?(?=</%1$s>))"),
    /**
     * 获取标记之间内容 {0}、{1}分别代表开始结束标记
     */
    MatchTagBetweenContent("(?<=%s)([.\\S\\s]*)(?=%s)"),

    /**
     * 是否是英文字母
     */
    MatchEnglishLetters("^[A-Za-z]"),
    /**
     * 身份证正则
     */
    IDCard("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)"),
    /**
     * 匹配域名
     */
    MatchDomain("((?<=://)[a-zA-Z\\.0-9]+(?=\\/))|(?<=://)[a-zA-Z\\.0-9]+(?=)"),
    /**
     * 匹配url参数
     */
    MatchUrlParams("(?i)[^\\?&]?%s=[^&]+");

    private String value = "";

    RuleParams(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
