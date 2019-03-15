package com.cloud.debrisTest.okhttp;

import com.cloud.objects.events.OnRequestApiUrl;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/2
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class UrlsProvider implements OnRequestApiUrl {
    @Override
    public String onBaseUrl(Integer apiUrlTypeName) {
        //参数apiUrlTypeName用于区分不同的基础地址
        //如在接口注解类中有这样一个注解@BaseUrlTypeName(value = ServiceAPI.Normal),
        //其中的ServiceAPI的值会作为参数apiUrlTypeName传入用于区分www.AA.com、www.BB.com等;
        //最终将拼接好的协议头、域名、端口、[统一路径]一起返回
        switch (apiUrlTypeName) {
            case ServiceAPI.mtalksvc:
                return "http://mtalksvc.sq108.net/api/";
            case ServiceAPI.sq:
                return "http://app.108sq.cn/Api/";
        }
        return null;
    }
}
