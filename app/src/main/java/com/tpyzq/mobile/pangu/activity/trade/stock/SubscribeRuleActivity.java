package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * 新股申购规则 界面
 */
public class SubscribeRuleActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivSubscribeRule_back=null;
    private WebView SubscribeRule=null;
    @Override
    public void initView() {
        this.ivSubscribeRule_back= (ImageView) this.findViewById(R.id.ivSubscribeRule_back);
        this.ivSubscribeRule_back.setOnClickListener(this);
        this.SubscribeRule= (WebView) this.findViewById(R.id.wvSubscribeRule);
        this.SubscribeRule.getSettings().setJavaScriptEnabled(true);
        this.SubscribeRule.loadUrl("file:///android_asset/trade.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && SubscribeRule.canGoBack()){
            SubscribeRule.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_subscribe_rule;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSubscribeRule_back:
                this.finish();
                break;
        }
    }
}
