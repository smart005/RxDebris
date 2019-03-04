package com.cloud.objects.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2015-11-23 下午3:55:20
 * Description: 目录、路径处理类(http://stackoverflow.com/questions/412380/combine-paths
 * -in-java)
 * Modifier:
 * ModifyContent:
 */
public class PathsUtils {

    /**
     * 组合路径
     * <p>
     * param paths 要组合的地址列表
     * return
     */
    public static String combine(String... paths) {
        if (paths == null || paths.length == 0) {
            return "";
        }
        File file = new File(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            file = new File(file, paths[i]);
        }
        String uri = file.getPath();
        if (TextUtils.isEmpty(uri)) {
            return "";
        }
        if (!uri.contains("://") && uri.contains(":/")) {
            uri = uri.replace(":/", "://");
        }
        return uri;
    }
}
