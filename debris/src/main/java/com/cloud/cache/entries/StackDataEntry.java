package com.cloud.cache.entries;

import com.cloud.cache.StackInfoItem;
import com.cloud.cache.daos.DaoSession;
import com.cloud.cache.daos.StackInfoItemDao;
import com.cloud.cache.events.OnDataChainRunnable;
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
public class StackDataEntry extends BaseCacheDao {
    private <T> void cacheDataDao(final Action2<StackInfoItemDao, T[]> perform, T... extras) {
        try {
            addDataChain(new OnDataChainRunnable<StackInfoItemDao, DaoSession>() {
                @Override
                public StackInfoItemDao run(DaoSession daoSession) {
                    StackInfoItemDao.createTable(daoSession.getDatabase(), true);
                    return daoSession.getStackInfoItemDao();
                }

                @Override
                public void complete(StackInfoItemDao stackInfoItemDao, Object... extras) {
                    if (perform == null) {
                        return;
                    }
                    perform.call(stackInfoItemDao, (T[]) extras);
                }
            }, extras);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public void insertOrReplace(StackInfoItem... entities) {
        cacheDataDao(new Action2<StackInfoItemDao, StackInfoItem[]>() {
            @Override
            public void call(StackInfoItemDao stackInfoItemDao, StackInfoItem... entities) {
                stackInfoItemDao.insertOrReplaceInTx(entities);
            }
        }, entities);
    }

    public synchronized StackInfoItem getStackInfo(final OnDataChainRunnable<StackInfoItem, StackInfoItemDao> runnable) {
        final StackInfoItem[] dataItems = {new StackInfoItem()};
        cacheDataDao(new Action2<StackInfoItemDao, Object[]>() {
            @Override
            public void call(StackInfoItemDao stackInfoItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                StackInfoItem dataItem = runnable.run(stackInfoItemDao);
                dataItems[0] = (dataItem == null ? new StackInfoItem() : dataItem);
            }
        });
        return dataItems[0];
    }

    public synchronized List<StackInfoItem> getStackList(final OnDataChainRunnable<List<StackInfoItem>, StackInfoItemDao> runnable) {
        final List<StackInfoItem> lst = new ArrayList<StackInfoItem>();
        cacheDataDao(new Action2<StackInfoItemDao, Object[]>() {
            @Override
            public void call(StackInfoItemDao stackInfoItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                List<StackInfoItem> result = runnable.run(stackInfoItemDao);
                if (ObjectJudge.isNullOrEmpty(result)) {
                    return;
                }
                lst.addAll(result);
            }
        });
        return lst;
    }

    public void deleteListInTx(final OnDataChainRunnable<List<StackInfoItem>, StackInfoItemDao> runnable) {
        cacheDataDao(new Action2<StackInfoItemDao, Object[]>() {
            @Override
            public void call(StackInfoItemDao stackInfoItemDao, Object... entities) {
                if (runnable == null) {
                    return;
                }
                List<StackInfoItem> dataItems = runnable.run(stackInfoItemDao);
                if (dataItems == null) {
                    return;
                }
                stackInfoItemDao.deleteInTx(dataItems);
            }
        });
    }

    public void execute(final OnDataChainRunnable<Void, StackInfoItemDao> runnable) {
        cacheDataDao(new Action2<StackInfoItemDao, Object[]>() {
            @Override
            public void call(StackInfoItemDao stackInfoItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                runnable.run(stackInfoItemDao);
            }
        });
    }
}
