package com.cloud.debrisTest.okhttp.annotations;

import com.cloud.debrisTest.okhttp.ServiceAPI;
import com.cloud.debrisTest.okhttp.beans.RecommandInfo;
import com.cloud.nets.annotations.BaseUrlTypeName;
import com.cloud.nets.annotations.DataCallStatus;
import com.cloud.nets.annotations.DataParam;
import com.cloud.nets.annotations.GET;
import com.cloud.nets.annotations.Param;
import com.cloud.nets.annotations.Path;
import com.cloud.nets.annotations.RequestTimeLimit;
import com.cloud.nets.annotations.RequestTimePart;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.ResponseDataType;
import com.cloud.objects.enums.RequestContentType;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    @GET(value = "/AHome/GetRecommendInfor", isFailureRetry = true)
//    @BaseUrlTypeName(contentType = RequestContentType.Form)
//    @GET(value = "{url}", isFullUrl = true)
    @RequestTimeLimit(totalTime = "{requestTime}", unit = TimeUnit.SECONDS)
    @DataParam(RecommandInfo.class)
    RetrofitParams requestRecommandInfo(
            @Path("url") String url,
            @Param("SiteID") int siteID,
            @Param() HashMap<String, String> map,
            @RequestTimePart("requestTime") long requestTime,
            @DataCallStatus() CallStatus callStatus
    );

    @GET(value = "http://app.108sq.org:920/Api/UserPrestigeInfos?UserIDs=6070%7C438484%7C438484", isFullUrl = true)
//    @DataParam(value = UserItem.class, isCollection = true)
    @DataParam(String.class)
    RetrofitParams requestUserList();

    @GET(value = "http://login.108sq.cn/login/Validate/MobileImage.aspx", isFullUrl = true)
    @DataParam(value = File.class, responseDataType = ResponseDataType.byteData)
    RetrofitParams getValidateCode(
            @Param("username") String username,
            @Param(isTargetFile = true) File file
    );
}
