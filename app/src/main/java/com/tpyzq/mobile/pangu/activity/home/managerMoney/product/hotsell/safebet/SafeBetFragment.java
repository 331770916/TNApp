package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell.safebet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.SafeBetAdapter;
import com.tpyzq.mobile.pangu.activity.trade.LazyBaseFragment;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/28.
 * 稳赢
 */

public class SafeBetFragment extends LazyBaseFragment implements DialogInterface.OnCancelListener{
    private final String TAG = SafeBetFragment.class.getSimpleName();
    private SafeBetAdapter mAdapter;
    private Dialog mProgressDialog;
    private boolean          clickBackKey;//判断用户是否点击返回键取消网络请求
    @Override
    public void initView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.safebateRl);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SafeBetAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        test();
    }

    private void test() {
        List<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("holdNo", "No.1");
        map1.put("holdProName", "B级基金");
        map1.put("holdPrdio", "6.29");
        map1.put("holdDay", "90天");
        map1.put("holdPrice", "100元");
        map1.put("lable", "中低风险,太平洋产品,收益高");

        Map<String, String> map2 = new HashMap<>();
        map2.put("holdNo", "No.2");
        map2.put("holdProName", "B级基金");
        map2.put("holdPrdio", "6.29");
        map2.put("holdDay", "90天");
        map2.put("holdPrice", "100元");
        map2.put("lable", "中低风险,太平洋产品,收益高");

        Map<String, String> map3 = new HashMap<>();
        map3.put("holdNo", "No.3");
        map3.put("holdProName", "B级基金");
        map3.put("holdPrdio", "6.29");
        map3.put("holdDay", "90天");
        map3.put("holdPrice", "100元");
        map3.put("lable", "中低风险,太平洋产品,收益高");

        Map<String, String> map4 = new HashMap<>();
        map4.put("holdProName", "B级基金");
        map4.put("holdPrdio", "6.29");
        map4.put("holdDay", "90天");
        map4.put("holdPrice", "100元");
        map4.put("lable", "中低风险,太平洋产品,收益高");

        Map<String, String> map5 = new HashMap<>();
        map5.put("holdProName", "B级基金");
        map5.put("holdPrdio", "6.29");
        map5.put("holdDay", "90天");
        map5.put("holdPrice", "100元");
        map5.put("lable", "中低风险,太平洋产品,收益高");


        datas.add(map1);
        datas.add(map2);
        datas.add(map3);
        datas.add(map4);
        datas.add(map5);

        mAdapter.setDatas(datas);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
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
        return R.layout.fragment_safebet;
    }
}
