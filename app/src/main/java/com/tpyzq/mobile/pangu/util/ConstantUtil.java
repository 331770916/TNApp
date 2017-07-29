package com.tpyzq.mobile.pangu.util;

import android.content.Context;
import android.text.TextUtils;


import com.tpyzq.mobile.pangu.BuildConfig;

import java.io.File;

/**
 * 业务常量文件
 */
public class ConstantUtil {

    public static String TAG = "ConstantUtil";
    public static String  DB_FILE_NAME 	= "mydb.db";
    public static boolean LOG_DEBUG = false;
    public static boolean LOG_IS_BASE64 = false;
    public static String PUB_STOCKLIST = "pub_stocklist";//自选股
    public static String MARKET_TAG = "isStartConnect";
    public static int TYPE_INCRINCE = 1;
    public static int TYPE_LOW = 2;
    public static int SEEEIOIN_FAILED = 1000;
    public static String MONEYTYPE = "1109";//服务端规定的1109就是货币基金

    public static boolean jumpSelfChoiceNews = false;
    public static boolean jumpHangqing = false;

    public static String MANAGERMONEYTYPE_FUND = "1";     //基金类型
    public static int MANAGERMONEYTYPE_FUND_INT = 1;

    public static String MANAGERMONEYTYPE_OTC = "3";       //otc类型
    public static int MANAGERMONEYTYPE_OTC_INT = 3;

    public static String MANAGERMONEYTYPE_FOURTEEN = "2"; //14天类型
    public static int MANAGERMONEYTYPE_FOURTEEN_INT = 2;

    public static String OTC_L = "1"; //otc 固定类型
    public static String OTC_F = "2";   // OTC 浮动类型
    public static String OTC_FL = "3"; //otc固定加浮动

    public static int FUND_MISTAKETYPE = 1;   //自定义非货币基金类型
    public static int FUND_TYPE = 2;          //自定义货币基金类型
    public static int FUND_CHANGEGROUP = 7;   //自定义换一组

    public static int CLEARED = 1;        //下拉刷新  清除数据源的 标识
    public static int NOCLEARED = -1;     //上拉加载  不清除数据源的 标识
    public static final String NETWORK_ERROR = "网络异常";
    public static final String NETWORK_ERROR_CODE = "400";
    public static final String SERVICE_NO_DATA = "服务未返回数据";
    public static final String SERVICE_NO_DATA_CODE = "-3";
    public static final String JSON_ERROR = "服务数据解析异常";
    public static final String JSON_ERROR_CODE ="-2";


    /**
     * 交易相关常量
     */
    public static final int PAGE_INDEX_PRODUCTBUY = 2;//精选理财

    public static String ONE_SERVLET = "/HTTPServer/servlet";     ////新股 & 交易 & 手机注册 & 自选股 & 双向认证 & 消息推送

    public static final String TWO_SERVLET = "/hqserver/http/newGet";   //行情

    public static String THREE_SERVLET = "/Bigdata/bighttp";      //资讯 & 手机验证 & H5

    public static String FOUR_SERVLET = "/Bigdata/ImageServlet"; //登录验证码

    public static String FOUR_SHOUJI = "/unikeyAuth/ImageServlet"; //登录验证码

    public static String FIVE_RESOU = "/webapp/bigAction"; //热搜 & 金融生活

    public static String FIVE_SERVLET = "/webapp/bigAction";

    public static String SEVEN_ZIXUN = "/webapp/action";  //资讯
    public static String SIX_SERVLET = "/tainiu_Business/business/WTaction";

    public static String SEVEN_YZ = "/note/send";    //手机短信

    public static String EIGHT_VALIDATION = "/note/auth";     //手机效验

    public static String NINE_VALIDATION = "/note/sendVoice";     //发送语音


