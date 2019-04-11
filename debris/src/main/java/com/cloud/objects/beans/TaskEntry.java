package com.cloud.objects.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/10
 * Description:任务数据项
 * Modifier:
 * ModifyContent:
 */
public class TaskEntry<T extends Runnable> {

    /**
     * 任务key
     */
    private String key;
    /**
     * 任务
     */
    private T task;
    /**
     * 执行计数
     */
    private int performCounts;
    /**
     * 延迟执行时间
     */
    private long delayTime;
    /**
     * 当前执行计数
     */
    private int count = 0;
    /**
     * 执行方式:synchronous或asynchronous
     */
    private String implementWay = "";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getTask() {
        return task;
    }

    public void setTask(T task) {
        this.task = task;
    }

    public int getPerformCounts() {
        return performCounts;
    }

    public void setPerformCounts(int performCounts) {
        this.performCounts = performCounts;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImplementWay() {
        return implementWay;
    }

    public void setImplementWay(String implementWay) {
        this.implementWay = implementWay;
    }
}
