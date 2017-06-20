package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_BargainHistoryAdapter;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/9/7 13:43
 * 自定义
 */
public class OTC_BargainCustom extends BasePager implements TimePickerView.OnTimeSelectListener, View.OnClickListener {

    private String TAG = "OTC_BargainCustom";
    private PullToRefreshListView mListView;
    private TextView tv_OTCBargainZDYStartDate, tv_OTCBargainZDYEndDate, tv_OTCBargainZDYQueryBtn = null;
    private ArrayList<HashMap<String, String>> list;
    private OTC_BargainHistoryAdapter adapter;
    private TimePickerView mPvTime;
    private boolean mJuedgeTv;
    private SimpleDateFormat mFormate;
    private ImageView OTCBargainKong;
    private Dialog mDialog;

    public OTC_BargainCustom(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        rootView.findViewById(R.id.ll_OTCBargainZiDingYi).setVisibility(View.VISIBLE);  //显示 自定义的部分
        OTCBargainKong = (ImageView) rootView.findViewById(R.id.OTCBargainKong);        //空 图片
        mFormate = new SimpleDateFormat("yyyy-MM-dd");                                   //实例化时间格式

        mPvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);     //实例化选择时间空间
        mPvTime.setTime(new Date());
        mPvTime.setCyclic(false);
        mPvTime.setCancelable(true);
        mPvTime.setTitle("选择日期");
        mPvTime.setOnTimeSelectListener(this);                                         //时间选择后回调

        tv_OTCBargainZDYStartDate = (TextView) rootView.findViewById(R.id.tv_OTCBargainZDYStartDate);     //开始时间
        tv_OTCBargainZDYEndDate = (TextView) rootView.findViewById(R.id.tv_OTCBargainZDYEndDate);         //结束时间
        tv_OTCBargainZDYQueryBtn = (TextView) rootView.findViewById(R.id.tv_OTCBargainZDYQueryBtn);       //查询按钮

        tv_OTCBargainZDYStartDate.setText(mFormate.format(new Date()));
        tv_OTCBargainZDYEndDate.setText(mFormate.format(new Date()));

