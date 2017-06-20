package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.WareHouseAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetWareConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetWareConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/9/29.
 * 重仓
 */
public class WareHouseView extends MoneyFundBaseView implements ICallbackResult {

    private WareHouseAdapter mAdapter;
    private RelativeLayout mKongLayout;
    private MyListView mListView;
    private static final String TAG = "WareHouseView";

    public WareHouseView(Activity activity, Object object) {
        super(activity, object);
    }

    @Override
    public void initView(View view, Activity activity, Object object) {
        mKongLayout = (RelativeLayout) view.findViewById(R.id.rl_warehose_kong);
        mListView = (MyListView) view.findViewById(R.id.warehoseListView);
        mAdapter = new WareHouseAdapter();
        mListView.setAdapter(mAdapter);

        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;

        if (entity != null && !TextUtils.isEmpty(entity.getSECURITYCODE())) {
            SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
            simpleRemoteControl.setCommand(new ToGetWareConnect(new GetWareConnect(TAG, entity.getSECURITYCODE())));
            simpleRemoteControl.startConnect();
        }
    }

    @Override
    public void getResult(Object result, String tag) {
        Map<String, Object> datas = (Map<String, Object>) result;

        try {

            if (datas == null) {
                mKongLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                return;
            }

            String code = (String)datas.get("code");
            String msg = (String) datas.get("msg");

            if (!TextUtils.isEmpty(code) && "-1".equals(code)) {
                Helper.getInstance().showToast(CustomApplication.getContext(), msg);
                mKongLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                return;
            }

//            List<Object> data =(List<Object>) datas.get("data");
//
//            if (data == null || data.size() <= 0) {
//                mKongLayout.setVisibility(View.VISIBLE);
//                mListView.setVisibility(View.GONE);
//                return;
//            }

            List<Map<String, String>> wareHouseList = (List<Map<String, String>>) datas.get("wareHouseList");

            if (wareHouseList == null || wareHouseList.size() <= 0) {
                mKongLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                return;
            }

            ArrayList<CleverManamgerMoneyEntity> cleverManamgerMoneyEntitys = new ArrayList<>();
            for (Map<String, String> item : wareHouseList) {
                CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
                String secuCode = item.get("secuCode");
                String raionNv = item.get("rationnv");
                String secuBbr = item.get("secuabbr");

                entity.setStockName(secuBbr);
                entity.setStockNumber(secuCode);
                entity.setProgressPersent(raionNv);
                cleverManamgerMoneyEntitys.add(entity);
            }

            mAdapter.setDatas(cleverManamgerMoneyEntitys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_warehose;
    }
}
