package com.cloud.objects.utils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/9/6
 * @Description:排序
 * @Modifier:
 * @ModifyContent:
 */
public class SortUtils {

    /**
     * 字符排序
     *
     * @param isDesc true-降序;false-升序;
     * @param charts 需要排序的字符项
     * @return
     */
    public static String[] chartsSort(final boolean isDesc, String... charts) {
        Arrays.sort(charts, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (isDesc) {
                    return o2.compareTo(o1);
                } else {
                    return o1.compareTo(o2);
                }
            }
        });
        return charts;
    }

    /**
     * 字符排序
     *
     * @param charts 需要排序的字符项
     * @return
     */
    public static String[] chartsSort(String... charts) {
        return chartsSort(false, charts);
    }

    /**
     * 字符排序
     *
     * @param isDesc true-降序;false-升序;
     * @param charts 需要排序的数值
     * @return
     */
    public static Integer[] numbersSort(final boolean isDesc, Integer... numbers) {
        Arrays.sort(numbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (isDesc) {
                    return o2.compareTo(o1);
                } else {
                    return o1.compareTo(o2);
                }
            }
        });
        return numbers;
    }

    /**
     * 字符排序
     *
     * @param isDesc true-降序;false-升序;
     * @param charts 需要排序的数值
     * @return
     */
    public static Integer[] numbersSort(Integer... numbers) {
        return numbersSort(false, numbers);
    }
}
