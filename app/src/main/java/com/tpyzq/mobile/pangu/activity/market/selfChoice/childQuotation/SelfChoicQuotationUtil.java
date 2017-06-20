package com.tpyzq.mobile.pangu.activity.market.selfChoice.childQuotation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.JudgeStockUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/10/26.
 */
public class SelfChoicQuotationUtil {

    public static final int DEFAULT = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;

    /**
     * 设置箭头位置和方向
     * @param action
     * @param thisheadTv
     * @param beforeheadTv
     */
    public static void settingHeadIv(int action, TextView thisheadTv, TextView beforeheadTv) {
        switch (action) {
            case DEFAULT:
                seetingDisplayHeadIvAndArray(thisheadTv, beforeheadTv);
                break;
            case UP:
                seetingHeadIvAndArrayL(thisheadTv, beforeheadTv);
                break;
            case DOWN:
                seetingHeadIvAndArrayI(thisheadTv, beforeheadTv);
                break;
        }
    }

    private static void seetingHeadIvAndArrayI(TextView thisheadTv, TextView beforeheadTv) {
        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_p);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        thisheadTv.setCompoundDrawables(null, null, drawable, null);//画在右边


        //默认图片的显示
        Drawable drawableDefult = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_default);
        drawableDefult.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        beforeheadTv.setCompoundDrawables(null, null, drawableDefult, null);
    }

    private static void seetingHeadIvAndArrayL(TextView thisheadTv, TextView beforeheadTv) {
        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_n);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        thisheadTv.setCompoundDrawables(null, null, drawable, null);//画在右边


        //默认图片的显示
        Drawable drawableDefult = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_default);
        drawableDefult.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        beforeheadTv.setCompoundDrawables(null, null, drawableDefult, null);
    }

    private static void seetingDisplayHeadIvAndArray(TextView thisheadTv, TextView beforeheadTv) {
        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_default);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界

        thisheadTv.setCompoundDrawables(null, null, drawable, null);//画在右边
        beforeheadTv.setCompoundDrawables(null, null, drawable, null);

//        if (thisheadTv != null) {
//            thisheadTv.setCompoundDrawables(null, null, drawable, null);//画在右边
//        } else if (beforeheadTv != null) {
//            beforeheadTv.setCompoundDrawables(null, null, drawable, null);
//        }
    }


//    /**
//     * 获取集合中沪深300这只股票的position位置
//     * @param entities
//     * @return
//     */
//    public static int getHuShen300Position(ArrayList<StockInfoEntity> entities) {
//
//        int _hushen300Index = -1 ;
//
//        if (entities == null || entities.size() <= 0) {
//            return _hushen300Index;
//        }
//
//        for (StockInfoEntity entity : entities) {
//            if ("20399300".equals(entity.getStockNumber())) {
//                _hushen300Index = entities.indexOf(entity);
//            }
//        }
//
//        return _hushen300Index;
//    }

//    /**
//     * 设置左侧进度条
//     * @param beans
//     * @return
//     */
//    public static int  settingLeftProgress(ArrayList<StockInfoEntity> beans, RoundProgressBar leftProgress, TextView textView) {
//        int winNum      = 0;    //几胜
//        int loseNum     = 0;    //几负
//        int totalSize   = 0;    //总个数
//        double zdf      = 0d;   //涨跌值
//        double zdz      = 0d;   //涨跌值
//        int _hushen300Index = -1;
//
//        for (StockInfoEntity entity : beans) {
//            if ("20399300".equals(entity.getStockNumber())) {
//                zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//                zdz = Helper.getZdz(entity.getClose(), entity.getNewPrice());
//                entity.setPriceChangeRatio("" + zdf);
//                entity.setUpAndDownValue("" + zdz);
//                _hushen300Index = beans.indexOf(entity);
//            }
//        }
//
//        for (StockInfoEntity entity : beans) {
//
//            if (JudgeStockUtils.getStockWay(entity.getStockNumber())) {
//                totalSize = totalSize + 1;
//                double _zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//                double _zdz = Helper.getZdz(entity.getClose(), entity.getNewPrice());
//
//                entity.setPriceChangeRatio("" + _zdf);
//                entity.setUpAndDownValue("" + _zdz);
//
//                if (_zdf > zdf) {
//                    winNum = winNum + 1;
//                } else if (_zdf < zdf) {
//                    loseNum = loseNum + 1;
//                }
//            }
//        }
//
//        int winNumLength = String.valueOf(winNum).length();
//        int loseNumLenght = String.valueOf(loseNum).length();
//
//        SpannableString ss = new SpannableString(winNum + "胜/" + loseNum + "负\n跑赢大盘");
//        settingProTextViewStyle(ss, winNumLength, loseNumLenght);
//
//        textView.setText(ss);
//
//        float persent = (float) winNum/ (float) totalSize;
//
//        float leftProgressData = persent * 100;
//        leftProgress.setProgress((int)leftProgressData);
//
//        return _hushen300Index;
//
//    }

