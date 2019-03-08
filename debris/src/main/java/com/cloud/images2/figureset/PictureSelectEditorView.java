package com.cloud.images2.figureset;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.debris.R;
import com.cloud.images2.beans.SelectImageProperties;
import com.cloud.images2.linear.LinearDrager;
import com.cloud.images2.linear.OnLinearDragerListener;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.PixelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/15
 * @Description:图片选择编辑器
 */
abstract class PictureSelectEditorView extends LinearLayout implements OnLinearDragerListener {

    private int IMAGE_ITEM_IV = 1424857564;
    private int eachRowNumber = 4;
    private int containerWidth = 0;
    private int imgSize = 0;
    private int maxImageCount = 1;
    private boolean isAlignMiddle = false;
    private boolean isAllowModify = false;
    private boolean isModify = false;
    private boolean isOnlyRead = false;
    private boolean isAllowDel = false;
    private boolean isAllowDrager = false;
    private int delImage = 0;
    private int delImageMarginTop = 0;
    private int delImageMarginLeft = 0;
    private int modifyPosition = 0;
    private int SEL_IMG_INDEX_TAG = 771916386;
    private int STATUS_TEXT_ID = 1684646507;
    private int DEL_ID = 1861546519;
    private int DEL_ADD_FLAG_KEY = 1667332261;
    private int addBackgoundResource = 0;
    private boolean isAddImage = true;
    private TreeMap<Integer, Uri> imgUrls = new TreeMap<Integer, Uri>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
    private HashMap<String, Integer> maskViewIds = new HashMap<String, Integer>();
    private Activity activity = null;
    private OnPictureSelectChangedListener onPictureSelectChangedListener = null;
    private OnPictureSelectReviewOriginalImageListener onPictureSelectReviewOriginalImageListener = null;
    private OnPictureSelectDeleteListener onPictureSelectDeleteListener = null;
    private int CAMERA_PERMISSION = 5678;
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
    /**
     * 是否需要剪裁(只有单选时此属性才生效,默认为false)
     */
    private boolean isTailoring = false;
    /**
     * 是否显示拍照图片(默认为显示)
     */
    private boolean isShowTakingPictures = true;
    private int ASPECT_X = 0;
    private int ASPECT_Y = 0;
    private int MAX_X = 0;
    private int MAX_Y = 0;
    //拖拽对象
    private HashMap<Object, LinearDrager<LinearLayout>> dragerHashMap = new HashMap<Object, LinearDrager<LinearLayout>>();
    private int dragerViewTag = 119873247;
    private int dragerPrevViewPositionTag = 1430216983;
    private OnBindDefaultImagesListener onBindDefaultImagesListener = null;

    public PictureSelectEditorView(Context context) {
        super(context);
        init(true, null);
    }

