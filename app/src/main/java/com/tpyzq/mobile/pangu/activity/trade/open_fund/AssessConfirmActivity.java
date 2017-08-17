package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.PdfActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.BuyResultActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.PdfEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.DownloadDocPdfDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils.encryptBySessionKey;
import static com.umeng.socialize.Config.dialog;


/**
 * 协议签署界面
 */
public class AssessConfirmActivity extends BaseActivity implements View.OnClickListener, InterfaceCollection.InterfaceCallback {
    private String TAG = "AssessConfirmActivity";
    private Button bt_continue_buy;
    private AssessConfirmEntity assessConfirmBean;
    private CheckBox cb_affixto, cb_open_fund;
    private ImageView iv_back;
    private LinearLayout ll_view2, ll_view3, ll_view3_1;
//    private LinearLayout ll_view1, ll_view2, ll_view3, ll_view3_1;
    private String IS_AGREEMENT;
    private String IS_OPEN;
    private String session;
    private String flag;
    private Intent buyintent;
    private MyListView lv_agreement;
    private ArrayAdapter<String> adapter;
    private List<PdfEntity> pdfs;
    private TextView tv_notice;
    private Message message;
//    private TextView tv_fit, tv_risk_level, tv_produce_risk_level, tv_evaluating;
    private String SERIAL_NO;
    private String AUTO_BUY;
    @Override
    public void initView() {
        ll_view2 = (LinearLayout) findViewById(R.id.ll_view2);
        ll_view3 = (LinearLayout) findViewById(R.id.ll_view3);
        ll_view3_1 = (LinearLayout) findViewById(R.id.ll_view3_1);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_continue_buy = (Button) findViewById(R.id.bt_continue_buy);
        cb_affixto = (CheckBox) findViewById(R.id.cb_affixto);
        cb_open_fund = (CheckBox) findViewById(R.id.cb_open_fund);
        lv_agreement = (MyListView) findViewById(R.id.lv_agreement);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        initData();
    }

    private void initData() {
        message = new Message();
        message.what = 0;
        pdfs = new ArrayList<>();
        session = SpUtils.getString(this, "mSession", null);
        iv_back.setOnClickListener(this);
        buyintent = getIntent();
        flag = buyintent.getStringExtra("transaction");
        SERIAL_NO = buyintent.getStringExtra("SERIAL_NO");
        AUTO_BUY = buyintent.getStringExtra("AUTO_BUY");
        if (TextUtils.isEmpty(SERIAL_NO)){
            SERIAL_NO = "-1";
        }
        Bundle bundle = buyintent.getExtras();

        assessConfirmBean = (AssessConfirmEntity) bundle.getSerializable("assessConfirm");     //上页面传入的bean对象
        bt_continue_buy.setOnClickListener(this);
        cb_affixto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (View.GONE == ll_view3.getVisibility()) {
                        bt_continue_buy.setClickable(true);
                        bt_continue_buy.setBackgroundColor(ColorUtils.ORANGE);
                    } else {
                        if (!cb_open_fund.isChecked()) {
                            bt_continue_buy.setClickable(false);
                            bt_continue_buy.setBackgroundColor(ColorUtils.BT_GRAY);
                        } else {
                            bt_continue_buy.setClickable(true);
                            bt_continue_buy.setBackgroundColor(ColorUtils.ORANGE);
                        }
                    }
                } else {
                    bt_continue_buy.setClickable(false);
                    bt_continue_buy.setBackgroundColor(ColorUtils.BT_GRAY);
                }
            }
        });
        cb_open_fund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (View.GONE == ll_view2.getVisibility()) {
                        bt_continue_buy.setClickable(true);
                        bt_continue_buy.setBackgroundColor(ColorUtils.ORANGE);
                    } else {
                        if (!cb_affixto.isChecked()) {
                            bt_continue_buy.setClickable(false);
                            bt_continue_buy.setBackgroundColor(ColorUtils.BT_GRAY);
                        } else {
                            bt_continue_buy.setClickable(true);
                            bt_continue_buy.setBackgroundColor(ColorUtils.ORANGE);
                        }
                    }
                } else {
                    bt_continue_buy.setClickable(false);
                    bt_continue_buy.setBackgroundColor(ColorUtils.BT_GRAY);
                }
            }
        });

        if (assessConfirmBean.type.equals("3") || assessConfirmBean.type.equals("4")) {
            cb_open_fund.setText("开通OTC账户");
            ll_view3_1.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(assessConfirmBean.IS_AGREEMENT) && "0".equals(assessConfirmBean.IS_AGREEMENT)) {//不需要
                ll_view2.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(assessConfirmBean.IS_AGREEMENT) && "1".equals(assessConfirmBean.IS_AGREEMENT)) {//需要
                ll_view2.setVisibility(View.VISIBLE);
            }

        } else {
            cb_open_fund.setText("开通基金账户");
            ll_view3_1.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(assessConfirmBean.IS_AGREEMENT) && "1".equals(assessConfirmBean.IS_AGREEMENT)) {//不需要
                ll_view2.setVisibility(View.GONE);
                IS_AGREEMENT = "0";
            } else if (!TextUtils.isEmpty(assessConfirmBean.IS_AGREEMENT) && "0".equals(assessConfirmBean.IS_AGREEMENT)) {//需要
                ll_view2.setVisibility(View.VISIBLE);
                IS_AGREEMENT = "1";
            }
        }


        if (!TextUtils.isEmpty(assessConfirmBean.IS_OPEN) && "0".equals(assessConfirmBean.IS_OPEN)) {
            ll_view3.setVisibility(View.GONE);
            IS_OPEN = "0";
        } else {
            ll_view3.setVisibility(View.VISIBLE);
            IS_OPEN = "1";
        }
        switch (assessConfirmBean.type) {
            case "0"://基金定投
            case "1":
            case "2":
                if ("0".equals(assessConfirmBean.IS_AGREEMENT)) {
                    getFundPdf(assessConfirmBean.productcode);
                }
                break;
            case "3":
            case "4":
                getOTCPdf(assessConfirmBean.productcode);
                break;
        }
        if (View.GONE == ll_view2.getVisibility() && View.GONE == ll_view3.getVisibility()) {
            bt_continue_buy.setClickable(true);
            bt_continue_buy.setBackgroundColor(ColorUtils.ORANGE);
        } else {
            bt_continue_buy.setClickable(false);
            bt_continue_buy.setBackgroundColor(ColorUtils.BT_GRAY);
        }
