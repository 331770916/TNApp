package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.NewStockSubAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 刘泽鹏
 * 新股申购 导航界面
 */
public class NewStockSubscribeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mySubscribe, tvQueryLimit, rlSubscribeLimit, rlNewStockRiLi,RelativeLayout_2;
    private TextView yiJianSubscribe, tvNewStockIssue = null;
    private static final String TAG = "NewStockSubscribeActivity";
    private String session;
    private NewStockEnitiy entitiy, mCalendarEntitiy;   //新股日历
    private ListView mListView;
    private NewStockSubAdapter adapter;
    private ArrayList<NewStockEnitiy.DataBeanToday> mAdapterDatas;
    private List<CalendarDay> calendarDays;
    private ArrayList<NewStockEnitiy> list;
    private LinearLayout mListViewTitle;

    private TextView tvHuANum,tvShenANum;
    private LinearLayout ll_query;

    @Override
    public void initView() {
        //我的申购
        this.tvHuANum = (TextView) this.findViewById(R.id.tvHuANum);         //沪A的可购买数量
        this.tvShenANum = (TextView) this.findViewById(R.id.tvShenANum);     //深A的值可购买数量
        this.ll_query = (LinearLayout) this.findViewById(R.id.ll_query);     //深A的值可购买数量
        this.ll_query.setOnClickListener(this);
        //新股日历
        mListView = (ListView) this.findViewById(R.id.lvNewStockSubscribe); //listView
        this.findViewById(R.id.ivNewStock_back).setOnClickListener(this);   //点击返回按钮销毁当前界面
        this.yiJianSubscribe = (TextView) this.findViewById(R.id.tvYiJianSubscribe);//一键申购
        this.mySubscribe = (RelativeLayout) this.findViewById(R.id.rlMySubscribe);//我的申购
        this.tvQueryLimit = (RelativeLayout) this.findViewById(R.id.rlQueryLimit);//查询可用额度
        this.rlSubscribeLimit = (RelativeLayout) this.findViewById(R.id.rlSubscribeLimit);//新股申购规则
        this.rlNewStockRiLi = (RelativeLayout) this.findViewById(R.id.rlNewStockRiLi);//今日发行新股
        this.RelativeLayout_2 = (RelativeLayout) this.findViewById(R.id.RelativeLayout_2);//隐藏
        this.tvNewStockIssue = (TextView) this.findViewById(R.id.tvNewStockIssue);//今日发行新股的个数
        this.mListViewTitle = (LinearLayout) this.findViewById(R.id.ListViewTitle);//ListView标题
        this.yiJianSubscribe.setOnClickListener(this);
        this.mySubscribe.setOnClickListener(this);
        this.tvQueryLimit.setOnClickListener(this);
        this.rlSubscribeLimit.setOnClickListener(this);
        this.rlNewStockRiLi.setOnClickListener(this);
        calendarDays = new ArrayList<>();
        initData();

    }

    private void initData() {
        adapter = new NewStockSubAdapter(this);
        initLogIc();
        newStockCalenderConnect();
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < list.size(); i++) {

                    try {
                        NewStockEnitiy newStockEnitiy = list.get(i);
                        String time = newStockEnitiy.getmTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(time);
                        CalendarDay calendarDay = CalendarDay.from(date);
                        calendarDays.add(calendarDay);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                mAdapterDatas = disposeDatas(calendarDays.get(0), mCalendarEntitiy.getData());
                Intent intent = new Intent();
                intent.putExtra("name", mAdapterDatas.get(position).getISSUENAMEABBR_ONLINE());
                intent.putExtra("number", mAdapterDatas.get(position).getSECUCODE());
                intent.setClass(NewStockSubscribeActivity.this, PublishNewStockDetail.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_new_stock_subscribe;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_query://查看额度
                Intent intent = new Intent();
                intent.setClass(this, QueryLimitActivity.class);
                intent.putExtra("session", SpUtils.getString(this, "mSession", ""));
                startActivity(intent);
                break;
            case R.id.tvYiJianSubscribe:    //一键申购界面
                Intent intentYiJianSubscribe = new Intent();
                intentYiJianSubscribe.setClass(this, OneKeySubscribeActivity.class);
                startActivity(intentYiJianSubscribe);
                break;
            case R.id.rlMySubscribe:    //我的申购界面
                Intent intentMySubscribe = new Intent();
                intentMySubscribe.setClass(this, MySubscribeActivity.class);
                startActivity(intentMySubscribe);
                break;
            case R.id.rlQueryLimit:    //查询可用额度界面
                Intent intentQueryLimit = new Intent();
                intentQueryLimit.setClass(this, QueryLimitActivity.class);
                startActivity(intentQueryLimit);
                break;
            case R.id.rlSubscribeLimit:    //新股申购规则界面
                Intent intentSubscribeLimit = new Intent();
                intentSubscribeLimit.setClass(this, SubscribeRuleActivity.class);
                startActivity(intentSubscribeLimit);
                break;
            case R.id.rlNewStockRiLi:    //新股日历界面
                Intent intentNewStockRiLi = new Intent();
                intentNewStockRiLi.putExtra("entiity", mCalendarEntitiy);
                intentNewStockRiLi.setClass(this, CalendarNewStockActivity.class);
                startActivity(intentNewStockRiLi);
                break;
            case R.id.ivNewStock_back:    //销毁当前界面
                this.finish();
                break;
        }
    }


    //获取深A和沪股持股数量
    private void initLogIc() {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("funcid", "300380");
        map1.put("token", SpUtils.getString(this, "mSession", ""));
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(this, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<OneKeySubscribeBean>() {
                }.getType();
                OneKeySubscribeBean bean = gson.fromJson(response, type);
                List<OneKeySubscribeBean.DataBean> data = bean.getData();
                String code = bean.getCode();
                if (data != null && code.equals("0") && data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        OneKeySubscribeBean.DataBean dataBean = data.get(i);
                        String market = dataBean.getMARKET();
                        if (market.equals("1")) {
                            tvHuANum.setText(dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf(".")) + "股");
                        } else if (market.equals("2")) {
                            tvShenANum.setText(dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf(".")) + "股");
                        }
                    }

                }
            }
        });
    }


    /**
     * 新股日历网络请求
     */
    private void newStockCalenderConnect() {
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "100213");
        map.put("token", "");
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {


                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<NewStockEnitiy>() {
                    }.getType();

                    NewStockEnitiy bean = gson.fromJson(response, type);

                    if (bean.getCode().equals("-6")) {
                        Intent intent = new Intent(NewStockSubscribeActivity.this, TransactionLoginActivity.class);
                        NewStockSubscribeActivity.this.startActivity(intent);
                        finish();
                    } else if (bean.getCode().equals("0") && bean.getData() != null && bean.getData().size() > 0) {

                        list = new ArrayList<NewStockEnitiy>();
                        int publishNum = 0;
                        for (NewStockEnitiy.DataBeanToday dataBeanToday : bean.getData()) {

                            entitiy = new NewStockEnitiy();
                            if ("N".equals(dataBeanToday.getISTODAY())) {
                                publishNum++;

                                String stockName = dataBeanToday.getISSUENAMEABBR_ONLINE();
                                String stockCode = dataBeanToday.getAPPLYCODEONLINE();
                                String price = dataBeanToday.getISSUEPRICE();
                                String perent = dataBeanToday.getDILUTEDPERATIO();
                                String value = dataBeanToday.getAPPLYMAXONLINEMONEY();
                                String istoday = dataBeanToday.getISTODAY();
                                String onlinestartdate = dataBeanToday.getONLINESTARTDATE();

                                entitiy.setName(stockName);
                                entitiy.setNumber(stockCode);
                                entitiy.setIsSuepRice(price);
                                entitiy.setWeIghtedPeraioO(perent);
                                entitiy.setAppLymaxonlIneMoney(value);
                                entitiy.setIsToday(istoday);
                                entitiy.setmTime(onlinestartdate);
                                list.add(entitiy);
                                mListViewTitle.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.VISIBLE);
                                RelativeLayout_2.setVisibility(View.VISIBLE);
                            }

                        }

                        adapter.setList(list);
                        bean.setNewStockSize(publishNum);
                    } else {
                        CentreToast.showText(NewStockSubscribeActivity.this,ConstantUtil.NETWORK_ERROR);
                    }

                    mCalendarEntitiy = bean;

                    if (null != mCalendarEntitiy) {
                        int newStockSize = mCalendarEntitiy.getNewStockSize();
                        tvNewStockIssue.setText("今日" + newStockSize + "只新股发行");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    /**
     * 数据处理
     *
     * @param _calendarDay 天数
     * @param datas        数据源
     */
    private ArrayList<NewStockEnitiy.DataBeanToday> disposeDatas(CalendarDay _calendarDay, List<NewStockEnitiy.DataBeanToday> datas) {

        ArrayList<NewStockEnitiy.DataBeanToday> beans = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (NewStockEnitiy.DataBeanToday bean : datas) {

            try {
                String onLineDate = bean.getONLINESTARTDATE();
                Date date = sdf.parse(onLineDate);
                CalendarDay calendarDay = CalendarDay.from(date);
                if (calendarDay.equals(_calendarDay)) {
                    beans.add(bean);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return beans;
    }
}
