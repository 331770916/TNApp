package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/20.
 * 身份证有效期修改
 */

public class UpdateIdCodeValidityActivity extends BaseActivity implements DialogInterface.OnCancelListener, View.OnClickListener {

    private final String TAG = UpdateIdCodeValidityActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("身份证有效期修改");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();

        String isRest = getIntent().getStringExtra("isRest");
        isRest = TextUtils.isEmpty(isRest) ? "0" :"1";
        String token = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        getUpdateIdCodeInfo(isRest, token);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_idcode;
    }

    /**  网络连接 **/

    /**
     * 获取身份证有效期更新的信息
     * @param isReset  是否重置的标识
     * @param token    session
     */
    private void getUpdateIdCodeInfo(String isReset, String token) {
        Map map = new HashMap();
        map.put("code", "WTLogin");
        Map<String,String> params = new HashMap<>();
        params.put("isReset", isReset);
        params.put("token", token);
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_USERINFO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常");

            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                try {
                    showMistackDialog("网络数据解析异常");
                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常");
                }
            }
        });
    }

    private void showMistackDialog(String errorMsg) {
        AlertDialog alertDialog = new AlertDialog.Builder(UpdateIdCodeValidityActivity.this).create();
        alertDialog.setMessage(errorMsg);
        Message msg = new Message();
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", msg);
        alertDialog.show();
    }
}
