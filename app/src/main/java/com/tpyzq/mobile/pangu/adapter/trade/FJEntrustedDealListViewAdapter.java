package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;

/**
 * Created by ltyhome on 26/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:  entrusted deal listview adapter
 */

public class FJEntrustedDealListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private List<StructuredFundEntity> mList;
    private StructuredFundEntity bean;
    private ScallCallback callback;
    private Context mContext;
    private int type;

    public FJEntrustedDealListViewAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
    }

    public void setCallback(ScallCallback callback) {
        this.callback = callback;
    }

    public void setData(List<StructuredFundEntity> setText) {
        mList = setText;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FJEntrustedDealListViewAdapter.ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fj_item, null);
            viewHodler.tvName = (TextView) convertView.findViewById(R.id.fjtv_name);
            viewHodler.tvCode = (TextView) convertView.findViewById(R.id.fjtv_code);
            viewHodler.tvBuss = (TextView) convertView.findViewById(R.id.fjtv_business);
            viewHodler.tvStatus = (TextView) convertView.findViewById(R.id.fjtv_status);
            viewHodler.tvTime = (TextView) convertView.findViewById(R.id.fjtv_time);
            viewHodler.tvDate = (TextView) convertView.findViewById(R.id.fjtv_date);
            viewHodler.llContent = (LinearLayout) convertView.findViewById(R.id.fj_content);
            viewHodler.tableSpan = (TableLayout) convertView.findViewById(R.id.fj_tablespan);
            viewHodler.rlSpan = (RelativeLayout) convertView.findViewById(R.id.fj_goneimage);
            viewHodler.tvGone1 = (TextView) convertView.findViewById(R.id.fj_gonetvcontent1);
            viewHodler.tvGone2 = (TextView) convertView.findViewById(R.id.fj_gonetvcontent2);
            viewHodler.tvGone3 = (TextView) convertView.findViewById(R.id.fj_gonetvcontent3);
            viewHodler.tvGone4 = (TextView) convertView.findViewById(R.id.fj_gonetvcontent4);
            viewHodler.tableGone = (TableLayout) convertView.findViewById(R.id.fj_tablegone);
            switch (type) {
                case 0:
                    viewHodler.tableGone.setVisibility(View.GONE);
                    ((TextView) convertView.findViewById(R.id.fj_gonetv1)).setText("委托编号：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv2)).setText("委托金额：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv3)).setText("委托数量：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv4)).setText("成交数量：");
                    break;
                case 1:
                    viewHodler.tableGone.setVisibility(View.VISIBLE);
                    ((TextView) convertView.findViewById(R.id.fj_gonetv1)).setText("委托数量：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv2)).setText("成交金额：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv3)).setText("委托金额：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv4)).setText("发生日期：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv5)).setText("成交数量：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv6)).setText("流水号：");
                    ((TextView) convertView.findViewById(R.id.fj_gonetv7)).setText("成交价格：");
                    viewHodler.tvGone5 = (TextView) convertView.findViewById(R.id.fj_gonetvcontent5);
                    viewHodler.tvGone6 = (TextView) convertView.findViewById(R.id.fj_gonetvcontent6);
                    viewHodler.tvGone7 = (TextView) convertView.findViewById(R.id.fj_gonetvcontent7);
                    break;
            }
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        bean = mList.get(position);
        viewHodler.llContent.setOnClickListener(this);
        viewHodler.llContent.setTag(bean);
        viewHodler.rlSpan.setOnClickListener(this);
        viewHodler.rlSpan.setTag(bean);
        viewHodler.tvName.setText(bean.getStoken_name());
        viewHodler.tvCode.setText(bean.getStocken_code());
        viewHodler.tvBuss.setText(bean.getBusiness_name());
        String entrust_bs = bean.getEntrust_status();
        switch (entrust_bs) {
            case "0":
                viewHodler.tvStatus.setText("未报");
                break;
            case "1":
                viewHodler.tvStatus.setText("待报");
                break;
            case "2":
                viewHodler.tvStatus.setText("已报");
                break;
            case "3":
                viewHodler.tvStatus.setText("已报待撤");
                break;
            case "4":
                viewHodler.tvStatus.setText("部成待撤");
                break;
            case "5":
                viewHodler.tvStatus.setText("部撤");
                break;
            case "6":
                viewHodler.tvStatus.setText("已撤");
                break;
            case "7":
                viewHodler.tvStatus.setText("部成");
                break;
            case "8":
                viewHodler.tvStatus.setText("已成");
                break;
            case "9":
                viewHodler.tvStatus.setText("废单");
                break;
            case "A":
                viewHodler.tvStatus.setText("已报待改(港股)");
                break;
            case "B":
                viewHodler.tvStatus.setText("预埋单撤单(港股)");
                break;
            case "C":
                viewHodler.tvStatus.setText("正报");
                break;
            case "D":
                viewHodler.tvStatus.setText("撤废");
                break;
            case "E":
                viewHodler.tvStatus.setText("部成待改(港股)");
                break;
            case "F":
                viewHodler.tvStatus.setText("预埋单废单(港股)");
                break;
            case "G":
                viewHodler.tvStatus.setText("单腿成交");
                break;
            case "H":
                viewHodler.tvStatus.setText("待审核(港股)");
                break;
            case "J":
                viewHodler.tvStatus.setText("复核未通过(港股)");
                break;
            case "M":
                viewHodler.tvStatus.setText("Wait for Confirming");
                break;
            case "U":
                viewHodler.tvStatus.setText("已确认待撤");
                break;
            case "V":
                viewHodler.tvStatus.setText("已确认");
                break;
            case "W":
                viewHodler.tvStatus.setText("待确认");
                break;
            case "X":
                viewHodler.tvStatus.setText("预成交");
                break;
            case "Y":
                viewHodler.tvStatus.setText("购回待确认");
                break;
            case "Z":
                viewHodler.tvStatus.setText("已购回");
                break;
            default:
                viewHodler.tvStatus.setText("--");
                break;
        }


        viewHodler.tvTime.setText(Helper.getMyDateHMS(bean.getReport_time()));
        viewHodler.tvDate.setText(Helper.formateDate1(bean.getCurr_date()));
        if (bean.isShowRule()) {
            if (viewHodler.llContent.isSelected()) {
                bean.setShowRule(false);
                viewHodler.llContent.setSelected(false);
                viewHodler.tableSpan.setVisibility(View.GONE);
            } else {
                bean.setShowRule(true);
                viewHodler.llContent.setSelected(true);
                viewHodler.tableSpan.setVisibility(View.VISIBLE);
                switch (type) {
                    case 0:
                        viewHodler.tvGone1.setText(bean.getEntrust_no());
                        viewHodler.tvGone2.setText(bean.getEntrust_balance());
                        viewHodler.tvGone3.setText(bean.getEntrust_amount());
                        viewHodler.tvGone4.setText(bean.getBusiness_amount());
                        break;
                    case 1:
                        viewHodler.tvGone1.setText(bean.getEntrust_amount());
                        viewHodler.tvGone2.setText(bean.getMatched_amt());
                        viewHodler.tvGone3.setText(bean.getEntrust_balance());
                        viewHodler.tvGone4.setText(bean.getInit_date());
                        viewHodler.tvGone5.setText(bean.getBusiness_amount());
                        viewHodler.tvGone6.setText(bean.getSerial_no());
                        viewHodler.tvGone7.setText(bean.getMatched_price());
                        break;
                }
            }
        } else {
            bean.setShowRule(false);
            viewHodler.llContent.setSelected(false);
            viewHodler.tableSpan.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        bean = (StructuredFundEntity) v.getTag();
        if (bean.isShowRule())
            bean.setShowRule(false);
        else
            bean.setShowRule(true);
        notifyDataSetChanged();
        callback.callScall();
    }

    public interface ScallCallback {
        void callScall();
    }

    class ViewHodler {
        LinearLayout llContent;
        TextView tvName, tvCode, tvBuss, tvStatus, tvTime, tvDate, tvGone1, tvGone2, tvGone3, tvGone4, tvGone5, tvGone6, tvGone7;
        TableLayout tableSpan, tableGone;
        RelativeLayout rlSpan;
    }
}
