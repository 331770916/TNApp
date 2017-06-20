package com.tpyzq.mobile.pangu.activity.market.selfChoice.childQuotation;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/8/17.
 * 自选行情Adapter
 */
public class SelfChoiceQuotationAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mDatas;
    private boolean mJudgeOption;       //判断  刷新数据的时候， 当前状态是 涨跌值  还是涨跌幅   true 是涨跌幅， false 是涨跌值
    private OnSelfClickListener mOnSelfClickListener;
//    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;
    private int colorRed,colorGreen,colorGray ;
    private String mAppearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");

    public SelfChoiceQuotationAdapter() {
//        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
        colorRed = ContextCompat.getColor(CustomApplication.getContext(), R.color.red);
        colorGreen =ContextCompat.getColor(CustomApplication.getContext(), R.color.green);
        colorGray  =ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor);
//        LogHelper.e("SelfChoiceQuotationAdapter ","mAppearHold:"+mAppearHold);
    }
    public void refleshHoldStatus(){
        mAppearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");
    }
    public void setDatas(ArrayList<StockInfoEntity> datas, boolean judgeOption) {
        mDatas = datas;
        mJudgeOption = judgeOption;
//        mAppearHold = SpUtils.getString(CustomApplication.getContext(), FileUtil.APPEARHOLD, "false");
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mDatas != null ) {
            return mDatas.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_selfchoice_quotation, null);
        }
        TextView stockName = (TextView) convertView.findViewById(R.id.selfchoice_Name);
        TextView stockNumber = (TextView) convertView.findViewById(R.id.selfchoice_Number);
        TextView currentPrice = (TextView) convertView.findViewById(R.id.selfchoice_Price);
        ImageView iv_color_mask = (ImageView) convertView.findViewById(R.id.iv_color_mask);
        final TextView zdf = (TextView) convertView.findViewById(R.id.selfchoice_zdf);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.selfchoice_HoldIc);
        ImageView hotIv = (ImageView) convertView.findViewById(R.id.selfchoice_HotIc);
        imageView.setVisibility(View.GONE);
        hotIv.setVisibility(View.GONE);

        stockName.setText(null);
        stockNumber.setText(null);
        currentPrice.setText(null);
        zdf.setText(null);
//        setImageAnimation(iv_color_mask, mDatas.get(position).getUp_down());
        try {
            StockInfoEntity sie = mDatas.get(position);
            if (!TextUtils.isEmpty(sie.getStockName())) {
                stockName.setText(sie.getStockName());
            }

            if (!TextUtils.isEmpty(sie.getStockNumber())) {
                stockNumber.setText(Helper.getStockNumber(sie.getStockNumber()));
            }

            if ("true".equals(mAppearHold) &&"true".equals(sie.getStockholdon())) {
                imageView.setVisibility(View.VISIBLE);
            }

            if ("1".equals(sie.getHot())) {
                hotIv.setVisibility(View.VISIBLE);
            }
            boolean isZero = false;
            if (!TextUtils.isEmpty(sie.getNewPrice())) {
                if (Helper.isDecimal(sie.getNewPrice())) {
                    double nPrice = TransitionUtils.string2double(sie.getNewPrice());
                    if (nPrice == 0) {
                        isZero = true;
                        currentPrice.setText("--");
                    } else {
                        currentPrice.setText(TransitionUtils.fundPirce(sie.getStockNumber(), sie.getNewPrice()));
                    }
                } else {
                    currentPrice.setText("--");
                }
            }

            if (!TextUtils.isEmpty(sie.getClose()) && !TextUtils.isEmpty(sie.getNewPrice())) {
                if (Helper.isDecimal(sie.getClose())
                        && Helper.isDecimal(sie.getNewPrice())
                        || Helper.isENum(sie.getClose())
                        && Helper.isENum(sie.getNewPrice())) {
//                    double _currentPrice = Double.parseDouble(sie.getNewPrice());
//                    double _close = Double.parseDouble(sie.getClose());
                    double _zdValue = sie.getUpAndDownValue();
                    double _zdf = sie.getPriceChangeRatio();

                    if (_zdValue > 0) {
                        zdf.setBackgroundColor(colorRed);
                    } else if (_zdValue == 0) {
                        zdf.setBackgroundColor(colorGray);
                    } else if (_zdValue < 0) {
                        zdf.setBackgroundColor(colorGreen);
                    }
                    if(isZero){
                        zdf.setText("--");
                    }else{
                        if (mJudgeOption) {
                            if (_zdf > 0) {
                                zdf.setText("+" + mFormat2.format(_zdf));
                            } else {
                                zdf.setText(mFormat2.format(_zdf));
                            }
                        }else{
                            if (_zdValue > 0) {
                                zdf.setText("+" + TransitionUtils.fundPirce(sie.getStockNumber(), _zdValue+""));
                            } else {
                                zdf.setText(TransitionUtils.fundPirce(sie.getStockNumber(), _zdValue+""));
                            }
                        }
                    }

                    zdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mJudgeOption = !mJudgeOption;
                            if (null != mOnSelfClickListener) {
                                mOnSelfClickListener.onClick(v, mJudgeOption);
                            }
                            notifyDataSetInvalidated();
                        }
                    });
                } else {
                    zdf.setText("--");
                    zdf.setBackgroundColor(colorGray);
                    zdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mJudgeOption = !mJudgeOption;
                            if (null != mOnSelfClickListener) {
                                mOnSelfClickListener.onClick(v, mJudgeOption);
                            }
                            notifyDataSetInvalidated();
                        }
                    });
                }
            } else {
                zdf.setText("--");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

//    private void setImageAnimation(final ImageView iv, int i) {
//        ObjectAnimator animator;
//        switch (i) {
//            case 0:
//                iv.setVisibility(View.VISIBLE);
//                iv.setImageResource(R.mipmap.item_green);
//
//                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                        Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
//                        1.0f);
//
//                translateAnimation.setDuration(1000);
//
//                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        iv.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//                iv.setAnimation(translateAnimation);
//
////                iv.setVisibility(View.GONE);
//
//                break;
//            case 1:
//                iv.setVisibility(View.VISIBLE);
//                iv.setImageResource(R.mipmap.item_red);
//
//                TranslateAnimation translateAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
//                        -1.0f);
//
//                translateAnimation1.setDuration(1000);
//
//                translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        iv.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                iv.setAnimation(translateAnimation1);
//
//
//
//                break;
//            case 2:
//                iv.setVisibility(View.GONE);
////                animator = ObjectAnimator.ofFloat(iv, "Y", 0, 0);
////                animator.setDuration(1800);
////                animator.start();
//                break;
//        }
//    }

    public void setSelfChlickListener(OnSelfClickListener listener) {
        mOnSelfClickListener = listener;
    }

    public interface OnSelfClickListener {
        void onClick(View v, boolean falg);
    }
}
