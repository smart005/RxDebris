package com.cloud.debrisTest.okhttp.services;

import com.cloud.debrisTest.okhttp.UrlsProvider;
import com.cloud.debrisTest.okhttp.annotations.IGetAPI;
import com.cloud.debrisTest.okhttp.beans.RecommandInfo;
import com.cloud.nets.BaseOkrxService;
import com.cloud.nets.BaseSubscriber;
import com.cloud.nets.annotations.APIUrlInterfaceClass;
import com.cloud.nets.annotations.ApiCheckAnnotation;
import com.cloud.nets.beans.RequestParam;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.events.OnSuccessfulListener;
import com.cloud.objects.events.Func2;

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

    @ApiCheckAnnotation(IsTokenValid = true)
    public void requestRecommandInfo(OnSuccessfulListener<RecommandInfo> successfulListener, RequestParam... params) {
        //请求订阅对象
        BaseSubscriber baseSubscriber = new BaseSubscriber<RecommandInfo, GetService>(this);
        //设置成功监听器
        baseSubscriber.setOnSuccessfulListener(successfulListener);
        //请求api
        requestObject(IGetAPI.class, this, baseSubscriber, new Func2<RetrofitParams, IGetAPI, RequestParam[]>() {
            @Override
            public RetrofitParams call(IGetAPI getAPI, RequestParam... requestParams) {
                return getAPI.requestRecommandInfo(String.valueOf(requestParams[0].getFieldValue()));
            }
        }, params);
    }
}
