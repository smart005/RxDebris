package com.cloud.debris.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.transition.Transition;
import com.cloud.debris.BaseFragment;
import com.cloud.debris.R;
import com.cloud.debris.bundle.RedirectUtils;
import com.cloud.images.glide.GFileCallback;
import com.cloud.images.glide.GlideOptimize;
import com.cloud.images.view.viewer.OnOutsidePhotoTapListener;
import com.cloud.images.view.viewer.OnPhotoTapListener;
import com.cloud.images.view.viewer.PhotoView;
import com.cloud.images.view.viewer.PhotoViewAttacher;
import com.cloud.objects.events.Action2;
import com.cloud.objects.utils.JsonUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/14
 * @Description:图片预览视图
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BasePreviewImageFragment extends BaseFragment {

    private ViewPager imgViewerVp;
    private TextView imageCountTv;
    private List<String> imgUrls = new LinkedList<String>();
    private PicasaPagerAdapter currAdapter = null;
    //是否显示图片计数
    private boolean isShowCount;
    //当前索引
    private int currPosition;
    //保存图片目录名
    private String saveImageDirName = "images";

    /**
     * 修饰视图
     *
     * @param container container
     */
    protected abstract void onModifiedView(FrameLayout container);

    /**
     * 已初始化(相关初始值在这里设置)
     */
    protected abstract void onInitialized();

    /**
     * 已保存文件
     *
     * @param file saved file
     */
    protected abstract void onSavedFile(File file);

    /**
     * 是否显示图片计数(默认显示)
     *
     * @param showCount 显示计数
     */
    public void setShowCount(boolean showCount) {
        isShowCount = showCount;
    }

    /**
     * 设置保存图片目录名(默认为images)
     *
     * @param saveImageDirName 保存图片目录名(默认为images)
     */
    public void setSaveImageDirName(String saveImageDirName) {
        this.saveImageDirName = saveImageDirName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preview_image_content_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onModifiedView((FrameLayout) view);
        imgViewerVp = (ViewPager) view.findViewById(R.id.img_viewer_vp);
        imageCountTv = (TextView) view.findViewById(R.id.image_count_tv);
        currAdapter = new PicasaPagerAdapter();
        imgViewerVp.setAdapter(currAdapter);
        ImagePageChange imagePageChange = new ImagePageChange();
        imgViewerVp.addOnPageChangeListener(imagePageChange);
        int position = getIntBundle("position");
        String imgUrlsJson = getStringBundle("urls", "[]");
        imgUrls = JsonUtils.parseArray(imgUrlsJson, String.class);
        imgViewerVp.setCurrentItem(position);
        onInitialized();
        //显示当前计数
        imagePageChange.onPageSelected(position);
        imageCountTv.setVisibility(isShowCount ? View.VISIBLE : View.GONE);
        currAdapter.notifyDataSetChanged();
    }

    private class ImagePageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currPosition = position;
            if (isShowCount) {
                imageCountTv.setText(String.format("%s / %s", (position + 1), imgUrls.size()));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class PicasaPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imgUrls == null ? 0 : imgUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            String url = imgUrls.get(position);
            final PhotoView photoView = new PhotoView(container.getContext());
            photoView.setAllowParentInterceptOnEdge(true);
            final PhotoViewAttacher attacher = photoView.getAttacher();
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    RedirectUtils.finishActivity(getActivity());
                }
            });
            photoView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
                @Override
                public void onOutsidePhotoTap(ImageView imageView) {
                    RedirectUtils.finishActivity(getActivity());
                }
            });
            GlideOptimize.with(getContext()).load(url).into(photoView, new Action2<Drawable, Transition<? super Drawable>>() {
                @Override
                public void call(Drawable drawable, Transition<? super Drawable> transition) {
                    attacher.update();
                }
            });
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 保存当前预览图片
     */
    public void saveCurrentPreviewImage() {
        if (currPosition < 0 || imgUrls == null || currPosition >= imgUrls.size()) {
            return;
        }
        String url = imgUrls.get(currPosition);
        GlideOptimize.with(this)
                .load(url)
                .toMove(saveImageDirName)
                .into(new GFileCallback<File>() {
                    @Override
                    public void call(File file) {
                        onSavedFile(file);
                    }
                });
    }
}
