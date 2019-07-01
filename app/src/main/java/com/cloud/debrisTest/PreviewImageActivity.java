package com.cloud.debrisTest;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.cloud.debris.BaseFragmentActivity;
import com.cloud.debris.utils.BaseCommonUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.ResUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-06-29
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class PreviewImageActivity extends BaseFragmentActivity {

    private FrameLayout imageFragmentFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_image_view);
        imageFragmentFl = (FrameLayout) findViewById(R.id.image_fragment_fl);

        PreviewImageFragment fragment = new PreviewImageFragment();
        String uri = ResUtils.getResourcesUri(this, R.drawable.timg);
        String[] urls = {
                uri,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561915542470&di=8f8006c1cd57c64d6db81250ce5e617f&imgtype=0&src=http%3A%2F%2Fwww.xiugei.com%2Fviewps%2Fcontentimg%2Fshuzhuo-shuyi%2Fcontent1421978910151.png",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561916307169&di=9f26826b95c10cc8c466828ace4bf947&imgtype=0&src=http%3A%2F%2Fimg004.hc360.cn%2Fg7%2FM00%2F49%2F4B%2FwKhQslM2OSqEQgzSAAAAAEA7gtc494.jpg",
                "https://b-ssl.duitang.com/uploads/item/201703/19/20170319105558_JwkaT.thumb.700_0.jpeg"
        };
        Bundle bundle = new Bundle();
        bundle.putInt("position", 0);
        bundle.putString("urls", JsonUtils.toJson(urls));
        fragment.setArguments(bundle);
        BaseCommonUtils.bindFrameLayout(this, R.id.image_fragment_fl, fragment);
    }
}
