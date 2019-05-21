package com.cloud.mixed.h5.events;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.ValueCallback;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-05-19
 * Description:
 * Modifier:
 * ModifyContent:
 */
public interface OnWebViewListener {

    public boolean shouldOverrideUrlLoading(Object view, String url);

    public boolean shouldOverrideUrlLoading(Object view, Object webResourceRequest);

    public void onReceivedError(Object view, int errorCode, String description, String failingUrl);

    public void onPageStarted(Object view, String url, Bitmap favicon);

    public void onPageFinished(Object view, String url);

    public boolean onJsConfirm(Object view, String url, String message, Object result);

    public boolean onJsPrompt(Object view, String url, String message, String defaultValue, Object result);

    public boolean onJsAlert(Object view, String url, String message, Object result);

    public void onProgressChanged(Object view, int newProgress);

    public void onReceivedTitle(Object view, String title);

    public void openFileChooser(ValueCallback<Uri> uploadMsg);

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType);

    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture);

    public boolean onShowFileChooser(Object view, ValueCallback<Uri[]> uploadMsg, Object fileChooserParams);
}
