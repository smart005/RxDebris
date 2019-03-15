package com.cloud.nets;

import android.text.TextUtils;

import com.cloud.nets.annotations.ApiHeadersCall;
import com.cloud.nets.annotations.BYTES;
import com.cloud.nets.annotations.BaseUrlTypeName;
import com.cloud.nets.annotations.DELETE;
import com.cloud.nets.annotations.DataParam;
import com.cloud.nets.annotations.DelQuery;
import com.cloud.nets.annotations.GET;
import com.cloud.nets.annotations.Header;
import com.cloud.nets.annotations.HeaderPart;
import com.cloud.nets.annotations.Headers;
import com.cloud.nets.annotations.PATCH;
import com.cloud.nets.annotations.POST;
import com.cloud.nets.annotations.PUT;
import com.cloud.nets.annotations.Param;
import com.cloud.nets.annotations.ParamList;
import com.cloud.nets.annotations.Path;
import com.cloud.nets.annotations.RequestTimeLimit;
import com.cloud.nets.annotations.RequestTimePart;
import com.cloud.nets.annotations.RetCodes;
import com.cloud.nets.annotations.UrlItem;
import com.cloud.nets.annotations.UrlItemKey;
import com.cloud.nets.beans.RetrofitParams;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RequestContentType;
import com.cloud.objects.enums.RequestType;
import com.cloud.objects.enums.RuleParams;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.ValidUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/8
 * Description:okrx解析
 * Modifier:
 * ModifyContent:
 */
