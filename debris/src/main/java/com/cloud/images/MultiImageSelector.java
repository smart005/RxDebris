package com.cloud.images;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.cloud.dialogs.toasty.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 图片选择器
 * Created by nereo on 16/3/17.
 */
public class MultiImageSelector {

    public static final String EXTRA_RESULT = MultiImageSelectorActivity.EXTRA_RESULT;

    private boolean mShowCamera = true;
    private int mMaxCount = 9;
    private int mMode = MultiImageSelectorActivity.MODE_MULTI;
    private ArrayList<String> mOriginData;
    private static MultiImageSelector sSelector;

    @Deprecated
    private MultiImageSelector(Context context) {

    }

    private MultiImageSelector() {
    }

    @Deprecated
    public static MultiImageSelector create(Context context) {
        if (sSelector == null) {
            sSelector = new MultiImageSelector(context);
        }
        return sSelector;
    }

    public static MultiImageSelector create() {
        if (sSelector == null) {
            sSelector = new MultiImageSelector();
        }
        return sSelector;
    }

    public MultiImageSelector showCamera(boolean show) {
        mShowCamera = show;
        return sSelector;
    }

    public MultiImageSelector count(int count) {
        mMaxCount = count;
        return sSelector;
    }

    public MultiImageSelector single() {
        mMode = MultiImageSelectorActivity.MODE_SINGLE;
        return sSelector;
    }

    public MultiImageSelector multi() {
        mMode = MultiImageSelectorActivity.MODE_MULTI;
        return sSelector;
    }

    public MultiImageSelector origin(ArrayList<String> images) {
        mOriginData = images;
        return sSelector;
    }

    public void start(final FragmentActivity activity, final int requestCode) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        Disposable disposable = rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            activity.startActivityForResult(createIntent(activity), requestCode);
                        } else {
                            ToastUtils.show(activity, "无摄像头权限,请转到设置页面开启.");
                        }
                    }
                });
    }

    public void start(final Fragment fragment, final int requestCode) {
        RxPermissions rxPermissions = new RxPermissions(fragment);
        Disposable disposable = rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            fragment.startActivityForResult(createIntent(fragment.getContext()), requestCode);
                        } else {
                            ToastUtils.show(fragment.getContext(), "无摄像头权限,请转到设置页面开启.");
                        }
                    }
                });
    }

    private Intent createIntent(Context context) {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, mShowCamera);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, mMaxCount);
        if (mOriginData != null) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData);
        }
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mMode);
        return intent;
    }
}
