package com.cloud.cache.greens;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/19
 * Description:数据库目录监听
 * Modifier:
 * ModifyContent:
 */
public interface OnDatabasePathListener {

    /**
     * 回调数据库目录
     *
     * @return 数据库根目录
     */
    public File onDatabaseRootPath();
}
