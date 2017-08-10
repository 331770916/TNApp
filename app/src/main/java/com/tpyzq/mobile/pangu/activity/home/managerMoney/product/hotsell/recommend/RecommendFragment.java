package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell.recommend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.RecommendAdapter;
import com.tpyzq.mobile.pangu.activity.trade.LazyBaseFragment;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.RecommendAdapter.TYPE_HOT;
import static com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.RecommendAdapter.TYPE_NEW;
import static com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.RecommendAdapter.TYPE_SELL;
import static com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.RecommendAdapter.TYPE_TITLE_HOT;
import static com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.RecommendAdapter.TYPE_TITLE_SELL;

/**
 * Created by zhangwenbo on 2017/7/28.
 * 推荐
 */

public class RecommendFragment extends LazyBaseFragment implements DialogInterface.OnCancelListener {

    private final String TAG = RecommendFragment.class.getSimpleName();
    private RecommendAdapter mAdapter;
    private Dialog           mProgressDialog;
    private boolean          clickBackKey;//判断用户是否点击返回键取消网络请求

    @Override
    public void initView(View view) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recommendRl);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecommendAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        test();
    }

    private void test() {
        List<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("type", TYPE_NEW);
        map.put("hold1ProName", "货币基金A");
        map.put("hold1Prdio", "7.71");
        map.put("hold1Day", "15天");
        map.put("hold1Qgje", "100元");
        map.put("lable", "60天后可转让,到期前30天不可转让,综合年后5.8%");

        Map<String, String> map8 = new HashMap<>();
        map8.put("type", TYPE_TITLE_HOT);
        Map<String, String> map9 = new HashMap<>();
        map9.put("type", TYPE_TITLE_SELL);


        Map<String, String> map2 = new HashMap<>();
        map2.put("type", TYPE_HOT);
        map2.put("hold2ProName", "B级基金");
        map2.put("hold2Time", "12小时56分");
        map2.put("hold2Prdio", "6.29");
        map2.put("hold2Day", "90天");
        map2.put("hold2Price", "100元");
        map2.put("lable", "中低风险,太平洋产品,收益高");

        Map<String, String> map3 = new HashMap<>();
        map3.put("type", TYPE_HOT);
        map3.put("hold2ProName", "B级基金");
        map3.put("hold2Time", "12小时56分");
        map3.put("hold2Prdio", "6.29");
        map3.put("hold2Day", "90天");
        map3.put("hold2Price", "100元");
        map3.put("lable", "中低风险,太平洋产品,收益高");

        Map<String, String> map4 = new HashMap<>();
        map4.put("type", TYPE_SELL);
        map4.put("hold2ProName", "B级基金");
        map4.put("hold2Prdio", "6.29");
        map4.put("hold2Day", "90天");
        map4.put("hold2Price", "100元");
        map4.put("lable", "中低风险,太平洋产品,收益高");


        Map<String, String> map5 = new HashMap<>();
        map5.put("type", TYPE_SELL);
        map5.put("hold2ProName", "B级基金");
        map5.put("hold2Prdio", "6.29");
        map5.put("hold2Day", "90天");
        map5.put("hold2Price", "100元");
        map5.put("lable", "中低风险,太平洋产品,收益高");


        Map<String, String> map6 = new HashMap<>();
        map6.put("type", TYPE_SELL);
        map6.put("hold2ProName", "B级基金");
        map6.put("hold2Prdio", "6.29");
        map6.put("hold2Day", "90天");
        map6.put("hold2Price", "100元");
        map6.put("lable", "中低风险,太平洋产品,收益高");


        Map<String, String> map7 = new HashMap<>();
        map7.put("type", TYPE_SELL);
        map7.put("hold2ProName", "B级基金");
        map7.put("hold2Prdio", "6.29");
        map7.put("hold2Day", "90天");
        map7.put("hold2Price", "100元");
        map7.put("lable", "中低风险,太平洋产品,收益高");



        datas.add(map);

        datas.add(map8);
        datas.add(map2);
        datas.add(map3);

        datas.add(map9);
        datas.add(map4);
        datas.add(map5);
        datas.add(map6);
        datas.add(map7);


        mAdapter.setDatas(datas);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                clickBackKey = true;
                mProgressDialog.cancel();
                return false;
            } else {
                return true;
            }
        }else {
            return true;
        }
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(getActivity(), "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void cloasLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_recommend;
    }
}
