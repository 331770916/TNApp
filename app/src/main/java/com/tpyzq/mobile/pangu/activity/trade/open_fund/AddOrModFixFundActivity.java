package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskConfirmActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.FixFundEntity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.activity.trade.open_fund.FundInfoActivity.IS_SHOW;
import static com.tpyzq.mobile.pangu.activity.trade.open_fund.FundInfoActivity.ITEM_CLICK;
import static com.tpyzq.mobile.pangu.activity.trade.open_fund.FundInfoActivity.LIST_TYPE;

/**
 * 增加修改定投
 * Created by lx on 2017/7/21.
 */

public class AddOrModFixFundActivity extends BaseActivity implements View.OnClickListener, StructuredFundDialog.Expression, InterfaceCollection.InterfaceCallback {

    public static final String TAG_REQUEST_FUND = "queryfund";
    public static final String TAG_SUBMIT= "submit";
    public static final String TAG_MODIFY= "modify";
    private static int REQEST_CHOOSE = 10001;
    private static final int REQUEST_RISK = 10002;
    private static final int REQAGREEMENTCODE = 10003;
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_input_code;
    private TextView tv_en_date,tv_start_date,tv_end_date,tv_choose_fund,tv_fund_name,tv_fund_jz,tv_branch_enable;
    private FixFundEntity fixFundEntity;
    private int position;
    private EditText et_input_branch;
    private Button bt_commint;
    private TextView tv_dest;//时间选择器点击完成复制的TextView
    private Dialog mRevokeDialog;
    private Dialog mDialog;

    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TimePickerView choosStartDate;
    private TimePickerView choosEndDate;
    private TimePickerView choosEnDate;
    private String currentDate;
    private StructuredFundDialog mStructuredFundDialog;
    private RelativeLayout rl_en_date,rl_start_date,rl_end_date;

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
        rl_en_date = (RelativeLayout)findViewById(R.id.rl_en_date);//月定投日
        tv_start_date = (TextView)findViewById(R.id.tv_start_date);//开始时间
        rl_start_date = (RelativeLayout)findViewById(R.id.rl_start_date);//开始时间大布局
        tv_end_date = (TextView)findViewById(R.id.tv_end_date);//结束时间
        rl_end_date = (RelativeLayout)findViewById(R.id.rl_end_date);//结束时间大布局
        tv_choose_fund = (TextView)findViewById(R.id.tv_choose_fund);//选择产品点击
        tv_branch_enable = (TextView)findViewById(R.id.tv_branch_enable);//可用资金
        bt_commint = (Button)findViewById(R.id.bt_commint);//确定按钮
        initData();
//        getUserfulPrice();
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
            tv_end_date.setText(Helper.getInstance().getNextYear(new Date(),"yyyy-MM-dd"));

