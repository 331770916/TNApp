package com.tpyzq.mobile.pangu.activity.myself.handhall.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.data.PartnerInfoEntity;
import com.tpyzq.mobile.pangu.http.doConnect.self.StartUpBoardActivityPresenter;

/**
 * anthor:Created by tianchen on 2017/3/22.
 * email:963181974@qq.com
 */

public class KTCGFragment extends BaseFragment {
    private StartUpBoardActivityPresenter presenter;
    private PartnerInfoEntity partnerInfo;
    private TextView tv_sh_account;
    private TextView tv_status;
    private TextView tv_open_time;

    public static KTCGFragment newInstance(StartUpBoardActivityPresenter presenter) {
        KTCGFragment ktcgFragment = new KTCGFragment();
        ktcgFragment.setPresenter(presenter);
        return ktcgFragment;
    }

    private void setPresenter(StartUpBoardActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView(View view) {
        tv_sh_account = (TextView) view.findViewById(R.id.tv_sh_account);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        tv_open_time = (TextView) view.findViewById(R.id.tv_open_time);
        initData();
    }

    private void initData() {
        partnerInfo = presenter.getActivity().getPartnerInfo();
        if (partnerInfo == null) {
            return;
        }
        tv_sh_account.setText(partnerInfo.STOCK_ACCOUNT);
        String time = partnerInfo.RIGHT_OPEN_DATE;
        if (TextUtils.isEmpty(time)) {
            tv_open_time.setVisibility(View.GONE);
        } else {
            tv_open_time.setVisibility(View.VISIBLE);
            tv_open_time.setText("开通时间:" + partnerInfo.RIGHT_OPEN_DATE);
        }
        String status = partnerInfo.STATUS;
        String state = "";
        switch (status) {
            case "0":
                state = "已开通";
                break;
            case "1":
                state = "已转签";
                break;
            case "2":
                state = "检查通过";
                break;
        }
        tv_status.setText(state);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_ktcg;
    }
}
