package com.cloud.images.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:glide优化处理类,使用时调用此工具类即可;[使用入口类]
 * with中传入不同的对象用于管理生命周期(最好不用Application上下文)
 * Modifier:
 * ModifyContent:
 */
public class GlideOptimize {

    //手机密度
    private static float density = 0;

    /**
     * context方式加载图片
     *
     * @param context 上下文
     * @return GlideImageModels
     */
    public static GlideImageModels with(Context context) {
        if (density == 0) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            density = metrics.density;
        }
        RequestManager manager = Glide.with(context);
        GlideImageModels builder = GlideImageModels.getBuilder(density, context, manager);
        return builder;
    }

    /**
     * fragment方式加载图片
     *
     * @param fragment 上下文
     * @return GlideImageModels
     */
    public static GlideImageModels with(Fragment fragment) {
        if (density == 0) {
            DisplayMetrics metrics = fragment.getResources().getDisplayMetrics();
            density = metrics.density;
        }
        RequestManager manager = Glide.with(fragment);
        GlideImageModels builder = GlideImageModels.getBuilder(density, fragment.getContext(), manager);
        return builder;
    }

    /**
     * activity方式加载图片
     *
     * @param activity 上下文
     * @return GlideImageModels
     */
    public static GlideImageModels with(Activity activity) {
        if (density == 0) {
            DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
            density = metrics.density;
        }
        RequestManager manager = Glide.with(activity);
        GlideImageModels builder = GlideImageModels.getBuilder(density, activity, manager);
        return builder;
    }

    /**
     * fragmentActivity方式加载图片
     *
     * @param fragmentActivity 上下文
     * @return GlideImageModels
     */
    public static GlideImageModels with(FragmentActivity fragmentActivity) {
        if (density == 0) {
            DisplayMetrics metrics = fragmentActivity.getResources().getDisplayMetrics();
            density = metrics.density;
        }
        RequestManager manager = Glide.with(fragmentActivity);
        GlideImageModels builder = GlideImageModels.getBuilder(density, fragmentActivity, manager);
        return builder;
    }
}
