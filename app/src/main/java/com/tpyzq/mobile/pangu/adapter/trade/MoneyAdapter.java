package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.CapitalEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by wangqi on 2016/8/16.
 * 资金流水
 */
public class MoneyAdapter extends BaseAdapter implements View.OnClickListener {
    private int mExpandedMenuPos = -1;
    private Context mContext;
    List<CapitalEntity> mSetText;

    public MoneyAdapter(Context context) {
        mContext = context;

    }

    public void setData(int ExpandedMenuPos) {
        mExpandedMenuPos = ExpandedMenuPos;
    }

    public void setData(List<CapitalEntity> setText) {
        mSetText = setText;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.capitaltoday_item, null);
            viewHodler.takeapositionItem = (LinearLayout) convertView.findViewById(R.id.LinearLayout);  //item
            viewHodler.takeapositionItem0 = (LinearLayout) convertView.findViewById(R.id.capitalItem_LL);
            viewHodler.takeapositionItem1 = (TextView) convertView.findViewById(R.id.clinch_Date);
            viewHodler.takeapositionItem2 = (TextView) convertView.findViewById(R.id.clinch_Name);
            viewHodler.takeapositionItem3 = (TextView) convertView.findViewById(R.id.clinch_Business);
            viewHodler.takeapositionItem4 = (TextView) convertView.findViewById(R.id.clinch_Balance);
            viewHodler.takeapositionItem6 = (ImageView) convertView.findViewById(R.id.ImageView);

            viewHodler.TtemName = (TextView) convertView.findViewById(R.id.Name);
            viewHodler.ItemStockcde = (TextView) convertView.findViewById(R.id.Stockcde);
            viewHodler.TtemSecurityPrice = (TextView) convertView.findViewById(R.id.SecurityPrice);
            viewHodler.ItemHbcjsl = (TextView) convertView.findViewById(R.id.Hbcjsl);
            viewHodler.ItemCurrency = (TextView) convertView.findViewById(R.id.Currency);

            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        DecimalFormat mFormat1 = new DecimalFormat("#0.000");
        viewHodler.takeapositionItem0.setOnClickListener(this);
        viewHodler.takeapositionItem1.setText(Helper.getMyDateY_M_D(mSetText.get(position).getDate()));
        viewHodler.takeapositionItem2.setText(mSetText.get(position).getBusiness());

        viewHodler.takeapositionItem3.setText(mSetText.get(position).getName());
        viewHodler.takeapositionItem4.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getBalance())));

        viewHodler.TtemName.setText(mSetText.get(position).getStockName());
        viewHodler.ItemStockcde.setText(mSetText.get(position).getStockcde());
        viewHodler.TtemSecurityPrice.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getSecurityPrice())));

        if (!TextUtils.isEmpty(mSetText.get(position).getHbcjsl())&&!"0".equals(mSetText.get(position).getHbcjsl()
        )){
            String str = String.valueOf(mSetText.get(position).getHbcjsl());//浮点变量a转换为字符串str
            int idx = str.lastIndexOf(".");//查找小数点的位置
            String strNum = str.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
            viewHodler.ItemHbcjsl.setText(strNum);
        }else {
            viewHodler.ItemHbcjsl.setText(mSetText.get(position).getHbcjsl());
        }


        if (!TextUtils.isEmpty(mSetText.get(position).getCurrency())) {
            switch (mSetText.get(position).getCurrency()) {
                case "0":
                    viewHodler.ItemCurrency.setText("人民币");
                    break;
                case "1":
                    viewHodler.ItemCurrency.setText("美元");
                    break;
                case "2":
                    viewHodler.ItemCurrency.setText("港版");
                    break;
            }
        } else {
            viewHodler.ItemCurrency.setText("--");
        }


        if ("0".equals(mSetText.get(position).getBusiness_type())) {
            viewHodler.takeapositionItem0.setSelected(true);
            viewHodler.takeapositionItem3.setVisibility(View.VISIBLE);
            viewHodler.takeapositionItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedMenuPos = position;//记录点击的position
                    notifyDataSetChanged();
                }
            });
        } else if ("1".equals(mSetText.get(position).getBusiness_type())){
            viewHodler.takeapositionItem0.setSelected(false);
            viewHodler.takeapositionItem3.setVisibility(View.GONE);
        }



        //item 列表显示和隐藏
        if (mExpandedMenuPos == position) {
            if (viewHodler.takeapositionItem.isSelected()) {
                viewHodler.takeapositionItem.setSelected(false);

                viewHodler.takeapositionItem0.setVisibility(View.GONE);
                mExpandedMenuPos = -1;
            } else {
                viewHodler.takeapositionItem.setSelected(true);
                viewHodler.takeapositionItem0.setVisibility(View.VISIBLE);
            }
        } else {
            viewHodler.takeapositionItem.setSelected(false);
            viewHodler.takeapositionItem0.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capitalItem_LL) {
            mExpandedMenuPos = -1;
            notifyDataSetChanged();
        }
    }

    class ViewHodler {
        LinearLayout takeapositionItem;
        LinearLayout takeapositionItem0;
        TextView takeapositionItem1;
        TextView takeapositionItem2;
        TextView takeapositionItem3;
        TextView takeapositionItem4;
        ImageView takeapositionItem6;

        TextView TtemName;
        TextView ItemStockcde;
        TextView TtemSecurityPrice;
        TextView ItemHbcjsl;
        TextView ItemCurrency;
    }
}
