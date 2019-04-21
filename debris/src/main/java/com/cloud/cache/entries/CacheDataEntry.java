package com.cloud.cache.entries;

import com.cloud.cache.CacheDataItem;
import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.cache.daos.DaoSession;
import com.cloud.cache.events.OnDataChainRunnable;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action2;
import com.cloud.objects.logs.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/18
 * Description:信息缓存操作类
 * Modifier:
 * ModifyContent:
 */
public class CacheDataEntry extends BaseCacheDao {

    private <T> void cacheDataDao(final Action2<CacheDataItemDao, T[]> perform, T... extras) {
        try {
            addDataChain(new OnDataChainRunnable<CacheDataItemDao, DaoSession>() {
                @Override
                public CacheDataItemDao run(DaoSession daoSession) {
                    CacheDataItemDao.createTable(daoSession.getDatabase(), true);
                    return daoSession.getCacheDataItemDao();
                }

                @Override
                public void complete(CacheDataItemDao cacheDataItemDao, Object... extras) {
                    if (perform == null) {
                        return;
                    }
                    perform.call(cacheDataItemDao, (T[]) extras);
                }
            }, extras);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 插入或更新缓存数据
     *
     * @param entities 数据集合
     */
    public void insertOrReplace(CacheDataItem... entities) {
        cacheDataDao(new Action2<CacheDataItemDao, CacheDataItem[]>() {
            @Override
            public void call(CacheDataItemDao cacheDataItemDao, CacheDataItem... entities) {
                cacheDataItemDao.insertOrReplaceInTx(entities);
            }
        }, entities);
    }

    public void deleteInTx(final OnDataChainRunnable<CacheDataItem[], CacheDataItemDao> runnable) {
        cacheDataDao(new Action2<CacheDataItemDao, Object[]>() {
            @Override
            public void call(CacheDataItemDao cacheDataItemDao, Object... entities) {
                if (runnable == null) {
                    return;
                }
                CacheDataItem[] dataItems = runnable.run(cacheDataItemDao);
                if (dataItems == null) {
                    return;
                }
                cacheDataItemDao.deleteInTx(dataItems);
            }
        });
    }

    public void deleteListInTx(final OnDataChainRunnable<List<CacheDataItem>, CacheDataItemDao> runnable) {
        cacheDataDao(new Action2<CacheDataItemDao, Object[]>() {
            @Override
            public void call(CacheDataItemDao cacheDataItemDao, Object... entities) {
                if (runnable == null) {
                    return;
                }
                List<CacheDataItem> dataItems = runnable.run(cacheDataItemDao);
                if (dataItems == null) {
                    return;
                }
                cacheDataItemDao.deleteInTx(dataItems);
            }
        });
    }

    public void delete(HashSet<String> keys) {
        cacheDataDao(new Action2<CacheDataItemDao, HashSet<String>[]>() {
            @Override
            public void call(CacheDataItemDao cacheDataItemDao, HashSet<String>... keys) {
                cacheDataItemDao.deleteByKeyInTx(keys[0]);
            }
        }, keys);
    }

    public void deleteAll() {
        cacheDataDao(new Action2<CacheDataItemDao, Object[]>() {
            @Override
            public void call(CacheDataItemDao cacheDataItemDao, Object... keys) {
                cacheDataItemDao.deleteAll();
            }
        });
    }

    /**
     * 获取缓存数据
     *
     * @param runnable 查询回调
     * @return CacheDataItem
     */
    public synchronized CacheDataItem getCacheData(final OnDataChainRunnable<CacheDataItem, CacheDataItemDao> runnable) {
        final CacheDataItem[] dataItems = {new CacheDataItem()};
        cacheDataDao(new Action2<CacheDataItemDao, Object[]>() {
            @Override
            public void call(CacheDataItemDao cacheDataItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                CacheDataItem dataItem = runnable.run(cacheDataItemDao);
                dataItems[0] = (dataItem == null ? new CacheDataItem() : dataItem);
            }
        });
        return dataItems[0];
    }

    /**
     * 获取缓存数据
     *
     * @param runnable 查询回调
     * @return CacheDataItem
     */
    public synchronized List<CacheDataItem> getCacheList(final OnDataChainRunnable<List<CacheDataItem>, CacheDataItemDao> runnable) {
        final List<CacheDataItem> lst = new ArrayList<CacheDataItem>();
        cacheDataDao(new Action2<CacheDataItemDao, Object[]>() {
            @Override
            public void call(CacheDataItemDao cacheDataItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                List<CacheDataItem> result = runnable.run(cacheDataItemDao);
                if (ObjectJudge.isNullOrEmpty(result)) {
                    return;
                }
                lst.addAll(result);
            }
        });
        return lst;
    }
}