    public static String GET_SITES = "/note/getPath";     //获取站点

//    public static boolean auto_flag = false;
    public static String status = BuildConfig.status;
    public static String IP = BuildConfig.IP;  //行情 IP
    public static String registerServerUrl = IP;
    public static String registerNoteUrl=IP;
    public static String SJYZM = BuildConfig.SJYZM;   //交易 IP
    public static String BORY = BuildConfig.BORY;   //博瑞url
    public static String BORY_APPID = BuildConfig.BORY_APPID; //博瑞id
    public static String APP_ID = BuildConfig.APP_ID;   //生产ID
    public static String SecurityIp = BuildConfig.SecurityIp;   //注册  绑定手机号  资产分析  交易动态  股市月账单
    public static String SecurityIps = BuildConfig.SecurityIps;   //短信验证码 语音验证码  验证码验证


    public static final String OPEN_ACCOUNT_CHANNEL = "tainiuapp"; //开户id
    //新股
    public static String URL_NEW = registerServerUrl + ONE_SERVLET;
    //行情
    public static String URL = IP + TWO_SERVLET;
    //资讯
    public static String URL_ZX = IP + THREE_SERVLET;
    //新资讯
    public static String URL_NEW_ZX = IP + SEVEN_ZIXUN;

    public static String URL_BIGDATA = IP + THREE_SERVLET;
    //资讯 故事
    public static String URL_ZX_GS = registerServerUrl + ONE_SERVLET;
    //交易 & 分级基金信息查询
    public static String URL_JY = SJYZM + ONE_SERVLET;

    //登录验证码
    public static String URL_YZM = IP + FOUR_SERVLET;

    //手机注册
    public static String URL_SJLI = registerServerUrl + ONE_SERVLET;
//    public static String URL_SJLI = IPS + ONE_SERVLET;


    //手机验证
    public static String URL_SJYZM = registerNoteUrl + SEVEN_YZ;
//    public static String URL_SJYZM = IPS + THREE_SERVLET;
    public static String URL_VALIDATION = registerNoteUrl + EIGHT_VALIDATION;    //手机验证
    //语音验证
    public static String URL_YY = registerNoteUrl + NINE_VALIDATION;

    //交易登录验证码
    public static String URL_JYYZM = SJYZM + FOUR_SHOUJI;

    //交易用户绑定
    public static String URL_JYBD = registerServerUrl + ONE_SERVLET;

    //自选股
    public static String URL_SELFCHOICENET = registerServerUrl + ONE_SERVLET;

    //查询产品信息

    public static String ULR_MANAGEMONEY = registerServerUrl + ONE_SERVLET;

    public static String URL_MANAGEMONEY2 = IP + FIVE_SERVLET;

    public static String URL_SXRZ = registerServerUrl + ONE_SERVLET;

    //手机注册绑定
    public static String URL_SJZCBD = registerServerUrl + ONE_SERVLET;
//    public static String URL_SJZCBD = IPS + ONE_SERVLET;

    //H5
    public static String URL_H5 = IP + THREE_SERVLET;
    public static String URL_H5_New = SecurityIp + THREE_SERVLET;

    //分享
    public static String URL_FX = IP + THREE_SERVLET;

    //热搜
    public static String URL_RS = IP + FIVE_RESOU;

    //身份证有效期变更
//    public static String URL_SFZYXQ = IP + SIX_SERVLET;
    //填写信息对应接口
//    public static String URL_USERINFO = IP + SIX_SERVLET;
    public static String getURL_USERINFO(){
        String URL_USERINFO = "";
        if ("com.tpyzq.self.mobile.pangu".equalsIgnoreCase(BuildConfig.APPLICATION_ID)) {
            URL_USERINFO = SJYZM + SIX_SERVLET;
        } else {
            URL_USERINFO = IP + SIX_SERVLET;
        }
        return URL_USERINFO;
    }

    //三存银行变更
    public static String URL_SCYHBG = SJYZM + ONE_SERVLET;

    //跟新下载
    public static String URL_UPDATE = IP + SEVEN_ZIXUN;

    //显示持仓的key
    public static String APPEARHOLD = "appearHold";

