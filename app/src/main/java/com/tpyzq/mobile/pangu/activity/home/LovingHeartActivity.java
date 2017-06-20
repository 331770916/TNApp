package com.tpyzq.mobile.pangu.activity.home;

import android.view.View;
import android.webkit.WebView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.dialog.SaveImageDialog;
import com.tpyzq.mobile.pangu.view.webview.ProgressWebView;


/**
 * Created by wangqi on 2017/4/1.
 * 爱心暖洋洋
 */

public class LovingHeartActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {


    @Override
    public void initView() {
        ProgressWebView mwebView = (ProgressWebView) findViewById(R.id.mwebView);
        WebView mwebView2 = (WebView) findViewById(R.id.mwebView2);

        String str = "file:///android_asset/lovingheartwild.png";
        String str1 = "file:///android_asset/2Dbarcode.png";
        mwebView.loadUrl(str);
        mwebView.getSettings().setUseWideViewPort(true);
        mwebView.getSettings().setLoadWithOverviewMode(true);

        mwebView2.loadUrl(str1);
        mwebView2.getSettings().setUseWideViewPort(true);
        mwebView2.getSettings().setLoadWithOverviewMode(true);


        findViewById(R.id.iv_back).setOnClickListener(this);
        mwebView2.setOnLongClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lovingheart;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }



    @Override
    public boolean onLongClick(View v) {
        SaveImageDialog dialog = new SaveImageDialog(this);
        dialog.show();
        return false;
    }




//    private void WEIXIN() {
//        String appid = "wxd930ea5d5a258f4f";//开发者平台ID
//        IWXAPI api = WXAPIFactory.createWXAPI(this, appid, false);
//        if (api.isWXAppInstalled()) {
//            JumpToBizProfile.Req req = new JumpToBizProfile.Req();
//            req.toUserName = "gh_4e4d759a27f0"; // 公众号原始ID
//            req.extMsg = "";
//            req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE; // 普通公众号
//            api.sendReq(req);
//        } else {
//            Helper.getInstance().showToast(this, "微信未安装");
//        }
//    }

}
