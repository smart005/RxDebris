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
     * 上报debug日志
     */
    reportDebug,
    /**
     * 控制台信息类日志
     */
    info,
    /**
     * 上报info日志
     */
    reportInfo,
    /**
     * 版本相关日志
     */
    version,
    /**
     * 上报version日志
     */
    reportVersion,
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
     * 上报警告信息
     */
    reportWarn,
    /**
     * 错误类日志(normal)
     */
    error,
    /**
     * 上报error日志
     */
    reportError
}
