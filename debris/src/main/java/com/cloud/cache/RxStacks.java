package com.cloud.cache;

import android.text.TextUtils;

import com.cloud.cache.daos.StackInfoItemDao;
import com.cloud.cache.greens.DBManager;
import com.cloud.nets.beans.RequestErrorInfo;
import com.cloud.objects.logs.CrashUtils;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashMap;
import java.util.Iterator;
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
    private static String getStackKey(String prefixKey) {
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
        stackInfoItem.setKey(getStackKey(prefixKey));
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
     * 记录请求渠道信息
     *
     * @param tag           本次请求标识
     * @param requestType   请求类型
     * @param url           请求url
     * @param commonHeaders 公共头信息
     */
    public static void setRequestChainInfo(Object tag, String requestType, String url, HashMap<String, String> commonHeaders) {
        if (tag == null || !(tag instanceof String)) {
            return;
        }
        String key = getStackKey(String.format("%s_CHAIN", tag));
        StackInfoItem stackInfoItem = new StackInfoItem();
        stackInfoItem.setKey(key);
        stackInfoItem.setRequestType(requestType);
        stackInfoItem.setUrl(url);
        stackInfoItem.setHeaders(JsonUtils.toStr(commonHeaders));

        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            cacheDao.insertOrReplace(stackInfoItem);
            //使用完后关闭database
            DBManager.getInstance().close();
        }
    }

    /**
     * 记录请求基本信息
     *
     * @param prefixkey 存储在数据库中键的前缀
     * @param headers   请求头信息
     * @param params    请求参数
     * @param message   错误消息
     */
    public static void setRequestInfo(String prefixkey, Map<String, String> headers, Map<String, Object> params, String message) {
        if (TextUtils.isEmpty(prefixkey)) {
            return;
        }
        String key = getStackKey(String.format("%s_REQUEST_INFO", prefixkey));
        StackInfoItem stackInfoItem = new StackInfoItem();
        stackInfoItem.setKey(key);
        stackInfoItem.setHeaders(JsonUtils.toStr(headers));
        stackInfoItem.setParams(JsonUtils.toStr(params));
        stackInfoItem.setMessage(message);

        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            cacheDao.insertOrReplace(stackInfoItem);
            //使用完后关闭database
            DBManager.getInstance().close();
        }
    }

    /**
     * 设置请求状态和协议
     *
     * @param tag      本次请求标识
     * @param code     请求状态码
     * @param protocol 请求协议
     */
    public static void setRequestStateProtocol(Object tag, int code, String protocol) {
        if (tag == null || (tag instanceof String)) {
            return;
        }
        String key = getStackKey(String.format("%s_STATE_PROTOCOL", tag));
        StackInfoItem stackInfoItem = new StackInfoItem();
        stackInfoItem.setKey(key);
        stackInfoItem.setCode(code);
        stackInfoItem.setProtocol(protocol);

        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            cacheDao.insertOrReplace(stackInfoItem);
            //使用完后关闭database
            DBManager.getInstance().close();
        }
    }

    /**
     * 获取请求错误信息
     * (调用之后即刻清除历史数据)
     *
     * @param prefixKey 存储在数据库中键的前缀
     * @return RequestErrorInfo
     */
    public static RequestErrorInfo getRequestErrorInfos(String prefixKey) {
        RequestErrorInfo errorInfo = new RequestErrorInfo();
        if (TextUtils.isEmpty(prefixKey)) {
            return errorInfo;
        }
        TreeSet<String> stacks = errorInfo.getStacks();
        stacks.add(getCommonInfo());
        //prefixKey-这里指调用方法的方法名
        stacks.add(prefixKey);
        DbCacheDao dbCacheDao = new DbCacheDao();
        StackInfoItemDao cacheDao = dbCacheDao.getStackInfoItemDao();
        if (cacheDao != null) {
            QueryBuilder<StackInfoItem> builder = cacheDao.queryBuilder();
            builder.where(StackInfoItemDao.Properties.Key.like("%" + prefixKey + "%"));
            List<StackInfoItem> list = builder.list();
            if (list != null) {
                String chainKey = String.format("%s_CHAIN", prefixKey);
                String pkey = String.format("%s_STATE_PROTOCOL", prefixKey);
                String infoKey = String.format("%s_REQUEST_INFO", prefixKey);
                Iterator<StackInfoItem> iterator = list.iterator();
                while (iterator.hasNext()) {
                    StackInfoItem next = iterator.next();
                    if (next.getKey() == null) {
                        continue;
                    }
                    if (next.getKey().contains(chainKey)) {
                        //包含网络请求chain info
                        errorInfo.setRequestType(next.getRequestType());
                        errorInfo.setUrl(next.getUrl());
                        errorInfo.setCommonHeaders(next.getHeaders());
                        iterator.remove();
                    } else if (next.getKey().contains(pkey)) {
                        //包含网络请求协议部分
                        errorInfo.setStatus(next.getCode());
                        errorInfo.setRequestProtocol(next.getProtocol());
                        iterator.remove();
                    } else if (next.getKey().contains(infoKey)) {
                        //包含网络请求基本信息部分
                        errorInfo.setHeaders(next.getHeaders());
                        errorInfo.setParams(next.getParams());
                        errorInfo.setMessage(next.getMessage());
                    } else {
                        stacks.add(next.getStack());
                    }
                }
            }
            //删除所有符合条件数据
            DeleteQuery<StackInfoItem> delete = builder.buildDelete();
            delete.executeDeleteWithoutDetachingEntities();
        }
        return errorInfo;
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
            QueryBuilder<StackInfoItem> where = builder.where(StackInfoItemDao.Properties.Key.like("%" + prefixKey + "%"));
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
