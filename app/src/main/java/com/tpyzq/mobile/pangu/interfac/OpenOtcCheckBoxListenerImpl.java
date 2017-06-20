package com.tpyzq.mobile.pangu.interfac;

import android.widget.CompoundButton;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/2.
 */

public class OpenOtcCheckBoxListenerImpl implements CompoundButton.OnCheckedChangeListener {

    private TextView mBtn;
    private CheckResultListener mCheckResultListener;
    private static final String TAG = "OpenOtcCheckBoxListenerImpl";

    public OpenOtcCheckBoxListenerImpl(TextView submitBtn, CheckResultListener checkResultListener) {
        mBtn = submitBtn;
        mCheckResultListener = checkResultListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Map<String, Boolean> map = new HashMap<>();
            map.put(TAG, isChecked);
            mCheckResultListener.checkResult(map);
        } else {
            mBtn.setBackgroundResource(R.drawable.lonin4);                //背景灰色
            mBtn.setEnabled(false);
            mCheckResultListener.checkCancelResult(TAG);
        }
    }
}
