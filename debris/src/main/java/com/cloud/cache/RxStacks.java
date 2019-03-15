package com.cloud.cache;

import android.text.TextUtils;

import com.cloud.cache.daos.StackInfoItemDao;
import com.cloud.cache.greens.DBManager;
import com.cloud.objects.logs.CrashUtils;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.GlobalUtils;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:记录堆栈信息
 * Modifier:
 * ModifyContent:
 */
public class RxStacks {

    /**
     * 获取堆栈信息缓存key
     *
     * @param prefixKey key-prefix
     * @return 堆栈信息缓存key
     */
    public static String getStackKey(String prefixKey) {
        return String.format("%s_%s", prefixKey, GlobalUtils.getNewGuid());
    }

    /**
     * 设置公共信息
     *
     * @param isEffectiveDb 是否有效db
     */
    private static String setCommonInfo(boolean isEffectiveDb) {
        Map<String, Object> deviceInfo = CrashUtils.getProgramDeviceInfo();
        String join = ConvertUtils.toJoin(deviceInfo, "\n");
        if (isEffectiveDb) {
            StackInfoItem stackInfoItem = new StackInfoItem();
            stackInfoItem.setKey("PROGRAM_DEVICE_COMMON_INFO");
            stackInfoItem.setStack(join);

            DbCacheDao dbCacheDao = new DbCacheDao();
            StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
            if (cacheDao != null) {
                cacheDao.insertOrReplace(stackInfoItem);
                //使用完后关闭database
                DBManager.getInstance().close();
            }
        }
        return join;
    }

    /**
     * 获取公共信息
     *
     * @return 公共信息
     */
    public static String getCommonInfo() {
        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao == null) {
            String stackInfo = setCommonInfo(false);
            return stackInfo;
        }
        QueryBuilder<StackInfoItem> builder = cacheDao.queryBuilder();
        QueryBuilder<StackInfoItem> where = builder.where(StackInfoItemDao.Properties.Key.eq("PROGRAM_DEVICE_COMMON_INFO"));
        QueryBuilder<StackInfoItem> limit = where.limit(1);
        if (limit == null) {
            String stackInfo = setCommonInfo(true);
            return stackInfo;
        }
        StackInfoItem unique = limit.unique();
        if (unique == null) {
            String stackInfo = setCommonInfo(true);
            return stackInfo;
        }
        DBManager.getInstance().close();
        return unique.getStack();
    }

    /**
     * 保存堆栈信息
     *
     * @param prefixKey 存储在数据库中键的前缀
     * @param throwable 堆栈信息
     */
    public static void setStack(String prefixKey, Throwable throwable) {
        if (TextUtils.isEmpty(prefixKey) || throwable == null) {
            return;
        }
        StackInfoItem stackInfoItem = new StackInfoItem();
        stackInfoItem.setKey(prefixKey);
        stackInfoItem.setStack(CrashUtils.getCrashInfo(throwable));

        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            cacheDao.insertOrReplace(stackInfoItem);
            //使用完后关闭database
            DBManager.getInstance().close();
        }
    }

    /**
     * 获取所有堆栈信息
     * (调用之后即刻清除历史数据)
     *
     * @param prefixKey 存储在数据库中键的前缀
     * @return 堆栈集合
     */
    public static TreeSet<String> getAllStacks(String prefixKey) {
        TreeSet<String> set = new TreeSet<>();
        if (TextUtils.isEmpty(prefixKey)) {
            return set;
        }
        set.add(getCommonInfo());
        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            QueryBuilder<StackInfoItem> builder = cacheDao.queryBuilder();
            QueryBuilder<StackInfoItem> where = builder.where(StackInfoItemDao.Properties.Key.like(prefixKey));
            //最大限制50条记录
            QueryBuilder<StackInfoItem> limit = where.limit(50);
            if (limit != null) {
                List<StackInfoItem> list = limit.list();
                if (list != null) {
                    for (StackInfoItem item : list) {
                        set.add(item.getStack());
                    }
                }
            }
            //删除所有符合条件数据
            DeleteQuery<StackInfoItem> delete = where.buildDelete();
            delete.executeDeleteWithoutDetachingEntities();
        }
        return set;
    }

    /**
     * 清除所有业务栈
     *
     * @param prefixKey 存储在数据库中键的前缀
     */
    public static void clearBusStacks(String prefixKey) {
        if (TextUtils.isEmpty(prefixKey)) {
            return;
        }
        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            QueryBuilder<StackInfoItem> builder = cacheDao.queryBuilder();
            QueryBuilder<StackInfoItem> where = builder.where(StackInfoItemDao.Properties.Key.like(prefixKey));
            DeleteQuery<StackInfoItem> delete = where.buildDelete();
            delete.executeDeleteWithoutDetachingEntities();
        }
    }

    /**
     * 清除所有数据(一般用于应用被卸载)
     */
    public static void clear() {
        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            cacheDao.deleteAll();
        }
    }
}
