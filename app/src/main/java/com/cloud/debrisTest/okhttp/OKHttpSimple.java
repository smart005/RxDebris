package com.cloud.debrisTest.okhttp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cloud.debrisTest.R;
import com.cloud.debrisTest.databinding.OkhttpViewBinding;
import com.cloud.debrisTest.models.NetModel;
import com.cloud.debrisTest.okhttp.beans.RecommandInfo;
import com.cloud.debrisTest.okhttp.services.GetService;
import com.cloud.nets.enums.DataType;
import com.cloud.nets.enums.ErrorType;
import com.cloud.nets.events.OnSuccessfulListener;
import com.cloud.objects.utils.JsonUtils;

public class OKHttpSimple extends AppCompatActivity {

    private OkhttpViewBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.okhttp_view);
        binding.setModel(new NetModel());

        getService.requestRecommandInfo(42, recommandListener);

//        getService.requestUserList(new OnSuccessfulListener<String>() {
//            @Override
//            public void onSuccessful(String userItems, DataType dataType, Object... extras) {
//
//            }
//        });
//
//        getService.getValidateCode(new OnSuccessfulListener<File>() {
//            @Override
//            public void onSuccessful(File file, DataType dataType, Object... extras) {
//
//            }
//
//            @Override
//            public void onError(ErrorType errorType, Object... extras) {
//
//            }
//
//            @Override
//            public void onCompleted(Object... extras) {
//
//            }
//        });

