package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell.recommend;

import android.app.Dialog;
import android.content.DialogInterface;
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

import static com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.RecommendAdapter.TYPE_NEW;

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
        mAdapter = new RecommendAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        test();
    }

    private void test() {
        List<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("type", TYPE_NEW);
        map.put("hold1ProName", "货币基金A");
        map.put("hold1Prdio", "30%");
        map.put("hold1Day", "15天");
        map.put("hold1Qgje", "100元");
//        map.put("", "");
//        map.put("", "");
        datas.add(map);
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
