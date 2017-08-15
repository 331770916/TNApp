package com.tpyzq.mobile.pangu.util;

import android.content.Context;
import android.text.TextUtils;


import com.tpyzq.mobile.pangu.BuildConfig;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 业务常量文件
 */
public class ConstantUtil {

    public static String TAG = "ConstantUtil";
    public static String DB_FILE_NAME = "mydb.db";
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
    public static final String JSON_ERROR_CODE = "-2";

    public static final String DEFAULT_NUM = "30";//默认请求数据条数
    public static final String ZIXUN_NUM = "10";//默认请求数据条数

    //显示持仓的key
    public static String APPEARHOLD = "appearHold";

    public static String HOLD_APPEAR = "true";        //显示持仓
    public static String HOLD_DISAPPEAR = "false";    //不显示持仓

    public static boolean USERFUL_KEYBOARD = true;
    public static boolean list_item_flag = true; //    判断listview item 不能重复点击

    /**
     * 交易相关常量
     */
    public static final int PAGE_INDEX_PRODUCTBUY = 2;//精选理财


    //    public static boolean auto_flag = false;
    public static String status = BuildConfig.status;
    public static String HQ_IP = BuildConfig.IP;  //行情 IP
    public static String JY_IP = BuildConfig.SJYZM;   //交易 IP
    public static String registerServerUrl = BuildConfig.registerServerUrl;//默认注册地址
    public static String registerNoteUrl=BuildConfig.registerNoteUrl;
    public static String BORY = BuildConfig.BORY;   //博瑞url
    public static String BORY_APPID = BuildConfig.BORY_APPID; //博瑞id
    public static String APP_ID = BuildConfig.APP_ID;   //生产ID
    public static String SecurityIp = BuildConfig.SecurityIp;   //注册  绑定手机号  资产分析  交易动态  股市月账单
    public static String SecurityIps = BuildConfig.SecurityIps;   //短信验证码 语音验证码  验证码验证

    //    //生产 北京或昆明
    public static String bjUrl = BuildConfig.bjUrl;//北京行情地址 测试
    public static String kmUrl = BuildConfig.kmUrl;//昆明行情地址  灰度
    public static String currentUrl = BuildConfig.currentUrl;//昆明行情地址  灰度
    //测试或灰度
//    public static String bjUrl = BuildConfig.bjUrl;//北京行情地址 测试
//    public static String kmUrl = BuildConfig.kmUrl;//昆明行情地址  灰度
//    public static String currentUrl = BuildConfig.currentUrl;//昆明行情地址  灰度

    public static ArrayList<HashMap<String, String>> stock_account_list = new ArrayList<>();

    public static String SITE_JSON ;

    public static String setSiteJson() {
        String url = "";
        try {
            Context context = CustomApplication.getContext();
            String resValueStr = context.getResources().getString(R.string.site_json);
            int id = context.getResources().getIdentifier(resValueStr, "string", context.getPackageName());
            url = context.getResources().getString(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return url;
        }
    }

    public static final String OPEN_ACCOUNT_CHANNEL = "tainiuapp"; //开户id

    //资讯 故事 手机注册 交易用户绑定 自选股 查询产品信息 新股
    public static String getURL_NEW(){
        return registerServerUrl + "/HTTPServer/servlet";
    }

    ///////////////////////////////////// 行情地址 /////////////////////////////////////////

    //新股 & 资讯故事 & 自选股 & 微信注册 & 查询产品信息
    public static String getURL_HQ_HS() {
        return HQ_IP + "/HTTPServer/servlet";
    }

    //行情
    public static String getURL_HQ_HHN() {
        return HQ_IP + "/hqserver/http/newGet";
    }

    //H5 & 分享
    public static String getURL_HQ_BB() {
        return HQ_IP + "/Bigdata/bighttp";
    }

    //热搜
    public static String getURL_HQ_WB() {
        return HQ_IP + "/webapp/bigAction";
    }

    //资讯 & 跟新下载
    public static String getURL_HQ_WA() {
        return HQ_IP + "/webapp/action";
    }

    //1.7.1要闻
    public static String getURL_IMPORTANT() {
        return HQ_IP + "/news/important";
    }

    //1.7.2直播
    public static String getURL_STREAMING() {
        return HQ_IP + "/news/streaming";
    }

    //1.7.3 查询栏目信息列表
    public static String getURL_HKSTOCKS() {
        return HQ_IP + "/news/hkstocks";
    }

    //1.7.4 信息详情
    public static String getURL_DETAIL() {
        return HQ_IP + "/news/detail";
    }

    //1.7.5 栏目list
    public static String getURL_CLASSLIST() {
        return HQ_IP + "/news/classlist";
    }

    //1.7.6 股票相关新闻
    public static String getURL_STOCKNEWS(){
        return HQ_IP + "/news/stockNews";
    }


    ///////////////////////////////////// 交易地址 /////////////////////////////////////////

    //分级基金信息查询 & 三存银行变更
    public static String getURL_JY_HS() {
        return JY_IP + "/HTTPServer/servlet";
    }

    //交易登录图片验证码
    public static String getURL_JY_UI() {
        return JY_IP + "/unikeyAuth/ImageServlet";
    }

    ///////////////////////////////////// HTTPS地址 /////////////////////////////////////////
    //新的URL路径
    //手机图片验证码
    public static String getURL_HANDSE_PICTURE() {
        return registerNoteUrl + "/note/getImage";
    }

    //手机短信验证码
    public static String getURL_HANDSE_SMS() {
        return registerNoteUrl + "/note/imgAuthSms";
    }

    //手机语音验证码
    public static String getURL_HANDSE_SPEECH() {
        return registerNoteUrl + "/note/imgAuthVoice?=&=&=";
    }

    //手机注册
    public static String getURL_HANDSE_REGISTER() {
        return registerNoteUrl + "/note/authAndRegister";
    }

    //手机绑定手机号
    public static String getURL_HANDSE_BINDING() {
        return registerNoteUrl + "/note/WXBinding";
    }

    //H5
    // 资产分析 & 交易动态 & 股市月账单
    public static String getURL_S_BB() {
        return SecurityIp + "/Bigdata/bighttp";
    }

    public static String getURL_USERINFO() {
        String URL_USERINFO = "";
        if ("com.tpyzq.self.mobile.pangu".equalsIgnoreCase(BuildConfig.APPLICATION_ID)) {
            URL_USERINFO = JY_IP + "/tainiu_Business/business/WTaction";
        } else {
            URL_USERINFO = HQ_IP + "/tainiu_Business/business/WTaction";
        }
        return URL_USERINFO;
    }


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

    public static void setUrl(String appip, String jyip) {
        if (!TextUtils.isEmpty(appip)) {
            HQ_IP = appip;
        }

        if (!TextUtils.isEmpty(jyip)) {
            JY_IP = jyip;
        }


    }
}
