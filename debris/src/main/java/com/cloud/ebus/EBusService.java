package com.cloud.ebus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/12/25
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class EBusService extends Service {

    private int processId = 0;
    private EBusBinder binder = null;

    @Override
    public void onCreate() {
        super.onCreate();
        processId = android.os.Process.myPid();
    }

    @Override
    public IBinder onBind(Intent intent) {
        binder = new EBusBinder(processId);
        return binder;
    }
}
