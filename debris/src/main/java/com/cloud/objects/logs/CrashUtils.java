package com.cloud.objects.logs;

import android.os.Build;

import com.cloud.objects.utils.ConvertUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 收集程序或设备信息
     */
    public static Map<String, Object> getProgramDeviceInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        //development codename
        map.put("BASE_OS", Build.VERSION.CODENAME);
        //散列值
        map.put("INCREMENTAL", Build.VERSION.INCREMENTAL);
        //系统版本
        map.put("OS_VERSION_NAME", Build.VERSION.RELEASE);
        //系统版本号
        map.put("OS_VERSION_CODE", Build.VERSION.SDK_INT);
        //无线电固件版本
        map.put("RADIO_VERSION", Build.getRadioVersion());
        //与产品/硬件相关联的消费者可见品牌
        map.put("BRAND", Build.BRAND);
        //设备名称
        map.put("DEVICE", Build.DEVICE);
        //用于显示给用户的构建ID字符串
        map.put("BUILD_ID", Build.DISPLAY);
        //设备硬件
        map.put("HARDWARE", Build.HARDWARE);
        //产品/硬件的制造商
        map.put("MANUFACTURER", Build.MANUFACTURER);
        //整个产品的名称
        map.put("PRODUCT_NAME", Build.PRODUCT);
        //package tags,like "unsigned,debug"
        map.put("PACKAGE_TAG", Build.TAGS);
        if (Build.VERSION.SDK_INT >= 21) {
            //支持32abis
            map.put("SUPPORTED_32_ABIS", ConvertUtils.toJoin(Build.SUPPORTED_32_BIT_ABIS, ","));
            //支持64abis
            map.put("SUPPORTED_64_ABIS", ConvertUtils.toJoin(Build.SUPPORTED_64_BIT_ABIS, ","));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            //操作系统
            map.put("BASE_OS", Build.VERSION.BASE_OS);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            //设备序列号
            map.put("SERIAL", Build.getSerial());
        }
        return map;
    }
}
