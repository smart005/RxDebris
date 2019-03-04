package com.cloud.debrisTest.okhttp.annotations;

import com.cloud.debrisTest.okhttp.ServiceAPI;
import com.cloud.debrisTest.okhttp.beans.RecommandInfo;
import com.cloud.nets.annotations.BaseUrlTypeName;
import com.cloud.nets.annotations.DataParam;
import com.cloud.nets.annotations.GET;
import com.cloud.nets.annotations.Param;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.objects.enums.RequestContentType;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:网络测试用例
 * Modifier:
 * ModifyContent:
 */
@BaseUrlTypeName(value = ServiceAPI.mtalksvc, contentType = RequestContentType.Json)
public interface IGetAPI {

    @GET(value = "/AHome/GetRecommendInfor")
    @DataParam(RecommandInfo.class)
    RetrofitParams requestRecommandInfo(
            @Param("SiteID") String siteID
    );
}
