package com.cloud.nets.beans;

import com.cloud.nets.annotations.ApiHeadersCall;
import com.cloud.nets.annotations.BaseUrlTypeName;
import com.cloud.nets.enums.CallStatus;
import com.cloud.nets.enums.ResponseDataType;
import com.cloud.objects.enums.RequestContentType;
import com.cloud.objects.enums.RequestType;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/1
 * Description:http 改造参数
 * Modifier:
 * ModifyContent:
 */
public class RetrofitParams {
    /**
     * 请求类型
     */
    private RequestType requestType = RequestType.GET;
    /**
     * 请求地址
     */
    private String requestUrl = "";
    /**
     * 头信息
     */
    private HashMap<String, String> headParams = null;
    /**
     * 请求参数
     * (对于当次请求,线程间切换数据都是顺序传递的,因此使用TreeMap[非线程安全结构]不影响)
     */
    private TreeMap<String, Object> params = null;
    /**
     * 文件后缀参数
     */
    private HashMap<String, String> fileSuffixParams = null;
    /**
     * del query请求参数
     */
    private HashMap<String, String> delQueryParams = null;

    /**
     * 缓存key
     */
    private String cacheKey = "";

    /**
     * 缓存时间(单位秒)
     */
    private long cacheTime = 0;
    /**
     * api名称
     */
    private String apiName = "";
    /**
     * 数据类
     */
    private Class dataClass = null;
    /**
     * 是否集合数据类型
     */
    private boolean isCollectionDataType = false;
    /**
     * 请求验证是否通过(默认为true)
     */
    private boolean flag = true;
    /**
     * 是否拼接url
     */
    private boolean isJoinUrl = false;

    private BaseUrlTypeName urlTypeName = null;
    /**
     * 数据提交方式
     */
    private RequestContentType requestContentType = null;
    /**
     * 允许接口返回码
     */
    private List<String> allowRetCodes = null;

    /**
     * 是否输出api日志
     */
    private boolean isPrintApiLog = false;

    /**
     * 是否对回调结果进行验证
     */
    private boolean isValidCallResult = true;
    /**
     * 末尾是否包含/
     */
    private boolean isLastContainsPath = false;
    /**
     * api请求头回调注解
     */
    private ApiHeadersCall apiHeadersCall = null;
    /**
     * 请求总时间
     */
    private long requestTotalTime = 0;
    /**
     * 当前请求时间
     */
    private long currentRequestTime = 0;
    /**
     * 回调数据类型(默认OnlyNet)
     */
    private CallStatus callStatus = CallStatus.OnlyNet;
    /**
     * 间隔缓存时间
     */
    private long intervalCacheTime = 0;
    /**
     * 请求方法名
     */
    private String invokeMethodName = "";
    /**
     * 数据响应类型
     */
    private ResponseDataType responseDataType = ResponseDataType.object;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public HashMap<String, String> getHeadParams() {
        if (headParams == null) {
            headParams = new HashMap<String, String>();
        }
        return headParams;
    }

    public TreeMap<String, Object> getParams() {
        if (params == null) {
            params = new TreeMap<String, Object>();
        }
        return params;
    }

    public HashMap<String, String> getFileSuffixParams() {
        if (fileSuffixParams == null) {
            fileSuffixParams = new HashMap<String, String>();
        }
        return fileSuffixParams;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    /**
     * 获取api名称
     */
    public String getApiName() {
        if (apiName == null) {
            apiName = "";
        }
        return apiName;
    }

    /**
     * 设置api名称
     * <p>
     * param apiName
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Class getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class dataClass) {
        this.dataClass = dataClass;
    }

    public boolean isCollectionDataType() {
        return isCollectionDataType;
    }

    public void setCollectionDataType(boolean collectionDataType) {
        isCollectionDataType = collectionDataType;
    }

    /**
     * 获取请求验证是否通过
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * 设置请求验证是否通过
     * <p>
     * param flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * 获取是否拼接url
     */
    public boolean getIsJoinUrl() {
        return isJoinUrl;
    }

    /**
     * 设置是否拼接url
     * <p>
     * param isJoinUrl
     */
    public void setIsJoinUrl(boolean isJoinUrl) {
        this.isJoinUrl = isJoinUrl;
    }

    public HashMap<String, String> getDelQueryParams() {
        if (delQueryParams == null) {
            delQueryParams = new HashMap<String, String>();
        }
        return delQueryParams;
    }

    public void setDelQueryParams(HashMap<String, String> delQueryParams) {
        this.delQueryParams = delQueryParams;
    }

    public BaseUrlTypeName getUrlTypeName() {
        return urlTypeName;
    }

    public void setUrlTypeName(BaseUrlTypeName urlTypeName) {
        this.urlTypeName = urlTypeName;
    }

    public RequestContentType getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(RequestContentType requestContentType) {
        this.requestContentType = requestContentType;
    }

    public List<String> getAllowRetCodes() {
        return allowRetCodes;
    }

    public void setAllowRetCodes(List<String> allowRetCodes) {
        this.allowRetCodes = allowRetCodes;
    }

    public boolean isPrintApiLog() {
        return isPrintApiLog;
    }

    public void setPrintApiLog(boolean printApiLog) {
        isPrintApiLog = printApiLog;
    }

    public boolean isValidCallResult() {
        return isValidCallResult;
    }

    public void setValidCallResult(boolean validCallResult) {
        isValidCallResult = validCallResult;
    }

    public boolean isLastContainsPath() {
        return isLastContainsPath;
    }

    public void setLastContainsPath(boolean lastContainsPath) {
        isLastContainsPath = lastContainsPath;
    }

    public ApiHeadersCall getApiHeadersCall() {
        return apiHeadersCall;
    }

    public void setApiHeadersCall(ApiHeadersCall apiHeadersCall) {
        this.apiHeadersCall = apiHeadersCall;
    }

    public long getRequestTotalTime() {
        return requestTotalTime;
    }

    public void setRequestTotalTime(long requestTotalTime) {
        this.requestTotalTime = requestTotalTime;
    }

    public long getCurrentRequestTime() {
        return currentRequestTime;
    }

    public void setCurrentRequestTime(long currentRequestTime) {
        this.currentRequestTime = currentRequestTime;
    }

    public CallStatus getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public long getIntervalCacheTime() {
        return intervalCacheTime;
    }

    public void setIntervalCacheTime(long intervalCacheTime) {
        this.intervalCacheTime = intervalCacheTime;
    }

    public String getInvokeMethodName() {
        return invokeMethodName;
    }

    public void setInvokeMethodName(String invokeMethodName) {
        this.invokeMethodName = invokeMethodName;
    }

    public ResponseDataType getResponseDataType() {
        if (responseDataType == null) {
            responseDataType = ResponseDataType.object;
        }
        return responseDataType;
    }

    public void setResponseDataType(ResponseDataType responseDataType) {
        this.responseDataType = responseDataType;
    }
}
