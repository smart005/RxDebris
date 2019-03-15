package com.cloud.objects.config;

import android.text.TextUtils;

import com.cloud.cache.MemoryCache;
import com.cloud.objects.events.OnNetworkConnectListener;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/12
 * Description:框架参数配置工具类
 * Modifier:
 * ModifyContent:
 */
public class RxAndroid {

    private static RxAndroid rxAndroid = null;
    private RxAndroidBuilder builder = null;
    //保存部分基本信息
    private HashMap<String, Object> softConfigMap = new HashMap<String, Object>();
    private SoftReference<HashMap<String, Object>> softConfig = new SoftReference<HashMap<String, Object>>(softConfigMap);
    //网络状态事件监听
    private OnNetworkConnectListener onNetworkConnectListener = null;

    private RxAndroid() {
        //外部不能直接实例
    }

    public class RxAndroidBuilder {
        /**
         * 项目构建配置类包名(用于获取基本配置信息)
         */
        private String projectBuildConfigPackgeName = null;
        /**
         * http header参数名
         */
        private String[] httpHeaderParamNames = null;
        //根目录名
        private String rootDirName = "";
        /**
         * 内部缓存根目录
         */
        private String internalCacheRootDir = "";
        /**
         * 外部缓存根目录
         */
        private String externalCacheRootDir = "";
        /**
         * 是否debug
         */
        private boolean isDebug = false;
        /**
         * 是否beta版本
         * true-对于全局日志即便isDebug==false也会记录在本地文件目录中;false-不作记录;
         */
        private boolean isBetaVersion = false;
        /**
         * 日志统一tag显示名
         */
        private String loggeruTag = "";
        /**
         * 缓存数据库名称
         */
        private String databaseName = "";

        private RxAndroidBuilder() {
            //不允许外部实例
        }

        private <T> T getParamsObject(String key) {
            HashMap<String, Object> map = softConfig.get();
            if (map == null || !map.containsKey(key)) {
                return null;
            }
            Object o = map.get(key);
            return (T) o;
        }

        public String getProjectBuildConfigPackgeName() {
            projectBuildConfigPackgeName = getParamsObject("BuildConfigPackgeName");
            return projectBuildConfigPackgeName;
        }

        public RxAndroidBuilder setProjectBuildConfigPackgeName(String projectBuildConfigPackgeName) {
            this.projectBuildConfigPackgeName = projectBuildConfigPackgeName;
            return this;
        }

        public String[] getHttpHeaderParamNames() {
            httpHeaderParamNames = getParamsObject("HttpHeaderParamName");
            return httpHeaderParamNames;
        }

        public RxAndroidBuilder setHttpHeaderParamNames(String... httpHeaderParamNames) {
            this.httpHeaderParamNames = httpHeaderParamNames;
            return this;
        }

        /**
         * 获取根目录名称
         *
         * @return
         */
        public String getRootDirName() {
            rootDirName = getParamsObject("RootDirName");
            if (TextUtils.isEmpty(rootDirName)) {
                rootDirName = "cloudRoot";
            }
            return this.rootDirName;
        }

        public RxAndroidBuilder setRootDirName(String rootDirName) {
            this.rootDirName = rootDirName;
            return this;
        }

        public String getInternalCacheRootDir() {
            if (TextUtils.isEmpty(internalCacheRootDir)) {
                internalCacheRootDir = getParamsObject("InternalCacheRootDir");
            }
            return internalCacheRootDir;
        }

        public RxAndroidBuilder setInternalCacheRootDir(String internalCacheRootDir) {
            this.internalCacheRootDir = internalCacheRootDir;
            return this;
        }

        public String getExternalCacheRootDir() {
            if (TextUtils.isEmpty(externalCacheRootDir)) {
                externalCacheRootDir = getParamsObject("ExternalCacheRootDir");
            }
            return externalCacheRootDir;
        }

        public RxAndroidBuilder setExternalCacheRootDir(String externalCacheRootDir) {
            this.externalCacheRootDir = externalCacheRootDir;
            return this;
        }

