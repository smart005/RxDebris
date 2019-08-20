package com.cloud.debris.notify;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.cloud.debris.event.OnApkDownloadCompleteListener;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.storage.FilenameUtils;
import com.cloud.objects.storage.StorageUtils;
import com.cloud.objects.utils.GlobalUtils;

import java.io.File;
import java.util.HashMap;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-11
 * Description:下载apk管理
 * Modifier:
 * ModifyContent:
 */
public class DownloadApkManager {

    private HashMap<Long, DownloadProperties> downloadMap = new HashMap<>();
    private BroadcastReceiver broadcastReceiver;
    private OnApkDownloadCompleteListener onApkDownloadCompleteListener;

    public void setOnApkDownloadCompleteListener(OnApkDownloadCompleteListener listener) {
        this.onApkDownloadCompleteListener = listener;
    }

    private class DownloadProperties {
        public String url;
        public File downloadFile;
        public String target;
        public Object extras;
    }

    /**
     * 下载apk
     *
     * @param context     context
     * @param url         url
     * @param title       标题
     * @param description 文件描述
     * @param target      用于业务判断此次下载
     * @param extras      扩展参数
     */
    public void downApp(Context context, String url, String title, String description, String target, Object extras) {
        try {
            String suffix = GlobalUtils.getSuffixName(url);
            if (!TextUtils.equals(suffix, "apk")) {
                return;
            }
            String name = FilenameUtils.getName(url);
            //获取显示标题
            if (TextUtils.isEmpty(title)) {
                title = name;
            }
            //下载
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setVisibleInDownloadsUi(true);
            //通知栏显示的内容
            request.setTitle(title);
            request.setDescription(description);
            //下载过程和下载完成后通知栏有通知消息
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //下载文件存放路径
            File dir = StorageUtils.getDir("download");
            File downfile = new File(dir, name);
            request.setDestinationUri(Uri.fromFile(downfile));
            //禁止漫游状态下载
            request.setAllowedOverRoaming(false);
            //允许下载文件格式
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            request.setMimeType(mimeTypeMap.getMimeTypeFromExtension(url));
            //开始下载
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            //在接收到广播时用于判断下载是否完成
            long enqueue = downloadManager.enqueue(request);
            DownloadProperties properties = new DownloadProperties();
            properties.url = url;
            properties.downloadFile = downfile;
            properties.target = target;
            properties.extras = extras;
            downloadMap.put(enqueue, properties);
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (downloadMap.containsKey(id)) {
                        DownloadProperties downloadProperties = downloadMap.get(id);
                        if (onApkDownloadCompleteListener != null) {
                            onApkDownloadCompleteListener.onApkDownloadComplete(downloadProperties.target, downloadProperties.url, downloadProperties.downloadFile, downloadProperties.extras);
                        }
                        downloadMap.remove(id);
                    }
                }
            };
            context.registerReceiver(broadcastReceiver, intentFilter);
            if (onApkDownloadCompleteListener != null) {
                onApkDownloadCompleteListener.onApkDownloadStart(properties.target, properties.url, properties.extras);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    //销毁时释放资源
    public void destory(Context context) {
        if (broadcastReceiver == null || context == null) {
            return;
        }
        try {
            context.unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            //unregisterReceiver
        }
        broadcastReceiver = null;
    }
}
