package com.tpyzq.mobile.pangu.activity.home.managerMoney.product;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.MistakeFundHistoryValueAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetFundHistoryValueConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetFundHistoryValueConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/10/17.
 */
public class MoneyFundHistoryListActivity extends BaseActivity implements View.OnClickListener, ICallbackResult {

    private MistakeFundHistoryValueAdapter mAdapter;
    private ArrayList<CleverManamgerMoneyEntity> mEntities;
    private String mFundTypeCode;
    private static final String TAG = "MoneyFundHistoryListActivity";

    @Override
    public void initView() {
        findViewById(R.id.fundHistorylist_back).setOnClickListener(this);

        Intent intent = getIntent();
        String fundCode = intent.getStringExtra("fundCode");
        mFundTypeCode = intent.getStringExtra("fundTypeCode");
        if (TextUtils.isEmpty(mFundTypeCode) || TextUtils.isEmpty(mFundTypeCode)) {
            return;
        }

        TextView middleTitle = (TextView) findViewById(R.id.fundHistoryListValueMiddleTitle);
        TextView rightTilte = (TextView) findViewById(R.id.fundHistoryListValueRightTitle);

        if (ConstantUtil.MONEYTYPE.equals(mFundTypeCode)) {//货币基金
            middleTitle.setText("每万份收益");
            rightTilte.setText("七日年化收益率");
        } else {
            middleTitle.setText("单位净值");
            rightTilte.setText("累计净值");
        }

        ListView listView = (ListView) findViewById(R.id.fundHistorylistView);
        mAdapter = new MistakeFundHistoryValueAdapter();
        listView.setAdapter(mAdapter);

        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
        simpleRemoteControl.setCommand(new ToGetFundHistoryValueConnect(new GetFundHistoryValueConnect(TAG, fundCode, getMounthDate()[1], getMounthDate()[0], "", "", "")));
        simpleRemoteControl.startConnect();
    }

    /**
     * 获取一个月的起始时间
     * @return
     */
    private String[] getMounthDate() {
        String[] dates = new String[2];
        String startDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        String endDate = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());

        dates[0] = startDate;
        dates[1] = endDate;
        return dates;
    }

    @Override
    public void getResult(Object result, String tag) {
        Map<String, Object> datas = (Map<String, Object>) result;

        try {
            if (datas == null) {
                return;
            }

            String code = (String) datas.get("code");
            String msg = (String) datas.get("msg");

            if (!TextUtils.isEmpty(code) && "-1".equals(code)) {
                CentreToast.showText(CustomApplication.getContext(), msg);
                return;
            }

//            List<Object> data = (List<Object>) datas.get("data");
//
//            if (data == null || data.size() <= 0) {
//                return;
//            }

            List<Map<String, String>> chartDatasList = (List<Map<String, String>>) datas.get("chartDatasList");

            if (chartDatasList == null || chartDatasList.size() <= 0) {
                return;
            }

            mEntities = new ArrayList<>();
            for (int i = 0; i < chartDatasList.size(); i++) {

                CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
                String date = chartDatasList.get(i).get("endDate");
                String unitnv = chartDatasList.get(i).get("unitnv");//单位净值
                String accumulatedunitnv = chartDatasList.get(i).get("accumulatedunitnv");//累计净值

                String latestweeklyyield = chartDatasList.get(i).get("latestweeklyyield");//七日年化收益率
                String dailyProfit = chartDatasList.get(i).get("dailyProfit");      //每万分收益

                entity.setHistoryNetValueDate(Helper.formateDate2(date));
                entity.setHistoryNetValueUnit(unitnv);
                entity.setHistoryNetValueTotal(accumulatedunitnv);
                entity.setLATESTWEEKLYYIELD(latestweeklyyield);
                entity.setDAILYPROFIT(dailyProfit);
                mEntities.add(entity);

            }

            mAdapter.setDatas(mEntities, mFundTypeCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fundHistorylist_back:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_moneyfundhistory;
    }
}
