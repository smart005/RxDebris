package com.cloud.debrisTest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cloud.debris.BaseActivity;
import com.cloud.debris.bundle.RedirectUtils;
import com.cloud.debrisTest.databinding.MainViewBinding;
import com.cloud.debrisTest.images.ImagesActivity;
import com.cloud.debrisTest.okhttp.OKHttpSimple;
import com.cloud.debrisTest.web.H5Test;
import com.cloud.debrisTest.web.LayoutAdapterActivity;
import com.cloud.debrisTest.web.NKitActivity;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.TimingManager;
import com.cloud.objects.beans.MapEntryItem;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.storage.DirectoryUtils;
import com.cloud.objects.utils.JsonUtils;

import java.io.File;

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

        HandlerManager.getInstance().post(new RunnableParamsN<Integer>() {
            @Override
            public void run(Integer... params) {
                Logger.info("当前消息" + params[0]);
            }
        }, 10);

        json = "{\"uptoken\":\"0KsWRl8QAypL9e3EAKKT7ZPhAFvSNj4iH_LHxsd7:HqctaCmjpKOJrJrd_wdgcWvZqEo=:eyJzY29wZSI6IjEwOHNxdmlkZW90ZXN0IiwiZGVhZGxpbmUiOjE1NTMyMzY1OTUsImluc2VydE9ubHkiOjAsImRldGVjdE1pbWUiOjAsImZzaXplTGltaXQiOjAsImNhbGxiYWNrRmV0Y2hLZXkiOjB9\"}";
        String uptoken = JsonUtils.getAccurateValue("uptoken", json);

        String jsonString = "{\"page_size\":\"12321321\",\"list\":null,\"name\":\"测试\",\"list2\":[{\"name\":\"杭州\",\"detail\":\"1302室\"}],\"page_size\":\"123\",\"page_index\":0,\"page_count\":0,\"has_next\":false,\"datas\":null,\"code\":0,\"issuccess\":true,\"msg\":null}";
        boolean containerKey = JsonUtils.containerKey("page_size", jsonString);
        String value = JsonUtils.getValue("page_size", jsonString);
        String list = JsonUtils.getValue("list", jsonString);
        String list2 = JsonUtils.getValue("list2", jsonString);
        String name = JsonUtils.getValue("name", jsonString);
        String accurateValue = JsonUtils.getAccurateValue("list2->name", jsonString);

        TimingManager.executionTimeStatistics(new Runnable() {
            @Override
            public void run() {
                String dirs = "[images->[forum->[video,temp],user,comments]]";
                DirectoryUtils.getInstance().addDirectory("images")
                        .addChildDirectory("forum")
                        .addChildDirectory("video")
                        .addDirectory("temp")
                        .prevDirectory("user")
                        .addDirectory("comments")
                        .buildDirectories();
                File video = DirectoryUtils.getInstance().getDirectory("video");
                File directory = DirectoryUtils.getInstance().getDirectory("video");
                String dirJson = DirectoryUtils.getInstance().toString();
            }
        });
    }

    public void OnNetFrameClick(View view) {
        RedirectUtils.startActivity(this, OKHttpSimple.class,
                new MapEntryItem<>("key1", "1111"),
                new MapEntryItem<>("key2", null),
                new MapEntryItem<>("key3", ""),
                new MapEntryItem<>("key4", 2),
                new MapEntryItem<>("key5", null));
    }

    public void OnH5Click(View view) {
        RedirectUtils.startActivity(this, H5Test.class);
    }

    public void OnAndroidWebkitClick(View view) {
        RedirectUtils.startActivity(this, NKitActivity.class);
    }

    public void OnImageFunctionClick(View view) {
        RedirectUtils.startActivity(this, ImagesActivity.class);
    }

    public void OnWebviewLayoutAdapterClick(View view) {
        RedirectUtils.startActivity(this, LayoutAdapterActivity.class);
    }
}
