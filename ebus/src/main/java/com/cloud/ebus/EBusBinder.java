package com.cloud.ebus;

import com.cloud.objects.utils.JsonUtils;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/12/25
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class EBusBinder extends IEBusAidl.Stub {

    private int processId = 0;

    public EBusBinder(int processId) {
        this.processId = processId;
    }

    @Override
    public int getProcessId() {
        return this.processId;
    }

    @Override
    public void receiveEBusData(String receiveKey, String events) {
        List<String> array = JsonUtils.parseArray(events, String.class);
        if (array != null && array.size() > 0) {
            Object[] objects = new Object[array.size()];
            for (int i = 0; i < array.size(); i++) {
                objects[i] = array.get(i);
            }
            EBus.getInstance().post(receiveKey, objects);
        } else {
            EBus.getInstance().post(receiveKey);
        }
    }
}
