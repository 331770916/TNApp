package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/12.
 * 各种股票的列表的Adapter
 */
public class StockListAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mBeans;
//    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;
    private String mFromStockListTag;
    public StockListAdapter () {
//        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
    }

    public void setData(ArrayList<StockInfoEntity> beans, String fromStockListTag) {
        mBeans = beans;
        mFromStockListTag = fromStockListTag;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mBeans != null && mBeans.size() > 0) {
            return mBeans.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (mBeans != null && mBeans.size() > 0) {
            return mBeans.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_stocklist, null);
            viewHodler.turnoverName = (TextView) convertView.findViewById(R.id.stockItemName);
            viewHodler.turnoverNumber = (TextView) convertView.findViewById(R.id.stockItemNumber);
            viewHodler.turnoverPrice = (TextView) convertView.findViewById(R.id.stockItemPrice);
            viewHodler.turnoverRate = (TextView) convertView.findViewById(R.id.stockItemRate);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }


        viewHodler.turnoverName.setText("--");
        viewHodler.turnoverNumber.setText("--");
        viewHodler.turnoverPrice.setText("--");
        viewHodler.turnoverRate.setText("--");

        if (!TextUtils.isEmpty(mBeans.get(position).getStockName())) {
            viewHodler.turnoverName.setText(mBeans.get(position).getStockName());
        }

        viewHodler.turnoverName.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));

        if (!TextUtils.isEmpty(mBeans.get(position).getStockNumber())) {
            viewHodler.turnoverNumber.setText(Helper.getStockNumber(mBeans.get(position).getStockNumber()));
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getNewPrice())) {

            if (Helper.isDecimal(mBeans.get(position).getNewPrice())
                    || Helper.isENum(mBeans.get(position).getNewPrice())
                    && !TextUtils.isEmpty(mBeans.get(position).getStockNumber())) {

                String strPrice = TransitionUtils.fundPirce(mBeans.get(position).getStockNumber(), mBeans.get(position).getNewPrice());
                viewHodler.turnoverPrice.setText(strPrice);
            } else {
                viewHodler.turnoverPrice.setText(mBeans.get(position).getNewPrice());
            }


        }


        if ("lingzhang".equals(mFromStockListTag) || "lingdie".equals(mFromStockListTag)) {
            //涨跌幅
            String tt = mBeans.get(position).getNewPrice();
            if(!TextUtils.isEmpty(tt)) {

//                if (Helper.isDecimal(priceRatio) || Helper.isENum(priceRatio)) {
                    double zdf = mBeans.get(position).getPriceChangeRatio();
                    String formateZdf = mFormat2.format(zdf);
                    viewHodler.turnoverRate.setText(formateZdf);
                    if (zdf > 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (zdf == 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (zdf < 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }

//                } else {
//                    viewHodler.turnoverRate.setText(priceRatio);
//                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
//                    viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
//                }


            }


        } else if ("zijinliuru".equals(mFromStockListTag) || "zijinliuchu".equals(mFromStockListTag)) {

            //资金流入流出
            if (!TextUtils.isEmpty(mBeans.get(position).getPriceToal())) {

                if (Helper.isDecimal(mBeans.get(position).getPriceToal())
                        || Helper.isENum(mBeans.get(position).getPriceToal())) {

                    double changeHandRatio = Double.parseDouble(mBeans.get(position).getPriceToal());
                    viewHodler.turnoverRate.setText(Helper.long2million(mBeans.get(position).getPriceToal()));

                    if (changeHandRatio > 0) {
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (changeHandRatio == 0) {
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (changeHandRatio < 0) {
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }
                } else {
                    viewHodler.turnoverRate.setText(mBeans.get(position).getPriceToal());
                    viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                }


                double priceRatio = mBeans.get(position).getPriceChangeRatio();

//                if (Helper.isDecimal(priceRatio) || Helper.isENum(priceRatio)) {
                    if (priceRatio > 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (priceRatio == 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (priceRatio < 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }
//                } else {
//                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
//                }
            }

        } else if ("huanshou".equals(mFromStockListTag)) {

            double priceRatio = mBeans.get(position).getPriceChangeRatio();
            String turnover = mBeans.get(position).getTurnover();
            //换手率
            if (!TextUtils.isEmpty(turnover)) {

                if (Helper.isDecimal(turnover) || Helper.isENum(turnover)) {
                    viewHodler.turnoverRate.setText(mFormat2.format(Double.parseDouble(turnover)));
                    if (!TextUtils.isEmpty(turnover) && Double.parseDouble(turnover) > 0) {
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (!TextUtils.isEmpty(turnover) && Double.parseDouble(turnover) == 0) {
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (!TextUtils.isEmpty(turnover) && Double.parseDouble(turnover) < 0) {
                        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }
                } else {
                    viewHodler.turnoverRate.setText(turnover);
                    viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                }

            }

//            if (Helper.isDecimal(priceRatio) || Helper.isENum(priceRatio)) {
                if (priceRatio > 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                } else if (priceRatio == 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                } else if (priceRatio < 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                }
//            } else {
//                viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
//            }


        }

        return convertView;
    }

    private class ViewHodler {
        TextView turnoverName;
        TextView turnoverNumber;
        TextView turnoverPrice;
        TextView turnoverRate;
    }
}