public class OkRxParsing {
    public <T> T createAPI(Class<T> service) {
        if (!ValidUtils.validateServiceInterface(service)) {
            return null;
        }
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new ApiInvocationHandler(service));
    }

    private class ApiInvocationHandler<T> implements InvocationHandler {

        private Class<T> apiClass = null;

        public ApiInvocationHandler(Class<T> apiClass) {
            this.apiClass = apiClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            try {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                } else if (method.getReturnType() == RetrofitParams.class) {
                    RetrofitParams retrofitParams = new RetrofitParams();
                    Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                    if (ObjectJudge.isNullOrEmpty(declaredAnnotations)) {
                        retrofitParams.setFlag(false);
                        return retrofitParams;
                    }
                    Annotation requestAnnotation = getRequestAnnotation(declaredAnnotations);
                    if (requestAnnotation == null) {
                        //若不符合接口请求则结束请求
                        retrofitParams.setFlag(false);
                        return retrofitParams;
                    }
                    //此isRemoveEmptyValueField为接口请求全局变量,默认为false
                    boolean isRemoveEmptyValueField = false;
                    for (Annotation declaredAnnotation : declaredAnnotations) {
                        if (declaredAnnotation.annotationType() == POST.class) {
                            POST annotation = method.getAnnotation(POST.class);
                            isRemoveEmptyValueField = annotation.isRemoveEmptyValueField();
                            retrofitParams.setRequestType(RequestType.POST);
                            retrofitParams.setValidCallResult(annotation.isValidCallResult());
                            bindRequestAnnontation(apiClass, method, retrofitParams, args, annotation.value(), annotation.isFullUrl(), annotation.values(), annotation.isPrintApiLog(), annotation.contentType());
                        } else if (declaredAnnotation.annotationType() == BYTES.class) {
                            BYTES annotation = method.getAnnotation(BYTES.class);
                            isRemoveEmptyValueField = annotation.isRemoveEmptyValueField();
                            retrofitParams.setRequestType(RequestType.BYTES);
                            retrofitParams.setValidCallResult(annotation.isValidCallResult());
                            bindRequestAnnontation(apiClass, method, retrofitParams, args, annotation.value(), annotation.isFullUrl(), annotation.values(), annotation.isPrintApiLog(), annotation.contentType());
                        } else if (declaredAnnotation.annotationType() == GET.class) {
                            GET annotation = method.getAnnotation(GET.class);
                            isRemoveEmptyValueField = annotation.isRemoveEmptyValueField();
                            retrofitParams.setRequestType(RequestType.GET);
                            retrofitParams.setValidCallResult(annotation.isValidCallResult());
                            bindRequestAnnontation(apiClass, method, retrofitParams, args, annotation.value(), annotation.isFullUrl(), annotation.values(), annotation.isPrintApiLog(), RequestContentType.None);
                        } else if (declaredAnnotation.annotationType() == DELETE.class) {
                            DELETE annotation = method.getAnnotation(DELETE.class);
                            isRemoveEmptyValueField = annotation.isRemoveEmptyValueField();
                            retrofitParams.setRequestType(RequestType.DELETE);
                            retrofitParams.setValidCallResult(annotation.isValidCallResult());
                            bindRequestAnnontation(apiClass, method, retrofitParams, args, annotation.value(), annotation.isFullUrl(), annotation.values(), annotation.isPrintApiLog(), annotation.contentType());
                        } else if (declaredAnnotation.annotationType() == PUT.class) {
                            PUT annotation = method.getAnnotation(PUT.class);
                            isRemoveEmptyValueField = annotation.isRemoveEmptyValueField();
                            retrofitParams.setRequestType(RequestType.PUT);
                            retrofitParams.setValidCallResult(annotation.isValidCallResult());
                            bindRequestAnnontation(apiClass, method, retrofitParams, args, annotation.value(), annotation.isFullUrl(), annotation.values(), annotation.isPrintApiLog(), annotation.contentType());
                        } else if (declaredAnnotation.annotationType() == PATCH.class) {
                            PATCH annotation = method.getAnnotation(PATCH.class);
                            isRemoveEmptyValueField = annotation.isRemoveEmptyValueField();
                            retrofitParams.setRequestType(RequestType.PATCH);
                            retrofitParams.setValidCallResult(annotation.isValidCallResult());
                            bindRequestAnnontation(apiClass, method, retrofitParams, args, annotation.value(), annotation.isFullUrl(), annotation.values(), annotation.isPrintApiLog(), annotation.contentType());
                        } else if (declaredAnnotation.annotationType() == Header.class) {
                            bindHeaderAnnontation(method, retrofitParams, args, isRemoveEmptyValueField);
                        } else if (declaredAnnotation.annotationType() == Headers.class) {
                            bindHeadersAnnontation(method, retrofitParams, args, isRemoveEmptyValueField);
                        } else if (declaredAnnotation.annotationType() == DataParam.class) {
                            DataParam annotation = method.getAnnotation(DataParam.class);
                            retrofitParams.setDataClass(annotation.value());
                            retrofitParams.setCollectionDataType(annotation.isCollection());
                        } else if (declaredAnnotation.annotationType() == RetCodes.class) {
                            RetCodes annotation = method.getAnnotation(RetCodes.class);
                            if (!ObjectJudge.isNullOrEmpty(annotation.value())) {
                                retrofitParams.setAllowRetCodes(Arrays.asList(annotation.value()));
                            }
                        } else if (declaredAnnotation.annotationType() == ApiHeadersCall.class) {
                            ApiHeadersCall annotation = method.getAnnotation(ApiHeadersCall.class);
                            retrofitParams.setApiHeadersCall(annotation);
                        } else if (declaredAnnotation.annotationType() == RequestTimeLimit.class) {
                            bindRequestTimeAnnontation(method, retrofitParams, args);
                        }
                    }
                    //获取参数集合
                    bindParamAnnontation(method, retrofitParams, args, isRemoveEmptyValueField, requestAnnotation.annotationType());
                    return retrofitParams;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    private void bindRequestTime(String totalTime, TimeUnit unit, RetrofitParams retrofitParams) {
        if (!TextUtils.isDigitsOnly(totalTime)) {
            return;
        }
        long time = Long.parseLong(totalTime);
        long milliseconds = ConvertUtils.toMilliseconds(time, unit);
        retrofitParams.setRequestTotalTime(milliseconds);
    }

    private void bindRequestTimeAnnontation(Method method, RetrofitParams retrofitParams, Object[] args) {
        RequestTimeLimit annotation = method.getAnnotation(RequestTimeLimit.class);
        if (annotation == null) {
            return;
        }
        String pattent = String.format(RuleParams.MatchTagBetweenContent.getValue(), "\\{", "\\}");
        String matche = ValidUtils.matche(pattent, annotation.totalTime());
        if (TextUtils.isEmpty(matche)) {
            //没有占位符直接计算
            bindRequestTime(annotation.totalTime(), annotation.unit(), retrofitParams);
        } else {
            HashMap<RequestTimePart, Integer> paramAnnotationObject = getParamAnnotationObject(method, RequestTimePart.class);
            if (ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                return;
            }
            for (Map.Entry<RequestTimePart, Integer> entry : paramAnnotationObject.entrySet()) {
                RequestTimePart part = entry.getKey();
                if (!TextUtils.equals(part.value(), matche)) {
                    continue;
                }
                String dataValue = String.valueOf(args[entry.getValue()]);
                bindRequestTime(dataValue, annotation.unit(), retrofitParams);
                break;
            }
        }
    }

    private void addParams(String paramKey, Param key, Object arg, HashMap<String, Object> params, boolean isRemoveEmptyValueField) {
        paramKey = TextUtils.isEmpty(paramKey) ? key.value() : paramKey;
        if (arg instanceof String) {
            //string类型
            if (key.isRemoveEmptyValueField()) {
                if (!TextUtils.isEmpty(String.valueOf(arg))) {
                    params.put(paramKey, arg);
                }
            } else {
                String json = JsonUtils.toStr(arg);
                if (isRemoveEmptyValueField) {
                    if (!TextUtils.isEmpty(json)) {
                        params.put(paramKey, json);
                    }
                } else {
                    params.put(paramKey, json);
                }
            }
        } else {
            //集合类型
            String json = JsonUtils.toStr(arg);
            if (key.isRemoveEmptyValueField()) {
                if (!TextUtils.isEmpty(json)) {
                    params.put(paramKey, json);
                }
            } else {
                if (isRemoveEmptyValueField) {
                    if (!TextUtils.isEmpty(json)) {
                        params.put(paramKey, json);
                    }
                } else {
                    params.put(paramKey, json);
                }
            }
        }
    }

    private boolean bindJsonParams(int position, Param key, int paramPosition, HashMap<String, Object> params, Object[] args, boolean isRemoveEmptyValueField) {
        if (TextUtils.isEmpty(key.value())) {
            params.clear();
            Object arg = args[paramPosition];
            //如果参数key为空且value类型不为int double float long file byte[]那么最终在提交时忽略此参数对应的key
            if (arg != null &&
                    !(arg instanceof Integer) &&
                    !(arg instanceof Double) &&
                    !(arg instanceof Float) &&
                    !(arg instanceof Long) &&
                    !(arg instanceof File) &&
                    !(arg instanceof byte[])) {
                String paramKey = OkRxKeys.ignoreParamContainsKey + position;
                addParams(paramKey, key, arg, params, isRemoveEmptyValueField);
            }
            return true;
        } else {
            if (!params.containsKey(key.value())) {
                Object arg = args[paramPosition];
                if (arg != null &&
                        !(arg instanceof Integer) &&
                        !(arg instanceof Double) &&
                        !(arg instanceof Float) &&
                        !(arg instanceof Long) &&
                        !(arg instanceof File) &&
                        !(arg instanceof byte[])) {
                    addParams("", key, arg, params, isRemoveEmptyValueField);
                } else {
                    putParamValue(key, arg, params, null, isRemoveEmptyValueField);
                }
            }
        }
        return false;
    }

    private boolean bindSingleParam(Param key, int position, int paramPosition, HashMap<String, Object> params, HashMap<String, String> suffixParams, Object[] args, boolean isRemoveEmptyValueField) {
        if (key.isJson()) {
            return bindJsonParams(position, key, paramPosition, params, args, isRemoveEmptyValueField);
        } else {
            if (!params.containsKey(key.value())) {
                Object arg = args[paramPosition];
                putParamValue(key, arg, params, suffixParams, isRemoveEmptyValueField);
            }
        }
        return false;
    }

    private void bindParams(HashMap<Param, Integer> paramAnnotationObject, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField) {
        if (ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
            return;
        }
        int position = 0;
        HashMap<String, Object> params = retrofitParams.getParams();
        HashMap<String, String> suffixParams = retrofitParams.getFileSuffixParams();
        for (Map.Entry<Param, Integer> paramIntegerEntry : paramAnnotationObject.entrySet()) {
            Param key = paramIntegerEntry.getKey();
            if (bindSingleParam(key, position, paramIntegerEntry.getValue(), params, suffixParams, args, isRemoveEmptyValueField)) {
                break;
            }
            position++;
        }
    }

    private void bindDeletes(Method method, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField, Class<? extends Annotation> annotationType) {
        if (annotationType == DELETE.class) {
            //delete 请求时加query注解时将参数拼接到url后面
            HashMap<DelQuery, Integer> delQueryIntegerHashMap = getParamAnnotationObject(method, DelQuery.class);
            if (!ObjectJudge.isNullOrEmpty(delQueryIntegerHashMap)) {
                HashMap<String, String> params = retrofitParams.getDelQueryParams();
                for (Map.Entry<DelQuery, Integer> delQueryIntegerEntry : delQueryIntegerHashMap.entrySet()) {
                    DelQuery key = delQueryIntegerEntry.getKey();
                    if (!params.containsKey(key.value())) {
                        Object arg = args[delQueryIntegerEntry.getValue()];
                        if (key.isRemoveEmptyValueField()) {
                            if (arg != null && !TextUtils.isEmpty(String.valueOf(arg))) {
                                params.put(key.value(), String.valueOf(arg));
                            }
                        } else {
                            if (isRemoveEmptyValueField) {
                                if (arg != null && !TextUtils.isEmpty(String.valueOf(arg))) {
                                    params.put(key.value(), String.valueOf(arg));
                                }
                            } else {
                                params.put(key.value(), String.valueOf(arg));
                            }
                        }
                    }
                }
            }
        }
    }

    private void bindSingleParamList(ParamList key, HashMap<String, Object> argmap, RetrofitParams retrofitParams, boolean isRemoveEmptyValueField) {
        isRemoveEmptyValueField = key.isRemoveEmptyValueField() ? key.isRemoveEmptyValueField() : isRemoveEmptyValueField;
        HashMap<String, Object> params = retrofitParams.getParams();
        for (Map.Entry<String, Object> entry : argmap.entrySet()) {
            if (isRemoveEmptyValueField && (entry.getValue() == null || TextUtils.isEmpty(String.valueOf(entry.getValue())))) {
                //如果isRemoveEmptyValueField==true且参数值为空则跳过
                continue;
            }
            if (params.containsKey(entry.getKey())) {
                //如果参数key已经包含在集合列表中则跳过
                continue;
            }
            params.put(entry.getKey(), entry.getValue());
        }
    }

    private void bindParamList(HashMap<ParamList, Integer> paramAnnotations, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField) {
        if (ObjectJudge.isNullOrEmpty(paramAnnotations)) {
            return;
        }
        for (Map.Entry<ParamList, Integer> entry : paramAnnotations.entrySet()) {
            Object arg = args[entry.getValue()];
            if (!(arg instanceof HashMap)) {
                //非hash map类型则跳过
                continue;
            }
            HashMap<String, Object> argmap = (HashMap<String, Object>) arg;
            if (ObjectJudge.isNullOrEmpty(argmap)) {
                //参数值为空则跳过
                continue;
            }
            ParamList key = entry.getKey();
            bindSingleParamList(key, argmap, retrofitParams, isRemoveEmptyValueField);
        }
    }

    private void bindParamAnnontation(Method method,
                                      RetrofitParams retrofitParams,
                                      Object[] args,
                                      boolean isRemoveEmptyValueField,
                                      Class<? extends Annotation> annotationType) {
        //获取请求方法中所有包含有Param的请求参数字段
        HashMap<Param, Integer> paramAnnotationObject = getParamAnnotationObject(method, Param.class);
        //绑定Param参数
        bindParams(paramAnnotationObject, retrofitParams, args, isRemoveEmptyValueField);
        //绑定ParamList参数
        HashMap<ParamList, Integer> paramAnnotations = getParamAnnotationObject(method, ParamList.class);
        bindParamList(paramAnnotations, retrofitParams, args, isRemoveEmptyValueField);
        //绑定DelQuery参数
        bindDeletes(method, retrofitParams, args, isRemoveEmptyValueField, annotationType);
    }

    private void putValueByIsRemoveEmpty(Param key, Object arg, HashMap<String, Object> params, HashMap<String, String> suffixParams) {
        if (arg == null) {
            return;
        }
        //文件
        if (arg instanceof File) {
            File file = (File) arg;
            if (!file.exists()) {
                //文件不存在
                return;
            }
            params.put(key.value(), arg);
            if (suffixParams != null) {
                suffixParams.put(key.value(), key.fileSuffixAfterUpload());
            }
            return;
        }
        //字节流
        if ((arg instanceof byte[]) || (arg instanceof Byte[])) {
            params.put(key.value(), arg);
            if (suffixParams != null) {
                suffixParams.put(key.value(), key.fileSuffixAfterUpload());
            }
            return;
        }
        if (arg instanceof String) {
            params.put(key.value(), arg);
            if (suffixParams != null) {
                suffixParams.put(key.value(), key.fileSuffixAfterUpload());
            }
            return;
        }
        params.put(key.value(), arg);
        if (suffixParams != null) {
            suffixParams.put(key.value(), key.fileSuffixAfterUpload());
        }
    }

    private void putParamValue(Param key, Object arg, HashMap<String, Object> params, HashMap<String, String> suffixParams, boolean isRemoveEmptyValueField) {
        if (key.isRemoveEmptyValueField() || isRemoveEmptyValueField) {
            putValueByIsRemoveEmpty(key, arg, params, suffixParams);
            return;
        }
        params.put(key.value(), arg);
        if (suffixParams != null) {
            suffixParams.put(key.value(), key.fileSuffixAfterUpload());
        }
    }

    private UrlItem getMatchUrlItem(UrlItem[] urlItems, String matchKey) {
        UrlItem urlItem = null;
        if (!ObjectJudge.isNullOrEmpty(urlItems)) {
            for (UrlItem item : urlItems) {
                if (!TextUtils.isEmpty(matchKey) && TextUtils.equals(item.key(), matchKey)) {
                    urlItem = item;
                    break;
                }
            }
        }
        return urlItem;
    }

    private <T> void bindRequestAnnontation(Class<T> apiClass,
                                            Method method,
                                            RetrofitParams retrofitParams,
                                            Object[] args,
                                            String formatUrl,
                                            boolean isFullUrl,
                                            UrlItem[] urlItems,
                                            boolean isPrintLog,
                                            RequestContentType methodRequestContentType) {
        //若formatUrl为空则从urlItems集合中取出对应的地址
        if (TextUtils.isEmpty(formatUrl)) {
            if (ObjectJudge.isNullOrEmpty(urlItems)) {
                retrofitParams.setFlag(false);
                return;
            }
            HashMap<UrlItemKey, Integer> urlItemKeys = getParamAnnotationObject(method, UrlItemKey.class);
            if (ObjectJudge.isNullOrEmpty(urlItemKeys)) {
                UrlItem urlItem = urlItems[0];
                if (TextUtils.isEmpty(urlItem.value())) {
                    retrofitParams.setFlag(false);
                    return;
                }
                formatUrl = urlItem.value().trim();
                if (formatUrl.endsWith("/")) {
                    retrofitParams.setLastContainsPath(true);
                }
            } else {
                for (Map.Entry<UrlItemKey, Integer> entry : urlItemKeys.entrySet()) {
                    UrlItem urlItem = getMatchUrlItem(urlItems, String.valueOf(args[entry.getValue()]));
                    if (urlItem == null) {
                        retrofitParams.setFlag(false);
                        return;
                    }
                    formatUrl = urlItem.value().trim();
                    if (formatUrl.endsWith("/")) {
                        retrofitParams.setLastContainsPath(true);
                    }
                    break;
                }
            }
        } else {
            if (formatUrl.trim().endsWith("/")) {
                retrofitParams.setLastContainsPath(true);
            }
        }
        if (TextUtils.isEmpty(formatUrl)) {
            retrofitParams.setFlag(false);
            return;
        }
        //url参数配置
        String[] furlsplit = formatUrl.split("/|\\?|=");
        if (ObjectJudge.isNullOrEmpty(furlsplit)) {
            retrofitParams.setFlag(false);
            return;
        }
        List<String> matches = new ArrayList<String>();
        String pattent = String.format(RuleParams.MatchTagBetweenContent.getValue(), "\\{", "\\}");
        for (String fsitem : furlsplit) {
            if (TextUtils.isEmpty(fsitem)) {
                continue;
            }
            //判断匹配对象是否包含{}
            if (ObjectJudge.isNullOrEmpty(ValidUtils.matches("[\\{\\}]{1,2}", fsitem))) {
                continue;
            }
            String matche = ValidUtils.matche(pattent, fsitem);
            if (!TextUtils.isEmpty(matche) && !matches.contains(matche)) {
                matches.add(matche);
            }
        }
        if (ObjectJudge.isNullOrEmpty(matches)) {
            matchRequestUrl(apiClass,
                    method,
                    retrofitParams,
                    formatUrl,
                    isFullUrl,
                    isPrintLog,
                    methodRequestContentType);
        } else {
            String rativeUrl = formatUrl;
            HashMap<Path, Integer> paramAnnotationObject = getParamAnnotationObject(method, Path.class);
            if (!ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                //如果未匹配到则按原地址请求
                for (Map.Entry<Path, Integer> pathIntegerEntry : paramAnnotationObject.entrySet()) {
                    Path path = pathIntegerEntry.getKey();
                    if (matches.contains(path.value())) {
                        rativeUrl = rativeUrl.replace(String.format("{%s}", path.value()), "%s");
                        rativeUrl = String.format(rativeUrl, String.valueOf(args[pathIntegerEntry.getValue()]));
                    }
                }

                matchRequestUrl(apiClass,
                        method,
                        retrofitParams,
                        rativeUrl,
                        isFullUrl,
                        isPrintLog,
                        methodRequestContentType);
            } else {
                retrofitParams.setFlag(false);
            }
        }
    }

    private <T> void matchRequestUrl(Class<T> apiClass,
                                     Method method,
                                     RetrofitParams retrofitParams,
                                     String url,
                                     boolean isFullUrl,
                                     boolean isPrintLog,
                                     RequestContentType methodRequestContentType) {
        retrofitParams.setApiName(url);
        RequestContentType contentType = RequestContentType.None;
        BaseUrlTypeName urlTypeName = method.getAnnotation(BaseUrlTypeName.class);
        if (urlTypeName == null) {
            //全局基础地址配置
            urlTypeName = apiClass.getAnnotation(BaseUrlTypeName.class);
            if (urlTypeName != null) {
                contentType = urlTypeName.contentType();
            }
            //获取日志flag标识
            if (!isPrintLog) {
                isPrintLog = urlTypeName.isPrintApiLog();
            }
        } else {
            //获取content-type
            BaseUrlTypeName annotation = apiClass.getAnnotation(BaseUrlTypeName.class);
            if (urlTypeName.contentType() == RequestContentType.None) {
                if (annotation != null) {
                    contentType = annotation.contentType();
                }
            } else {
                contentType = urlTypeName.contentType();
            }
            //获取日志flag标识
            if (!isPrintLog) {
                boolean printApiLog = urlTypeName.isPrintApiLog();
                isPrintLog = printApiLog ? printApiLog : annotation.isPrintApiLog();
            }
        }
        retrofitParams.setUrlTypeName(urlTypeName);
        if (methodRequestContentType != null && methodRequestContentType != RequestContentType.None) {
            retrofitParams.setRequestContentType(contentType == methodRequestContentType ? contentType : methodRequestContentType);
        } else {
            retrofitParams.setRequestContentType(contentType);
        }
        retrofitParams.setPrintApiLog(isPrintLog);
        if (urlTypeName == null) {
            //未配置局部和全局基础地址
            retrofitParams.setFlag(false);
        } else {
            if (isFullUrl) {
                retrofitParams.setRequestUrl(url);
                retrofitParams.setIsJoinUrl(false);
            } else {
                retrofitParams.setRequestUrl(url);
                if (ValidUtils.valid(RuleParams.Url.getValue(), url)) {
                    retrofitParams.setIsJoinUrl(false);
                } else {
                    retrofitParams.setIsJoinUrl(true);
                }
            }
        }
    }

    private void bindHeaderAnnontation(Method method, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField) {
        Header annotation = method.getAnnotation(Header.class);
        HashMap<String, String> headParams = retrofitParams.getHeadParams();
        if (!headParams.containsKey(annotation.name()) &&
                !TextUtils.isEmpty(annotation.value())) {
            String pattent = String.format(RuleParams.MatchTagBetweenContent.getValue(), "\\{", "\\}");
            String matche = ValidUtils.matche(pattent, annotation.value());
            if (TextUtils.isEmpty(matche)) {
                if (annotation.isRemoveEmptyValueField()) {
                    if (!TextUtils.isEmpty(annotation.value())) {
                        headParams.put(annotation.name(), annotation.value());
                    }
                } else {
                    if (isRemoveEmptyValueField) {
                        if (!TextUtils.isEmpty(annotation.value())) {
                            headParams.put(annotation.name(), annotation.value());
                        }
                    } else {
                        headParams.put(annotation.name(), annotation.value());
                    }
                }
            } else {
                HashMap<HeaderPart, Integer> paramAnnotationObject = getParamAnnotationObject(method, HeaderPart.class);
                if (!ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                    for (Map.Entry<HeaderPart, Integer> headerPartIntegerEntry : paramAnnotationObject.entrySet()) {
                        HeaderPart headerPart = headerPartIntegerEntry.getKey();
                        if (TextUtils.equals(headerPart.value(), matche)) {
                            String dataValue = String.valueOf(args[headerPartIntegerEntry.getValue()]);
                            if (headerPart.isRemoveEmptyValueField()) {
                                if (!TextUtils.isEmpty(dataValue)) {
                                    headParams.put(annotation.name(), dataValue);
                                }
                            } else {
                                if (isRemoveEmptyValueField) {
                                    if (!TextUtils.isEmpty(dataValue)) {
                                        headParams.put(annotation.name(), dataValue);
                                    }
                                } else {
                                    headParams.put(annotation.name(), dataValue);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void bindHeadersAnnontation(Method method, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField) {
        Headers annotation = method.getAnnotation(Headers.class);
        if (annotation != null) {
            String[] values = annotation.value();
            if (!ObjectJudge.isNullOrEmpty(values)) {
                HashMap<String, String> headParams = retrofitParams.getHeadParams();
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    String[] lst = value.split(":");
                    if (lst.length == 2 && !TextUtils.isEmpty(lst[0])
                            && !TextUtils.isEmpty(lst[1]) && !headParams.containsKey(lst[0])) {
                        String pattent = String.format(RuleParams.MatchTagBetweenContent.getValue(), "\\{", "\\}");
                        String matche = ValidUtils.matche(pattent, lst[1]);
                        if (TextUtils.isEmpty(matche)) {
                            if (annotation.isRemoveEmptyValueField()) {
                                if (!TextUtils.isEmpty(lst[1])) {
                                    headParams.put(lst[0], lst[1]);
                                }
                            } else {
                                if (isRemoveEmptyValueField) {
                                    if (!TextUtils.isEmpty(lst[1])) {
                                        headParams.put(lst[0], lst[1]);
                                    }
                                } else {
                                    headParams.put(lst[0], lst[1]);
                                }
                            }
                        } else {
                            //取HeaderPart中的内容
                            HashMap<HeaderPart, Integer> paramAnnotationObject = getParamAnnotationObject(method, HeaderPart.class);
                            if (!ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                                for (Map.Entry<HeaderPart, Integer> headerPartIntegerEntry : paramAnnotationObject.entrySet()) {
                                    HeaderPart headerPart = headerPartIntegerEntry.getKey();
                                    if (TextUtils.equals(headerPart.value(), matche)) {
                                        String dataValue = String.valueOf(args[headerPartIntegerEntry.getValue()]);
                                        if (annotation.isRemoveEmptyValueField()) {
                                            if (!TextUtils.isEmpty(dataValue)) {
                                                headParams.put(lst[0], dataValue);
                                            }
                                        } else {
                                            if (isRemoveEmptyValueField) {
                                                if (!TextUtils.isEmpty(dataValue)) {
                                                    headParams.put(lst[0], dataValue);
                                                }
                                            } else {
                                                headParams.put(lst[0], dataValue);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Annotation getRequestAnnotation(Annotation[] declaredAnnotations) {
        Annotation annotation = null;
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation.annotationType() == POST.class ||
                    declaredAnnotation.annotationType() == GET.class ||
                    declaredAnnotation.annotationType() == DELETE.class ||
                    declaredAnnotation.annotationType() == PUT.class ||
                    declaredAnnotation.annotationType() == PATCH.class ||
                    declaredAnnotation.annotationType() == BYTES.class) {
                annotation = declaredAnnotation;
                break;
            }
        }
        return annotation;
    }

    //获取指定参数的注解对象
    private <T> HashMap<T, Integer> getParamAnnotationObject(Method method, Class<T> annotationClass) {
        HashMap<T, Integer> lst = new HashMap<T, Integer>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (!ObjectJudge.isNullOrEmpty(parameterAnnotations)) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                if (ObjectJudge.isNullOrEmpty(parameterAnnotation)) {
                    continue;
                }
                if (parameterAnnotation[0].annotationType() == annotationClass) {
                    if (!lst.containsKey(parameterAnnotation[0])) {
                        lst.put((T) parameterAnnotation[0], i);
                    }
                }
            }
        }
        return lst;
    }
}
