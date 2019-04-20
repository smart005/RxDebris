package com.cloud.cache.entries;

import com.cloud.cache.PathCacheInfoItem;
import com.cloud.cache.daos.DaoSession;
import com.cloud.cache.daos.PathCacheInfoItemDao;
import com.cloud.cache.events.OnDataChainRunnable;
import com.cloud.objects.events.Action2;
import com.cloud.objects.logs.Logger;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-04-19
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class PathCacheDataEntry extends BaseCacheDao {

    private <T> void cacheDataDao(final Action2<PathCacheInfoItemDao, T[]> perform, T... extras) {
        try {
            addDataChain(new OnDataChainRunnable<PathCacheInfoItemDao, DaoSession>() {
                @Override
                public PathCacheInfoItemDao run(DaoSession daoSession) {
                    PathCacheInfoItemDao.createTable(daoSession.getDatabase(), true);
                    return daoSession.getPathCacheInfoItemDao();
                }

                @Override
                public void complete(PathCacheInfoItemDao pathCacheInfoItemDao, Object... extras) {
                    if (perform == null) {
                        return;
                    }
                    perform.call(pathCacheInfoItemDao, (T[]) extras);
                }
            }, extras);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public void execute(final OnDataChainRunnable<Void, PathCacheInfoItemDao> runnable) {
        cacheDataDao(new Action2<PathCacheInfoItemDao, Object[]>() {
            @Override
            public void call(PathCacheInfoItemDao pathCacheInfoItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                runnable.run(pathCacheInfoItemDao);
            }
        });
    }

    public synchronized PathCacheInfoItem getPathCacheInfo(final OnDataChainRunnable<PathCacheInfoItem, PathCacheInfoItemDao> runnable) {
        final PathCacheInfoItem[] dataItems = {new PathCacheInfoItem()};
        cacheDataDao(new Action2<PathCacheInfoItemDao, Object[]>() {
            @Override
            public void call(PathCacheInfoItemDao pathCacheInfoItemDao, Object... extras) {
                if (runnable == null) {
                    return;
                }
                PathCacheInfoItem dataItem = runnable.run(pathCacheInfoItemDao);
                dataItems[0] = (dataItem == null ? new PathCacheInfoItem() : dataItem);
            }
        });
        return dataItems[0];
    }

    public void insertOrReplace(PathCacheInfoItem... entities) {
        cacheDataDao(new Action2<PathCacheInfoItemDao, PathCacheInfoItem[]>() {
            @Override
            public void call(PathCacheInfoItemDao pathCacheInfoItemDao, PathCacheInfoItem... entities) {
                pathCacheInfoItemDao.insertOrReplaceInTx(entities);
            }
        }, entities);
    }
}
