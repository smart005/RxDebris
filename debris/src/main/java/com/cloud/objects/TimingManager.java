package com.cloud.objects;

import com.cloud.objects.logs.Logger;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/10
 * Description:计时管理
 * Modifier:
 * ModifyContent:
 */
public class TimingManager {

    public static void executionTimeStatistics(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        long preTime = System.currentTimeMillis();
        runnable.run();
        long diff = System.currentTimeMillis() - preTime;
        Logger.info(String.format("执行时间：%sms", diff));
    }
}
