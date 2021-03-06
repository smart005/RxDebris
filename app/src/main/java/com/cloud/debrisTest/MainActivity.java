package com.cloud.debrisTest;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cloud.cache.RxCache;
import com.cloud.debris.BasicActivity;
import com.cloud.debris.annotations.ActivityTagParams;
import com.cloud.debris.bundle.RedirectUtils;
import com.cloud.debris.notify.NotifyManager;
import com.cloud.debrisTest.databinding.MainViewBinding;
import com.cloud.debrisTest.web.H5Test;
import com.cloud.debrisTest.web.LayoutAdapterActivity;
import com.cloud.debrisTest.web.NKitActivity;
import com.cloud.ebus.EBus;
import com.cloud.ebus.SubscribeEBus;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.beans.MapEntryItem;
import com.cloud.objects.events.OnChainInputRunnable;
import com.cloud.objects.events.OnChainRunnable;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.manager.TimingManager;
import com.cloud.objects.mapper.UrlParamsEntry;
import com.cloud.objects.observable.ObservableComponent;
import com.cloud.objects.storage.DirectoryUtils;
import com.cloud.objects.tasks.SyncChainTasks;
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
@ActivityTagParams
public class MainActivity extends BasicActivity {

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

        String json3 = " {\n" +
                "                \"ID\": 0,\n" +
                "                \"Style\": 1,\n" +
                "                \"Weight\": 2,\n" +
                "                \"Position\": 3,\n" +
                "                \"Tag\": \"广告\",\n" +
                "                \"Title\": \"daddy pig\",\n" +
                "                \"Link\": \"https://10.10.56.21?fromapp108sq=news&fromapp108sqdata=3\",\n" +
                "                \"AdId\": 168,\n" +
                "                \"SiteID\": 28,\n" +
                "                \"AdUnionAndroid\": \"1212\",\n" +
                "                \"AdUnionIos\": \"12\",\n" +
                "                \"ImagesField\": \"115479,/user/2018/0206/1007381968814054464815097.thumb.jpg,0,300*378\"\n" +
                "            }";
        ADItem adItem = JsonUtils.parseT(json3, ADItem.class);

        SyncChainTasks.getInstance()
                .addChain(new OnChainRunnable<Integer, Integer>() {
                    @Override
                    public Integer run(Integer integer, Object extras) {
                        return integer + 2;
                    }
                })
                .addChain(new OnChainRunnable<String, Integer>() {
                    @Override
                    public String run(Integer integer, Object extras) {
                        return "params:" + integer;
                    }
                })
                .addChain(new OnChainRunnable<ADItem, String>() {
                    @Override
                    public ADItem run(String s, Object extras) {
                        ADItem item = new ADItem();
                        item.setTitle(s);
                        return item;
                    }
                })
                .addChain(new OnChainInputRunnable<ADItem>() {
                    @Override
                    public Void run(ADItem adItem, Object extras) {

                        return null;
                    }
                })
                .build(2, "参数");

        RxCache.setCacheData("cache_key", "2222");
        String data = RxCache.getCacheData("cache_key");

        UrlParamsEntry urlParamsEntry = new UrlParamsEntry();
        urlParamsEntry.mapper("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=图片&hs=0&pn=1&spn=0&di=176660&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=234634259%2C4236876085&os=54892700%2C159557102&simid=3579428015%2C308375459&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fpic15.nipic.com%2F20110628%2F1369025_192645024000_2.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bgtrtv_z%26e3Bv54AzdH3Ffi5oAzdH3F90088lb_z%26e3Bip4s&gsm=0&islist=&querylist=");
        urlParamsEntry.getParams("word");
        boolean containsKey = urlParamsEntry.containsKey("objurl");

        EBus.getInstance().post("msg_key", "111", "222");

        String jsoncheck = "{\n" +
                "  \"Message\": \"\",\n" +
                "  \"State\": 1,\n" +
                "  \"Result\": {\n" +
                "    \"IsNewReg\": true,\n" +
                "    \"UserName\": \"風殇\"\n" +
                "  }\n" +
                "}";
        boolean check = ObjectJudge.isJson(jsoncheck);

        String name1 = PreviewImageActivity.class.getName();
    }

    private ObservableComponent component = new ObservableComponent() {
        @Override
        protected Object subscribeWith(Object[] objects) throws Exception {
            return null;
        }
    };

    @SubscribeEBus(receiveKey = "msg_key")
    public void onEbusEvent(String test, String str) {

    }

    public void OnH5Click(View view) {
        RedirectUtils.startActivity(this, H5Test.class,
                new MapEntryItem<>("isAutoPlayAudioVideo", true));
    }

    public void OnAndroidWebkitClick(View view) {
        RedirectUtils.startActivity(this, NKitActivity.class);
    }

    public void OnWebviewLayoutAdapterClick(View view) {
        RedirectUtils.startActivity(this, LayoutAdapterActivity.class);
    }

    @SuppressLint("WrongConstant")
    public void OnToastClick(View view) {
//        Intent intent_p = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//        startActivity(intent_p);
        if (!ObjectJudge.isNotificationEnabled(this)) {
            RedirectUtils.startAppNotication(this);
            Intent intent_s = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent_s);
            return;
        }

        NotifyManager.NotifyBuilder builder = NotifyManager.getInstance().builder(getApplicationContext())
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("习近平为什么屡屡对领导干部讲这两个典故")
                .setText("【学习进行时】“只有不忘初心、牢记使命、永远奋斗，才能让中国共产党永远年轻。”当前，“不忘初心、牢记使命”主题教育正在全党展开。新华社《学习进行时》原创品牌栏目“讲习所”围绕习近平总书记提到的有关“不忘初心”的故事推出系列解读，今天推出《习近平为什么屡屡对领导干部讲这两个典故")
                .setBroadCast(true);
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, PreviewImageActivity.class);
        builder.setIntent(intent);
        builder.show();
    }

    public void OnPreviewImageClick(View view) {
        RedirectUtils.startActivity(this, PreviewImageActivity.class);
    }

    public void OnParamPassClick(View view) {
        RedirectUtils.startActivity(this, PreviewImageActivity.class,
                new MapEntryItem<>("", ""));
    }

    public void OnStartNoticeServiceClick(View view) {
        Intent intent = new Intent(this, NoticeReceiveService.class);
        startService(intent);
    }

    public void OnStopNoticeServiceClick(View view) {
        Intent intent = new Intent(this, NoticeReceiveService.class);
        stopService(intent);
    }

    public boolean isNotificationListenersEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean gotoNotificationAccessSetting() {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;

        } catch (ActivityNotFoundException e) {//普通情况下找不到的时候需要再特殊处理找一次
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                startActivity(intent);
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Toast.makeText(this, "对不起，您的手机暂不支持", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    public void OnNoticePermissionCheckClick(View view) {
        if (!isNotificationListenersEnabled()) {
            gotoNotificationAccessSetting();
        }
    }

    private void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NoticeReceiveService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, NoticeReceiveService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public void OnEnableNoticeListenerServiceClick(View view) {
        toggleNotificationListenerService(this);
    }
}
