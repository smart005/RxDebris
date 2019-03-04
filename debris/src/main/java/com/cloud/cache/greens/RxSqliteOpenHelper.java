package com.cloud.cache.greens;

import android.content.Context;

import com.cloud.cache.daos.DaoMaster;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class RxSqliteOpenHelper extends DaoMaster.DevOpenHelper {

    private Class<? extends AbstractDao<?, ?>>[] daoClasses = null;

    public RxSqliteOpenHelper(Context context,
                              String name,
                              OnDatabasePathListener listener,
                              Class<? extends AbstractDao<?, ?>>... daoClasses) {
        super(new DatabaseContext(context, listener), name, null);
        this.daoClasses = daoClasses;
    }

    @Override
    public void onUpgrade(Database db,
                          int oldVersion,
                          int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, daoClasses);
    }
}
