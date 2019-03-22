package com.cloud.nets.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/6
 * Description:
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value() default "";

    /**
     * 若该请求字段值为空,则请求时不添加该字段
     * <p>
     * return
     */
    boolean isRemoveEmptyValueField() default true;

    /**
     * true
     * 1.file/int/double/float/long/byte/string类型作为普通参数提交;
     * --value()为空
     * ----1.参数个数1个,对象或集合类型转为json作为本次提交的数据;
     * ----2.参数个数大于1且含有普通参数时,则忽略此类参数;
     * ----3.所有参数该值均为空则把所有参数转为数组以json形式作为本次提交的数据;
     * --value()不为空
     * ----1.作为普通参数方式提交;
     * false 文件或普通参数类型;
     *
     * @return
     */
    boolean isJson() default false;

    /**
     * 上传后文件后缀
     * 在网络请求时以此文件后缀上传
     *
     * @return 文件后缀
     */
    String fileSuffixAfterUpload() default "";

    /**
     * 是否目标文件
     *
     * @return true-当responseDataType()为byteData或stream类型时,则最终将转换为当前参数提供的文件;
     * false-根据接口指定的数据类型返回;
     */
    boolean isTargetFile() default false;
}
