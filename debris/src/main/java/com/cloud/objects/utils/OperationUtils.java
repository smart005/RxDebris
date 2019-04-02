package com.cloud.objects.utils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/26
 * Description:位、与、移位、幂次等相关运算
 * Modifier:
 * ModifyContent:
 */
public class OperationUtils {

    /**
     * 判断value为奇数还是偶数
     *
     * @param value 校验的数值
     * @return true-奇数;false-偶数;
     */
    public static boolean parity(long value) {
        //value&1==1为奇数,==0为偶数;
        return (value & 1) == 1;
    }

    /**
     * 计算x、y平均值
     * (避免x+y的和大于int最大值)
     *
     * @param x x value
     * @param y y value
     * @return 平均值
     */
    public static int average(int x, int y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    /**
     * 是否幂指数
     * (例:2^n)
     *
     * @param value 校验数值
     * @return true-幂指数;false-非幂指数;
     */
    public static boolean isExponential(long value) {
        return ((value > 0) && ((value & (value - 1)) == 0));
    }

    /**
     * 获取相对最大幂指数
     *
     * @param value 当前数值
     * @return 相对最大幂指数
     */
    public static int getRelativeMaxExponential(int value) {
        if (value <= 0) {
            return 0;
        }
        if (isExponential(value)) {
            return value;
        }
        int minexp = value & (value - 1);
        return minexp * 2;
    }

    /**
     * 获取数据结构扩容值
     *
     * @param value  数据结构参数初始化值
     * @param factor 扩容因子(一般为0.75)
     * @return 数据结构扩容值
     */
    public static int getCapacityValue(int value, double factor) {
        int exponential = getRelativeMaxExponential(value);
        int m = (int) (exponential * factor);
        return (m > value ? exponential : (exponential * 2));
    }

    /**
     * 获取数据结构扩容值
     * 扩容因子(一般为0.75)
     *
     * @param value 数据结构参数初始化值
     * @return 数据结构扩容值
     */
    public static int getCapacityValue(int value) {
        return getCapacityValue(value, 0.75);
    }
}