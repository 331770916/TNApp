package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.MoneyFundHistoryListActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.MistakeFundHistoryValueAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetFundHistoryValueConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetFundHistoryValueConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/9/26.
 * 非货币基金历史净值
 */
public class MoneyFundFundHistoryValue extends MoneyFundBaseView implements ICallbackResult, AdapterView.OnItemClickListener{

    private MistakeFundHistoryValueAdapter mAdapter;
    private Activity mActivity;
    private ArrayList<CleverManamgerMoneyEntity> mEntities;
    private static final String TAG ="MoneyFundFundHistoryValue";
    private String mFundCode;
    private String mFundTypeCode;

    public MoneyFundFundHistoryValue(Activity activity, Object object) {
        super(activity, object);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity, Object object) {
        mActivity = activity;
        TextView middleTv = (TextView) view.findViewById(R.id.fundHistoryValueMiddleTitle);
        TextView rightTv = (TextView) view.findViewById(R.id.fundHistoryValueRightTitle);

        MyListView listView = (MyListView) view.findViewById(R.id.mistakeFundHistoryValueList);
        mAdapter = new MistakeFundHistoryValueAdapter();
        listView.setAdapter(mAdapter);
        View v = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.foot_mistakefundhistory, null);
        listView.addFooterView(v);

        listView.setOnItemClickListener(this);


        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;

        if (!TextUtils.isEmpty(entity.getFUNDTYPECODE())) {
            mFundTypeCode = entity.getFUNDTYPECODE();
        }

        if (ConstantUtil.MONEYTYPE.equals(mFundTypeCode)) {//货币基金
            middleTv.setText("每万份收益");
            rightTv.setText("七日年化收益率");
        } else {
            middleTv.setText("单位净值");
            rightTv.setText("累计净值");
        }


        if (entity != null && !TextUtils.isEmpty(entity.getSECURITYCODE())) {
            mFundCode = entity.getSECURITYCODE();


            SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
            simpleRemoteControl.setCommand(new ToGetFundHistoryValueConnect(new GetFundHistoryValueConnect(TAG, mFundCode, getMounthDate()[1], getMounthDate()[0], "", "", "")));
            simpleRemoteControl.startConnect();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (mEntities != null && position == mEntities.size()) {
            if (!TextUtils.isEmpty(mFundCode)) {
                intent.putExtra("fundCode", mFundCode);
            }

            if (!TextUtils.isEmpty(mFundTypeCode)) {
                intent.putExtra("fundTypeCode", mFundTypeCode);
            }

            intent.setClass(mActivity, MoneyFundHistoryListActivity.class);
            mActivity.startActivity(intent);
        }

    }

    /**
     * 获取一个月的起始时间
     * @return
     */
    private String[] getMounthDate () {
        String [] dates = new String[2];
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

            String code = (String)datas.get("code");
            String msg = (String) datas.get("msg");

            if (!TextUtils.isEmpty(code) && "-1".equals(code)) {
                CentreToast.showText(CustomApplication.getContext(), msg);
                return;
            }

//            List<Object> data =(List<Object>) datas.get("data");
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

                if (i < 7) {
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
            }

            mAdapter.setDatas(mEntities, mFundTypeCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.view_mistakefund_historyvalue;
    }
}
