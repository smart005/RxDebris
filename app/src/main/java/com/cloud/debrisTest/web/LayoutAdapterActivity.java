package com.cloud.debrisTest.web;

import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloud.debris.BaseActivity;
import com.cloud.debrisTest.R;
import com.cloud.debrisTest.databinding.LayoutAdapterViewBinding;
import com.cloud.objects.logs.Logger;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/15
 * Description:布局适配
 * Modifier:
 * ModifyContent:
 */
public class LayoutAdapterActivity extends BaseActivity {

    private LayoutAdapterViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_adapter_view);
        //bindSetting();

        //支持javascript
        binding.webWv.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        binding.webWv.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        binding.webWv.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        binding.webWv.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        binding.webWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        binding.webWv.getSettings().setLoadWithOverviewMode(true);
        binding.webWv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        //如果不设置WebViewClient，请求会跳转系统浏览器
        binding.webWv.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
//                        //接受证书
                handler.proceed();
                //handleMessage(Message msg); 其他处理
            }

        });
        //binding.webWv.loadUrl("http://sz.108sq.org:920/shuo/detail/267401");
        bindSetting();
        String content = "<div style=\"overflow: auto;-webkit-overflow-scrolling:touch;width:100%;height:100%;\"><p>\n" +
                "\n" +
                "</p>\n" +
                "<p style=\"text-align:center\">\n" +
                "<a href=\"http://common.108sq.cn/Html/Subect/Infos/8652.html\" target=\"_self\"><img src=\"http://photoshow.108sq.org:814/user/2019/0409/0942462341132130516362455.jpg\" sq-imgid=\"150250\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\"></a>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "<br>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "终于盼来清明长假了，\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "明天起，\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "德清将迎来游客高峰，三天时间！\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "有假期的人，<br>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "娃放假要去玩的\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "都在问：<span style=\"text-shadow: #000000 2px 2px 10px; background-color: #C00000;\"><strong><span style=\"text-shadow: #000000 2px 2px 10px; color: #FFFFFF;\">求推荐好玩的地方！</span></strong></span>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "<br>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "▼\n" +
                "</p>\n" +
                "<p>\n" +
                "<br>\n" +
                "</p>\n" +
                "<p>\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">@TQ~14716040< ; ; ; ; /a><span style=\"font-size: 17px;\">：清明节放假哪有好玩的地方？求推荐一下</span></a>\n" +
                "</p>\n" +
                "<p>\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "<p>\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">@CT~5255830< ; ; ; ; /a><span style=\"font-size: 17px;\">：社区的朋友清明节两天假请问周边有什么好玩的地方推荐一下。本人现在武康。最好不要太远。60公里左右。谢谢</span></a>\n" +
                "</p>\n" +
                "<p>\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "<p>\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">@Cc林宝宝< ; ; ; ; /a><span style=\"font-size: 17px;\">：想出去走走，有啥好玩的地方推荐吗？闲散的慢生活类型的</span></a>\n" +
                "</p>\n" +
                "<p>\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">有推荐的<span style=\"color: #FFFFFF; background-color: #E36C09;\"><strong><span style=\"text-shadow: #E36C09 2px 2px 10px;\">友友赶紧地晒出你的游记</span></strong></span></a>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "   <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">跟大家推荐一下吧~</a>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "   <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">让不出门的德清人，</a>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "   <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">找到一个在自家门口过节的好地方！</a>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "   <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\">小编先来盘点一下</a>\n" +
                "</p>\n" +
                "<p>\n" +
                "   <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" p=\"\" a=\"\" data-tcsay=\"img\" span=\"\" strong=\"\" img=\"\" src=\"http://photoshow.108sq.org:814/user/2019/0409/094247255171061433001758553.gif\" sq-imgid=\"150251\" alt=\"\" width=\"auto\" height=\"auto\" height:=\"\">\n" +
                "   <section style=\"max-width: 100%; min-height: 40px;\">\n" +
                "       <section style=\"margin-right: auto; margin-left: auto; max-width: 100%; width: 100%;\">\n" +
                "           <table width=\"666\">\n" +
                "               <tbody style=\"max-width: 100%;\">\n" +
                "                   <tr style=\"max-width: 100%;\" class=\"firstRow\">\n" +
                "                       <td colspan=\"1\" rowspan=\"1\" style=\"padding-top: 0px; padding-bottom: 0px; padding-left: 10px; border-width: 0px 0px 0px 3px; border-top-style: none; border-right-style: none; border-bottom-style: none; border-color: #3e3e3e #3e3e3e #3e3e3e #ebedf4; max-width: 100%; border-radius: 0px;\">\n" +
                "                           <section style=\"max-width: 100%;\">\n" +
                "                               <section style=\"max-width: 100%;\">\n" +
                "                                   <section style=\"max-width: 100%;\">\n" +
                "                                       <p style=\"max-width: 100%; min-height: 1em;\">\n" +
                "                                           <br>\n" +
                "                                       </p>\n" +
                "                                   </section>\n" +
                "                               </section>\n" +
                "                           </section>\n" +
                "                           <section style=\"max-width: 100%;\">\n" +
                "                               <section style=\"max-width: 100%;\">\n" +
                "                                   <section style=\"max-width: 100%; font-size: 14px; color: #838383; line-height: 2; letter-spacing: 1.6px;\">\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 0em;\">\n" +
                "<span style=\"font-size: 17px; color: #000000;\">清明期间，下渚湖来了135个稻草人，和传统印象中的稻草人不一样，135个稻草人将各种卡通形象以稻草为原材料生动地展现在大家的眼前，由几十名设计师纯手工历时一个月编织完成。稻草人分布于乐岛的各个区域，有的在油菜花丛中，有的在草坪上，或分散或集中，形成了国内外寓言故事、森林动物王国、卡通动画形象等多个主题，生动形象、萌态百出。</span>\n" +
                "                                       </p>\n" +
                "                                   </section>\n" +
                "                               </section>\n" +
                "                           </section>\n" +
                "                           <section style=\"max-width: 100%;\">\n" +
                "                               <section style=\"max-width: 100%;\">\n" +
                "                                   <section style=\"max-width: 100%;\">\n" +
                "                                       <p style=\"text-align:center\">\n" +
                "                                           <img src=\"http://photoshow.108sq.org:814/user/2019/0409/09425377154461224075576100.gif\" sq-imgid=\"150252\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "                                       </p>\n" +
                "                                       <p style=\"text-align:center\">\n" +
                "                                           <img src=\"http://photoshow.108sq.org:814/user/2019/0409/09425586566453131421164574.jpg\" sq-imgid=\"150253\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "                                       </p>\n" +
                "                                       <p style=\"max-width: 100%; min-height: 1em;\">\n" +
                "                                           <br>\n" +
                "                                       </p>\n" +
                "                                   </section>\n" +
                "                               </section>\n" +
                "                           </section>\n" +
                "                       </td>\n" +
                "                   </tr>\n" +
                "               </tbody>\n" +
                "           </table>\n" +
                "       </section>\n" +
                "   </section>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" p=\"\" a=\"\" data-tcsay=\"user\">\n" +
                "   <section style=\"max-width: 100%;\">\n" +
                "       <section style=\"max-width: 100%; color: rgb(231, 147, 133); font-size: 20px;\">\n" +
                "           <p style=\"max-width: 100%; min-height: 1em; text-align: center;\">\n" +
                "               <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 18px;\"><strong><strong style=\"max-width: 100%;\"><span style=\"max-width: 100%; text-shadow: #FCAC82 2px 0px 2px;\">第二站：</span><span style=\"max-width: 100%; letter-spacing: 0px; text-align: left;\">莫干山赏花节</span></strong></strong></span></a>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-align: center;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" height:=\"\" class=\"_135editor\" section=\"\" a=\"\" data-tcsay=\"user\" strong=\"\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"padding-right: 20px; padding-left: 20px; max-width: 100%; color: rgb(134, 129, 129); line-height: 2; letter-spacing: 2px;\">\n" +
                "<p style=\"width: auto; height: auto; color: rgb(131, 131, 131); font-size: 14px; letter-spacing: 1.6px; max-width: 100%; min-height: 1em; text-indent: 2em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px; color: #000000;\">时间：4月5日上午10:00</span></a>\n" +
                "           </p>\n" +
                "           <p style=\"width: auto; height: auto; color: rgb(131, 131, 131); font-size: 14px; letter-spacing: 1.6px; max-width: 100%; min-height: 1em; text-indent: 2em;\">\n" +
                "               <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px; color: #000000;\">地点：莫干山镇庾村广场、民国风情街</span></a>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 0em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 0em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px; color: #000000;\">莫干山庾村花市是每年都会举办的活动，每年这个时候，这里就变成了花的海洋。爱花赏花的人都会聚集在这里！</span></a>\n" +
                "           </p>\n" +
                "       </section>\n" +
                "   </section>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" p=\"\" a=\"\" data-tcsay=\"user\">\n" +
                "   <section style=\"max-width: 100%;\">\n" +
                "       <section style=\"max-width: 100%; font-size: 20px; color: #e79385;\">\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 0em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px;\">骑上自行车，一边迎着微风，一边欣赏沿途的风景，身临其境感受山花烂漫，停下还能来张美美的合照！</span></a>\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<p style=\"text-align:center\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><img src=\"http://photoshow.108sq.org:814/user/2019/0409/0942566967116018417089032.jpg\" sq-imgid=\"150254\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\"></a>\n" +
                "</p>\n" +
                "<p style=\"text-align:center\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" p=\"\" a=\"\" data-tcsay=\"user\" span=\"\">\n" +
                "<section style=\"max-width: 100%; text-align: center;\">\n" +
                "<section style=\"padding-right: 5px; padding-left: 7px; max-width: 100%; display: inline-block; vertical-align: middle; width: 50%;\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<p style=\"max-width: 100%; min-height: 1em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"margin-top: 5px; margin-bottom: 5px; max-width: 100%;\">\n" +
                "<section style=\"max-width: 100%; display: inline-block; vertical-align: bottom; width: 64.2917px;\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"margin-bottom: 5px; max-width: 100%;\">\n" +
                "<section style=\"max-width: 100%; vertical-align: middle; display: inline-block; line-height: 0; width: 41.7778px;\"></section>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<section style=\"padding-left: 5px; max-width: 100%; display: inline-block; vertical-align: bottom; width: 257.194px;\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"margin-top: 5px; max-width: 100%;\">\n" +
                "<section style=\"max-width: 100%; text-align: justify; line-height: 1.5; color: #e79385;\">\n" +
                "                                   <p style=\"max-width: 100%; min-height: 1em;\">\n" +
                "                                       <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px;\">莫干山花花世界</span></a>\n" +
                "                                   </p>\n" +
                "                               </section>\n" +
                "                           </section>\n" +
                "                       </section>\n" +
                "                   </section>\n" +
                "               </section>\n" +
                "           </section>\n" +
                "           <section style=\"max-width: 100%;\">\n" +
                "               <section style=\"max-width: 100%;\">\n" +
                "                   <section style=\"padding-right: 20px; padding-left: 20px; max-width: 100%; font-size: 14px; color: rgb(131, 131, 131); text-align: justify; line-height: 2; letter-spacing: 2px;\">\n" +
                "                       <p>\n" +
                "                           <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><br></a>\n" +
                "                       </p>\n" +
                "                   </section>\n" +
                "               </section>\n" +
                "           </section>\n" +
                "       </section>\n" +
                "   </section>\n" +
                "</section>\n" +
                "<p style=\"color: rgb(131, 131, 131); font-family: -apple-system-font, BlinkMacSystemFont, \" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" font-size:=\"\" letter-spacing:=\"\" text-align:=\"\" widows:=\"\" a=\"\" data-tcsay=\"user\" span=\"\" p=\"\">\n" +
                "   <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px; color: #000000;\">暖阳之下更加浪漫动人！<br style=\"max-width: 100%;\"></span></a>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" font-size:=\"\" letter-spacing:=\"\" text-align:=\"\" widows:=\"\" a=\"\" data-tcsay=\"user\" span=\"\" section=\"\" p=\"\" line-height:=\"\" white-space:=\"\" class=\"_135editor\"></p>\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"padding-right: 20px; padding-left: 20px; max-width: 100%; line-height: 2; letter-spacing: 2px;\">\n" +
                "<p style=\"max-width: 100%; min-height: 1em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px;\">一睁眼就是夺目的妖娆</span></a>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; min-height: 1em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px;\">一回首便是抓拍的瞬间</span></a>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; min-height: 1em;\">\n" +
                "<a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"font-size: 17px;\">紫藤长廊 缠绕了这一春的浪漫</span></a>\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" p=\"\" a=\"\" data-tcsay=\"user\" img=\"\" src=\"http://photoshow.108sq.org:814/user/2019/0409/09425822979973160300332915.jpg\" sq-imgid=\"150256\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"max-width: 100%; color: #e79385; font-size: 20px;\">\n" +
                "           <p style=\"max-width: 100%; min-height: 1em; text-align: center;\">\n" +
                "               <a data-tcsay=\"user\" style=\"color: rgb(0, 102, 255); font-size: 17px; text-decoration-line: underline;\"><span style=\"max-width: 100%; text-shadow: #fcac82 2px 0px 2px; font-size: 18px;\"><strong style=\"max-width: 100%;\">第四站：新市古镇</strong></span></a>\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<section style=\"border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" p=\"\" a=\"\" data-tcsay=\"user\" span=\"\" font-size:=\"\" text-indent:=\"\">\n" +
                "蚕花庙会本是蚕农自发在清明节那天组织的祭祀活动，以祈求风调雨顺，蚕事顺利，随着几十年发展逐渐发展成了一大盛会！<span style=\"font-size: 15px;\">（</span><a href=\"http://deqing.108sq.cn/shuo/detail/38488047\" target=\"_self\" style=\"font-size: 15px; text-decoration-line: underline; color: rgb(0, 176, 240);\"><span style=\"font-size: 17px;\"><strong><span style=\"font-size: 15px;\">点击链接查看现场直播←</span></strong></span></a><span style=\"font-size: 15px;\">）</span>\n" +
                "</section>\n" +
                "<p></p>\n" +
                "<section style=\"margin-top: 10px; margin-bottom: 10px; max-width: 100%; text-align: center;\">\n" +
                "<p>\n" +
                "<img src=\"http://photoshow.108sq.org:814/user/2019/0409/09425969788364758485180437.jpg\" sq-imgid=\"150257\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "</p>\n" +
                "<p>\n" +
                "<img src=\"http://photoshow.108sq.org:814/user/2019/0409/09430052893061812144108722.jpg\" sq-imgid=\"150258\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "</p>\n" +
                "<p>\n" +
                "<img src=\"http://photoshow.108sq.org:814/user/2019/0409/0943011349653578747482472.jpg\" sq-imgid=\"150259\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "</p>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" p=\"\" span=\"\">\n" +
                "<section style=\"margin-top: 10px; margin-bottom: 10px; max-width: 100%; text-align: center;\">\n" +
                "<section style=\"max-width: 100%; vertical-align: middle; display: inline-block; line-height: 0;\"></section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" img=\"\" src=\"http://photoshow.108sq.org:814/user/2019/0409/09430168399656433643192324.jpg\" sq-imgid=\"150260\" data-tcsay=\"img\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "<p style=\"margin-top: 10px; margin-bottom: 10px; max-width: 100%; text-indent: 0em;\">\n" +
                "<br>\n" +
                "</p>\n" +
                "<section style=\"padding: 10px; max-width: 100%; display: inline-block; width: 100%; border: 2px solid rgb(192, 200, 209); border-radius: 0.7em;\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"padding-right: 20px; padding-left: 20px; max-width: 100%; line-height: 2; letter-spacing: 2px;\">\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 2em;\">\n" +
                "<span style=\"font-size: 17px;\">蚕花姑娘沿途抛洒的是粘着蚕花的糖果，蚕花并不是真的花卉，而是用彩纸或绸帛做成的花饰，寓意蚕农期待蚕桑生产丰收的心愿。围观的村民和游客们争相接住蚕花，就是为了讨个好彩头。</span>\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<p>\n" +
                "<br>\n" +
                "</p>\n" +
                "</section>\n" +
                "<section style=\"max-width: 100%; color: rgb(51, 51, 51); border: 0px none;\" helvetica=\"\" pingfang=\"\" hiragino=\"\" sans=\"\" microsoft=\"\" yahei=\"\" letter-spacing:=\"\" line-height:=\"\" text-align:=\"\" white-space:=\"\" widows:=\"\" class=\"_135editor\" section=\"\" img=\"\" src=\"http://photoshow.108sq.org:814/user/2019/0409/09430241603857772281137395.jpg\" sq-imgid=\"150261\" data-tcsay=\"img\" alt=\"\" width=\"auto\" height=\"auto\">\n" +
                "<section style=\"max-width: 100%;\">\n" +
                "<section style=\"padding-right: 20px; padding-left: 20px; max-width: 100%; line-height: 2; letter-spacing: 2px;\">\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 0em;\">\n" +
                "<span style=\"font-size: 17px;\">清明作为传统节日，各地都会举行各式各样的活动或者传统，不管是德清人，还是来旅游的游客，这些都是不容错过的游玩项目哦，还可以参与到其中。</span>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 0em;\">\n" +
                "<br>\n" +
                "</p>\n" +
                "<p style=\"max-width: 100%; min-height: 1em; text-indent: 0em;\">\n" +
                "<span style=\"font-size: 17px;\">清明将至，不管是祭祀还是踏青，请大家文明出行，在野外不用明火，不随地丢烟头，不烧纸钱。</span>\n" +
                "</p>\n" +
                "</section>\n" +
                "</section>\n" +
                "</section>\n" +
                "<p>\n" +
                "<span style=\"font-size: 14px; color: #A5A5A5;\">来源：德清旅游</span>\n" +
                "</p>\n" +
                "<p>\n" +
                "   <br>\n" +
                "</p>\n" +
                "<p style=\"text-align: center;\">\n" +
                "   更多关于清明信息<span style=\"color: #FFFFFF; text-shadow: #C00000 2px 2px 10px; background-color: #E36C09;\">点击下图查看↓↓↓</span><br>\n" +
                "</p>\n" +
                "<p style=\"text-align:center\">\n" +
                "<a href=\"http://common.108sq.cn/Html/Subect/Infos/8652.html\" target=\"_self\"><img src=\"http://photoshow.108sq.org:814/user/2019/0409/0943032350856041776462465.jpg\" sq-imgid=\"150262\" data-tcsay=\"img\" style=\"width: auto; height: auto; max-width: 100%;\" alt=\"\" width=\"auto\" height=\"auto\"></a>\n" +
                "</p>\n" +
                "<p>\n" +
                "<br>\n" +
                "</p>\n" +
                "<section data-role=\"paragraph\" class=\"_135editor\" style=\"border: 0px none;\">\n" +
                "<p>\n" +
                "<br>\n" +
                "</p>\n" +
                "</section>\n" +
                "<p>\n" +
                "\n" +
                "\n" +
                "</p></div>\n" +
                "\n";
        loadData(content);
