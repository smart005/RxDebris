/*Copyright 2016 Zheng Zibin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package com.cloud.images.compress;

import android.graphics.Bitmap;
import android.support.annotation.IntDef;

import com.cloud.images.RxImage;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Luban {

    public static final int FIRST_GEAR = 1;

    public static final int THIRD_GEAR = 3;

    public static final int CUSTOM_GEAR = 4;

    private File mFile;

    private List<File> mFileList;

    private LubanBuilder mBuilder;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Luban(File cacheDir) {
        mBuilder = new LubanBuilder(cacheDir);
    }

    /**
     * 压缩图片
     *
     * @param file 需要压缩的图片文件
     * @return Luban
     */
    public static Luban compress(File file) {
        RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
        File imageCacheDir = builder.getImageCacheDir();
        Luban luban = new Luban(imageCacheDir);
        luban.mFile = file;
        luban.mFileList = Collections.singletonList(file);
        return luban;
    }

    /**
     * 压缩图片集合
     *
     * @param files 图片集合
     * @return Luban
     */
    public static Luban compress(List<File> files) {
        RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
        File imageCacheDir = builder.getImageCacheDir();
        Luban luban = new Luban(imageCacheDir);
        luban.mFileList = new ArrayList<>(files);
        luban.mFile = files.get(0);
        return luban;
    }

    /**
     * 自定义压缩模式 FIRST_GEAR、THIRD_GEAR、CUSTOM_GEAR
     */
    public Luban putGear(@GEAR int gear) {
        mBuilder.gear = gear;
        return this;
    }

    /**
     * 自定义图片压缩格式
     */
    public Luban setCompressFormat(Bitmap.CompressFormat compressFormat) {
        mBuilder.compressFormat = compressFormat;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大体积
     */
    public Luban setMaxSize(int size) {
        mBuilder.maxSize = size;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大宽度
     *
     * @param width 最大宽度
     */
    public Luban setMaxWidth(int width) {
        mBuilder.maxWidth = width;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大高度
     *
     * @param height 最大高度
     */
    public Luban setMaxHeight(int height) {
        mBuilder.maxHeight = height;
        return this;
    }

    /**
     * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
     *
     * @param listener 接收回调结果
     */
    public void launch(final OnCompressListener listener) {
        Disposable subscribe = asObservable().observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(
                new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.onStart();
                    }
                })
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        listener.onSuccess(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.onError(throwable);
                    }
                });
        mCompositeDisposable.add(subscribe);
    }

    /**
     * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
     *
     * @param listener 接收回调结果
     */
    public void launch(final OnMultiCompressListener listener) {
        Disposable subscribe = asListObservable().observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.onStart();
                    }
                })
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        listener.onSuccess(files);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.onError(throwable);
                    }
                });
        mCompositeDisposable.add(subscribe);
    }

    /**
     * 返回File Observable
     */
    public Observable<File> asObservable() {
        LubanCompresser compresser = new LubanCompresser(mBuilder);
        return compresser.singleAction(mFile);
    }

    /**
     * 返回fileList Observable
     */
    public Observable<List<File>> asListObservable() {
        LubanCompresser compresser = new LubanCompresser(mBuilder);
        return compresser.multiAction(mFileList);
    }

    @IntDef({FIRST_GEAR, THIRD_GEAR, CUSTOM_GEAR})
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.SOURCE)
    @Documented
    @Inherited
    @interface GEAR {

    }
}