package com.cloud.cache.entries;

import android.database.sqlite.SQLiteDatabase;

import com.cloud.cache.daos.DaoMaster;
import com.cloud.cache.daos.DaoSession;
import com.cloud.cache.events.OnDataChainRunnable;
import com.cloud.cache.greens.DbHelper;
import com.cloud.cache.greens.RxSqliteOpenHelper;
import com.cloud.objects.events.OnChainRunnable;
import com.cloud.objects.tasks.SyncChainTasks;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/18
 * Description:
 * Modifier:
 * ModifyContent:
 */
class BaseCacheDao {

    private SyncChainTasks chainTasks = SyncChainTasks.getInstance();

    protected DaoSession getDaoSession() {
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

    private <CR extends OnChainRunnable,T> void perform(CR runnable,T... extras) {
        chainTasks.addChain(runnable);
        DaoSession daoSession = getDaoSession();
        if (daoSession == null) {
            DbHelper.getHelper().close();
            return;
        }
        chainTasks.build(daoSession,extras);
    }

    protected <T> void addDataChain(final OnDataChainRunnable runnable,T... extras) {
        perform(new OnChainRunnable<Void, DaoSession>() {
            @Override
            public Void run(DaoSession daoSession, Object... extras) {
                Object result = runnable.run(daoSession);
                runnable.complete(result, extras);
                return null;
            }

            @Override
            public void finish() {
                DbHelper.getHelper().close();
            }
        },extras);
    }
}
