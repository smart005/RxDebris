package com.cloud.objects.logs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.enums.LogLevel;
import com.cloud.objects.events.OnLogPrinterListener;
import com.cloud.objects.logs.lart.AndroidLogAdapter;
import com.cloud.objects.logs.lart.FormatStrategy;
import com.cloud.objects.logs.lart.PrettyFormatStrategy;
import com.cloud.objects.logs.lart.Printer;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/27
 * Description:基于orhanobut的日志处理
 * Modifier:
 * ModifyContent:
 */
public class Logger {

    private static FormatStrategy formatStrategy;

    private static FormatStrategy getFormatStrategy() {
        if (formatStrategy == null) {
            RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
            String loggeruTag = androidBuilder.getLoggeruTag();

            PrettyFormatStrategy.Builder builder = PrettyFormatStrategy.newBuilder();
            formatStrategy = builder.showThreadInfo(true)
                    .methodCount(5)
                    .methodOffset(0)
                    .tag(loggeruTag)
                    .build();
        }
        return formatStrategy;
    }

    private static Printer printer(String tag) {
        com.cloud.objects.logs.lart.Logger.clearLogAdapters();
        FormatStrategy formatStrategy = getFormatStrategy();
        Printer printer = com.cloud.objects.logs.lart.Logger.t(tag);
        printer.addAdapter(new AndroidLogAdapter(formatStrategy));
        return printer;
    }

    private static boolean logIntercept(String tag, LogLevel level, String message, Throwable throwable, Object... args) {
        RxAndroid rxAndroid = RxAndroid.getInstance();
        RxAndroid.RxAndroidBuilder builder = rxAndroid.getBuilder();
        if (!builder.isDebug()) {
            //线上消息需要监听做相应记录
            OnLogPrinterListener logPrinterListener = rxAndroid.getOnLogPrinterListener();
            if (logPrinterListener != null) {
                //如果tag为空则取统一日志tag名
                if (TextUtils.isEmpty(tag)) {
                    tag = builder.getLoggeruTag();
                }
                StringBuilder logBuilder = new StringBuilder();
                //追加参数
                if (!ObjectJudge.isNullOrEmpty(args)) {
                    logBuilder.append(String.format(message, args));
                } else {
                    logBuilder.append(message);
                }
                if (throwable != null) {
                    String crashInfo = CrashUtils.getCrashInfo(throwable);
                    logBuilder.append("\n");
                    logBuilder.append(crashInfo);
                }
                logPrinterListener.onLogPrinter(tag, level, logBuilder.toString());
            }
            return true;
        }
        return false;
    }

//    /**
//     * debug日志
//     *
//     * @param tag    当前日志标签；示例[全局标签-tag]
//     * @param object 基础数据结构对象
//     */
//    public static void debug(String tag, @NonNull Object object) {
//        printer(tag).d(object);
//    }

//    /**
//     * debug日志
//     *
//     * @param object 基础数据结构对象
//     */
//    public static void debug(@NonNull Object object) {
//        debug("", object);
//    }

    /**
     * debug日志
     *
     * @param tag     当前日志标签；示例[全局标签-tag]
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void debug(String tag, @NonNull String message, @Nullable Object... args) {
        if (logIntercept(tag, LogLevel.debug, message, null, args)) {
            return;
        }
        printer(tag).d(message, args);
    }

    /**
     * debug日志
     *
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void debug(@NonNull String message, @Nullable Object... args) {
        if (logIntercept("", LogLevel.debug, message, null, args)) {
            return;
        }
        debug("", message, args);
    }

    /**
     * 错误日志
     *
     * @param tag       当前日志标签；示例[全局标签-tag]
     * @param throwable 栈信息
     * @param message   消息
     * @param args      基础数据结构对象
     */
    public static void error(String tag, @NonNull Throwable throwable, @NonNull String message, @Nullable Object... args) {
        if (logIntercept(tag, LogLevel.error, message, throwable, args)) {
            return;
        }
        printer(tag).e(throwable, message, args);
    }

    /**
     * 错误日志
     *
     * @param throwable 栈信息
     * @param message   消息
     * @param args      基础数据结构对象
     */
    public static void error(@NonNull Throwable throwable, @NonNull String message, @Nullable Object... args) {
        error("", throwable, message, args);
    }

    /**
     * 错误日志
     *
     * @param throwable 栈信息
     */
    public static void error(@NonNull Throwable throwable) {
        error(throwable, "");
    }

    /**
     * 错误日志
     *
     * @param tag     当前日志标签；示例[全局标签-tag]
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void error(String tag, @NonNull String message, @Nullable Object... args) {
        error(tag, null, message, args);
    }

    /**
     * 错误日志
     *
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void error(@NonNull String message, @Nullable Object... args) {
        error("", message, args);
    }

    /**
     * 信息日志
     *
     * @param tag     当前日志标签；示例[全局标签-tag]
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void info(String tag, String message, Object... args) {
        if (logIntercept(tag, LogLevel.info, message, null, args)) {
            return;
        }
        printer(tag).i(message, args);
    }

    /**
     * 信息日志
     *
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void info(@NonNull String message, @Nullable Object... args) {
        info("", message, args);
    }

    /**
     * 版本记录日志
     *
     * @param tag     当前日志标签；示例[全局标签-tag]
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void version(String tag, @NonNull String message, @Nullable Object... args) {
        if (logIntercept(tag, LogLevel.version, message, null, args)) {
            return;
        }
        printer(tag).v(message, args);
    }

    /**
     * 版本记录日志
     *
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void version(@NonNull String message, @Nullable Object... args) {
        version("", message, args);
    }

    /**
     * 警告类日志
     *
     * @param tag     当前日志标签；示例[全局标签-tag]
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void warn(String tag, @NonNull String message, @Nullable Object... args) {
        if (logIntercept(tag, LogLevel.warn, message, null, args)) {
            return;
        }
        printer(tag).w(message, args);
    }

    /**
     * 警告类日志
     *
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void warn(@NonNull String message, @Nullable Object... args) {
        warn("", message, args);
    }

    /**
     * 记录json日志
     *
     * @param tag  当前日志标签；示例[全局标签-tag]
     * @param json json内容
     */
    public static void json(String tag, @Nullable String json) {
        if (logIntercept(tag, LogLevel.json, json, null, null)) {
            return;
        }
        printer(tag).json(json);
    }

    /**
     * 记录json日志
     *
     * @param json json内容
     */
    public static void json(@Nullable String json) {
        json("", json);
    }

    /**
     * 记录xml日志
     *
     * @param tag 当前日志标签；示例[全局标签-tag]
     * @param xml xml内容
     */
    public static void xml(String tag, @Nullable String xml) {
        if (logIntercept(tag, LogLevel.xml, xml, null, null)) {
            return;
        }
        printer(tag).xml(xml);
    }

    /**
     * 记录xml日志
     *
     * @param xml xml内容
     */
    public static void xml(@Nullable String xml) {
        xml("", xml);
    }
}
