package com.cloud.debrisTest;

import android.app.Application;

import com.cloud.cache.greens.DBManager;
import com.cloud.cache.greens.OnDatabasePathListener;
import com.cloud.nets.OkRx;
import com.cloud.nets.events.OnBeanParsingJsonListener;
import com.cloud.nets.events.OnConfigParamsListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.storage.StorageUtils;
import com.cloud.objects.utils.JsonUtils;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/2
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RxAndroid.getInstance().getBuilder()
                .setDebug(true)
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
                });
        //网络框架初始化
        OkRx.getInstance()
                .setOnConfigParamsListener(new OnConfigParamsListener() {
                    @Override
                    public OkRxConfigParams onConfigParamsCall(OkRx okRx) {
                        OkRxConfigParams configParams = okRx.getOkRxConfigParams();
                        //true-网络请求成功后内部处理相关状态;false-网络请求success后直接返回;
                        configParams.setProcessNetResults(false);
                        //是否进行网络状态码拦截(默认为false)
                        configParams.setNetStatusCodeIntercept(false);
                        //修改自定义配置
                        return configParams;
                    }
                })
                .setOnBeanParsingJsonListener(new OnBeanParsingJsonListener() {
                    @Override
                    public Object onBeanParsingJson(String response, Class dataClass) {
                        //如果有父类嵌套子类的情况这里可自行处理
                        Logger.info(response);
                        return JsonUtils.parseT(response, dataClass);
                    }
                }).initialize(this);
    }
}
