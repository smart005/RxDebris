package com.cloud.nets;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.events.OnAuthListener;
import com.cloud.nets.events.OnSuccessfulListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.enums.ResultState;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.observable.ObservableComponent;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.ThreadPoolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/6/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseSubscriber<T, BaseT extends BaseService> {

    private BaseT baseT = null;
    private String apiName = "";
    private Object[] extra = null;
    private List<String> allowRetCodes = new ArrayList<String>();
    private boolean isLoginValid = true;
    private List<String> apiUnloginRetCodes = new ArrayList<String>();
    /**
     * 请求方法名
     */
    private String invokeMethodName = "";

    /**
     * 是否对回调结果进行验证
     */
    private boolean isValidCallResult = true;
    private String apiRequestKey = "";
    private Handler mhandler = new Handler(Looper.getMainLooper());
    private ScheduledThreadPoolExecutor multiTaskExecutor = null;
    //接口成功回调监听
    private OnSuccessfulListener onSuccessfulListener = null;

    public void setInvokeMethodName(String invokeMethodName) {
        this.invokeMethodName = invokeMethodName;
    }

    public ScheduledThreadPoolExecutor getMultiTaskExecutor() {
        if (multiTaskExecutor == null) {
            multiTaskExecutor = ThreadPoolUtils.getInstance().getMultiTaskExecutor(10);
        }
        return multiTaskExecutor;
    }

    /**
     * 设置接口成功回调监听
     *
     * @param onSuccessfulListener 监听对象
     */
    public void setOnSuccessfulListener(@NonNull OnSuccessfulListener onSuccessfulListener) {
        this.onSuccessfulListener = onSuccessfulListener;
    }

    public OnSuccessfulListener getOnSuccessfulListener() {
        return onSuccessfulListener;
    }

    /**
     * 对接口返回的结果是否需要进行验证
     *
     * @param isValidCallResult true-做相关验证及过滤;false-不处理;
     */
    public void setValidCallResult(boolean isValidCallResult) {
        this.isValidCallResult = isValidCallResult;
    }

    /**
     * 设置api名称
     *
     * @param apiName 当前请求的api名称
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * 设置扩展数据
     *
     * @param extra 扩展数据
     */
    public void setExtra(Object... extra) {
        this.extra = extra;
    }

    /**
     * 获取扩展数据
     *
     * @return 扩展数据
     */
    public Object[] getExtra() {
        return extra;
    }

    /**
     * 获取接口定义中的请允许验证通过的返回码
     *
     * @return 返回码集合
     */
    public List<String> getAllowRetCodes() {
        return this.allowRetCodes;
    }

    public <ExtraT> BaseSubscriber(BaseT cls) {
        this.baseT = cls;
        if (baseT != null) {
            baseT.setBaseSubscriber(this);
        }
    }

    public void onNext(T t, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiRequestKey, DataType dataType, long requestStartTime, long requestTotalTime) {
        this.apiRequestKey = apiRequestKey;
        //如果isProcessNetResults==false则直接返回交由外面处理
        OkRxConfigParams okRxConfigParams = OkRx.getInstance().getOkRxConfigParams();
        if (okRxConfigParams.isProcessNetResults()) {
            component.build(t, dataType, requestStartTime, requestTotalTime);
        } else {
            successWith(t, ResultState.Success, dataType, requestStartTime, requestTotalTime);
        }
    }

    private ObservableComponent<Object, Object> component = new ObservableComponent<Object, Object>() {
        @Override
        protected Object subscribeWith(Object[] params) {
            T t = (T) params[0];
            DataType dataType = (DataType) params[1];
            long requestStartTime = ConvertUtils.toLong(params[2]);
            long requestTotalTime = ConvertUtils.toLong(params[3]);
            new ApiCallWith(t, dataType, requestStartTime, requestTotalTime);
            return null;
        }
    };

    private class ResultParams {
        public T t;
        public DataType dataType;
        public long requestStartTime;
        public long requestTotalTime;
        public ResultState state;
    }

    private void successWith(T t, ResultState state, DataType dataType, long requestStartTime, long requestTotalTime) {
        ResultParams resultParams = new ResultParams();
        resultParams.t = t;
        resultParams.state = state;
        resultParams.dataType = dataType;
        resultParams.requestStartTime = requestStartTime;
        resultParams.requestTotalTime = requestTotalTime;
        if (dataType == DataType.NetData) {
            long cdiff = System.currentTimeMillis() - requestStartTime;
            if (cdiff >= requestTotalTime) {
                //将数据发送到main线程
                HandlerManager.getInstance().post(new ApiHandlerRun(resultParams));
            } else {
                //倒计时时间差(以毫秒为单位)
                long mdiff = requestTotalTime - cdiff;
                delayCall(mdiff, new ApiHandlerRun(resultParams));
            }
        } else {
            //将数据发送到main线程
            HandlerManager.getInstance().post(new ApiHandlerRun(resultParams));
        }
    }

    @SuppressLint("CheckResult")
    private void delayCall(long milliseconds, ApiHandlerRun run) {
        Observable.just(run)
                .delay(milliseconds, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiHandlerRun>() {
                    @Override
                    public void accept(ApiHandlerRun run) {
                        if (run == null) {
                            return;
                        }
                        run.run();
                    }
                });
    }

    private class ApiHandlerRun extends RunnableParamsN {

        private ResultParams params = null;

        public ApiHandlerRun(ResultParams params) {
            this.params = params;
        }

        @Override
        public void run(Object... objects) {
            if (params == null) {
                return;
            }
            if (onSuccessfulListener != null) {
                onSuccessfulListener.onSuccessful(params.t, params.dataType, extra);
                onSuccessfulListener.onCompleted(extra);
            }
        }
    }

    private class ApiCallWith implements Runnable {

        private T t;
        private DataType dataType;
        private long requestStartTime;
        private long requestTotalTime;

        public ApiCallWith(T t, DataType dataType, long requestStartTime, long requestTotalTime) {
            this.t = t;
            this.dataType = dataType;
            this.requestStartTime = requestStartTime;
            this.requestTotalTime = requestTotalTime;
        }

        @Override
        public void run() {
            try {
                if (t == null) {
                    successWith(t, ResultState.Fail, dataType, requestStartTime, requestTotalTime);
                    return;
                }
                if (TextUtils.isEmpty(apiName)) {
                    apiName = baseT.getApiName();
                }
                OkRxConfigParams configParams = OkRx.getInstance().getOkRxConfigParams();
                //获取状态码值
                String code = String.valueOf(GlobalUtils.getPropertiesValue(t, "code"));
                if (TextUtils.isEmpty(code)) {
                    successWith(t, ResultState.Success, dataType, requestStartTime, requestTotalTime);
                } else {
                    if (isValidCallResult) {
                        //需要做基本的回调验证
                        if (configParams.getApiSuccessRetCodes().contains(code) ||
                                configParams.getApiNameCodeValidFilter().contains(apiName) ||
                                (allowRetCodes != null && allowRetCodes.contains(code))) {
                            successWith(t, ResultState.Success, dataType, requestStartTime, requestTotalTime);
                        } else {
                            if (configParams.getUnauthorizedRet().contains(code)) {
                                unLoginSend(t);
                            } else {
                                failRemind(configParams.getApiSuccessRetCodes(), t, configParams.getMessageFilterRetCodes(), code);
                            }
                            successWith(t, ResultState.Fail, dataType, requestStartTime, requestTotalTime);
                        }
                    } else {
                        //只作未登录验证
                        if (configParams.getApiSuccessRetCodes().contains(code) ||
                                configParams.getApiNameCodeValidFilter().contains(apiName) ||
                                (allowRetCodes != null && allowRetCodes.contains(code))) {
                            successWith(t, ResultState.Success, dataType, requestStartTime, requestTotalTime);
                        } else {
                            if (configParams.getUnauthorizedRet().contains(code)) {
                                unLoginSend(t);
                                successWith(t, ResultState.Fail, dataType, requestStartTime, requestTotalTime);
                            } else {
                                successWith(t, ResultState.None, dataType, requestStartTime, requestTotalTime);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                successWith(t, ResultState.Fail, dataType, requestStartTime, requestTotalTime);
                Logger.error(e);
            }
        }
    }

    private void failRemind(Set<String> apiSuccessRet, T t, Set<String> messageFilterCodes, final String code) {
        if (TextUtils.isEmpty(code) || !messageFilterCodes.contains(code)) {
            if (apiSuccessRet.contains(code)) {
                return;
            }
            if (onSuccessfulListener != null) {
                onSuccessfulListener.onError(t, ErrorType.businessProcess, this.extra);
                onSuccessfulListener.onError(ErrorType.businessProcess, this.extra);
            }
        }
    }

    private void unLoginSend(T t) {
        final OnAuthListener authListener = OkRx.getInstance().getOnAuthListener();
        if (authListener != null) {
            HandlerManager.getInstance().post(new RunnableParamsN<Object>() {
                @Override
                public void run(Object... objects) {
                    authListener.onLoginCall(invokeMethodName);
                }
            });
        }
    }
}
