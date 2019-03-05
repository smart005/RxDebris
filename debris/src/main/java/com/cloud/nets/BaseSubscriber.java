package com.cloud.nets;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cloud.nets.events.OnAuthCallInfoListener;
import com.cloud.nets.events.OnSuccessfulListener;
import com.cloud.nets.properties.OkRxConfigParams;
import com.cloud.nets.properties.ReqQueueItem;
import com.cloud.objects.HandlerManager;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.ResultState;
import com.cloud.objects.events.Runnable1;
import com.cloud.objects.logs.Logger;
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

    private OnAuthCallInfoListener onAuthCallInfoListener = null;

    /**
     * 是否对回调结果进行验证
     */
    private boolean isValidCallResult = true;
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = null;
    private String apiRequestKey = "";
    private Handler mhandler = new Handler(Looper.getMainLooper());
    private ScheduledThreadPoolExecutor multiTaskExecutor = null;
    //接口成功回调监听
    private OnSuccessfulListener onSuccessfulListener = null;

    public ScheduledThreadPoolExecutor getMultiTaskExecutor() {
        if (multiTaskExecutor == null) {
            multiTaskExecutor = ThreadPoolUtils.getInstance().getMultiTaskExecutor(10);
        }
        return multiTaskExecutor;
    }

    /**
     * 设置未登录回调监听
     *
     * @param listener 事件监听
     */
    public void setOnAuthCallInfoListener(OnAuthCallInfoListener listener) {
        this.onAuthCallInfoListener = listener;
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
     * 获取未登录回调监听
     *
     * @return 事件监听
     */
    public OnAuthCallInfoListener getOnAuthCallInfoListener() {
        return this.onAuthCallInfoListener;
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

    public void setLoginValid(boolean isLoginValid) {
        this.isLoginValid = isLoginValid;
    }

    public void setApiUnloginRetCodes(List<String> apiUnloginRetCodes) {
        this.apiUnloginRetCodes = apiUnloginRetCodes;
    }

    public <ExtraT> BaseSubscriber(BaseT cls) {
        this.baseT = cls;
        if (baseT != null) {
            baseT.setBaseSubscriber(this);
        }
    }

    public void onCompleted() {
        if (baseT == null) {
            return;
        }
        if (ObjectJudge.isMainThread()) {
            if (onSuccessfulListener != null) {
                onSuccessfulListener.onCompleted(extra);
            }
            baseT.onRequestCompleted();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    if (onSuccessfulListener != null) {
                        onSuccessfulListener.onCompleted(extra);
                    }
                    baseT.onRequestCompleted();
                }
            });
        }
    }

    public void onNext(T t, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiRequestKey, boolean isLastCall, long requestStartTime, long requestTotalTime) {
        this.reqQueueItemHashMap = reqQueueItemHashMap;
        this.apiRequestKey = apiRequestKey;
        //如果isProcessNetResults==false则直接返回交由外面处理
        OkRxConfigParams okRxConfigParams = OkRx.getInstance().getOkRxConfigParams();
        if (okRxConfigParams.isProcessNetResults()) {
            ScheduledThreadPoolExecutor multiTaskExecutor = getMultiTaskExecutor();
            ThreadPoolUtils.getInstance().addTask(multiTaskExecutor, new ApiCallWith(t, isLastCall, requestStartTime, requestTotalTime));
        } else {
            successWith(t, ResultState.Success, isLastCall, requestStartTime, requestTotalTime);
        }
    }

    private class ResultParams {
        public T t;
        public boolean isLastCall;
        public long requestStartTime;
        public long requestTotalTime;
        public ResultState state;
    }

    private void successWith(T t, ResultState state, boolean isLastCall, long requestStartTime, long requestTotalTime) {
        ResultParams resultParams = new ResultParams();
        resultParams.t = t;
        resultParams.state = state;
        resultParams.isLastCall = isLastCall;
        resultParams.requestStartTime = requestStartTime;
        resultParams.requestTotalTime = requestTotalTime;
        if (isLastCall) {
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

    private class ApiHandlerRun implements Runnable {

        private ResultParams params = null;

        public ApiHandlerRun(ResultParams params) {
            this.params = params;
        }

        @Override
        public void run() {
            if (params == null) {
                return;
            }
            if (onSuccessfulListener != null) {
                onSuccessfulListener.onSuccessful(params.t, params.isLastCall, extra);
            }
            requestFinishWith();
        }
    }

    private void requestFinishWith() {
        if (reqQueueItemHashMap == null || TextUtils.isEmpty(apiRequestKey)) {
            return;
        }
        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
            queueItem.setSuccess(true);//这里只作为临时变量，如果成功先回调那么在onRequestCompleted中有用;
            if (queueItem.isReqNetCompleted() && queueItem.isSuccess()) {
                onCompleted();
                reqQueueItemHashMap.remove(apiRequestKey);
            }
        } else {
            onCompleted();
        }
    }

    private class ApiCallWith implements Runnable {

        private T t;
        private boolean isLastCall;
        private long requestStartTime;
        private long requestTotalTime;

        public ApiCallWith(T t, boolean isLassCall, long requestStartTime, long requestTotalTime) {
            this.t = t;
            this.isLastCall = isLassCall;
            this.requestStartTime = requestStartTime;
            this.requestTotalTime = requestTotalTime;
        }

        @Override
        public void run() {
            try {
                if (t == null) {
                    successWith(t, ResultState.Fail, isLastCall, requestStartTime, requestTotalTime);
                    requestFinishWith();
                    return;
                }
                if (TextUtils.isEmpty(apiName)) {
                    apiName = baseT.getApiName();
                }
                OkRxConfigParams configParams = OkRx.getInstance().getOkRxConfigParams();
                //获取状态码值
                String code = String.valueOf(GlobalUtils.getPropertiesValue(t, "code"));
                if (TextUtils.isEmpty(code)) {
                    successWith(t, ResultState.Success, isLastCall, requestStartTime, requestTotalTime);
                } else {
                    if (isValidCallResult) {
                        //需要做基本的回调验证
                        if (configParams.getApiSuccessRetCodes().contains(code) ||
                                configParams.getApiNameCodeValidFilter().contains(apiName) ||
                                (allowRetCodes != null && allowRetCodes.contains(code))) {
                            successWith(t, ResultState.Success, isLastCall, requestStartTime, requestTotalTime);
                        } else {
                            if (configParams.getUnauthorizedRet().contains(code)) {
                                unLoginSend(t);
                            } else {
                                failRemind(configParams.getApiSuccessRetCodes(), t, configParams.getMessageFilterRetCodes(), code);
                            }
                            successWith(t, ResultState.Fail, isLastCall, requestStartTime, requestTotalTime);
                        }
                    } else {
                        //只作未登录验证
                        if (configParams.getApiSuccessRetCodes().contains(code) ||
                                configParams.getApiNameCodeValidFilter().contains(apiName) ||
                                (allowRetCodes != null && allowRetCodes.contains(code))) {
                            successWith(t, ResultState.Success, isLastCall, requestStartTime, requestTotalTime);
                        } else {
                            if (configParams.getUnauthorizedRet().contains(code)) {
                                unLoginSend(t);
                                successWith(t, ResultState.Fail, isLastCall, requestStartTime, requestTotalTime);
                            } else {
                                successWith(t, ResultState.None, isLastCall, requestStartTime, requestTotalTime);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                successWith(t, ResultState.Fail, isLastCall, requestStartTime, requestTotalTime);
                requestFinishWith();
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
                onSuccessfulListener.onError(t, this.extra);
            }
        }
    }

    private void unLoginSend(T t) {
        if (baseT != null) {
            //请求token api请求中的token清空
            baseT.setToken("");
        }
        if (onAuthCallInfoListener != null) {
            HandlerManager.getInstance().post(new Runnable1<T>(t) {
                @Override
                public void run() {
                    onAuthCallInfoListener.onCallInfo(apiName);
                }
            });
        }
    }
}
