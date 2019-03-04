package com.cloud.ebus;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/12/26
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ConnInfo {
    //service status connected
    private boolean isConnected = false;
    //ebus message queue (eg. key-value) for process
    private HashMap<String, Object[]> messageQueues = new HashMap<String, Object[]>();

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public HashMap<String, Object[]> getMessageQueues() {
        return messageQueues;
    }
}
