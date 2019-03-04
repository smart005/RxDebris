package com.cloud.objects;

import android.os.Handler;
import android.os.Looper;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/9
 * Description:handler管理
 * Modifier:
 * ModifyContent:
 */
public class HandlerManager {

    private static HandlerManager handlerManager = null;
    private Handler mainHandler = null;

    public static HandlerManager getInstance() {
        return handlerManager == null ? handlerManager = new HandlerManager() : handlerManager;
    }

    /**
     * 清空引用
     */
    public void clearReference() {
        mainHandler = null;
        handlerManager = null;
    }

    private Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    private class InternalRunable implements Runnable {

        private Runnable runnable = null;
        private Handler handler = null;

        public InternalRunable(Handler handler, Runnable runnable) {
            this.handler = handler;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            if (runnable != null) {
                runnable.run();
                if (handler != null) {
                    handler.removeCallbacks(runnable);
                }
            }
        }
    }

    /**
     * Causes the Runnable r to be added to the message queue.
     * The runnable will be run on the thread to which this handler is
     * attached.
     *
     * @param handler
     * @param runnable
     */
    public void post(Runnable runnable) {
        Handler handler = getMainHandler();
        handler.post(new InternalRunable(handler, runnable));
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     *
     * @param handler
     * @param runnable
     * @param delayMillis
     */
    public void postDelayed(Runnable runnable, long delayMillis) {
        Handler handler = getMainHandler();
        handler.postDelayed(new InternalRunable(handler, runnable), delayMillis);
    }

    /**
     * 回收runnable
     *
     * @param handler
     * @param runnable
     */
    public void recyclingRunnable(Handler handler, Runnable runnable) {
        if (runnable != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 回收runnable
     *
     * @param handler
     * @param runnable
     */
    public void recyclingRunnable(Runnable runnable) {
        Handler handler = getMainHandler();
        recyclingRunnable(handler, runnable);
    }
}
