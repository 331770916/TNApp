package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.FundHistoryThreeEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/10/24.
 * 开放式基金今日委托
 */
public class FundHistoryAdapter extends BaseAdapter {
    private List<FundHistoryThreeEntity> fundHistoryThreeBeen;
    private int tempPosition;
    private Context context;


    public FundHistoryAdapter(Context context) {
        this.context = context;
    }


    public void setHisThreeBean(List<FundHistoryThreeEntity> fundHistoryThreeBeen) {
        this.fundHistoryThreeBeen = fundHistoryThreeBeen;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (fundHistoryThreeBeen != null && fundHistoryThreeBeen.size() > 0) {
            return fundHistoryThreeBeen.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_fund_history_all, null);
            viewHolder.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
            viewHolder.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
            viewHolder.tv_text3 = (TextView) convertView.findViewById(R.id.tv_text3);
            viewHolder.tv_text4 = (TextView) convertView.findViewById(R.id.tv_text4);
            viewHolder.tv_text5 = (TextView) convertView.findViewById(R.id.tv_text5);
            viewHolder.tv_text6 = (TextView) convertView.findViewById(R.id.tv_text6);
            viewHolder.tv_text7 = (TextView) convertView.findViewById(R.id.tv_text7);
            viewHolder.tv_text8 = (TextView) convertView.findViewById(R.id.tv_text8);
            viewHolder.tv_fund_company_name = (TextView) convertView.findViewById(R.id.tv_fund_company_name);
            viewHolder.tv_fund_company_code = (TextView) convertView.findViewById(R.id.tv_fund_company_code);
            viewHolder.tv_fund_poundage = (TextView) convertView.findViewById(R.id.tv_fund_poundage);
            viewHolder.tv_tax = (TextView) convertView.findViewById(R.id.tv_tax);
            viewHolder.tv_other_money = (TextView) convertView.findViewById(R.id.tv_other_money);
            viewHolder.ib_contract = (ImageButton) convertView.findViewById(R.id.ib_contract);
            viewHolder.ll_view1 = (LinearLayout) convertView.findViewById(R.id.ll_view1);
            viewHolder.ll_view2 = (LinearLayout) convertView.findViewById(R.id.ll_view2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_text1.setText(fundHistoryThreeBeen.get(position).FUND_NAME);
        viewHolder.tv_text2.setText(fundHistoryThreeBeen.get(position).FUND_CODE);
        viewHolder.tv_text3.setText(Helper.getMyDateY_M_D(fundHistoryThreeBeen.get(position).ALLOT_DATE));
        viewHolder.tv_text4.setText(Helper.getMyDateHMS(fundHistoryThreeBeen.get(position).TIME));
        viewHolder.tv_text5.setText(fundHistoryThreeBeen.get(position).BUSINESS_NAME);
        viewHolder.tv_text6.setVisibility(View.GONE);
        viewHolder.tv_text7.setText(fundHistoryThreeBeen.get(position).BALANCE);
        viewHolder.tv_text8.setText(fundHistoryThreeBeen.get(position).SHARES);
        viewHolder.tv_fund_company_name.setText(fundHistoryThreeBeen.get(position).FUND_COMPANY_NAME);
        viewHolder.tv_fund_company_code.setText(fundHistoryThreeBeen.get(position).FUND_COMPANY);
        viewHolder.tv_fund_poundage.setText(fundHistoryThreeBeen.get(position).FARE_SX);
        viewHolder.tv_tax.setText(fundHistoryThreeBeen.get(position).STAMP_TAX);
        viewHolder.tv_other_money.setText(fundHistoryThreeBeen.get(position).OTHER_FARE);



        if (!fundHistoryThreeBeen.get(position).unfold) {
            viewHolder.ll_view2.setVisibility(View.GONE);
        } else {
            viewHolder.ll_view2.setVisibility(View.VISIBLE);
        }

        viewHolder.ll_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ll_view2.setVisibility(View.VISIBLE);
                fundHistoryThreeBeen.get(tempPosition).unfold = false;
                fundHistoryThreeBeen.get(position).unfold = true;
                tempPosition = position;
                notifyDataSetChanged();
            }
        });

        viewHolder.ib_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ll_view2.setVisibility(View.GONE);
                fundHistoryThreeBeen.get(position).unfold = false;
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_text1;
        TextView tv_text2;
        TextView tv_text3;
        TextView tv_text4;
        TextView tv_text5;
        TextView tv_text6;
        TextView tv_text7;
        TextView tv_text8;
        TextView tv_fund_company_name;
        TextView tv_fund_company_code;
        TextView tv_fund_poundage;
        TextView tv_tax;
        TextView tv_other_money;
        ImageButton ib_contract;
        LinearLayout ll_view1;
        LinearLayout ll_view2;
    }
}
