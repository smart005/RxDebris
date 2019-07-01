package com.cloud.debris.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-06-29
 * Description:基础工具类
 * Modifier:
 * ModifyContent:
 */
public class BaseCommonUtils {

    /**
     * 绑定Fragment至FragmentLayout容器
     *
     * @param fragmentManager fragment管理器
     * @param containerViewId 容器id
     * @param fragment        添加的fragment对象
     */
    public static void bindFrameLayout(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 绑定Fragment至FragmentLayout容器
     *
     * @param fragmentActivity fragment管理器
     * @param containerViewId  容器id
     * @param fragment         添加的fragment对象
     */
    public static void bindFrameLayout(FragmentActivity fragmentActivity, int containerViewId, Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            bindFrameLayout(fragmentManager, containerViewId, fragment);
        }
    }
}
