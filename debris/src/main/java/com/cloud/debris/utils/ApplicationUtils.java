package com.cloud.debris.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/27
 * Description:应用程序工具类
 * Modifier:
 * ModifyContent:
 */
public class ApplicationUtils {

    /**
     * 检测应用是否已安装
     *
     * @param applicationContext 上下文(最好是Application上下文)
     * @param schemeUrl          第三方应用默认scheme url
     * @return
     */
    public static boolean checkApplicationInstalled(Context applicationContext, String schemeUrl) {
        if (applicationContext == null || TextUtils.isEmpty(schemeUrl)) {
            return false;
        }
        Uri uri = Uri.parse(schemeUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(applicationContext.getPackageManager());
        return componentName != null;
    }
}
