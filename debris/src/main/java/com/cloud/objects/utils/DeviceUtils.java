package com.cloud.objects.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import com.cloud.objects.beans.DeviceInfo;
import com.cloud.objects.logs.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/12
 * Description:设备信息工具类
 * Modifier:
 * ModifyContent:
 */
public class DeviceUtils {

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
        map.put("OS_VERSION_CODE", String.valueOf(Build.VERSION.SDK_INT));
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
            map.put("BASE_OS_2", Build.VERSION.BASE_OS);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            //设备序列号
            map.put("SERIAL", Build.getSerial());
        }
        return map;
    }

    /**
     * 获取CPU序列号
     * <p>
     * return CPU序列号(16位) 读取失败为""
     */
    private static String getCPUSerial() {
        String str = "", strCPU = "", cpuAddress = "";
        try {
            // 读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            // 查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    // 查找到序列号所在行
                    if (str.contains("Serial")) {
                        strCPU = str.substring(str.indexOf(":") + 1);
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.error(ex);
        }
        return cpuAddress;
    }

    /**
     * 获取设备信息
     *
     * @param context
     * @return
     */
    public static DeviceInfo getDeviceInfo(Context context) {
        DeviceInfo dvinfo = new DeviceInfo();
        dvinfo.setImei(getCPUSerial());
        if (TextUtils.isEmpty(dvinfo.getImei())) {
            dvinfo.setImei(Build.SERIAL);
        }
        if (TextUtils.isEmpty(dvinfo.getImei())) {
            dvinfo.setImei(dvinfo.getMac());
        }
        if (TextUtils.isEmpty(dvinfo.getImei())) {
            dvinfo.setImei(dvinfo.getImsi());
        }
        if (TextUtils.isEmpty(dvinfo.getImei())) {
            dvinfo.setImei(dvinfo.getSimSerialNumber());
        }
        dvinfo.setModel(Build.MODEL);
        dvinfo.setRelease(Build.VERSION.RELEASE);
        dvinfo.setSerialNumber(Build.SERIAL);
        if (TextUtils.isEmpty(dvinfo.getImei())) {
            dvinfo.setImei(dvinfo.getSerialNumber());
        }
        dvinfo.setSdkVersion(Build.VERSION.SDK_INT);
        validBind(dvinfo, context);
        return dvinfo;
    }

    @SuppressLint("HardwareIds")
    private static void validBind(DeviceInfo deviceInfo, Context context) {
        try {
            if (TextUtils.isEmpty(deviceInfo.getImei())) {
                deviceInfo.setImei(android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID));
            }
            WifiManager wifi = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            deviceInfo.setMac(wifi.getConnectionInfo().getMacAddress());
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
