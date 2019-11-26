package com.cloud.debris.notify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.cloud.debris.R;
import com.cloud.images.glide.GBitmapCallback;
import com.cloud.images.glide.GlideOptimize;
import com.cloud.launchs.LauncherState;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.manager.ObjectManager;
import com.cloud.objects.utils.PixelUtils;
import com.cloud.objects.utils.RandomUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-26
 * Description:通知管理
 * Modifier:
 * ModifyContent:
 */
public class NotifyManager {

    private static NotifyManager notifyManager;
    private NotificationManager nm = null;
    private int screenWidth;

    private NotifyManager() {
        Context applicationContext = LauncherState.getApplicationContext();
        screenWidth = ObjectManager.getScreenWidth(applicationContext);
    }

    public static NotifyManager getInstance() {
        if (notifyManager == null) {
            synchronized (NotifyManager.class) {
                if (notifyManager == null) {
                    notifyManager = new NotifyManager();
                }
            }
        }
        return notifyManager;
    }

    private NotificationManager getNotificationManager(Context context) {
        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        }
        return nm;
    }

    /**
     * 取消通知
     *
     * @param context        context
     * @param notificationId 该通知id
     */
    public void cancel(Context context, int notificationId) {
        try {
            if (context == null) {
                return;
            }
            Context applicationContext = context.getApplicationContext();
            NotificationManager manager = getNotificationManager(applicationContext);
            manager.cancel(notificationId);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 清除所有通知
     *
     * @param context context
     */
    public void cancelAll(Context context) {
        if (context == null) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        NotificationManager manager = getNotificationManager(applicationContext);
        manager.cancelAll();
    }

    public class NotifyBuilder {

        private Context context;
        private Intent intent;
        private String packageName;
        private int icon;
        private String notifyTime;
        private Bitmap bitmap;
        private String image;
        private int imageRes;
        private String title;
        private String text;
        private int requestCode;
        private int notificationId;
        private boolean isBroadCast;

        public NotifyBuilder(Context context) {
            this.context = context.getApplicationContext();
            packageName = context.getPackageName();
        }

        /**
         * 设置通知跳转intent
         *
         * @param intent goto intent
         * @return NotifyBuilder
         */
        public NotifyBuilder setIntent(Intent intent) {
            this.intent = intent;
            return this;
        }

        /**
         * 设置通知icon
         *
         * @param icon 一般指app icon
         * @return NotifyBuilder
         */
        public NotifyBuilder setIcon(@DrawableRes int icon) {
            this.icon = icon;
            return this;
        }

        /**
         * 设置通知时间
         *
         * @param time 此条通知显示时间
         * @return NotifyBuilder
         */
        public NotifyBuilder setNotifyTime(String time) {
            this.notifyTime = time;
            return this;
        }

        /**
         * 设置通知图片(与setImage(...)同一字段)
         * 优先级高于setImage、setImageRes
         *
         * @param bitmap 信息右边显示的图片
         * @return NotifyBuilder
         */
        public NotifyBuilder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title title
         * @return NotifyBuilder
         */
        public NotifyBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置通知内容
         *
         * @param text 通知内容
         * @return NotifyBuilder
         */
        public NotifyBuilder setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * requestCode
         *
         * @param requestCode requestCode
         * @return NotifyBuilder
         */
        public NotifyBuilder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        /**
         * 通知id
         *
         * @param notificationId notificationId
         * @return NotifyBuilder
         */
        public NotifyBuilder setNotificationId(int notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        /**
         * 设置图片(与setBitmap(...)同一字段)
         * 优先级:> setBitmap & < setImageRes
         *
         * @param url url
         * @return NotifyBuilder
         */
        public NotifyBuilder setImage(String url) {
            this.image = url;
            return this;
        }

        /**
         * 设置图片资源
         * 优先级:< setImage < setBitmap
         *
         * @param imageRes imageRes
         * @return NotifyBuilder
         */
        public NotifyBuilder setImageRes(@DrawableRes int imageRes) {
            this.imageRes = imageRes;
            return this;
        }

        /**
         * 点击时是否广播跳转
         *
         * @param broadCast true-点击时跳转到指定的广播;false-跳转到指定的activity;
         * @return NotifyBuilder
         */
        public NotifyBuilder setBroadCast(boolean broadCast) {
            isBroadCast = broadCast;
            return this;
        }

        private RemoteViews buildRemoteViews(String title, String text) {
            RemoteViews remoteViews = new RemoteViews(packageName, R.layout.cl_cus_notitfication_view);
            //通知显示时间
            if (TextUtils.isEmpty(notifyTime)) {

                remoteViews.setViewVisibility(R.id.m_time, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.m_time, View.VISIBLE);
                remoteViews.setTextViewText(R.id.m_time, notifyTime);
            }
            //image
            if (bitmap == null) {
                if (imageRes == 0) {
                    remoteViews.setViewVisibility(R.id.m_img_iv, View.GONE);
                } else {
                    remoteViews.setViewVisibility(R.id.m_img_iv, View.VISIBLE);
                    remoteViews.setImageViewResource(R.id.m_img_iv, imageRes);
                }
            } else {
                remoteViews.setViewVisibility(R.id.m_img_iv, View.VISIBLE);
                remoteViews.setImageViewBitmap(R.id.m_img_iv, bitmap);
            }
            //title
            if (TextUtils.isEmpty(title)) {
                remoteViews.setViewVisibility(R.id.m_title_tv, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.m_title_tv, View.VISIBLE);
                remoteViews.setTextViewText(R.id.m_title_tv, title);
            }
            //content
            remoteViews.setTextViewText(R.id.m_text, text);
            return remoteViews;
        }

        private void showNotify() {
            //这里最好不用activity:
            //1.当前activity处于假前端时可能会出现异常;
            //2.传入的activity未使用完成时资源是无法释放的;
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent startIntent;
            if (isBroadCast) {
                startIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                startIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            String textLimit = textLimit(text);
            String mtitle = (TextUtils.isEmpty(this.title) ? textLimit : this.title);
            RemoteViews remoteViews = buildRemoteViews(mtitle, textLimit);
            //定义按钮点击后的动作(设置后auto cancel失效)
            //remoteViews.setOnClickPendingIntent(R.id.notice_root_view, startIntent);
            NotificationManager manager = getNotificationManager(context);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, packageName);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(
                        String.format("%s.channel", packageName),
                        "畅说108通知", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                manager.createNotificationChannel(channel);
                notification.setChannelId(String.format("%s.channel", packageName));
            }
            notification.setCustomContentView(remoteViews);
            //该通知要启动的activity
            notification.setContentIntent(startIntent);
            //当前时间
            notification.setWhen(System.currentTimeMillis());
            //设置顶部状态栏的小图标
            notification.setSmallIcon(icon);
            //在顶部状态栏中的提示信息
            //title->text
            notification.setTicker(mtitle);
            notification.setContentTitle(title);
            notification.setContentText(text);
            // 将Ongoing设为true 那么notification将不能滑动删除
            notification.setOngoing(false);
            /*
             * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
             * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
             */
            if (Build.VERSION.SDK_INT >= 24) {
                notification.setPriority(NotificationCompat.PRIORITY_MAX);
            } else {
                notification.setPriority(Notification.PRIORITY_DEFAULT);
            }
            notification.setTimeoutAfter(2592000000L);
            /*
             * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
             * Notification.DEFAULT_SOUND：系统默认铃声。
             * Notification.DEFAULT_VIBRATE：系统默认震动。
             * Notification.DEFAULT_LIGHTS：系统默认闪光。
             * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
             */
            notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.setSound(sound);
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(notification).bigText(text);
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.setSummaryText(textLimit);
            Notification build = bigTextStyle.build();
            //设置 Notification 的 flags = FLAG_NO_CLEAR
            //FLAG_NO_CLEAR 表示该通知不能被状态栏的清除按钮给清除掉,也不能被手动清除,但能通过 cancel() 方法清除
            //flags 可以通过 |= 运算叠加效果
            build.flags |= Notification.FLAG_NO_CLEAR;
            manager.notify(getNotifyId(), build);
        }

        private int getNotifyId() {
            if (notificationId == 0) {
                notificationId = RandomUtils.getRandom(1000, 4000);
            }
            return notificationId;
        }

        public void show() {
            if (context == null || intent == null || TextUtils.isEmpty(packageName)) {
                return;
            }
            try {
                if (bitmap == null) {
                    if (TextUtils.isEmpty(image)) {
                        showNotify();
                    } else {
                        GlideOptimize.with(context)
                                .load(image)
                                .setRoundCorners(4)
                                .setWidth(PixelUtils.dip2px(46))
                                .setHeight(PixelUtils.dip2px(46))
                                .setImageRule("?imageMogr2/auto-orient/thumbnail/{0}x/format/webp/q/75/sharpen/1/ignore-error/1")
                                .into(new GBitmapCallback() {
                                    @Override
                                    public void call(Bitmap bitmap) {
                                        NotifyBuilder.this.bitmap = bitmap;
                                        showNotify();
                                    }
                                });
                    }
                } else {
                    showNotify();
                }
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }

    public NotifyBuilder builder(Context context) {
        Context applicationContext = context.getApplicationContext();
        return new NotifyBuilder(applicationContext);
    }

    private String textLimit(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        if (screenWidth <= 0) {
            return text;
        }
        try {
            int width = screenWidth - PixelUtils.dip2px(24);
            int textSize = PixelUtils.sp2px(14);
            int preNumber = width / textSize;
            int count = text.length();
            int total = preNumber;
            if (count > total) {
                String stext = text.substring(0, total - 4);
                StringBuilder builder = new StringBuilder(stext);
                builder.append(">>>");
                return builder.toString();
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return text;
    }
}