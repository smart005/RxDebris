package com.cloud.images.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cloud.cache.PathCacheInfoItem;
import com.cloud.cache.daos.PathCacheInfoItemDao;
import com.cloud.cache.entries.PathCacheDataEntry;
import com.cloud.cache.events.OnDataChainRunnable;
import com.cloud.objects.logs.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;

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

    /**
     * 通过orginalUrl获取本地文件
     *
     * @param originalUrl 原url
     * @return 本地文件
     */
    public File getLocalFile(final String originalUrl) {
        if (TextUtils.isEmpty(originalUrl)) {
            return null;
        }
        PathCacheDataEntry pathCacheDataEntry = new PathCacheDataEntry();
        PathCacheInfoItem pathCacheInfo = pathCacheDataEntry.getInfo(new OnDataChainRunnable<PathCacheInfoItem, PathCacheInfoItemDao, Object>() {
            @Override
            public PathCacheInfoItem run(PathCacheInfoItemDao pathCacheInfoItemDao) {
                QueryBuilder<PathCacheInfoItem> builder = pathCacheInfoItemDao.queryBuilder();
                builder.where(PathCacheInfoItemDao.Properties.Url.eq(originalUrl));
                PathCacheInfoItem unique = builder.unique();
                return unique;
            }
        });
        if (TextUtils.isEmpty(pathCacheInfo.getTargetPath())) {
            return null;
        }
        File file = new File(pathCacheInfo.getTargetPath());
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    /**
     * 移除文件
     *
     * @param originalUrl 原文件对应的网络url
     */
    public void removeFile(final String originalUrl) {
        if (TextUtils.isEmpty(originalUrl)) {
            return;
        }
        PathCacheDataEntry pathCacheDataEntry = new PathCacheDataEntry();
        PathCacheInfoItem pathCacheInfo = pathCacheDataEntry.getInfo(new OnDataChainRunnable<PathCacheInfoItem, PathCacheInfoItemDao, Object>() {
            @Override
            public PathCacheInfoItem run(PathCacheInfoItemDao pathCacheInfoItemDao) {
                QueryBuilder<PathCacheInfoItem> builder = pathCacheInfoItemDao.queryBuilder();
                builder.where(PathCacheInfoItemDao.Properties.Url.eq(originalUrl));
                PathCacheInfoItem unique = builder.unique();
                return unique;
            }
        });
        if (TextUtils.isEmpty(pathCacheInfo.getTargetPath())) {
            return;
        }
        File file = new File(pathCacheInfo.getTargetPath());
        //删除文件
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                Logger.info("remove file fail.");
            }
        }
        //删除记录信息
        pathCacheDataEntry.execute(new OnDataChainRunnable<Void, PathCacheInfoItemDao, Object>() {
            @Override
            public Void run(PathCacheInfoItemDao pathCacheInfoItemDao) {
                QueryBuilder<PathCacheInfoItem> builder = pathCacheInfoItemDao.queryBuilder();
                builder.where(PathCacheInfoItemDao.Properties.Url.eq(originalUrl));
                builder.buildDelete();
                return null;
            }
        });
    }
}
