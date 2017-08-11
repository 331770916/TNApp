package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;

import java.util.HashMap;

/**
 * Created by 33920_000 on 2017/6/26.
 * 风险确认页面，基金认购、基金申购、OTC认购、TC申购 稳赢产品中的基金和otc认购申购 跳转此页面
 */

public class RiskConfirmActivity extends BaseActivity implements View.OnClickListener {

    private ImageView AGpublish_back;
    private TextView tv_risk_level,tv_product_level,tv_result,tv_warn,tv_response;
    private CheckBox cb_open_fund;
//    private LinearLayout ll_btn;
    private LinearLayout ll_response;
    private Button btn_ok,btn_cancel;
    private FundDataEntity fundSubsBean;
    private String session;
    private String instr_batch_no;//记录批次号
    private FundSubsEntity fundData;
    private String fundCompany,fundCode,prodta_no,prod_code;
    private String from;
    private HashMap<String, String> resultMap;
    private boolean isMatch = true;
    private static String OK_RESULT = "适当性评估结果确认书";
    private static String ERROR_RESULT = "金融产品或金融服务不适当警示\n及客户投资确认书";
    private TextView Headline;//页面标题

    @Override
    public void initView() {
        AGpublish_back = (ImageView)findViewById(R.id.AGpublish_back);//返回按钮
        AGpublish_back.setOnClickListener(this);
        tv_risk_level = (TextView)findViewById(R.id.tv_risk_level);//风险测评等级
        Headline = (TextView) findViewById(R.id.Headline);//风险测评等级
        tv_product_level = (TextView)findViewById(R.id.tv_product_level);//产品风险等级
        ll_response = (LinearLayout)findViewById(R.id.ll_response);//不匹配原因大布局
        tv_result = (TextView)findViewById(R.id.tv_result);//不匹配项
        tv_warn = (TextView)findViewById(R.id.tv_warn);//警告提示
        tv_response = (TextView)findViewById(R.id.tv_response);//风险内容
        cb_open_fund = (CheckBox)findViewById(R.id.cb_open_fund);//同意协议选择框
        btn_ok = (Button)findViewById(R.id.btn_ok);//同意按钮
        btn_ok.setOnClickListener(this);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);//取消按钮
        btn_cancel.setOnClickListener(this);
        setData();
//        from = getIntent().getStringExtra("from");//从那个页面回来 fundPurchase 基金申购 fundSubs 基金认购 OTC_Subscription  OTC认购 OTC_Subscribe  OTC申购

/*
        InterfaceCollection.getInstance().queryProductSuitability(session, prodta_no, prod_code, fundCompany, fundCode, "331261", new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                HashMap<String,String> resultMap = null;
                if ("0".equalsIgnoreCase(code)) {
                    resultMap = (HashMap<String,String>)info.getData();
                    if (null == resultMap) {
                        showErrorDialog("数据异常");
                        return;
                    }
//                    ll_btn.setVisibility(View.VISIBLE);
                    tv_product_level.setText(resultMap.get("PRODRISK_LEVEL"));//产品等级
                    tv_risk_level.setText(resultMap.get("CORP_RISK_LEVEL_INFO"));//用户等级
                    elig_risk_flag = resultMap.get("ELIG_RISK_FLAG");
                    if ("0".equalsIgnoreCase(elig_risk_flag)) {//不匹配
                        tv_result.setTextColor(Color.parseColor("#d0011b"));
                        tv_result.setText("不匹配");//匹配状态
                        ll_response.setVisibility(View.VISIBLE);
                        tv_warn.setVisibility(View.VISIBLE);
                    } else {
                        tv_result.setTextColor(Color.parseColor("#28a946"));
                        tv_result.setText("匹配");//匹配状态
                        ll_response.setVisibility(View.GONE);
                        tv_warn.setVisibility(View.GONE);
                    }
                    StringBuffer sb = new StringBuffer();
                    CharSequence eligRiskFlagInfo = resultMap.get("ELIG_RISK_FLAG_INFO");
                    if ("0".equalsIgnoreCase(elig_risk_flag)&& !TextUtils.isEmpty(eligRiskFlagInfo)) {
                        sb.append("·"+eligRiskFlagInfo);
                    }
                    String elig_term_flag = resultMap.get("ELIG_TERM_FLAG");
                    String elig_term_flag_info = resultMap.get("ELIG_TERM_FLAG_INFO");
                    if ("0".equalsIgnoreCase(elig_term_flag) && !TextUtils.isEmpty(elig_term_flag_info)) {
                        if (!TextUtils.isEmpty(sb.toString())){
                            sb.append("\r\n");
                        }
                        sb.append("·"+elig_term_flag_info);
                    }
                    String elig_investkind_flag = resultMap.get("ELIG_INVESTKIND_FLAG");
                    String elig_investkind_flag_info = resultMap.get("ELIG_INVESTKIND_FLAG_INFO");
                    if ("0".equalsIgnoreCase(elig_investkind_flag) && !TextUtils.isEmpty(elig_investkind_flag_info)) {
                        if (!TextUtils.isEmpty(sb.toString())){
                            sb.append("\r\n");
                        }
                        sb.append("·"+elig_investkind_flag_info);
                    }
                    tv_response.setText(sb.toString().trim());
                    instr_batch_no = resultMap.get("INSTR_BATCH_NO");
                    //弹框逻辑
                    //是否可以委托
                    if ("0".equalsIgnoreCase(resultMap.get("ENABLE_FLAG"))) {//不可委托
                        //尊敬的客户:\n       根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理
                        CancelDialog.cancleDialog(RiskConfirmActivity.this,"",4000,null,null);
                    } else {
                        //可以委托 判断是否需要视频录制
                        if ("1".equalsIgnoreCase(resultMap.get("NEED_VIDEO_FLAG"))) {
                            CancelDialog.cancleDialog(RiskConfirmActivity.this,"尊敬的客户:\n根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理",4000,null,null);
                        }
                    }
                } else {
                    showErrorDialog(msg);
                }
            }
        });
*/

    }

    private void setData() {
        resultMap = (HashMap<String,String>)getIntent().getSerializableExtra("resultMap");//传递数据进入
        tv_product_level.setText(resultMap.get("PRODRISK_LEVEL"));//产品等级
        tv_risk_level.setText(resultMap.get("CORP_RISK_LEVEL_INFO"));//用户等级
        StringBuffer sb = new StringBuffer();
        String elig_risk_flag = resultMap.get("ELIG_RISK_FLAG");//风险匹配标志
        CharSequence eligRiskFlagInfo = resultMap.get("ELIG_RISK_FLAG_INFO");//风险匹配标志描述
        if ("0".equalsIgnoreCase(elig_risk_flag)&& !TextUtils.isEmpty(eligRiskFlagInfo)) {
            sb.append("·"+eligRiskFlagInfo);
        }
        //暂时只处理风险匹配信息，其他暂不考虑
//        String elig_term_flag = resultMap.get("ELIG_TERM_FLAG");//投资周期匹配标志
//        String elig_term_flag_info = resultMap.get("ELIG_TERM_FLAG_INFO");//投资品种标志描述
//        if ("0".equalsIgnoreCase(elig_term_flag) && !TextUtils.isEmpty(elig_term_flag_info)) {
//            if (!TextUtils.isEmpty(sb.toString())){
//                sb.append("\r\n");
//            }
//            sb.append("·"+elig_term_flag_info);
//        }
//        String elig_investkind_flag = resultMap.get("ELIG_INVESTKIND_FLAG");//投资品种标志
//        String elig_investkind_flag_info = resultMap.get("ELIG_INVESTKIND_FLAG_INFO");//投资品种标志描述
//        if ("0".equalsIgnoreCase(elig_investkind_flag) && !TextUtils.isEmpty(elig_investkind_flag_info)) {
//            if (!TextUtils.isEmpty(sb.toString())){
//                sb.append("\r\n");
//            }
//            sb.append("·"+elig_investkind_flag_info);
//        }
        tv_response.setText(sb.toString().trim());

//        if ("0".equalsIgnoreCase(elig_risk_flag)||"0".equalsIgnoreCase(elig_investkind_flag)||"0".equalsIgnoreCase(elig_term_flag)) {//不匹配
        if ("0".equalsIgnoreCase(elig_risk_flag)) {//不匹配
            Headline.setText(ERROR_RESULT);
            isMatch = false;
            tv_result.setTextColor(Color.parseColor("#d0011b"));
            tv_result.setText("不匹配");//匹配状态
            ll_response.setVisibility(View.VISIBLE);
            tv_warn.setVisibility(View.VISIBLE);
        } else {
            Headline.setText(OK_RESULT);
            isMatch = true;
            tv_result.setTextColor(Color.parseColor("#28a946"));
            tv_result.setText("匹配");//匹配状态
            ll_response.setVisibility(View.GONE);
            tv_warn.setVisibility(View.GONE);
        }
        instr_batch_no = resultMap.get("INSTR_BATCH_NO");//指令批号
        session = SpUtils.getString(this, "mSession", null);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_risk_confirm;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AGpublish_back://返回按钮
                OkHttpUtil.cancelSingleRequestByTag("331261");//取消网络请求-适用性查询
                OkHttpUtil.cancelSingleRequestByTag("331279");//取消网络请求-适用性记录
                finish();
                break;
            case R.id.btn_ok://确定按钮
                if (!cb_open_fund.isChecked()) {
                    CentreToast.showText(this,"您还没有签署协议");
                    break;
                }
                String oper_info;
                if (isMatch) {
                    oper_info = "已向用户揭示适当性评估结果确认书，经用户确认同意后继续委托";
                } else {
                    oper_info = "已向用户揭示适当性评估结果不匹配确认书，经用户确认同意后继续委托";
                }
                InterfaceCollection.getInstance().productSuitabilityRecord(session, instr_batch_no, oper_info, "331279", new InterfaceCollection.InterfaceCallback() {
                    @Override
                    public void callResult(ResultInfo info) {
                        if ("0".equalsIgnoreCase(info.getCode())) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(info.getMsg(),CustomCenterDialog.SHOWCENTER);
                            customCenterDialog.show(getFragmentManager(),RiskConfirmActivity.class.toString());
                        }
                    }
                });
                break;
            case R.id.btn_cancel://取消按钮
                OkHttpUtil.cancelSingleRequestByTag("331261");//取消网络请求-适用性查询
                OkHttpUtil.cancelSingleRequestByTag("331279");//取消网络请求-适用性记录
                finish();
                break;
        }
    }
}
