package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.PdfActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_ContractAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;
import com.tpyzq.mobile.pangu.data.PdfEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.CheckResultListener;
import com.tpyzq.mobile.pangu.interfac.ContractCheckBoxListenerImpl;
import com.tpyzq.mobile.pangu.interfac.OpenOtcCheckBoxListenerImpl;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.DownloadDocPdfDialog;
import com.tpyzq.mobile.pangu.view.dialog.HandoverDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * OTC 电子合同签署界面
 * 刘泽鹏
 */
public class OTC_ContractSignActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, CheckResultListener {

    private static final String TAG = "OTC_ContractSign";


    private OTC_ContractAdapter mAdapter;
    private String mSession;
    private String mProductCode;       //产品代码
    private String mProductNo;         //产品编号
    private List<PdfEntity> mPdfs;
    private boolean mContractCheckResult;//协议签署结果
    private boolean mOpenOtcCheckResult;//开通Otc
    private boolean mVisibleTag = false;
    private TextView mSubmitBtn;
    private LinearLayout mLinearLayout;


    @Override
    public void initView() {
        ArrayList<OTC_ElectronicContractEntity> OTC_ElectronicContractIntents = (ArrayList<OTC_ElectronicContractEntity>) getIntent().getSerializableExtra("list");
        if (OTC_ElectronicContractIntents == null || OTC_ElectronicContractIntents.size() <= 0) {
            return;
        }

        //初始化listview
        MyListView lv_agreement = (MyListView) this.findViewById(R.id.electronicContractListView);
        mAdapter = new OTC_ContractAdapter();
        lv_agreement.setAdapter(mAdapter);
        lv_agreement.setOnItemClickListener(this);

        //初始回退按钮
        findViewById(R.id.ivOTC_ContractSign_back).setOnClickListener(this);
        findViewById(R.id.tv_open).setOnClickListener(this);
        findViewById(R.id.rlOTCKaiTongQY).setVisibility(View.GONE);
        //初始化确定按钮
        mSubmitBtn = (TextView) this.findViewById(R.id.tvContractSignQD);
        mSubmitBtn.setBackgroundResource(R.drawable.lonin4);                //背景灰色
        mSubmitBtn.setEnabled(false);                                       //不可点击
        mSubmitBtn.setOnClickListener(this);

        mLinearLayout = (LinearLayout) findViewById(R.id.llOTCKaiTongQY);

        //初始CheckBox

        CheckBox contractChexBox = (CheckBox) this.findViewById(R.id.cbUp);
        contractChexBox.setOnCheckedChangeListener(new ContractCheckBoxListenerImpl(mSubmitBtn, this));
        CheckBox openOTCChecxBox = (CheckBox) this.findViewById(R.id.cbDown);

        if (View.VISIBLE == mLinearLayout.getVisibility()) {
            mVisibleTag = true;
            openOTCChecxBox.setOnCheckedChangeListener(new OpenOtcCheckBoxListenerImpl(mSubmitBtn, this));
        }


        mSession = SpUtils.getString(this, "mSession", "");
        mPdfs = new ArrayList<>();

        OTC_ElectronicContractEntity assessConfirmBean = OTC_ElectronicContractIntents.get(0);
        mProductCode = assessConfirmBean.getProd_code();
        mProductNo = assessConfirmBean.getProdta_no();

        //判断协议签没签过
        getFinishContract();
        //下载协议
        DownLoadContract(mProductCode);

    }


    @Override
    public void checkResult(Map<String, Boolean> checkedResult) {

        if (checkedResult.get("OpenOtcCheckBoxListenerImpl") != null) {
            mOpenOtcCheckResult = checkedResult.get("OpenOtcCheckBoxListenerImpl");

        }

        if (checkedResult.get("ContractCheckBoxListenerImpl") != null) {
            mContractCheckResult = checkedResult.get("ContractCheckBoxListenerImpl");
        }

        if (mVisibleTag) {
            if (mContractCheckResult && mOpenOtcCheckResult) {
                mSubmitBtn.setBackgroundResource(R.drawable.lonin);                 //背景亮
                mSubmitBtn.setEnabled(true);
            }
        } else {
            if (mContractCheckResult) {
                mSubmitBtn.setBackgroundResource(R.drawable.lonin);                 //背景亮
                mSubmitBtn.setEnabled(true);
            }
        }


    }

