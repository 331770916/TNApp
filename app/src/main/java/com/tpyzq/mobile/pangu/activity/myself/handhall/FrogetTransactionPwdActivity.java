package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.UpdateIdCodeValidityEntity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/23.
 * 忘记交易密码
 */

public class FrogetTransactionPwdActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener {

    private final String TAG = FrogetTransactionPwdActivity.class.getSimpleName();

    private EditText mFundAccout;
    private EditText mName;
    private EditText mIdCard;
    private TextView mNextStupBtn;

    private boolean  fundAccountIsNotNull;
    private boolean  nameIsNotNull;
    private boolean  idCardIsNotNull;

    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private Dialog mProgressDialog;

    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);

        mFundAccout = (EditText) findViewById(R.id.ed_userId_inputFundAccount);
        mName = (EditText) findViewById(R.id.ed_userId_inputName);
        mIdCard = (EditText) findViewById(R.id.ed_userId_inputIdCard);

        mFundAccout.addTextChangedListener(new TextChanged("account"));
        mName.addTextChangedListener(new TextChanged("name"));
        mIdCard.addTextChangedListener(new TextChanged("idcard"));

        TextView warn = (TextView) findViewById(R.id.tv_userid_warn);

        String str1 = "业务办理时间为";
        String str2 = "交易日 09:00—15:50";

        SpannableString ss = new SpannableString(str1 + str2);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.hideTextColor)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.calendarBtnColor)), str1.length(), (str1 + str2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        warn.setText(ss);

        mNextStupBtn = (TextView) findViewById(R.id.nextStupBtn);
        mNextStupBtn.setClickable(false);
        mNextStupBtn.setFocusable(false);

        mNextStupBtn.setOnClickListener(this);

//        mFundAccout.setText("680000100");
//        mIdCard.setText("372402197903190311");
//        mName.setText("李磊");

//        getServerTime();

//        mFundAccout.setText("680000101");
//        mIdCard.setText("53220119701202xxxx");
//        mName.setText("周琴钱");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
            case R.id.nextStupBtn:

                String user_name = mName.getText().toString();
                String id_no = mIdCard.getText().toString();
                String fund_account = mFundAccout.getText().toString();

                if (pass(fund_account, user_name, id_no)) {
                    initLoadDialog();
                    getUserInfo(fund_account, user_name, id_no, fund_account);
                }
                break;

        }
    }

    /**
     * 验证数据可靠性
     * @return
     */
    private boolean pass(String fund_account, String user_name, String id_no) {

        if (TextUtils.isEmpty(fund_account)) {
            CentreToast.showText(FrogetTransactionPwdActivity.this, "资金账号不能为空");
            return false;
        }

        if (TextUtils.isEmpty(user_name)) {
            CentreToast.showText(FrogetTransactionPwdActivity.this, "名字不能为空");
            return false;
        }

        if (TextUtils.isEmpty(id_no)) {
            CentreToast.showText(FrogetTransactionPwdActivity.this, "身份证不能为空");
            return false;
        }

//        if (id_no.length() < 18 ||!verifyIDNumber(id_no) ) {
//            Toast.makeText(FrogetTransactionPwdActivity.this, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }

    /**
     * 身份证号码验证
     */
    private boolean verifyIDNumber(String idCardNO) {
        if (idCardNO == null || idCardNO.length() <= 0) {
            return false;
        }

        idCardNO = idCardNO.toUpperCase();

        int total = 0;
        int[] w = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] v = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        for (int i = 0; i < 17; ++i) {
            int a = idCardNO.charAt(i) - '0';
            total += a * w[i];
        }
        int mod = total % 11;

        return v[mod] == idCardNO.charAt(17);
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
        return R.layout.activity_ftpwd;
    }


    /**
     * 获取系统时间接口
     */
    private void getServerTime() {
        Map params = new HashMap();
        params.put("token", "");
        params.put("funcid","400109");
        Map map = new HashMap();
        params.put("parms", map);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), params, new StringCallback() {
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
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if (!"0".equals(code)) {
                        showMistackDialog(msg, null);
                        return;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    Date nowDate = sdf.parse(msg);
                    Calendar date = Calendar.getInstance();
                    date.setTime(nowDate);

                    int hour = date.get(Calendar.HOUR_OF_DAY);
                    int minute = date.get(Calendar.MINUTE);

                    if (hour < 9  || hour >= 16 || hour == 15 && minute > 50) {
                        String str1 = "抱歉当前不在业务办理时间。\n请在";
                        String str2 = "交易日 09:00—15:50";
                        String str3 = "使用此功能。";
                        SpannableString ss = new SpannableString(str1 + str2 + str3);

                        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(FrogetTransactionPwdActivity.this, R.color.hushenTab_titleColor)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(FrogetTransactionPwdActivity.this, R.color.calendarBtnColor)), str1.length(), (str1 + str2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(FrogetTransactionPwdActivity.this, R.color.hushenTab_titleColor)), (str1 + str2).length(), (str1 + str2 + str3).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        showMistackDialog(ss.toString(), new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {
                                finish();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });
    }

    /**
     * @param cust_no 资金账号
     * @param user_name 用户名
     * @param id_no     身份证号码
     * @param fund_account 资金账号
     */
    private void getUserInfo(String cust_no, String user_name, String id_no, final String fund_account) {
        Map map = new HashMap();
        map.put("code", "WT6319");

        Map<String,String> params = new HashMap<>();
        params.put("cust_no", cust_no);
        params.put("user_name", user_name);
        params.put("id_no", id_no);
        params.put("fund_account", fund_account);
        map.put("params", params);
        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
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
                //{"error_no":"-2","error_info":"客户[680300101]信息记录不存在"}
                if (TextUtils.isEmpty(response)) {
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String errorNo = jsonObject.getString("error_no");
                    String error_info = jsonObject.getString("error_info");

                    if (!"0".equals(errorNo)) {
                        showMistackDialog(error_info, null);
                        return;
                    }

                    String user_id = jsonObject.getString("user_id");
                    String verify_id = jsonObject.getString("verify_id");
                    String biz_unit_id = jsonObject.getString("biz_unit_id");
                    String user_biz_id = jsonObject.getString("user_biz_id");

                    UpdateIdCodeValidityEntity entity = new UpdateIdCodeValidityEntity();
                    entity.setUserId(user_id);
                    entity.setUser_biz_id(user_biz_id);

                    Intent intent = new Intent();
                    intent.putExtra("biz_unit_id", biz_unit_id);
                    intent.putExtra("fund_account", fund_account);
                    intent.putExtra("verify_id", verify_id);
                    intent.putExtra("entity", entity);
                    intent.setClass(FrogetTransactionPwdActivity.this, UpLoadIdCardPicActivity.class);
                    startActivity(intent);
                    finish();


                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });
    }

    private class TextChanged implements TextWatcher {
        private String tag;
        public TextChanged(String tag) {
            this.tag = tag;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString())) {
                settingNextBtn(tag, true);
            } else {
                settingNextBtn(tag, false);
            }
        }
    }

    private void settingNextBtn (String tag, boolean isNotNull) {
        switch (tag) {
            case "account":
                fundAccountIsNotNull = isNotNull;
                nextStupBtnStauts();
                break;
            case "name":
                nameIsNotNull = isNotNull;
                nextStupBtnStauts();
                break;
            case "idcard":
                idCardIsNotNull = isNotNull;
                nextStupBtnStauts();
                break;
        }
    }

    private void nextStupBtnStauts() {
        if (fundAccountIsNotNull && idCardIsNotNull && nameIsNotNull) {
            mNextStupBtn.setClickable(true);
            mNextStupBtn.setFocusable(true);
            mNextStupBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.userid_nextstup_p));
        } else {
            mNextStupBtn.setClickable(false);
            mNextStupBtn.setFocusable(false);
            mNextStupBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.userid_nextstup_n));
        }

    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(FrogetTransactionPwdActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg,  CancelDialog.PositiveClickListener onClickListener) {

        CancelDialog.cancleDialog(FrogetTransactionPwdActivity.this, errorMsg, onClickListener);

//        AlertDialog alertDialog = new AlertDialog.Builder(FrogetTransactionPwdActivity.this).create();
//        alertDialog.setMessage(errorMsg);
//        alertDialog.setCancelable(false);
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", onClickListener);
//        alertDialog.show();
    }
}
