package com.cloud.images.figureset;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cloud.coms.dialogs.LoadingDialog;
import com.cloud.coms.dialogs.plugs.DialogPlus;
import com.cloud.coms.dialogs.progress.DonutProgress;
import com.cloud.debris.R;
import com.cloud.objects.ObjectJudge;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/15
 * @Description:图片添加、删除视图
 * @Modifier:
 * @ModifyContent:
 */
public class ImageEditorView extends PictureSelectEditorView implements OnPictureSelectChangedListener,
        OnPictureSelectReviewOriginalImageListener, OnPictureSelectDeleteListener, OnBindDefaultImagesListener {

    private String ossAssumeRoleUrl = "";
    //是否自动上传图片
    private boolean isAutoUploadImage = true;
    private TreeMap<Integer, SelectImageItem> selectImageItems = new TreeMap<Integer, SelectImageItem>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
    private HashMap<Integer, SelectImageItem> delImageItems = new HashMap<Integer, SelectImageItem>();
    private HashMap<Integer, SelectImageItem> uploadedImageItems = new HashMap<Integer, SelectImageItem>();
    private TreeMap<Integer, String> uploadedUrls = new TreeMap<Integer, String>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
    private HashMap<String, Float> progressList = new HashMap<String, Float>();
    private boolean isStartSubmit = false;
    private LoadingDialog mloading = new LoadingDialog();
    private final int START_SHOW_UPLOAD = 1308754678;
    private final int DISMISS_LOADING = 1311980761;
    private final int UPLOADING_WITH = 476990333;
    private final int UPLOAD_PROGRESS = 594159809;
    private DialogPlus dialogPlus = null;
    private final String UPLOAD_DIALOG_ID = "1820554346";
    private float totalProgress = 0;
    private OnUploadCompletedListener onUploadCompletedListener = null;
    private HashMap<String, Integer> countMap = new HashMap<String, Integer>();
    private OnReviewImageListener onReviewImageListener = null;
    private OnImageSelectedListener onImageSelectedListener = null;
    private OnImageDeletedListener onImageDeletedListener = null;
    private OnDrageredImageListener onDrageredImageListener = null;

    public ImageEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnPictureSelectChangedListener(this);
        super.setOnPictureSelectReviewOriginalImageListener(this);
        super.setOnPictureSelectDeleteListener(this);
        super.setOnBindDefaultImagesListener(this);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageEditorView);
        isAutoUploadImage = a.getBoolean(R.styleable.ImageEditorView_iev_isAutoUploadImage, true);
        a.recycle();
    }

    @Override
    public void setActivity(FragmentActivity activity) {
        super.setActivity(activity);
        selectImageItems.clear();
        delImageItems.clear();
    }

    /**
     * 设置上传结束回调监听
     *
     * @param listener 上传结束回调监听
     */
    public void setOnUploadCompletedListener(OnUploadCompletedListener listener) {
        this.onUploadCompletedListener = listener;
    }

    /**
     * 设置图片预览回调监听
     *
     * @param listener 图片预览回调监听
     */
    public void setOnReviewImageListener(OnReviewImageListener listener) {
        this.onReviewImageListener = listener;
    }

    /**
     * 设置oss上传目录路径
     *
     * @param ossAssumeRoleUrl oss上传目录路径(http://api.mibaostore.cn/v2_1_1/aliFileUpload/stsToken/headImg)
     */
    public void setOssAssumeRoleUrl(String ossAssumeRoleUrl) {
        this.ossAssumeRoleUrl = ossAssumeRoleUrl;
    }

    /**
     * 设置图片选择回调监听
     *
     * @param listener 图片选择回调监听
     */
    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.onImageSelectedListener = listener;
    }

    /**
     * 设置图片删除回调监听
     *
     * @param listener 图片删除回调监听
     */
    public void setOnImageDeletedListener(OnImageDeletedListener listener) {
        this.onImageDeletedListener = listener;
    }

    /**
     * 设置已拖拽图片监听
     *
     * @param listener 已拖拽图片监听
     */
    public void setOnDrageredImageListener(OnDrageredImageListener listener) {
        this.onDrageredImageListener = listener;
    }

    @Override
    public void onPictureSelectChanged(Uri imgUri, String fileName, int position) {
        if (imgUri == null) {
            return;
        }
        isStartSubmit = false;
        imageSelectWith(position, imgUri);
    }

    @Override
    public void onLinearDrager(View view, int position) {
        int oldImagePosition = getItemViewImagePosition(view, position);
        if (!selectImageItems.containsKey(oldImagePosition)) {
            return;
        }
        //更新图片列表数据,移除oldImagePosition并添加至position
        Object[] array = selectImageItems.keySet().toArray();
        if (array.length <= position) {
            //如果索引大于数据长度则返回
            return;
        }
        int selKey = (int) array[position];
        //如果selKey不包含在图片选择列表中则返回
        if (!selectImageItems.containsKey(selKey)) {
            return;
        }
        SelectImageItem target = selectImageItems.get(selKey);
        target.position = oldImagePosition;

        SelectImageItem selectImageItem = selectImageItems.remove(oldImagePosition);
        selectImageItem.position = position;

        TreeMap<Integer, SelectImageItem> clone = (TreeMap<Integer, SelectImageItem>) selectImageItems.clone();
        selectImageItems.clear();
        for (Map.Entry<Integer, SelectImageItem> entry : clone.entrySet()) {
            SelectImageItem value = entry.getValue();
            selectImageItems.put(value.position, value);
        }
        selectImageItems.put(selectImageItem.position, selectImageItem);
        if (onDrageredImageListener == null) {
            return;
        }
        //监听回调
        onDrageredImageListener.onDrageredImage(selectImageItem, oldImagePosition, position);
    }

    /**
     * 获取已选择图片集合（包括已上传或未上传图片）
     *
     * @return 已选择图片集合(包括已上传或未上传图片)
     */
    public List<SelectImageItem> getSelectImageItems() {
        List<SelectImageItem> list = new ArrayList<SelectImageItem>();
        //取已上传图片
        for (Map.Entry<Integer, SelectImageItem> entry : this.uploadedImageItems.entrySet()) {
            if (!list.contains(entry.getValue())) {
                list.add(entry.getValue());
            }
        }
        //取未上传图片
        for (Map.Entry<Integer, SelectImageItem> entry : this.selectImageItems.entrySet()) {
            if (!list.contains(entry.getValue()) && (!delImageItems.containsKey(entry.getKey()) || !uploadedImageItems.containsKey(entry.getKey()))) {
                list.add(entry.getValue());
            }
        }
        Collections.sort(list, new Comparator<SelectImageItem>() {
            @Override
            public int compare(SelectImageItem o1, SelectImageItem o2) {
                Integer prevPosition = o1.position;
                return prevPosition.compareTo(o2.position);
            }
        });
        return list;
    }

    private void fileUpload(Uri uri, int position, boolean showUploadProgress) {
//        UploadUtils uploadUtils = new UploadUtils() {
//            @Override
//            protected void onUploadProgress(float progress, String uploadKey) {
//                progressList.put(uploadKey, progress);
//                if (isAutoUploadImage) {
//                    Bundle bundle = new Bundle();
//                    bundle.putFloat("PROGRESS_KEY", progress);
//                    bundle.putString("UPLOAD_KEY", uploadKey);
//                    Message message = mhandler.obtainMessage(UPLOAD_PROGRESS);
//                    message.setData(bundle);
//                    mhandler.sendMessage(message);
//                }
//                if (isStartSubmit) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    float currProgress = 0;
//                    for (Map.Entry<String, Float> entry : progressList.entrySet()) {
//                        currProgress += entry.getValue();
//                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putFloat("PROGRESS_KEY", (currProgress * 100) / totalProgress);
//                    Message message = mhandler.obtainMessage(UPLOADING_WITH);
//                    message.setData(bundle);
//                    mhandler.sendMessage(message);
//                }
//                int position = ConvertUtils.toInt(uploadKey.split("_")[2]);
//                uploadedImageItems.put(position, selectImageItems.get(position));
//            }
//
//            @Override
//            protected void onUploadSuccess(final int position, String url, String uploadKey, Object extra) {
//                uploadedUrls.put(position, url);
//                if (isStartSubmit && onUploadCompletedListener != null) {
//                    countMap.put("UP_COUNT", countMap.get("UP_COUNT") + 1);
//                    if (countMap.get("UP_TOTAL") == countMap.get("UP_COUNT")) {
//                        onUploadCompletedListener.onUploadCompleted(uploadedUrls);
//                        isStartSubmit = false;
//                        selectImageItems.clear();
//                        delImageItems.clear();
//                        mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
//                    }
//                }
//                remoteUploaded(uploadKey);
//            }
//        };
        File imgFile = new File(String.format("file://%s", uri.getPath()));
        if (!imgFile.exists()) {
            return;
        }
//        uploadUtils.setCount(1);
//        uploadUtils.setShowUploadProgress(showUploadProgress);
//        uploadUtils.upload(
//                getActivity(),
//                fileName,
//                imgFile,
//                ossAssumeRoleUrl,
//                "MASK_IMG_" + position,
//                position);
    }

    private void remoteUploaded(String uploadKey) {
        int index = -1;
        for (Map.Entry<Integer, SelectImageItem> entry : selectImageItems.entrySet()) {
            SelectImageItem value = entry.getValue();
            if (TextUtils.equals(String.format("MASK_IMG_%s", value.position), uploadKey)) {
                index = entry.getKey();
                break;
            }
        }
        if (selectImageItems.containsKey(index)) {
            selectImageItems.remove(index);
        }
    }

    @Override
    public void OnPictureSelectReviewOriginalImage(Uri imguri, int position) {
        if (onReviewImageListener == null) {
            return;
        }
        try {
            List<String> images = new ArrayList<String>();
            if (isAutoUploadImage) {
//                ImagesBuilder builder = RxImages.getInstance().getImagesBuilder();
//                String baseUrl = builder.getImgBaseUrl();
//                for (Map.Entry<Integer, String> entry : uploadedUrls.entrySet()) {
//                    images.add(PathsUtils.combine(baseUrl, entry.getValue()));
//                }
                onReviewImageListener.onReview(images, position);
            } else {
                for (Map.Entry<Integer, SelectImageItem> entry : selectImageItems.entrySet()) {
                    SelectImageItem value = entry.getValue();
                    if (value.uri == null || value.uri == Uri.EMPTY) {
                        continue;
                    }
                    images.add(value.uri.getPath());
                }
                onReviewImageListener.onReview(images, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPictureSelectDelete(int position) {
        //当前删除操作时selectImageItems里的索引是对应的
        //之后可能不一样
        if (!selectImageItems.containsKey(position)) {
            return;
        }
        int delPosition = 0;
        Object[] array = selectImageItems.keySet().toArray();
        for (int i = 0; i < array.length; i++) {
            int key = (int) array[i];
            if (key == position) {
                delPosition = i;
                break;
            }
        }
        delImageItems.put(position, selectImageItems.remove(position));
        if (uploadedUrls.containsKey(position)) {
            uploadedUrls.remove(position);
        }
        if (onImageDeletedListener != null) {
            onImageDeletedListener.onImageDeleted(delPosition);
        }
    }

    /**
     * 获取已上传的图片集合
     *
     * @return 已上传图片集合
     */
    public TreeMap<Integer, String> getUploadedUrls() {
        return uploadedUrls;
    }

    /**
     * 检测是否已上传完成
     *
     * @return
     */
    private HashMap<Integer, SelectImageItem> isUploadedComplete() {
        HashMap<Integer, SelectImageItem> map = new HashMap<Integer, SelectImageItem>();
        for (Map.Entry<Integer, SelectImageItem> entry : this.selectImageItems.entrySet()) {
            if (!delImageItems.containsKey(entry.getKey()) || !uploadedImageItems.containsKey(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    /**
     * 检测并上传
     */
    public void checkAndUploads() {
        try {
            this.isStartSubmit = true;
            HashMap<Integer, SelectImageItem> map = isUploadedComplete();
            if (ObjectJudge.isNullOrEmpty(map)) {
                if (onUploadCompletedListener != null) {
                    onUploadCompletedListener.onUploadCompleted(uploadedUrls);
                }
                this.isStartSubmit = false;
                selectImageItems.clear();
                delImageItems.clear();
                mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
            } else {
                mhandler.obtainMessage(START_SHOW_UPLOAD, "图片上传中").sendToTarget();
                countMap.put("UP_TOTAL", map.size() + uploadedUrls.size());
                countMap.put("UP_COUNT", 0);
                progressList.clear();
                this.totalProgress = map.size() * 100;
                if (!isAutoUploadImage) {
                    int position = 0;
                    for (Map.Entry<Integer, SelectImageItem> entry : map.entrySet()) {
                        SelectImageItem value = entry.getValue();
                        fileUpload(value.uri, position, false);
                        position++;
                    }
                }
            }
        } catch (Exception e) {
            this.isStartSubmit = false;
            selectImageItems.clear();
            delImageItems.clear();
            mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
        }
    }

    private Handler mhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_SHOW_UPLOAD) {
                if (dialogPlus == null) {
                    dialogPlus = mloading.buildDialog(getActivity(), UPLOAD_DIALOG_ID, String.valueOf(msg.obj));
                }
                mloading.setRotate(dialogPlus, false);
                mloading.setCurrentProgress(dialogPlus, 0);
                mloading.setMaxProgress(dialogPlus, 100);
                mloading.setContent(dialogPlus, String.valueOf(msg.obj));
                dialogPlus.show();
            } else if (msg.what == UPLOADING_WITH) {
                if (dialogPlus != null) {
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        float progress = bundle.getFloat("PROGRESS_KEY", 0);
                        if (progress > 0) {
                            if (dialogPlus.getHolderView() != null) {
                                DonutProgress dpProgress = (DonutProgress) dialogPlus.getHolderView().findViewById(R.id.dp_progress);
                                if (dpProgress != null) {
                                    dpProgress.setProgress(progress);
                                }
                            }
                        }
                    }
                }
            } else if (msg.what == DISMISS_LOADING) {
                if (dialogPlus != null && dialogPlus.isShowing()) {
                    mloading.dismiss(dialogPlus);
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogPlus = null;
                        }
                    }, 500);
                }
            } else if (msg.what == UPLOAD_PROGRESS) {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    float progress = bundle.getFloat("PROGRESS_KEY", 0);
                    String uploadKey = bundle.getString("UPLOAD_KEY", "");
                    updateProgress(progress, uploadKey);
                }
            }
        }
    };

    @Override
    public void onBindDefaultImages(int position, Uri uri) {
        isStartSubmit = false;
        imageSelectWith(position, uri);
    }

    private void imageSelectWith(int position, Uri uri) {
        //添加已选择的图片
        SelectImageItem selectImageItem = new SelectImageItem();
        selectImageItem.uri = uri;
        selectImageItem.position = position;
        selectImageItems.put(position, selectImageItem);
        if (onImageSelectedListener != null) {
            List<SelectImageItem> imageItems = getSelectImageItems();
            onImageSelectedListener.onImageSelected(selectImageItem, imageItems);
        }
        //如果自动上传再上传图片，上传成功后将地址添加到已完成列表
        if (isAutoUploadImage) {
            fileUpload(uri, position, false);
        }
    }
}
