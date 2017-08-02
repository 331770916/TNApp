package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell.safebet;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.LazyBaseFragment;

/**
 * Created by zhangwenbo on 2017/7/28.
 * 稳赢
 */

public class SafeBetFragment extends LazyBaseFragment {

    @Override
    public void initView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.safebateRl);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                clickBackKey = true;
//                mProgressDialog.cancel();
//                return false;
//            } else {
//                return true;
//            }
//        }else {
//            return true;
//        }

        return false;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_safebet;
    }
}
