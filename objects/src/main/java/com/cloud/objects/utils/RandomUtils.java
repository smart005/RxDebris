package com.cloud.objects.utils;

import java.util.Random;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/10
 * Description:随机数
 * Modifier:
 * ModifyContent:
 */
public class RandomUtils {

    /**
     * 获取指定范围的随机数([min,max-1))
     *
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
