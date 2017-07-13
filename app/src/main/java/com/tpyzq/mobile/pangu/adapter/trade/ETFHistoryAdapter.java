package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;

import java.util.List;

/**
 * Created by wangqi on 2017/7/4.
 * ETF 申赎历史查询
 */

public class ETFHistoryAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<EtfDataEntity> mList;
    private ScallCallback callback;
    private EtfDataEntity bean;

    public ETFHistoryAdapter(Context context) {
        this.context = context;
    }

    public void setCallback(ScallCallback callback) {
        this.callback = callback;
    }

    public void setData(List<EtfDataEntity> setText) {
        this.mList = setText;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_etf_query_his, null);
            viewHodler.llContent = (LinearLayout) convertView.findViewById(R.id.fj_content);
            viewHodler.tvName = (TextView) convertView.findViewById(R.id.tv1);
            viewHodler.tvCode = (TextView) convertView.findViewById(R.id.tv2);
            viewHodler.tvStatus = (TextView) convertView.findViewById(R.id.tv4);
            viewHodler.tvQuantity = (TextView) convertView.findViewById(R.id.tv5);
            viewHodler.tvTime = (TextView) convertView.findViewById(R.id.tv6);
            viewHodler.tvDate = (TextView) convertView.findViewById(R.id.tv7);

            viewHodler.tableSpan = (LinearLayout) convertView.findViewById(R.id.tableSpan);
            viewHodler.rlSpan = (ImageView) convertView.findViewById(R.id.deliveryImageView);
            viewHodler.tvGone1 = (TextView) convertView.findViewById(R.id.ll_tv1);
            viewHodler.tvGone2 = (TextView) convertView.findViewById(R.id.ll_tv2);
            viewHodler.tvGone3 = (TextView) convertView.findViewById(R.id.ll_tv3);
            viewHodler.tvGone4 = (TextView) convertView.findViewById(R.id.ll_tv4);
            viewHodler.tvGone5 = (TextView) convertView.findViewById(R.id.ll_tv5);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        bean = mList.get(position);
        viewHodler.llContent.setOnClickListener(this);
        viewHodler.llContent.setTag(bean);
        viewHodler.rlSpan.setOnClickListener(this);
        viewHodler.rlSpan.setTag(bean);

        viewHodler.tvName.setText(bean.getStock_name());
        viewHodler.tvCode.setText(bean.getStock_code());
//        viewHodler.tvVocationalName.setText("");
        viewHodler.tvStatus.setText(bean.getEntrust_amount());
        viewHodler.tvTime.setText(bean.getInit_date());
        viewHodler.tvDate.setText(bean.getReport_time());
//        viewHodler.tvQuantity.setText(bean.getEntrust_status());
        viewHodler.tvQuantity.setText(bean.getEntrust_status_name());


        if (bean.isShowRule()) {
            if (viewHodler.llContent.isSelected()) {
                bean.setShowRule(false);
                viewHodler.llContent.setSelected(false);
                viewHodler.tableSpan.setVisibility(View.GONE);
            } else {
                bean.setShowRule(true);
                viewHodler.llContent.setSelected(true);
                viewHodler.tableSpan.setVisibility(View.VISIBLE);
                viewHodler.tvGone1.setText(bean.getEntrust_no());
                viewHodler.tvGone2.setText(bean.getPrev_balance());
                viewHodler.tvGone3.setText(bean.getEntrust_prop());
                String entrust_bs = bean.getEntrust_bs();
                if ("1".equals(entrust_bs)) {
                    viewHodler.tvGone4.setText("买");
                } else if ("2".equals(entrust_bs)){
                    viewHodler.tvGone4.setText("卖");
                } else {
                    viewHodler.tvGone4.setText("--");
                }
                viewHodler.tvGone5.setText("--");
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
        bean = (EtfDataEntity) v.getTag();
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
        LinearLayout llContent, tableSpan;
        TextView tvName, tvCode, tvVocationalName, tvStatus, tvQuantity, tvTime, tvDate, tvGone1, tvGone2, tvGone3, tvGone4, tvGone5;
        ImageView rlSpan;
    }
}
