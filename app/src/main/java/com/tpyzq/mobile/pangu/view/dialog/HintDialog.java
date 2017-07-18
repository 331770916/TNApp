package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialogCenter;


/**
 * Created by 陈新宇 on 2016/12/8.
 * 公告弹窗
 */

public class HintDialog extends BaseDialogCenter implements View.OnClickListener {
    private TextView tv_title,tv_info;
    private Button but_yes;
    private String mtoken_Inform;
    private String mInform_push_time;
    private WebView wv;

    public HintDialog(Context context, String token_Inform, String inform_push_time) {
        super(context);
        mtoken_Inform = token_Inform;
        mInform_push_time=inform_push_time;
    }


    @Override
    public void setView() {
        /*tv_title = (TextView) findViewById(R.id.tv_title);
        tv_info = (TextView) findViewById(R.id.tv_info);


        but_yes = (Button) findViewById(R.id.but_yes);
        tv_title.setText(mtoken_Inform);
        tv_info.setText(mInform_push_time);*/
        tv_title = (TextView) findViewById(R.id.tv_title);
        but_yes = (Button) findViewById(R.id.but_yes);
        wv = (WebView) findViewById(R.id.wv);
        tv_title.setText(mtoken_Inform);
        wv.loadData(mInform_push_time, "text/html; charset=UTF-8", null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_hint;
    }

    @Override
    public void initData() {
        but_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_yes:
                dismiss();
                break;
        }
    }


}
