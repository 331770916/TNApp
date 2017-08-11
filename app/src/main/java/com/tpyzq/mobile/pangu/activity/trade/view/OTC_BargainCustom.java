package com.tpyzq.mobile.pangu.activity.trade.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_BargainQueryActivity.otc_FragmentManager;

/**
 * 作者：刘泽鹏 on 2016/9/7 13:43
 * 自定义
 */
public class OTC_BargainCustom extends OTC_BargainToday implements TimePickerView.OnTimeSelectListener, View.OnClickListener {

    private final String TAG = OTC_BargainCustom.class.getSimpleName();
    private TimePickerView   mPvTime;
    private boolean          mJuedgeTv;
    private SimpleDateFormat mFormate;
    private TextView         mStartDateTv;
    private TextView         mEndDateTv;


    @Override
    protected void showBargainCustom(View view) {
        mFormate = new SimpleDateFormat("yyyy-MM-dd");
        view.findViewById(R.id.ll_OTCBargainZiDingYi).setVisibility(View.VISIBLE);

        mStartDateTv = (TextView) view.findViewById(R.id.tv_OTCBargainZDYStartDate);     //开始时间
        mEndDateTv = (TextView) view.findViewById(R.id.tv_OTCBargainZDYEndDate);         //结束时间
        mStartDateTv.setOnClickListener(this);
        mEndDateTv.setOnClickListener(this);
        view.findViewById(R.id.tv_OTCBargainZDYQueryBtn).setOnClickListener(this);       //查询按钮
        mStartDateTv.setText(Helper.getBeforeString());
        mEndDateTv.setText(Helper.getBeforeString());

        mPvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);     //实例化选择时间空间
        mPvTime.setTime(Helper.getBeforeDate());
        mPvTime.setCyclic(false);
        mPvTime.setCancelable(true);
        mPvTime.setTitle("选择日期");
        mPvTime.setOnTimeSelectListener(this);
    }

    @Override
    protected void toConnect() {
        String startDate = Helper.getMyDate(mStartDateTv.getText().toString());
        String endDate = Helper.getMyDate(mEndDateTv.getText().toString());

        if (type == BARGAIN_TYPE) {
            mConnect.toCustomDayConnect(TAG, "0", startDate, endDate, this);
        } else if (type == ENTRUST_TYPE) {
            mConnect.toEnturstCustomDayConnect(TAG, "0", startDate, endDate, this);
        }
    }


    @Override
    public void onTimeSelect(Date date) {
        if (mJuedgeTv) {
            mStartDateTv.setText(mFormate.format(date));
        } else {
            mEndDateTv.setText(mFormate.format(date));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_OTCBargainZDYStartDate:       //点击开始
                mJuedgeTv = true;
                mPvTime.show();
                break;
            case R.id.tv_OTCBargainZDYEndDate:         //点击结束
                mJuedgeTv = false;
                mPvTime.show();
                break;
            case R.id.tv_OTCBargainZDYQueryBtn:        //点击查询
                if (!TextUtils.isEmpty(mStartDateTv.getText().toString()) && !TextUtils.isEmpty(mEndDateTv.getText().toString())) {

                    String startDay = Helper.getMyDate(mStartDateTv.getText().toString());
                    String endDay = Helper.getMyDate(mEndDateTv.getText().toString());

                    String str = Helper.compareTo(startDay, endDay);

                    int days = Helper.daysBetween(startDay, endDay);

                    if (str.equalsIgnoreCase(startDay)&& !str.equals(startDay)) {
                        showDialog("请选择正确日期,起始日期不能超过截止日期");
                    } else if (days > 90) {
                        showDialog("选择的日期间隔不能超过3个月");
                    } else {
                        onVisible();
                    }
                }


                break;
        }
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(otc_FragmentManager,OTC_BargainCustom.class.toString());
    }


}
