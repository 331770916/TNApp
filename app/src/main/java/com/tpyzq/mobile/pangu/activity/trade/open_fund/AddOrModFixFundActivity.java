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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    private static int REQEST_CHOOSE = 10001;
    private Dialog mRevokeDialog;
    private Dialog mDialog;

    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TimePickerView choosStartDate;
    private TimePickerView choosEndDate;
    private TimePickerView choosEnDate;
    private String currentDate;

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
        currentDate = Helper.getCurDate();
        position = getIntent().getIntExtra("position",-1);//position不为-1表示为修改
        if (position != -1) {
            fixFundEntity = (FixFundEntity)getIntent().getSerializableExtra("fixFundEntity");
            tv_title.setText(getResources().getString(R.string.modify_fund));
            tv_choose_fund.setVisibility(View.GONE);
            et_input_code.setText(fixFundEntity.getFUND_CODE());
            et_input_code.setEnabled(false);
            tv_fund_name.setText(fixFundEntity.getFUND_NAME());
//            tv_fund_jz.setText(fixFundEntity.getBALANCE());
            tv_en_date.setText(fixFundEntity.getEN_FUND_DATE()+"日");
            tv_start_date.setText(fixFundEntity.getSTART_DATE());
            tv_end_date.setText(fixFundEntity.getEND_DATE());
            et_input_branch.setText(fixFundEntity.getBALANCE());
            tv_fund_jz.setText(fixFundEntity.getFUND_VAL());
            //获取基金净值和可用资金
            mDialog.show();
            InterfaceCollection.getInstance().getFundData(fixFundEntity.getFUND_CODE(), fixFundEntity.getFUND_COMPANY(),TAG_REQUEST_FUND,this);
        } else {
            tv_title.setText(getResources().getString(R.string.add_fund));
            tv_start_date.setText(currentDate);
            tv_end_date.setText(currentDate);
            fixFundEntity = new FixFundEntity();
            fixFundEntity.setEN_FUND_DATE("1");
            fixFundEntity.setSTART_DATE(currentDate);
            fixFundEntity.setEND_DATE(currentDate);
        }
        tv_en_date.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        tv_choose_fund.setOnClickListener(this);
        bt_commint.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        choosStartDate = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        choosStartDate.setTime(new Date());
        choosStartDate.setCyclic(false);
        choosStartDate.setCancelable(true);
        choosStartDate.setTitle("选择日期");
        choosStartDate.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String chooseTime = simpleDateFormat.format(date);
                fixFundEntity.setSTART_DATE(chooseTime);
                tv_dest.setText(chooseTime);
            }
        });
        choosEndDate = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        choosEndDate.setTime(new Date());
        choosEndDate.setCyclic(false);
        choosEndDate.setCancelable(true);
        choosEndDate.setTitle("选择日期");
        choosEndDate.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String chooseTime = simpleDateFormat.format(date);
                fixFundEntity.setEND_DATE(chooseTime);
                tv_dest.setText(chooseTime);
            }
        });

        choosEnDate = new TimePickerView(this, TimePickerView.Type.DAY);
        choosEnDate.setDay(1,20,1);
        choosEnDate.setCyclic(false);
        choosEnDate.setCancelable(true);
        choosEnDate.setTitle("选择天数");
        choosEnDate.setOnDaySelectListener(new TimePickerView.OnDaySelectListener() {
            @Override
            public void onDaySelect(int day) {
                fixFundEntity.setEN_FUND_DATE(day+"");
                tv_dest.setText(day+"日");
            }
        });
        et_input_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String fundcode = s.toString();
                if (!TextUtils.isEmpty(fundcode) && s.length() == 6) {
                    InterfaceCollection.getInstance().getFundData(et_input_code.getText().toString(), "",TAG_REQUEST_FUND,AddOrModFixFundActivity.this);
                }
                clearView(false);
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
                tv_dest = (TextView) v;
                choosStartDate.show();
                break;
            case R.id.tv_end_date:
                tv_dest = (TextView) v;
                choosEndDate.show();
                break;
            case R.id.tv_choose_fund:
                startActivityForResult(new Intent(this, OptionalFundActivity.class), REQEST_CHOOSE);
                break;
            case R.id.bt_commint:
                String balance = et_input_branch.getText().toString().trim();
                if (TextUtils.isEmpty(balance)) {
                    CentreToast.showText(this,"请输入定投金额");
                    break;
                }
                if (null!=mRevokeDialog && !mRevokeDialog.isShowing()){
                    mRevokeDialog.show();
                }
                fixFundEntity.setBALANCE(balance);
                if (position == -1){//新增
                    InterfaceCollection.getInstance().addFixFund(fixFundEntity.getFUND_COMPANY(),
                            fixFundEntity.getFUND_CODE(),"0", balance,
                            Helper.getInstance().getMyDate(fixFundEntity.getSTART_DATE()),Helper.getInstance().getMyDate(fixFundEntity.getEND_DATE()),
                            fixFundEntity.getEN_FUND_DATE(),TAG_SUBMIT,this);
                } else {//修改
                    InterfaceCollection.getInstance().modifyFixFund(fixFundEntity.getFUND_COMPANY(),
                            fixFundEntity.getFUND_CODE(), balance,
                            Helper.getInstance().getMyDate(fixFundEntity.getSTART_DATE()),Helper.getInstance().getMyDate(fixFundEntity.getEND_DATE()),
                            fixFundEntity.getEN_FUND_DATE(),fixFundEntity.getALLOTNO(),TAG_SUBMIT,this);
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
                    clearView(true);
                    MistakeDialog.showDialog(msg,this,null);
                }
                break;
            case TAG_MODIFY:
                if ("0".equalsIgnoreCase(code)) {
                    CentreToast.showText(this, msg);
                    Intent modifyIntent= new Intent();
                    modifyIntent.putExtra("fixFundEntity",fixFundEntity);
                    modifyIntent.putExtra("position",position);
                    setResult(RESULT_OK, modifyIntent);
                    finish();
                } else {
                    MistakeDialog.showDialog(msg,this,null);
                }
                break;
            case TAG_SUBMIT:
                if ("0".equalsIgnoreCase(code)) {
                    CentreToast.showText(this, msg);
                    Intent submitIntent = new Intent();
                    fixFundEntity.setSEND_BALANCE("0");
                    fixFundEntity.setDEAL_FLAG("0");
                    fixFundEntity.setDEAL_FLAG_NAME("未处理");
                    submitIntent.putExtra("fixFundEntity",fixFundEntity);
                    submitIntent.putExtra("position",position);
                    setResult(RESULT_OK,submitIntent);
                    finish();
                } else {
                    MistakeDialog.showDialog(msg,this,null);
                }
                break;
        }
    }

    private void clearView(boolean isAllClear) {
        tv_fund_name.setText("--");
        tv_fund_jz.setText("--");
        tv_en_date.setText("1日");
        tv_start_date.setText(currentDate);
        tv_end_date.setText(currentDate);
        et_input_branch.setText("");
        fixFundEntity.setSTART_DATE(currentDate);
        fixFundEntity.setEND_DATE(currentDate);
        fixFundEntity.setEN_FUND_DATE("1");
        if (isAllClear) {
            tv_branch_enable.setText("--");
            et_input_code.setText("");
        }
    }

    private void setTextView(FundDataEntity fundDataBean) {
//        et_fund_price.setEnabled(true);
        tv_fund_name.setText(fundDataBean.data.get(0).FUND_NAME);
        tv_fund_jz.setText(fundDataBean.data.get(0).NAV);
        tv_branch_enable.setText("可用资金："+fundDataBean.data.get(0).ENABLE_BALANCE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQEST_CHOOSE && resultCode == RESULT_OK) {
            FundSubsEntity fundData = (FundSubsEntity) data.getSerializableExtra("data");
            et_input_code.setText(fundData.FUND_CODE);
        }
    }
}
