package com.cloud.debrisTest.okhttp;

import android.support.test.runner.AndroidJUnit4;

import com.cloud.debrisTest.okhttp.beans.RecommandInfo;
import com.cloud.debrisTest.okhttp.services.GetService;
import com.cloud.nets.events.OnSuccessfulListener;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GetInstrumentedTest {
    @Test
    public void useAppContext() {
        getService.requestRecommandInfo(recommandListener);
    }

    private OnSuccessfulListener<RecommandInfo> recommandListener = new OnSuccessfulListener<RecommandInfo>() {
        @Override
        public void onSuccessful(RecommandInfo recommandInfo, Object... extras) {

        }
    };

    private GetService getService = new GetService();
}