            fixFundEntity = new FixFundEntity();
            fixFundEntity.setEN_FUND_DATE("1");
            fixFundEntity.setSTART_DATE(currentDate);
            fixFundEntity.setEND_DATE(Helper.getInstance().getNextYear(new Date(),"yyyyMMdd"));
        }
        rl_en_date.setOnClickListener(this);
        rl_start_date.setOnClickListener(this);
        rl_end_date.setOnClickListener(this);
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
                fixFundEntity.setEN_FUND_DATE(day+1+"");
                tv_dest.setText(day+1+"日");
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
            case R.id.rl_en_date:
                tv_dest = tv_en_date;
                choosEnDate.show();
                break;
            case R.id.rl_start_date:
                tv_dest = tv_start_date;
                choosStartDate.show();
                break;
            case R.id.rl_end_date:
                tv_dest = tv_end_date;
                choosEndDate.show();
                break;
            case R.id.tv_choose_fund:
                Intent intent = new Intent();
                intent.setClass(this, FundInfoActivity.class);
                intent.putExtra(IS_SHOW, true);
                intent.putExtra(ITEM_CLICK, true);
                intent.putExtra(LIST_TYPE, "3");
                startActivityForResult(intent, REQEST_CHOOSE);
                break;
            case R.id.bt_commint:
                final String balance = et_input_branch.getText().toString().trim();
                if (TextUtils.isEmpty(balance)) {
                    CentreToast.showText(this,"请输入定投金额");
                    break;
                }

                int numDate2 = Helper.compareToDate(Helper.getCurDate(), tv_start_date.getText().toString());
                if (numDate2 == 1) {
                    CentreToast.showText(this,"开始日期不能小于当前日期");
                    break;
                }

                int numDate1 = Helper.compareToDate(tv_start_date.getText().toString(), tv_end_date.getText().toString());
                if (numDate1 == 1) {
                    CentreToast.showText(this,"结束日期不能小于开始日期");
                    break;
                }

                fixFundEntity.setBALANCE(balance);
                if (position == -1) {//新增
                    if (Helper.getInstance().isNeedShowRiskDialog()) {//判断风险评测
                        if (null!=mRevokeDialog && mRevokeDialog.isShowing()){
                            mRevokeDialog.dismiss();
                        }
                        Helper.getInstance().showCorpDialog(this, new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {
                                Intent intent = new Intent(AddOrModFixFundActivity.this, RiskEvaluationActivity.class);
                                intent.putExtra("isLogin", true);
                                AddOrModFixFundActivity.this.startActivity(intent);
                            }
                        }, new CancelDialog.NagtiveClickListener() {
                            @Override
                            public void onNagtiveClick() {
                                showDialog();
                            }
                        });
                    } else {
                        showDialog();
                    }
                } else {//修改
                    showDialog();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
    private void showDialog() {
        if (ConstantUtil.list_item_flag) {
            ConstantUtil.list_item_flag = false;
            mStructuredFundDialog = new StructuredFundDialog(this);
            mStructuredFundDialog.setData(TAG_REQUEST_FUND, this, fixFundEntity, "定投登记", null);
            mStructuredFundDialog.show();
        }
    }

    private void addFixFund() {
        if (null!=mRevokeDialog && !mRevokeDialog.isShowing()){
            mRevokeDialog.show();
        }
        InterfaceCollection.getInstance().addFixFund(fixFundEntity.getFUND_COMPANY(),
                fixFundEntity.getFUND_CODE(), "0", fixFundEntity.getBALANCE(),
                Helper.getInstance().getMyDate(fixFundEntity.getSTART_DATE()),
                Helper.getInstance().getMyDate(fixFundEntity.getEND_DATE()),
                fixFundEntity.getEN_FUND_DATE(),"1","0","1", TAG_SUBMIT, this);
    }

    @Override
    public void State() {
        mStructuredFundDialog.dismiss();
        mStructuredFundDialog = null;
        if (null!=mRevokeDialog && !mRevokeDialog.isShowing()){
            mRevokeDialog.show();
        }
        if (position == -1) {
            InterfaceCollection.getInstance().queryProductSuitability("", "", "",
                    fixFundEntity.getFUND_COMPANY(), fixFundEntity.getFUND_CODE(), "331261", new InterfaceCollection.InterfaceCallback() {
                        @Override
                        public void callResult(ResultInfo info) {
                            if (null != mRevokeDialog && mRevokeDialog.isShowing()) {
                                mRevokeDialog.dismiss();
                            }
                            String code = info.getCode();
                            String msg = info.getMsg();
                            HashMap<String, String> resultMap = null;
                            if ("0".equalsIgnoreCase(code)) {
                                resultMap = (HashMap<String, String>) info.getData();
                                if (null == resultMap) {
                                    showDialog("数据异常");
                                    return;
                                }
                                //弹框逻辑
                                //是否可以委托
                                if ("0".equalsIgnoreCase(resultMap.get("ENABLE_FLAG"))) {//不可委托
                                    //尊敬的客户:\n       根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理
                                    CancelDialog.cancleDialog(AddOrModFixFundActivity.this, "", 4000, new CancelDialog.PositiveClickListener() {
                                        @Override
                                        public void onPositiveClick() {
                                        }
                                    }, null);
                                    return;
                                }
//                        else {
//                            //可以委托 判断是否需要视频录制
//                            if ("1".equalsIgnoreCase(resultMap.get("NEED_VIDEO_FLAG"))) {
//                                CancelDialog.cancleDialog(FundPurchaseActivity.this,"尊敬的客户:\n根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理",4000,null,null);
//                                return;
//                            }
//                        }
                                //跳转到适用性页面
                                Intent intent = new Intent(AddOrModFixFundActivity.this, RiskConfirmActivity.class);
                                intent.putExtra("resultMap", resultMap);
                                AddOrModFixFundActivity.this.startActivityForResult(intent, REQUEST_RISK);
                            } else {
                                showDialog(msg);
                            }
                        }
                    });
        } else {
            String balance = et_input_branch.getText().toString().trim();
            if (null!=mRevokeDialog && !mRevokeDialog.isShowing()){
                mRevokeDialog.show();
            }
            InterfaceCollection.getInstance().modifyFixFund(fixFundEntity.getFUND_COMPANY(),
                    fixFundEntity.getFUND_CODE(), balance,
                    Helper.getInstance().getMyDate(fixFundEntity.getSTART_DATE()), Helper.getInstance().getMyDate(fixFundEntity.getEND_DATE()),
                    fixFundEntity.getEN_FUND_DATE(), fixFundEntity.getALLOTNO(), TAG_SUBMIT, this);
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
                    showDialog(msg);
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
                   /* CentreToast.showText(this, msg);
                    finish();*/
                } else {
                    showDialog(msg);
                }
                break;
            case TAG_SUBMIT:
                if ("0".equalsIgnoreCase(code)) {
                    AssessConfirmEntity assessConfirmEntity = (AssessConfirmEntity)info.getData();
                    if ("0".equalsIgnoreCase(assessConfirmEntity.IS_ABLE)) {
                       /* CentreToast.showText(this, msg);
                        finish();*/
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
                        if ("1".equalsIgnoreCase(assessConfirmEntity.IS_OPEN) || "0".equalsIgnoreCase(assessConfirmEntity.IS_AGREEMENT)){
                            Intent intent = new Intent();
                            intent.setClass(AddOrModFixFundActivity.this, AssessConfirmActivity.class);
                            intent.putExtra("transaction", "true");
                            intent.putExtra("assessConfirm", assessConfirmEntity);
                            startActivityForResult(intent, REQAGREEMENTCODE);
                        }
                    }
                } else if ("-6".equalsIgnoreCase(code)) {
                    startActivity(new Intent(AddOrModFixFundActivity.this, TransactionLoginActivity.class));
                }  else if ("400".equalsIgnoreCase(code)) {
                    startActivity(new Intent(AddOrModFixFundActivity.this, AgreementActivity.class));
                } else {
                    showDialog(msg);
                }
                break;
        }
    }

    private void clearView(boolean isAllClear) {
        tv_fund_name.setText("--");
        tv_fund_jz.setText("--");
        tv_en_date.setText("1日");
        tv_start_date.setText(currentDate);
        tv_end_date.setText(Helper.getInstance().getNextYear(new Date(),"yyyy-MM-dd"));
        et_input_branch.setText("");
        fixFundEntity.setSTART_DATE(currentDate);
        fixFundEntity.setEND_DATE(Helper.getInstance().getNextYear(new Date(),"yyyy-MM-dd"));
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
            tv_fund_name.setText(fundData.FUND_NAME);
            tv_fund_jz.setText(fundData.FUND_VAL);

            if (fixFundEntity != null) {
                fixFundEntity.setFUND_NAME(fundData.FUND_NAME);
                fixFundEntity.setFUND_CODE(fundData.FUND_CODE);
                fixFundEntity.setFUND_COMPANY(fundData.FUND_COMPANY);
            }
        }
        if (requestCode == REQAGREEMENTCODE && resultCode == RESULT_OK) {//签署协议页面返回
            et_input_branch.setText("");
        }
        if (requestCode == REQUEST_RISK && resultCode == RESULT_OK) {//风险同意书签署返回
            addFixFund();
        }
    }

    private final String TAG = AddOrModFixFundActivity.class.getSimpleName();
    private void getUserfulPrice() {

        HashMap map300441 = new HashMap();
        map300441.put("funcid", "300120");
        map300441.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", null));
        HashMap map300441_1 = new HashMap();
        map300441_1.put("SEC_ID", "tpyzq");
        map300441_1.put("FLAG", "true");
        map300441.put("parms", map300441_1);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map300441, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                CentreToast.showText(AddOrModFixFundActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    String BALANCE = "-.-";
                    if (!"0".equals(code)) {
                        showDialog(msg);
                        return;
                    }

                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i =0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        BALANCE = object.optString("BALANCE");
                    }

                    tv_branch_enable.setText(BALANCE);

                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog(ConstantUtil.JSON_ERROR);
                }

            }
        });

    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),AddOrModFixFundActivity.class.toString());
    }
}
