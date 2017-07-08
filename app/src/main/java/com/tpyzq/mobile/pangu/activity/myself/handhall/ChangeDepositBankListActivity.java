package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.myself.ChangeDepositBankListAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwnebo on 2017/7/5.
 * 三存银行变更银行列表
 */

public class ChangeDepositBankListActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, DialogInterface.OnCancelListener{

    private final String TAG = ChangeDepositBankListActivity.class.getSimpleName();
    private String mBankName;
    private Dialog mProgressDialog;
    private ChangeDepositBankListAdapter mAdapter;
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private List<Map<String, String>> mDatas;

    @Override
    public void initView() {

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("选择银行");

        Intent intent = getIntent();
        mBankName = intent.getStringExtra("bankName");
        ListView listView = (ListView) findViewById(R.id.changeBankListView);
        mAdapter = new ChangeDepositBankListAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        mDatas = new ArrayList<Map<String, String>>();
        getBankList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> bankInfo = mDatas.get(position);
        String BANK_NO = bankInfo.get("BANK_NO");
        String BANK_NAME = bankInfo.get("BANK_NAME");
        Intent intent = new Intent();
        intent.putExtra("BANK_NAME", BANK_NAME);
        intent.putExtra("BANK_NO", BANK_NO);
        setResult(RESULT_OK, intent);
        finish();
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
        return R.layout.activity_changebank_list;
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(ChangeDepositBankListActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg, CancelDialog.PositiveClickListener listener) {
        CancelDialog.cancleDialog(ChangeDepositBankListActivity.this, errorMsg, CancelDialog.NOT_BUY, listener, null);
    }

    private void getBankList() {

        initLoadDialog();

        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        map.put("funcid", "711035");
        map.put("token", mSession);
        HashMap hashMap = new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        hashMap.put("BANK_NO", "");
        map.put("parms", hashMap);
        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_SCYHBG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                showMistackDialog(ConstantUtil.NETWORK_ERROR, null);
            }

            @Override
            public void onResponse(String response, int id) {

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog(ConstantUtil.SERVICE_NO_DATA, null);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");

                    if ("-6".equals(code)) {
                        Intent intent = new Intent();
                        if (!Db_PUB_USERS.isRegister()) {
                            intent = new Intent(ChangeDepositBankListActivity.this, ShouJiZhuCeActivity.class);
                            ChangeDepositBankListActivity.this.startActivity(intent);
                        } else if (!Db_PUB_USERS.islogin()) {
                            intent = new Intent();
                            intent.setClass(ChangeDepositBankListActivity.this, TransactionLoginActivity.class);
                            ChangeDepositBankListActivity.this.startActivity(intent);
                        } else {
                            intent = new Intent();
                            intent.setClass(ChangeDepositBankListActivity.this, TransactionLoginActivity.class);
                            ChangeDepositBankListActivity.this.startActivity(intent);
                        }
                        finish();
                        return;
                    }

                    if (!"0".equals(code)) {
                        showMistackDialog(msg, null);
                        return;
                    }


                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        return;
                    }

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String BANK_NAME = object.optString("BANK_NAME");
                        String BANK_NO = object.optString("BANK_NO");

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("BANK_NAME", BANK_NAME);
                        map.put("BANK_NO", BANK_NO);
                        mDatas.add(map);
                    }

                    mAdapter.setDatas(mDatas, mBankName);

                } catch (JSONException e) {
                    e.printStackTrace();
                    showMistackDialog(ConstantUtil.JSON_ERROR, null);
                }
            }
        });

    }
}
