package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.UpdateIdCodeValidityEntity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/23.
 * 开始视频导航页面
 */

public class StartVideoActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener {

    private final String TAG = StartVideoActivity.class.getSimpleName();
    private String verify_id;
    private String fund_account;
    private UpdateIdCodeValidityEntity mEntity;

    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private ProgressDialog mProgressDialog;

    @Override
    public void initView() {

        Intent intent = getIntent();
        verify_id = intent.getStringExtra("verify_id");
        fund_account = intent.getStringExtra("fund_account");
        mEntity = intent.getParcelableExtra("entity");

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("视频验证");
        findViewById(R.id.waterView).setOnClickListener(this);

        initLoadDialog();

        stup(mEntity.getUser_biz_id(), mEntity.getUserId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                CancelDialog.cancleDialog(StartVideoActivity.this);
                break;
            case R.id.waterView:
                initLoadDialog();
                addUserVideo(verify_id, mEntity.getUserId());
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                clickBackKey = true;
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
        return R.layout.activity_startvideo;
    }

    /**
     * 进行视频步骤接口
     * @param user_biz_id
     * @param userid
     */
    private void stup(String user_biz_id, String userid) {
        Map map = new HashMap();
        map.put("code", "WT6041");

        Map<String,String> params = new HashMap<>();
        params.put("user_biz_id", user_biz_id);
        params.put("node_code", "4");
        params.put("node_name", "视频验证");//交易
        params.put("opr_memo", "视频验证");//资金
        params.put("opr_type", "0");//通信
        params.put("opr_id", userid);
        params.put("result_comment", "视频验证");
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_USERINFO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String error_no = jsonObject.getString("error_no");
                    String errorInfo = jsonObject.getString("error_info");

                    if (!"0".equals(error_no)) {
                        showMistackDialog(errorInfo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });

    }


    private void addUserVideo(final String verify_id, String userId) {
        Map map = new HashMap();
        map.put("code", "addUser");

        Map<String,String> params = new HashMap<>();
        params.put("wtVerifyId", verify_id);
        params.put("rnd", "" + System.currentTimeMillis());
        params.put("orgCode", "680");
        params.put("userId", userId);
        params.put("type", "31006");
        params.put("wtApplyId", "0");
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_USERINFO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String errorNo = jsonObject.getString("errorNo");
                    String errorInfo = jsonObject.getString("errorInfo");
                    if (!"0".equals(errorNo)) {
                        showMistackDialog(errorInfo, null);
                        return;
                    }

                    Intent intent = new Intent();
                    intent.putExtra("verify_id", verify_id);
                    intent.putExtra("fund_account", fund_account);
                    intent.putExtra("entity", mEntity);
                    intent.setClass(StartVideoActivity.this, QueueVideoActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });


    }

    private void initLoadDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg,  DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(StartVideoActivity.this).create();
        alertDialog.setMessage(errorMsg);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", onClickListener);
        alertDialog.show();
    }
}
