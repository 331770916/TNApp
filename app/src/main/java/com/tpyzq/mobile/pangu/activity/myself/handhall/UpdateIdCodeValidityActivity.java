package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.UpdateIdCodeValidityEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/20.
 * 身份证有效期修改
 */

public class UpdateIdCodeValidityActivity extends BaseActivity implements DialogInterface.OnCancelListener, View.OnClickListener {

    private final String TAG = UpdateIdCodeValidityActivity.class.getSimpleName();
    private Dialog mProgressDialog;
    private LinearLayout   mParentLayout;
    private UpdateIdCodeValidityEntity mEntity;
    private TextView mRightBtn;

    private RadioButton mLongTimeRb;
    private RadioButton mEffectiveTimeRb;
    private TextView mStartTimeTv;
    private TextView mEndTimeTv;
    private TextView mEffectiveTimeStrTagTv;
    private boolean mUpdateEffectiveState = false;
    private int mCheckedStateId = -1;
    private int mTempCheckedStateId = -1;
    private String mTempStartTime;
    private String mTempEndTime;

    private EditText mGroveMent;
    private EditText mAddress;

    private String mTempGroveMentText;
    private String mTempAddressText;

    private String isRest = "0";  //是否是重新变更  0:否， 1:是
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("身份证有效期变更");

        mRightBtn = (TextView) findViewById(R.id.userIdOutBtn);
        mRightBtn.setOnClickListener(this);

        mParentLayout = (LinearLayout) findViewById(R.id.ll_updateIdcode);

        mEntity = new UpdateIdCodeValidityEntity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mParentLayout.removeAllViews();
        String token = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        initLoadDialog();
        getUpdateIdCodeInfo(isRest, token);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
            case R.id.checkTaskFailed_out:
            case R.id.checkTaskIsRuning_btn:
                finish();
                break;
            case R.id.checkTaskFailed_restartCommite:
                isRest = "1";
                onResume();
                break;
            case R.id.userIdOutBtn:
                if (mUpdateEffectiveState) {
                    if (isDoSomeThing()) {
                        nextStup();
                    }
                } else {
                    showUpdateEffectiveState();
                }
                break;

