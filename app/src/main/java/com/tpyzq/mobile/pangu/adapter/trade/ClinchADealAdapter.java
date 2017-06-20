package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.ClinchDealEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wangqi on 2016/8/16.
 * 成交 查詢  Adapter
 */
public class ClinchADealAdapter extends BaseAdapter {
    private Context mContext;
    private List<ClinchDealEntity> mSetText;

    public ClinchADealAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ClinchDealEntity> setText) {
        mSetText = setText;
    }

    @Override
    public int getCount() {
        if (mSetText != null && mSetText.size() > 0) {
            return mSetText.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mSetText != null && mSetText.size() > 0) {

            return mSetText.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.clinchtoday_item, null);
            viewHodler.takeapositionItem1 = (TextView) convertView.findViewById(R.id.clinch_Name);
            viewHodler.takeapositionItem2 = (TextView) convertView.findViewById(R.id.clinch_Num);
            viewHodler.takeapositionItem3 = (TextView) convertView.findViewById(R.id.clinch_Date);
            viewHodler.takeapositionItem4 = (TextView) convertView.findViewById(R.id.clinch_Time);
            viewHodler.takeapositionItem5 = (TextView) convertView.findViewById(R.id.clinch_Entrust);
            viewHodler.takeapositionItem6 = (TextView) convertView.findViewById(R.id.clinch_Succeed);
            viewHodler.takeapositionItem7 = (TextView) convertView.findViewById(R.id.clinch_Transaction);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        DecimalFormat mFormat1 = new DecimalFormat("#0.000");
        viewHodler.takeapositionItem1.setText(mSetText.get(position).getName());
        viewHodler.takeapositionItem2.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getNum())));
        viewHodler.takeapositionItem3.setText(Helper.getMyDateY_M_D(mSetText.get(position).getDate()));
        if (!"0".equals(mSetText.get(position).getTime())) {
            viewHodler.takeapositionItem4.setText(Helper.formateDate(mSetText.get(position).getTime()));
        }
        switch (mSetText.get(position).getTransaction()) {
            case "1":
                viewHodler.takeapositionItem5.setText("买入");
                viewHodler.takeapositionItem5.setTextColor(Color.parseColor("#fa5050"));
                break;
            case "2":
                viewHodler.takeapositionItem5.setText("卖出");
                viewHodler.takeapositionItem5.setTextColor(Color.parseColor("#FF50BF6A"));
                break;
            default:
                viewHodler.takeapositionItem5.setText("--");
                break;
        }
        String str = String.valueOf(Double.parseDouble(mSetText.get(position).getAmount()));//浮点变量a转换为字符串str
        int idx = str.lastIndexOf(".");//查找小数点的位置
        String strNum = str.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
        viewHodler.takeapositionItem6.setText(strNum);
        viewHodler.takeapositionItem7.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getMoney())));

        return convertView;
    }

    class ViewHodler {
        TextView takeapositionItem1;
        TextView takeapositionItem2;
        TextView takeapositionItem3;
        TextView takeapositionItem4;
        TextView takeapositionItem5;
        TextView takeapositionItem6;
        TextView takeapositionItem7;
    }
}

