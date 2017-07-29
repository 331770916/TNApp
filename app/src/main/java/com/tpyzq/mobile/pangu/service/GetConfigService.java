package com.tpyzq.mobile.pangu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;

import java.util.HashMap;

/**
 * 获取站点列表
 * Created by lx 2017/7/28.
 */

public class GetConfigService extends Service implements InterfaceCollection.InterfaceCallback {
    private static String TAG_REQUEST_CURRENT = "10001";
    private static String TAG_REQUEST_OTHER = "10002";
    private HashMap hashMap;
    private int maxRequstCount = 0;//最大可请求数，根据几个站点决定
    private int count = 0;//当前请求数
    private String[] hqs; //地址集合

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doConnect() throws InterruptedException {
        //首先使用本地选择站点请求站点列表 如果成功继续执行业务，
        // 如果失败，调用其它站点获取站点信息找到可用站点弹框提示并切换，如果都不可用，弹框提示
        if (Helper.isNetWorked()) {
            InterfaceCollection.getInstance().getSites(ConstantUtil.currentUrl + ConstantUtil.GET_SITES, TAG_REQUEST_CURRENT, this);
        } else {
            Thread.sleep(1000);
            if (count <= 2) {
                doConnect();
                count++;
            } else {
                stopSelf();
            }
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        if (TAG_REQUEST_CURRENT.equalsIgnoreCase(info.getTag())) {//使用本地站点请求
            if ("0".equalsIgnoreCase(info.getCode())) {
                stopSelf();
            } else {
                String url = getUrl();
                InterfaceCollection.getInstance().getSites(url+ConstantUtil.GET_SITES,TAG_REQUEST_OTHER,this);
            }
        } else if (TAG_REQUEST_OTHER.equalsIgnoreCase(info.getTag())){
            if ("0".equalsIgnoreCase(info.getCode())){
                //当前站点好用，获取当前站点名称和url
//                String registerUrl = (String)hashMap.get("register");
                HashMap resultMap = (HashMap)info.getData();
                String[] tradeArr = (String[])resultMap.get("trade");
                String[] hqArr = (String[])resultMap.get("hq");
                if (hqArr.length == 0){
                    //都不可用
                    sendAllErrorBrocast();
                } else if (hqArr.length == 1){
                    ConstantUtil.currentUrl = getUrl();
                    //只有一个可用
                    SpUtils.putString(CustomApplication.getContext(), "market_ip", hqArr[0].split("\\|")[1]);
                    ConstantUtil.IP = hqArr[0].split("\\|")[1];
                    // 判断交易用哪个
                   if (tradeArr.length == 1){
                        //只有一个可用
                        SpUtils.putString(CustomApplication.getContext(), "jy_ip", tradeArr[0].split("\\|")[1]);
                        ConstantUtil.SJYZM = tradeArr[0].split("\\|")[1];
                    }
                } else if (hqArr.length == 2){
                    //都可用不变
                }
            } else {
                sendAllErrorBrocast();
            }
        }
    }

    private String getUrl() {
        String url;
        if (ConstantUtil.currentUrl.equalsIgnoreCase(ConstantUtil.bjUrl)) {
            url = ConstantUtil.kmUrl;
        } else {
            url = ConstantUtil.bjUrl;
        }
        return url;
    }

    private void sendAllErrorBrocast() {
        //弹框提示
        Intent mIntent=new Intent();
        mIntent.putExtra("type", "2");
        mIntent.setAction("com.pangu.showdialog");
        sendBroadcast(mIntent);
    }
}
