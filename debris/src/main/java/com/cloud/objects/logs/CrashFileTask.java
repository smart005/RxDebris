package com.cloud.objects.logs;

import android.text.TextUtils;

import com.cloud.objects.enums.DateFormatEnum;
import com.cloud.objects.storage.StorageUtils;
import com.cloud.objects.utils.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Properties;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2015-7-11 上午10:49:22
 * Description: 缓存异常信息至文件
 * Modifier:
 * ModifyContent:
 */
class CrashFileTask {

    private final String STACK_TRACE = "STACK_TRACE";
    /**
     * 错误报告文件的扩展名
     */
    private final String CRASH_REPORTER_EXTENSION = "txt";

    public void writeLog(Throwable throwable) {
        try {
            if (throwable == null) {
                return;
            }
            String crashInfo = CrashUtils.getCrashInfo(throwable);
            if (TextUtils.isEmpty(crashInfo)) {
                return;
            }
            Properties mDeviceCrashInfo = new Properties();
            mDeviceCrashInfo.putAll(CrashUtils.getProgramDeviceInfo());
            saveToFile(crashInfo, mDeviceCrashInfo);
        } catch (Exception e) {
            // write log error
        }
    }

    /**
     * 保存日志
     * <p>
     * param crashInfo        错误信息
     * param mDeviceCrashInfo 设置相关信息
     * param level            错误等级
     */
    private void saveToFile(String crashInfo, Properties mDeviceCrashInfo) {
        try {
            File dir = StorageUtils.getDir("error");
            final String contentperx = crashInfo.length() > 20 ? crashInfo.substring(0, 20) : crashInfo.toString();
            String fileName = String.format("%s_%s.%s", DateUtils.getDateTime(DateFormatEnum.YYYYMMDDHHMMSS), contentperx, CRASH_REPORTER_EXTENSION);
            File[] filelst = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (TextUtils.isEmpty(filename)) {
                        return false;
                    } else if (filename.contains(contentperx)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            if (filelst != null && filelst.length > 0) {
                for (File file : filelst) {
                    file.delete();
                }
            }
            mDeviceCrashInfo.put(STACK_TRACE, crashInfo);
            File mfile = new File(dir, fileName);
            if (!mfile.exists()) {
                mfile.createNewFile();
            }
            // 保存文件
            FileOutputStream trace = new FileOutputStream(mfile, true);
            mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
        } catch (Exception e) {
            // collectCrashDeviceInfo save to file error
        }
    }
}