//    /**
//     * 设置右侧Progress的值
//     * @param beans
//     */
//    public static void settingRightProgress (ArrayList<StockInfoEntity> beans, RoundProgressBar rightRoundProgress, TextView textView) {
//        int increase    = 0;    //几涨
//        int decline     = 0;    //几跌
//        int totalSize   = 0;    //总个数
//
//        for (StockInfoEntity entity : beans) {
//            if (JudgeStockUtils.getStockWay(entity.getStockNumber())) {
//                totalSize = totalSize + 1;
//                double _zdz = Helper.getZdz(entity.getClose(), entity.getNewPrice());
//                double _zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//
//                entity.setPriceChangeRatio("" + _zdf);
//                entity.setUpAndDownValue("" + _zdz);
//
//                if (_zdz > 0) {
//                    increase = increase + 1;
//                } else if (_zdz < 0) {
//                    decline = decline + 1;
//                }
//            }
//        }
//
//        int winNumLength = String.valueOf(increase).length();
//        int loseNumLenght = String.valueOf(decline).length();
//
//        SpannableString ss1 = new SpannableString(increase + "涨/" + decline + "跌\n有涨有跌");
//        settingProTextViewStyle(ss1, winNumLength, loseNumLenght);
//        textView.setText(ss1);
//
//        float  persent =  (float) increase/(float) totalSize;
//
//        float rightProgress = persent * 100;
//        rightRoundProgress.setProgress((int)rightProgress);
//    }



//    /**
//     * 新需求 设置几胜几负的算法
//     * @param beans
//     * @return
//     */
//    public static int  settingNewLeftProgress(ArrayList<StockInfoEntity> beans, TextView textView) {
//        int winNum      = 0;    //几胜
//        int loseNum     = 0;    //几负
//        int totalSize   = 0;    //总个数
//        double zdf      = 0d;   //涨跌值
//        double zdz      = 0d;   //涨跌值
//        int _hushen300Index = -1;
//
//        if (beans == null || beans.size() <= 0) {
//
//            int winNumLength = String.valueOf(winNum).length();
//            int loseNumLenght = String.valueOf(loseNum).length();
//
//            SpannableString ss = new SpannableString(winNum + "胜大盘 / " + loseNum + "负大盘");
//            settingNewProTextViewStyle(ss, winNumLength, loseNumLenght);
//
//            return -1;
//        }
//
//        for (StockInfoEntity entity : beans) {
//            if ("20399300".equals(entity.getStockNumber())) {
//                zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//                zdz = Helper.getZdz(entity.getClose(), entity.getNewPrice());
//                entity.setPriceChangeRatio("" + zdf);
//                entity.setUpAndDownValue("" + zdz);
//                _hushen300Index = beans.indexOf(entity);
//            }
//        }
//
//
//        for (StockInfoEntity entity : beans) {
//
//            if (JudgeStockUtils.getStockWay(entity.getStockNumber())) {
//                totalSize = totalSize + 1;
//
//                try {
//                    double _zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//                    double _zdz = Helper.getZdz(entity.getClose(), entity.getNewPrice());
//
//                    if (!TextUtils.isEmpty(entity.getClose()) && !TextUtils.isEmpty(entity.getNewPrice())
//                            && Helper.isDecimal(entity.getClose()) && Helper.isDecimal(entity.getNewPrice())) {
//                        entity.setPriceChangeRatio("" + _zdf);
//                        entity.setUpAndDownValue("" + _zdz);
//
//                        if (_zdf > zdf) {
//                            winNum = winNum + 1;
//                        } else if (_zdf < zdf) {
//                            loseNum = loseNum + 1;
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        int winNumLength = String.valueOf(winNum).length();
//        int loseNumLenght = String.valueOf(loseNum).length();
//
//        SpannableString ss = new SpannableString(winNum + "胜大盘 / " + loseNum + "负大盘");
//        settingNewProTextViewStyle(ss, winNumLength, loseNumLenght);
//
//
//        textView.setText(ss);
//
//        return _hushen300Index;
//
//    }

