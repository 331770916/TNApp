package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
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
import java.util.List;

/**
 * Created by ltyhome on 26/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:  entrusted deal listview adapter
 */

public class FJEntrustedDealListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private List<StructuredFundEntity> mList;
    private StructuredFundEntity bean;
    private ScallCallback callback;
    private Context mContext;
    private int type,height;

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
            viewHodler.llContent =  (LinearLayout) convertView.findViewById(R.id.fj_content);
            viewHodler.tableSpan = (TableLayout) convertView.findViewById(R.id.fj_tablespan);
            height = viewHodler.tableSpan.getHeight();
            viewHodler.rlSpan = (RelativeLayout)convertView.findViewById(R.id.fj_goneimage);
            viewHodler.tvGone1 = (TextView)convertView.findViewById(R.id.fj_gonetvcontent1);
            viewHodler.tvGone2 = (TextView)convertView.findViewById(R.id.fj_gonetvcontent2);
            viewHodler.tvGone3 = (TextView)convertView.findViewById(R.id.fj_gonetvcontent3);
            viewHodler.tvGone4 = (TextView)convertView.findViewById(R.id.fj_gonetvcontent4);
            viewHodler.tableGone = (TableLayout) convertView.findViewById(R.id.fj_tablegone);
            switch (type){
                case 0:
                    viewHodler.tableGone.setVisibility(View.GONE);
                    ((TextView)convertView.findViewById(R.id.fj_gonetv1)).setText("委托编号：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv2)).setText("委托金额：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv3)).setText("委托数量：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv4)).setText("成交数量：");
                    break;
                case 1:
                    viewHodler.tableGone.setVisibility(View.VISIBLE);
                    ((TextView)convertView.findViewById(R.id.fj_gonetv1)).setText("委托数量：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv2)).setText("成交金额：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv4)).setText("委托金额：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv3)).setText("发生日期：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv5)).setText("成交数量：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv6)).setText("流水号：");
                    ((TextView)convertView.findViewById(R.id.fj_gonetv7)).setText("成交价格：");
                    viewHodler.tvGone5 = (TextView)convertView.findViewById(R.id.fj_gonetvcontent5);
                    viewHodler.tvGone6 = (TextView)convertView.findViewById(R.id.fj_gonetvcontent6);
                    viewHodler.tvGone7 = (TextView)convertView.findViewById(R.id.fj_gonetvcontent7);
                    break;
            }
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (FJEntrustedDealListViewAdapter.ViewHodler) convertView.getTag();
        }
        bean = mList.get(position);
        viewHodler.llContent.setOnClickListener(this);
        viewHodler.llContent.setTag(bean);
        viewHodler.rlSpan.setOnClickListener(this);
        viewHodler.rlSpan.setTag(bean);
        viewHodler.tvName.setText(bean.getStoken_name());
        viewHodler.tvCode.setText(bean.getStocken_code());
        viewHodler.tvBuss.setText(bean.getBusiness_name());
        viewHodler.tvStatus.setText(bean.getEntrust_status());
        viewHodler.tvTime.setText(bean.getReport_time());
        viewHodler.tvDate.setText(bean.getCurr_date());
        if (bean.isShowRule()) {
            if (viewHodler.llContent.isSelected()) {
                viewHodler.llContent.setSelected(false);
                viewHodler.tableSpan.setVisibility(View.GONE);
            } else {
                viewHodler.llContent.setSelected(true);
                viewHodler.tableSpan.setVisibility(View.VISIBLE);
                switch (type){
                    case 0:
                        viewHodler.tvGone1.setText(bean.getEntrust_no());
                        viewHodler.tvGone2.setText(bean.getEntrust_balance());
                        viewHodler.tvGone3.setText(bean.getEntrust_amount());
                        viewHodler.tvGone4.setText(bean.getBusiness_amount());
                        break;
                    case 1:
                        viewHodler.tvGone1.setText(bean.getEntrust_amount());
                        viewHodler.tvGone2.setText("10000");
                        viewHodler.tvGone3.setText(bean.getEntrust_balance());
                        viewHodler.tvGone4.setText(bean.getInit_date());
                        viewHodler.tvGone5.setText(bean.getBusiness_amount());
                        viewHodler.tvGone6.setText(bean.getSerial_no());
                        viewHodler.tvGone7.setText("8.88");
                        break;
                }
            }
        } else {
            viewHodler.llContent.setSelected(false);
            viewHodler.tableSpan.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        bean = (StructuredFundEntity)v.getTag();
        if(bean.isShowRule())
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
        TextView tvName,tvCode,tvBuss,tvStatus,tvTime,tvDate,tvGone1,tvGone2,tvGone3,tvGone4,tvGone5,tvGone6,tvGone7;
        TableLayout tableSpan,tableGone;
        RelativeLayout rlSpan;
    }
}
