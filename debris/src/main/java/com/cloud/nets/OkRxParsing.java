package com.cloud.nets;

import android.text.TextUtils;

import com.cloud.nets.annotations.ApiHeadersCall;
import com.cloud.nets.annotations.BYTES;
import com.cloud.nets.annotations.BaseUrlTypeName;
import com.cloud.nets.annotations.DELETE;
import com.cloud.nets.annotations.DataCallStatus;
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
import com.cloud.nets.enums.CallStatus;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
    //第二参数从接口请求方法取出并传入，若后面参数中有配置则被替换，若没有配置则以此值为准;
    public <T> T createAPI(Class<T> service, CallStatus callStatus) {
        if (!ValidUtils.validateServiceInterface(service)) {
            return null;
        }
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new ApiInvocationHandler(service, callStatus));
    }

    private class ApiInvocationHandler<T> implements InvocationHandler {

        private Class<T> apiClass = null;
        private CallStatus callStatus = CallStatus.OnlyNet;

        public ApiInvocationHandler(Class<T> apiClass, CallStatus callStatus) {
            this.apiClass = apiClass;
            this.callStatus = callStatus;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            try {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                } else if (method.getReturnType() == RetrofitParams.class) {
                    RetrofitParams retrofitParams = new RetrofitParams();
                    //数据回调状态值设置
                    retrofitParams.setCallStatus(callStatus);
                    //相关注解值获取
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
                    boolean isRemoveEmptyValueField = bindRequestTypes(declaredAnnotations, method, apiClass, retrofitParams, args);
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

    //绑定请求方式
    private <T> boolean bindRequestTypes(Annotation[] declaredAnnotations, Method method, Class<T> apiClass, RetrofitParams retrofitParams, Object[] args) {
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
                retrofitParams.setResponseDataType(annotation.responseDataType());
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
        return isRemoveEmptyValueField;
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
            TreeMap<Integer, RequestTimePart> paramAnnotationObject = getParamAnnotationObject(method, RequestTimePart.class);
            if (ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                return;
            }
            for (Map.Entry<Integer, RequestTimePart> entry : paramAnnotationObject.entrySet()) {
                RequestTimePart part = entry.getValue();
                if (!TextUtils.equals(part.value(), matche)) {
                    continue;
                }
                String dataValue = String.valueOf(args[entry.getKey()]);
                bindRequestTime(dataValue, annotation.unit(), retrofitParams);
                break;
            }
        }
    }

    private void addJsonParams(String paramKey, Param key, Object arg, TreeMap<String, Object> params, boolean isRemoveEmptyValueField) {
        //若参数为null则忽略
        if (key.isRemoveEmptyValueField() || isRemoveEmptyValueField) {
            if (arg == null) {
                return;
            }
        }
        if (arg instanceof String) {
            //string类型
            if (key.isRemoveEmptyValueField() || isRemoveEmptyValueField) {
                //记为""移除
                if (!TextUtils.isEmpty(String.valueOf(arg))) {
                    params.put(paramKey, arg);
                }
            } else {
                params.put(paramKey, arg);
            }
        } else {
            //对象或集合类型,此时除了开始判断null之外不可能为空字符串;
            String json = JsonUtils.toStr(arg);
            params.put(paramKey, json);
        }
    }

    private void bindJsonParams(int position, Param key, int paramPosition, TreeMap<String, Object> params, Object[] args, boolean isRemoveEmptyValueField) {
        if (TextUtils.isEmpty(key.value())) {
            //若参数key为空且value类型为int double float long file byte[] Byte[]那么最终在提交时将被忽略
            Object arg = args[paramPosition];
            if (arg != null &&
                    !(arg instanceof Integer) &&
                    !(arg instanceof Double) &&
                    !(arg instanceof Float) &&
                    !(arg instanceof Long) &&
                    !(arg instanceof File) &&
                    !(arg instanceof byte[]) &&
                    !(arg instanceof Byte[])) {
                //忽略参数key,在提交之前将作为验证判断依据;
                String paramKey = OkRxKeys.ignoreParamContainsKey + position;
                //添加json参数
                addJsonParams(paramKey, key, arg, params, isRemoveEmptyValueField);
            }
        } else {
            //排除设置的相同参数key
            if (!params.containsKey(key.value())) {
                Object arg = args[paramPosition];
                if (arg != null &&
                        !(arg instanceof Integer) &&
                        !(arg instanceof Double) &&
                        !(arg instanceof Float) &&
                        !(arg instanceof Long) &&
                        !(arg instanceof File) &&
                        !(arg instanceof byte[]) &&
                        !(arg instanceof Byte[])) {
                    //添加json参数
                    addJsonParams(key.value(), key, arg, params, isRemoveEmptyValueField);
                } else {
                    //作为普通参数提交
                    putParamValue(key, arg, params, null, isRemoveEmptyValueField);
                }
            }
        }
    }

    private void bindSingleParam(Param key, int position, int paramPosition, TreeMap<String, Object> params, HashMap<String, String> suffixParams, Object[] args, boolean isRemoveEmptyValueField) {
        if (key.isJson()) {
            bindJsonParams(position, key, paramPosition, params, args, isRemoveEmptyValueField);
        } else {
            if (!params.containsKey(key.value())) {
                Object arg = args[paramPosition];
                putParamValue(key, arg, params, suffixParams, isRemoveEmptyValueField);
            }
        }
    }

    private void bindParams(TreeMap<Integer, Param> paramAnnotationObject, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField) {
        if (ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
            return;
        }
        int position = 0;
        //提交参数
        TreeMap<String, Object> params = retrofitParams.getParams();
        //文件上传时可自定义
        HashMap<String, String> suffixParams = retrofitParams.getFileSuffixParams();
        for (Map.Entry<Integer, Param> paramIntegerEntry : paramAnnotationObject.entrySet()) {
            Param key = paramIntegerEntry.getValue();
            if (key.isTargetFile()) {
                Object arg = args[paramIntegerEntry.getKey()];
                if (arg instanceof File) {
                    File file = (File) arg;
                    retrofitParams.setTargetFilePath(file.getAbsolutePath());
                } else if (arg instanceof String) {
                    retrofitParams.setTargetFilePath((String) arg);
                }
                continue;
            }
            //绑定参数
            bindSingleParam(key, position, paramIntegerEntry.getKey(), params, suffixParams, args, isRemoveEmptyValueField);
            position++;
        }
        //参数校验
        checkParams(params);
    }

    //参数校验
    private void checkParams(TreeMap<String, Object> params) {
        if (ObjectJudge.isNullOrEmpty(params) || params.size() == 1) {
            //如果参数为空或参数个数为1,则不做校验处理
            /**
             * params.size() == 1几种情况如下:
             * 1.普通key-value正常提交即可;
             * 2.json且用户有指定key,则key-value正常提交即可;
             * 3.json且用户未指定key,则在真正网络请求前会作处理;
             */
            return;
        }
//        int notSpecifiedParamKeyCount = 0;//记录未指定参数key对应的参数个数
        Set<String> notSpecifiedParamKeys = new HashSet<String>();//记录未指定参数key集合与params中的key对应
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().contains(OkRxKeys.ignoreParamContainsKey)) {
                notSpecifiedParamKeys.add(entry.getKey());
            }
        }
        if (ObjectJudge.isNullOrEmpty(notSpecifiedParamKeys)) {
            //若均已指定参数则正常提交即可
            return;
        }
        //若未指定参数为1且params.size()>1,则移除对应的未指定参数
        //此时params.size()必大于1,因此无须加条件限制;
        if (notSpecifiedParamKeys.size() == 1) {
            Iterator<String> iterator = notSpecifiedParamKeys.iterator();
            String next = iterator.next();
            params.remove(next);
            return;
        }
        //若未指定参数个数>1且不等于params.size(),则移除所有未指定参数;
        if (notSpecifiedParamKeys.size() > 1 && notSpecifiedParamKeys.size() != params.size()) {
            for (String next : notSpecifiedParamKeys) {
                params.remove(next);
            }
            return;
        }
        //若未指定参数个数与params相同,则将所有参数以[{}|[],{}]的json形式作为本次提交的数据
        if (notSpecifiedParamKeys.size() == params.size()) {
            String json = JsonUtils.toStr(params.values());
            params.clear();
            params.put(OkRxKeys.ignoreParamContainsKey + 1, json);
        }
    }

    private void bindDeletes(Method method, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField, Class<? extends Annotation> annotationType) {
        if (annotationType == DELETE.class) {
            //delete 请求时加query注解时将参数拼接到url后面
            TreeMap<Integer, DelQuery> delQueryIntegerHashMap = getParamAnnotationObject(method, DelQuery.class);
            if (!ObjectJudge.isNullOrEmpty(delQueryIntegerHashMap)) {
                HashMap<String, String> params = retrofitParams.getDelQueryParams();
                for (Map.Entry<Integer, DelQuery> delQueryIntegerEntry : delQueryIntegerHashMap.entrySet()) {
                    DelQuery key = delQueryIntegerEntry.getValue();
                    if (!params.containsKey(key.value())) {
                        Object arg = args[delQueryIntegerEntry.getKey()];
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
        TreeMap<String, Object> params = retrofitParams.getParams();
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

    private void bindParamList(TreeMap<Integer, ParamList> paramAnnotations, RetrofitParams retrofitParams, Object[] args, boolean isRemoveEmptyValueField) {
        if (ObjectJudge.isNullOrEmpty(paramAnnotations)) {
            return;
        }
        for (Map.Entry<Integer, ParamList> entry : paramAnnotations.entrySet()) {
            Object arg = args[entry.getKey()];
            if (!(arg instanceof HashMap)) {
                //非hash map类型则跳过
                continue;
            }
            HashMap<String, Object> argmap = (HashMap<String, Object>) arg;
            if (ObjectJudge.isNullOrEmpty(argmap)) {
                //参数值为空则跳过
                continue;
            }
            ParamList key = entry.getValue();
            bindSingleParamList(key, argmap, retrofitParams, isRemoveEmptyValueField);
        }
    }

    private void bindParamAnnontation(Method method,
                                      RetrofitParams retrofitParams,
                                      Object[] args,
                                      boolean isRemoveEmptyValueField,
                                      Class<? extends Annotation> annotationType) {
        //获取请求方法中所有包含有Param的请求参数字段
        TreeMap<Integer, Param> paramAnnotationObject = getParamAnnotationObject(method, Param.class);
        //绑定Param参数
        bindParams(paramAnnotationObject, retrofitParams, args, isRemoveEmptyValueField);
        //绑定ParamList参数
        TreeMap<Integer, ParamList> paramAnnotations = getParamAnnotationObject(method, ParamList.class);
        bindParamList(paramAnnotations, retrofitParams, args, isRemoveEmptyValueField);
        //绑定DelQuery参数
        bindDeletes(method, retrofitParams, args, isRemoveEmptyValueField, annotationType);
        //绑定DataCallStatus参数
        bindDataCallStatus(method, retrofitParams, args);
    }

    private void bindDataCallStatus(Method method, RetrofitParams retrofitParams, Object[] args) {
        TreeMap<Integer, DataCallStatus> callStatusTreeMap = getParamAnnotationObject(method, DataCallStatus.class);
        if (ObjectJudge.isNullOrEmpty(callStatusTreeMap)) {
            //如果为这不处理
            return;
        }
        //如果接口参数里加了多个配置，那么以第一个配置为准
        Map.Entry<Integer, DataCallStatus> entry = callStatusTreeMap.firstEntry();
        Object arg = args[entry.getKey()];
        //如果传入的类型非CallStatus类型则不处理
        if (!(arg instanceof CallStatus)) {
            return;
        }
        CallStatus callStatus = (CallStatus) arg;
        retrofitParams.setCallStatus(callStatus);
    }

    private void putValueByIsRemoveEmpty(Param key, Object arg, TreeMap<String, Object> params) {
        if (arg == null) {
            //值为空则此参数提交
            //Integer\Double\Long\Float\String\File\Byte\Byte[]\byte[]\自定义对象\Map\List\Set
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
            return;
        }
        //字节流
        if ((arg instanceof byte[]) || (arg instanceof Byte[])) {
            params.put(key.value(), arg);
            return;
        }
        if (arg instanceof String) {
            params.put(key.value(), arg);
            return;
        }
        params.put(key.value(), arg);
    }

    private void putParamValue(Param key, Object arg, TreeMap<String, Object> params, HashMap<String, String> suffixParams, boolean isRemoveEmptyValueField) {
        if (suffixParams != null && !TextUtils.isEmpty(key.fileSuffixAfterUpload())) {
            //若设置了file类型的后缀参数则提交时以此后缀为次，未设置默认取文件本身后缀；
            suffixParams.put(key.value(), key.fileSuffixAfterUpload());
        }
        if (key.isRemoveEmptyValueField() || isRemoveEmptyValueField) {
            //若参数值无效则key-value不作为本次请求的参数序列中
            putValueByIsRemoveEmpty(key, arg, params);
        } else {
            params.put(key.value(), arg);
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
            TreeMap<Integer, UrlItemKey> urlItemKeys = getParamAnnotationObject(method, UrlItemKey.class);
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
                for (Map.Entry<Integer, UrlItemKey> entry : urlItemKeys.entrySet()) {
                    UrlItem urlItem = getMatchUrlItem(urlItems, String.valueOf(args[entry.getKey()]));
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
            TreeMap<Integer, Path> paramAnnotationObject = getParamAnnotationObject(method, Path.class);
            if (!ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                //如果未匹配到则按原地址请求
                for (Map.Entry<Integer, Path> pathIntegerEntry : paramAnnotationObject.entrySet()) {
                    Path path = pathIntegerEntry.getValue();
                    if (matches.contains(path.value())) {
                        rativeUrl = rativeUrl.replace(String.format("{%s}", path.value()), "%s");
                        rativeUrl = String.format(rativeUrl, String.valueOf(args[pathIntegerEntry.getKey()]));
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
                TreeMap<Integer, HeaderPart> paramAnnotationObject = getParamAnnotationObject(method, HeaderPart.class);
                if (!ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                    for (Map.Entry<Integer, HeaderPart> headerPartIntegerEntry : paramAnnotationObject.entrySet()) {
                        HeaderPart headerPart = headerPartIntegerEntry.getValue();
                        if (TextUtils.equals(headerPart.value(), matche)) {
                            String dataValue = String.valueOf(args[headerPartIntegerEntry.getKey()]);
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
                            TreeMap<Integer, HeaderPart> paramAnnotationObject = getParamAnnotationObject(method, HeaderPart.class);
                            if (!ObjectJudge.isNullOrEmpty(paramAnnotationObject)) {
                                for (Map.Entry<Integer, HeaderPart> headerPartIntegerEntry : paramAnnotationObject.entrySet()) {
                                    HeaderPart headerPart = headerPartIntegerEntry.getValue();
                                    if (TextUtils.equals(headerPart.value(), matche)) {
                                        String dataValue = String.valueOf(args[headerPartIntegerEntry.getKey()]);
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
    private <T> TreeMap<Integer, T> getParamAnnotationObject(Method method, Class<T> annotationClass) {
        TreeMap<Integer, T> lst = new TreeMap<Integer, T>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (!ObjectJudge.isNullOrEmpty(parameterAnnotations)) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                if (ObjectJudge.isNullOrEmpty(parameterAnnotation)) {
                    continue;
                }
                if (parameterAnnotation[0].annotationType() == annotationClass) {
                    lst.put(i, (T) parameterAnnotation[0]);
                }
            }
        }
        return lst;
    }
}
