package com.cloud.dialogs.daos;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.cloud.dialogs.options.beans.OptionsItem;

import com.cloud.dialogs.daos.OptionsItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig optionsItemDaoConfig;

    private final OptionsItemDao optionsItemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        optionsItemDaoConfig = daoConfigMap.get(OptionsItemDao.class).clone();
        optionsItemDaoConfig.initIdentityScope(type);

        optionsItemDao = new OptionsItemDao(optionsItemDaoConfig, this);

        registerDao(OptionsItem.class, optionsItemDao);
    }
    
    public void clear() {
        optionsItemDaoConfig.clearIdentityScope();
    }

    public OptionsItemDao getOptionsItemDao() {
        return optionsItemDao;
    }

}
