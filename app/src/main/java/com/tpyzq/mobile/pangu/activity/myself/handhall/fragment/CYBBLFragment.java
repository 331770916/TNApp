package com.tpyzq.mobile.pangu.activity.myself.handhall.fragment;

import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.http.doConnect.self.StartUpBoardActivityPresenter;

/**
 * anthor:Created by tianchen on 2017/3/21.
 * email:963181974@qq.com
 * 创业板办理
 */

public class CYBBLFragment extends BaseFragment {
    private StartUpBoardActivityPresenter presenter;
//    private Button bt_wybl;

    public static CYBBLFragment newInstance(StartUpBoardActivityPresenter presenter) {
        CYBBLFragment cybblFragment = new CYBBLFragment();
        cybblFragment.setPresenter(presenter);
        return cybblFragment;
    }

    private void setPresenter(StartUpBoardActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView(View view) {
//        bt_wybl = (Button) view.findViewById(bt_wybl);
        initData();
    }

    private void initData() {
        presenter.transact();
//        bt_wybl.setOnClickListener(this);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_cybl;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_wybl:
//                presenter.transact();
//                break;
//        }
//    }
}
