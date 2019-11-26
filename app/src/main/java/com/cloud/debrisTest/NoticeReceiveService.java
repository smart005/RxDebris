package com.cloud.debrisTest;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-09-26
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class NoticeReceiveService extends NotificationListenerService {
    public NoticeReceiveService() {
        super();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        //服务成功启动后回调,可能有1~3秒延迟;
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        //与通知信息流断开连接,此时一般需要重连才能收到后续消息;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        //监听到通知栏的一条消息,包括:本应用消息、此服务独立于应用离线消息、其它应用任何推送消息;
        //一般过滤对应消息业务在这里处理;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //通知栏消息被移除时回调;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //此服务被销毁时回调
    }
}