    public PictureSelectEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false, attrs);
    }

    public PictureSelectEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false, attrs);
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
     * 是否需要剪裁(只有单选时此属性才生效,默认为false)
     *
     * @param isTailoring true-选择后对图片进行裁剪;false-不作裁剪处理;
     */
    public void setTailoring(boolean isTailoring) {
        this.isTailoring = isTailoring;
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

    /**
     * 设置Activity
     *
     * @param activity activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * 获取当前activity
     *
     * @return activity
     */
    public Activity getActivity() {
        return this.activity;
    }

    protected void setOnPictureSelectChangedListener(OnPictureSelectChangedListener listener) {
        this.onPictureSelectChangedListener = listener;
    }

    protected void setOnPictureSelectReviewOriginalImageListener(OnPictureSelectReviewOriginalImageListener listener) {
        this.onPictureSelectReviewOriginalImageListener = listener;
    }

    protected void setOnPictureSelectDeleteListener(OnPictureSelectDeleteListener listener) {
        this.onPictureSelectDeleteListener = listener;
    }

    protected void setOnBindDefaultImagesListener(OnBindDefaultImagesListener listener) {
        this.onBindDefaultImagesListener = listener;
    }

    private void init(boolean flag, AttributeSet attrs) {
        if (flag) {
            ViewGroup.LayoutParams vgparam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(vgparam);
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PictureSelectEditorView);
            isAlignMiddle = a.getBoolean(R.styleable.PictureSelectEditorView_iev_isAlignMiddle, false);
            maxImageCount = a.getInt(R.styleable.PictureSelectEditorView_iev_maxImageCount, 1);
            eachRowNumber = a.getInt(R.styleable.PictureSelectEditorView_iev_eachRowNumber, 4);
            isAllowModify = a.getBoolean(R.styleable.PictureSelectEditorView_iev_isAllowModify, false);
            isAllowDel = a.getBoolean(R.styleable.PictureSelectEditorView_iev_isAllowDel, true);
            isAllowDrager = a.getBoolean(R.styleable.PictureSelectEditorView_iev_isAllowDrager, false);
            addBackgoundResource = a.getResourceId(R.styleable.PictureSelectEditorView_iev_addBackgoundResource, R.drawable.cl_add_image_bg);
            isAddImage = a.getBoolean(R.styleable.PictureSelectEditorView_iev_isAddImage, true);
            delImage = a.getResourceId(R.styleable.PictureSelectEditorView_iev_delImage, 0);
            delImageMarginLeft = (int) a.getDimension(R.styleable.PictureSelectEditorView_iev_delImageMarginLeft, 0);
            delImageMarginTop = (int) a.getDimension(R.styleable.PictureSelectEditorView_iev_delImageMarginTop, 0);
            a.recycle();
        }
        this.setOrientation(VERTICAL);
        int[] androidAttrs = new int[]{
                android.R.attr.paddingLeft,
                android.R.attr.paddingRight,
                android.R.attr.layout_marginLeft,
                android.R.attr.layout_marginRight
        };
        int screenWidth = GlobalUtils.getScreenWidth(getContext());
        TypedArray a = getContext().obtainStyledAttributes(attrs, androidAttrs);
        containerWidth = screenWidth - (int) a.getDimension(0, 0);
        containerWidth -= (int) a.getDimension(1, 0);
        containerWidth -= (int) a.getDimension(2, 0);
        containerWidth -= (int) a.getDimension(3, 0);
        a.recycle();
        int splitSize = PixelUtils.dip2px(getContext(), 8);
        imgSize = (containerWidth - splitSize * (eachRowNumber + 1) - eachRowNumber * PixelUtils.dip2px(getContext(), 7)) / eachRowNumber;
        RowObjectItem objectItem = buildRow();
        if (isAllowDrager) {
            dragerHashMap.put(objectItem.row.getTag(dragerViewTag), objectItem.drager);
        }
        RelativeLayout item = buildItem(imgSize, true, null, -1, objectItem.drager);
        objectItem.row.addView(item);
        addView(objectItem.row);
    }

    /**
     * 设置视图只能预览图片
     *
     * @param isOnlyRead true-只能预览；false-可编辑;
     */
    public void setOnlyRead(boolean isOnlyRead) {
        this.isOnlyRead = isOnlyRead;
    }

    /**
     * 设置是否有添加图片功能
     *
     * @param isAddImage true-当选择图片数小于总图片数时有添加图片功能,反之为false;
     */
    public void setAddImage(boolean isAddImage) {
        this.isAddImage = isAddImage;
    }

    private class RowObjectItem {
        public LinearLayout row = null;
        public LinearDrager<LinearLayout> drager = null;
    }

    private RowObjectItem buildRow() {
        RowObjectItem objectItem = new RowObjectItem();

        LayoutParams rowparam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout row = new LinearLayout(getContext());
        row.setLayoutParams(rowparam);
        row.setOrientation(HORIZONTAL);
        if (isAlignMiddle) {
            row.setGravity(Gravity.CENTER);
        }
        row.setTag(dragerViewTag, "8dd326ab5ffb40eaaf318e7f834bc6ac");
        objectItem.row = row;

        if (isAllowDrager) {
            objectItem.drager = new LinearDrager<LinearLayout>();
            objectItem.drager.builder(getContext(), row);
            objectItem.drager.setOnLinearDragerListener(this);
        }
        return objectItem;
    }

    protected int getItemViewImagePosition(View view, int position) {
        if (view == null || !(view instanceof RelativeLayout)) {
            return -1;
        }
        RelativeLayout layout = (RelativeLayout) view;
        View imageview = layout.findViewById(IMAGE_ITEM_IV);
        if (imageview == null) {
            return -1;
        }
        Object index = imageview.getTag(SEL_IMG_INDEX_TAG);
        if (index == null) {
            return -1;
        }
        imageview.setTag(SEL_IMG_INDEX_TAG, position);
        View del = layout.findViewById(DEL_ID);
        if (del != null) {
            del.setTag(position);
        }

        int sposition = ConvertUtils.toInt(index);
        if (imgUrls.containsKey(sposition)) {
            Uri remove = imgUrls.remove(sposition);
            List<Uri> lst = new ArrayList<>(imgUrls.values());
            imgUrls.clear();
            lst.add(position, remove);
            for (int i = 0; i < lst.size(); i++) {
                imgUrls.put(i, lst.get(i));
            }
        }
        return sposition;
    }

    @SuppressLint("ClickableViewAccessibility")
    private RelativeLayout buildItem(int imgSize, boolean isAdd, Uri uri, int imgIndex, final LinearDrager<LinearLayout> drager) {
        LayoutParams rlparam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlparam.setMargins(PixelUtils.dip2px(getContext(), 8), 0, 0, 0);
        if (isAlignMiddle) {
            rlparam.gravity = Gravity.CENTER_HORIZONTAL;
        }
        final RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(rlparam);
        //图片
        RelativeLayout.LayoutParams ivparam = new RelativeLayout.LayoutParams(imgSize, imgSize);
        ivparam.addRule(RelativeLayout.CENTER_VERTICAL);
        ivparam.setMargins(0, PixelUtils.dip2px(getContext(), 12), 0, PixelUtils.dip2px(getContext(), 12));
        final ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(ivparam);
        imageView.setId(IMAGE_ITEM_IV);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (isAdd) {
            imageView.setImageResource(addBackgoundResource != 0 ? addBackgoundResource : R.drawable.cl_add_image_bg);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (activity != null) {
                        //添加图片
                        imageSelectDialog.setMaxFileSize(maxFileSize);
                        imageSelectDialog.setMaxImageHeight(maxImageHeight);
                        imageSelectDialog.setMaxImageWidth(maxImageWidth);
                        imageSelectDialog.setMaxSelectNumber(1);
                        imageSelectDialog.setTailoring(isTailoring);
                        imageSelectDialog.withAspect(ASPECT_X, ASPECT_Y);
                        imageSelectDialog.withMaxSize(MAX_X, MAX_Y);
                        imageSelectDialog.setShowTakingPictures(isShowTakingPictures);
                        imageSelectDialog.setExtra(imgUrls.size() > 0 ? (imgUrls.lastKey() + 1) : 0);
                        imageSelectDialog.show(activity);
                    }
                }
            });
        } else {
            imageView.setTag(SEL_IMG_INDEX_TAG, imgIndex);
            imageView.setBackgroundColor(Color.WHITE);
//            GlideProcess.load(getContext(), uri, new DrawableImageViewTarget(imageView) {
//                @Override
//                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                    super.onResourceReady(resource, transition);
//                    imageView.invalidate();
//                }
//            });
            if (isAllowDrager) {
                imageView.setOnTouchListener(new ImageTouchListener(relativeLayout, drager));
            }
            if (!isOnlyRead) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (isAllowModify) {
                            if (activity != null) {
                                isModify = true;
                                modifyPosition = ConvertUtils.toInt(v.getTag(SEL_IMG_INDEX_TAG));
                                //编辑图片
                                imageSelectDialog.setMaxFileSize(maxFileSize);
                                imageSelectDialog.setMaxImageHeight(maxImageHeight);
                                imageSelectDialog.setMaxImageWidth(maxImageWidth);
                                imageSelectDialog.setMaxSelectNumber(1);
                                imageSelectDialog.setTailoring(isTailoring);
                                imageSelectDialog.withAspect(ASPECT_X, ASPECT_Y);
                                imageSelectDialog.withMaxSize(MAX_X, MAX_Y);
                                imageSelectDialog.setShowTakingPictures(isShowTakingPictures);
                                imageSelectDialog.setExtra(modifyPosition);
                                imageSelectDialog.show(activity);
                            }
                        } else {
                            if (onPictureSelectReviewOriginalImageListener != null) {
                                int index = ConvertUtils.toInt(v.getTag(SEL_IMG_INDEX_TAG), 0);
                                Uri imguri = imgUrls.get(index);
                                onPictureSelectReviewOriginalImageListener.OnPictureSelectReviewOriginalImage(imguri, index);
                            }
                        }
                    }
                });
            }
        }
        imageView.setVisibility(!isAddImage && isAdd ? GONE : VISIBLE);
        relativeLayout.addView(imageView);
        if (!isAdd) {
            relativeLayout.addView(getProgressView(imgIndex));
        }
        //删除图标
        int delimgsize = PixelUtils.dip2px(getContext(), 16);
        RelativeLayout.LayoutParams delparam = new RelativeLayout.LayoutParams(delimgsize, delimgsize);
        delparam.addRule(RelativeLayout.ALIGN_TOP, IMAGE_ITEM_IV);
        delparam.addRule(RelativeLayout.RIGHT_OF, IMAGE_ITEM_IV);
        int left = PixelUtils.dip2px(getContext(), -10);
        int top = PixelUtils.dip2px(getContext(), -8);
        delparam.setMargins(left + delImageMarginLeft, top + delImageMarginTop, 0, 0);
        ImageView del = new ImageView(getContext());
        del.setId(DEL_ID);
        del.setLayoutParams(delparam);
        if (delImage == 0) {
            del.setImageResource(R.drawable.cl_delete_icon);
        } else {
            del.setImageResource(delImage);
        }
        del.setVisibility((isAdd || isOnlyRead || !isAllowDel) ? View.GONE : View.VISIBLE);
        del.setTag(imgIndex);
        del.setTag(DEL_ADD_FLAG_KEY, isAdd ? 1 : 0);
        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delImageView(v);
            }
        });
        relativeLayout.addView(del);
        return relativeLayout;
    }

    private class ImageTouchListener implements OnTouchListener {

        private RelativeLayout itemView = null;
        private LinearDrager<LinearLayout> drager = null;

        public ImageTouchListener(RelativeLayout itemView, LinearDrager<LinearLayout> drager) {
            this.itemView = itemView;
            this.drager = drager;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return drager != null && drager.onTouch(itemView, event);
        }
    }

    private RelativeLayout getProgressView(int imgIndex) {
        RelativeLayout.LayoutParams maskparam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        maskparam.addRule(RelativeLayout.ALIGN_LEFT, IMAGE_ITEM_IV);
        maskparam.addRule(RelativeLayout.ALIGN_RIGHT, IMAGE_ITEM_IV);
        maskparam.addRule(RelativeLayout.ALIGN_TOP, IMAGE_ITEM_IV);
        maskparam.addRule(RelativeLayout.ALIGN_BOTTOM, IMAGE_ITEM_IV);
        RelativeLayout mask = new RelativeLayout(getContext());
        mask.setLayoutParams(maskparam);
        mask.setBackgroundColor(Color.parseColor("#40000000"));
        mask.setId(GlobalUtils.getHashCodeByUUID());

        RelativeLayout.LayoutParams statetvparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        statetvparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView statutv = new TextView(getContext());
        statutv.setLayoutParams(statetvparam);
        statutv.setTextColor(Color.parseColor("#ffffff"));
        statutv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        statutv.setId(STATUS_TEXT_ID);
        statutv.setText("0%");
        statutv.setSingleLine(true);
        mask.addView(statutv);

        mask.setVisibility(View.GONE);
        maskViewIds.put("MASK_IMG_" + imgIndex, mask.getId());
        return mask;
    }

    private void appendImage(Uri imgUri, int imgIndex) {
        try {
            if (imgUri == null || containerWidth <= 0 || imgUrls.size() > maxImageCount) {
                return;
            }
            imgUrls.put(imgIndex, imgUri);
            LinearLayout row = (LinearLayout) this.getChildAt(this.getChildCount() - 1);
            if (row == null) {
                return;
            }

            LinearDrager<LinearLayout> drager = null;
            if (isAllowDrager && dragerHashMap.containsKey(row.getTag(dragerViewTag))) {
                drager = dragerHashMap.get(row.getTag(dragerViewTag));
            }

            if (row.getChildCount() == eachRowNumber) {
                row.removeViewAt(eachRowNumber - 1);

                RelativeLayout item = buildItem(imgSize, false, imgUri, imgIndex, drager);
                if (isAllowDrager && drager != null) {
                    drager.setDragViews(item);
                }
                row.addView(item);
                if (imgUrls.size() < maxImageCount) {
                    RowObjectItem objectItem = buildRow();
                    RelativeLayout buildItem = buildItem(imgSize, true, null, imgIndex, objectItem.drager);
                    row = objectItem.row;
                    if (isAllowDrager) {
                        dragerHashMap.put(row.getTag(dragerViewTag), objectItem.drager);
                        objectItem.drager.setDragViews(buildItem);
                    }
                    row.addView(buildItem);
                    this.addView(row);
                }
                return;
            }
            RelativeLayout item = buildItem(imgSize, false, imgUri, imgIndex, drager);
            if (isAllowDrager && drager != null) {
                drager.setDragViews(item);
            }
            row.addView(item, row.getChildCount() - 1);
            if (imgUrls.size() == maxImageCount) {
                row.removeViewAt(row.getChildCount() - 1);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private ImageSelectDialog imageSelectDialog = new ImageSelectDialog() {
        @Override
        protected void onSelectCompleted(List<SelectImageProperties> selectImageProperties, Object extra) {
            if (ObjectJudge.isNullOrEmpty(selectImageProperties)) {
                return;
            }
            SelectImageProperties seimgitem = selectImageProperties.get(0);
            File selimgfile = new File(seimgitem.getImagePath());
            //如果文件则返回
            if (!selimgfile.exists()) {
                return;
            }
            Uri imguri = Uri.fromFile(selimgfile);
            if (isModify) {
                //如果包含key则移除
                if (imgUrls.containsKey(modifyPosition)) {
                    imgUrls.remove(modifyPosition);
                }
                imgUrls.put(modifyPosition, imguri);
                removeAllViews();
                RowObjectItem objectItem = buildRow();
                if (isAllowDrager) {
                    dragerHashMap.put(objectItem.row.getTag(dragerViewTag), objectItem.drager);
                }
                RelativeLayout item = buildItem(imgSize, true, null, -1, objectItem.drager);
                objectItem.row.addView(item);
                addView(objectItem.row);

                for (Map.Entry<Integer, Uri> integerUriEntry : imgUrls.entrySet()) {
                    appendImage(integerUriEntry.getValue(), integerUriEntry.getKey());
                }
            } else {
                appendImage(imguri, imgUrls.size() > 0 ? (imgUrls.lastKey() + 1) : 0);
            }
            if (onPictureSelectChangedListener != null) {
                int imagePosition = ConvertUtils.toInt(extra);
                onPictureSelectChangedListener.onPictureSelectChanged(imguri, seimgitem.getImageFileName(), imagePosition);
            }
            isModify = false;
        }
    };

    /**
     * 需要在Activity中回调来处理图片选择的结果
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelectDialog.onActivityResult(activity, requestCode, resultCode, data);
    }

    private void delImageView(View delView) {
        try {
            View parent = (View) delView.getParent();
            if (parent == null) {
                return;
            }
            ViewGroup container = (ViewGroup) parent.getParent();
            if (container == null) {
                return;
            }
            int index = container.indexOfChild(parent);
            if (index < 0) {
                return;
            }
            int position = ConvertUtils.toInt(delView.getTag());
            //取要删除key
            int skey = 0;
            Object[] array = imgUrls.keySet().toArray();
            for (int i = 0; i < array.length; i++) {
                if (i == index) {
                    skey = (int) array[i];
                    break;
                }
            }
            this.removeAllViews();
            if (imgUrls.containsKey(skey)) {
                imgUrls.remove(skey);
            }
            RowObjectItem objectItem = buildRow();
            if (isAllowDrager) {
                dragerHashMap.put(objectItem.row.getTag(dragerViewTag), objectItem.drager);
            }
            RelativeLayout item = buildItem(imgSize, true, null, -1, objectItem.drager);
            objectItem.row.addView(item);
            addView(objectItem.row);

            for (Map.Entry<Integer, Uri> integerUriEntry : imgUrls.entrySet()) {
                appendImage(integerUriEntry.getValue(), integerUriEntry.getKey());
            }
            if (onPictureSelectDeleteListener != null) {
                onPictureSelectDeleteListener.onPictureSelectDelete(position);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 绑定图片列表
     *
     * @param imgs 需要显示的图片列表
     */
    public void bindImages(List<String> imgs) {
        try {
            if (ObjectJudge.isNullOrEmpty(imgs)) {
                return;
            }
            if (imgs.size() > maxImageCount) {
                for (int i = maxImageCount; i < imgs.size(); i++) {
                    imgs.remove(i);
                }
            }
            for (int i = 0; i < imgs.size(); i++) {
                String img = imgs.get(i);
                Uri uri = Uri.parse(img);
                if (onBindDefaultImagesListener != null) {
                    //将图片添加到已选择列表
                    String path = img.startsWith("file://") ? img.substring("file://".length()) : img;
                    File file = new File(path);
                    String fileName = file.getName();
                    onBindDefaultImagesListener.onBindDefaultImages(i, fileName, path);
                }
                //附加和显示图片
                appendImage(uri, i);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 设置是否允许删除
     *
     * @param isAllowDel true-请允许删除；false-不可删除；
     */
    public void setAllowDel(boolean isAllowDel) {
        try {
            this.isAllowDel = isAllowDel;
            int rowCount = getChildCount();
            for (int i = 0; i < rowCount; i++) {
                ViewGroup row = (ViewGroup) getChildAt(i);
                if (row != null) {
                    int childcount = row.getChildCount();
                    for (int i1 = 0; i1 < childcount; i1++) {
                        ViewGroup childitem = (ViewGroup) row.getChildAt(i1);
                        if (childitem != null) {
                            View delview = childitem.findViewById(DEL_ID);
                            if (delview != null) {
                                int addflag = ConvertUtils.toInt(delview.getTag(DEL_ADD_FLAG_KEY));
                                if (addflag == 1) {
                                    delview.setVisibility(View.GONE);
                                } else {
                                    delview.setVisibility(isAllowDel ? View.VISIBLE : View.GONE);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 图片上传进度
     *
     * @param progress  上传进度
     * @param uploadKey 本次上传对应的key
     */
    protected void updateProgress(float progress, String uploadKey) {
        try {
            if (progress < 0 || TextUtils.isEmpty(uploadKey) || !maskViewIds.containsKey(uploadKey)) {
                return;
            }
            Integer maskId = maskViewIds.get(uploadKey);
            View view = this.findViewById(maskId);
            if (view == null) {
                return;
            }
            if (progress >= 100) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
            TextView progressTv = (TextView) view.findViewById(STATUS_TEXT_ID);
            if (progressTv == null) {
                view.setVisibility(View.GONE);
                return;
            }
            progressTv.setText((int) progress + "%");
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
