package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockCapitalEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.histogram.Histogram;
import com.tpyzq.mobile.pangu.view.pieChart.PieChart;
import com.tpyzq.mobile.pangu.view.pieChart.PieDataEntity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/10/26.
 * 股票资金
 */
public class StockCapital extends BaseStockDetailPager {
    private PieChart pc_capital;
    private int[] mColors;
    private String[] mTitle;
    private Histogram histogram;
    private TextView tv_main_entry;
    private TextView tv_other_entry;
    private TextView tv_main_exit;
    private TextView tv_other_exit;
    private TextView tv_main_entry_val;
    private TextView tv_other_entry_val;

    public StockCapital(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        pc_capital = (PieChart) rootView.findViewById(R.id.pc_capital);
        histogram = (Histogram) rootView.findViewById(R.id.histogram);
        tv_main_entry = (TextView) rootView.findViewById(R.id.tv_main_entry);
        tv_other_entry = (TextView) rootView.findViewById(R.id.tv_other_entry);
        tv_main_exit = (TextView) rootView.findViewById(R.id.tv_main_exit);
        tv_other_exit = (TextView) rootView.findViewById(R.id.tv_other_exit);
        tv_main_entry_val = (TextView) rootView.findViewById(R.id.tv_main_entry_val);
        tv_other_entry_val = (TextView) rootView.findViewById(R.id.tv_other_entry_val);
    }

    @Override
    public void initData(String stockCode) {
        getCaptial(stockCode);
    }

    /**
     * 获取股票资产
     */
    private void getCaptial(String stockcode) {
        HashMap map100207 = new HashMap();
        map100207.put("funcid", "100207");
        map100207.put("token", "");
        HashMap map100207_1 = new HashMap();
        map100207_1.put("secucode", stockcode);
        map100207.put("parms", map100207_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map100207, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(mContext, "网络访问失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    String data = object.optString("data");
                    String msg = object.optString("msg");
                    JSONArray dataArray = new JSONArray(data);
                    if (code.equals("0")) {
                        StockCapitalEntity stockCapitalBean = new Gson().fromJson(dataArray.getString(0), StockCapitalEntity.class);
                        setData(stockCapitalBean);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        HashMap map100208 = new HashMap();
        map100208.put("funcid", "100208");
        map100208.put("token", "");
        HashMap map100208_1 = new HashMap();
        map100208_1.put("secucode", stockcode);
        map100208.put("parms", map100207_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map100208, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(mContext, "网络访问失败");
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
                    if (code.equals("0")) {
                        JSONArray dataArray = new JSONArray(data);
                        if (dataArray.length() == 5) {
                            float[] ZJC = new float[5];
                            ZJC[0] = TransitionUtils.price2tenthousandF(dataArray.getJSONObject(4).getString("ZJC"));
                            ZJC[1] = TransitionUtils.price2tenthousandF(dataArray.getJSONObject(3).getString("ZJC"));
                            ZJC[2] = TransitionUtils.price2tenthousandF(dataArray.getJSONObject(2).getString("ZJC"));
                            ZJC[3] = TransitionUtils.price2tenthousandF(dataArray.getJSONObject(1).getString("ZJC"));
                            ZJC[4] = TransitionUtils.price2tenthousandF(dataArray.getJSONObject(0).getString("ZJC"));
                            String[] TRADINGDATE = new String[5];
                            TRADINGDATE[0] = dataArray.getJSONObject(4).getString("TRADINGDATE").substring(5);
                            TRADINGDATE[1] = dataArray.getJSONObject(3).getString("TRADINGDATE").substring(5);
                            TRADINGDATE[2] = dataArray.getJSONObject(2).getString("TRADINGDATE").substring(5);
                            TRADINGDATE[3] = dataArray.getJSONObject(1).getString("TRADINGDATE").substring(5);
                            TRADINGDATE[4] = dataArray.getJSONObject(0).getString("TRADINGDATE").substring(5);
                            histogram.updateThisData(ZJC, TRADINGDATE);
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData(StockCapitalEntity stockCapitalBean) {
//        mColors = new int[]{0xFFF2AD27, 0xFF22AA46, 0xFF56D89D, 0xFFE84242};
        mColors = new int[]{0xFFF97A7A, 0xFF22AA46, 0xFF56D89D, 0xFFE84242};
        mTitle = new String[]{"散户流入", "主力流出", "散户流出", "主力流入"};
        List<PieDataEntity> dataEntities = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PieDataEntity entity;
            switch (i) {
                case 0:
                    entity = new PieDataEntity("name" + i, parseFloat(stockCapitalBean.SR), mColors[i], mTitle[i]);
                    dataEntities.add(entity);
                    break;
                case 1:
                    entity = new PieDataEntity("name" + i, parseFloat(stockCapitalBean.ZC), mColors[i], mTitle[i]);
                    dataEntities.add(entity);
                    break;
                case 2:
                    entity = new PieDataEntity("name" + i, parseFloat(stockCapitalBean.SC), mColors[i], mTitle[i]);
                    dataEntities.add(entity);
                    break;
                case 3:
                    entity = new PieDataEntity("name" + i, parseFloat(stockCapitalBean.ZR), mColors[i], mTitle[i]);
                    dataEntities.add(entity);
                    break;
            }
        }
        pc_capital.setDataList(dataEntities);
        tv_main_entry.setText(TransitionUtils.price2tenthousand(stockCapitalBean.MZR));
        tv_main_exit.setText(TransitionUtils.price2tenthousand(stockCapitalBean.MZC));
        tv_other_entry.setText(TransitionUtils.price2tenthousand(stockCapitalBean.MSR));
        tv_other_exit.setText(TransitionUtils.price2tenthousand(stockCapitalBean.MSC));
        String JMZR = TransitionUtils.price2tenthousand(stockCapitalBean.JMZR);
        String JMSR = TransitionUtils.price2tenthousand(stockCapitalBean.JMSR);
        if (Double.parseDouble(JMZR)>0){
            tv_main_entry_val.setTextColor(ColorUtils.RED);
        }else if (Double.parseDouble(JMZR)<0){
            tv_main_entry_val.setTextColor(ColorUtils.GREEN);
        }else if (Double.parseDouble(JMZR)==0){
            tv_main_entry_val.setTextColor(ColorUtils.BLACK);
        }
        if (Double.parseDouble(JMSR)>0){
            tv_other_entry_val.setTextColor(ColorUtils.RED);
        }else if (Double.parseDouble(JMSR)<0){
            tv_other_entry_val.setTextColor(ColorUtils.GREEN);
        }else if (Double.parseDouble(JMSR)==0){
            tv_other_entry_val.setTextColor(ColorUtils.BLACK);
        }
        tv_main_entry_val.setText(JMZR);
        tv_other_entry_val.setText(JMSR);
    }

    private float parseFloat(String value){
        float result = 0;
        try {
            result = Float.parseFloat(value);
        }catch (Exception e){
        }
        return result;
    }

    @Override
    public int getLayoutId() {
        return R.layout.vp_stock_capital;
    }
}
