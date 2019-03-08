package com.cloud.debrisTest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cloud.debrisTest.okhttp.beans.RecommandInfo;
import com.cloud.debrisTest.okhttp.services.GetService;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.events.OnSuccessfulListener;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.JsonUtils;

public class OKHttpSimple extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_view);

        getService.requestRecommandInfo(42, recommandListener);
    }

    private OnSuccessfulListener<RecommandInfo> recommandListener = new OnSuccessfulListener<RecommandInfo>() {
        @Override
        public void onSuccessful(RecommandInfo recommandInfo, DataType dataType, Object... extras) {
            //具体接口请求成功回调;
            //如果有缓存且存在缓存和网络均会回调时则isLastCall==true表示最后一次回调
            Logger.info(JsonUtils.toStr(recommandInfo));
        }

        @Override
        public void onError(RecommandInfo recommandInfo, Object... extras) {
            //【可选】具体接口请求失败回调
        }

        @Override
        public void onCompleted(Object... extras) {
            //【可选】具体接口请求完成回调
        }
    };

    private GetService getService = new GetService() {
        @Override
        protected void onRequestError() {
            //【可选】请求失败全局回调事件
        }

        @Override
        protected void onRequestCompleted() {
            //【可选】请求完成全局回调事件
        }
    };
}
