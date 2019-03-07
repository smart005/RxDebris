package com.cloud.cache;

import android.text.TextUtils;

import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.JsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/10/14
 * Description:
 * Modifier:
 * ModifyContent:
 */

public class RxCache {

    /**
     * 设置缓存数据
     *
     * @param cacheKey 缓存键
     * @param value    缓存数据
     * @param saveTime 缓存时间
     * @param timeUnit 时间单位
     */
    public static <T> void setBaseCacheData(String cacheKey, T value, long saveTime, TimeUnit timeUnit) {
        setBaseCacheData(cacheKey, value, saveTime, timeUnit, 0);
    }

    /**
     * 设置缓存数据
     *
     * @param cacheKey          缓存键
     * @param value             缓存数据
     * @param saveTime          缓存时间
     * @param timeUnit          时间单位
     * @param intervalCacheTime 间隔缓存时间(单位毫秒)
     */
    public static <T> void setBaseCacheData(String cacheKey, T value, long saveTime, TimeUnit timeUnit, long intervalCacheTime) {
        try {
            if (TextUtils.isEmpty(cacheKey) || value == null) {
                return;
            }
            remove(cacheKey);
            CacheDataItem dataItem = new CacheDataItem();
            dataItem.setStartTime(System.currentTimeMillis());
            dataItem.setIntervalCacheTime(intervalCacheTime);
            if (saveTime > 0 && timeUnit != null) {
                long time = ConvertUtils.toMilliseconds(saveTime, timeUnit);
                if (time > 0) {
                    long mtime = System.currentTimeMillis() + time;
                    dataItem.setKey(cacheKey);
                    setCacheValue(value, dataItem);
                    dataItem.setEffective(mtime);
                } else {
                    dataItem.setKey(cacheKey);
                    setCacheValue(value, dataItem);
                }
            } else {
                dataItem.setKey(cacheKey);
                setCacheValue(value, dataItem);
            }
            DbCacheDao dbCacheDao = new DbCacheDao();
            CacheDataItemDao cacheDao = dbCacheDao.getCacheDataItemDao(false);
            if (cacheDao != null) {
                CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
                cacheDao.insertOrReplace(dataItem);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static <T> void setCacheValue(T value, CacheDataItem dataItem) {
        if (value instanceof String) {
            dataItem.setValue(String.valueOf(value));
        } else if (value instanceof Boolean) {
            dataItem.setFlag((Boolean) value);
        } else if (value instanceof Integer) {
            dataItem.setIniValue(ConvertUtils.toInt(value));
        } else if (value instanceof Long) {
            dataItem.setLongValue(ConvertUtils.toLong(value));
        }
    }

    /**
     * 获取缓存数据
     *
     * @param cacheKey 缓存键
     * @return CacheDataItem
     */
    public static CacheDataItem getBaseCacheData(String cacheKey) {
        return getBaseCacheData(cacheKey, false);
    }

    /**
     * 获取缓存数据
     *
     * @param
     * @param cacheKey      缓存键
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     *                      return
     */
    public static CacheDataItem getBaseCacheData(String cacheKey, boolean isLimitations) {
        try {
            if (TextUtils.isEmpty(cacheKey)) {
                return null;
            }
            CacheDataItem first = null;
            DbCacheDao dbCacheDao = new DbCacheDao();
            CacheDataItemDao cacheDao = dbCacheDao.getCacheDataItemDao(false);
            if (cacheDao != null) {
                CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
                QueryBuilder<CacheDataItem> builder = cacheDao.queryBuilder();
                QueryBuilder<CacheDataItem> where = builder.where(CacheDataItemDao.Properties.Key.eq(cacheKey));
                QueryBuilder<CacheDataItem> limit = where.limit(1);
                if (limit != null) {
                    first = limit.unique();
                }
            }
            if (first == null) {
                return new CacheDataItem();
            }
            if (isLimitations) {
                if (first.getEffective() > 0) {
                    if (first.getEffective() > System.currentTimeMillis()) {
                        return first;
                    } else {
                        clear(true, cacheKey);
                        return new CacheDataItem();
                    }
                } else {
                    clear(true, cacheKey);
                    return new CacheDataItem();
                }
            } else {
                if (first.getEffective() > 0) {
                    if (first.getEffective() > System.currentTimeMillis()) {
                        return first;
                    } else {
                        clear(true, cacheKey);
                        return new CacheDataItem();
                    }
                } else {
                    return first;
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return new CacheDataItem();
    }

    /**
     * 获取缓存数据
     * <p>
     *
     * @param
     * @param cacheKey      缓存键
     * @param isLimitations
     * @return
     */
    public static String getCacheData(String cacheKey, boolean isLimitations) {
        CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
        if (dataItem == null) {
            return "";
        } else {
            return dataItem.getValue();
        }
    }

    /**
     * 获取缓存数据
     * <p>
     *
     * @param
     * @param cacheKey 缓存键
     * @return
     */
    public static String getCacheData(String cacheKey) {
        return getCacheData(cacheKey, false);
    }

    /**
     * 设置缓存数据
     * <p>
     * param cacheKey 缓存键
     * param value    缓存数据
     * param saveTime 缓存时间
     * param timeUnit 时间单位
     */
    public static void setCacheData(String cacheKey, String value, long saveTime, TimeUnit timeUnit) {
        setBaseCacheData(cacheKey, value, saveTime, timeUnit);
    }

    /**
     * 设置缓存数据
     * <p>
     * param cacheKey 缓存键
     * param value    缓存数据
     */
    public static void setCacheData(String cacheKey, String value) {
        setBaseCacheData(cacheKey, value, 0, null);
    }

    /**
     * 清空所有缓存
     */
    public static void clear() {
        try {
            DbCacheDao dbCacheDao = new DbCacheDao();
            CacheDataItemDao cacheDao = dbCacheDao.getCacheDataItemDao(false);
            if (cacheDao != null) {
                CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
                cacheDao.deleteAll();
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static void clear(boolean isBlurClear, String containsKey) {
        try {
            DbCacheDao dbCacheDao = new DbCacheDao();
            CacheDataItemDao cacheDao = dbCacheDao.getCacheDataItemDao(false);
            if (cacheDao != null) {
                CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
                if (isBlurClear) {
                    QueryBuilder<CacheDataItem> builder = cacheDao.queryBuilder();
                    QueryBuilder<CacheDataItem> where = builder.where(CacheDataItemDao.Properties.Key.like(containsKey));
                    List<CacheDataItem> dataItems = where.list();
                    if (!ObjectJudge.isNullOrEmpty(dataItems)) {
                        cacheDao.deleteInTx(dataItems);
                    }
                } else {
                    QueryBuilder<CacheDataItem> builder = cacheDao.queryBuilder();
                    QueryBuilder<CacheDataItem> where = builder.where(CacheDataItemDao.Properties.Key.eq(containsKey));
                    QueryBuilder<CacheDataItem> limit = where.limit(1);
                    if (limit != null) {
                        CacheDataItem unique = limit.unique();
                        if (unique == null) {
                            cacheDao.delete(unique);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static void clearForKey(String key) {
        clear(false, key);
    }

    public static void clearContainerKey(String containsKey) {
        clear(true, containsKey);
    }

    /**
     * 移除指定缓存
     * <p>
     * param cacheKey 缓存key
     */
    public static void remove(String cacheKey) {
        try {
            DbCacheDao dbCacheDao = new DbCacheDao();
            if (dbCacheDao != null) {
                CacheDataItemDao cacheDao = dbCacheDao.getCacheDataItemDao(false);
                if (cacheDao != null) {
                    CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
                    QueryBuilder<CacheDataItem> builder = cacheDao.queryBuilder();
                    QueryBuilder<CacheDataItem> where = builder.where(CacheDataItemDao.Properties.Key.eq(cacheKey));
                    QueryBuilder<CacheDataItem> limit = where.limit(1);
                    if (limit != null) {
                        CacheDataItem unique = limit.unique();
                        if (unique != null) {
                            cacheDao.delete(unique);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 设置缓存数据
     * <p>
     * param cacheKey 缓存键
     * param value    缓存数据
     * param saveTime 缓存时间
     * param timeUnit 时间单位
     */
    public static void setJsonObject(
            String cacheKey,
            JSONObject value,
            long saveTime,
            TimeUnit timeUnit) {
        if (TextUtils.isEmpty(cacheKey) || value == null) {
            return;
        }
        setBaseCacheData(cacheKey, value.toString(), saveTime, timeUnit);
    }

    /**
     * 设置缓存数据
     * <p>
     * param cacheKey 缓存键
     * param value    缓存数据
     */
    public static void setJsonObject(String cacheKey, JSONObject value) {
        if (TextUtils.isEmpty(cacheKey) || value == null) {
            return;
        }
        setBaseCacheData(cacheKey, value.toString(), 0, null);
    }

    /**
     * 获取缓存数据
     * <p>
     *
     * @param cacheKey      缓存键
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     * @return
     */
    public static JSONObject getJsonObject(String cacheKey, boolean isLimitations) {
        try {
            CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
            return dataItem == null ? new JSONObject() : new JSONObject(dataItem.getValue());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONObject getJsonObject(String cacheKey) {
        return getJsonObject(cacheKey, false);
    }

    /**
     * 设置缓存数据
     * <p>
     * param cacheKey 缓存键
     * param value    缓存数据
     * param saveTime 缓存时间
     * param timeUnit 时间单位
     */
    public static void setJsonArray(String cacheKey, JSONArray value, long saveTime, TimeUnit timeUnit) {
        if (TextUtils.isEmpty(cacheKey) || value == null) {
            return;
        }
        setBaseCacheData(cacheKey, value.toString(), saveTime, timeUnit);
    }

    /**
     * 设置缓存数据
     * <p>
     * param cacheKey 缓存键
     * param value    缓存数据
     */
    public static void setJsonArray(String cacheKey, JSONArray value) {
        setJsonArray(cacheKey, value, 0, null);
    }

    /**
     * 获取缓存数据
     * <p>
     *
     * @param cacheKey      缓存键
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     * @return
     */
    public static JSONArray getJsonArray(String cacheKey, boolean isLimitations) {
        try {
            CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
            return dataItem == null ? new JSONArray() : new JSONArray(dataItem.getValue());
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    public static JSONArray getJsonArray(String cacheKey) {
        return getJsonArray(cacheKey, false);
    }

    public static <T> void setCacheObject(
            String cacheKey,
            T data,
            long saveTime,
            TimeUnit timeUnit) {
        if (data == null) {
            return;
        }
        String value = JsonUtils.toStr(data);
        setBaseCacheData(cacheKey, value, saveTime, timeUnit);
    }

    /**
     * @param
     * @param cacheKey      缓存key
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     * @param dataClass     返回的类类型
     * @param <T>
     * @return
     */
    public static <T> T getCacheObject(String cacheKey, boolean isLimitations, Class<T> dataClass) {
        CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
        return dataItem == null ? JsonUtils.newNull(dataClass) : JsonUtils.parseT(dataItem.getValue(), dataClass);
    }

    public static <T> T getCacheObject(String cacheKey, Class<T> dataClass) {
        return getCacheObject(cacheKey, false, dataClass);
    }

    /**
     * @param
     * @param cacheKey      缓存key
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     * @param dataClass     返回的类类型
     * @param <T>
     * @return
     */
    public static <T> List<T> getCacheList(String cacheKey, boolean isLimitations, Class<T> dataClass) {
        CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
        return dataItem == null ? new ArrayList<T>() : JsonUtils.parseArray(dataItem.getValue(), dataClass);
    }

    public static <T> List<T> getCacheList(String cacheKey, Class<T> dataClass) {
        return getCacheList(cacheKey, false, dataClass);
    }

    public static void setCacheFlag(String cacheKey, boolean flag) {
        setBaseCacheData(cacheKey, flag, 0, null);
    }

    /**
     * @param
     * @param cacheKey      缓存key
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     * @param defaultValue  默认值
     * @return
     */
    public static boolean getCacheFlag(String cacheKey, boolean isLimitations, boolean defaultValue) {
        CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
        return dataItem == null ? defaultValue : dataItem.getFlag();
    }

    public static boolean getCacheFlag(String cacheKey, boolean defaultValue) {
        return getCacheFlag(cacheKey, false, defaultValue);
    }

    public static boolean getCacheFlag(String cacheKey) {
        return getCacheFlag(cacheKey, false);
    }

    public static void setCacheInt(String cacheKey, int value) {
        setBaseCacheData(cacheKey, value, 0, null);
    }

    /**
     * @param
     * @param cacheKey      缓存key
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     * @return
     */
    public static int getCacheInt(String cacheKey, boolean isLimitations) {
        CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
        int value = dataItem == null ? 0 : dataItem.getIniValue();
        return value;
    }

    public static int getCacheInt(String cacheKey) {
        return getCacheInt(cacheKey, false);
    }

    public static void setCacheLong(String cacheKey, long value) {
        setBaseCacheData(cacheKey, value, 0, null);
    }

    /**
     * @param
     * @param cacheKey      缓存key
     * @param isLimitations true必须通过有效验证;false有效为0则不验证否则进行时效验证;
     * @return
     */
    public static long getCacheLong(String cacheKey, boolean isLimitations) {
        CacheDataItem dataItem = getBaseCacheData(cacheKey, isLimitations);
        long value = dataItem == null ? 0 : dataItem.getLongValue();
        return value;
    }

    public static long getCacheLong(String cacheKey) {
        return getCacheLong(cacheKey, false);
    }
}
