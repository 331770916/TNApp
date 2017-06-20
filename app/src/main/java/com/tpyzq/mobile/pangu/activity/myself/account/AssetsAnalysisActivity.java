package com.tpyzq.mobile.pangu.activity.myself.account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundRedemptionEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.JSAPI;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.tpyzq.mobile.pangu.view.webview.ProgressWebView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 资产分析
 */
public class AssetsAnalysisActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "AssetsAnalysisActivity";
    ProgressWebView wb_assets_analysis;
    private JSAPI mJsapi;
    private String mSession;
    private String mStock_assert_val;
    private String mFund_money;
    private String mMoney_manage;
    private String mCrash_money;
    private String mMKT_VAL;
    private String mTOTAL_INCOME_BAL;
    private String mMtotal_income;

    List<HashMap<String, String>> mStockto_list_map;
    List<HashMap<String, String>> mFund_list_map;
    List<HashMap<String, String>> mMoney_list_map;
    private String mURL;
    private Dialog loadingDialog, HC_LoadingDialog;
    private RelativeLayout layout;
    private ShareDialog shareDialog;

    @Override
    public void initView() {
        initJSAPI();
        HC_LoadingDialog = LoadingDialog.initDialog(this, "加载中…");
        mSession = SpUtils.getString(this, "mSession", "");
        wb_assets_analysis = (ProgressWebView) findViewById(R.id.wb_assets_analysis);
        layout = (RelativeLayout) findViewById(R.id.stockNewsLayout);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_fenxiang).setOnClickListener(this);
        layout.setOnClickListener(this);
        setData();
        UserUtil.refrushUserInfo();
        getStockosition();
    }

    private void setData() {
        mStockto_list_map = new ArrayList<HashMap<String, String>>();
        mFund_list_map = new ArrayList<HashMap<String, String>>();
        mMoney_list_map = new ArrayList<HashMap<String, String>>();
        shareDialog = new ShareDialog(this);
    }

    /* 股票金额   (股票持仓)*/
    private void getStockosition() {

        HC_LoadingDialog.show();
        Map map_300201 = new HashMap();
        Map map2_300201 = new HashMap();
        map_300201.put("funcid", "300201");
        map_300201.put("token", mSession);
        map_300201.put("parms", map2_300201);
        map2_300201.put("SEC_ID", "tpyzq");
        map2_300201.put("FLAG", true);
        map2_300201.put("MARKET", "");
        map2_300201.put("SECU_CODE", "");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map_300201, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogHelper.e(TAG, e.toString());
                        setShow();
                        Helper.getInstance().showToast(AssetsAnalysisActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if ("0".equals(code)) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                if (data != null && data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {
                                        mStock_assert_val = data.getJSONObject(i).getString("ASSERT_VAL");       //总资产
                                        mMKT_VAL = data.getJSONObject(i).getString("MKT_VAL");                   //市值
                                        mTOTAL_INCOME_BAL = data.getJSONObject(i).getString("TOTAL_INCOME_BAL"); //盈亏

                                        JSONArray income_list = data.getJSONObject(i).getJSONArray("INCOME_LIST");
                                        for (int j = 0; j < income_list.length(); j++) {
                                            HashMap<String, String> Stock_hashMap = new HashMap<String, String>();
                                            Stock_hashMap.put("name", income_list.getJSONObject(j).getString("SECU_NAME"));
                                            Stock_hashMap.put("code", income_list.getJSONObject(j).getString("SECU_CODE"));
                                            Stock_hashMap.put("price", income_list.getJSONObject(j).getString("MKT_VAL"));
                                            mStockto_list_map.add(Stock_hashMap);
                                        }
                                    }
                                }
                                getFundPosition();
                            } else if ("-6".equals(jsonObject.getString("code"))) {
                                startActivity(new Intent(AssetsAnalysisActivity.this, TransactionLoginActivity.class));
                            } else {
                                setShow();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Helper.getInstance().showToast(AssetsAnalysisActivity.this, "网络异常");
                        }
                    }
                }
        );
    }

    /*基金金额  (基金持仓查询)*/
    private void getFundPosition() {
        HashMap map_720260 = new HashMap();
        map_720260.put("funcid", "720260");
        map_720260.put("token", mSession);
        HashMap map1_720260 = new HashMap();
        map_720260.put("parms", map1_720260);
        map1_720260.put("SEC_ID", "tpyzq");
        map1_720260.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map_720260, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                setShow();
                Helper.getInstance().showToast(AssetsAnalysisActivity.this, "网络异常");
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
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        FundRedemptionEntity fundRedemptionBean = new Gson().fromJson(jsonArray.getString(0), FundRedemptionEntity.class);
                        mFund_money = fundRedemptionBean.OPFUND_MARKET_VALUE;       //总盈亏
                        mMtotal_income = fundRedemptionBean.TOTAL_INCOME;           //市值

                        if (fundRedemptionBean != null && fundRedemptionBean.RESULT_LIST.size() > 0) {
                            for (int i = 0; i < fundRedemptionBean.RESULT_LIST.size(); i++) {
                                HashMap<String, String> fund_hashMap = new HashMap<String, String>();
                                fund_hashMap.put("name", fundRedemptionBean.RESULT_LIST.get(i).FUND_NAME);
                                fund_hashMap.put("code", fundRedemptionBean.RESULT_LIST.get(i).FUND_CODE);
                                fund_hashMap.put("price", fundRedemptionBean.RESULT_LIST.get(i).CURRENT_SHARE);
                                mFund_list_map.add(fund_hashMap);
                            }
                        }
                        getOTCPosition();
                    } else if ("-6".equals(data)) {
                        startActivity(new Intent(AssetsAnalysisActivity.this, TransactionLoginActivity.class));
                    } else {
                        setShow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(AssetsAnalysisActivity.this, "网络异常");
                }
            }
        });
    }

    /*理财金额  (证券理财份额查询)*/
    private void getOTCPosition() {
        HashMap map_300501 = new HashMap();
        HashMap map1_300501 = new HashMap();
        map1_300501.put("FLAG", "true");
        map1_300501.put("SEC_ID", "tpyzq");
        map_300501.put("funcid", "300501");
        map_300501.put("token", mSession);
        map_300501.put("parms", map1_300501);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map_300501, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                setShow();
                Helper.getInstance().showToast(AssetsAnalysisActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                mMoney_manage = data.getJSONObject(i).getString("OTC_MARKET_VALUE");
                                JSONArray otc_list = data.getJSONObject(i).getJSONArray("OTC_LIST");
                                if (otc_list != null && otc_list.length() > 0) {
                                    for (int j = 0; j < otc_list.length(); j++) {
                                        HashMap<String, String> money_hashMap = new HashMap<String, String>();
                                        money_hashMap.put("name", otc_list.getJSONObject(j).getString("PROD_NAME"));
                                        money_hashMap.put("price", otc_list.getJSONObject(j).getString("CURRENT_AMOUNT"));
                                        mMoney_list_map.add(money_hashMap);
                                    }
                                }
                            }
                        }
                        getNowMoney();
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(AssetsAnalysisActivity.this, TransactionLoginActivity.class));
                    } else {
                        setShow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*现金(客户资产和市值查询)*/
    private void getNowMoney() {
        HashMap map_300608 = new HashMap();
        map_300608.put("funcid", "300608");
        map_300608.put("token", mSession);
        HashMap map1_300608 = new HashMap();
        map_300608.put("parms", map1_300608);
        map1_300608.put("SEC_ID", "tpyzq");
        map1_300608.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map_300608, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(e.toString());
                setShow();
                Helper.getInstance().showToast(AssetsAnalysisActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    JSONArray data = jsonObject.getJSONArray("data");
                    if ("0".equals(code)) {
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                mCrash_money = data.getJSONObject(i).getString("ENABLE_BALANCE");
                            }
                        }
                        MoneyToConnect();
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(AssetsAnalysisActivity.this, TransactionLoginActivity.class));
                    } else {
                        setShow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void MoneyToConnect() {
        mJsapi.setMoney(mCrash_money);
        /*资产分析分享*/
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("FUNCTIONCODE", "HQFNG002");
        map.put("PARAMS", map1);
        map1.put("account", UserUtil.capitalAccount);
        map1.put("stock_money", mMKT_VAL);          //股票金额
        map1.put("fund_money", mFund_money);        //基金金额
        map1.put("money_manage", mMoney_manage);    //理财金额
        map1.put("crash_money", mCrash_money);      //现金

        map1.put("count", mStock_assert_val);          //总资产

        map1.put("stock_profit", mTOTAL_INCOME_BAL); //股票盈亏
        map1.put("fund_profit", mMtotal_income);     //基金盈亏
        map1.put("money_profit", "0");                 //理财盈亏
        map1.put("crash_profit", "0");                 //现金盈亏

        map1.put("stoce_obj", mStockto_list_map);
        map1.put("fund_obj", mFund_list_map);
        map1.put("money_obj", mMoney_list_map);
//                                                                URL_H5_New  URL_H5
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_H5_New, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (HC_LoadingDialog != null) {
                    HC_LoadingDialog.dismiss();
                }
                LogUtil.e(e.toString());
                setShow();
                Helper.getInstance().showToast(AssetsAnalysisActivity.this, "数据异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        if (HC_LoadingDialog != null) {
                            HC_LoadingDialog.dismiss();
                        }
                        String msg = jsonObject.getString("msg");
                        LogUtil.e("aaaaaa", msg);
                        setData(msg);
                        mURL = msg;
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        startActivity(new Intent(AssetsAnalysisActivity.this, TransactionLoginActivity.class));
                    } else {
                        setShow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setData(String url) {
        WebSettings setting = wb_assets_analysis.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wb_assets_analysis.setScrollbarFadingEnabled(true);
        wb_assets_analysis.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wb_assets_analysis.requestFocus();
        wb_assets_analysis.addJavascriptInterface(mJsapi, "Android");
        wb_assets_analysis.removeJavascriptInterface("searchBoxJavaBridge_");
        wb_assets_analysis.removeJavascriptInterface("accessibility");
        wb_assets_analysis.removeJavascriptInterface("accessibilityTraversal");
        wb_assets_analysis.getSettings().setSavePassword(false);
        wb_assets_analysis.loadUrl(url);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_fenxiang:
                loadingDialog = LoadingDialog.initDialog(this, "加载中…");
                loadingDialog.show();       //显示菊花
                getShare();
                break;
            case R.id.stockNewsLayout:
                HC_LoadingDialog = LoadingDialog.initDialog(this, "加载中…");
                HC_LoadingDialog.show();
                getStockosition();
                wb_assets_analysis.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                break;
        }
    }


    /**
     * 截屏 获取图片
     */
    private void getShare() {
        loadingDialog.dismiss();
        shareDialog.setUrl(mURL + "?hidden=1");
        shareDialog.show();
    }

    private void setShow() {
        if (HC_LoadingDialog != null) {
            HC_LoadingDialog.dismiss();
        }
        wb_assets_analysis.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }


    private void initJSAPI() {
        mJsapi = JSAPI.getInctance();
        mJsapi.setActivity(AssetsAnalysisActivity.this);
        mJsapi.setHandler(handler);

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                wb_assets_analysis.loadUrl("javascript:stockDetial()");
                wb_assets_analysis.loadUrl("javascript:fundDetial()");
                wb_assets_analysis.loadUrl("javascript:manageDetial()");
                wb_assets_analysis.loadUrl("javascript:crashDetial()");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_assets_analysis;
    }
}
