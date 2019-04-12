package com.cloud.debrisTest;

import com.cloud.cache.daos.CacheDataItemDao;
import com.cloud.cache.daos.OptionsItemDao;
import com.cloud.cache.daos.StackInfoItemDao;
import com.cloud.cache.greens.DBManager;
import com.cloud.cache.greens.OnDatabasePathListener;
import com.cloud.debris.BaseApplication;
import com.cloud.debrisTest.images.ImageSuffixCombination;
import com.cloud.images.RxImage;
import com.cloud.images.events.OnImageDirectoryListener;
import com.cloud.mixed.RxMixed;
import com.cloud.nets.OkRx;
import com.cloud.nets.beans.RequestErrorInfo;
import com.cloud.nets.events.OnBeanParsingJsonListener;
import com.cloud.nets.events.OnConfigParamsListener;
import com.cloud.nets.events.OnGlobalReuqestHeaderListener;
import com.cloud.nets.events.OnHeaderCookiesListener;
import com.cloud.nets.events.OnRequestErrorListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.events.OnNetworkConnectListener;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.storage.DirectoryUtils;
import com.cloud.objects.storage.StorageUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.NetworkUtils;

import java.io.File;
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

    @Override
    public void onApplicationCreated() {
        RxAndroid.getInstance()
                .setOnNetworkConnectListener(new OnNetworkConnectListener() {
                    @Override
                    public boolean isConnected() {
                        return NetworkUtils.isConnected(getApplicationContext());
                    }
                })
                .getBuilder()
                .setDebug(BuildConfig.DEBUG)
                //日志打印时统一标识
                .setLoggeruTag("gscloud")
                //内部缓存根目录
                .setInternalCacheRootDir(getApplicationContext().getCacheDir().getAbsolutePath())
                //外部缓存根目录
                .setExternalCacheRootDir(getExternalCacheDir().getAbsolutePath())
                //用于内部获取项目下BuildConfig信息
                .setProjectBuildConfigPackgeName(getPackageName())
                //应用安装后数据缓存根目录(在setApplicationRootDir()设置的目录下)
                .setRootDirName("gscloud")
                //缓存数据库名称
                .setDatabaseName("gscloud")
                .build();
        DBManager.getInstance().initialize(getApplicationContext(),
                new OnDatabasePathListener() {
                    @Override
                    public File onDatabaseRootPath() {
                        return StorageUtils.getDir("data");
                    }
                },
                CacheDataItemDao.class,
                OptionsItemDao.class,
                StackInfoItemDao.class);
        //dao对象也可以通过以下方式绑定
        //.bindDaos();
        //网络框架初始化
        OkRx.getInstance().initialize(this)
                //true-对于请求失败跟踪日志带有设备相关配置信息;反之则不带;
                .setHasFirmwareConfigInformationForTraceLog(false)
                .setOnConfigParamsListener(new OnConfigParamsListener() {
                    @Override
                    public OkRxConfigParams onConfigParamsCall(OkRxConfigParams configParams) {
                        //true-网络请求成功后内部处理相关状态;false-网络请求success后直接返回;
                        configParams.setProcessNetResults(false);
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
//                        cookies.put("hhwl_cookies", "108社区");
                        cookies.put("TC108Client", "ui=434060");
                        return cookies;
                    }
                })
                .build();
        //x5内核
        RxMixed.getInstance().build(this);
        //图片配置
        RxImage.getInstance().getBuilder()
                //图片缓存目录名称(根目录:sdcard存在取RxAndroid.setExternalCacheRootDir()
                //设置的目录;sdcard不存在取RxAndroid.setInternalCacheRootDir()设置的目录)
                //示例：images->[forum->[video,temp],user,comments]
                .setImageDirectories("images", new OnImageDirectoryListener() {
                    @Override
                    public void onImageDirectoryBuild(DirectoryUtils directoryUtils) {
                        directoryUtils.addChildDirectory("forum")
                                .addDirectory("user")
                                .buildDirectories();
                    }
                })
                //用于glide请求远程图片时追加第三方优化后缀(如阿里、七牛等)
                .setOnImageUrlCombinationListener(new ImageSuffixCombination());
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
