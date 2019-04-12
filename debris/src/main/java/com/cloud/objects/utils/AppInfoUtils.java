package com.cloud.objects.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;

import com.cloud.objects.ObjectManager;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Author LiJingHuan
 * Email:ljh0576123@163.com
 * CreateTime:2015-6-3 上午11:01:01
 * Description:app相关信息工具类
 * Modifier:
 * ModifyContent:
 */
public class AppInfoUtils {

    public static String getAppName(Context context, int pid) {
        String processName = null;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : am
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        return processName;
    }

    /**
     * 获取本地ip地址
     *
     * @return
     * @throws SocketException
     */
    public static String getLocalIpAddress() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface
                .getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf
                    .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    return inetAddress.getHostAddress().toString();
                }
            }
        }
        return "127.0.0.1";
    }

    /**
     * 获取可执行文件(apk或jar)包信息
     * <p>
     * param context
     * param file
     * return
     */
    public static PackageInfo getPackageInfoByFile(Context context, File file) {
        PackageManager pm = ObjectManager.getPackageManager(context);
        PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(),
                PackageManager.GET_ACTIVITIES);
        return info;
    }

    /**
     * 获取包信息
     *
     * @param application
     * @param <T>
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static <T extends Application> PackageInfo getPackageInfo(T application) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = application.getPackageManager();
        String packageName = application.getPackageName();
        PackageInfo info = packageManager.getPackageInfo(packageName, 0);
        return info;
    }

    /**
     * 获取manifest下meta数据
     *
     * @param application
     * @param <T>
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static <T extends Application> Bundle getApplicationMetaData(T application) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = application.getPackageManager();
        String packageName = application.getPackageName();
        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        Bundle mbundle = appInfo.metaData;
        return mbundle;
    }

    /**
     * 获取manifest下meta数据
     *
     * @param application
     * @param metaKey
     * @param <T>
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static <T extends Application> Object getMetaObject(T application, String metaKey) throws PackageManager.NameNotFoundException {
        Object metaName = null;
        Bundle mbundle = AppInfoUtils.getApplicationMetaData(application);
        if (mbundle.containsKey(metaKey)) {
            metaName = mbundle.get(metaKey);
        }
        return metaName;
    }

    /**
     * 获取manifest下meta数据
     *
     * @param application
     * @param metaKey
     * @param <T>
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static <T extends Application> boolean getMetaBoolean(T application, String metaKey) throws PackageManager.NameNotFoundException {
        boolean metaName = false;
        Bundle mbundle = AppInfoUtils.getApplicationMetaData(application);
        if (mbundle.containsKey(metaKey)) {
            metaName = mbundle.getBoolean(metaKey);
        }
        return metaName;
    }

    /**
     * 获得app进程名
     * <p>
     * param context 宿主上下文
     */
    public static String getAppProcessName(Context context) {
        int pid = android.os.Process.myPid();
        return getAppName(context, pid);
    }

    /**
     * 获取cup架构
     * <p>
     * return
     */
    public static List<String> getCPUAbis() {
        List<String> lst = new ArrayList<String>();
        String[] abis = new String[]{};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        for (String abi : abis) {
            lst.add(abi);
        }
        return lst;
    }

    /**
     * 获取SD卡剩余空间
     * <p>
     * return
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 获取SD卡总容量
     * <p>
     * return
     */
    public static long getSDAllSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }
}
