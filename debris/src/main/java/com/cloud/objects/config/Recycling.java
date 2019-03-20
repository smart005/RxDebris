package com.cloud.objects.config;

import com.cloud.objects.events.OnRecyclingListener;
import com.cloud.objects.utils.ThreadPoolUtils;

import java.util.LinkedList;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/6
 * Description:回调
 * Modifier:
 * ModifyContent:
 */
public class Recycling {

    private static Recycling recycling = null;
    private LinkedList<OnRecyclingListener> recyclingListeners = new LinkedList<OnRecyclingListener>();

    public static Recycling getInstance() {
        return recycling == null ? recycling = new Recycling() : recycling;
    }

    /**
     * 引用回调
     */
    public void referenceRecycling() {
        //清除线程池工具类引用
        ThreadPoolUtils.clearReference();
        clearRecyclings();
        recycling = null;
    }

    /**
     * 添加回收监听
     *
     * @param listener
     */
    public void addRecyclingListener(OnRecyclingListener listener) {
        if (!recyclingListeners.contains(listener)) {
            recyclingListeners.add(listener);
        }
    }

    private void clearRecyclings() {
        for (OnRecyclingListener listener : recyclingListeners) {
            if (listener == null) {
                continue;
            }
            listener.recycling();
        }
    }
}
