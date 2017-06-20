package com.tpyzq.mobile.pangu.view.chart.formate;

import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tpyzq.mobile.pangu.util.TimeUtil;

/**
 * Created by zhangwenbo on 2016/6/3.
 * 格式化时间轴
 */
public class MyXAxisValueFormatter implements XAxisValueFormatter {

    @Override
    public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
        long time = 0L;
        try {
            time = Long.parseLong(original);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return TimeUtil.dateFormat(time);
    }

}
