package com.cloud.cache;

import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.cache.daos.DaoMaster;
import com.cloud.cache.daos.DaoSession;
import com.cloud.cache.daos.OptionsItemDao;
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
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        return daoMaster.newSession();
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
        return daoSession.getOptionsItemDao();
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
        return daoSession.getStackInfoItemDao();
    }
}
