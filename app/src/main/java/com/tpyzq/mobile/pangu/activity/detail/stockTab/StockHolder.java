package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.detail.StockHolderAdapter;
import com.tpyzq.mobile.pangu.data.StockHolderEntity;
import com.tpyzq.mobile.pangu.data.StockHolderTop10Entity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/10/30.
 * 股东
 */
public class StockHolder extends BaseStockDetailPager {
    TextView tv_view1_01;
    TextView tv_view1_02;
    TextView tv_view1_03;
    TextView tv_view1_04;
    ListView lv_stockholder;
    List<StockHolderTop10Entity> stockHolderTop10Been;
    StockHolderAdapter stockHolderAdapter;
    View v_null;
    public StockHolder(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        tv_view1_01 = (TextView) rootView.findViewById(R.id.tv_view1_01);
        tv_view1_02 = (TextView) rootView.findViewById(R.id.tv_view1_02);
        tv_view1_03 = (TextView) rootView.findViewById(R.id.tv_view1_03);
        tv_view1_04 = (TextView) rootView.findViewById(R.id.tv_view1_04);
        lv_stockholder = (ListView) rootView.findViewById(R.id.lv_stockholder);

        v_null = rootView.findViewById(R.id.v_null);

    }

    @Override
    public void initData(String stockCode) {
        stockHolderTop10Been = new ArrayList<StockHolderTop10Entity>();
        stockHolderAdapter  = new StockHolderAdapter(mContext);
        lv_stockholder.setAdapter(stockHolderAdapter);
        lv_stockholder.setEmptyView(v_null);
        getStockHolder(stockCode);
    }

    private void getStockHolder(String stockcode) {
        HashMap map100202 = new HashMap();
        map100202.put("funcid", "100202");
        map100202.put("token", "");
        HashMap map100202_1 = new HashMap();
        map100202_1.put("secucode", stockcode);
        map100202.put("parms", map100202_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map100202, new StringCallback() {
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
                    if (code.equals("0")) {
                        JSONArray dataArray = new JSONArray(data);
                        JSONObject dateObject = dataArray.getJSONObject(0);
                        StockHolderEntity stockHolderBean = new StockHolderEntity();
                        stockHolderBean.AFLOATS = dateObject.getString("AFLOATS");
                        stockHolderBean.AVERAGEHOLDSUM = dateObject.getString("AVERAGEHOLDSUM");
                        stockHolderBean.SHNUM = dateObject.getString("SHNUM");
                        stockHolderBean.TOTALSHARES = dateObject.getString("TOTALSHARES");
                        setText(stockHolderBean);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        HashMap map100203 = new HashMap();
        map100203.put("funcid", "100203");
        map100203.put("token", "");
        HashMap map100203_1 = new HashMap();
        map100203_1.put("secucode", stockcode);
        map100203.put("parms", map100203_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map100203, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogUtil.e("十大股东", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    JSONArray dataArray = new JSONArray(data);
                    if (code.equals("0")) {
                        stockHolderTop10Been.clear();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObejct = dataArray.getJSONObject(i);
                            StockHolderTop10Entity stockHolderTop10Bean = new StockHolderTop10Entity();
                            stockHolderTop10Bean.ENDDATE = dataObejct.getString("ENDDATE");
                            stockHolderTop10Bean.HOLDSUMCHANGERATE = dataObejct.getString("HOLDSUMCHANGERATE");
                            stockHolderTop10Bean.PCTOFTOTALSHARES = dataObejct.getString("PCTOFTOTALSHARES");
                            stockHolderTop10Bean.SHLIST = dataObejct.getString("SHLIST");
                            stockHolderTop10Bean.SHNO = dataObejct.getString("SHNO");
                            stockHolderTop10Been.add(stockHolderTop10Bean);
                        }
                        stockHolderAdapter.setStockHolderTop10Beans(stockHolderTop10Been);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setText(StockHolderEntity stockHolderBean) {
        tv_view1_01.setText(TransitionUtils.long2million(stockHolderBean.TOTALSHARES));
        tv_view1_02.setText(TransitionUtils.long2million(stockHolderBean.AFLOATS));
        tv_view1_03.setText(TransitionUtils.long2million(stockHolderBean.SHNUM));
        tv_view1_04.setText(TransitionUtils.long2million(stockHolderBean.AVERAGEHOLDSUM));
    }

    @Override
    public int getLayoutId() {
        return R.layout.vp_stock_holder;
    }
}
