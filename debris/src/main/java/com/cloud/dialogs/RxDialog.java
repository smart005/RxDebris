package com.cloud.dialogs;

import com.cloud.objects.config.Recycling;
import com.cloud.objects.events.OnRecyclingListener;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/14
 * Description:dialog配置
 * Modifier:
 * ModifyContent:
 */
public class RxDialog implements OnRecyclingListener {

    private static RxDialog rxDialog = null;
    private RxDialogConfigBuilder builder = null;
    private HashMap<String, Object> softConfigMap = new HashMap<String, Object>();
    private SoftReference<HashMap<String, Object>> softConfig = new SoftReference<HashMap<String, Object>>(softConfigMap);

    public static RxDialog getInstance() {
        return rxDialog == null ? rxDialog = new RxDialog() : rxDialog;
    }

    private RxDialog() {
        Recycling.getInstance().addRecyclingListener(this);
    }

    @Override
    public void recycling() {
        builder = null;
        rxDialog = null;
    }

    public RxDialogConfigBuilder getBuilder() {
        return builder == null ? builder = new RxDialogConfigBuilder() : builder;
    }

    public class RxDialogConfigBuilder {

        //loading icon resource id
        private int loadingIcon = 0;

        public int getLoadingIcon() {
            if (loadingIcon == 0) {
                HashMap<String, Object> map = softConfig.get();
                if (map != null && map.containsKey("LOADING_ICON")) {
                    return (int) map.get("LOADING_ICON");
                }
            }
            return loadingIcon;
        }

        public RxDialogConfigBuilder setLoadingIcon(int loadingIcon) {
            this.loadingIcon = loadingIcon;
            HashMap<String, Object> map = softConfig.get();
            map.put("LOADING_ICON", loadingIcon);
            return this;
        }
    }
}
