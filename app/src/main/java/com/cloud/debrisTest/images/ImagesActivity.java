package com.cloud.debrisTest.images;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Priority;
import com.cloud.debris.BaseActivity;
import com.cloud.debrisTest.R;
import com.cloud.debrisTest.databinding.ImagesViewBinding;
import com.cloud.images.RxImage;
import com.cloud.images.glide.GlideOptimize;
import com.cloud.images.glide.ScaleType;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:图片相关功能
 * Modifier:
 * ModifyContent:
 */
public class ImagesActivity extends BaseActivity {

    private ImagesViewBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.images_view);

        String url = "http://img2.imgtn.bdimg.com/it/u=3685170612,1820641236&fm=26&gp=0.jpg";
        String url2 = "http://pic206.nipic.com/pic/20190219/22116547_213112169000_4.jpg";
        String url3 = "http://img1.imgtn.bdimg.com/it/u=873265023,1618187578&fm=26&gp=0.jpg";
        String gifurl = "http://img.zcool.cn/community/0128ee5bc73ab5a8012099c8745c1a.gif";
        File dir = RxImage.getInstance().getBuilder().getImageCacheDir();
        File file = new File(dir, "test_image.jpg");
        GlideOptimize.with(this)
                .load(Uri.fromFile(file))
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
    }
}
