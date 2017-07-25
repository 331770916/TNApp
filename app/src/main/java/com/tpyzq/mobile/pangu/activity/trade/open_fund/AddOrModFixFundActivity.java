package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.FixFundEntity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 增加修改定投
 * Created by lx on 2017/7/21.
 */

public class AddOrModFixFundActivity extends BaseActivity implements View.OnClickListener,InterfaceCollection.InterfaceCallback {

    private static final String TAG_REQUEST_FUND = "queryfund";
    private static final String TAG_SUBMIT= "submit";
    private static final String TAG_MODIFY= "modify";
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_input_code;
    private TextView tv_en_date,tv_start_date,tv_end_date,tv_choose_fund,tv_fund_name,tv_fund_jz,tv_branch_enable;
    private FixFundEntity fixFundEntity;
    private int position;
    private EditText et_input_branch;
    private Button bt_commint;
    private TextView tv_dest;//时间选择器点击完成复制的TextView
    private static int REQEST_CHOOSE = 10000001;
    private Dialog mRevokeDialog;
    private Dialog mDialog;
    private TimePickerView choosDate;
    private TimePickerView choosEnDate;

    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void initView() {
        mRevokeDialog = LoadingDialog.initDialog(this, "操作中...");
        mDialog = LoadingDialog.initDialog(this, "正在查询...");
        iv_back = (ImageView)findViewById(R.id.iv_back);//返回按钮
        tv_title = (TextView)findViewById(R.id.tv_title);//标题
        et_input_code = (EditText)findViewById(R.id.et_input_code);//基金代码
        tv_fund_name = (TextView)findViewById(R.id.tv_fund_name);//基金名称
        tv_fund_jz = (TextView)findViewById(R.id.tv_fund_jz);//基金净值
        et_input_branch = (EditText)findViewById(R.id.et_input_branch);//投资金额
        tv_en_date = (TextView)findViewById(R.id.tv_en_date);//月定投日
        tv_start_date = (TextView)findViewById(R.id.tv_start_date);//开始时间
        tv_end_date = (TextView)findViewById(R.id.tv_end_date);//结束时间
        tv_choose_fund = (TextView)findViewById(R.id.tv_choose_fund);//选择产品点击
        tv_branch_enable = (TextView)findViewById(R.id.tv_branch_enable);//可用资金
        bt_commint = (Button)findViewById(R.id.bt_commint);//确定按钮
        initData();
    }

    private void initData() {
        position = getIntent().getIntExtra("position",-1);//position不为-1表示为修改
        if (position != -1) {
            fixFundEntity = (FixFundEntity)getIntent().getSerializableExtra("fixFundEntity");
            tv_title.setText(getResources().getString(R.string.modify_fund));
            et_input_code.setEnabled(false);
            tv_choose_fund.setVisibility(View.GONE);
        } else {
            tv_title.setText(getResources().getString(R.string.add_fund));
        }
        tv_en_date.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        tv_choose_fund.setOnClickListener(this);
        bt_commint.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_start_date.setText(Helper.getCurDate());
        tv_en_date.setText(Helper.getCurDate());

        choosDate = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        choosDate.setTime(new Date());
        choosDate.setCyclic(false);
        choosDate.setCancelable(true);
        choosDate.setTitle("选择日期");
        choosDate.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv_dest.setText(simpleDateFormat.format(date));
            }
        });

        choosEnDate = new TimePickerView(this, TimePickerView.Type.DAY);
        choosEnDate.setDay(1,20,1);
        choosEnDate.setCyclic(false);
        choosEnDate.setCancelable(true);
        choosEnDate.setTitle("选择天数");
        choosEnDate.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv_dest.setText(simpleDateFormat.format(date));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_or_mod_fund;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_en_date:
                tv_dest = tv_en_date;
                choosEnDate.show();
                break;
            case R.id.tv_start_date:
            case R.id.tv_end_date:
                tv_dest = (TextView) v;
                choosDate.show();
                break;
            case R.id.tv_choose_fund:
                tv_dest = tv_end_date;
                break;
            case R.id.bt_commint:
                String balance = et_input_branch.getText().toString().trim();
                if (TextUtils.isEmpty(balance)) {
                    CentreToast.showText(this,"请输入定投金额");
                    break;
                }
                if (position == -1){//新增
                    InterfaceCollection.getInstance().addFixFund(fixFundEntity.getFUND_COMPANY(),
                            fixFundEntity.getFUND_CODE(),"0", balance,
                            tv_start_date.getText().toString(),tv_end_date.getText().toString(),
                            tv_en_date.getText().toString(),TAG_SUBMIT,this);
                } else {//修改
                    InterfaceCollection.getInstance().modifyFixFund(fixFundEntity.getFUND_COMPANY(),
                            fixFundEntity.getFUND_CODE(), balance,
                            tv_start_date.getText().toString(),tv_end_date.getText().toString(),
                            tv_en_date.getText().toString(),fixFundEntity.getALLOTNO(),TAG_SUBMIT,this);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        String code = info.getCode();
        String tag = info.getTag();
        String msg = info.getMsg();
        if (null!=mDialog&& mDialog.isShowing()){
            mDialog.dismiss();
        }
        if (null!=mRevokeDialog&& mRevokeDialog.isShowing()){
            mRevokeDialog.dismiss();
        }
        switch (tag) {
            case TAG_REQUEST_FUND:
                if ("0".equalsIgnoreCase(code)){
                    FundDataEntity fundDataBean = (FundDataEntity)info.getData();
                    setTextView(fundDataBean);
                    fixFundEntity.setFUND_CODE(fundDataBean.data.get(0).FUND_CODE);
                    fixFundEntity.setFUND_NAME(fundDataBean.data.get(0).FUND_NAME);
                    fixFundEntity.setFUND_COMPANY(fundDataBean.data.get(0).FUND_COMPANY);
                } else {
                    clearView();
                    CentreToast.showText(AddOrModFixFundActivity.this,msg, false);
                }
                break;
            case TAG_MODIFY:
                if ("0".equalsIgnoreCase(code)) {
                    CentreToast.showText(this, msg);
                    getIntent().putExtra("fixFundEntity",fixFundEntity);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    MistakeDialog.showDialog(msg,this,null);
                }
                break;
            case TAG_SUBMIT:
                if ("0".equalsIgnoreCase(code)) {
                    CentreToast.showText(this, msg);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    MistakeDialog.showDialog(msg,this,null);
                }
                break;
        }
    }

    private void clearView() {
        tv_fund_name.setText("--");
        tv_fund_jz.setText("--");
        tv_branch_enable.setText("--");
        et_input_code.setText("");
        tv_en_date.setText("1日");
        tv_start_date.setText(Helper.getCurDate());
        tv_en_date.setText(Helper.getCurDate());
        et_input_branch.setText("");
    }

    private void setTextView(FundDataEntity fundDataBean) {
//        et_fund_price.setEnabled(true);
        tv_fund_name.setText(fundDataBean.data.get(0).FUND_NAME);
        tv_fund_jz.setText(fundDataBean.data.get(0).NAV);
        tv_branch_enable.setText(fundDataBean.data.get(0).ENABLE_BALANCE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQEST_CHOOSE && resultCode == RESULT_OK) {
            FundSubsEntity fundData = (FundSubsEntity) data.getSerializableExtra("data");
            et_input_code.setText(fundData.FUND_CODE);
            mDialog.show();
            InterfaceCollection.getInstance().getFundData(fundData.FUND_CODE, fundData.FUND_COMPANY_NAME,TAG_REQUEST_FUND,this);
        }
    }
}
