package com.tpyzq.mobile.pangu.activity.myself.handhall.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.data.PartnerInfoEntity;
import com.tpyzq.mobile.pangu.http.doConnect.self.StartUpBoardActivityPresenter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * anthor:Created by tianchen on 2017/3/21.
 * email:963181974@qq.com
 */

public class KZQFragment extends BaseFragment implements View.OnClickListener {
    private StartUpBoardActivityPresenter presenter;
    private Button bt_next;
    private PartnerInfoEntity partnerInfo;
    private TextView tv_account;
    private TextView tv_open_time;

    public static KZQFragment newInstance(StartUpBoardActivityPresenter presenter) {
        KZQFragment kzqFragment = new KZQFragment();
        kzqFragment.setPresenter(presenter);
        return kzqFragment;
    }

    private void setPresenter(StartUpBoardActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView(View view) {
        bt_next = (Button) view.findViewById(R.id.bt_next);
        tv_account = (TextView) view.findViewById(R.id.tv_account);
        tv_open_time = (TextView) view.findViewById(R.id.tv_open_time);

        initData();
    }

    private void initData() {
        bt_next.setOnClickListener(this);
        partnerInfo = presenter.getActivity().getPartnerInfo();
        if (partnerInfo == null) {
            return;
        } else {
            tv_account.setText(partnerInfo.STOCK_ACCOUNT);
            tv_open_time.setText("开通时间:"+ StringToDate2(partnerInfo.RIGHT_OPEN_DATE, "yyyyMMdd", "yyyy年MM月dd日"));
        }
    }

    private String StringToDate2(String dateStr, String dateFormatStr, String formatStr) {
        String News = dateStr;
        try {
            Date date = null;
            DateFormat sdf = new SimpleDateFormat(dateFormatStr);
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            News = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return News;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_kzq;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                presenter.getActivity().startFragment(2);
                break;
        }
    }
}