//    /**
//     * 新需求 设置几涨几跌的算法
//     * @param beans
//     */
//    public static void settingNewRightProgress (ArrayList<StockInfoEntity> beans, TextView textView) {
//        int increase    = 0;    //几涨
//        int decline     = 0;    //几跌
//        int totalSize   = 0;    //总个数
//
//        if (beans == null || beans.size() <= 0) {
//
//            int winNumLength = String.valueOf(increase).length();
//            int loseNumLenght = String.valueOf(decline).length();
//            SpannableString ss1 = new SpannableString(increase + "只上涨 / " + decline + "只下跌");
//            settingNewProTextViewStyle(ss1, winNumLength, loseNumLenght);
//            textView.setText(ss1);
//
//            return;
//        }
//
//        for (StockInfoEntity entity : beans) {
//            if (JudgeStockUtils.getStockWay(entity.getStockNumber())) {
//                totalSize = totalSize + 1;
//                double _zdz = Helper.getZdz(entity.getClose(), entity.getNewPrice());
//                double _zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//
//                entity.setPriceChangeRatio("" + _zdf);
//                entity.setUpAndDownValue("" + _zdz);
//
//                if (_zdz > 0) {
//                    increase = increase + 1;
//                } else if (_zdz < 0) {
//                    decline = decline + 1;
//                }
//            }
//        }
//
//        int winNumLength = String.valueOf(increase).length();
//        int loseNumLenght = String.valueOf(decline).length();
//
//        SpannableString ss1 = new SpannableString(increase + "只上涨 / " + decline + "只下跌");
//        settingNewProTextViewStyle(ss1, winNumLength, loseNumLenght);
//        textView.setText(ss1);
//    }



//    /**
//     * 设置中间的值
//     *
//     */
//    public static void settingMiddleNumber(ArrayList<StockInfoEntity> beans, RoundProgressBar rightRoundProgress,
//                                     RoundProgressBar leftRoundProgress, RelativeLayout viewGroup, TextView textView) {
//        DecimalFormat format = new DecimalFormat("#0.00");
//        double totleZdf = 0d;   //涨跌幅
//        int number = 0;
//        for (StockInfoEntity entity : beans) {
//            if (JudgeStockUtils.getStockWay(entity.getStockNumber())) {
//                double zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//                totleZdf = totleZdf + zdf;
//                number = number + 1;
//            }
//        }
//
//        String formateTotalzdf = format.format(totleZdf / number * 100);
//        SpannableString ss2 = new SpannableString(formateTotalzdf + "%\n平均涨跌");
//
//        int length = formateTotalzdf.length();
//        sttingTotalZdTextViewStyle1(ss2, length);
//
//        textView.setText(ss2);
//
//        if (totleZdf > 0) {
//            viewGroup.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
//            rightRoundProgress.setCricleColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.progressRedBg));
//            leftRoundProgress.setCricleColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.progressRedBg));
//        } else if (totleZdf < 0) {
//            viewGroup.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
//            rightRoundProgress.setCricleColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.progressGreenBg));
//            leftRoundProgress.setCricleColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.progressGreenBg));
//        } else if (totleZdf == 0) {
//            viewGroup.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
//            rightRoundProgress.setCricleColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
//            leftRoundProgress.setCricleColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
//        }
//    }


