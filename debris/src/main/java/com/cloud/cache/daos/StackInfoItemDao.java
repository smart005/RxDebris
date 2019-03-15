package com.cloud.cache.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.cloud.cache.StackInfoItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "rx_cache_stack_info".
*/
public class StackInfoItemDao extends AbstractDao<StackInfoItem, String> {

    public static final String TABLENAME = "rx_cache_stack_info";

    /**
     * Properties of entity StackInfoItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Key = new Property(0, String.class, "key", true, "key");
        public final static Property Stack = new Property(1, String.class, "stack", false, "stack");
    }


    public StackInfoItemDao(DaoConfig config) {
        super(config);
    }
    
    public StackInfoItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"rx_cache_stack_info\" (" + //
                "\"key\" TEXT PRIMARY KEY NOT NULL ," + // 0: key
                "\"stack\" TEXT);"); // 1: stack
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_rx_cache_stack_info_key ON \"rx_cache_stack_info\"" +
                " (\"key\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"rx_cache_stack_info\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StackInfoItem entity) {
        stmt.clearBindings();
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(1, key);
        }
 
        String stack = entity.getStack();
        if (stack != null) {
            stmt.bindString(2, stack);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StackInfoItem entity) {
        stmt.clearBindings();
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(1, key);
        }
 
        String stack = entity.getStack();
        if (stack != null) {
            stmt.bindString(2, stack);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public StackInfoItem readEntity(Cursor cursor, int offset) {
        StackInfoItem entity = new StackInfoItem( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // key
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // stack
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, StackInfoItem entity, int offset) {
        entity.setKey(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setStack(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final String updateKeyAfterInsert(StackInfoItem entity, long rowId) {
        return entity.getKey();
    }
    
    @Override
    public String getKey(StackInfoItem entity) {
        if(entity != null) {
            return entity.getKey();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(StackInfoItem entity) {
        return entity.getKey() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
