package com.cloud.images.figureset;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.cloud.debris.R;
import com.cloud.dialogs.toasty.ToastUtils;
import com.cloud.images.MultiImageSelector;
import com.cloud.images.MultiImageSelectorActivity;
import com.cloud.images.RxImage;
import com.cloud.images.beans.SelectImageProperties;
import com.cloud.images.compress.Luban;
import com.cloud.images.compress.OnMultiCompressListener;
import com.cloud.images.cropimage.Crop;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.GlobalUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class ImageSelectDialog {

    private Activity activity = null;

    private int REQUEST_IMAGE = 10755;

    private int TAILORING_REQUEST_CODE = 32164;
    //仅拍照返回码标识
    private int ONLY_TAKING_RESULT_CODE = 3747;

    /**
     * 是否需要剪裁(只有单选时此属性才生效,默认为false)
     */
    private boolean isTailoring = false;
    /**
     * 最多选择数(最小可设为1)
     */
    private int maxSelectNumber = 1;
    /**
     * 是否显示拍照图片(默认为显示)
     */
    private boolean isShowTakingPictures = true;
    /**
     * 已选择图片
     */
    private ArrayList<String> selectedImages = null;
    /**
     * 图片路径
     */
    private List<SelectImageProperties> imagePaths = new ArrayList<SelectImageProperties>();
    /**
     * 最大文件大小
     */
    private int maxFileSize = 1024;
    /**
     * 最大图片宽度
     */
    private int maxImageWidth = 1080;
    /**
     * 最大图片高度
     */
    private int maxImageHeight = 1920;

    private int ASPECT_X = 0;
    private int ASPECT_Y = 0;
    private int MAX_X = 0;
    private int MAX_Y = 0;

    private final int PROCESS_COMPLETED_WITH = 1704312529;

    private Object extra = null;

    private File takingFile = null;

    private void init() {
        isTailoring = false;
        maxSelectNumber = 1;
        isShowTakingPictures = true;
        selectedImages = null;
        imagePaths.clear();
        extra = null;
        takingFile = null;
    }

    /**
     * 选择图片完成(在失败时也会回调此方法，此时参数值可能为空)
     *
     * @param selectImageProperties 选择图片属性
     */
    protected void onSelectCompleted(List<SelectImageProperties> selectImageProperties, Object extra) {

    }

    /**
     * 设置扩展数据
     *
     * @param extra 扩展数据
     */
    public void setExtra(Object extra) {
        this.extra = extra;
    }

    /**
     * 获取扩展数据
     *
     * @return 扩展数据
     */
    public Object getExtra() {
        return this.extra;
    }

    /**
     * 最大图片高度(默认为1920)
     *
     * @param maxImageHeight 图片压缩过程中限制的最大高度
     */
    public void setMaxImageHeight(int maxImageHeight) {
        this.maxImageHeight = maxImageHeight;
    }

    /**
     * 最大图片宽度(默认为1080)
     *
     * @param maxImageWidth 图片压缩过程中限制的最大宽度
     */
    public void setMaxImageWidth(int maxImageWidth) {
        this.maxImageWidth = maxImageWidth;
    }

    /**
     * 最大文件大小(默认为1024KB)
     *
     * @param maxFileSize 图片压缩过程中限制的最大文件大小
     */
    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * 设置已选择图片
     *
     * @param selectedImages 除相册图片外,额外添加需要的图片
     */
    public void setSelectedImages(ArrayList<String> selectedImages) {
        this.selectedImages = selectedImages;
    }

    /**
     * 是否需要剪裁(只有单选时此属性才生效,默认为false)
     *
     * @param isTailoring true-选择后对图片进行裁剪;false-不作裁剪处理;
     */
    public void setTailoring(boolean isTailoring) {
        this.isTailoring = isTailoring;
    }

    /**
     * 最多选择数(最小可设为1)
     * 当值为1时自动为单选
     *
     * @param maxSelectNumber 最多选择数
     */
    public void setMaxSelectNumber(int maxSelectNumber) {
        if (maxSelectNumber < 1) {
            maxSelectNumber = 1;
        }
        this.maxSelectNumber = maxSelectNumber;
    }

    /**
     * 是否显示拍照图片(默认为显示)
     *
     * @param isShowTakingPictures true:显示;false:隐藏;
     */
    public void setShowTakingPictures(boolean isShowTakingPictures) {
        this.isShowTakingPictures = isShowTakingPictures;
    }

    /**
     * 设置裁剪宽高比
     *
     * @param x 裁剪宽度比例
     * @param y 裁剪高度比例
     */
    public void withAspect(int x, int y) {
        this.ASPECT_X = x;
        this.ASPECT_Y = y;
    }

    /**
     * 设置裁剪最大宽高
     *
     * @param width  裁剪框最大宽度
     * @param height 裁剪框最大高度
     */
    public void withMaxSize(int width, int height) {
        this.MAX_X = width;
        this.MAX_Y = height;
    }

    private MultiImageSelector initShow(Context context) {
        MultiImageSelector multiImageSelector = MultiImageSelector.create(context).showCamera(isShowTakingPictures);
        if (maxSelectNumber > 1) {
            multiImageSelector.multi();
            multiImageSelector.count(maxSelectNumber);
            if (!ObjectJudge.isNullOrEmpty(selectedImages)) {
                multiImageSelector.origin(selectedImages);
            }
        } else {
            multiImageSelector.single();
        }
        return multiImageSelector;
    }

    /**
     * 显示图片选择对话框
     *
     * @param activity 当前窗口
     */
    public void show(FragmentActivity activity) {
        this.activity = activity;
        imagePaths.clear();
        if (isTailoring && maxSelectNumber == 1) {
            MultiImageSelector multiImageSelector = initShow(activity);
            multiImageSelector.start(activity, TAILORING_REQUEST_CODE);
        } else {
            MultiImageSelector multiImageSelector = initShow(activity);
            multiImageSelector.start(activity, REQUEST_IMAGE);
        }
    }

    /**
     * 仅显示拍照对话框
     *
     * @param activity 当前窗口
     */
    public void showTaking(Activity activity) {
        this.activity = activity;
        startTaking(activity);
    }

    private File getTempImageFile() {
        RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
        File dir = builder.getImageCacheDir();
        String fileName = String.format("%s.jpg", GlobalUtils.getGuidNoConnect());
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private void startTaking(Activity activity) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            takingFile = getTempImageFile();
            if (takingFile == null) {
                ToastUtils.showLong(activity, R.string.mis_error_image_not_exist);
            } else {
                intent.putExtra("output", Uri.fromFile(takingFile));
                activity.startActivityForResult(intent, ONLY_TAKING_RESULT_CODE);
            }
        } else {
            ToastUtils.showLong(activity, R.string.mis_msg_no_camera);
        }
    }

    /**
     * 需在Activity的onActivityResult回调
     *
     * @param activity    activity
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_IMAGE) {
                requestImage(activity, resultCode, data);
            } else if (requestCode == TAILORING_REQUEST_CODE) {
                tailoringRequestCode(activity, data);
            } else if (requestCode == Crop.REQUEST_CROP) {
                requestCrop(data);
            } else if (requestCode == ONLY_TAKING_RESULT_CODE) {
                onlyTakingResultProcess(activity, resultCode);
            }
        } catch (Exception e) {
            mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
            Logger.error(e);
        }
    }

    private void onlyTakingResultProcess(final Activity activity, int resultCode) {
        if (resultCode == activity.RESULT_OK) {
            if (this.takingFile != null) {
                List<File> imgPaths = new ArrayList<File>();
                imgPaths.add(takingFile);
                Luban.compress(imgPaths)
                        .putGear(Luban.CUSTOM_GEAR)
                        .setMaxSize(maxFileSize)
                        .setMaxWidth(maxImageWidth)
                        .setMaxHeight(maxImageHeight)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .launch(new OnMultiCompressListener() {
                            @Override
                            public void onStart() {
                                imagePaths.clear();
                            }

                            @Override
                            public void onSuccess(List<File> list) {
                                try {
                                    if (ObjectJudge.isNullOrEmpty(list)) {
                                        mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
                                    } else {
                                        File file = list.get(0);
                                        //判断是否裁剪
                                        if (isTailoring && maxSelectNumber == 1) {
                                            RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
                                            File dir = builder.getImageCacheDir();
                                            File destination = new File(dir, String.format("%s.rxtiny", GlobalUtils.getNewGuid()));
                                            Crop crop = Crop.of(Uri.fromFile(file), Uri.fromFile(destination));
                                            setCropProperties(crop);
                                            crop.start(activity);
                                        } else {
                                            SelectImageProperties selectImageProperties = new SelectImageProperties();
                                            selectImageProperties.setImagePath(file.getAbsolutePath());
                                            selectImageProperties.setImageFileName(file.getName());
                                            imagePaths.add(selectImageProperties);
                                            mhandler.obtainMessage(PROCESS_COMPLETED_WITH, imagePaths).sendToTarget();
                                        }
                                    }
                                } catch (Exception e) {
                                    if (takingFile != null && takingFile.exists()) {
                                        takingFile.delete();
                                    }
                                    mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
                                    Logger.error(e);
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                if (takingFile != null && takingFile.exists()) {
                                    takingFile.delete();
                                }
                                mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
                            }
                        });
            }
        } else {
            if (takingFile != null && takingFile.exists()) {
                takingFile.delete();
            }
        }
    }

    private void requestImage(Activity activity, int resultCode, Intent data) {
        if (resultCode == activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            final List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (ObjectJudge.isNullOrEmpty(paths)) {
                mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
            } else {
                final List<File> imgPaths = new ArrayList<File>();
                for (String path : paths) {
                    imgPaths.add(new File(path));
                }
                Luban.compress(imgPaths)
                        .putGear(Luban.CUSTOM_GEAR)
                        .setMaxSize(maxFileSize)
                        .setMaxWidth(maxImageWidth)
                        .setMaxHeight(maxImageHeight)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .launch(new OnMultiCompressListener() {
                            @Override
                            public void onStart() {
                                imagePaths.clear();
                            }

                            @Override
                            public void onSuccess(List<File> list) {
                                try {
                                    if (ObjectJudge.isNullOrEmpty(list)) {
                                        mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
                                    } else {
                                        for (File file : list) {
                                            SelectImageProperties selectImageProperties = new SelectImageProperties();
                                            selectImageProperties.setImagePath(file.getAbsolutePath());
                                            selectImageProperties.setImageFileName(file.getName());
                                            imagePaths.add(selectImageProperties);
                                        }
                                        mhandler.obtainMessage(PROCESS_COMPLETED_WITH, imagePaths).sendToTarget();
                                    }
                                } catch (Exception e) {
                                    mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
                                    Logger.error(e);
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                for (File imgPath : imgPaths) {
                                    SelectImageProperties selectImageProperties = new SelectImageProperties();
                                    selectImageProperties.setImagePath(imgPath.getAbsolutePath());
                                    selectImageProperties.setImageFileName(imgPath.getName());
                                    imagePaths.add(selectImageProperties);
                                }
                                mhandler.obtainMessage(PROCESS_COMPLETED_WITH, imagePaths).sendToTarget();
                            }
                        });
            }
        }
    }

    @SuppressLint("CheckResult")
    private void tailoringRequestCode(final Activity activity, final Intent data) {
        if (data != null) {
            imagePaths.clear();
            File imgfile = getCropImageFile(data);
//                    String[] proj = {MediaStore.Images.Media.DATA};
//                    // 好像是android多媒体数据库的封装接口，具体的看Android文档
//                    Cursor cursor = context.getContentResolver().query(, proj, null, null, null);
//                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
//                    if (cursor!=null && cursor.moveToFirst()) {
//                        // 按我个人理解 这个是获得用户选择的图片的索引值
//                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        // 最后根据索引值获取图片路径
//                        String path = cursor.getString(column_index);
            if (imgfile != null && imgfile.exists()) {
                //压缩图片
                Luban.compress(imgfile)
                        .setMaxSize(maxFileSize)
                        .setMaxWidth(maxImageWidth)
                        .setMaxHeight(maxImageHeight)
                        .putGear(Luban.CUSTOM_GEAR)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .asObservable()
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) {
                                try {
                                    SelectImageProperties selectImageProperties = new SelectImageProperties();
                                    selectImageProperties.setImagePath(file.getAbsolutePath());
                                    selectImageProperties.setImageFileName(file.getName());
                                    imagePaths.add(selectImageProperties);
                                    RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
                                    File dir = builder.getImageCacheDir();
                                    File destination = new File(dir, String.format("%s.rxtiny", GlobalUtils.getNewGuid()));
                                    Crop crop = Crop.of(Uri.fromFile(file), Uri.fromFile(destination));
                                    setCropProperties(crop);
                                    crop.start(activity);
                                } catch (Exception e) {
                                    Logger.error(e);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
                            }
                        });
            }
        } else {
            mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
        }
    }

    private void setCropProperties(Crop crop) {
        if (ASPECT_X > 0 && ASPECT_Y > 0) {
            crop = crop.withAspect(ASPECT_X, ASPECT_Y);
        } else {
            crop = crop.asSquare();
        }
        if (MAX_X > 0 && MAX_Y > 0) {
            crop.withMaxSize(MAX_X, MAX_Y);
        }
    }

    private void requestCrop(Intent data) {
        if (data != null) {
            imagePaths.clear();
            File imgfile = getCropImageFile(data);
            SelectImageProperties selectImageProperties = new SelectImageProperties();
            selectImageProperties.setImagePath(imgfile.getAbsolutePath());
            selectImageProperties.setImageFileName(imgfile.getName());
            selectImageProperties.setThumImagePath(imgfile.getAbsolutePath());
            selectImageProperties.setThumImageFileName(getFileName(imgfile.getAbsolutePath()));
            imagePaths.add(selectImageProperties);
            mhandler.obtainMessage(PROCESS_COMPLETED_WITH, imagePaths).sendToTarget();
        } else {
            mhandler.obtainMessage(PROCESS_COMPLETED_WITH, null).sendToTarget();
        }
    }

    private File getCropImageFile(Intent data) {
        ArrayList<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        if (ObjectJudge.isNullOrEmpty(paths)) {
            Uri uri = Crop.getOutput(data);
            if (uri == null || uri == Uri.EMPTY) {
                return null;
            }
            return new File(uri.getPath());
        } else {
            String path = paths.get(0);
            if (TextUtils.isEmpty(path)) {
                return null;
            } else {
                return new File(path);
            }
        }
    }

    private String getFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int seindex = path.lastIndexOf("/");
        if (seindex >= 0) {
            return path.substring(seindex + 1);
        } else {
            return "";
        }
    }

    /**
     * 删除已选择图片
     */
    public void deleteSelecteImages() {
        try {
            if (!ObjectJudge.isNullOrEmpty(imagePaths)) {
                for (SelectImageProperties imagePath : imagePaths) {
                    if (!TextUtils.isEmpty(imagePath.getImagePath())) {
                        File imageFile = new File(imagePath.getImagePath());
                        if (imageFile.exists()) {
                            imageFile.delete();
                        }
                    }
                    if (!TextUtils.isEmpty(imagePath.getThumImagePath())) {
                        File thumImageFile = new File(imagePath.getThumImagePath());
                        if (thumImageFile.exists()) {
                            thumImageFile.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PROCESS_COMPLETED_WITH) {
                List<SelectImageProperties> selectImageProperties = (List<SelectImageProperties>) msg.obj;
                onSelectCompleted(selectImageProperties, extra);
                //初始化相关变量
                init();
            }
        }
    };
}