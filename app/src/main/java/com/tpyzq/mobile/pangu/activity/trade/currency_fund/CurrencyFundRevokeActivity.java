package com.tpyzq.mobile.pangu.activity.trade.currency_fund;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.view.FundRevokePopupWindow;
import com.tpyzq.mobile.pangu.adapter.trade.CurrencyFundRevokeAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.CurrencyFundEntrustQueryTodayBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.pullrefreshrecyclerview.PullRefreshView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 刘泽鹏
 * 货币基金撤单  界面
 */
public class CurrencyFundRevokeActivity extends BaseActivity implements View.OnClickListener,FundRevokePopupWindow.IClick, AdapterView.OnItemClickListener {

    private String TAG = "CurrencyFundRevoke";
    private PullToRefreshListView mListView;
    private ArrayList<HashMap<String,String>> list;
    private CurrencyFundRevokeAdapter adapter;


    @Override
    public void initView() {
        //返回按钮
        mListView = (PullToRefreshListView) findViewById(R.id.lv_currency);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mListView.setEmptyView(findViewById(R.id.isEmpty));
        adapter = new CurrencyFundRevokeAdapter(this);
        mListView.setAdapter(adapter);      //适配
        list = new ArrayList<HashMap<String,String>>();
        initEvent();
        getData();
    }

    private void initEvent() {
        findViewById(R.id.ivCurrencyFundRevoke_back).setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  下拉
                list.clear();
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //不操作。
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map2.put("ACTION_IN","1");
        map1.put("funcid","300444");
        map1.put("token",SpUtils.getString(this, "mSession", ""));
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(CurrencyFundRevokeActivity.this,ConstantUtil.NETWORK_ERROR);
                mListView.onRefreshComplete();
            }

            @Override
            public void onResponse(String response, int id) {
                mListView.onRefreshComplete();
                if(TextUtils.isEmpty(response)){
                    return;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CurrencyFundEntrustQueryTodayBean>() {}.getType();
                CurrencyFundEntrustQueryTodayBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<CurrencyFundEntrustQueryTodayBean.DataBean> data = bean.getData();
                if(code.equals("-6")){
                    Intent intent = new Intent(CurrencyFundRevokeActivity.this, TransactionLoginActivity.class);
                    CurrencyFundRevokeActivity.this.startActivity(intent);
                    finish();
                }else
                if(code.equals("0") && data != null){
                    for(int i=0;i<data.size();i++){
                        HashMap<String,String> map = new HashMap<String, String>();
                        CurrencyFundEntrustQueryTodayBean.DataBean dataBean = data.get(i);
                        map.put("tv_stockName",dataBean.getSECU_NAME());        //基金名称
                        map.put("tv_stockCode",dataBean.getSECU_CODE());        //基金代码
                        map.put("tv_Data",dataBean.getORDER_DATE());            //日期
                        map.put("tv_Time",dataBean.getORDER_TIME());            //时间
                        map.put("tv_EntrustNumber",dataBean.getQTY());          //数量
                        map.put("tv_EntrustMoney",dataBean.getORDER_AMT());         //金额
                        map.put("tv_type",dataBean.getBUSINESS_NAME());           //类型
                        map.put("tv_market",dataBean.getMARKET());           //市场
                        map.put("entrust_no",dataBean.getENTRUST_NO());      //委托编号

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
                        adapter.setList(list);      //向适配器中添加数据
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_fund_revoke;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivCurrencyFundRevoke_back:
                this.finish();
                break;
        }
    }

    @Override
    public void OnClickListener(int position) {
        list.remove(position);
        adapter.setList(list);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.setList(list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HashMap<String, String> map = list.get(position-1);
        //实例化PopupWindow
        FundRevokePopupWindow popupWindow= new FundRevokePopupWindow(CurrencyFundRevokeActivity.this
                ,CurrencyFundRevokeActivity.this,map,position,CurrencyFundRevokeActivity.this);
        popupWindow.setFocusable(true);     //获取焦点
        ColorDrawable dw = new ColorDrawable(0xf0000000);     //0x60000000
        popupWindow.setBackgroundDrawable(dw);      //设置背景颜色
        popupWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //消失的时候设置窗体背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        //显示窗口
        popupWindow.showAtLocation(CurrencyFundRevokeActivity.this.findViewById(R.id.FundRevokeActivity),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置


    }
}