    @Override
    public void checkCancelResult(String tag) {
        if ("ContractCheckBoxListenerImpl".equals(tag)) {
            mContractCheckResult = false;
        } else if ("OpenOtcCheckBoxListenerImpl".equals(tag)) {
            mOpenOtcCheckResult = false;

        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__contract_sign;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivOTC_ContractSign_back:
                this.finish();                  //点击返回按钮销毁当前界面
                break;
            case R.id.tvContractSignQD:
                commit();
                break;
            case R.id.tv_open:
                Intent intent = new Intent();
                intent.setClass(OTC_ContractSignActivity.this, PdfActivity.class);
                File file = Helper.getExternalFileDir(CustomApplication.getContext(), "pdf");
                File[] files = file.listFiles();
                intent.putExtra("flag", 1);
                intent.putExtra("filePath", files[0].getPath());
                intent.putExtra("fileName", "《证券投资基金投资人权益须知》");
                startActivity(intent);
                break;
        }
    }


    /**
     * 获取签署过的协议
     */
    private void getFinishContract() {
        //300506
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300506");
        map2.put("PRODTA_NO", mProductNo);
        map1.put("token", mSession);
        map1.put("parms", map2);


        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
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
                    JSONArray data = jsonObject.optJSONArray("data");
                    if ("-6".equals(code)){
                        Intent intent = new Intent(OTC_ContractSignActivity.this, TransactionLoginActivity.class);
                        OTC_ContractSignActivity.this.startActivity(intent);
                        OTC_ContractSignActivity.this.finish();
                    }else if("0".equals(code)){
                        if(data.length() == 0){
                            mLinearLayout.setVisibility(View.VISIBLE);
                        } else {
                            mLinearLayout.setVisibility(View.GONE);
                        }
                    }else{
                        ToastUtils.showShort(OTC_ContractSignActivity.this,msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /**
                 Gson gson = new Gson();
                 Type type = new TypeToken<OTC_AccountQueryListBean>() {}.getType();
                 OTC_AccountQueryListBean bean = gson.fromJson(response, type);
                 String code = bean.getCode();
                 List<OTC_AccountQueryListBean.DataBean> data = bean.getData();
                 if (code.equals("-6")) {
                 Intent intent = new Intent(OTC_ContractSignActivity.this, TransactionLoginActivity.class);
                 OTC_ContractSignActivity.this.startActivity(intent);
                 OTC_ContractSignActivity.this.finish();
                 } else if (code.equals("0")) {

                 if(data.size() == 0){
                 mLinearLayout.setVisibility(View.VISIBLE);
                 } else {
                 mLinearLayout.setVisibility(View.GONE);
                 }

                 }
                 //报session失效
                 */

            }
        });

    }

    /**
     * 加载协议
     *
     * @param code
     */
    private void DownLoadContract(String code) {
        HashMap mapHQTNG106 = new HashMap();
        mapHQTNG106.put("FUNCTIONCODE", "HQTNG106");
        mapHQTNG106.put("TOKEN", "");
        HashMap map100237_1 = new HashMap();
        map100237_1.put("prod_code", code);
        map100237_1.put("prod_kind_type", "3");
        mapHQTNG106.put("PARAMS", map100237_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_HQ_WB(), mapHQTNG106, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                MistakeDialog.showDialog("暂无协议", OTC_ContractSignActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String type = object.getString("type");
                    String message = object.getString("message");
                    if ("200".equals(code)) {
                        JSONObject jsonObject = new JSONObject(message);
                        if (null != jsonObject.getString("open_account_contract") && !"".equals(jsonObject.getString("open_account_contract"))) {
                            PdfEntity pdfBean1 = new PdfEntity();
                            pdfBean1.filename = "《" + jsonObject.getString("open_account_contract") + "》";
                            pdfBean1.fileUrl = jsonObject.getString("open_account_contract_attr");
                            mPdfs.add(pdfBean1);
                        }

                        if (null != jsonObject.getString("prod_risk_book") && !"".equals(jsonObject.getString("prod_risk_book"))) {
                            PdfEntity pdfBean2 = new PdfEntity();
                            pdfBean2.filename = "《" + jsonObject.getString("prod_risk_book") + "》";
                            pdfBean2.fileUrl = jsonObject.getString("prod_risk_book_attr");
                            mPdfs.add(pdfBean2);
                        }

                        if (null != jsonObject.getString("risk_warning_book") && !"".equals(jsonObject.getString("risk_warning_book"))) {
                            PdfEntity pdfBean3 = new PdfEntity();
                            pdfBean3.filename = "《" + jsonObject.getString("risk_warning_book") + "》";
                            pdfBean3.fileUrl = jsonObject.getString("risk_warning_book_attr");
                            mPdfs.add(pdfBean3);
                        }

                        List<String> list = new ArrayList<String>();

                        if (mPdfs != null && mPdfs.size() > 0) {
                            for (int i = 0; i < mPdfs.size(); i++) {
                                if (!TextUtils.isEmpty(mPdfs.get(i).filename)) {
                                    list.add(mPdfs.get(i).filename);
                                }
                            }
                        }

                        mAdapter.setData(list);

                        if (list == null || list.size() <= 0) {
                            MistakeDialog.showDialog("暂无协议", OTC_ContractSignActivity.this, new MistakeDialog.MistakeDialgoListener() {
                                @Override
                                public void doPositive() {
                                    finish();
                                }
                            });
                        }

                    } else {
//                        MistakeDialog.showDialog(type, OTC_ContractSignActivity.this);
                        MistakeDialog.showDialog("暂无协议", OTC_ContractSignActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 签署协议
     */
    private void commit() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map2.put("PRODTA_NO", mProductNo);
        map2.put("PROD_CODE", mProductCode);
        map1.put("funcid", "300432");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
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
                    JSONArray data = jsonObject.optJSONArray("data");
                    if ("0".equals(code)){
                        CentreToast.showText(OTC_ContractSignActivity.this,"委托已提交",true);
                    }else if ("-6".equals(code)){
                        Intent intent = new Intent(OTC_ContractSignActivity.this, TransactionLoginActivity.class);
                        OTC_ContractSignActivity.this.startActivity(intent);
                        OTC_ContractSignActivity.this.finish();
                    }else if ("400".equals(code)){
                        Intent intent2 = new Intent(OTC_ContractSignActivity.this, AgreementActivity.class);
                        OTC_ContractSignActivity.this.startActivity(intent2);
                    }else {
                        MistakeDialog.showDialog(msg, OTC_ContractSignActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /**
                 Gson gson = new Gson();
                 Type type = new TypeToken<OTC_ContractSignCommit>() {
                 }.getType();
                 OTC_ContractSignCommit bean = gson.fromJson(response, type);
                 String code = bean.getCode();
                 String msg = bean.getMsg();
                 if ("-6".equals(code)) {
                 Intent intent = new Intent(OTC_ContractSignActivity.this, TransactionLoginActivity.class);
                 OTC_ContractSignActivity.this.startActivity(intent);
                 OTC_ContractSignActivity.this.finish();
                 } else if ("0".equals(code)) {
                 MistakeDialog.showDialog("委托已提交", OTC_ContractSignActivity.this);
                 } else if ("400".equals(code)) {
                 Intent intent2 = new Intent(OTC_ContractSignActivity.this, Agreement.class);
                 OTC_ContractSignActivity.this.startActivity(intent2);
                 } else {
                 //                    MistakeDialog.showDialog(msg, OTC_ContractSignActivity.this);
                 HandoverDialog.showDialog(msg, OTC_ContractSignActivity.this);
                 }
                 */

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        downLoadPdfDown(mPdfs.get(position).fileUrl, mPdfs.get(position).filename);
    }

    private void downLoadPdfDown(String url, String filename) {
        DownloadDocPdfDialog.getInstance().showDialog(this, downloadPdfCallback, url, filename);
    }

    DownloadDocPdfDialog.DownloadPdfCallback downloadPdfCallback = new DownloadDocPdfDialog.DownloadPdfCallback() {
        @Override
        public void downloadResult(String filePath, String fileName) {
            Intent intent = new Intent();
            intent.setClass(OTC_ContractSignActivity.this, PdfActivity.class);
            intent.putExtra("flag", 1);
            intent.putExtra("filePath", filePath);
            intent.putExtra("fileName", fileName);
            startActivity(intent);
        }
    };

}
