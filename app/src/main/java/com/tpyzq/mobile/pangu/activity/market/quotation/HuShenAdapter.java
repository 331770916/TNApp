package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/8/20.
 */
public class HuShenAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mBeans;
    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;
    private Activity mActivity;

    public HuShenAdapter(Activity activity) {
        mActivity = activity;
        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
    }

    public void setDatas(ArrayList<StockInfoEntity> beans) {
        mBeans = beans;
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
    public int getItemViewType(int position) {
        String type = mBeans.get(position).getType();
        int itemViewType = -1;
        if (!TextUtils.isEmpty(type) && type.equals("1")) {
            itemViewType = 0;
        } else if (!TextUtils.isEmpty(type) && type.equals("4")) {
            itemViewType = 1;
        } else if (!TextUtils.isEmpty(type) && type.equals("2")) {
            itemViewType = 2;
        } else if (!TextUtils.isEmpty(type) && type.equals("3")) {
            itemViewType = 3;
        }

        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        ViewHodler2 viewHodler2 = null;
        ViewHodler3 viewHodler3 = null;

        if (convertView == null) {

            switch (type) {
                case 0:
                    convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.hushen_item1, null);
                    break;
                case 1:
                    convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.hushen_item2, null);
                    break;
                case 2:
                    convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstock_top, null);
                    viewHodler2 = new ViewHodler2();

                    viewHodler2.shangZ_name = (TextView) convertView.findViewById(R.id.newStock_shangZheng);
                    viewHodler2.shangZ_currentPrice = (TextView) convertView.findViewById(R.id.newStock_shangZhengTv1);
                    viewHodler2.shangZ_zd = (TextView) convertView.findViewById(R.id.newStock_shangZhengTv2);
                    viewHodler2.shangZ_zdf = (TextView) convertView.findViewById(R.id.newStock_shangZhengTv3);

                    viewHodler2.shengZ_name = (TextView) convertView.findViewById(R.id.newStock_shenZheng);
                    viewHodler2.shenZ_currentPrice1 = (TextView) convertView.findViewById(R.id.newStock_shengZhengTv1);
                    viewHodler2.shenZ_zd = (TextView) convertView.findViewById(R.id.newStock_shengZhengTv2);
                    viewHodler2.shenZ_zdf = (TextView) convertView.findViewById(R.id.newStock_shengZhengTv3);

                    viewHodler2.chuangY_name = (TextView) convertView.findViewById(R.id.newStock_chuangye);
                    viewHodler2.chuangY_currentPrice = (TextView) convertView.findViewById(R.id.newStock_chuangyeTv1);
                    viewHodler2.chuangY_zd = (TextView) convertView.findViewById(R.id.newStock_chuangyeTv2);
                    viewHodler2.chuangY_zdf = (TextView) convertView.findViewById(R.id.newStock_chuangyeTv3);

                    viewHodler2.shangzheng = (RelativeLayout) convertView.findViewById(R.id.shangzheng);
                    viewHodler2.shenzheng = (RelativeLayout) convertView.findViewById(R.id.shengzheng);
                    viewHodler2.changye = (RelativeLayout) convertView.findViewById(R.id.changye);

                    viewHodler2.topBgLayout1 = (RelativeLayout) convertView.findViewById(R.id.rl_top1Bg);
                    viewHodler2.topBgLayout2 = (RelativeLayout) convertView.findViewById(R.id.rl_top2Bg);
                    viewHodler2.topBgLayout3 = (RelativeLayout) convertView.findViewById(R.id.rl_top3Bg);

                    convertView.setTag(viewHodler2);

                    break;
                case 3:
                    convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstock_middle, null);
                    viewHodler3 = new ViewHodler3();
                    viewHodler3.plantName1 = (TextView) convertView.findViewById(R.id.electronicInformation);
                    viewHodler3.plantRatio1 = (TextView) convertView.findViewById(R.id.electronicInformationTv1);
                    viewHodler3.stockName1 = (TextView) convertView.findViewById(R.id.electronicInformationTv2);
                    viewHodler3.stockZd1 = (TextView) convertView.findViewById(R.id.electronicInformationTv3);
                    viewHodler3.stockZdf1 = (TextView) convertView.findViewById(R.id.electronicInformationTv4);

                    viewHodler3.plantName2 = (TextView) convertView.findViewById(R.id.communication);
                    viewHodler3.plantRatio2 = (TextView) convertView.findViewById(R.id.communicationTv1);
                    viewHodler3.stockName2 = (TextView) convertView.findViewById(R.id.communicationTv2);
                    viewHodler3.stockZd2 = (TextView) convertView.findViewById(R.id.communicationTv3);
                    viewHodler3.stockZdf2 = (TextView) convertView.findViewById(R.id.communicationTv4);

                    viewHodler3.plantName3 = (TextView) convertView.findViewById(R.id.transportLogistics);
                    viewHodler3.plantRatio3 = (TextView) convertView.findViewById(R.id.transportLogisticsTv1);
                    viewHodler3.stockName3 = (TextView) convertView.findViewById(R.id.transportLogisticsTv2);
                    viewHodler3.stockZd3 = (TextView) convertView.findViewById(R.id.transportLogisticsTv3);
                    viewHodler3.stockZdf3 = (TextView) convertView.findViewById(R.id.transportLogisticsTv4);

                    viewHodler3.plantName4 = (TextView) convertView.findViewById(R.id.foreignTrade);
                    viewHodler3.plantRatio4 = (TextView) convertView.findViewById(R.id.foreignTradeTv1);
                    viewHodler3.stockName4 = (TextView) convertView.findViewById(R.id.foreignTradeTv2);
                    viewHodler3.stockZd4 = (TextView) convertView.findViewById(R.id.foreignTradeTv3);
                    viewHodler3.stockZdf4 = (TextView) convertView.findViewById(R.id.foreignTradeTv4);

                    viewHodler3.plantName5 = (TextView) convertView.findViewById(R.id.machinery);
                    viewHodler3.plantRatio5 = (TextView) convertView.findViewById(R.id.machineryTv1);
                    viewHodler3.stockName5 = (TextView) convertView.findViewById(R.id.machineryTv2);
                    viewHodler3.stockZd5 = (TextView) convertView.findViewById(R.id.machineryTv3);
                    viewHodler3.stockZdf5 = (TextView) convertView.findViewById(R.id.machineryTv4);

                    viewHodler3.plantName6 = (TextView) convertView.findViewById(R.id.buildingMaterials);
                    viewHodler3.plantRatio6 = (TextView) convertView.findViewById(R.id.buildingMaterialsTv1);
                    viewHodler3.stockName6 = (TextView) convertView.findViewById(R.id.buildingMaterialsTv2);
                    viewHodler3.stockZd6 = (TextView) convertView.findViewById(R.id.buildingMaterialsTv3);
                    viewHodler3.stockZdf6 = (TextView) convertView.findViewById(R.id.buildingMaterialsTv4);

                    viewHodler3.plantLayout1 = (RelativeLayout) convertView.findViewById(R.id.newStock_gv4);
                    viewHodler3.plantLayout2 = (RelativeLayout) convertView.findViewById(R.id.newStock_gv5);
                    viewHodler3.plantLayout3 = (RelativeLayout) convertView.findViewById(R.id.newStock_gv6);
                    viewHodler3.plantLayout4 = (RelativeLayout) convertView.findViewById(R.id.newStock_gv7);
                    viewHodler3.plantLayout5 = (RelativeLayout) convertView.findViewById(R.id.newStock_gv8);
                    viewHodler3.plantLayout6 = (RelativeLayout) convertView.findViewById(R.id.newStock_gv9);

                    convertView.setTag(viewHodler3);
                    break;
            }
        } else {
            switch (type) {
                case 2:
                    viewHodler2 = (ViewHodler2) convertView.getTag();
                    break;
                case 3:
                    viewHodler3 = (ViewHodler3) convertView.getTag();
                    break;
            }
        }

        if (type == 0) {
            TextView titleView = (TextView) convertView.findViewById(R.id.hushen_category_tv);
            titleView.setText(mBeans.get(position).getTitleTv());
        } else if (type == 1) {
            initBottomView(convertView, position);
        } else if (type == 2) {
            if (viewHodler2 != null) {
                initTopView(viewHodler2, position);
            }
        } else if (type == 3) {
            if (viewHodler3 != null) {
                initMiddleView(viewHodler3, position);
            }

        }

        return convertView;
    }

    private class ViewHodler2 {
        TextView shangZ_name;
        TextView shangZ_currentPrice;
        TextView shangZ_zd;
        TextView shangZ_zdf;

        TextView shengZ_name;
        TextView shenZ_currentPrice1;
        TextView shenZ_zd;
        TextView shenZ_zdf;

        TextView chuangY_name;
        TextView chuangY_currentPrice;
        TextView chuangY_zd;
        TextView chuangY_zdf;

        RelativeLayout shangzheng;
        RelativeLayout shenzheng;
        RelativeLayout changye;

        RelativeLayout topBgLayout1;
        RelativeLayout topBgLayout2;
        RelativeLayout topBgLayout3;
    }

    private class ViewHodler3 {

        RelativeLayout plantLayout1;
        RelativeLayout plantLayout2;
        RelativeLayout plantLayout3;
        RelativeLayout plantLayout4;
        RelativeLayout plantLayout5;
        RelativeLayout plantLayout6;

        TextView plantName1;
        TextView plantRatio1;
        TextView stockName1;
        TextView stockZd1;
        TextView stockZdf1;

        TextView plantName2;
        TextView plantRatio2;
        TextView stockName2;
        TextView stockZd2;
        TextView stockZdf2;

        TextView plantName3;
        TextView plantRatio3;
        TextView stockName3;
        TextView stockZd3;
        TextView stockZdf3;

        TextView plantName4;
        TextView plantRatio4;
        TextView stockName4;
        TextView stockZd4;
        TextView stockZdf4;

        TextView plantName5;
        TextView plantRatio5;
        TextView stockName5;
        TextView stockZd5;
        TextView stockZdf5;

        TextView plantName6;
        TextView plantRatio6;
        TextView stockName6;
        TextView stockZd6;
        TextView stockZdf6;
    }

    /**
     * 初始化底部布局
     *
     * @param convertView
     * @param position
     */
    private void initBottomView(final View convertView, int position) {

        final TextView stockName = (TextView) convertView.findViewById(R.id.hushen_stockName);
        TextView stockNumber = (TextView) convertView.findViewById(R.id.hushen_stockNumber);
        TextView currentPrice = (TextView) convertView.findViewById(R.id.hushen_stockCurrentPrice);
        TextView stockZdf = (TextView) convertView.findViewById(R.id.hushen_stockZD);

        //解决错乱问题
        stockName.setText("--");
        stockNumber.setText("--");
        currentPrice.setText("--");
        stockZdf.setText("--");

        if (!TextUtils.isEmpty(mBeans.get(position).getStockName())) {
            stockName.setText(mBeans.get(position).getStockName());
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getStockNumber())) {

            String _stockNumber = mBeans.get(position).getStockNumber();
            _stockNumber = _stockNumber.substring(2, _stockNumber.length());

            stockNumber.setText(_stockNumber);
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getNewPrice())) {

            if (Helper.isDecimal(mBeans.get(position).getNewPrice()) || Helper.isENum(mBeans.get(position).getNewPrice())) {
                currentPrice.setText(TransitionUtils.fundPirce(mBeans.get(position).getStockNumber(), mBeans.get(position).getNewPrice()));
            } else {
                currentPrice.setText(mBeans.get(position).getNewPrice());
            }


            if (!TextUtils.isEmpty(mBeans.get(position).getNewPrice())) {

//                if (Helper.isDecimal(mBeans.get(position).getPriceChangeRatio())) {
                double zdf = mBeans.get(position).getPriceChangeRatio();

                if (zdf > 0f) {
                    currentPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                } else if (zdf == 0f) {
                    currentPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                } else {
                    currentPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                }
//                } else {
//                    currentPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
//                }


            }
        }


        if (mBeans.get(position).getIsChangeHand() == 1004 ) {//&&            !TextUtils.isEmpty(mBeans.get(position).getPriceChangeRatio())

//            if (Helper.isDecimal(mBeans.get(position).getPriceChangeRatio())) {
            double zdf = mBeans.get(position).getPriceChangeRatio();
            stockZdf.setText(mFormat2.format(zdf));

            if (zdf > 0f) {
                stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
            } else if (zdf == 0f) {
                stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
            } else {
                stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
            }
//            } else {
//                stockZdf.setText(mBeans.get(position).getPriceChangeRatio());
//                stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
//            }


        } else if (mBeans.get(position).getIsChangeHand() == 1003
                && !TextUtils.isEmpty(mBeans.get(position).getTurnover())) {
            if ("-".equals(mBeans.get(position).getTurnover())) {
                stockZdf.setText(mBeans.get(position).getTurnover());
            } else {
                stockZdf.setText(mFormat2.format(Double.parseDouble(mBeans.get(position).getTurnover())));
            }
            stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
        } else if (mBeans.get(position).getFoundFlag() == 1000 && !TextUtils.isEmpty(mBeans.get(position).getPriceToal())) {
            stockZdf.setText(Helper.long2million(mBeans.get(position).getPriceToal()));
            stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
        } else if (mBeans.get(position).getFoundFlag() == 1001 && !TextUtils.isEmpty(mBeans.get(position).getPriceToal())) {
            stockZdf.setText(Helper.long2million(mBeans.get(position).getPriceToal()));
            stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
        }

    }


    private void initTopView(ViewHodler2 viewHodler2, int position) {

        TextView[] textViews = {viewHodler2.shengZ_name, viewHodler2.shenZ_currentPrice1, viewHodler2.shenZ_zd, viewHodler2.shenZ_zdf,//这四组是上证
                viewHodler2.shangZ_name, viewHodler2.shangZ_currentPrice, viewHodler2.shangZ_zd, viewHodler2.shangZ_zdf,//这四组是深证
                viewHodler2.chuangY_name, viewHodler2.chuangY_currentPrice, viewHodler2.chuangY_zd, viewHodler2.chuangY_zdf};//这四组是创业

        RelativeLayout[] rls = {viewHodler2.topBgLayout1, viewHodler2.topBgLayout2, viewHodler2.topBgLayout3};
        RelativeLayout[] rl_top = {viewHodler2.shangzheng,viewHodler2.shenzheng,viewHodler2.changye};

        List<StockInfoEntity> _beans = mBeans.get(position).getSubDatas();

        viewHodler2.shangzheng.setOnClickListener(new AdapterTopClickListener(_beans, mActivity, 1));
        viewHodler2.shenzheng.setOnClickListener(new AdapterTopClickListener(_beans, mActivity, 0));
        viewHodler2.changye.setOnClickListener(new AdapterTopClickListener(_beans, mActivity, 2));

        StockInfoEntity modelshang = null;
        StockInfoEntity modelshen = null;
        StockInfoEntity modelchuang = null;

        StockInfoEntity model = null;

        for (int i = 0; i < _beans.size(); i++) {
            model = _beans.get(i);
            if (null==model)return;
            if ("10000001".equals(model.getStockNumber())) {
                modelshang = model;
            } else if ("20399001".equals(model.getStockNumber())) {
                modelshen = model;
            } else if ("20399006".equals(model.getStockNumber())) {
                modelchuang = model;
            }
        }
        //清除数据
        _beans.clear();
        //重新添加规整数据
        _beans.add(modelshang);
        _beans.add(modelshen);
        _beans.add(modelchuang);


        if (null != _beans && _beans.size() > 0) {

            int num = 0;
            for (int i = 0; i < textViews.length; i++) {
                if ((i + 1) % 4 == 0 && _beans.get(num) != null) {
                    int colors = 100;// 100是绿色， 101是红色  102 是黑色

                    if (!TextUtils.isEmpty(_beans.get(num).getStockName())) {

                        textViews[i - 3].setText(_beans.get(num).getStockName());
                    } else {
                        textViews[i - 3].setText("--");
                    }

                    if (!TextUtils.isEmpty(_beans.get(num).getNewPrice())) {

                        if (Helper.isDecimal(_beans.get(num).getNewPrice())) {
                            if (!TextUtils.isEmpty(_beans.get(num).getStockNumber())) {
                                textViews[i - 2].setText(TransitionUtils.fundPirce(_beans.get(num).getStockNumber(), mFormat1.format(Double.parseDouble(_beans.get(num).getNewPrice()))));
                            } else {
                                textViews[i - 2].setText(mFormat1.format(Double.parseDouble(_beans.get(num).getNewPrice())));
                            }
                        } else {
                            textViews[i - 2].setText(_beans.get(num).getNewPrice());
                        }


                    } else {
                        textViews[i - 2].setText("--");
                    }

                    if (!TextUtils.isEmpty(_beans.get(num).getNewPrice()) &&
                            !TextUtils.isEmpty(_beans.get(num).getClose())) {

                        if (Helper.isDecimal(_beans.get(num).getNewPrice()) &&
                                Helper.isDecimal(_beans.get(num).getClose())
                                || Helper.isENum(_beans.get(num).getNewPrice())
                                || Helper.isENum(_beans.get(num).getClose())) {
                            double zd = Double.parseDouble(_beans.get(num).getNewPrice()) - Double.parseDouble(_beans.get(num).getClose());
                            double zdf = zd / Double.parseDouble(_beans.get(num).getClose());
                            textViews[i - 1].setText(mFormat1.format(zd));
                            textViews[i].setText(mFormat2.format(zdf));

                            if (zd > 0f) {
                                colors = 101;
                            } else if (zd == 0f) {
                                colors = 102;
                            } else if (zd < 0f) {
                                colors = 100;
                            }

                            textViews[i - 2].setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                            textViews[i - 1].setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                            textViews[i].setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));

                            if (colors == 101) {
                                rl_top[num].setBackgroundResource(R.drawable.shape_item_red);
//                                rls[num].setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                            } else if (colors == 100) {
                                rl_top[num].setBackgroundResource(R.drawable.shape_item_green);
//                                rls[num].setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));

                            } else if (colors == 102) {
                                rl_top[num].setBackgroundResource(R.drawable.shape_item_gray);
//                                rls[num].setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                            }


                        } else {
                            textViews[i - 1].setText("--");
                            textViews[i].setText("--");
//                            rls[num].setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                        }


                    } else {
                        textViews[i - 1].setText("--");
                        textViews[i].setText("--");
//                        rls[num].setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    }

                    num++;
                }
            }
        }
    }

    private void initMiddleView(ViewHodler3 viewHodler3, int position) {


        final TextView[] textViews = {viewHodler3.plantName1, viewHodler3.plantRatio1, viewHodler3.stockName1, viewHodler3.stockZd1, viewHodler3.stockZdf1,
                viewHodler3.plantName2, viewHodler3.plantRatio2, viewHodler3.stockName2, viewHodler3.stockZd2, viewHodler3.stockZdf2,
                viewHodler3.plantName3, viewHodler3.plantRatio3, viewHodler3.stockName3, viewHodler3.stockZd3, viewHodler3.stockZdf3,
                viewHodler3.plantName4, viewHodler3.plantRatio4, viewHodler3.stockName4, viewHodler3.stockZd4, viewHodler3.stockZdf4,
                viewHodler3.plantName5, viewHodler3.plantRatio5, viewHodler3.stockName5, viewHodler3.stockZd5, viewHodler3.stockZdf5,
                viewHodler3.plantName6, viewHodler3.plantRatio6, viewHodler3.stockName6, viewHodler3.stockZd6, viewHodler3.stockZdf6};

        List<StockInfoEntity> _beans = mBeans.get(position).getSubDatas();

        viewHodler3.plantLayout1.setOnClickListener(new LayoutClick(_beans, position));
        viewHodler3.plantLayout2.setOnClickListener(new LayoutClick(_beans, position));
        viewHodler3.plantLayout3.setOnClickListener(new LayoutClick(_beans, position));
        viewHodler3.plantLayout4.setOnClickListener(new LayoutClick(_beans, position));
        viewHodler3.plantLayout5.setOnClickListener(new LayoutClick(_beans, position));
        viewHodler3.plantLayout6.setOnClickListener(new LayoutClick(_beans, position));

        if (null != _beans && _beans.size() > 0) {
            int num = 0;
            for (int i = 0; i < textViews.length; i++) {
                if ((i + 1) % 5 == 0 && _beans.get(num) != null) {

                    int colors = 100;// 100是绿色， 101是红色  102 是黑色
                    if (!TextUtils.isEmpty(_beans.get(num).getIndustryName())) {
                        textViews[i - 4].setText(_beans.get(num).getIndustryName());
                    } else {
                        textViews[i - 4].setText("--");
                    }

                    if (!TextUtils.isEmpty(_beans.get(num).getIndustryUpAndDown())) {

                        if (Helper.isDecimal(_beans.get(num).getIndustryUpAndDown())
                                || Helper.isENum(_beans.get(num).getIndustryUpAndDown())) {
                            double industryUpAndDown = Double.parseDouble(_beans.get(num).getIndustryUpAndDown());
                            textViews[i - 3].setText(mFormat2.format(industryUpAndDown));

                            if (industryUpAndDown > 0f) {
                                colors = 101;
                            } else if (industryUpAndDown == 0f) {
                                colors = 102;
                            } else if (industryUpAndDown < 0f) {
                                colors = 100;
                            }

                        } else {
                            textViews[i - 3].setText(_beans.get(num).getIndustryUpAndDown());
                            colors = 102;
                        }


                    } else {
                        textViews[i - 3].setText("--");
                        colors = 102;
                    }

                    if (!TextUtils.isEmpty(_beans.get(num).getStockName())) {
                        textViews[i - 2].setText(_beans.get(num).getStockName());
                    } else {
                        textViews[i - 2].setText("--");
                    }

                    if (!TextUtils.isEmpty(_beans.get(num).getNewPrice())) {

                        if (Helper.isDecimal(_beans.get(num).getNewPrice())
                                || Helper.isENum(_beans.get(num).getNewPrice())) {

                            if (!TextUtils.isEmpty(_beans.get(num).getStockNumber()) && _beans.get(num).getStockNumber().length() > 2) {
                                textViews[i - 1].setText(TransitionUtils.fundPirce(_beans.get(num).getStockNumber(), _beans.get(num).getNewPrice()));
                            } else {
                                double newPrice = Double.parseDouble(_beans.get(num).getNewPrice());
                                textViews[i - 1].setText(mFormat1.format(newPrice));
                            }

                        } else {
                            textViews[i - 1].setText(_beans.get(num).getNewPrice());
                        }

                    } else {
                        textViews[i - 1].setText("--");
                    }

                    if (!TextUtils.isEmpty(_beans.get(num).getNewPrice())) {

//                        if (Helper.isDecimal(_beans.get(num).getPriceChangeRatio())) {
                        double zdf = _beans.get(num).getPriceChangeRatio();

                        textViews[i].setText(mFormat2.format(zdf));
//                        } else {
//                            textViews[i].setText(_beans.get(num).getPriceChangeRatio());
//                        }
                    } else {
                        textViews[i].setText("--");
                    }

                    if (colors == 101) {
                        textViews[i - 3].setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (colors == 100) {
                        textViews[i - 3].setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    } else if (colors == 102) {
                        textViews[i - 3].setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    }

                    num++;
                }
            }
        }
    }

    private class LayoutClick implements View.OnClickListener {

        private int mPosition;
        private List<StockInfoEntity> mEntitys;

        public LayoutClick(List<StockInfoEntity> _beans, int position) {
            mEntitys = _beans;
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();

            StockDetailEntity stockDetailEntity = new StockDetailEntity();
            switch (v.getId()) {
                case R.id.shangzheng:
                    stockDetailEntity.setStockName(mEntitys.get(0).getStockName());
                    stockDetailEntity.setStockCode(mEntitys.get(0).getStockNumber());
                    intent.putExtra("stockIntent", stockDetailEntity);
                    intent.setClass(mActivity, StockDetailActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.shengzheng:
                    stockDetailEntity.setStockName(mEntitys.get(1).getStockName());
                    stockDetailEntity.setStockCode(mEntitys.get(1).getStockNumber());
                    intent.putExtra("stockIntent", stockDetailEntity);
                    intent.setClass(mActivity, StockDetailActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.changye:
                    stockDetailEntity.setStockName(mEntitys.get(2).getStockName());
                    stockDetailEntity.setStockCode(mEntitys.get(2).getStockNumber());
                    intent.putExtra("stockIntent", stockDetailEntity);
                    intent.setClass(mActivity, StockDetailActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.newStock_gv4:
                case R.id.newStock_gv5:
                case R.id.newStock_gv6:
                case R.id.newStock_gv7:
                case R.id.newStock_gv8:
                case R.id.newStock_gv9:

                    if (mEntitys == null || mEntitys.size() <= 0) {
                        return;
                    }
                    StockListIntent data = new StockListIntent();
                    Integer[] ids = {R.id.newStock_gv4, R.id.newStock_gv5, R.id.newStock_gv6, R.id.newStock_gv7, R.id.newStock_gv8, R.id.newStock_gv9};

                    setIndustry(data, intent, ids, v.getId());

                    data.setHead2("现价");
                    data.setHead3("涨跌幅");
                    data.setHead1("股票名称");
                    intent.putExtra("market", "0");
                    intent.putExtra("type", "1");
                    intent.putExtra("tag", "lingzhang");
                    intent.putExtra("isIndustryStockList", true);
                    intent.putExtra("stockIntent", data);

                    intent.setClass(mActivity, StockListActivity.class);
                    mActivity.startActivity(intent);

                    break;
            }
        }

        private void setIndustry(StockListIntent data, Intent intent, Integer[] ids, int id) {

            for (int i = 0; i < ids.length; i++) {
                if (ids[i] == id) {
                    if (!TextUtils.isEmpty(mEntitys.get(i).getIndustryName())) {
                        data.setTitle(mEntitys.get(i).getIndustryName());
                    }

                    if (!TextUtils.isEmpty(mEntitys.get(i).getIndustryNumber())) {
                        intent.putExtra("code", mEntitys.get(i).getIndustryNumber());
                    }
                }
            }

        }
    }

}
