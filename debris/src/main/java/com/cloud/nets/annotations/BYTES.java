package com.cloud.nets.annotations;

import com.cloud.objects.annotations.OriginalField;
import com.cloud.objects.enums.RequestContentType;

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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BYTES {
    /**
     * 相对地址
     * <p>
     * return
     */
    String value() default "";

    /**
     * 相对地址库
     * <p>
     * return
     */
    UrlItem[] values() default {};

    /**
     * 若为true,则默认不添加此请求下的所有字段
     * <p>
     * return
     */
    boolean isRemoveEmptyValueField() default false;

    /**
     * 是否完整url
     * <p>
     * return
     */
    boolean isFullUrl() default false;

    /**
     * 数据提交方式
     * <p>
     * return
     */
    RequestContentType contentType() default RequestContentType.None;

    /**
     * 若ApiCheckAnnotation中的IsTokenValid=true时才有效
     * true:接口返回后需要做登录检验;false:忽略检验;
     * <p>
     * return
     */
    boolean isLoginValid() default true;

    /**
     * 是否输出api日志
     * <p>
     * return
     */
    boolean isPrintApiLog() default false;

    /**
     * 是否验证回调结果
     * true:对返回的结果做以下验证:
     * 1.登录;
     * 2.api异常;
     * false:只对登录做过滤其它不处理;
     *
     * @return
     */
    boolean isValidCallResult() default true;

    /**
     * 请求失败时是否重试(默认false)
     * 若为true则在{@link com.cloud.nets.properties.OkRxConfigParams}.retryCount之后，
     * 每次请求后延时间增加5秒；最多重试100;
     *
     * @return true-请求失败时自动重新请求直到成功为止,false-只请求一次;
     */
    boolean isFailureRetry() default false;

    /**
     * 是否关联赋值
     *
     * @return true-解析后对带有{@link OriginalField}注解的属性进行关联赋值,false-不处理此类逻辑;
     */
    boolean isAssociatedAssignment() default false;
}
