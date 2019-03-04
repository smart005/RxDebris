package com.cloud.objects.logs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cloud.objects.config.RxAndroid;
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
                    .methodCount(1)
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

    /**
     * debug日志
     *
     * @param tag    当前日志标签；示例[全局标签-tag]
     * @param object 基础数据结构对象
     */
    public static void debug(String tag, @NonNull Object object) {
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
            return;
        }
        printer(tag).d(object);
    }

    /**
     * debug日志
     *
     * @param object 基础数据结构对象
     */
    public static void debug(@NonNull Object object) {
        debug("", object);
    }

    /**
     * debug日志
     *
     * @param tag     当前日志标签；示例[全局标签-tag]
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void debug(String tag, @NonNull String message, @Nullable Object... args) {
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
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
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
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
    public static void info(String tag, @NonNull String message, @Nullable Object... args) {
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
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
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
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
     * 记录对象输入日志(如文件流)
     *
     * @param tag     当前日志标签；示例[全局标签-tag]
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void write(String tag, @NonNull String message, @Nullable Object... args) {
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
            return;
        }
        printer(tag).w(message, args);
    }

    /**
     * 记录对象输入日志(如文件流)
     *
     * @param message 消息
     * @param args    基础数据结构对象
     */
    public static void write(@NonNull String message, @Nullable Object... args) {
        write("", message, args);
    }

    /**
     * 记录json日志
     *
     * @param tag  当前日志标签；示例[全局标签-tag]
     * @param json json内容
     */
    public static void json(String tag, @Nullable String json) {
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
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
        RxAndroid.RxAndroidBuilder androidBuilder = RxAndroid.getInstance().getBuilder();
        if (!androidBuilder.isDebug()) {
            //线上消息不需要输入日志信息
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