//        adapter = new ArrayAdapter<String>(this,R.layout.item_agreement,);
        lv_agreement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPdfDown(pdfs.get(position).fileUrl, pdfs.get(position).filename);
            }
        });
        tv_notice.setOnClickListener(this);
    }

    private void getOTCPdf(String productcode) {
        HashMap mapHQTNG106 = new HashMap();
        mapHQTNG106.put("FUNCTIONCODE", "HQTNG106");
        mapHQTNG106.put("TOKEN", "");
        HashMap map100237_1 = new HashMap();
        map100237_1.put("prod_code", productcode);
        map100237_1.put("prod_kind_type", "3");
        mapHQTNG106.put("PARAMS", map100237_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_HQ_WB(), mapHQTNG106, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(AssessConfirmActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    showErrorDialog("暂无协议");
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String type = object.getString("type");
                    String message = object.getString("message");
                    if ("200".equals(code)) {
                        JSONObject jsonObject = new JSONObject(message);
                        PdfEntity pdfBean1 = new PdfEntity();
                        pdfBean1.filename = "《" + jsonObject.getString("open_account_contract") + "》";
                        pdfBean1.fileUrl = jsonObject.getString("open_account_contract_attr");
                        if (!TextUtils.isEmpty(pdfBean1.fileUrl)) {
                            pdfs.add(pdfBean1);
                        }
                        PdfEntity pdfBean2 = new PdfEntity();
                        pdfBean2.filename = "《" + jsonObject.getString("prod_risk_book") + "》";
                        pdfBean2.fileUrl = jsonObject.getString("prod_risk_book_attr");
                        if (!TextUtils.isEmpty(pdfBean2.fileUrl)) {
                            pdfs.add(pdfBean2);
                        }
                        PdfEntity pdfBean3 = new PdfEntity();
                        pdfBean3.filename = "《" + jsonObject.getString("risk_warning_book") + "》";
                        pdfBean3.fileUrl = jsonObject.getString("risk_warning_book_attr");
                        if (!TextUtils.isEmpty(pdfBean3.fileUrl)) {
                            pdfs.add(pdfBean3);
                        }
                        if (pdfs.size() == 0) {
                            showErrorDialog("暂无协议");}
                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < pdfs.size(); i++) {
                            list.add(pdfs.get(i).filename);
                        }
                        adapter = new ArrayAdapter<String>(AssessConfirmActivity.this, R.layout.item_agreement, list);
                        lv_agreement.setAdapter(adapter);
                    } else {
                        showErrorDialog("暂无协议");
                    }
                } catch (JSONException e) {
                    showErrorDialog("暂无协议");
                    e.printStackTrace();
                }
            }
        });
    }


    private void getFundPdf(String code) {
        HashMap map100237 = new HashMap();
        map100237.put("funcid", "100237");
        map100237.put("token", session);
        HashMap map100237_1 = new HashMap();
        map100237_1.put("prodcode", code);
        map100237.put("parms", map100237_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map100237, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showErrorDialog("暂无数据");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    showErrorDialog("暂无协议");
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    if ("0".equals(code)) {
                        String data = object.getString("PROAOCOLDATE");
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() <= 0) {
                            showErrorDialog("暂无协议");
                            return;
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PdfEntity pdfBean = new PdfEntity();
                            pdfBean.filename = "《" + jsonArray.getJSONObject(i).getString("fileName") + "》";
                            pdfBean.fileUrl = jsonArray.getJSONObject(i).getString("fileUrl");
                            pdfs.add(pdfBean);
                        }
                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            list.add(pdfs.get(i).filename);
                        }
                        adapter = new ArrayAdapter<String>(AssessConfirmActivity.this, R.layout.item_agreement, list);
                        lv_agreement.setAdapter(adapter);
                    } else {
                        showErrorDialog("暂无协议");
                    }
                } catch (JSONException e) {
                    showErrorDialog("暂无协议");
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPdfDown(String url, String filename) {
        DownloadDocPdfDialog.getInstance().showDialog(this, downloadPdfCallback, url, filename);
    }

    DownloadDocPdfDialog.DownloadPdfCallback downloadPdfCallback = new DownloadDocPdfDialog.DownloadPdfCallback() {
        @Override
        public void downloadResult(String filePath, String fileName) {
            Intent intent = new Intent();
            intent.setClass(AssessConfirmActivity.this, PdfActivity.class);
            intent.putExtra("filePath", filePath);
            intent.putExtra("fileName", fileName);
            intent.putExtra("flag", 1);
            startActivity(intent);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_assess_confirm;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_continue_buy:
                switch (assessConfirmBean.type) {
                    case "0":
                        String isAgreement = assessConfirmBean.IS_AGREEMENT.equalsIgnoreCase("0")?"1":"0";
                        InterfaceCollection.getInstance().addFixFund(assessConfirmBean.productcompany,
                                assessConfirmBean.productcode,"0",assessConfirmBean.productprice,
                                assessConfirmBean.RISK_RATING,assessConfirmBean.RISK_LEVEL,
                                assessConfirmBean.RISK_LEVEL_NAME,assessConfirmBean.IS_OPEN,isAgreement,
                                "0",AddOrModFixFundActivity.TAG_SUBMIT,this);
                        break;
                    case "1":
                        fund_rengou();
                        break;
                    case "2":
                        fund_shengou();
                        break;
                    case "3":
                        getOTCrengou();
                        break;
                    case "4":
                        getOTCshengou();
                        break;
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_notice:
                Intent intent = new Intent();
                intent.setClass(AssessConfirmActivity.this, PdfActivity.class);
                intent.putExtra("filePath", "fundOpenUser.pdf");
                intent.putExtra("fileName", "基金投资人权益须知");
                intent.putExtra("flag", 0);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取基金认购数据
     */
    private void fund_rengou() {
        if (TextUtils.isEmpty(assessConfirmBean.productcode)) {
            return;
        }
        HashMap map300439 = new HashMap();
        map300439.put("funcid", "300439");
        map300439.put("token", session);
        map300439.put("secret", UserUtil.Keyboard);   //1.加密 0.不加密
        HashMap map300439_1 = new HashMap();
        map300439_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        map300439_1.put("FUND_COMPANY", encryptBySessionKey(assessConfirmBean.productcompany));
        map300439_1.put("FUND_CODE", encryptBySessionKey(assessConfirmBean.productcode));
        map300439_1.put("BUY_AMOUNT", encryptBySessionKey(assessConfirmBean.productprice));
        map300439_1.put("FLAG", encryptBySessionKey("true"));
        map300439_1.put("DO_OPEN", encryptBySessionKey(IS_OPEN));
        map300439_1.put("DO_CONTRACT", encryptBySessionKey(IS_AGREEMENT));
        map300439_1.put("DO_PRE_CONDITION", encryptBySessionKey("0"));
        map300439_1.put("AUTO_BUY", encryptBySessionKey(AUTO_BUY));
        map300439.put("parms", map300439_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300439, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(AssessConfirmActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    if ("0".equalsIgnoreCase(code)) {
                        startFinish("0");
                    } else if ("-6".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, TransactionLoginActivity.class));
                    } else if ("400".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, AgreementActivity.class));
                    } else {
                        showErrorDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取基金申购数据
     */
    private void fund_shengou() {
        if (TextUtils.isEmpty(assessConfirmBean.productcode)) {
            return;
        }
        HashMap map300440 = new HashMap();
        map300440.put("funcid", "300440");
        map300440.put("token", session);
        map300440.put("secret", UserUtil.Keyboard);
        HashMap map300440_1 = new HashMap();
        map300440_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        map300440_1.put("FUND_COMPANY", encryptBySessionKey(assessConfirmBean.productcompany));
        map300440_1.put("FUND_CODE", encryptBySessionKey(assessConfirmBean.productcode));
        map300440_1.put("BUY_AMOUNT", encryptBySessionKey(assessConfirmBean.productprice));
        map300440_1.put("FLAG", encryptBySessionKey("true"));
        map300440_1.put("DO_OPEN", encryptBySessionKey(IS_OPEN));
        map300440_1.put("DO_CONTRACT", encryptBySessionKey(IS_AGREEMENT));
        map300440_1.put("DO_PRE_CONDITION", encryptBySessionKey("0"));
        map300440_1.put("AUTO_BUY", encryptBySessionKey(AUTO_BUY));
        map300440.put("parms", map300440_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300440, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(AssessConfirmActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    if ("0".equalsIgnoreCase(code)) {
                        startFinish("0");
                    } else if ("-6".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, TransactionLoginActivity.class));
                    }  else if ("400".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, AgreementActivity.class));
                    } else {
                        showErrorDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 确认otc认购
     */
    private void getOTCrengou() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", assessConfirmBean.productcode);
        map2.put("PRODTA_NO", assessConfirmBean.productcompany);
        map2.put("ENTRUST_BALANCE", assessConfirmBean.productprice);
        map2.put("FLAG", "true");
        map1.put("funcid", "730201");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(AssessConfirmActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equalsIgnoreCase(code)) {
                        SERIAL_NO = new JSONArray(data).getJSONObject(0).getString("SERIAL_NO");
                        startFinish("1");
                    } else if ("-6".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, TransactionLoginActivity.class));
                    } else if ("400".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, AgreementActivity.class));
                    } else {
                        showErrorDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 确认otc申购
     */
    private void getOTCshengou() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", assessConfirmBean.productcode);
        map2.put("PRODTA_NO", assessConfirmBean.productcompany);
        map2.put("ENTRUST_BALANCE", assessConfirmBean.productprice);
        map2.put("FLAG", "true");
        map1.put("funcid", "730202");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(AssessConfirmActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equalsIgnoreCase(code)) {
                        SERIAL_NO = new JSONArray(data).getJSONObject(0).getString("SERIAL_NO");
                        startFinish("1");
                    } else if ("-6".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, TransactionLoginActivity.class));
                    } else if ("400".equalsIgnoreCase(code)) {
                        startActivity(new Intent(AssessConfirmActivity.this, AgreementActivity.class));
                    } else {
                        showErrorDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        NetWorkUtil.cancelSingleRequestByTag("getOTCPdf");
        NetWorkUtil.cancelSingleRequestByTag("getFundPdf");
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    public void startFinish(String type) {
        if ("true".equals(flag)) {
//            CentreToast.showText(this, "委托已提交");
            CentreToast.showText(this,"委托已提交",true);
            setResult(RESULT_OK, buyintent);
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra("name", assessConfirmBean.productcode);
            intent.putExtra("price", assessConfirmBean.productprice);
            intent.putExtra("SERIAL_NO", SERIAL_NO);
            intent.putExtra("type", type);
            intent.setClass(this, BuyResultActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        String code = info.getCode();
        String tag = info.getTag();
        String msg = info.getMsg();
        switch (tag) {
            case AddOrModFixFundActivity.TAG_SUBMIT:
                if ("0".equalsIgnoreCase(code)) {
                    AssessConfirmEntity assessConfirmEntity = (AssessConfirmEntity) info.getData();
                    if ("0".equalsIgnoreCase(assessConfirmEntity.IS_ABLE)) {
                        CentreToast.showText(this, msg);
                        Intent submitIntent = new Intent();
                        setResult(RESULT_OK, submitIntent);
                        finish();
                    } else {
                        showWrongDialog(msg);
                    }
                } else if ("-6".equalsIgnoreCase(code)) {
                    startActivity(new Intent(AssessConfirmActivity.this, TransactionLoginActivity.class));
                } else if ("400".equalsIgnoreCase(code)) {
                    startActivity(new Intent(AssessConfirmActivity.this, AgreementActivity.class));
                } else {
                    showWrongDialog(msg);
                }
                break;
        }
    }

    /**
     * 显示警告
     * @param msg
     */
    private void showWrongDialog(String msg){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),AssessConfirmActivity.class.toString());
        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
            @Override
            public void confirmOnclick() {
                setResult(RESULT_CANCELED);
                finish();
                customCenterDialog.dismiss();
            }
        });
    }

    private void showErrorDialog(String msg){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),AssessConfirmActivity.class.toString());
        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
            @Override
            public void confirmOnclick() {
                setResult(RESULT_CANCELED);
                finish();
                customCenterDialog.dismiss();
            }
        });
    }
}