            case R.id.effective_startTime:
                showPickerCandler(mStartTimeTv);
                break;
            case R.id.effective_endTime:
                showPickerCandler(mEndTimeTv);
                break;
            case R.id.rb_effective_longTime:
                mTempCheckedStateId = R.id.rb_effective_longTime;
                mEffectiveTimeRb.setChecked(false);
                break;
            case R.id.rb_effective_time:
                mTempCheckedStateId = R.id.rb_effective_time;
                mLongTimeRb.setChecked(false);
                break;
            case R.id.reCheckTask_btn:
                isRest = "1";
                onResume();
                break;
            case R.id.checkTaskSuccess_btn:
                finish();
                break;
        }
    }

    /**
     * 日期选择器
     * @param textView
     */
    private void showPickerCandler(final TextView textView) {
        TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setTitle("选择日期");
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                textView.setText(simpleDateFormat.format(date));
            }
        });
        timePickerView.show();
    }

    /**
     * 判断是否进行了操作
     * 1.判断是否改变了radioBtn的显示
     * 2.判断是否操作的日期的更改
     * @return
     */
    private boolean isDoSomeThing() {

        boolean isDo = true;
        if (TextUtils.isEmpty(mGroveMent.getText().toString())) {
            CentreToast.showText(UpdateIdCodeValidityActivity.this, "请填写签发机关");
            isDo = false;
        } else if (TextUtils.isEmpty(mAddress.getText().toString())) {
            CentreToast.showText(UpdateIdCodeValidityActivity.this, "请填写身份证地址");
            isDo = false;
        } else if (mTempCheckedStateId == mCheckedStateId) {
            if (timeNoChanged() && grovementNoChanged() && addressNoChanged()) {
                CentreToast.showText(UpdateIdCodeValidityActivity.this, "您未做任何操作");
                isDo = false;
            }
        }

        return isDo;
    }

    private boolean timeNoChanged() {
        return mTempStartTime.equals(mStartTimeTv.getText().toString()) && mTempEndTime.equals(mEndTimeTv.getText().toString());
    }

    private boolean grovementNoChanged() {
        return mGroveMent.getText().toString().equals(mTempGroveMentText);
    }

    private boolean addressNoChanged() {
        return mAddress.getText().toString().equals(mTempAddressText);
    }

    /**
     * 至为可更改状态
     */
    private void showUpdateEffectiveState() {
        mRightBtn.setText("保存");
        mUpdateEffectiveState = true;
        mLongTimeRb.setVisibility(View.VISIBLE);
        mStartTimeTv.setOnClickListener(this);
        mEndTimeTv.setOnClickListener(this);

        mGroveMent.setClickable(true);
        mGroveMent.setFocusable(true);
        mGroveMent.setFocusableInTouchMode(true);

        mAddress.setClickable(true);
        mAddress.setFocusable(true);
        mAddress.setFocusableInTouchMode(true);

        changeEffectiveTimeTextColor(R.color.text, R.color.hushenTab_titleColor, R.color.text);
    }

    //进行下一步的处理
    private void nextStup() {
        initLoadDialog();
        String end = "";
        if (mLongTimeRb.isChecked()) {
            end = "30001231";
        } else {
            end = mEndTimeTv.getText().toString();
        }
        updateIdCardTime(mEntity.getUserId(), mEntity.getApply_id(), mEntity.getName(),
                mEntity.getIdCard(), mEntity.getIssued_depart(), mStartTimeTv.getText().toString(), end, mEntity.getIdCardAddress());
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

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);

            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }

                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_no = jsonObject.getString("error_no");
                    String error_info = jsonObject.getString("error_info");

                    if ("-6".equals(error_no)) {

                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.cancel();
                        }

                        sessionFailed(error_info);
                        return;
                    }

                    if (!"0".equals(error_no) && !"1".equals(error_no)) {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.cancel();
                        }

                        showMistackDialog(error_info, null);
                        return;
                    }

                    String apply_status = "";
                    if ("1".equals(error_no)) {
                        //审核状态， 9:失败; 8:成功; 2:处理中；1:申请中
                        apply_status = jsonObject.getString("apply_status");
                    }
                    View view = new View(UpdateIdCodeValidityActivity.this);
                    if (!TextUtils.isEmpty(apply_status) && !"8".equals(apply_status)) {


                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.cancel();
                        }

                        if ("9".equals(apply_status)) {
                            view = LayoutInflater.from(UpdateIdCodeValidityActivity.this).inflate(R.layout.view_updateidcodevalidity_failed, null);
                            view.findViewById(R.id.checkTaskFailed_restartCommite).setOnClickListener(UpdateIdCodeValidityActivity.this);
                            view.findViewById(R.id.checkTaskFailed_out).setOnClickListener(UpdateIdCodeValidityActivity.this);
                        } else {
                            view = LayoutInflater.from(UpdateIdCodeValidityActivity.this).inflate(R.layout.view_updateidcodevalidity_checking, null);
                            view.findViewById(R.id.checkTaskIsRuning_btn).setOnClickListener(UpdateIdCodeValidityActivity.this);
                        }
                        mParentLayout.addView(view);
                        return;
                    }

                    String id_no         = jsonObject.getString("id_no");           //身份证号码
                    String apply_id      = jsonObject.getString("apply_id");        //申请业务id
                    String id_address    = jsonObject.getString("id_address");      //身份证地址
                    String id_enddate    = jsonObject.getString("id_enddate");      //身份证有效期截止时间
                    String client_name   = jsonObject.getString("client_name");     //名字
                    String id_begindate  = jsonObject.getString("id_begindate");    //身份证有效期开始时间
                    String issued_depart = jsonObject.getString("issued_depart");   //身份证签发机关

                    if ("8".equals(apply_status)) {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.cancel();
                        }

                        view = LayoutInflater.from(UpdateIdCodeValidityActivity.this).inflate(R.layout.view_updateidcodevalidity_success, null);
                        view.findViewById(R.id.checkTaskSuccess_btn).setOnClickListener(UpdateIdCodeValidityActivity.this);
                        view.findViewById(R.id.reCheckTask_btn).setOnClickListener(UpdateIdCodeValidityActivity.this);
                        TextView name = (TextView) view.findViewById(R.id.Name);
                        name.setText(client_name);
                        TextView idcode = (TextView) view.findViewById(R.id.IDNumber);
                        idcode.setText(id_no);
                        TextView address = (TextView) view.findViewById(R.id.IdAddress);
                        address.setText(id_address);
                        TextView validityDate = (TextView) view.findViewById(R.id.validDate);

                        String validityStr = id_begindate + "至" + id_enddate;

                        if (!TextUtils.isEmpty(id_enddate) && id_enddate.startsWith("3000")) {
                            validityStr = "长期";//
                        }

                        validityDate.setText(validityStr);

                        mParentLayout.addView(view);
                    } else {
                        String user_id       = jsonObject.getString("user_id");
                        String user_biz_id   = jsonObject.getString("user_biz_id");
                        mRightBtn.setText("修改");
                        view = LayoutInflater.from(UpdateIdCodeValidityActivity.this).inflate(R.layout.view_updateidcodevalidity, null);

                        mLongTimeRb = (RadioButton) view.findViewById(R.id.rb_effective_longTime);
                        mLongTimeRb.setOnClickListener(UpdateIdCodeValidityActivity.this);
                        mLongTimeRb.setVisibility(View.GONE);

                        mEffectiveTimeRb = (RadioButton) view.findViewById(R.id.rb_effective_time);
                        mEffectiveTimeRb.setOnClickListener(UpdateIdCodeValidityActivity.this);

                        mStartTimeTv = (TextView) view.findViewById(R.id.effective_startTime);
                        mEndTimeTv = (TextView) view.findViewById(R.id.effective_endTime);
                        mEffectiveTimeStrTagTv = (TextView) view.findViewById(R.id.effectiveTimeStrTag);

                        TextView name = (TextView) view.findViewById(R.id.tv_effective_name);
                        name.setText(client_name);

                        TextView idCardNumber = (TextView) view.findViewById(R.id.tv_effective_idNumber);
                        idCardNumber.setText(id_no);

                        mGroveMent = (EditText) view.findViewById(R.id.tv_effective_grovement);
                        mGroveMent.setClickable(false);
                        mGroveMent.setFocusable(false);
                        mGroveMent.setFocusableInTouchMode(false);
                        mGroveMent.setText(issued_depart);
                        mTempGroveMentText = mGroveMent.getText().toString();

                        mAddress = (EditText) view.findViewById(R.id.tv_effective_address);
                        mAddress.setClickable(false);
                        mAddress.setFocusable(false);
                        mAddress.setFocusableInTouchMode(false);
                        mAddress.setText(id_address);
                        mTempAddressText = mAddress.getText().toString();

                        mTempStartTime = id_begindate;
                        mTempEndTime = id_enddate;
                        mStartTimeTv.setText(id_begindate);
                        mEndTimeTv.setText(id_enddate);

                        changeEffectiveTimeTextColor(R.color.hushenTab_titleColor, R.color.text, R.color.red);

                        if (id_enddate.startsWith("3000")) {
                            mLongTimeRb.setChecked(true);
                            mCheckedStateId = R.id.rb_effective_longTime;
                        } else {
                            mEffectiveTimeRb.setChecked(true);
                            mCheckedStateId = R.id.rb_effective_time;
                        }
                        mTempCheckedStateId = mCheckedStateId;

                        mParentLayout.addView(view);

                        mEntity.setIdCard(id_no);
                        mEntity.setUserId(user_id);
                        mEntity.setApply_id(apply_id);
                        mEntity.setIdCardAddress(id_address);
                        mEntity.setIdCardEndDate(id_enddate);
                        mEntity.setName(client_name);
                        mEntity.setUser_biz_id(user_biz_id);
                        mEntity.setIdCardBeginDate(id_begindate);
                        mEntity.setIssued_depart(issued_depart);

                        netStup(user_id, user_biz_id);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                }
            }
        });
    }

    /**
     * session失效弹框
     * @param error_info
     */
    private void sessionFailed (String error_info) {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(UpdateIdCodeValidityActivity.this, ShouJiZhuCeActivity.class);
            UpdateIdCodeValidityActivity.this.startActivity(intent);
        } else if (!Db_PUB_USERS.islogin()) {
            intent = new Intent();
            intent.setClass(UpdateIdCodeValidityActivity.this, TransactionLoginActivity.class);
            UpdateIdCodeValidityActivity.this.startActivity(intent);
        } else {
            intent = new Intent();
            intent.setClass(UpdateIdCodeValidityActivity.this, TransactionLoginActivity.class);
            UpdateIdCodeValidityActivity.this.startActivity(intent);
        }
        finish();
    }

    /**
     * 设置有效日期颜色的改变
     */
    private void changeEffectiveTimeTextColor(@ColorRes int startColor, @ColorRes int middleColor, @ColorRes int endColor) {
        mStartTimeTv.setTextColor(ContextCompat.getColor(UpdateIdCodeValidityActivity.this, startColor));
        mEffectiveTimeStrTagTv.setTextColor(ContextCompat.getColor(UpdateIdCodeValidityActivity.this, middleColor));
        mEndTimeTv.setTextColor(ContextCompat.getColor(UpdateIdCodeValidityActivity.this, endColor));
    }


    /**
     * 修改身份证有效期变更接口
     */
    private void updateIdCardTime(String user_id, String apply_id, String user_name, String identity_code,
                                  String delivery_org, String delivery_start, String delivery_end,
                                  String cert_addr){
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("code", "WT6014");
        map1.put("user_id", user_id);
        map1.put("apply_id", apply_id);
        map1.put("user_name", user_name);
        map1.put("identity_code", identity_code);
        map1.put("delivery_org", delivery_org);//发证机关
        map1.put("delivery_start", delivery_start);//身份证有效期开始
        map1.put("delivery_end", delivery_end);//长期 身份证有效期结束
        map1.put("cert_addr", cert_addr);//地址
        map.put("params", map1);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
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
                    String error_info = jsonObject.getString("error_info");

                    if ("0".equals(error_no)) {
                        Intent intent = new Intent();
                        intent.putExtra("entity", mEntity);
                        intent.setClass(UpdateIdCodeValidityActivity.this, UpLoadIdCardPicActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        showMistackDialog(error_info, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });
    }

    /**
     * 进行到哪一步的网络请求，作用是后台获取进行到了哪一步
     */
    private void netStup(String userid, String user_biz_id) {
        Map map = new HashMap();
        map.put("code", "WT6041");

        Map<String,String> params = new HashMap<>();
        params.put("user_biz_id", user_biz_id);
        params.put("node_code", "1");
        params.put("node_name", "信息录入");//交易
        params.put("opr_memo", "信息录入");//资金
        params.put("opr_type",  "0");//通信
        params.put("opr_id", userid);
        params.put("result_comment", "信息录入");
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                showMistackDialog(ConstantUtil.NETWORK_ERROR, new CancelDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        finish();
                    }
                });

//                showMistackDialog("网络异常", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {

                    showMistackDialog(ConstantUtil.SERVICE_NO_DATA, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            finish();
                        }
                    });

//                    showMistackDialog("网络数据返回为空", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String error_no = jsonObject.getString("error_no");
                    String errorInfo = jsonObject.getString("error_info");

                    if (!"0".equals(error_no)) {


                        showMistackDialog(errorInfo, new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {
                                finish();
                            }
                        });

//                        showMistackDialog(errorInfo, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        });
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(UpdateIdCodeValidityActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg, CancelDialog.PositiveClickListener listener) {
        CancelDialog.cancleDialog(UpdateIdCodeValidityActivity.this, errorMsg, CancelDialog.NOT_BUY, listener, null);
    }

//    private void showMistackDialog(String errorMsg,  DialogInterface.OnClickListener onClickListener) {
//
//        AlertDialog alertDialog = new AlertDialog.Builder(UpdateIdCodeValidityActivity.this).create();
//        alertDialog.setMessage(errorMsg);
//        alertDialog.setCancelable(false);
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", onClickListener);
//        alertDialog.show();
//    }
}
