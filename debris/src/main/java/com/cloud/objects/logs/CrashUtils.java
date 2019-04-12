package com.cloud.objects.logs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:crash信息
 * Modifier:
 * ModifyContent:
 */
public class CrashUtils {
    /**
     * 获取错误信息
     *
     * @param throwable 堆栈
     * @return 转换成string后的堆栈信息
     */
    public static String getCrashInfo(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        // printStackTrace(PrintWriter s)
        // 将此 throwable 及其追踪输出到指定的 PrintWriter
        throwable.printStackTrace(printWriter);
        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
        return result;
    }
}
