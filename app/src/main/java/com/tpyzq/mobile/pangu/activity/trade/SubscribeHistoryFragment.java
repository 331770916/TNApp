package com.tpyzq.mobile.pangu.activity.trade;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.SubscribeHistorysAdapter;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.data.SubscribeHistoryBasicBean;
import com.tpyzq.mobile.pangu.data.SubscribeHistoryStateBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.tpyzq.mobile.pangu.view.pullDownGroup.PullDownElasticImp;
import com.tpyzq.mobile.pangu.view.pullDownGroup.PullDownScrollView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 刘泽鹏 on 2016/8/10.
 * 历史申购  Fragment
 */
public class SubscribeHistoryFragment extends BaseFragment implements PullDownScrollView.RefreshListener {
    private static final String TAG = "SubHistoryFragment";
    private PullDownScrollView mPullDownScrollView;     //下拉刷新
    private ListView listView = null;
    private String mSession;
    private SubscribeHistorysAdapter adapter;
    private ImageView ivSubHistoryKong;
    private ArrayList<HashMap<String, String>> datas;

    @Override
    public void initView(View view) {
        mSession = SpUtils.getString(getActivity(), "mSession", "");

        getBasicData();             //获取基本信息

        ivSubHistoryKong = (ImageView) view.findViewById(R.id.ivSubHistoryKong);        //空 图片
        mPullDownScrollView = (PullDownScrollView) view.findViewById(R.id.XLSX);
        mPullDownScrollView.setRefreshListener(this);
        mPullDownScrollView.setPullDownElastic(new PullDownElasticImp(getActivity()));

        this.listView = (ListView) view.findViewById(R.id.lvSubscribeHistory);
        adapter = new SubscribeHistorysAdapter(getContext());
        this.listView.setAdapter(adapter);

    }

    /**
     * 获取状态信息的数据
     */
    private void getState(StringBuffer sb) {
        if(sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }
        Map map5 = new HashMap();
        Map map6 = new HashMap();
        map6.put("secucodes", sb.toString());
        map5.put("funcid", "100221");
        map5.put("token", mSession);
        map5.put("parms", map6);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW, map5, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.toString();
            }

            @Override
            public void onResponse(String response, int id) {
                if (response == null && response.equals("")) {
                    return;
                }

                ArrayList<String> stateList = new ArrayList<String>();
                Gson gson = new Gson();
                Type type = new TypeToken<SubscribeHistoryStateBean>() {
                }.getType();
                SubscribeHistoryStateBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<SubscribeHistoryStateBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(getContext(), TransactionLoginActivity.class);
                    getContext().startActivity(intent);
                    getActivity().finish();
                } else
                if (code != null && code.equals("0")) {
                    for (int i = 0; i < data.size(); i++) {
                        SubscribeHistoryStateBean.DataBean dataBean = data.get(i);
                        String status = dataBean.getSTATUS();
                        String new_status = null;
                        switch (status) {
                            case "":
                                new_status = "";
                                break;
                            case "1":
                                new_status = "已上市";
                                break;
                            case "2":
                                new_status = "待上市";
                                break;
                            case "3":
                                new_status = "申购中";
                                break;
                            case "4":
                                new_status = "待发行";
                                break;
                        }
//                        map.put("status",status);
                        stateList.add(new_status);
                    }

//                    adapter.setList(datas);
                    adapter.setStateList(stateList);
                } else {
                    ResultDialog.getInstance().showText(bean.getMsg());
                }
            }
        });
    }


    /**
     * 获取申购股票的基本信息
     */
    private void getBasicData() {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("KEY_STR", "");
        map2.put("FLAG", "true");
        map1.put("funcid", "300383");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.toString();
                listView.setVisibility(View.GONE);                    //隐藏  listView
                ivSubHistoryKong.setVisibility(View.VISIBLE);       //显示 空
            }

            @Override
            public void onResponse(String response, int id) {
                if (response == null && response.equals("")) {
                    return;
                }
                datas = new ArrayList<HashMap<String, String>>();
                StringBuffer sb = new StringBuffer();
                Gson gson = new Gson();
                Type type = new TypeToken<SubscribeHistoryBasicBean>() {
                }.getType();
                SubscribeHistoryBasicBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<SubscribeHistoryBasicBean.DataBean> data = bean.getData();
//                if (code.equals("-6")) {
//                    Intent intent = new Intent(getContext(), TransactionLoginActivity.class);
//                    getContext().startActivity(intent);
//                    getActivity().finish();
//                } else
                if (code != null && "0".equals(code)) {

                    if (data.size() == 0) {       //如果数据为空的   “data”= [];
                        listView.setVisibility(View.GONE);                  //隐藏 ListView
                        ivSubHistoryKong.setVisibility(View.VISIBLE);      //显示 空的图片
                    }

                    for (int i = 0; i < data.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        SubscribeHistoryBasicBean.DataBean dataBean = data.get(i);
                        map.put("stock_name", dataBean.getSTOCK_NAME());           //证券名称
                        String stockCode = dataBean.getSTOCK_CODE();
                        map.put("stock_code", stockCode);                          //证券代码
                        String entrustDate = dataBean.getENTRUST_DATE();
                        map.put("entrust_date", entrustDate);                      //申购日期
                        map.put("entrust_price", dataBean.getENTRUST_PRICE());     //申购价格
                        map.put("entrust_amount", dataBean.getENTRUST_AMOUNT());   //申购数量
                        map.put("occur_amount", dataBean.getOCCUR_AMOUNT());       //中签数量

                        //转换股票 代码
                        String STOCK_CODE = stockCode;
                        String start = "";
                        if (STOCK_CODE.startsWith("730")) {
                            start = "600";
                        } else if (STOCK_CODE.startsWith("780")) {
                            start = "601";
                        } else if (STOCK_CODE.startsWith("732")) {
                            start = "603";
                        } else {
                            start = STOCK_CODE.substring(0, 3);
                        }
                        STOCK_CODE = start + STOCK_CODE.substring(3, 6);

                        sb.append(STOCK_CODE).append("&");

//                        getSubscribeFlowData(stockCode,entrustDate,map,datas);     //获取申购流程数据
                        datas.add(map);
                        adapter.setList(datas);
                    }
                    if (!TextUtils.isEmpty(sb)){
                        getState(sb);           //获取状态信息
                    }
                }
            }
        });
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_subscribe_history;
    }

    @Override
    public void onRefresh(PullDownScrollView view) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                datas=null;
                getBasicData();

                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String bDate = format1.format(new Date());
                mPullDownScrollView.finishRefresh("上次刷新时间:" + bDate);
            }
        }, 2000);
    }
}
