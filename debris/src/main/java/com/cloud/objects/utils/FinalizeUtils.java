package com.cloud.objects.utils;

import com.cloud.objects.events.Action0;
import com.cloud.objects.observable.ObservableComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/26
 * Description:finalize time out工具类
 * Modifier:
 * ModifyContent:
 */
public class FinalizeUtils {

    /**
     * 部分广商订制的系统由于回收对象时间过长，由FinalizerWatchdogDaemon负责计时，超时后抛出finalize() timed out异常关闭VM的;
     * 因此在根据友盟或bugly关闭对某些机型的统计;
     * 此方法在应用启动后调用(最好根据相应的bug统一在对应的设备下关闭)
     */
    public static void finalizerWatchdogDaemon() {
        component.build();
    }

    private static ObservableComponent component = new ObservableComponent() {
        @Override
        protected Object subscribeWith(Object[] objects) throws Exception {
            Action0 nanosAction = new Action0() {
                @Override
                public void call() {
                    try {
                        Class<?> c = Class.forName("java.lang.Daemons");
                        Field maxField = c.getDeclaredField("MAX_FINALIZE_NANOS");
                        maxField.setAccessible(true);
                        maxField.set(null, Long.MAX_VALUE);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            };

            try {
                Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
                Method method = clazz.getSuperclass().getDeclaredMethod("stop");
                method.setAccessible(true);
                Field field = clazz.getDeclaredField("INSTANCE");
                field.setAccessible(true);
                method.invoke(field.get(null));
            } catch (Throwable e) {
                nanosAction.call();
            }
            return null;
        }
    };
}
