package com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart;

import android.util.SparseArray;

import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.MoneyFundFundChartView;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/9/27.
 */
public class OnMounthLabels implements MoneyFundFundChartView.TypeXLabels {

    private boolean mIsFundMoney;

    public OnMounthLabels(boolean isFundMoney) {
        mIsFundMoney = isFundMoney;
    }

    @Override
    public SparseArray<String> setXOnLabels(ArrayList<String> dates) {
        if (dates == null || dates.size() <= 0) {
            return null;
        }

        SparseArray<String> xLabels = new SparseArray<>();

        xLabels.put(0, dates.get(0));

        if (!mIsFundMoney) {

            double min4P1 = Math.max((dates.size() - 1)/3, dates.size()/3);
            double min4p3 = Math.min((dates.size() - 1) * 2/3, dates.size()*2/3);
            int towD = (int)min4P1;
            int threeD = (int)min4p3;
            xLabels.put(towD, dates.get(towD));
            xLabels.put(threeD, dates.get(threeD));
        }

        xLabels.put(dates.size()-1, dates.get(dates.size()-1));

        return xLabels;
    }
}