//        binding.webWv.loadData(Html.fromHtml(content).toString(), "text/html", "UTF-8");
    }

    public void loadData(String htmlContent) {
        try {
            if (htmlContent.contains("<html>")) {
                binding.webWv.loadDataWithBaseURL("", htmlContent, "text/html", "utf-8", "");
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("<!DOCTYPE html>");
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<meta charset=\"utf-8\"/>");
                sb.append("<meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;\" name=\"viewport\" />");
                sb.append("<style type=\"text/css\">");
                sb.append("body,div,ul,li,p,section,table,a {padding: 0;margin: 0;display: block;}");
                sb.append("img{max-width:100% !important; width:auto; height:auto;}");
//                sb.append("p,section,div,table");
//                sb.append("p,section,div,table,body,span {\n" +
//                        "" +
//                        //"    overflow : hidden;\n" +
//                        "    text-overflow: ellipsis;\n" +
//                        "-o-text-overflow: ellipsis;" +
//                        "    display: -webkit-box;\n" +
//                        "    -webkit-line-clamp: *;\n" +
//                        "    -webkit-box-orient: vertical;\n" +
//                        "word-wrap:break-word;" +
//                        "word-break:break-all;" +
//                        "}");
                sb.append("</style></head>");
                sb.append("<body>");
                sb.append(htmlContent);
                sb.append("</body>");
                sb.append("</html>");
                sb.append("<script type=\"text/javascript\">\n" +
                        "\twindow.onload = function() {\n" +
                        "\t\t$_init_element_max_size();\n" +
                        "\t};\n" +
                        "\t$_init_element_max_size();\n" +
                        "\n" +
                        "\tfunction $_init_element_max_size() {\n" +
                        "\t\t//取客户端最大宽度\n" +
                        "\t\tvar maxwidth = document.body.clientWidth;\n" +
                        "\t\t//需要处理元素集\n" +
                        "\t\tvar elements = ['body', 'section', 'img', 'p', 'table', 'div'];\n" +
                        "\t\t//此元素下偏移量减10\n" +
                        "\t\tvar offsets = ['section'];\n" +
                        "\t\tfor(var i in elements) {\n" +
                        "\t\t\tif(!elements[i] || elements[i] == 'undefined') {\n" +
                        "\t\t\t\tcontinue;\n" +
                        "\t\t\t}\n" +
                        "\t\t\tvar elementList = document.getElementsByTagName(elements[i]);\n" +
                        "\t\t\tfor(var e in elementList) {\n" +
                        "\t\t\t\tif(!elementList[e].style || elementList[e].style == 'undefined') {\n" +
                        "\t\t\t\t\tcontinue;\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t\tif(offsets.indexOf(elements[i]) >= 0) {\n" +
                        "\t\t\t\t\telementList[e].style.maxWidth = (maxwidth - 10) + \"px\";\n" +
                        "\t\t\t\t} else {\n" +
                        "\t\t\t\t\telementList[e].style.maxWidth = maxwidth + \"px\";\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "</script>");
//                sb.append("<script type=\"text/javascript\">");
//                sb.append("alert('4');window.onload = function() {alert(1);");
//                sb.append("var maxwidth = document.body.clientWidth;");
//                sb.append("var imgs = document.getElementsByTagName('img');");
//                sb.append("for(var i in imgs) {");
//                sb.append("imgs[i].style.width = maxwidth+'px';");
//                sb.append("} alert(maxwidth); ");
//                sb.append("var sections = document.getElementsByTagName('section');");
//                sb.append("for(var i in sections) {");
//                sb.append("sections[i].style.width = maxwidth+'px';");
//                sb.append("}");
//                sb.append(" var ps = document.getElementsByTagName('p');");
//                sb.append("for(var i in ps) {");
//                sb.append("ps[i].style.width = maxwidth+'px';");
//                sb.append("}");
//                sb.append(" var divs = document.getElementsByTagName('div');");
//                sb.append("for(var i in divs) {");
//                sb.append("divs[i].style.width = maxwidth+'px';");
//                sb.append("}");
//                sb.append(" var tables = document.getElementsByTagName('table');");
//                sb.append("for(var i in tables) {alert(tables[i]);");
//                sb.append("tables[i].style.width = maxwidth+'px';");
//                sb.append("}");
//                sb.append("document.getElementsByTagName('p');");
//                sb.append("};</script>");
                binding.webWv.loadDataWithBaseURL("", sb.toString(), "text/html", "utf-8", "");
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private void bindSetting() {
        WebSettings settings = binding.webWv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //是否可访问本地文件，默认值true
        settings.setAllowFileAccess(true);
        //NARROW_COLUMNS
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        }
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //是否可访问Content Provider的资源，默认值true
        settings.setAllowContentAccess(true);
        //允许webview对文件的操作
        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
        }
        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setSavePassword(true);
    }
}
