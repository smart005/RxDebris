package com.cloud.cache.greens;

import android.content.Context;

import com.cloud.cache.MemoryCache;
import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.logs.Logger;

import org.greenrobot.greendao.AbstractDao;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/14
 * Description:数据库管理类
 * Modifier:
 * ModifyContent:
 */
public class DBManager {

    private static DBManager dbManager = null;
    private static Context applicationContext = null;

    public static DBManager getInstance() {
        return dbManager == null ? dbManager = new DBManager() : dbManager;
    }

    private DBManager() {
        //init
    }

    /**
     * 初始化数据库(最好在application中初始化)
     *
     * @param context    上下文
     * @param listener   数据库目录回调,null为应用目录
     * @param daoClasses 表对应的dao类
     * @return DBManager
     */
    @SafeVarargs
    public final DBManager initialize(Context context,
                                      OnDatabasePathListener listener,
                                      Class<? extends AbstractDao<?, ?>>... daoClasses) {
        applicationContext = context;
        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        String databaseName = builder.getDatabaseName();
        Class<? extends AbstractDao<?, ?>>[] array = toJoinArray(CacheDataItemDao.class, daoClasses);
        new RxSqliteOpenHelper(context, databaseName, listener, array);
        MemoryCache.getInstance().setSoftCache("CacheDatabasePathListener", listener);
        return this;
    }

    private Class<? extends AbstractDao<?, ?>>[] toJoinArray(Class<? extends AbstractDao<?, ?>> object, Class<? extends AbstractDao<?, ?>>... params) {
        if (object == null || ObjectJudge.isNullOrEmpty(params)) {
            return null;
        }
        Class<?>[] daos = new Class<?>[params.length + 1];
        for (int i = 0; i < params.length; i++) {
            Class<?> param = params[i];
            daos[i] = param;
        }
        daos[params.length] = object;
        return params;
    }

    /**
     * 获取sqlite帮助类
     *
     * @param databaseName 数据库名称
     * @return
     */
    public RxSqliteOpenHelper getHelper(String databaseName) {
        if (applicationContext != null) {
            Object listener = MemoryCache.getInstance().getSoftCache("CacheDatabasePathListener");
            if (listener instanceof OnDatabasePathListener) {
                OnDatabasePathListener databasePathListener = (OnDatabasePathListener) listener;
                RxSqliteOpenHelper helper = new RxSqliteOpenHelper(applicationContext, databaseName, databasePathListener);
                return helper;
            }
        }
        return null;
    }

    /**
     * 关闭数据库(在读取或写入完成后调用)
     */
    public void close() {
        try {
            RxSqliteOpenHelper helper = DbHelper.getHelper();
            if (helper == null) {
                return;
            }
            helper.close();
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