//        String json = "{\"Slides\":[{\"Type\":4,\"Title\":\"爱生活，爱音乐！108社友喜欢的音乐交流区\",\"Id\":46244,\"InfoIdentity\":\"http://hz.108sq.cn/shuo/forum/006002?fromapp108sq=slide&fromapp108sqdata=1\",\"ImgUrl\":\"http://images.108sq.cn//Files/BuinessAd/2019/03/22/759e44c9-60e0-4bf0-968b-f3ad28a589b7.jpg\",\"NewImgUrl\":\"http://images.108sq.cn//Files/BuinessAd/2019/03/22/759e44c9-60e0-4bf0-968b-f3ad28a589b7.jpg\"},{\"Type\":4,\"Title\":\"【每周五更新】生活大求真，妙招我来测！\",\"Id\":45403,\"InfoIdentity\":\"http://108sq.cn/wo/135668646?fromapp108sq=slide&fromapp108sqdata=2\",\"ImgUrl\":\"http://images.108sq.cn//Files/BuinessAd/2019/03/12/abfda517-9ad5-419f-a920-2a8e6ec22fcb.png\",\"NewImgUrl\":\"http://images.108sq.cn//Files/BuinessAd/2019/03/12/abfda517-9ad5-419f-a920-2a8e6ec22fcb.png\"},{\"Type\":4,\"Title\":\"2019第一场重磅网络安全技能大赛，全面崛起\",\"Id\":45999,\"InfoIdentity\":\"http://hz.108sq.cn/shuo/detail/37928896?fromapp108sq=slide&fromapp108sqdata=3\",\"ImgUrl\":\"http://images.108sq.cn//Files/BuinessAd/2019/03/19/08aa897d-16d5-4681-a4bc-ca09f084b190.jpg\",\"NewImgUrl\":\"http://images.108sq.cn//Files/BuinessAd/2019/03/19/08aa897d-16d5-4681-a4bc-ca09f084b190.jpg\"},{\"Type\":4,\"Title\":\"天气预报实时更新\",\"Id\":0,\"InfoIdentity\":\"http://calendar.108sq.cn/Weather/weather.html?siteid=42\",\"ImgUrl\":\"http://calendar.108sq.cn/Weather/image.aspx?siteid=42&t=2019041214\",\"NewImgUrl\":\"http://calendar.108sq.cn/Weather/image.aspx?siteid=42&t=2019041214\"}],\"Recommends\":[{\"SiteID\":0,\"Title\":\"用白醋和小苏打能制作会“飞”的气球？真相是这样的！\",\"ImagesField\":\"58922417,/user/2019/0411/170905985375410803371525152.gif,0,390*266\",\"CommentCount\":58,\"BrowserCount\":6290,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38748205},{\"SiteID\":0,\"Title\":\"一阵大风刮过，男子手掌还在，4根手指却没了...\",\"ImagesField\":\"58923199,/user/2019/0411/1730137666255520835783764.png,0,307*204\",\"CommentCount\":328,\"BrowserCount\":22452,\"Tag\":\"早知道\",\"Link\":\"http://common.108sq.cn/Html/Subect/Infos/8683.html\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38748731},{\"SiteID\":0,\"Title\":\"吓死人！女子打开衣柜后尖叫，在里面看到一个人！\",\"ImagesField\":\"58959022,/user/2019/0412/13091146975084447784101570.png,0,297*212\",\"CommentCount\":2,\"BrowserCount\":3764,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38772013},{\"SiteID\":0,\"Title\":\"“吸血鬼”婆婆！就知道榨我爸妈的钱，却给我吃剩菜\",\"ImagesField\":\"58921147,/user/2019/0411/1635355322500448772636398.jpg,0,640*400\",\"CommentCount\":81,\"BrowserCount\":9753,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38747459},{\"SiteID\":0,\"Title\":\"痛心！河南火车脱轨事故已致4人遇难，仍有2人失联\",\"ImagesField\":\"58918162,/user/2019/0411/15272642287554256133740850.png,0,609*423\",\"CommentCount\":63,\"BrowserCount\":12586,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38745901},{\"SiteID\":0,\"Title\":\"谜底揭开！寻找了29年的神秘捐款人，竟然是他\",\"ImagesField\":\"58917997,/user/2019/0411/15241664162506801205399445.png,0,605*355\",\"CommentCount\":43,\"BrowserCount\":8230,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38745738},{\"SiteID\":0,\"Title\":\"突然一阵大风刮过，男子手掌还在，4根手指却没了...\",\"ImagesField\":\"58914943,/user/2019/0411/1420102353758741500466639.png,0,316*207\",\"CommentCount\":182,\"BrowserCount\":26496,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38743463},{\"SiteID\":0,\"Title\":\"灭绝人性！福建4名男孩遭亲生父亲割颈，亲戚们都震惊了！\",\"ImagesField\":\"58912196,/user/2019/0411/1313581728751078324437867.jpg,0,504*370\",\"CommentCount\":55,\"BrowserCount\":12286,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38741386},{\"SiteID\":0,\"Title\":\"22岁漂亮女学霸做游戏陪玩师，暴瘦10斤！有客户打飞的追着求见面\",\"ImagesField\":\"58907379,/user/2019/0411/11203323537542153602391518.png,0,490*316\",\"CommentCount\":26,\"BrowserCount\":9310,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38737219},{\"SiteID\":0,\"Title\":\"再大的黑洞也大不过段子手脑洞！关于黑洞你想知道的都在这\",\"ImagesField\":\"58904379,/user/2019/0411/102012579125711080282073147.gif,0,467*296\",\"CommentCount\":14,\"BrowserCount\":6167,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38733464},{\"SiteID\":0,\"Title\":\"高管被妻子视频举报有60多个情人，企业回应…\",\"ImagesField\":\"58877341,/user/2019/0410/1749365791257130310039730.thumb.jpg,1,635*364\",\"CommentCount\":67,\"BrowserCount\":13923,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38714883},{\"SiteID\":0,\"Title\":\"大婚前夕，她收到几十封情书！还有男生偷偷赶来现场！原来…\",\"ImagesField\":\"58877142,/user/2019/0410/17444795412570704746517022.png,0,527*379\",\"CommentCount\":49,\"BrowserCount\":11872,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38714749},{\"SiteID\":0,\"Title\":\"男子深夜约小姐，对方竟发来惊悚视频！看完后他陷入煎熬..\",\"ImagesField\":\"58875995,/user/2019/0410/17215261037535646853796048.gif,0,294*233\",\"CommentCount\":48,\"BrowserCount\":14143,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38713524},{\"SiteID\":0,\"Title\":\"210㎡豪宅，5元起拍！无底价拍卖！网友评论亮了\",\"ImagesField\":\"58874748,/user/2019/0410/16590406350087511110121362.jpg,0,1080*720\",\"CommentCount\":19,\"BrowserCount\":22983,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38711239},{\"SiteID\":0,\"Title\":\"重磅！68个城市全面放开落户！京沪落户会松动吗？\",\"ImagesField\":\"58871631,/user/2019/0410/1537493916250274035852102.jpg,0,482*361\",\"CommentCount\":13,\"BrowserCount\":7534,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38710783},{\"SiteID\":0,\"Title\":\"好消息！小车驾照全国即将“一证通考”，还能异地分科考试\",\"ImagesField\":\"58870994,/user/2019/0410/15201048537520348055370921.png,0,529*391\",\"CommentCount\":14,\"BrowserCount\":10677,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38710613},{\"SiteID\":0,\"Title\":\"\\\"小龙虾托梦给我，让我救它\\\"！男子花七八万买虾放生，结果…\",\"ImagesField\":\"58865728,/user/2019/0410/132948688500658187071365448.gif,0,550*309\",\"CommentCount\":29,\"BrowserCount\":12964,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38707249},{\"SiteID\":0,\"Title\":\"杭州一名模拍照时遭妈妈飞踹！网友心疼坏了！妈妈回应…\",\"ImagesField\":\"58865543,/user/2019/0410/13263868850040631478321546.gif,0,145*212\",\"CommentCount\":40,\"BrowserCount\":12482,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38707076},{\"SiteID\":0,\"Title\":\"温柔主妇突然性情大变，梦里对老公拳打脚踢，医生道出真相\",\"ImagesField\":\"58828146,/user/2019/0409/165240376000825628321332879.gif,0,480*236\",\"CommentCount\":39,\"BrowserCount\":12498,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38679303},{\"SiteID\":0,\"Title\":\"女孩手机被偷，手机定位发现小偷位置！出乎意料的是..\",\"ImagesField\":\"58827821,/user/2019/0409/16451573537551313676119060.png,0,360*252\",\"CommentCount\":50,\"BrowserCount\":16684,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38679116},{\"SiteID\":0,\"Title\":\"朋友圈新功能慎用！网友：谁用拉黑谁，不留活口\",\"ImagesField\":\"58826399,/user/2019/0409/1607210791254553055149016.png,0,316*174\",\"CommentCount\":12,\"BrowserCount\":11818,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38678254},{\"SiteID\":0,\"Title\":\"泪崩！浙江妈妈收到礼物，打开一听竟是5年前去世女儿的心跳\",\"ImagesField\":\"58826181,/user/2019/0409/1601222978752877032472016.thumb.jpg,1,720*633\",\"CommentCount\":31,\"BrowserCount\":22764,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38678027},{\"SiteID\":0,\"Title\":\"男子为“求财运”竟放生40公斤蛇！百余人出动搜寻…\",\"ImagesField\":\"58824572,/user/2019/0409/15202915725068226015369287.png,0,463*348\",\"CommentCount\":46,\"BrowserCount\":14556,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38677178},{\"SiteID\":0,\"Title\":\"投5万赚1亿？女子痴迷“发财梦”，丈夫苦劝还被抓伤脸…\",\"ImagesField\":\"58824120,/user/2019/0409/15111437600038434844217318.png,0,371*264\",\"CommentCount\":41,\"BrowserCount\":9699,\"Tag\":\"\",\"Link\":\"\",\"AdId\":0,\"InfoType\":1,\"IsMoreImages\":false,\"ID\":38676823}]}";
//        RecommandInfo recommandInfo = JsonUtils.parseT(json, RecommandInfo.class);
//        FieldInjections fieldInjections = new FieldInjections();
//        fieldInjections.injection(recommandInfo, json);
    }

    private OnSuccessfulListener<RecommandInfo> recommandListener = new OnSuccessfulListener<RecommandInfo>() {
        @Override
        public void onSuccessful(RecommandInfo recommandInfo, DataType dataType, Object... extras) {
            //如果dataType==DataType.EmptyForOnlyCache则recommandInfo==null
            //具体接口请求成功回调;
            //如果有缓存且存在缓存和网络均会回调时则isLastCall==true表示最后一次回调
            String json = JsonUtils.toStr(recommandInfo);
            NetModel model = binding.getModel();
            model.setNetdata(json);
        }

        @Override
        public void onError(RecommandInfo recommandInfo, ErrorType errorType, Object... extras) {
            //【可选】具体接口请求失败回调
        }

        @Override
        public void onCompleted(Object... extras) {
            //【可选】具体接口请求完成回调
        }
    };

    private GetService getService = new GetService() {
        @Override
        protected void onRequestError() {
            //【可选】请求失败全局回调事件
        }

        @Override
        protected void onRequestCompleted() {
            //【可选】请求完成全局回调事件
        }
    };
}
