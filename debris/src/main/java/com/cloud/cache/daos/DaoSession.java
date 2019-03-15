package com.cloud.cache.daos;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.cloud.cache.CacheDataItem;
import com.cloud.dialogs.options.beans.OptionsItem;
import com.cloud.cache.StackInfoItem;

import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.cache.daos.OptionsItemDao;
import com.cloud.cache.daos.StackInfoItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cacheDataItemDaoConfig;
    private final DaoConfig optionsItemDaoConfig;
    private final DaoConfig stackInfoItemDaoConfig;

    private final CacheDataItemDao cacheDataItemDao;
    private final OptionsItemDao optionsItemDao;
    private final StackInfoItemDao stackInfoItemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        cacheDataItemDaoConfig = daoConfigMap.get(CacheDataItemDao.class).clone();
        cacheDataItemDaoConfig.initIdentityScope(type);

        optionsItemDaoConfig = daoConfigMap.get(OptionsItemDao.class).clone();
        optionsItemDaoConfig.initIdentityScope(type);

        stackInfoItemDaoConfig = daoConfigMap.get(StackInfoItemDao.class).clone();
        stackInfoItemDaoConfig.initIdentityScope(type);

        cacheDataItemDao = new CacheDataItemDao(cacheDataItemDaoConfig, this);
        optionsItemDao = new OptionsItemDao(optionsItemDaoConfig, this);
        stackInfoItemDao = new StackInfoItemDao(stackInfoItemDaoConfig, this);

        registerDao(CacheDataItem.class, cacheDataItemDao);
        registerDao(OptionsItem.class, optionsItemDao);
        registerDao(StackInfoItem.class, stackInfoItemDao);
    }
    
    public void clear() {
        cacheDataItemDaoConfig.clearIdentityScope();
        optionsItemDaoConfig.clearIdentityScope();
        stackInfoItemDaoConfig.clearIdentityScope();
    }

    public CacheDataItemDao getCacheDataItemDao() {
        return cacheDataItemDao;
    }

    public OptionsItemDao getOptionsItemDao() {
        return optionsItemDao;
    }

    public StackInfoItemDao getStackInfoItemDao() {
        return stackInfoItemDao;
    }

}
