package com.tpyzq.mobile.pangu.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.IndexActivity;
import com.tpyzq.mobile.pangu.activity.home.HomeFragment;
import com.tpyzq.mobile.pangu.activity.home.LovingHeartActivity;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.myself.account.AssetsAnalysisActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper.*;

/**
 * 常用工具类
 */
public class Helper {

    private static Helper mHelper;
    private static Toast mToast;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long towTime = 0;
    public static final String TAG = "Helper";


    /**
     * 广告跳转
     */
    public static void startActivity(Activity activity, String code, String url){
        startActivity(activity,code,url,null);
    }

    public static void startActivity(Activity activity, String code, String url, HomeFragment.JumpPageListener listener){
        String jump="",type="";
        switch (code){
            case "TN0000"://我的账户
                jump = MYACCOUNT;
                break;
            case "TN0001"://自选股
                jump = SELFCHOICESTOCK;
                break;
            case "TN0002"://搜索股票
                jump = SEARCHSTOCK;
                break;
            case "TN0003":// 新股
                jump = NEWSTOCK;
                break;
            case "TN0004":// 资产分析
                jump = APPROACH;
                break;
            case "TN0005"://稳赢理财
                jump = AMAZING;
                break;
            case "TN0006"://开户
                jump = OPEN_USER;
                break;
            case "TN0007 "://资讯
                jump = MESSAGE;
                break;
            case "TN0008"://交易动态
                jump = TRANSACTION;
                break;
            case "TN0009"://股市回忆录
                jump = MARKETMEMORY;
                break;
            case "TN0010"://股票买入
                jump = STOCKBUY;
                break;
            case "TN0011"://股票卖出
                jump = STOCKSELL;
                break;
            case "TN0012"://股票持仓
                jump = HOLDSTOCK;
                break;
            case "TN0013"://OTC持仓
                jump = OTCHOLD;
                break;
            case "TN0014"://        可取资金
                jump = ADVISABLEFUND;
                break;
            case "TN0015"://        银行转账
                jump = BANKTRANSFER;
                break;
            case "TN0016"://        业务办理
                jump = BUSINESSHANDLING;
                break;
            case "TN0017"://        自选股新闻
                jump = NEWSSELFCHOICE;
                break;
            case "TN0018"://        资金持仓
                jump = HOLDFUND;
                break;
            case "TN0019"://        我的消息
                jump= MYINFO;
                break;
            case "TN0020"://        股市月账单
                jump = STOCKBILL;
                break;
            case "TN0021"://        热搜股票
                jump = HOTSEARCH;
                break;
            case "TN0022"://        国债逆回购
                jump = REVERSEREPO;
                break;
            case "TN0023"://网页
                jump = url;
                type = "0";
                break;
        }
        if(activity.getClass().equals(IndexActivity.class)){
            if(!TextUtils.isEmpty(type)){
                Intent intent = new Intent(activity,LovingHeartActivity.class);
                intent.putExtra("jump",jump);
                activity.startActivity(intent);
            }else
                HomeFragmentHelper.getInstance().gotoPager(jump, activity, listener, null);
        }else {
            Intent intent = new Intent(activity,IndexActivity.class);
            intent.putExtra("jump",jump);
            if(!TextUtils.isEmpty(type))
                intent.putExtra("type",type);
            activity.startActivity(intent);
        }
    }

    public static int getTime(){
        int time;
        String timelogo = Db_PUB_USERS.searchRefreshTime();
        if(TextUtils.isEmpty(timelogo)){
            time = 3000;
            return time;
        }
        switch (timelogo){
            case "1":
                time =  1000;
                break;
            case "2":
                time =  2000;
                break;
            case  "3":
                time =  3000;
                break;
            default:
                time = 3000;
                break;
        }
        return time;
    }

    private Helper() {
    }

    public static Helper getInstance() {

        if (mHelper == null) {
            mHelper = new Helper();
        }

        return mHelper;
    }


