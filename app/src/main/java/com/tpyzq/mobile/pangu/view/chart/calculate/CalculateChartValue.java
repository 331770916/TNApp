package com.tpyzq.mobile.pangu.view.chart.calculate;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import com.tpyzq.mobile.pangu.data.ChartKEntity;
import com.tpyzq.mobile.pangu.data.MACDEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/6/16.
 * 工具类，该类的作用是各种K线图所需的经过算法计算所需的值
 */
public class CalculateChartValue {

    /**
     * 计算MACD数据
     * @param chartKEntities 数据源
     * @param context
     * @return
     */
    public static List<MACDEntity> convertMACDData(List<ChartKEntity> chartKEntities, Context context){
        int macdL = 12;
        int macdM = 26;
        int macdS = 9;

        List<MACDEntity> macd = new ArrayList<MACDEntity>();
        double[] inReal = new double[chartKEntities.size()];

        for (int i = 0; i < inReal.length; i++) {
            inReal[i] = Double.parseDouble(chartKEntities.get(i).getClose());
        }

        double[] outMACD = new double[chartKEntities.size()];
        double[] outMACDSignal = new double[chartKEntities.size()];
        double[] outMACDHist = new double[chartKEntities.size()];

        MInteger outBegIdx = new MInteger();
        outBegIdx.value = 0;
        MInteger outNBElement = new MInteger();
        outNBElement.value = 0;
        Core core = new Core();
        RetCode retCode = core.macd(0, chartKEntities.size() - 1, inReal, macdS, macdL, macdM, outBegIdx, outNBElement, outMACD, outMACDSignal, outMACDHist);
        if (RetCode.Success == retCode) {
            double _max = outMACDHist[0] * 2; //选取数组中最大的值所用

            for (int i = 0; i < inReal.length; i++) {
                ChartKEntity chartKEntity = chartKEntities.get(i);

                try {
                    if (i<outBegIdx.value){
                        // macd == 0
                        MACDEntity macdEntity = new MACDEntity(Float.MAX_VALUE, Float.MAX_VALUE, 0, chartKEntity.getTime());
                        macd.add(macdEntity);
                    }else {
                        int index = i-outBegIdx.value;
                        if (outMACDHist[index] * 2 > _max) {
                            _max = outMACDHist[index] * 2;
                        }
                        MACDEntity macdEntity = new MACDEntity(outMACDSignal[index], outMACD[index], outMACDHist[index] * 2, chartKEntity.getTime());
                        MACDEntity.value = ((float) _max) + 0.1f;
                        macd.add(macdEntity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return macd;
    }

    /**
     *
     * @param chartKEntities
     * @param context
     * @return
     * 获得KDJ数据
     */
    public static ArrayList<ArrayList<Entry>> convertKDJData(List<ChartKEntity> chartKEntities, Context context){
        int kdjN = 9;

        ArrayList<ArrayList<Entry>> entrys = new ArrayList<>();

        ArrayList<Entry> values1 = new ArrayList<Entry>();
        ArrayList<Entry> values2 = new ArrayList<Entry>();
        ArrayList<Entry> values3 = new ArrayList<Entry>();


        double[] inHigh = new double[chartKEntities.size()];
        for (int i = 0; i < inHigh.length; i++) {
            inHigh[i] = Double.parseDouble(chartKEntities.get(i).getHigh());
        }

        double[] inLow = new double[chartKEntities.size()];
        for (int i = 0; i < inLow.length; i++) {
            inLow[i] = Double.parseDouble(chartKEntities.get(i).getLow());
        }

        double[] inClose = new double[chartKEntities.size()];
        for (int i = 0; i < inClose.length; i++) {
            inClose[i] = Double.parseDouble(chartKEntities.get(i).getClose());
        }

        MInteger outBegIdx = new MInteger();
        outBegIdx.value = 0;
        MInteger outNBElement = new MInteger();
        outNBElement.value = 0;

        double[] outSlowK = new double[chartKEntities.size()];
        double[] outSlowD = new double[chartKEntities.size()];

        Core core = new Core();
        RetCode retCode = core.stoch(0, chartKEntities.size()-1, inHigh, inLow, inClose, kdjN, 3, MAType.Ema, 3, MAType.Ema, outBegIdx, outNBElement, outSlowK, outSlowD);

        if (RetCode.Success == retCode) {
            for (int i = 0; i < chartKEntities.size(); i++) {
                try {
                    if (i<outBegIdx.value){
                        values1.add(new Entry((float) 0, i));
                        values2.add(new Entry((float) 0, i));
                        values3.add(new Entry((float) 0, i));
                    } else {
                        int index = i-outBegIdx.value;
                        values1.add(new Entry((float) outSlowK[index], i));
                        values2.add(new Entry((float) outSlowD[index], i));
                        double slowKLine3k2d = 3 * outSlowK[index] - 2 * outSlowD[index];
                        values3.add(new Entry((float) slowKLine3k2d, i));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            entrys.add(values1);
            entrys.add(values2);
            entrys.add(values3);
        }

        return entrys;
    }
}
