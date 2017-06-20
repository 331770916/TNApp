package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_EntrustTodayAdapter;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.OTC_Helper;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/9/6 21:23
 */
public class OTC_EntrustThreeMonth extends BasePager  {

    private String TAG = "OTC_EntrustThreeMonth";
    private PullToRefreshListView mListView;
    private ArrayList<HashMap<String,String>> list;
    private OTC_EntrustTodayAdapter adapter;
    private ImageView OTCEntrustKong;


    public OTC_EntrustThreeMonth(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        OTCEntrustKong = (ImageView) rootView.findViewById(R.id.OTCEntrustKong);        //空 图片
        mListView= (PullToRefreshListView) rootView.findViewById(R.id.rl_OTCEntrustRefresh);
    }

    @Override
    public int getLayoutId() {
        return R.layout.otc_entrust_todaypager;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<HashMap<String,String>>();
        adapter = new OTC_EntrustTodayAdapter(mContext);
        getData();
        mListView.setEmptyView(OTCEntrustKong);
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

    private void getData() {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map2.put("HIS_TYPE","3");
        map1.put("funcid","300508");
        map1.put("token",mSession);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("-6".equals(code)){
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
                }catch (JSONException e){
                    e.printStackTrace();
                }
                adapter.setList(list);
                /**
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<OTC_EmtristHistoryBean>() {}.getType();
                OTC_EmtristHistoryBean historyBean = gson.fromJson(response, type);
                String code = historyBean.getCode();
                List<OTC_EmtristHistoryBean.DataBean> data = historyBean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                    mContext.startActivity(intent);
                }else
                if(data.size() == 0){
                    OTCEntrustKong.setVisibility(View.VISIBLE);     //显示 空图片
                }else
                if(code.equals("0") && data !=null){
                    for(int i=0;i<data.size();i++){
                        HashMap<String,String> map = new HashMap<String, String>();
                        OTC_EmtristHistoryBean.DataBean dataBean = data.get(i);

                        String datas = dataBean.getENTRUST_DATE();
                        String times = dataBean.getENTRUST_TIME();
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                            // SimpleDateFormat的parse(String time)方法将String转换为Date
                            java.util.Date date = simpleDateFormat.parse(datas);
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            // SimpleDateFormat的format(Date date)方法将Date转换为String
                            String entrustDate = simpleDateFormat.format(date);
                            map.put("tv_Data",entrustDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("HHmmss");
                            // SimpleDateFormat的parse(String time)方法将String转换为Date
                            if(times.length()==5){
                                times="0"+times;
                            }
                            java.util.Date  date = simpleDateFormats.parse(times);
                            simpleDateFormats = new SimpleDateFormat("HH:mm:ss");
                            // SimpleDateFormat的format(Date date)方法将Date转换为String
                            String entrustTime = simpleDateFormats.format(date);
                            map.put("tv_Time",entrustTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        map.put("tv_stockName",dataBean.getPROD_NAME());
                        map.put("tv_stockCode",dataBean.getPROD_CODE());
                        map.put("tv_EntrustMoney",dataBean.getENTRUST_BALANCE());
                        map.put("tv_EntrustNumber",dataBean.getENTRUST_AMOUNT());
                        map.put("tv_type",dataBean.getBUSINESS_FLAG());

                        String entrust_status;
                        if(dataBean.getENTRUST_STATUS().equals("0")){
                            entrust_status="未报";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("1")){
                            entrust_status="待报";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("2")){
                            entrust_status="已报";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("3")){
                            entrust_status="已报待撤";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("4")){
                            entrust_status="部成待撤";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("6")){
                            entrust_status="已撤";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("7")){
                            entrust_status="部成";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("8")){
                            entrust_status="已成";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("9")){
                            entrust_status="废单";
                            map.put("tv_state",entrust_status);
                        }

                        list.add(map);
                    }
                }
                adapter.setList(list);
                */

            }
        });
    }
}
