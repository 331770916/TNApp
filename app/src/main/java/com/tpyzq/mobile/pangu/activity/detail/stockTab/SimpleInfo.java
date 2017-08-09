package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockCompanyInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/10/29.
 * 简况页签
 */
public class SimpleInfo extends BaseStockDetailPager {
    TextView tv_company_name;
    TextView tv_timetomarket;
    TextView tv_issue_price;
    TextView tv_issue_sum;
    TextView tv_area;
    TextView tv_trade;
    TextView tv_business;
    TextView tv_compand_info;

    public SimpleInfo(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        tv_company_name = (TextView) rootView.findViewById(R.id.tv_company_name);
        tv_timetomarket = (TextView) rootView.findViewById(R.id.tv_timetomarket);
        tv_issue_price = (TextView) rootView.findViewById(R.id.tv_issue_price);
        tv_issue_sum = (TextView) rootView.findViewById(R.id.tv_issue_sum);
        tv_area = (TextView) rootView.findViewById(R.id.tv_area);
        tv_trade = (TextView) rootView.findViewById(R.id.tv_trade);
        tv_business = (TextView) rootView.findViewById(R.id.tv_business);
        tv_compand_info = (TextView) rootView.findViewById(R.id.tv_compand_info);
    }

    @Override
    public void initData(String stockCode) {
        getSimpleInfo(stockCode);
    }
    /**
     * 获取股票资产
     */
    private void getSimpleInfo(String stockcode) {
        HashMap map100200 = new HashMap();
        map100200.put("funcid", "100200");
        map100200.put("token", "");
        HashMap map100200_1 = new HashMap();
        map100200_1.put("secucode", stockcode);
        map100200.put("parms", map100200_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map100200, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    JSONArray dataArray = new JSONArray(data);
                    if (code.equals("0")) {
                        StockCompanyInfoEntity stockCompanyInfoBean = new Gson().fromJson(dataArray.getString(0),StockCompanyInfoEntity.class);
                        setText(stockCompanyInfoBean);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setText(StockCompanyInfoEntity stockCompanyInfoBean) {
        tv_company_name.setText(stockCompanyInfoBean.CHINAME);
        tv_timetomarket.setText(stockCompanyInfoBean.LISTDATE);
        tv_issue_price.setText(stockCompanyInfoBean.ISSUEPRICE);
        tv_issue_sum.setText(stockCompanyInfoBean.ISSUEVOL);
        tv_area.setText(stockCompanyInfoBean.MS);
        tv_trade.setText(stockCompanyInfoBean.INDUSTRYNAME);
        tv_business.setText(stockCompanyInfoBean.BUSINESSMAJOR);
        tv_compand_info.setText(stockCompanyInfoBean.BRIEFINTROTEXT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.vp_stock_simpleinfo;
    }
}
