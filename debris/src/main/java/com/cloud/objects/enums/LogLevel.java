package com.cloud.objects.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/11
 * Description:日志级别
 * Modifier:
 * ModifyContent:
 */
public enum LogLevel {
    /**
     * 调试日志
     */
    debug,
    /**
     * 控制台信息类日志
     */
    info,
    /**
     * 版本相关日志
     */
    version,
    /**
     * json格式日志
     */
    json,
    /**
     * xml格式日志
     */
    xml,
    /**
     * 警告类日志
     */
    warn,
    /**
     * 错误类日志(normal)
     */
    error,
    /**
     * 微小错误日志
     */
    errorTrivial,
    /**
     * 主要错误日志
     */
    errorMajor,
    /**
     * 致命错误日志
     */
    errorDeadly
}
