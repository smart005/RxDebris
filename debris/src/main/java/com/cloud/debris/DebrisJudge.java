package com.cloud.debris;

import com.cloud.cache.DerivedCache;
import com.cloud.objects.ObjectJudge;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-05-11
 * Description:框架判断
 * Modifier:
 * ModifyContent:
 */
public class DebrisJudge {

    /**
     * 是否启动主窗口
     *
     * @return true-已启动;false-未启动;
     */
    public static boolean isLaunchMain() {
        HashMap<String, Object> values = DerivedCache.getInstance().getValues("20882af386ee4fb1b052f8d701527402");
        Object isLaunch = values.get("isLaunch");
        return ObjectJudge.isTrue(isLaunch);
    }
}
