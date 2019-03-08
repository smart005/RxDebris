package com.cloud.images2.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

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

    public static GlideImageModels with(Context context) {
        RequestManager manager = Glide.with(context);
        GlideImageModels builder = GlideImageModels.getBuilder(manager);
        return builder;
    }

    public static GlideImageModels withSet(Context context) {
        //渲染图
        RequestManager manager = Glide.with(context);
        GlideImageModels builder = GlideImageModels.getBuilder(manager);
        //缩略图
        GlideImageModels thumbnailBuilder = GlideImageModels.getBuilder(Glide.with(context));

        return builder;
    }

    public static GlideImageModels with(Fragment fragment) {
        RequestManager manager = Glide.with(fragment);
        GlideImageModels builder = GlideImageModels.getBuilder(manager);
        return builder;
    }

    public static GlideImageModels with(Activity activity) {
        RequestManager manager = Glide.with(activity);
        GlideImageModels builder = GlideImageModels.getBuilder(manager);
        return builder;
    }

    public static GlideImageModels with(FragmentActivity fragmentActivity) {
        RequestManager manager = Glide.with(fragmentActivity);
        GlideImageModels builder = GlideImageModels.getBuilder(manager);
        return builder;
    }
}
