package com.cloud.objects.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:资源相关工具类
 * Modifier:
 * ModifyContent:
 */
public class ResUtils {
    /**
     * 获取drawable资源图片全路径
     *
     * @param context 上下文
     * @param id      资源id
     * @return
     */
    public static String getResourcesUri(Context context, @DrawableRes int id) {
        Resources resources = context.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

    /**
     * 根据资源id获取drawable
     *
     * @param context 上下文
     * @param id      资源id
     * @return Drawable
     */
    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        return AppCompatResources.getDrawable(context, id);
    }
}
