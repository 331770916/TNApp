package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.PdfActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.ContractEntity;
import com.tpyzq.mobile.pangu.data.PdfEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.dialog.DownloadDocPdfDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 签署协议界面
 */
public class SignActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button bt_sure;
    private ImageView iv_back;
    private LinearLayout ll_view2;
    private CheckBox cb_sign_protocol;
    private CheckBox cb_open_fund;
    private ContractEntity contractBean;
    private ListView lv_agreement;
    private List<PdfEntity> pdfs;
    private ArrayAdapter<String> adapter;
    private String session;
    private TextView tv_notice;

    @Override
    public void initView() {
        bt_sure = (Button) findViewById(R.id.bt_sure);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_view2 = (LinearLayout) findViewById(R.id.ll_view2);
        cb_sign_protocol = (CheckBox) findViewById(R.id.cb_sign_protocol);
        cb_open_fund = (CheckBox) findViewById(R.id.cb_open_fund);
        lv_agreement = (ListView) findViewById(R.id.lv_agreement);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        initData();

    }

    private void initData() {
        bt_sure.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
        pdfs = new ArrayList<>();
        session = SpUtils.getString(this, "mSession", null);
        Intent intent = getIntent();
        contractBean = (ContractEntity) intent.getSerializableExtra("sign");
        getFundData(contractBean.FUND_COMPANY);
        getContract(contractBean.FUND_CODE);
        cb_sign_protocol.setOnCheckedChangeListener(this);
        cb_open_fund.setOnCheckedChangeListener(this);
        bt_sure.setClickable(false);
        lv_agreement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPdfDown(pdfs.get(position).fileUrl, pdfs.get(position).filename);
            }
        });
    }

    private void getContract(String code) {
        HashMap map100237 = new HashMap();
        map100237.put("funcid", "100237");
        map100237.put("token", session);
        HashMap map100237_1 = new HashMap();
        map100237_1.put("prodcode", code);
        map100237.put("parms", map100237_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map100237, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MistakeDialog.showDialog(e.toString(), SignActivity.this);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    MistakeDialog.showDialog("暂无协议", SignActivity.this, callback);
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("PROAOCOLDATE");
                    if (code.equals("0")) {
                        JSONArray jsonArray = new JSONArray(data);
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
                        adapter = new ArrayAdapter<String>(SignActivity.this, R.layout.item_agreement, list);
                        lv_agreement.setAdapter(adapter);
                    }
                    if (pdfs.size() == 0) {
                        MistakeDialog.showDialog("暂无协议", SignActivity.this, callback);
                    }
                } catch (JSONException e) {
                    MistakeDialog.showDialog("暂无协议", SignActivity.this, callback);
                    e.printStackTrace();
                }
            }
        });
    }

    MistakeDialog.MistakeDialgoListener callback = new MistakeDialog.MistakeDialgoListener() {
        @Override
        public void doPositive() {
            finish();
        }
    };

    private void setPdfDown(String url, String filename) {
        DownloadDocPdfDialog.getInstance().showDialog(this, downloadPdfCallback, url, filename);
    }

    DownloadDocPdfDialog.DownloadPdfCallback downloadPdfCallback = new DownloadDocPdfDialog.DownloadPdfCallback() {
        @Override
        public void downloadResult(String filePath, String fileName) {
            Intent intent = new Intent();
            intent.setClass(SignActivity.this, PdfActivity.class);
            intent.putExtra("filePath", filePath);
            intent.putExtra("fileName", fileName);
            intent.putExtra("flag", 1);
            startActivity(intent);
        }
    };

    /**
     * 获取基金数据
     */
    private void getFundData(String fund_company) {
        HashMap map300435 = new HashMap();
        map300435.put("funcid", "300435");
        map300435.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300435_1 = new HashMap();
        map300435_1.put("SEC_ID", "tpyzq");
        map300435_1.put("FLAG", "true");
        map300435_1.put("FUND_COMPANY", fund_company);
        map300435.put("parms", map300435_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300435, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MistakeDialog.showDialog(e.toString(), SignActivity.this);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    JSONArray jsonArray = new JSONArray(data);
                    if ("0".equals(code)) {
                        if (jsonArray.length() <= 0) {
                            ll_view2.setVisibility(View.VISIBLE);
                        } else {
                            ll_view2.setVisibility(View.GONE);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(SignActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(SignActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取基金数据
     */
    private void setFund(String fund_code, String fund_company) {
        HashMap map300432 = new HashMap();
        map300432.put("funcid", "300432");
        map300432.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300432_1 = new HashMap();
        map300432_1.put("SEC_ID", "tpyzq");
        map300432_1.put("FLAG", "true");
        map300432_1.put("FUND_CODE", fund_code);
        map300432_1.put("FUND_COMPANY", fund_company);
        map300432.put("parms", map300432_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300432, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MistakeDialog.showDialog(e.toString(), SignActivity.this);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    JSONArray jsonArray = new JSONArray(data);
                    if ("0".equals(code)) {
                        ResultDialog.getInstance().show("签署成功", R.mipmap.lc_success);
                        setResult(1);
                        finish();
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(SignActivity.this, TransactionLoginActivity.class));
                    } else if ("400".equals(code)) {
//                        startActivity(new Intent(SignActivity.this, Agreement.class));
                    } else {
                        ToastUtils.showShort(SignActivity.this, msg);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                setFund(contractBean.FUND_CODE, contractBean.FUND_COMPANY);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_notice:
                String path = Helper.getAppFileDirPath(CustomApplication.getContext()) + "pdf/fundOpenUser.pdf";
                Intent intent = new Intent();
                intent.setClass(SignActivity.this, PdfActivity.class);
                intent.putExtra("flag", 1);
                intent.putExtra("filePath", path);
                intent.putExtra("fileName", "基金投资人权益须知");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_sign_protocol:
                if (ll_view2.isShown()) {
                    if (cb_open_fund.isChecked()) {
                        if (isChecked) {
                            bt_sure.setBackgroundColor(ColorUtils.ORANGE);
                            bt_sure.setClickable(true);
                        } else {
                            bt_sure.setBackgroundColor(ColorUtils.BT_GRAY);
                            bt_sure.setClickable(false);
                        }
                    } else {
                        bt_sure.setBackgroundColor(ColorUtils.BT_GRAY);
                        bt_sure.setClickable(false);
                    }

                } else {
                    if (isChecked) {
                        bt_sure.setBackgroundColor(ColorUtils.ORANGE);
                        bt_sure.setClickable(true);
                    } else {
                        bt_sure.setBackgroundColor(ColorUtils.BT_GRAY);
                        bt_sure.setClickable(false);
                    }
                }
                break;
            case R.id.cb_open_fund:
                if (cb_sign_protocol.isChecked()) {
                    if (isChecked) {
                        bt_sure.setBackgroundColor(ColorUtils.ORANGE);
                        bt_sure.setClickable(true);
                    } else {
                        bt_sure.setBackgroundColor(ColorUtils.BT_GRAY);
                        bt_sure.setClickable(false);
                    }
                } else {
                    bt_sure.setBackgroundColor(ColorUtils.BT_GRAY);
                    bt_sure.setClickable(false);
                }
                break;
        }
    }
}
