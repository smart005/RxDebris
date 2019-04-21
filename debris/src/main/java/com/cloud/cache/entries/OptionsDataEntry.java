package com.cloud.cache.entries;

import com.cloud.cache.daos.DaoSession;
import com.cloud.cache.daos.OptionsItemDao;
import com.cloud.cache.events.OnDataChainRunnable;
import com.cloud.dialogs.options.beans.OptionsItem;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action2;
import com.cloud.objects.logs.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-04-19
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class OptionsDataEntry extends BaseCacheDao {

    private <T> void cacheDataDao(final Action2<OptionsItemDao, T[]> perform, T... extras) {
        try {
            addDataChain(new OnDataChainRunnable<OptionsItemDao, DaoSession>() {
                @Override
                public OptionsItemDao run(DaoSession daoSession) {
                    OptionsItemDao.createTable(daoSession.getDatabase(), true);
                    return daoSession.getOptionsItemDao();
                }

                @Override
                public void complete(OptionsItemDao optionsItemDao, Object... extras) {
                    if (perform == null) {
                        return;
                    }
                    perform.call(optionsItemDao, (T[]) extras);
                }
            }, extras);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public void insertOrReplace(Iterable<OptionsItem> entities) {
        cacheDataDao(new Action2<OptionsItemDao, Iterable<OptionsItem>[]>() {
            @Override
            public void call(OptionsItemDao optionsItemDao, Iterable<OptionsItem>... entities) {
                optionsItemDao.insertOrReplaceInTx(entities[0]);
            }
        }, entities);
    }

    public synchronized List<OptionsItem> getOptionsList(final OnDataChainRunnable<List<OptionsItem>, OptionsItemDao> runnable) {
        final List<OptionsItem> lst = new ArrayList<OptionsItem>();
        cacheDataDao(new Action2<OptionsItemDao, Object[]>() {
            @Override
            public void call(OptionsItemDao optionsItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                List<OptionsItem> result = runnable.run(optionsItemDao);
                if (ObjectJudge.isNullOrEmpty(result)) {
                    return;
                }
                lst.addAll(result);
            }
        });
        return lst;
    }
}
