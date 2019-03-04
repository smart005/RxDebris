/**
 * Title: ViewUtils.java
 * Description:
 * author: lijinghuan
 * data: 2015-3-3 上午9:17:16
 */
package com.cloud.objects.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.cloud.objects.events.Action0;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.HookEvent;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {

    /**
     * 获取根视图
     * <p>
     * param win Window
     * return View layout root view
     */
    public static View getRootView(Window win) {
        View fragment = win.getDecorView().findViewById(android.R.id.content);
        return fragment;
    }

    /**
     * 根据tag递归找视图
     *
     * @param vg     容器
     * @param tagkey tag设置中对应的键
     * @param tag    tag设置中对应的值
     * @return 匹配的视图列表
     */
    public static List<View> getViewsByTag(ViewGroup vg, int tagkey, Object tag) {
        List<View> lst = new ArrayList<View>();
        if (vg == null) {
            return lst;
        }
        int count = vg.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = vg.getChildAt(i);
            if (tagkey > 0) {
                if (TextUtils.equals(String.valueOf(v.getTag(tagkey)),
                        String.valueOf(tag))) {
                    lst.add(v);
                }
            } else {
                if (TextUtils.equals(String.valueOf(v.getTag()),
                        String.valueOf(tag))) {
                    lst.add(v);
                }
            }
            if (v instanceof ViewGroup) {
                lst.addAll(getViewsByTag((ViewGroup) v, tagkey, tag));
            }
        }
        return lst;
    }

    public static List<View> getViewsByTag(ViewGroup vg, Object tag) {
        return getViewsByTag(vg, 0, tag);
    }

    /**
     * 获取视图缓存图片
     * <p>
     * param container 需要获取缓存图片的视图
     * return
     */
    public static Bitmap getViewCacheBitmap(View container) {
        container.setDrawingCacheEnabled(true);
        container.buildDrawingCache();
        Bitmap bt = container.getDrawingCache();
        return bt;
    }

    /**
     * 获取并缓存列表项视图
     * <p>
     * param convertView list item view
     * param id          控件id
     * param         控件类型
     * return
     */
    public static <T extends View> T getItemView(View convertView, int id) {
        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if (holder == null) {
            holder = new SparseArray<View>();
            convertView.setTag(holder);
        }
        View view = holder.get(id);
        if (view == null) {
            view = convertView.findViewById(id);
            holder.put(id, view);
        }
        return (T) view;
    }

    /**
     * 绑定视图
     * <p>
     * param source 视图
     * param target 目标对象
     * param    控件类型
     * return
     */
    public static <T extends View> T bindView(View source, int target) {
        if (source != null && target != 0) {
            View view = source.findViewById(target);
            return (T) view;
        }
        return null;
    }

    /**
     * 设置视图监听
     *
     * @param view
     * @param action click事件回调
     * @param extras click回调函数中返回
     * @param <T>    扩展数据类型
     */
    public static <T> void setViewListener(View view, final Action1<T> action, T extras) {
        if (view == null || action == null) {
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            boolean registerListener = HookEvent.isRegisterListener(group);
            if (!registerListener) {
                group.setTag(463716424, extras);
                group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (action != null) {
                            Object obj = v.getTag(463716424);
                            action.call((T) obj);
                        }
                    }
                });
            }
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = group.getChildAt(i);
                setViewListener(v, action, extras);
            }
        } else {
            boolean registerListener = HookEvent.isRegisterListener(view);
            if (!registerListener) {
                view.setTag(463716424, extras);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (action != null) {
                            Object obj = v.getTag(463716424);
                            action.call((T) obj);
                        }
                    }
                });
            }
        }
    }

    /**
     * 设置视图监听
     *
     * @param view
     * @param action click回调函数中返回
     */
    public static void setViewListener(View view, final Action0 action) {
        setViewListener(view, new Action1<Object>() {
            @Override
            public void call(Object t1) {
                if (action != null) {
                    action.call();
                }
            }
        }, null);
    }
}