//    /**
//     * 新UI更改设置中间的值
//     *
//     */
//    public static void settingNewMiddleNumber(ArrayList<StockInfoEntity> beans, View view, TextView textView) {
//        DecimalFormat format = new DecimalFormat("#0.00%");
//        double totleZdf = 0d;   //涨跌幅
//        int number = 0;
//
//        if (beans == null || beans.size() <= 0) {
//
//            String formateTotalzdf = format.format(0.0);
//            SpannableString ss2 = new SpannableString(formateTotalzdf + "\n自选股平均涨跌");
//
//            int length = formateTotalzdf.length();
//            sttingNewTotalZdTextViewStyle1(ss2, length);
//            view.setBackgroundResource(R.mipmap.selfchoice_bg_default);
//            return;
//        }
//
//        for (StockInfoEntity entity : beans) {
//            if (JudgeStockUtils.getStockWay(entity.getStockNumber())) {
//                double zdf = Helper.getZdf(entity.getClose(), entity.getNewPrice());
//                totleZdf = totleZdf + zdf;
//                number = number + 1;
//            }
//        }
//
//        String formateTotalzdf = format.format(totleZdf / number);
//        SpannableString ss2 = new SpannableString(formateTotalzdf + "\n自选股平均涨跌");
//
//        int length = formateTotalzdf.length();
//        sttingNewTotalZdTextViewStyle1(ss2, length);
//
//        textView.setText(ss2);
//
//        if (totleZdf > 0) {
//            view.setBackgroundResource(R.mipmap.selfchoice_bg_up);
////            viewGroup.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.selfchoice_bg_up));
//        } else if (totleZdf < 0) {
//            view.setBackgroundResource(R.mipmap.selfchoice_bg_down);
////            viewGroup.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.selfchoice_bg_down));
//        } else if (totleZdf == 0) {
//            view.setBackgroundResource(R.mipmap.selfchoice_bg_default);
////            viewGroup.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.selfchoice_bg_default));
//        }
//    }

    /**
     * 处理自选股 顶部 胜率计算
     */
    public static void setSelfTopValue(ArrayList<StockInfoEntity> beans, StockInfoEntity hushen300Entity, View viewTopLayour, TextView aTotalTv, TextView winProTv, TextView zdProTv) {
        DecimalFormat format = new DecimalFormat("#0.00%");
        double totleZdf = 0d;   //涨跌幅
        int number = 0;

        ///win
        int winNum      = 0;    //几胜
        int loseNum     = 0;    //几负
        double hs300zdf    = hushen300Entity.getPriceChangeRatio();   //涨跌幅
//        double hs300zdz    =hushen300Entity.getUpAndDownValue();   //涨跌值
        ///zd
        int increase    = 0;    //几涨
        int decline     = 0;    //几跌

        if (beans == null || beans.size() <= 0) {
//            String formateTotalzdf = format.format(0);
//            SpannableString ss2 = new SpannableString(formateTotalzdf + "\n自选股平均涨跌");
//            int length = formateTotalzdf.length();
//            sttingNewTotalZdTextViewStyle1(ss2, length);
//            viewTopLayour.setBackgroundResource(R.mipmap.selfchoice_bg_default);
//
//
//            SpannableString ss = new SpannableString(winNum + "胜大盘 / " + loseNum + "负大盘");
//            settingNewProTextViewStyle(ss, 1, 1);
//            SpannableString ss1 = new SpannableString(0 + "只上涨 / " + 0 + "只下跌");
//            settingNewProTextViewStyle(ss1, 1, 1);
            return;
        }

        for (StockInfoEntity entity : beans) {
            if (JudgeStockUtils.getStockWay(entity.getStockNumber())) {
                try{
                    double zdfT = entity.getPriceChangeRatio();
                    totleZdf = totleZdf + zdfT;
                    number = number + 1;

                    //win
                    double _zdf = entity.getPriceChangeRatio();//Helper.getZdf(entity.getClose(), entity.getNewPrice());
                    double _zdz = entity.getUpAndDownValue();//.getZdz(entity.getClose(), entity.getNewPrice());

                    if ( Helper.isDecimal(entity.getClose()) && Helper.isDecimal(entity.getNewPrice())) {
                        if (_zdf > hs300zdf) {
                            winNum = winNum + 1;
                        } else if (_zdf < hs300zdf) {
                            loseNum = loseNum + 1;
                        }
                    }

                    //zd
                    if (_zdz > 0) {
                        increase = increase + 1;
                    } else if (_zdz < 0) {
                        decline = decline + 1;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        String formateTotalzdf = format.format(totleZdf / number);
        SpannableString ss2 = new SpannableString(formateTotalzdf + "\n自选股平均涨跌");
        int length = formateTotalzdf.length();
        sttingNewTotalZdTextViewStyle1(ss2, length);
        aTotalTv.setText(ss2);
        if (totleZdf > 0) {
            viewTopLayour.setBackgroundResource(R.mipmap.selfchoice_bg_up);
        } else if (totleZdf < 0) {
            viewTopLayour.setBackgroundResource(R.mipmap.selfchoice_bg_down);
        } else if (totleZdf == 0) {
            viewTopLayour.setBackgroundResource(R.mipmap.selfchoice_bg_default);
        }
        //win
        int winNumLength = String.valueOf(winNum).length();
        int loseNumLenght = String.valueOf(loseNum).length();
        SpannableString ss = new SpannableString(winNum + "胜大盘 / " + loseNum + "负大盘");
        settingNewProTextViewStyle(ss, winNumLength, loseNumLenght);
        winProTv.setText(ss);

        //zd
        winNumLength = String.valueOf(increase).length();
        loseNumLenght = String.valueOf(decline).length();
        SpannableString ss1 = new SpannableString(increase + "只上涨 / " + decline + "只下跌");
        settingNewProTextViewStyle(ss1, winNumLength, loseNumLenght);
        zdProTv.setText(ss1);
    }
    /**
     * 设置两个进度条中间的字体颜色和风格
     *
     * @param ss
     */
    private static void sttingNewTotalZdTextViewStyle1(SpannableString ss, int length) {

        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, length - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(2.5f), 0, length + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), length + 1, length + 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    /**
     * 设置两个进度条中间的字体颜色和风格
     *
     * @param ss
     */
    private static void sttingTotalZdTextViewStyle1(SpannableString ss, int length) {

        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), length, length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(2.5f), 0, length + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), length + 1, length + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    /**
     * 设置进度条内部字体颜色风格
     *
     * @param ss
     */
    private static void settingProTextViewStyle(SpannableString ss, int winLength, int loseLength) {
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, winLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), winLength, winLength + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), winLength + 2, winLength + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), winLength + 2, winLength + loseLength + 2 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), winLength + loseLength + 2, winLength + loseLength + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), winLength + loseLength + 3, winLength + loseLength + 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(0.6f), winLength + loseLength + 3, winLength + loseLength + 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }


    /**
     * 设置进度条内部字体颜色风格
     *
     * @param ss
     */
    private static void settingNewProTextViewStyle(SpannableString ss, int winLength, int loseLength) {
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, winLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), winLength, winLength + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), winLength + 3, winLength + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), winLength + 6, winLength + loseLength + 6 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), winLength + loseLength + 6, winLength + loseLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    /**
     * 得到多个股票的code串 并传递到网络请求的参数
     * @param datas
     * @return
     */
    public static String getCodes(ArrayList<StockInfoEntity> datas, View view) {
        StringBuilder sb = new StringBuilder();
        int totalSize   = 0;    //总个数
        for (int i = 0; i < datas.size(); i++) {
            if (JudgeStockUtils.getStockWay(datas.get(i).getStockNumber())) {
                totalSize = totalSize + 1;
            }
            sb.append(datas.get(i).getStockNumber()).append("&");
        }

//        sb.append("20399300&");
        sb.append("20399300");
        if (totalSize == 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return sb.toString();
    }

//    public static ArrayList<StockInfoEntity> updateEntitiy(ArrayList<StockInfoEntity> toUpdateEntity, ArrayList<StockInfoEntity> newEntitiy) {
//
//        for (int i = 0; i < toUpdateEntity.size(); i++) {
//
//            if (!TextUtils.isEmpty(newEntitiy.get(i).getStockNumber()) && "20399300".equals(newEntitiy.get(i).getStockNumber())) {
//            } else {
//                StockInfoEntity stockInfoEntity = newEntitiy.get(i);
//                StockInfoEntity entitiy = toUpdateEntity.get(i);
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getApperHoldStock())) {
//                    entitiy.setApperHoldStock(stockInfoEntity.getApperHoldStock());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getStockNumber())) {
//                    entitiy.setStockNumber(stockInfoEntity.getStockNumber());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getStockName())) {
//                    entitiy.setStockName(stockInfoEntity.getStockName());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getUpAndDownValue())) {
//                    entitiy.setUpAndDownValue(stockInfoEntity.getUpAndDownValue());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getPriceChangeRatio())) {
//                    entitiy.setPriceChangeRatio(stockInfoEntity.getPriceChangeRatio());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getNewPrice())) {
//                    entitiy.setNewPrice(stockInfoEntity.getNewPrice());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getClose())) {
//                    entitiy.setClose(stockInfoEntity.getClose());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getTodayOpenPrice())) {
//                    entitiy.setTodayOpenPrice(stockInfoEntity.getTodayOpenPrice());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getTime())) {
//                    entitiy.setTime(stockInfoEntity.getTime());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getDate())) {
//                    entitiy.setDate(stockInfoEntity.getDate());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getHigh())) {
//                    entitiy.setHigh(stockInfoEntity.getHigh());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getLow())) {
//                    entitiy.setLow(stockInfoEntity.getLow());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getPlate())) {
//                    entitiy.setPlate(stockInfoEntity.getPlate());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getPresentAmount())) {
//                    entitiy.setPresentAmount(stockInfoEntity.getPresentAmount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getTotalVolume())) {
//                    entitiy.setTotalVolume(stockInfoEntity.getTotalVolume());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getTotalPrice())) {
//                    entitiy.setTotalPrice(stockInfoEntity.getTotalPrice());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getInflowMoney())) {
//                    entitiy.setInflowMoney(stockInfoEntity.getInflowMoney());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getOutflowMoney())) {
//                    entitiy.setOutflowMoney(stockInfoEntity.getOutflowMoney());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuyOrder())) {
//                    entitiy.setBuyOrder(stockInfoEntity.getBuyOrder());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSellOrder())) {
//                    entitiy.setSellOrder(stockInfoEntity.getSellOrder());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getTurnover())) {
//                    entitiy.setTurnover(stockInfoEntity.getTurnover());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getTrade())) {
//                    entitiy.setTrade(stockInfoEntity.getTrade());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getIndustryName())) {
//                    entitiy.setIndustryName(stockInfoEntity.getIndustryName());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getIndustryNumber())) {
//                    entitiy.setIndustryNumber(stockInfoEntity.getIndustryNumber());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getPeg())) {
//                    entitiy.setPeg(stockInfoEntity.getPeg());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getFlowEquity())) {
//                    entitiy.setFlowEquity(stockInfoEntity.getFlowEquity());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getTotalEquity())) {
//                    entitiy.setTotalEquity(stockInfoEntity.getTotalEquity());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getPbr())) {
//                    entitiy.setPbr(stockInfoEntity.getPbr());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getIndustryUpAndDown())) {
//                    entitiy.setIndustryUpAndDown(stockInfoEntity.getIndustryUpAndDown());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getEquivalentRatio())) {
//                    entitiy.setEquivalentRatio(stockInfoEntity.getEquivalentRatio());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy1())) {
//                    entitiy.setBuy1(stockInfoEntity.getBuy1());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy2())) {
//                    entitiy.setBuy2(stockInfoEntity.getBuy2());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy3()))
//                    entitiy.setBuy3(stockInfoEntity.getBuy3());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy4()))
//                    entitiy.setBuy4(stockInfoEntity.getBuy4());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy5())) {
//                    entitiy.setBuy5(stockInfoEntity.getBuy5());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy1Amount())) {
//                    entitiy.setBuy1Amount(stockInfoEntity.getBuy1Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy2Amount())) {
//                    entitiy.setBuy2Amount(stockInfoEntity.getBuy2Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy3Amount())) {
//                    entitiy.setBuy3Amount(stockInfoEntity.getBuy3Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy4Amount())) {
//                    entitiy.setBuy4Amount(stockInfoEntity.getBuy4Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBuy5Amount())) {
//                    entitiy.setBuy5Amount(stockInfoEntity.getBuy5Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell1())) {
//                    entitiy.setSell1(stockInfoEntity.getSell1());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell2())) {
//                    entitiy.setSell2(stockInfoEntity.getSell2());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell3())) {
//                    entitiy.setSell3(stockInfoEntity.getSell3());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell4())) {
//                    entitiy.setSell4(stockInfoEntity.getSell4());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell5())) {
//                    entitiy.setSell5(stockInfoEntity.getSell5());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell1Amount())) {
//                    entitiy.setSell1Amount(stockInfoEntity.getSell1Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell2Amount())) {
//                    entitiy.setSell2Amount(stockInfoEntity.getSell2Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell3Amount())) {
//                    entitiy.setSell3Amount(stockInfoEntity.getSell3Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell3Amount())) {
//                    entitiy.setSell4Amount(stockInfoEntity.getSell3Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSell5Amount())) {
//                    entitiy.setSell5Amount(stockInfoEntity.getSell5Amount());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getIsHoldStock())) {
//                    entitiy.setIsHoldStock(stockInfoEntity.getIsHoldStock());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getApperHoldStock())) {
//                    entitiy.setApperHoldStock(stockInfoEntity.getApperHoldStock());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getMKT_VAL())) {
//                    entitiy.setMKT_VAL(stockInfoEntity.getMKT_VAL());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSECU_ACC())) {
//                    entitiy.setSECU_ACC(stockInfoEntity.getSECU_ACC());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSHARE_BLN())) {
//                    entitiy.setSHARE_BLN(stockInfoEntity.getSHARE_BLN());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSHARE_AVL())) {
//                    entitiy.setSHARE_AVL(stockInfoEntity.getSHARE_AVL());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSHARE_QTY())) {
//                    entitiy.setSHARE_QTY(stockInfoEntity.getSHARE_QTY());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getPROFIT_RATIO())) {
//                    entitiy.setPROFIT_RATIO(stockInfoEntity.getPROFIT_RATIO());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getMARKET())) {
//                    entitiy.setMARKET(stockInfoEntity.getMARKET());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getBRANCH())) {
//                    entitiy.setBRANCH(stockInfoEntity.getBRANCH());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getMKT_PRICE())) {
//                    entitiy.setMKT_PRICE(stockInfoEntity.getMKT_PRICE());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSECU_CODE())) {
//                    entitiy.setSECU_CODE(stockInfoEntity.getSECU_CODE());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getINCOME_AMT())) {
//                    entitiy.setINCOME_AMT(stockInfoEntity.getINCOME_AMT());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getSECU_NAME())) {
//                    entitiy.setSECU_NAME(stockInfoEntity.getSECU_NAME());
//                }
//
//                if (!TextUtils.isEmpty(stockInfoEntity.getCURRENT_COST())) {
//                    entitiy.setCURRENT_COST(stockInfoEntity.getCURRENT_COST());
//                }
//            }
//
//        }
//
//        return toUpdateEntity;
//    }


}