        //添加监听
        tv_OTCBargainZDYStartDate.setOnClickListener(this);
        tv_OTCBargainZDYEndDate.setOnClickListener(this);
        tv_OTCBargainZDYQueryBtn.setOnClickListener(this);
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.rl_OTCBargainRefresh);
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<HashMap<String, String>>();
        adapter = new OTC_BargainHistoryAdapter(mContext);
        mListView.setEmptyView(OTCBargainKong);
        mListView.setAdapter(adapter);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                            list.clear();
                            getData();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //将下拉视图收起
                        mListView.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.otc_bargain_query;
    }


    @Override
    public void onTimeSelect(Date date) {
        if (mJuedgeTv) {
            tv_OTCBargainZDYStartDate.setText(mFormate.format(date));
        } else {
            tv_OTCBargainZDYEndDate.setText(mFormate.format(date));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_OTCBargainZDYStartDate:       //点击开始
                mJuedgeTv = true;
                mPvTime.show();
                break;
            case R.id.tv_OTCBargainZDYEndDate:         //点击结束
                mJuedgeTv = false;
                mPvTime.show();
                break;
            case R.id.tv_OTCBargainZDYQueryBtn:        //点击查询
                if (!TextUtils.isEmpty(tv_OTCBargainZDYStartDate.getText().toString()) && !TextUtils.isEmpty(tv_OTCBargainZDYEndDate.getText().toString())) {

                    String startDay = Helper.getMyDate(tv_OTCBargainZDYStartDate.getText().toString());
                    String endDay = Helper.getMyDate(tv_OTCBargainZDYEndDate.getText().toString());

                    String str = Helper.compareTo(startDay, endDay);

                    int days = Helper.daysBetween(startDay, endDay);

                    if (str.equalsIgnoreCase(startDay)) {
                        MistakeDialog.showDialog("请选择正确日期,起始日期不能超过截止日期", (Activity) mContext);
                    } else if (days > 90) {
                        MistakeDialog.showDialog("选择的日期间隔不能超过3个月", (Activity) mContext);
                    } else {
                        mDialog = LoadingDialog.initDialog((Activity) mContext, "正在提交...");
                        mDialog.show();
                        getData();
                    }
                }


                break;
        }
    }

    private void getData() {
        SimpleDateFormat formated = new SimpleDateFormat("yyyyMMdd");
        String startDate = tv_OTCBargainZDYStartDate.getText().toString();
        String newStartDate = startDate.replaceAll("-", "");
        String endDate = tv_OTCBargainZDYEndDate.getText().toString();
        String newEndDate = endDate.replaceAll("-", "");
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", newStartDate);
        map2.put("END_DATE", newEndDate);
        map1.put("funcid", "300505");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mDialog!=null){
                    mDialog.dismiss();
                }

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("-6".equals(code)){
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                        Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                        mContext.startActivity(intent);
                    }else if("0".equals(code)){
                        JSONArray jsonArray  = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0 ;i < jsonArray.length();i++){
                                JSONObject json = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("tv_Data", Helper.formateDate3(json.optString("BUSINESS_DATE")));
                                map.put("tv_Time", Helper.formateDate(json.optString("BUSINESS_TIME")));
                                map.put("tv_stockName", json.optString("PROD_NAME"));
                                map.put("tv_stockCode", json.optString("PROD_CODE"));
                                map.put("tv_businessName", json.optString("PROD_PROP_NAME"));
                                map.put("tv_businessMoney", json.optString("BUSINESS_BALANCE"));
                                map.put("tv_businessShare", json.optString("BUSINESS_AMOUNT"));
                                list.add(map);
                            }
                        }else{
                            OTCBargainKong.setVisibility(View.VISIBLE);
                        }
                    }else{
                        ResultDialog.getInstance().showText(res.optString("msg"));
                    }
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                adapter.setList(list);


               /**
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<OTC_BargainHistoryBean>() {
                }.getType();
                OTC_BargainHistoryBean historyBean = gson.fromJson(response, type);
                String code = historyBean.getCode();
                List<OTC_BargainHistoryBean.DataBean> data = historyBean.getData();
                if (code.equals("-6")) {
                    if (mDialog!=null){
                        mDialog.dismiss();
                    }
                    Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                    mContext.startActivity(intent);
                } else if (data.size() == 0) {
                    if (mDialog!=null){
                        mDialog.dismiss();
                    }
                    OTCBargainKong.setVisibility(View.VISIBLE);
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        OTC_BargainHistoryBean.DataBean dataBean = data.get(i);

                        String datas = dataBean.getBUSINESS_DATE();
                        String times = dataBean.getBUSINESS_TIME();
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                            // SimpleDateFormat的parse(String time)方法将String转换为Date
                            Date date = simpleDateFormat.parse(datas);
                            simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                            // SimpleDateFormat的format(Date date)方法将Date转换为String
                            String entrustDate = simpleDateFormat.format(date);
                            map.put("tv_Data", entrustDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("HHmmss");
                            // SimpleDateFormat的parse(String time)方法将String转换为Date
                            if (times.length() == 5) {
                                times = "0" + times;
                            }
                            Date date = simpleDateFormats.parse(times);
                            simpleDateFormats = new SimpleDateFormat("HH:mm:ss");
                            // SimpleDateFormat的format(Date date)方法将Date转换为String
                            String entrustTime = simpleDateFormats.format(date);
                            map.put("tv_Time", entrustTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        map.put("tv_stockName", dataBean.getPROD_NAME());
                        map.put("tv_stockCode", dataBean.getPROD_CODE());
                        map.put("tv_businessName", dataBean.getPROD_PROP_NAME());
                        map.put("tv_businessMoney", dataBean.getBUSINESS_BALANCE());
                        map.put("tv_businessShare", dataBean.getBUSINESS_AMOUNT());

                        list.add(map);
                    }
                }
                if (mDialog!=null){
                    mDialog.dismiss();
                }
                adapter.setList(list);

                */
            }
        });
    }
}
