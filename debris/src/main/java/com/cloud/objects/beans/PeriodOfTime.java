package com.cloud.objects.beans;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/14
 * @Description:时间段属性
 * @Modifier:
 * @ModifyContent:
 */
public class PeriodOfTime {
    /**
     * 时间段内返回多少天
     */
    private int days = 0;
    /**
     * 时间段内返回多少小时
     */
    private int hours = 0;
    /**
     * 时间段内返回多少分钟
     */
    private int minutes = 0;
    /**
     * 秒
     */
    private int seconds = 0;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
