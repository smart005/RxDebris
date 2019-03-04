package com.cloud.cache.greens;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.cloud.cache.MemoryCache;
import com.cloud.objects.config.RxAndroid;

import java.io.File;
import java.io.IOException;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/7
 * @Description:数据上下文,在初始化时传入
 * @Modifier:
 * @ModifyContent:
 */
public class DatabaseContext extends ContextWrapper {

    private OnDatabasePathListener onDatabasePathListener = null;

    public DatabaseContext(Context base, OnDatabasePathListener listener) {
        super(base);
        this.onDatabasePathListener = listener;
    }

    private File getDataDirectory(String databaseName) {
        //如果数据库初始时未指定存储目录则默认以内部缓存目录作为数据库的根目录
        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        String applicationRootDir = builder.getInternalCacheRootDir();
        File dir = new File(applicationRootDir);
        //目录不存在创建
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, databaseName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private OnDatabasePathListener getOnDatabasePathListener() {
        if (onDatabasePathListener == null) {
            onDatabasePathListener = MemoryCache.getInstance().getSoftCache("CacheDatabasePathListener");
        }
        return onDatabasePathListener;
    }

    @Override
    public File getDatabasePath(String name) {
        OnDatabasePathListener listener = getOnDatabasePathListener();
        if (listener == null) {
            return getDataDirectory(name);
        } else {
            File dir = listener.onDatabaseRootPath();
            if (dir == null) {
                return getDataDirectory(name);
            } else {
                File file = new File(dir, name);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return file;
            }
        }
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
}
