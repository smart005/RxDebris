package com.cloud.cache;

import android.database.sqlite.SQLiteDatabase;

import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.cache.daos.DaoMaster;
import com.cloud.cache.daos.DaoSession;
import com.cloud.cache.daos.OptionsItemDao;
import com.cloud.cache.daos.PathCacheInfoItemDao;
import com.cloud.cache.daos.StackInfoItemDao;
import com.cloud.cache.greens.DbHelper;
import com.cloud.cache.greens.RxSqliteOpenHelper;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class DbCacheDao {

    private DaoSession getDaoSession() {
        RxSqliteOpenHelper helper = DbHelper.getHelper();
        if (helper == null) {
            return null;
        }
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();
        if (daoSession == null) {
            helper.close();
        }
        return daoSession;
    }

    /**
     * 获取缓存dao
     *
     * @return 缓存dao
     */
    public CacheDataItemDao getCacheDataItemDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession == null) {
            return null;
        }
        CacheDataItemDao.createTable(daoSession.getDatabase(), true);
        return daoSession.getCacheDataItemDao();
    }

    /**
     * 获取配置选项dao
     *
     * @return 操作选项dao
     */
    public OptionsItemDao getOptionsItemDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession == null) {
            return null;
        }
        OptionsItemDao.createTable(daoSession.getDatabase(), true);
        OptionsItemDao optionsItemDao = daoSession.getOptionsItemDao();
        if (optionsItemDao == null) {
            DbHelper.getHelper().close();
        }
        return optionsItemDao;
    }

    /**
     * 获取堆栈信息dao
     *
     * @return 堆栈信息dao
     */
    public StackInfoItemDao getStackInfoItemDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession == null) {
            return null;
        }
        StackInfoItemDao.createTable(daoSession.getDatabase(), true);
        StackInfoItemDao stackInfoItemDao = daoSession.getStackInfoItemDao();
        if (stackInfoItemDao == null) {
            DbHelper.getHelper().close();
        }
        return stackInfoItemDao;
    }

    /**
     * 获取路径缓存信息
     *
     * @return PathCacheInfoItemDao
     */
    public PathCacheInfoItemDao getPathCacheInfoItemDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession == null) {
            return null;
        }
        PathCacheInfoItemDao.createTable(daoSession.getDatabase(), true);
        PathCacheInfoItemDao pathCacheInfoItemDao = daoSession.getPathCacheInfoItemDao();
        if (pathCacheInfoItemDao == null) {
            DbHelper.getHelper().close();
        }
        return pathCacheInfoItemDao;
    }
}
