package com.cloud.cache;

import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.cache.daos.DaoMaster;
import com.cloud.cache.daos.DaoSession;
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

    private DaoSession getDaoSession(boolean isOnlyRead) {
        RxSqliteOpenHelper helper = DbHelper.getHelper();
        if (helper == null) {
            return null;
        }
        DaoMaster daoMaster = new DaoMaster(isOnlyRead ? helper.getReadableDatabase() : helper.getWritableDatabase());
        return daoMaster.newSession();
    }

    /**
     * 获取缓存dao
     *
     * @param isOnlyRead true-只读;false-可写;
     * @return
     */
    public CacheDataItemDao getCacheDataItemDao(boolean isOnlyRead) {
        DaoSession daoSession = getDaoSession(isOnlyRead);
        if (daoSession == null) {
            return null;
        }
        CacheDataItemDao.createTable(daoSession.getDatabase(), true);
        return daoSession.getCacheDataItemDao();
    }
}