    public static String HOLD_APPEAR = "true";        //显示持仓
    public static String HOLD_DISAPPEAR = "false";    //不显示持仓

    public static boolean USERFUL_KEYBOARD = true;
    public static boolean list_item_flag = true ; //    判断listview item 不能重复点击

//    //生产 北京或昆明
//    public static String bjUrl = "http://bj-tnhq.tpyzq.com";//北京行情地址 测试
//    public static String kmUrl = "http://km-tnhq.tpyzq.com";//昆明行情地址  灰度
    //测试或灰度
    public static String bjUrl = "https://test-tnhq.tpyzq.com";//北京行情地址 测试
    public static String kmUrl = "https://tnhq.tpyzq.com";//昆明行情地址  灰度
    public static String currentUrl = "https://test-tnhq.tpyzq.com";//昆明行情地址  灰度

    public static String SITE_JSON = "{\"message\":{\"register-httpServer\":\"http://tnhq.tpyzq.com\",\"hq\":[{\"name\":\"测试站点\",\"url\":\"http://test-tnhq.tpyzq.com:8082\"},{\"name\":\"灰度站点\",\"url\":\"http://tnhq.tpyzq.com\"}],\"register-note\":\"https://tnhq.tpyzq.com\",\"trade\":[{\"name\":\"测试站点\",\"url\":\"http://106.120.112.246:8081\"},{\"name\":\"灰度站点\",\"url\":\"https://tnhq.tpyzq.com\"}]},\"code\":\"0\",\"type\":\"success\"}";

/////////////// SDcard文件存储位置相关/////////////////////////////////////////

    /**
     * @param context
     * @param fileDir  文件夹名称
     * @param fileName 文件名称
     * @return
     */
    public static File saveFile(Context context, String fileDir, String fileName) {
        File file = Helper.getExternalFile(context, fileDir, fileName);
        if (null == file) {
            return null;
        }
        return file;
    }

    public static void setUrl(String appip,String jyip) {
        if (!TextUtils.isEmpty(appip)) {
            IP = appip;
        } else {
            appip = IP;
        }

        if (!TextUtils.isEmpty(jyip)) {
            SJYZM = jyip;
        } else {
            jyip = SJYZM;
        }
//        URL_NEW = appip + ONE_SERVLET;
        URL = appip + TWO_SERVLET;
        URL_BIGDATA = appip + THREE_SERVLET;
//        URL_ZX_GS = appip + ONE_SERVLET;
        URL_YZM = appip + FOUR_SERVLET;
//        URL_JYBD = appip + ONE_SERVLET;
//        URL_SELFCHOICENET = appip + ONE_SERVLET;
//        ULR_MANAGEMONEY = appip + ONE_SERVLET;
        URL_MANAGEMONEY2 = appip + FIVE_SERVLET;
//        URL_SXRZ = appip + ONE_SERVLET;
        URL_FX = appip + THREE_SERVLET;
        URL_RS = appip + FIVE_RESOU;
//        URL_SCYHBG = appip + ONE_SERVLET;
//        URL_SFZYXQ = appip + SIX_SERVLET;
        URL_UPDATE = appip + SEVEN_ZIXUN;
        //交易
        URL_JY = jyip + ONE_SERVLET;
        URL_JYYZM = jyip + FOUR_SHOUJI;
        URL_SCYHBG = jyip + ONE_SERVLET;

//        URL_SJLI = appip + ONE_SERVLET;             //http 手机注册
//        URL_SJYZM = appip + THREE_SERVLET;          //http 发送短信
//        URL_SJZCBD = appip + ONE_SERVLET;           //http 手机注册绑定
//        URL_YY = appip + SEVEN_ZIXUN;               //http 发送语音
//        URL_H5 = appip + THREE_SERVLET;                 //http H5


//        QutataionConnectUtil.HANGQING_URL = URL;
//        QutataionConnectUtil.NEWSTOCK_URL = URL_NEW;
    }
}
