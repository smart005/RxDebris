package com.cloud.debrisTest;

import com.cloud.debris.BaseApplication;
import com.cloud.mixed.RxMixed;
import com.cloud.nets.OkRx;
import com.cloud.nets.beans.RequestErrorInfo;
import com.cloud.nets.events.OnAuthListener;
import com.cloud.nets.events.OnBeanParsingJsonListener;
import com.cloud.nets.events.OnConfigParamsListener;
import com.cloud.nets.events.OnGlobalReuqestHeaderListener;
import com.cloud.nets.events.OnHeaderCookiesListener;
import com.cloud.nets.events.OnRequestErrorListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.events.OnDirectoryBuildListener;
import com.cloud.objects.events.OnNetworkConnectListener;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.storage.DirectoryUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/2
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class TestApplication extends BaseApplication {

    private static TestApplication application;

    public static TestApplication getInstance() {
        return application;
    }

    @Override
    public void onApplicationCreated() {
        application = this;
        RxAndroid.getInstance()
                .setOnNetworkConnectListener(new OnNetworkConnectListener() {
                    @Override
                    public boolean isConnected() {
                        return NetworkUtils.isConnected(getApplicationContext());
                    }
                })
                //设置的目录;sdcard不存在取RxAndroid.setInternalCacheRootDir()设置的目录)
                //示例：images->[forum->[video,temp],user,comments]
                .setOnDirectoryBuildListener(new OnDirectoryBuildListener() {
                    @Override
                    public void onDirectoryBuild(DirectoryUtils directoryUtils) {
                        directoryUtils.addDirectory("images");
                        directoryUtils.addDirectory("shorVideo");
                        directoryUtils.addDirectory("videos");
                        directoryUtils.addDirectory("temporary");
                    }
                })
                .getBuilder()
                .setDebug(BuildConfig.DEBUG)
                //日志打印时统一标识
                .setLoggeruTag("gscloud")
                //缓存根目录
                .setCacheRootDir(getApplicationContext().getCacheDir().getAbsolutePath())
                //用于内部获取项目下BuildConfig信息
                .setProjectBuildConfigPackgeName(getPackageName())
                //应用安装后数据缓存根目录(在setApplicationRootDir()设置的目录下)
                .setRootDirName("gscloud")
                //缓存数据库名称
                .setDatabaseName("gscloud")
                .build();
        //数据库初始化
//        DBManager.getInstance().initialize(getApplicationContext(),
//                new OnDatabasePathListener() {
//                    @Override
//                    public File onDatabaseRootPath() {
//                        return StorageUtils.getDir("data");
//                    }
//                },
//                CacheDataItemDao.class,
//                StackInfoItemDao.class);
        //dao对象也可以通过以下方式绑定
        //.bindDaos();
        //网络框架初始化
        OkRx.getInstance().initialize(this)
                //true-对于请求失败跟踪日志带有设备相关配置信息;反之则不带;
                .setHasFirmwareConfigInformationForTraceLog(false)
                .setOnConfigParamsListener(new OnConfigParamsListener() {
                    @Override
                    public OkRxConfigParams onConfigParamsCall(OkRxConfigParams configParams) {
                        //是否进行网络状态码拦截(默认为false)
                        configParams.setNetStatusCodeIntercept(false);
                        return configParams;
                    }
                })
                .setOnBeanParsingJsonListener(new OnBeanParsingJsonListener() {
                    @Override
                    public Object onBeanParsingJson(String response, Class dataClass, boolean isCollectionDataType) {
                        //如果有父类嵌套子类的情况这里可自行处理
                        if (isCollectionDataType) {
                            return JsonUtils.parseArray(response, dataClass);
                        } else {
                            return JsonUtils.parseT(response, dataClass);
                        }
                    }
                })
                .setOnGlobalReuqestHeaderListener(new OnGlobalReuqestHeaderListener() {
                    @Override
                    public HashMap<String, String> onHeaderParams() {
                        return null;
                    }
                })
                .setOnRequestErrorListener(new OnRequestErrorListener() {
                    @Override
                    public void onFailure(RequestErrorInfo errorInfo) {
                        //网络请求失败回调
                    }
                })
                .setOnHeaderCookiesListener(new OnHeaderCookiesListener() {
                    @Override
                    public Map<String, String> onCookiesCall() {
                        Map<String, String> cookies = new HashMap<String, String>();
                        cookies.put("TC108Client", "ui=434060");
                        return cookies;
                    }
                })
                .setOnAuthListener(new OnAuthListener() {
                    @Override
                    public void onLoginCall(String requestMethodName) {
                        //当前接口请求需要用户登录时但未登录,会回调到这里统一处理;
                    }
                })
                .build();
        //x5内核
        RxMixed.getInstance().build(this);
    }

    @Override
    public void onAppSiwtchToBack() {

    }

    @Override
    public void onAppSiwtchToFront() {

    }

    @Override
    public void onReleaseLogIntercept(Throwable throwable) {
        Logger.error(throwable);
    }
}
