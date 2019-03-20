package com.cloud.objects;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.logs.Logger;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/9
 * Description:handler管理
 * Modifier:
 * ModifyContent:
 */
public class HandlerManager {

    private ThreadLocal<Handler> handlerThreadLocal = new ThreadLocal<Handler>();
    private Object[] params;

    private HandlerManager() {
        Handler mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                try {
                    if (msg.what != 48820 || msg.obj == null || !(msg.obj instanceof RunnableParamsN)) {
                        params = null;
                        handlerThreadLocal.remove();
                        return;
                    }
                    RunnableParamsN runnable = (RunnableParamsN) msg.obj;
                    runnable.run(params);
                    handlerThreadLocal.remove();
                } catch (Exception e) {
                    Logger.debug(e.getMessage());
                }
            }
        };
        handlerThreadLocal.set(mainHandler);
    }

    public static HandlerManager getInstance() {
        return new HandlerManager();
    }

    /**
     * Causes the Runnable r to be added to the message queue.
     * The runnable will be run on the thread to which this handler is
     * attached.
     *
     * @param runnable call runnable
     * @param params   参数
     */
    public <T> void post(RunnableParamsN<T> runnable, T... params) {
        if (runnable == null) {
            return;
        }
        try {
            this.params = params;
            Handler handler = handlerThreadLocal.get();
            if (handler == null) {
                this.params = null;
                //引用为空则取消发送
                handlerThreadLocal.remove();
                return;
            }
            Message message = handler.obtainMessage(48820, runnable);
            handler.sendMessage(message);
        } catch (Exception e) {
            Logger.debug(e.getMessage());
        }
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     *
     * @param runnable    call runnable
     * @param delayMillis 延迟delayMillis毫秒后回调
     * @param params      参数
     */
    public <T> void postDelayed(RunnableParamsN<T> runnable, long delayMillis, T... params) {
        if (runnable == null) {
            return;
        }
        try {
            this.params = params;
            Handler handler = handlerThreadLocal.get();
            if (handler == null) {
                this.params = null;
                //引用为空则取消发送
                handlerThreadLocal.remove();
                return;
            }
            Message message = handler.obtainMessage(48820, runnable);
            message.obj = params;
            if (delayMillis <= 0) {
                handler.sendMessage(message);
            } else {
                handler.sendMessageDelayed(message, delayMillis);
            }
        } catch (Exception e) {
            Logger.debug(e.getMessage());
        }
    }
}