    /**
     * 从Assets文件夹下读取文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @return
     */
    public String getStringFromAssets(Context context, String fileName) {
        final String ENCODING = "UTF-8";
        String strData = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            strData = EncodingUtils.getString(buffer, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strData;
    }

    /**
     * 显示tost
     *
     * @param context 上下文
     * @param content 内容
     */
    public void showToast(Context context, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, Toast.LENGTH_LONG);
            mToast.show();
            oldMsg = content;
            oneTime = System.currentTimeMillis();
        } else {
            towTime = System.currentTimeMillis();
            if (content.equals(oldMsg)) {
                if (towTime - oneTime > Toast.LENGTH_LONG) {
                    mToast.show();
                }
            } else {
                oldMsg = content;
                mToast.setText(content);
                mToast.show();
            }
        }
        oneTime = towTime;
    }

    /**
     * 展示tost
     *
     * @param context 上下文
     * @param resId   strings 里的资源
     */
    public void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue dp值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue px值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取手机的Imei号
     *
     * @param context 上下文
     * @return
     */
    public static String getImei(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyMgr.getDeviceId();
    }

    /**
     * 获取手机的Imsi号
     *
     * @param context 上下文
     * @return
     */
    public static String getImsi(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyMgr.getSubscriberId();
    }

    /**
     * 判断字符串是否为空
     *
     * @param key 传入的字符串
     * @return
     */
    public static String notNullStr(String key) {
        if (null == key) {
            return "";
        }

        return key;
    }

    /**
     * 添加单引号
     *
     * @param key
     * @return
     */
    public static String getSafeString(String key) {
        if (null == key || key.length() <= 0) {
            return "''";
        }

        return "'" + key + "'";
    }


    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
     *
     * @return
     */
    public static int getScreenViewTopHeight(View view) {
        return view.getTop();
    }

    /**
     * 获取屏幕中控件底部位置的高度--即控件底部的Y点
     *
     * @return
     */
    public static int getScreenViewBottomHeight(View view) {
        return view.getBottom();
    }

    /**
     * 获取屏幕中控件左侧的位置--即控件左侧的X点
     *
     * @return
     */
    public static int getScreenViewLeftHeight(View view) {
        return view.getLeft();
    }

    /**
     * 获取屏幕中控件右侧的位置--即控件右侧的X点
     *
     * @return
     */
    public static int getScreenViewRightHeight(View view) {
        return view.getRight();
    }


    //转换成亿或者万数据（字符串可为double类型long类型）
    public static String long2million(String s) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (!s.contains("E")) {
            if (!s.equals("")) {
                if (s.contains(" ")) {
                    return "0.00";
                } else {
                    double d = Double.parseDouble(s);
                    if (Math.abs(d) > 100000000) {
                        return df.format(d / 100000000) + "亿";
                    } else if (Math.abs(d) > 10000) {
                        return df.format(d / 10000) + "万";
                    } else {
                        return df.format(d);
                    }
                }
            } else {
                return "0.00";
            }
        } else {
            BigDecimal bd = new BigDecimal(s);
            String ss = bd.toPlainString();
            double d = Double.parseDouble(ss);
            if (Math.abs(d) > 100000000) {
                return df.format(d / 100000000) + "亿";
            } else if (Math.abs(d) > 10000) {
                return df.format(d / 10000) + "万";
            } else {
                return df.format(d);
            }
        }
    }


    //转换成亿或者万数据（字符串可为double类型long类型）
    public static String long2string(String s) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (!s.contains("E")) {
            if (!s.equals("")) {
                if (s.contains(" ")) {
                    return "0.00";
                } else {
                    double d = Double.parseDouble(s);
                    if (Math.abs(d / 100) > 100000000) {
                        return df.format(d / 100000000 / 100) + "亿";
                    } else if (Math.abs(d / 100) > 10000) {
                        return df.format(d / 10000 / 100) + "万";
                    } else {
                        return df.format(d / 100);
                    }
                }
            } else {
                return "0.00";
            }
        } else {
            BigDecimal bd = new BigDecimal(s);
            String ss = bd.toPlainString();
            double d = Double.parseDouble(ss);
            if (Math.abs(d / 100) > 100000000) {
                return df.format(d / 100000000 / 100) + "亿";
            } else if (Math.abs(d / 100) > 10000) {
                return df.format(d / 10000 / 100) + "万";
            } else {
                return df.format(d / 100);
            }
        }
    }

    public static String getStockNumber(String stockNumber) {

        if (TextUtils.isEmpty(stockNumber)) {
            return null;
        }

        String _stockNumber = stockNumber.substring(2, stockNumber.length());

        return _stockNumber;
    }


    public static boolean isNumeric(String str) {
        try {
            for (int i = 0; i < str.length(); i++) {
                System.out.println(str.charAt(i));
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 涨跌值
     *
     * @param close
     * @param price
     * @return
     */
    public static double getZdz(String close, String price) {

        double zdValue = 0f;

        if (!TextUtils.isEmpty(close) && !TextUtils.isEmpty(price) && Helper.isDecimal(close) && Helper.isDecimal(price)) {
            zdValue = Double.parseDouble(price) - Double.parseDouble(close);
        }
        return zdValue;
    }

    /**
     * 获取涨跌幅
     * @param a_price
     * @param a_close
     * @return
     */
    public static float getDiffPercent(String a_price,String a_close){
        float result = 0;
        try {
            int price = Math.round(Float.parseFloat(a_price)*1000);
            int close = Math.round(Float.parseFloat(a_close)*1000);
            if(close==0){
                return 0;
            }
            result = (float)(price-close)/close;
        }catch (Exception e){
        }
        return  result;
    }

    /**
     * 涨跌幅
     *
     * @param close
     * @param price
     * @return
     */
    public static double getZdf(String close, String price) {

        double zdf = 0f;
        if (!TextUtils.isEmpty(close) && !TextUtils.isEmpty(price) && Helper.isDecimal(close) && Helper.isDecimal(price)) {
            double dClose = Double.parseDouble(close);
            if (dClose == 0) {
                zdf = 0.0;
            } else {
                zdf = (Double.parseDouble(price) - dClose) / dClose;
            }

        }

        return zdf;
    }
    /**
     * 涨跌幅 和涨跌额
     *
     * @param close
     * @param price
     * @return
     */
    public static void getZdfAndzdz(StockInfoEntity stkInfo , String close, String price) {

        double zdf = 0f;
        double zdz = 0f;
        if (!TextUtils.isEmpty(close) && !TextUtils.isEmpty(price) && Helper.isDecimal(close) && Helper.isDecimal(price)) {
            double dClose = Double.parseDouble(close);
            if (dClose == 0) {
                zdf = 0f;
                zdz = 0f;
            } else {
                zdz = Double.parseDouble(price) - Double.parseDouble(close);
                zdf = zdz / dClose;
            }
        }
        stkInfo.setPriceChangeRatio(zdf);
        stkInfo.setUpAndDownValue(zdz);
    }
    /**
     * 涨跌幅 和涨跌额
     * @return
     */
    public static void getZdfAndzdz(StockInfoEntity stkInfo ) {
        String price = stkInfo.getNewPrice();
        String close = stkInfo.getClose();

        double zdf = 0f;
        double zdz = 0f;
        if (!TextUtils.isEmpty(close) && !TextUtils.isEmpty(price) && Helper.isDecimal(close) && Helper.isDecimal(price)) {
            double dClose = Double.parseDouble(close);
            if (dClose == 0) {
                zdf = 0f;
                zdz = 0f;
            } else {
                zdz = Double.parseDouble(price) - Double.parseDouble(close);
                zdf = zdz / dClose;
            }
        }
        stkInfo.setPriceChangeRatio(zdf);
        stkInfo.setUpAndDownValue(zdz);
    }
    /**
     * 新 6位转8位
     *
     * @return 出参: String股票代码
     * @content 获取股票代码
     * @date 2016年7月29日 下午2:01:44
     * @params 入参:股票代码，市场
     */
    public static String getStockCode(String code, String maker) {
        String code_2 = code.substring(0, 2);
        String code_3 = code.substring(0, 3);
        if (maker.equals("83")) {
            if (code_2.equals("60")) {
                return 11 + code; // 沪市A股

            } else if (code_3.equals("900")) {
                return 12 + code;// 沪市B股
            } else if (code_3.equals("019") || //
                    code_3.equals("122") || //
                    code_3.equals("130") || //
                    code_3.equals("126") || //
                    code_3.equals("110") || //
                    code_3.equals("113") ||//
                    code_3.equals("204")) {
                return 13 + code; // 沪市债券
            } else if (code_2.equals("51") || code_2.equals("50")) {
                return 15 + code; // 沪市基金
            } else if (code_2.equals("00")) {
                return 10 + code; // 沪市指数
            } else if (code_3.equals("204")) {
                return 17 + code;   // 逆回购
            } else {
                return 16 + code; // 其他
            }
        } else if (maker.equals("90")) {
            // 000xxx、002xxx、300xxx
            if (code_2.equals("20")) {
                return 22 + code; // 深市B股
            } else if (code_2.equals("39")) {
                return 20 + code; // 深市指数
            } else if (code_3.equals("300")) {
                return 26 + code; // 深市创业板
            } else if (code_3.equals("002")) {
                return 27 + code; // 深市中小板
            } else if (code_2.equals("15") || code_2.equals("16") || code_3.equals("118")) {
                return 24 + code; // 深市基金
            } else if (code_2.equals("10") || code_3.equals("111") || code_3.equals("112") || code_3.equals("115") || code_3.equals("12") || code_3.equals("131")) {
                return 23 + code; // 深市债券
            } else if (code_2.equals("00")) {
                return 21 + code;// 深市A股
            } else if (code_3.equals("002")) {
                return 29 + code;// 逆回购
            }else {
                return 28 + code;
            }
        }
        return null;
    }


    /**
     * 根据股票代码选择该股票的的类型
     * <p>
     * 100.深市指数
     * 200.深市A股
     * 300.深市B股
     * 400.深市债券
     * 500.深市基金
     * 600.深市创业板
     * 700.深市中小板
     * 800.深市其他
     * <p>
     * 900.沪市指数
     * 1000.沪市A股
     * 1100.沪市B股
     * 1200.沪市债券
     * 1300.沪市基金
     * 1400.其他
     *
     * @param stockNumber
     * @return
     */
    public static int switchStockType(String stockNumber) {

        int falg = -1;

        String firstNo = stockNumber.substring(0, 1);
        String second = stockNumber.substring(1, 2);

        if ("2".equals(firstNo) && "0".equals(second)) {
            falg = 100;
        } else if ("2".equals(firstNo) && "1".equals(second)) {
            falg = 200;
        } else if ("2".equals(firstNo) && "2".equals(second)) {
            falg = 300;
        } else if ("2".equals(firstNo) && "3".equals(second)) {
            falg = 400;
        } else if ("2".equals(firstNo) && "4".equals(second)) {
            falg = 500;
        } else if ("2".equals(firstNo) && "6".equals(second)) {
            falg = 600;
        } else if ("2".equals(firstNo) && "7".equals(second)) {
            falg = 700;
        } else if ("2".equals(firstNo) && "8".equals(second)) {
            falg = 800;
        } else if ("1".equals(firstNo) && "0".equals(second)) {
            falg = 900;
        } else if ("1".equals(firstNo) && "1".equals(second)) {
            falg = 1000;
        } else if ("1".equals(firstNo) && "2".equals(second)) {
            falg = 1100;
        } else if ("1".equals(firstNo) && "3".equals(second)) {
            falg = 1200;
        } else if ("1".equals(firstNo) && "4".equals(second)) {
            falg = 1300;
        } else if ("1".equals(firstNo) && "6".equals(second)) {
            falg = 1400;
        }

        return falg;
    }

    /**
     * 获取银行账户
     *
     * @param accountNumber
     * @return
     */
    public static String getBanksAccountNumber(String accountNumber) {

        int length = accountNumber.length();

        if (length < 4) {
            return accountNumber;
        }

        accountNumber = accountNumber.substring(length - 4, length);

        return "**** " + accountNumber;
    }


    /**
     * 手机号码  前俩位
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * \
     * 密码效验
     *
     * @param text
     * @return
     */
    public static int weakPwdCheck(String text) {
        //匹配6位顺增
        String incre = "(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}\\d";
        //匹配6位顺降
        String decre = "(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5}\\d";
        //匹配6位顺增或顺降
        String in_de = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5})\\d";
        //匹配3位以上的重复数字
        String same3 = "([\\d])\\1{2,}";
        //匹配223355类型
        String two = "([\\d])\\1{1,}([\\d])\\2{1,}([\\d])\\3{1,}";

        if (text.matches(incre) || text.matches(decre) || text.matches(in_de)) {
            //连续相同的数字
            return 1;
        } else if (text.matches(same3) || text.matches(two)) {
            //连续的数字 连续的数字
            return 2;
        } else {
            //通过弱密码测试，正常密码
            return 0;
        }
    }

    /**
     * 验证  AAABBB 类型
     *
     * @param text
     * @return
     */
    public static boolean isContinuous(String text) {
        String pattern = "^.*(\\d)(?=\\1{2}(\\d)(?=\\2{2})).*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        System.out.println(m.matches());
        return m.matches();
    }

    /**
     * 验证邮政编码
     *
     * @param post
     * @return
     */
    public static boolean checkPost(String post) {
        return post.matches("[0-9]\\d{5}(?!\\d)");
    }

    /**
     * base64字符串转Bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static File getRootExternalDir(Context context) {

        if (!isSDcardExsit()) {
            CentreToast.showText(context, "无SDCard");
            LogHelper.e(TAG, "sdcard not exist");
            return null;
        }

        final String cacheDir = "/Pangu/";
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + cacheDir);
    }

    /**
     * 获取程序外部(sd)的目录
     *
     * @param context
     * @return
     */
    public static File getExternalDir(Context context, String dirName) {
        return new File(getAppFileDirPath(context) + dirName + "/");
    }

    /**
     * 获取app的父类文件夹
     *
     * @return
     */
    public static String getAppFileDirPath(Context context) {

        if (!isSDcardExsit()) {
            CentreToast.showText(context, "无SDCard");
            LogHelper.e(TAG, "sdcard not exist");
            return null;
        }

        String cacheDir = "/Pangu/" + CustomApplication.getContext().getPackageName() + "/";
        return Environment.getExternalStorageDirectory().getAbsolutePath() + cacheDir;
    }

    public static String getExternalDirPath(Context context, String dirName, String fileName) {

        File file = getExternalFile(context, dirName, fileName);
        return file.getPath();
    }

    /**
     * 判断sdcard是否存在
     *
     * @return
     */
    public static boolean isSDcardExsit() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static File getExternalFile(Context context, String fileDir, String fileName) {

        if (!isSDcardExsit()) {
            CentreToast.showText(context, "无SDCard");
            LogHelper.e(TAG, "sdcard not exist");
            return null;
        }

        File file = null;
        try {
            File _fileDir = getExternalDir(context, fileDir);
            if (!_fileDir.exists()) {
                _fileDir.mkdirs();
            }

            file = new File(_fileDir, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.e(TAG, e.toString());
        }

        return file;
    }

    /**
     * 获取某文件夹
     *
     * @param context
     * @param fileDir
     * @return
     */
    public static File getExternalFileDir(Context context, String fileDir) {
        if (!isSDcardExsit()) {
            CentreToast.showText(context, "无SDCard");
            LogHelper.e(TAG, "sdcard not exist");
            return null;
        }

        File _fileDir = null;
        try {
            _fileDir = getExternalDir(context, fileDir);
            if (!_fileDir.exists()) {
                _fileDir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.e(TAG, e.toString());
        }

        return _fileDir;

    }

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public static boolean isNetWorked() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CustomApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        if (networkInfo != null && networkInfo.length > 0) {
            for (int i = 0; i < networkInfo.length; i++) {
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

//
    /**
     * 格式化HHmmss类型的时间
     */
    public static String formateDate(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String strDate = "";
        try {
            while(time.length() < 6){
                time = "0"+time;
            }
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            strDate = "--";
            e.printStackTrace();
        }

        return strDate;
    }

    /**
     * 时间格式yyyymmdd转 yyyy-mm-dd
     *
     * @param time
     * @return
     */
    public static String formateDate1(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strDate = "";
        try {
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }

    /**
     * 时间格式yyyy-mm-dd转 yyyy-mm-dd
     *
     * @param time
     * @return
     */
    public static String formateDate2(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = "";
        try {
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }


    /**
     * 时间格式yyyymmdd hh:mm:ss转 yyyy.mm.dd
     *
     * @param time
     * @return
     */
    public static String formateDate3(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strDate = "";
        try {
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }


    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static void getAssestFile(String dir, String fileName) {

        File file = ConstantUtil.saveFile(CustomApplication.getContext(), dir, fileName);

        if (file == null) {
            return;
        }


        InputStream is = null;
        FileOutputStream fos = null;
        try {
            AssetManager am = CustomApplication.getContext().getAssets();
            is = am.open(fileName);
            fos = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int readLen = 0;
            while (readLen != -1) {
                readLen = is.read(buf, 0, 1024);
                if (readLen > 0) {
                    fos.write(buf, 0, readLen);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static Bitmap getAssetBitmap(String fileName){
        Bitmap bitmap = null;
        try {
            AssetManager am = CustomApplication.getContext().getAssets();
            InputStream is = am.open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 时间戳转时间
     *
     * @param timec
     * @return
     */
    public static String getTimeByTimeC(String timec) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(Long.parseLong(String.valueOf(timec))));
        return date;
    }

    /**
     * 获取系统日期
     *
     * @return
     */
    public static String getCurDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = simpledateformat.format(calendar.getTime());
        return strDate;
    }

    /**
     * 两个时间比较大小 取大
     */
    public static String compareTo(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        //获取Calendar实例
        Calendar currentTime = Calendar.getInstance();
        Calendar compareTime = Calendar.getInstance();
        try {
            //把字符串转成日期类型
            currentTime.setTime(df.parse(startDate));
            compareTime.setTime(df.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //利用Calendar的方法比较大小
        if (currentTime.compareTo(compareTime) > 0) {

            return startDate;
        } else {
            return endDate;
        }
    }


    /**
     * 两个时间比较大小
     * 起始时间等于结束时间返回0
     * 起始时间大于结束时间返回1
     * 起始时间小于结束时间返回2
     */
    public static int compareToDate(String startDate, String endDate) {
        int num = -1;
        //获取Calendar实例
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentTime = Calendar.getInstance();
        Calendar compareTime = Calendar.getInstance();
        Date df_startDate = null;
        Date df_endDate = null;
        try {
            //把字符串转成日期类型
            df_startDate = df.parse(startDate);
            df_endDate = df.parse(endDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            //把字符串转成日期类型
            df_startDate = df1.parse(startDate);
            df_endDate = df1.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentTime.setTime(df_startDate);
        compareTime.setTime(df_endDate);
        //利用Calendar的方法比较大小
        if (currentTime.compareTo(compareTime) > 0) {
            num = 1;
        } else if (currentTime.compareTo(compareTime) == 0) {
            num = 0;
        } else {
            num = 2;
        }
        return num;
    }

    /**
     * 获取当前日期下一年的前一天
     * @param date
     * @return
     */
    public static String getNextYear(Date date,String dateStyle) {
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(date);
        ca.add(Calendar.YEAR, 1); // 年份加1
//        ca.add(Calendar.MONTH, -1);// 月份减1
        ca.add(Calendar.DATE, -1);// 日期减1
        Date resultDate = ca.getTime(); // 结果
        SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);
        return sdf.format(resultDate);
    }


    /**
     * 获取当前日期下一天
     * @param date
     * @return
     */
    public static String getNextDate(Date date,String dateStyle) {
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(date);
//        ca.add(Calendar.YEAR, 1); // 年份加1
//        ca.add(Calendar.MONTH, -1);// 月份减1
        ca.add(Calendar.DATE, 1);// 日期减1
        Date resultDate = ca.getTime(); // 结果
        SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);
        return sdf.format(resultDate);
    }

    /**
     * 获取当前日期的前一天
     * @return
     */
    public static Date getBeforeDate(){
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(new Date());
        ca.add(Calendar.DATE,-1);
        return ca.getTime();
    }

    /**
     * 获取当前日期的前一天
     * @return
     */
    public static String getBeforeString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(getBeforeDate());
    }

    /**
     * 转换日期格式(yyyyMMdd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDate(String str) {
        return StringToDate(str, "yyyy-MM-dd", "yyyyMMdd");
    }

    public static String getProTime(String str) {
        String defaultFromate = "yyyy-MM-dd";
        if (Helper.isDateFromatYYYY_MM_dd(str)) {
            defaultFromate = "yyyy-MM-dd";
        }

        if (Helper.isDateFromatYYYYMMdd(str)) {
            defaultFromate = "yyyyMMdd";
        }

        return StringToDate(str, defaultFromate, "MM-dd");
    }

    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        String News = dateStr;
        try {
            Date date = null;
            DateFormat sdf = new SimpleDateFormat(dateFormatStr);
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            News = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return News;
    }


    /**
     * 转换日期格式(yyyyMMdd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDate2(String str) {
        return StringToDate2(str, "yyyy-MM-dd", "yyyyMMdd");
    }

    public static String getProTime2(String str) {
        return StringToDate2(str, "yyyy-MM-dd", "MM月dd日");
    }

    public static String StringToDate2(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }


    /**
     * 转换日期格式(yyyy.MM.dd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDateYMD(String str) {
        return StringToDateYMD(str, "yyyyMMdd", "yyyy.MM.dd");
    }

    public static String StringToDateYMD(String dateStr, String dateFormatStr, String formatStr) {
       String nDate = dateStr ;
        Date date = null;
        try {
            DateFormat sdf = new SimpleDateFormat(dateFormatStr);
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            nDate = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nDate ;
    }

    /**
     * 转换日期格式(yyyy-MM-dd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDateY_M_D(String str) {
        return StringToDateY_M_D(str, "yyyyMMdd", "yyyy-MM-dd");
    }

    public static boolean isDateFromatYYYY_MM_dd(String strDate) {
        String reglx = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
        return isMatch(reglx, strDate);
    }

    public static boolean isDateFromatYYYYMMdd(String strDate) {
        String reglx = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";
        return isMatch(reglx, strDate);
    }

    public static String StringToDateY_M_D(String dateStr, String dateFormatStr, String formatStr) {
       String nDate = dateStr ;
        Date date = null;
        try {
            DateFormat sdf = new SimpleDateFormat(dateFormatStr);
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            nDate = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nDate ;
    }


    /**
     * 转换时间格式(HH:mm:ss)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDateHMS(String str) {
        if (!TextUtils.isEmpty(str) && str.length() == 5) {
            str = "0" + str;
        } else if (TextUtils.isEmpty(str)) {
            return "";
        }
        return StringToDateYMD(str, "HHmmss", "HH:mm:ss");
    }

    public static String StringToDateHMS(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    /**
     * yyyyMMdd 格式获取当前年
     *
     * @param dateStr
     * @return
     */
    public static int getYEAR(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        int year = 0;
        try {
            date = sdf.parse(dateStr);

            Calendar now = Calendar.getInstance();
            now.setTime(date);
            now.setTime(date);

            year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1; // 0-based!
            int day = now.get(Calendar.DAY_OF_MONTH);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return year;
    }

    /**
     * 获取2个日期之间周六，周日的天数
     *
     * @param startDate
     * @param endDate
     * @param
     * @return
     */
    public static int getSundayNum(String startDate, String endDate) {
        int num = 0;//周六，周日的总天数

        if ("00000000".equals(startDate) || endDate.equals("00000000")) {
            return 0;
        }


        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


        List yearMonthDayList = new ArrayList();
        Date start = null, stop = null;

        String format = "";

        try {

            try {
                start = sdf.parse(startDate);
                stop = sdf.parse(endDate);
                format = "yyyyMMdd";
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                start = sdf1.parse(startDate);
                stop = sdf1.parse(endDate);
                format = "yyyy-MM-dd";
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (start.after(stop)) {
                Date tmp = start;
                start = stop;
                stop = tmp;
            }
            //将起止时间中的所有时间加到List中
            Calendar calendarTemp = Calendar.getInstance();
            calendarTemp.setTime(start);

            while (calendarTemp.getTime().getTime() <= stop.getTime()) {
                yearMonthDayList.add(new SimpleDateFormat(format)
                        .format(calendarTemp.getTime()));
                calendarTemp.add(Calendar.DAY_OF_YEAR, 1);
            }

            Collections.sort(yearMonthDayList);

            int size = yearMonthDayList.size();
            int week = 0;

            for (int i = 0; i < size; i++) {
                String day = (String) yearMonthDayList.get(i);
                System.out.println(day);

                week = getWeek(day, format);

                if (week == 6 || week == 0) {//周六，周日
                    num++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return num;
    }



    /**
     * 获取某个日期是星期几
     *
     * @param date
     * @param format
     * @return 0-星期日
     * @author zhaigx
     * @date 2013-3-13
     */
    public static int getWeek(String date, String format) {
        Calendar calendarTemp = Calendar.getInstance();
        try {
            calendarTemp.setTime(new SimpleDateFormat(format).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = calendarTemp.get(Calendar.DAY_OF_WEEK);
        int value = i - 1;//0-星期日
        //        System.out.println(value);
        return value;
    }

    /**
     * 两个日期间差了多少天
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 隐藏键盘
     *
     * @param ed
     * @param activity
     */
    public static void hideSoftInputMethod(EditText ed, Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static boolean hideKeyboard(Activity activity, EditText editText, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive(editText)) {//因为是在fragment下，所以用了getView()获取view，也可以用findViewById（）来获取父控件
            view.requestFocus();//使其它view获取焦点.这里因为是在fragment下,所以便用了getView(),可以指定任意其它view
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }


    public static void hideSystemInputSoft(Activity activity, EditText editText) {


        if (activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {

            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//            InputMethodManager inputMethodManager=(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static void showSystemInputSoft(EditText ed) {
        ed.requestFocus();
        InputMethodManager imm = (InputMethodManager) ed.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static DecimalFormat fromMateByPersent() {
        DecimalFormat format = new DecimalFormat("#0.00%");
        return format;
    }



//    public static void sendUpdateSelfChoiceBrodcast(Context context, String stockNumber) {
//        Intent intent = new Intent();
//        intent.setAction("com.tpyzq.self.mobile.pangu.modle.market.selfChoice.childNews.action");
//        if (!TextUtils.isEmpty(stockNumber)) {
//            intent.putExtra("stockNumber", stockNumber);
//        }
//        intent.putExtra("doAction", "updateSelfChoiceData");
//        context.sendBroadcast(intent);
//    }

    /**
     * 银行卡号的显示
     *
     * @param bankNo
     * @return
     */
    public static String transferBanksNumber(String bankNo) {

        if (TextUtils.isEmpty(bankNo)) {
            return "";
        }

        String bankNumber = "";

        char[] charNum = bankNo.toCharArray();

        if (charNum.length >= 4) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < charNum.length; i++) {
                if (i >= charNum.length - 4) {
                    sb.append(charNum[i]);
                } else {

                    if ((i + 1) % 4 == 0) {
                        sb.append("* ");
                    } else {
                        sb.append("*");
                    }

                }
            }

            bankNumber = sb.toString();

        } else {
            bankNumber = bankNo;
        }

        return bankNumber;

    }

    /**
     * 判断字符串是不是数字
     *
     * @return
     */
    public static boolean isNumberDimc(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isNumbers(String Numbers) {
        boolean isNum = Numbers.matches("[0-9]+");
        return isNum;

    }

    /**
     * 判断正整数，对于正整数而言，可以带+号，第一个数字不能为0
     *
     * @param regex
     * @param orginal
     * @return
     */
    public static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    /**
     * 判断负正数，对于负整数而言，必须带负号，第一个数字也不能为0
     *
     * @param orginal
     * @return
     */
    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
    }

    /**
     * 判断是否是整数，对于整数而言，实际是由0，正整数和负整数组成的，所以偷个懒用前两个方法一起判断
     *
     * @param orginal
     * @return
     */
    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*", orginal);
    }

    /**
     * 判断正小数而言，可以考带+号，并考虑两种情况，第一个数字为0和第一个数字不为0，第一个数字为0时，则小数点后面应该不为0，第一个数字不为0时，小数点后可以为任意数字
     *
     * @param orginal
     * @return
     */
    public static boolean isWholeNumber(String orginal) {
        return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    /**
     * 判断是否是负小数，负小数而言，必须带负号，其余都同上
     *
     * @param orginal
     * @return
     */
    public static boolean isPositiveDecimal(String orginal) {
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 判断是否是小数，对于小数，可以带正负号，并且带小数点就行了，但是至少保证小数点有一边不为空，所以这里还是分左边不为空和右边不为空的情况
     *
     * @param orginal
     * @return
     */
    public static boolean isNegativeDecimal(String orginal) {
        return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 实数比较简单，，要么是整数，要么是小数
     *
     * @param orginal
     * @return
     */
    public static boolean isDecimal(String orginal) {
        boolean falg = isMatch("^[-+]?[0-9]+(\\.[0-9]+)?$", orginal);
        return falg;
    }


    /**
     * 判断是不是科学计数法
     *
     * @param input
     * @return
     */
    public static boolean isENum(String input) {//判断输入字符串是否为科学计数法
        String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";//科学计数法正则表达式
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(input).matches();
    }

    /**
     * 科学计数法转普通
     *
     * @param num
     * @return
     */
    public static String ENumToNomarl(String num) {

        String str = num;
        if (isENum(num)) {
            BigDecimal bd = new BigDecimal(num);
            str = bd.toPlainString();
        }

        return str;

    }

    /**
     * 判断是不是百分数
     *
     * @param input
     * @return
     */
    public static boolean isPersent(String input) {
        String str = "^\\d+\\.?\\d*\\%?$";
        Pattern pattern = Pattern.compile(str);
        return pattern.matcher(input).matches();
    }

    public static boolean isRealNumber(String orginal) {
        return isWholeNumber(orginal) || isDecimal(orginal);
    }

    /**
     * 百分比转小数
     *
     * @return
     */
    public static Number persentToNumber(String persent) {

        try {

            if (isPersent(persent)) {
                NumberFormat nf = NumberFormat.getPercentInstance();
                Number m = nf.parse(persent);
                return m;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断当前activity是否在运行
     *
     * @param mContext
     * @param activityClassName
     * @return
     */
    public static boolean isActivityRunning(Activity mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            String classname = component.getClassName();
            if (classname.equals(activityClassName)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 判断是否是合法的url
     *
     * @param urlString
     * @return
     */
    public static boolean isUrl(String urlString) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        boolean isMatch = matcher.matches();
        if (!isMatch) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isGoToActivity(Activity activity, CancelDialog.PositiveClickListener listener) {
        return true;
        /*boolean flag = false;
        String corpResult = SpUtils.getString(activity,"corpResult","");
        if (TextUtils.isEmpty(corpResult)) {
            CancelDialog.cancleDialog(activity,"",4000,null);
        } else {
            String[] args = corpResult.split("--");
            if ("2".equalsIgnoreCase(args[0])||"3".equalsIgnoreCase(args[0])) {
                CancelDialog.cancleDialog(activity,"",4000,null);
            } else {
                flag = true;
            }
        }
        return flag;*/
    }

    //是否要弹适用性弹框
    public static boolean isNeedShowRiskDialog() {
        boolean flag = false;
        String IS_OVERDUE = SpUtils.getString(CustomApplication.getContext(),"IS_OVERDUE","");
        if ("2".equalsIgnoreCase(IS_OVERDUE) || "3".equalsIgnoreCase(IS_OVERDUE)) {
            flag = true;
        }
        return flag;
    }

    //弹出适用性弹框
    public static void showCorpDialog(final Activity activity,CancelDialog.PositiveClickListener listener,CancelDialog.NagtiveClickListener nagtiveClickListener) {
        int style = 2000;
        String IS_OVERDUE = SpUtils.getString(CustomApplication.getContext(),"IS_OVERDUE","");
        String CORP_END_DATE = SpUtils.getString(CustomApplication.getContext(),"CORP_END_DATE","");;

        if ("2".equalsIgnoreCase(IS_OVERDUE)) {
            //过期
            style = 2000;
        } else {//3的情况  未做
            style = 3000;
        }
        CancelDialog.cancleDialog(activity, CORP_END_DATE, style, listener,nagtiveClickListener);
    }

    /**
     * 此 dialog 效果从底部弹出，可以添加任意多个Item
     * @param context
     * @param mWidth   需要设置的dialog的宽度
     * @param listener 选择Item会调用此监听
     * @param contents 任意多个Item选项内容（从最顶端到最低端）
     * @return
     */
    public static Dialog showItemSelectDialog(Context context
            , int mWidth
            , final OnItemSelectedListener listener
            , boolean isShowCancel, final String... contents) {
        final Dialog mDialog = new Dialog(context, R.style.Theme_Dialog_From_Bottom);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_choice, null);
        LinearLayout contentsView = (LinearLayout) rootView.findViewById(R.id.dialogContent);
        for (int i = 0; i < contents.length; i++) {
            if (i == 0) {
                View topView = LayoutInflater.from(context).inflate(R.layout.dialog_top_item, null);
                TextView topText = (TextView) topView.findViewById(R.id.dialog_top);
                topText.setText(contents[0]);
                topText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        if (listener != null)
                            listener.getSelectedItem(contents[0]);
                    }
                });
                contentsView.addView(topView);
            } else if (i == contents.length - 1) {
                View bottomView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_item, null);
                TextView boottomTv = (TextView) bottomView.findViewById(R.id.dialog_bottom);
                boottomTv.setText(contents[contents.length - 1]);
                boottomTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDialog.dismiss();
                        if (listener != null)
                            listener.getSelectedItem(contents[contents.length - 1]);
                    }
                });
                contentsView.addView(bottomView);
            } else {
                View centerView = LayoutInflater.from(context).inflate(R.layout.dialog_center_item, null);
                TextView centTv = (TextView) centerView.findViewById(R.id.dialog_center_item);
                final int finalI = i;
                centTv.setText(contents[finalI]);
                centTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDialog.dismiss();
                        if (listener != null)
                            listener.getSelectedItem(contents[finalI]);
                    }
                });
                contentsView.addView(centerView);
            }
        }
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        if(!isShowCancel)
            rootView.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
        mDialog.setContentView(rootView);
        mDialog.setCanceledOnTouchOutside(true);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = mWidth;
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawableResource(R.color.white);
        window.setAttributes(params);
        mDialog.show();
        return mDialog;
    }

    public interface OnItemSelectedListener {
        void getSelectedItem(String content);
    }

}
