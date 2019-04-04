package com.cloud.debrisTest.okhttp.services;

import com.cloud.debris.portfolio.StorageManager;
import com.cloud.debrisTest.okhttp.UrlsProvider;
import com.cloud.debrisTest.okhttp.annotations.IGetAPI;
import com.cloud.debrisTest.okhttp.beans.RecommandInfo;
import com.cloud.nets.BaseOkrxService;
import com.cloud.nets.BaseSubscriber;
import com.cloud.nets.annotations.APIUrlInterfaceClass;
import com.cloud.nets.annotations.ApiCheckAnnotation;
import com.cloud.nets.annotations.RequestTimeLimit;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.events.OnSuccessfulListener;
import com.cloud.objects.events.Func2;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:网络请求测试类
 * Modifier:
 * ModifyContent:
 */
@APIUrlInterfaceClass(UrlsProvider.class)
public class GetService extends BaseOkrxService {

    //若不需要token验证则加注解即可
    //@ApiCheckAnnotation
    //网络请求-不做缓存
    //@ApiCheckAnnotation(isTokenValid = true)
    //网络缓存会根据设置的cacheKey+请求条件作缓存处理;(如[recommand_info_siteId_42_pageSize_1_pageNumber_20])
    //网络请求-在缓存未失效时网络数据与缓存只会返回其中一个,缓存失效后先请求网络->再缓存->最后返回;
    //@ApiCheckAnnotation(callStatus = CallStatus.OnlyCache, cacheTime = 1, cacheTimeUnit = TimeUnit.MINUTES, cacheKey = "recommand_info")
    //网络请求-在缓存未失败时获取到网络数据和缓存数据均会回调,缓存失效后先请求网络->再缓存->最后返回(即此时只作网络数据的回调);
    //@ApiCheckAnnotation(callStatus = CallStatus.PersistentIntervalCache, cacheIntervalTime = 10000, cacheTime = 1, cacheTimeUnit = TimeUnit.MINUTES, cacheKey = "recommand_info")
    //请求时间限制;可在请求方法或接口定义上添加;接口定义上设置的值优先于请求方法设置的值;
    @ApiCheckAnnotation(callStatus = CallStatus.PersistentIntervalCache)
    @RequestTimeLimit(totalTime = "2", unit = TimeUnit.SECONDS)
    public void requestRecommandInfo(final int siteId, OnSuccessfulListener<RecommandInfo> successfulListener) {
        //请求订阅对象
        BaseSubscriber baseSubscriber = new BaseSubscriber<RecommandInfo, GetService>(this);
        //设置成功监听器
        baseSubscriber.setOnSuccessfulListener(successfulListener);
        //设置扩展数据(将在回调中返回)
        //baseSubscriber.setExtra("");
        //请求api
        requestObject(IGetAPI.class, this, baseSubscriber, new Func2<RetrofitParams, IGetAPI, HashMap<String, Object>>() {
            @Override
            public RetrofitParams call(IGetAPI getAPI, HashMap<String, Object> params) {
                String url = "http://mtalksvc.sq108.net/api/AHome/GetRecommendInfor";
                HashMap<String, String> map = new HashMap<>();
                map.put("parame1", "123");
                return getAPI.requestRecommandInfo(url, siteId, map, 3, CallStatus.PersistentIntervalCache);
            }
        });
    }

    @ApiCheckAnnotation
    public void requestUserList(OnSuccessfulListener<String> successfulListener) {
        BaseSubscriber baseSubscriber = new BaseSubscriber<String, GetService>(this);
        baseSubscriber.setOnSuccessfulListener(successfulListener);
        requestObject(IGetAPI.class, this, baseSubscriber, new Func2<RetrofitParams, IGetAPI, HashMap<String, Object>>() {
            @Override
            public RetrofitParams call(IGetAPI getAPI, HashMap<String, Object> params) {
                return getAPI.requestUserList();
            }
        });
    }

    @ApiCheckAnnotation
    public void getValidateCode(OnSuccessfulListener<File> successfulListener) {
        BaseSubscriber baseSubscriber = new BaseSubscriber<File, GetService>(this);
        baseSubscriber.setOnSuccessfulListener(successfulListener);
        requestObject(IGetAPI.class, this, baseSubscriber, new Func2<RetrofitParams, IGetAPI, HashMap<String, Object>>() {
            @Override
            public RetrofitParams call(IGetAPI getAPI, HashMap<String, Object> params) {
                File file = StorageManager.createImageFile("test_image.jpg");
                return getAPI.getValidateCode("lijh", file);
            }
        });
    }
}
