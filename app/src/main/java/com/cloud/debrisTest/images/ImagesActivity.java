package com.cloud.debrisTest.images;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Priority;
import com.cloud.debris.BaseFragmentActivity;
import com.cloud.debrisTest.R;
import com.cloud.debrisTest.databinding.ImagesViewBinding;
import com.cloud.images.RxImage;
import com.cloud.images.beans.SelectImageProperties;
import com.cloud.images.figureset.ImageSelectDialog;
import com.cloud.images.glide.GlideOptimize;
import com.cloud.images.glide.ScaleType;
import com.cloud.objects.events.Action1;
import com.cloud.objects.utils.GlobalUtils;

import java.io.File;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:图片相关功能
 * Modifier:
 * ModifyContent:
 */
public class ImagesActivity extends BaseFragmentActivity {

    private ImagesViewBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.images_view);
        binding.setHandler(this);

        glideLoadImage();
    }

    private void glideLoadImage() {
        String url = "http://img2.imgtn.bdimg.com/it/u=3685170612,1820641236&fm=26&gp=0.jpg";
        String url2 = "http://pic206.nipic.com/pic/20190219/22116547_213112169000_4.jpg";
        String url3 = "http://img1.imgtn.bdimg.com/it/u=873265023,1618187578&fm=26&gp=0.jpg";
        String gifurl = "http://img.zcool.cn/community/0128ee5bc73ab5a8012099c8745c1a.gif";
        File dir = RxImage.getInstance().getBuilder().getImageCacheDir();
        File file = new File(dir, "test_image.jpg");
        GlideOptimize.with(this)
                .load(url)
                //可自定义宽度和高度，不设置默认取控件的宽高;
//                .setWidth(100)
//                .setHeight(200)
                //glide图片加载完成之前的占位图片
                //未设置时取RxImage.setDefImage()设置的默认图片
                .setPlaceholder(R.drawable.def_image)
                //图片加载过程中优化级(一般用于比较重要位置或优化要显示的图片)
                .setPriority(Priority.HIGH)
                //true-将图片转为正圆再渲染;false-不作转换处理;
                //对于网络图片若尺寸或文件大小太大会导致不能转成圆形
                .setRound(true)
                //先加载相对于原图的缩放比例(按宽高比)的缩略图
                .setThumbnailScale(0.5f)
                //具体说明看{@link ScaleType}
                .setScaleType(ScaleType.centerCrop)
                //图片圆角弧度(由第三方规则处理)
                .setRoundCorners(20)
                //图片规则(根据第三方文档设定,如阿里、七牛)
                .setImageRule("sdfsf")
                //gif图片需要设置此属性
                //.asGif()
                .into(binding.testIv0);

        //所有的参数配置与上面一样
        GlideOptimize.with(this)
                .loadBitmap(url)
                .into(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        //glide内部原因这里可能会被回调两次,
                        //没有特殊要求最好使用第一种方式渲染图片
                    }
                });
    }

    //图片选择
    public void OnImageSelectClick(View view) {
        //选择后图片最大压缩大小
        imageSelectDialog.setMaxFileSize(1024);
        //最多选择图片数量
        imageSelectDialog.setMaxSelectNumber(1);
        //是否显示拍照选项
        imageSelectDialog.setShowTakingPictures(false);
        //选择后压缩图片最大宽度
        imageSelectDialog.setMaxImageWidth(720);
        //选择后压缩图片最大高度
        imageSelectDialog.setMaxImageHeight(1920);
        //选择图片后是否进行裁剪处理
        imageSelectDialog.setTailoring(true);
        //设置裁剪最大宽高
        int screenWidth = GlobalUtils.getScreenWidth(this) * 2;
        int height = screenWidth * 83 / 345;
        imageSelectDialog.withMaxSize(screenWidth, height);
        //设置裁剪宽高比
        imageSelectDialog.withAspect(345, 83);
        //显示图片选择
        imageSelectDialog.show(this);
    }

    //直接显示拍照窗口
    public void OnTakingClick(View view) {
        //选择后图片最大压缩大小
        imageSelectDialog.setMaxFileSize(1024);
        //选择后压缩图片最大宽度
        imageSelectDialog.setMaxImageWidth(720);
        //选择后压缩图片最大高度
        imageSelectDialog.setMaxImageHeight(1920);
        //选择图片后是否进行裁剪处理
        imageSelectDialog.setTailoring(true);
        //设置裁剪最大宽高
        int screenWidth = GlobalUtils.getScreenWidth(this) * 2;
        int height = screenWidth * 83 / 345;
        imageSelectDialog.withMaxSize(screenWidth, height);
        //设置裁剪宽高比
        imageSelectDialog.withAspect(345, 83);
        //显示图片选择
        imageSelectDialog.showTaking(this);
    }

    //图片选择+拍照
    public void OnImageSelectTakingClick(View view) {
        //选择后图片最大压缩大小
        imageSelectDialog.setMaxFileSize(1024);
        //最多选择图片数量
        imageSelectDialog.setMaxSelectNumber(1);
        //是否显示拍照选项
        imageSelectDialog.setShowTakingPictures(true);
        //选择后压缩图片最大宽度
        imageSelectDialog.setMaxImageWidth(720);
        //选择后压缩图片最大高度
        imageSelectDialog.setMaxImageHeight(1920);
        //选择图片后是否进行裁剪处理
        imageSelectDialog.setTailoring(true);
        //设置裁剪最大宽高
        int screenWidth = GlobalUtils.getScreenWidth(this) * 2;
        int height = screenWidth * 83 / 345;
        imageSelectDialog.withMaxSize(screenWidth, height);
        //设置裁剪宽高比
        imageSelectDialog.withAspect(345, 83);
        //显示图片选择
        imageSelectDialog.show(this);
    }

    private ImageSelectDialog imageSelectDialog = new ImageSelectDialog() {
        @Override
        protected void onSelectCompleted(List<SelectImageProperties> selectImageProperties, Object extra) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelectDialog.onActivityResult(this, requestCode, resultCode, data);
    }
}
