package com.cloud.nets.requests;

import com.cloud.nets.OkRx;
import com.cloud.nets.events.OnRequestErrorListener;

import java.io.IOException;

import okhttp3.Call;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:错误处理
 * Modifier:
 * ModifyContent:
 */
public class ErrorWith {

    public void call(Call call, IOException e) {
        OnRequestErrorListener errorListener = OkRx.getInstance().getOnRequestErrorListener();
        if (errorListener == null) {
            return;
        }
        //errorListener.onFailure(call, e);
    }
}
