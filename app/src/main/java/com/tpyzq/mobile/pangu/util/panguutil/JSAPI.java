package com.tpyzq.mobile.pangu.util.panguutil;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.tpyzq.mobile.pangu.activity.myself.account.UsableCapitalActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TakeAPositionActivity;

import java.io.File;

public class JSAPI {

    private static JSAPI mjs;
    private Activity mActivity;
    private Handler mHandler;
    private File mCameraFile;
    private String mMoney;
    private String mDiscrib;
    private int pixNum;
    private int action;//无用Web端 javaScript需要
    private Intent intent;

    public static JSAPI getInctance() {
        if (mjs == null) {
            mjs = new JSAPI();
        }
        return mjs;
    }

    private JSAPI() {
    }

    public void setActivity(Activity activity) {

        if (activity != null) {
            mActivity = activity;
        }

    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    @JavascriptInterface
    public void setFileChooser(int action) { // 设置"选择文件"的动作
        this.action = action;
    }

    @JavascriptInterface
    public void stockDetial() {
        Intent intent = new Intent();
        intent = new Intent();
        intent.setClass(mActivity, TakeAPositionActivity.class);
        mActivity.startActivity(intent);
    }

    @JavascriptInterface
    public void fundDetial() {
        intent = new Intent();
        intent.setClass(mActivity, FundShareActivity.class);
        mActivity.startActivity(intent);
    }

    @JavascriptInterface
    public void manageDetial() {
        intent = new Intent();
        intent.setClass(mActivity, OTC_ShareActivity.class);
        mActivity.startActivity(intent);
    }

    @JavascriptInterface
    public void crashDetial() {
        intent = new Intent();
        intent.putExtra("money", getMoney());
        intent.setClass(mActivity, UsableCapitalActivity.class);
        mActivity.startActivity(intent);
    }

    public String getmDiscrib() {
        return mDiscrib;
    }

    public void setmDiscrib(String mDiscrib) {
        this.mDiscrib = mDiscrib;
    }

    public int getPixNum() {
        return pixNum;
    }

    public void setPixNum(int pixNum) {
        this.pixNum = pixNum;
    }

    public File getmCameraFile() {
        return mCameraFile;
    }

    public void setmCameraFile(File mCameraFile) {
        this.mCameraFile = mCameraFile;
    }

    public void setMoney(String money) {
        this.mMoney = money;
    }

    public String getMoney() {
        return mMoney;
    }

}
