package com.tpyzq.mobile.pangu.activity.myself.handhall.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.github.barteksc.pdfviewer.PDFView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.http.doConnect.self.StartUpBoardActivityPresenter;

/**
 * anthor:Created by tianchen on 2017/3/21.
 * email:963181974@qq.com
 */

public class FXJSFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private StartUpBoardActivityPresenter presenter;
    private Button bt_true;
    private PDFView pdf_info;
    private CheckBox cb_risk;
    public static FXJSFragment newInstance(StartUpBoardActivityPresenter presenter) {
        FXJSFragment fxjsFragment = new FXJSFragment();
        fxjsFragment.setPresenter(presenter);
        return fxjsFragment;
    }

    private void setPresenter(StartUpBoardActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView(View view) {
        bt_true = (Button) view.findViewById(R.id.bt_true);
        pdf_info = (PDFView) view.findViewById(R.id.pdf_info);
        cb_risk = (CheckBox) view.findViewById(R.id.cb_risk);
        initData();
    }

    private void initData() {
        bt_true.setOnClickListener(this);
        pdf_info.fromAsset("GemRiskHint.pdf").load();
        cb_risk.setOnCheckedChangeListener(this);
        bt_true.setEnabled(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_fxjs;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_true:
                presenter.setGEMRegister();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            bt_true.setEnabled(true);
            bt_true.setBackgroundResource(R.drawable.button_login_pitchon);
        }else {
            bt_true.setEnabled(false);
            bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        }
    }
}
