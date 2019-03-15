package com.cloud.debrisTest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cloud.debris.BaseActivity;
import com.cloud.debris.utils.RedirectUtils;
import com.cloud.debrisTest.databinding.MainViewBinding;
import com.cloud.debrisTest.images.ImagesActivity;
import com.cloud.debrisTest.okhttp.OKHttpSimple;
import com.cloud.objects.ObjectJudge;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class MainActivity extends BaseActivity {

    private MainViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_view);
        binding.setHandler(this);

//        String json = "[{\"UserId\":436195,\"GradeInfo\":\"Lv8\"}]";
//        List<UserItem> userItems = JsonUtils.parseArray(json, UserItem.class);
        String json = "{\"FanCount\":0,\"FriendsCount\":6,\"InfoCount\":0,\"PrestigeID\":\"Lv4\",\"PrestigeName\":\"秀才\",\"UserID\":439469,\"UserName\":\"lijh\",\"Sex\":0,\"SexName\":\"男\",\"IsUpdateName\":true,\"IntegralCount\":203,\"Mobile\":\"153****5753\",\"IsSuperModerator\":false,\"RegDate\":\"2019-02-18\",\"DisplayCount\":104,\"Introduction\":\"坚持坚持\",\"BlacklistStatus\":false,\"BackgroundImage\":\"\",\"IsHavBackgroundRole\":false,\"IsVerified\":true}";
        boolean status = ObjectJudge.isJson(json);
    }

    public void OnNetFrameClick(View view) {
        RedirectUtils.startActivity(this, OKHttpSimple.class);
    }

    public void OnH5Click(View view) {
        RedirectUtils.startActivity(this, H5Test.class);
    }

    public void OnImageFunctionClick(View view) {
        RedirectUtils.startActivity(this, ImagesActivity.class);
    }

}
