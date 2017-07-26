package com.tpyzq.mobile.pangu.activity.myself.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain.RemainActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ChangeAccoutActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;


/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    LinearLayout ll_version /*版本与升级*/, ll_help_feedback/*意见与反馈*/;
    TextView tv_choose_account;
    TextView tv_user_icon;
    TextView tv_now_fund_account;
    TextView tv_account;
    TextView tv_version_name;
    LinearLayout ll_set_remind;
    LinearLayout ll_set_refresh;
    LinearLayout ll_speed;
    LinearLayout ll_login;
    SimpleDraweeView iv_user_icon;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_version = (LinearLayout) findViewById(R.id.ll_version);
        ll_help_feedback = (LinearLayout) findViewById(R.id.ll_help_feedback);
        ll_set_refresh = (LinearLayout) findViewById(R.id.ll_set_refresh);
        tv_choose_account = (TextView) findViewById(R.id.tv_choose_account);
        tv_user_icon = (TextView) findViewById(R.id.tv_user_icon);
        tv_now_fund_account = (TextView) findViewById(R.id.tv_now_fund_account);
        tv_account = (TextView) findViewById(R.id.tv_account);
        ll_set_remind = (LinearLayout) findViewById(R.id.ll_set_remind);
        ll_speed = (LinearLayout) findViewById(R.id.ll_speed);
        ll_login = (LinearLayout) findViewById(R.id.ll_login);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        iv_user_icon = (SimpleDraweeView) findViewById(R.id.iv_user_icon);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        ll_version.setOnClickListener(this);
        ll_help_feedback.setOnClickListener(this);
        tv_choose_account.setOnClickListener(this);
        ll_set_refresh.setOnClickListener(this);
        ll_set_remind.setOnClickListener(this);
        ll_speed.setOnClickListener(this);
        ll_login.setOnClickListener(this);
        iv_user_icon.setOnClickListener(this);
        tv_user_icon.setOnClickListener(this);
        tv_version_name.setText("V" + APPInfoUtils.getVersionName(this));
        findViewById(R.id.jy_speed).setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserUtil.refrushUserInfo();
        if (Db_PUB_USERS.isRegister()) {
            tv_user_icon.setText("注册账号\t" + Db_PUB_USERS.queryingScno());
        } else {
            tv_user_icon.setText("登录/注册");
        }
        if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
            tv_now_fund_account.setText("更换交易账号");
            tv_account.setText(UserUtil.capitalAccount);
        } else {
            tv_now_fund_account.setText("未登录");
            tv_account.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_set_refresh:
                // 设置跳转到行情刷新频率界面
                intent.setClass(this,RefreshSpeedActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_version:
                // 跳转版本控制界面
                intent.setClass(SettingActivity.this, VersionUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_help_feedback:
                // 跳转反馈界面
                if (!Db_PUB_USERS.isRegister()) {
                    intent.setClass(SettingActivity.this, ShouJiZhuCeActivity.class);
                    intent.putExtra("Identify", "2");
                } else {
                    if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                    intent.setClass(SettingActivity.this, FeedbackActivity.class);
                    } else {
                            intent.setClass(SettingActivity.this, ShouJiVerificationActivity.class);
                            intent.putExtra("Identify", "2");
                    }
                }
                startActivity(intent);
                break;
            case R.id.tv_user_icon:
            case R.id.tv_choose_account:
                    intent.setClass(SettingActivity.this, ShouJiZhuCeActivity.class);
                    intent.putExtra("Identify", "1");
                    startActivity(intent);
                break;
            case R.id.ll_set_remind:
                if (!Db_PUB_USERS.isRegister()) {
                    intent.setClass(SettingActivity.this, ShouJiZhuCeActivity.class);
                    intent.putExtra("Identify", "2");
                } else {
                    if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                    intent.setClass(SettingActivity.this, RemainActivity.class);        // 股票提醒页面 market整合结束
                    } else {
                            intent.setClass(SettingActivity.this, ShouJiVerificationActivity.class);
                            intent.putExtra("Identify", "2");
                        }
                }
                startActivity(intent);
                break;
            case R.id.ll_speed:
                // 跳转到站点测速界面
                intent = new Intent(SettingActivity.this, SpeedTestActivity.class);
                startActivity(intent);
                break;
            case R.id.jy_speed:
                intent = new Intent(SettingActivity.this, SpeedJYActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_login:
                UserUtil.refrushUserInfo();
                if (Db_PUB_USERS.isRegister()) {
                    if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                        tv_user_icon.setText("注册账号\t" + Db_PUB_USERS.queryingScno());
                        if ("未登录".equals(tv_now_fund_account.getText().toString())){
                            intent.setClass(SettingActivity.this, TransactionLoginActivity.class);
                        }else {
                            intent.setClass(SettingActivity.this, TransactionLoginActivity.class);
                        }
                        intent.putExtra("EXIT",true);
                    } else {
                            intent.setClass(SettingActivity.this, ShouJiVerificationActivity.class);
                    }
                } else {
                    intent.setClass(SettingActivity.this, ShouJiZhuCeActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.iv_user_icon:
                intent.setClass(SettingActivity.this, ShouJiZhuCeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserUtil.refrushUserInfo();
        if (!Db_PUB_USERS.isRegister()) {
            iv_user_icon.setClickable(true);
            tv_user_icon.setClickable(true);
            tv_choose_account.setVisibility(View.INVISIBLE);
        } else {
            iv_user_icon.setClickable(false);
            tv_user_icon.setClickable(false);
        }
    }

}
