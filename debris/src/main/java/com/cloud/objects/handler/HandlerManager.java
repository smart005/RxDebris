package com.cloud.objects.handler;

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

    private HandlerManager(Looper looper) {
        builder(looper);
    }

    public static HandlerManager getInstance() {
        return new HandlerManager(Looper.getMainLooper());
    }

    /**
     * 获取handler构建对象
     *
     * @param looper 不传代表主线程
     * @return HandlerManager
     */
    public static HandlerManager getBuilder(Looper looper) {
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        return new HandlerManager(looper);
    }

    private void builder(Looper looper) {
        Handler mainHandler = new Handler(looper) {
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
                    msg.obj = null;
                    handlerThreadLocal.remove();
                } catch (Exception e) {
                    Logger.debug(e.getMessage());
                }
            }
        };
        handlerThreadLocal.set(mainHandler);
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
            message.obj = runnable;
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
