package com.cloud.cache.greens;

import com.cloud.objects.config.RxAndroid;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:数据库帮助类
 * (后期增加配置)
 * Modifier:
 * ModifyContent:
 */
public class DbHelper {

    /**
     * 获取sqlite对象
     *
     * @return
     */
    public static RxSqliteOpenHelper getHelper() {
        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        String databaseName = builder.getDatabaseName();
        RxSqliteOpenHelper helper = DBManager.getInstance().getHelper(databaseName);
        return helper;
    }
}
