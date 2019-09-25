package com.cloud.debrisTest;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-09-06
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class NotificationManagerUtils {
    @SuppressLint("WrongConstant")
    public static void startNotificationManager(String title, int idIco){
        Context applicationContext = TestApplication.getInstance().getApplicationContext();

        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(applicationContext, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0);

        long [] vibrate = {0, 500, 1000, 1500};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder notification = new Notification
                    .Builder(applicationContext)
                    .setContentTitle("您有一条新消息")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(idIco)
                    .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                    .setVibrate(vibrate)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setChannelId(applicationContext.getPackageName())
                    .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);

            NotificationChannel channel = new NotificationChannel(
                    applicationContext.getPackageName(),
                    "会话消息(掌嗨)",
                    NotificationManager.IMPORTANCE_DEFAULT

            );

            notificationManager.createNotificationChannel(channel);

            notificationManager.notify(0,notification.build());
        }else{

            Notification.Builder notification = new Notification
                    .Builder(applicationContext)
                    .setContentTitle("您有一条新消息")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(idIco)
                    .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                    .setVibrate(vibrate)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
            notificationManager.notify(0,notification.build());

        }

    }
}