        public boolean isDebug() {
            Boolean debug = getParamsObject("Debug");
            if (debug == null) {
                isDebug = false;
            } else {
                isDebug = debug;
            }
            return isDebug;
        }

        public RxAndroidBuilder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        public String getLoggeruTag() {
            if (TextUtils.isEmpty(loggeruTag)) {
                loggeruTag = getParamsObject("LoggeruTag");
            }
            return loggeruTag;
        }

        public RxAndroidBuilder setLoggeruTag(String loggeruTag) {
            this.loggeruTag = loggeruTag;
            return this;
        }

        /**
         * 获取缓存数据库名称
         *
         * @return 数据库名称
         */
        public String getDatabaseName() {
            if (TextUtils.isEmpty(databaseName)) {
                databaseName = getParamsObject("CacheDatabaseName");
            }
            return databaseName;
        }

        /**
         * 设置缓存数据库名称
         *
         * @param databaseName 数据库名称
         */
        public RxAndroidBuilder setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public boolean isBetaVersion() {
            Boolean betaVersion = getParamsObject("BetaVersion");
            if (betaVersion == null) {
                isBetaVersion = false;
            } else {
                isBetaVersion = betaVersion;
            }
            return isBetaVersion;
        }

        public RxAndroidBuilder setBetaVersion(boolean betaVersion) {
            isBetaVersion = betaVersion;
            return this;
        }

        /**
         * 对部分信息持久化
         */
        public void build() {
            if (builder == null) {
                return;
            }
            HashMap<String, Object> map = softConfig.get();
            if (map == null) {
                softConfig = new SoftReference<HashMap<String, Object>>(softConfigMap);
                map = softConfig.get();
                if (map == null) {
                    return;
                }
            }
            //缓存配置类信息
            map.put("BuildConfigPackgeName", projectBuildConfigPackgeName);
            //http header参数
            map.put("HttpHeaderParamName", httpHeaderParamNames);
            //根目录名
            map.put("RootDirName", rootDirName);
            //内部缓存根目录
            map.put("InternalCacheRootDir", internalCacheRootDir);
            //外部缓存根目录
            map.put("ExternalCacheRootDir", externalCacheRootDir);
            //调试状态
            map.put("Debug", isDebug);
            //日志全局标识
            map.put("LoggeruTag", loggeruTag);
            //缓存数据库名称
            map.put("CacheDatabaseName", databaseName);
            //设置beta状态
            map.put("BetaVersion", isBetaVersion);
        }
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static RxAndroid getInstance() {
        return rxAndroid == null ? rxAndroid = new RxAndroid() : rxAndroid;
    }

    /**
     * 回收
     */
    public void recycliing() {
        Recycling recycling = new Recycling();
        //引用回收
        recycling.referenceRecycling();

        //自身引用
        rxAndroid = null;
    }

    /**
     * 获取信息构建对象
     *
     * @return
     */
    public RxAndroidBuilder getBuilder() {
        if (builder == null) {
            builder = new RxAndroidBuilder();
        }
        return builder;
    }

    /**
     * 获取网络连接监听
     *
     * @return 网络连接监听
     */
    public OnNetworkConnectListener getOnNetworkConnectListener() {
        if (onNetworkConnectListener == null) {
            Object connectListener = MemoryCache.getInstance().getSoftCache("$_NetworkConnectListener");
            if (connectListener instanceof OnNetworkConnectListener) {
                onNetworkConnectListener = (OnNetworkConnectListener) connectListener;
            }
        }
        return onNetworkConnectListener;
    }

    /**
     * 设置网络连接监听
     *
     * @param listener 网络连接监听
     * @return RxAndroid
     */
    public RxAndroid setOnNetworkConnectListener(OnNetworkConnectListener listener) {
        this.onNetworkConnectListener = listener;
        MemoryCache.getInstance().setSoftCache("$_NetworkConnectListener", listener);
        return this;
    }
}
