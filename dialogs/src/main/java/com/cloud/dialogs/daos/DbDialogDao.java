package com.cloud.dialogs.daos;

import com.cloud.cache.greens.DbHelper;
import com.cloud.cache.greens.RxSqliteOpenHelper;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class DbDialogDao {

    private DaoSession getDaoSession(boolean isOnlyRead) {
        RxSqliteOpenHelper helper = DbHelper.getHelper();
        if (helper == null) {
            return null;
        }
        DaoMaster daoMaster = new DaoMaster(isOnlyRead ? helper.getReadableDatabase() : helper.getWritableDatabase());
        return daoMaster.newSession();
    }

    /**
     * 获取配置选项dao
     *
     * @param isOnlyRead true-只读;false-可写;
     * @return
     */
    public OptionsItemDao getOptionsItemDao(boolean isOnlyRead) {
        DaoSession daoSession = getDaoSession(isOnlyRead);
        if (daoSession == null) {
            return null;
        }
        OptionsItemDao.createTable(daoSession.getDatabase(), true);
        return daoSession.getOptionsItemDao();
    }
}
