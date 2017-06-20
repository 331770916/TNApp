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
import com.tpyzq.mobile.pangu.adapter.trade.OTC_EntrustTodayAdapter;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.OTC_Helper;
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
 * 作者：刘泽鹏 on 2016/9/6 21:25
 * OTC 委托查询  自定义
 */
public class OTC_EntrustCustom extends BasePager implements TimePickerView.OnTimeSelectListener, View.OnClickListener {


    private String TAG = "OTC_EntrustCustom";
    private PullToRefreshListView mListView;
    private TextView tv_OTCEntrustZDYStartDate, tv_OTCEntrustZDYEndDate, tv_OTCEntrustZDYQueryBtn = null;
    private ArrayList<HashMap<String, String>> list;
    private OTC_EntrustTodayAdapter adapter;
    private TimePickerView mPvTime;
    private boolean mJuedgeTv;
    private SimpleDateFormat mFormate;
    private ImageView OTCEntrustKong;
    private Dialog mDialog;

    public OTC_EntrustCustom(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        rootView.findViewById(R.id.ll_OTCEntrustZiDingYi).setVisibility(View.VISIBLE);  //显示 自定义 选择日期的 控件
        OTCEntrustKong = (ImageView) rootView.findViewById(R.id.OTCEntrustKong);        //空 图片
        mFormate = new SimpleDateFormat("yyyy-MM-dd");                                   //实例化时间格式

        mPvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);     //实例化选择时间空间
        mPvTime.setTime(new Date());
        mPvTime.setCyclic(false);
        mPvTime.setCancelable(true);
        mPvTime.setTitle("选择日期");
        mPvTime.setOnTimeSelectListener(this);                                         //时间选择后回调

        tv_OTCEntrustZDYStartDate = (TextView) rootView.findViewById(R.id.tv_OTCEntrustZDYStartDate);     //开始时间
        tv_OTCEntrustZDYEndDate = (TextView) rootView.findViewById(R.id.tv_OTCEntrustZDYEndDate);         //结束时间
        tv_OTCEntrustZDYQueryBtn = (TextView) rootView.findViewById(R.id.tv_OTCEntrustZDYQueryBtn);       //查询按钮

        tv_OTCEntrustZDYStartDate.setText(mFormate.format(new Date()));
        tv_OTCEntrustZDYEndDate.setText(mFormate.format(new Date()));

        //添加监听
        tv_OTCEntrustZDYStartDate.setOnClickListener(this);
        tv_OTCEntrustZDYEndDate.setOnClickListener(this);
        tv_OTCEntrustZDYQueryBtn.setOnClickListener(this);
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.rl_OTCEntrustRefresh);

        //下拉刷新
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
    public void initData() {
        super.initData();
        list = new ArrayList<HashMap<String, String>>();
        adapter = new OTC_EntrustTodayAdapter(mContext);
        mListView.setEmptyView(OTCEntrustKong);
        mListView.setAdapter(adapter);

    }


    @Override
    public int getLayoutId() {
        return R.layout.otc_entrust_todaypager;
    }

    @Override
    public void onTimeSelect(Date date) {
        if (mJuedgeTv) {
            tv_OTCEntrustZDYStartDate.setText(mFormate.format(date));
        } else {
            tv_OTCEntrustZDYEndDate.setText(mFormate.format(date));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_OTCEntrustZDYStartDate:       //点击开始
                mJuedgeTv = true;
                mPvTime.show();
                break;
            case R.id.tv_OTCEntrustZDYEndDate:         //点击结束
                mJuedgeTv = false;
                mPvTime.show();
                break;
            case R.id.tv_OTCEntrustZDYQueryBtn:        //点击查询
                if (!TextUtils.isEmpty(tv_OTCEntrustZDYStartDate.getText().toString()) && !TextUtils.isEmpty(tv_OTCEntrustZDYEndDate.getText().toString())) {

                    String startDay = Helper.getMyDate(tv_OTCEntrustZDYStartDate.getText().toString());
                    String endDay = Helper.getMyDate(tv_OTCEntrustZDYEndDate.getText().toString());

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
        String startDate = tv_OTCEntrustZDYStartDate.getText().toString();
        String newStartDate = startDate.replaceAll("-", "");
        String endDate = tv_OTCEntrustZDYEndDate.getText().toString();
        String newEndDate = endDate.replaceAll("-", "");
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", newStartDate);
        map2.put("END_DATE", newEndDate);
        map1.put("funcid", "300508");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mDialog != null) {
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
                                map.put("tv_Data", Helper.formateDate1(json.optString("ENTRUST_DATE")));
                                map.put("tv_Time", Helper.formateDate(json.optString("ENTRUST_TIME")));
                                map.put("tv_stockName", json.optString("PROD_NAME"));
                                map.put("tv_stockCode", json.optString("PROD_CODE"));
                                map.put("tv_EntrustMoney", json.optString("ENTRUST_BALANCE"));
                                map.put("tv_EntrustNumber", json.optString("ENTRUST_AMOUNT"));
                                map.put("tv_type", json.optString("BUSINESS_FLAG"));
                                map.put("tv_state", OTC_Helper.getEntrustStaus(json.optString("ENTRUST_STATUS")));
                                list.add(map);
                            }
                        }else{
                            OTCEntrustKong.setVisibility(View.VISIBLE);
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
                java.lang.reflect.Type type = new TypeToken<OTC_EmtristHistoryBean>() {
                }.getType();
                OTC_EmtristHistoryBean historyBean = gson.fromJson(response, type);
                String code = historyBean.getCode();
                List<OTC_EmtristHistoryBean.DataBean> data = historyBean.getData();
                if (code.equals("-6")) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                    mContext.startActivity(intent);
                } else if (data.size() == 0) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    OTCEntrustKong.setVisibility(View.VISIBLE);
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        OTC_EmtristHistoryBean.DataBean dataBean = data.get(i);

                        String datas = dataBean.getENTRUST_DATE();
                        String times = dataBean.getENTRUST_TIME();
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                            // SimpleDateFormat的parse(String time)方法将String转换为Date
                            Date date = simpleDateFormat.parse(datas);
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
                        map.put("tv_EntrustMoney", dataBean.getENTRUST_BALANCE());
                        map.put("tv_EntrustNumber", dataBean.getENTRUST_AMOUNT());
                        map.put("tv_type", dataBean.getBUSINESS_FLAG());

                        String entrust_status;
                        if (dataBean.getENTRUST_STATUS().equals("0")) {
                            entrust_status = "未报";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("1")) {
                            entrust_status = "待报";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("2")) {
                            entrust_status = "已报";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("3")) {
                            entrust_status = "已报待撤";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("4")) {
                            entrust_status = "部成待撤";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("6")) {
                            entrust_status = "已撤";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("7")) {
                            entrust_status = "部成";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("8")) {
                            entrust_status = "已成";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("9")) {
                            entrust_status = "废单";
                            map.put("tv_state", entrust_status);
                        }

                        list.add(map);
                    }
                } else {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    ResultDialog.getInstance().showText("网络异常");
                }
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                adapter.setList(list);
                 */
            }
        });
    }
}
