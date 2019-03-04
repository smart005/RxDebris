package com.cloud.objects;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2015-7-23 下午10:50:56
 * Description: 倒计时处理类
 * Modifier:
 * ModifyContent:
 */
public class CountdownExecutor<T> {

    private ScheduledThreadPoolExecutor sc = null;
    private long period = 1;
    private long time = 0;
    /**
     * 倒计时总时间(单位是秒)
     */
    private long countdownTotalTime = 0;
    /**
     * 扩展数据
     */
    private T extras = null;

    /**
     * param 设置每次执行时间间隔 (以秒为单位)
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * param 设置倒计时总时间 (单位是秒)
     */
    public void setCountdownTotalTime(long countdownTotalTime) {
        this.countdownTotalTime = countdownTotalTime;
    }

    /**
     * return 获取倒计时总时间
     * (单位是秒)
     */
    public long getCountdownTotalTime() {
        return time;
    }

    /**
     * 在倒计时启动之前执行
     * <p>
     * param seconds 当前计时秒数
     * param extras  扩展数据
     */
    protected void onPerExecutor(long seconds, T extras) {

    }

    /**
     * 在计时中执行
     * <p>
     * param seconds 当前计时秒数
     * param extras  扩展数据
     */
    protected void onDoingExecutor(long seconds, T extras) {

    }

    /**
     * 结束之后执行
     * <p>
     * param extras 扩展数据
     */
    protected void onPostExecutor(T extras) {

    }

    /**
     * 设置扩展数据
     * <p>
     * param extras
     */
    public void setExtras(T extras) {
        this.extras = extras;
    }

    public void start() {
        if (countdownTotalTime <= 0) {
            return;
        }
        stop();
        sc = new ScheduledThreadPoolExecutor(1);
        time = countdownTotalTime;
        onPerExecutor(time, extras);
        sc.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                time--;
                onDoingExecutor(time, extras);
                if (time <= 0) {
                    stop();
                    mhandler.obtainMessage(-2000).sendToTarget();
                }
            }
        }, 0, period, TimeUnit.SECONDS);
    }

    public void stop() {
        if (sc != null && !sc.isShutdown()) {
            sc.shutdown();
        }
    }

    private Handler mhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -2000) {
                onPostExecutor(extras);
            }
        }
    };
}
