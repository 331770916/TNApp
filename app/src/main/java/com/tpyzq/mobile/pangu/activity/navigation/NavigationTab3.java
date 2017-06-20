package com.tpyzq.mobile.pangu.activity.navigation;

import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.IndexActivity;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.util.SpUtils;

/**
 * Created by zhangwenbo on 2016/12/20.
 * 引导页3
 */

public class NavigationTab3 extends BaseFragment implements View.OnClickListener{

    @Override
    public void initView(View view) {
        view.findViewById(R.id.intoLuncherBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SpUtils.putBoolean(getActivity(), NavigationActivity.FIRST_INTO_APP, true);
        Intent intent = new Intent();
        intent.setClass(getActivity(), IndexActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.navigatioin_tab3;
    }
}
