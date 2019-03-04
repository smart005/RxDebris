package com.cloud.objects.events;

import android.view.View;

import com.cloud.objects.logs.Logger;

import java.lang.reflect.Field;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/14
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class HookEvent {
    protected void onPreClick(View v) {

    }

    protected void onAfterClick(View v) {

    }

    public void didHook(View view) {
        try {
            Class viewClazz = View.class;
            if (isRegisterListener(view)) {
                Field infoField = viewClazz.getDeclaredField("mListenerInfo");
                if (infoField == null) {
                    return;
                }
                if (!infoField.isAccessible()) {
                    infoField.setAccessible(true);
                }
                Object listenerInfo = infoField.get(view);
                if (listenerInfo == null) {
                    return;
                }
                Class infoClazz = Class.forName("android.view.View$ListenerInfo");
                if (infoClazz == null) {
                    return;
                }
                Field listenerField = infoClazz.getDeclaredField("mOnClickListener");
                if (listenerField == null) {
                    return;
                }
                if (!listenerField.isAccessible()) {
                    listenerField.setAccessible(true);
                }
                View.OnClickListener mOnClickListener = (View.OnClickListener) listenerField.get(listenerInfo);
                OnClickListenerProxy onClickListenerProxy = new OnClickListenerProxy(mOnClickListener) {
                    @Override
                    protected void onPreClickProxy(View v) {
                        onPreClick(v);
                    }

                    @Override
                    protected void onAfterClickProxy(View v) {
                        onAfterClick(v);
                    }
                };
                listenerField.set(listenerInfo, onClickListenerProxy);
            } else {
                OnClickListenerProxy onClickListenerProxy = new OnClickListenerProxy(null) {
                    @Override
                    protected void onPreClickProxy(View v) {
                        onPreClick(v);
                    }

                    @Override
                    protected void onAfterClickProxy(View v) {
                        onAfterClick(v);
                    }
                };
                view.setOnClickListener(onClickListenerProxy);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static boolean isRegisterListener(Class viewClazz, View view) {
        try {
            Field infoField = viewClazz.getDeclaredField("mListenerInfo");
            if (infoField == null) {
                return false;
            }
            if (!infoField.isAccessible()) {
                infoField.setAccessible(true);
            }
            Object listenerInfo = infoField.get(view);
            if (listenerInfo == null) {
                return false;
            }
            Class infoClazz = Class.forName("android.view.View$ListenerInfo");
            if (infoClazz == null) {
                return false;
            }
            Field listenerField = infoClazz.getDeclaredField("mOnClickListener");
            if (listenerField == null) {
                return false;
            }
            if (!listenerField.isAccessible()) {
                listenerField.setAccessible(true);
            }
            View.OnClickListener mOnClickListener = (View.OnClickListener) listenerField.get(listenerInfo);
            if (mOnClickListener == null) {
                return false;
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return true;
    }

    public static boolean isRegisterListener(View view) {
        Class viewClazz = View.class;
        return isRegisterListener(viewClazz, view);
    }
}
