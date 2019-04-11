package com.cloud.objects.events;

import com.cloud.objects.enums.LogLevel;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/11
 * Description:日志打印监听
 * Modifier:
 * ModifyContent:
 */
public interface OnLogPrinterListener {

    /**
     * 日志打印监听
     *
     * @param tag     日志标签
     * @param level   日志级别
     * @param message 日志内容
     */
    public void onLogPrinter(String tag, LogLevel level, String message);
}
