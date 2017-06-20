package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.PanguParameters;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/8/26.
 * 多银行余额查询Adapter
 */
public class BalanceQueryAdapter extends BaseAdapter {

    private ArrayList<BankAccountEntity> mDatas;
    private BalanceQueryListener mListener;
    private boolean mShowPrice;

    public BalanceQueryAdapter(BalanceQueryListener listener, boolean showPrice) {
        mListener = listener;
        mShowPrice  = showPrice;
    }

    public void setDatas(ArrayList<BankAccountEntity> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mDatas != null && mDatas.size() > 0) {
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHodler viewHodler = null;

        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_balance_query, null);

            viewHodler.loglIv = (ImageView) convertView.findViewById(R.id.balance_bankLoag);
            viewHodler.background = (RelativeLayout) convertView.findViewById(R.id.bankBg);
            viewHodler.balanceAccountTv = (TextView) convertView.findViewById(R.id.balanceAccountTv);
            viewHodler.balanceBankNameTv = (TextView) convertView.findViewById(R.id.balanceBankNameTv);
            viewHodler.balance_currencyTv = (TextView) convertView.findViewById(R.id.balance_currency);
            viewHodler.banlance_AllotqueryTv = (TextView) convertView.findViewById(R.id.banlance_Allotquery);

            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.loglIv.setImageDrawable(null);
        viewHodler.background.setBackgroundResource(0);
        viewHodler.balanceAccountTv.setText(null);
        viewHodler.balanceBankNameTv.setText(null);
        viewHodler.balance_currencyTv.setText(null);
        viewHodler.banlance_AllotqueryTv.setText(null);

        if (!TextUtils.isEmpty(mDatas.get(position).getBANK_NO())) {
            String no = mDatas.get(position).getBANK_NO();

            viewHodler.loglIv.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), PanguParameters.getBankBgLogo().get(no)));
            viewHodler.background.setBackgroundResource(PanguParameters.getBankBackground().get(no));
        }


        if ("1".equals(mDatas.get(position).getMAIN_FLAG())) {
            viewHodler.balanceAccountTv.setText("主资金账号\u3000(" + mDatas.get(position).getFUND_ACCOUNT() + ")");
        } else {
            viewHodler.balanceAccountTv.setText("辅资金账号\u3000(" + mDatas.get(position).getFUND_ACCOUNT() + ")");
        }
//-

        String account = "";
        if (!TextUtils.isEmpty(mDatas.get(position).getBANK_ACCOUNT())) {
            account = Helper.getBanksAccountNumber(mDatas.get(position).getBANK_ACCOUNT());
        }
        viewHodler.balanceBankNameTv.setText(mDatas.get(position).getBANK_NAME() + "(" + account + ")");


        viewHodler.balance_currencyTv.setText("币种\u3000(" + "人民币" + ")");

        if (mShowPrice) {
            if (!TextUtils.isEmpty(mDatas.get(position).getFETCH_BALANCE())) {
                viewHodler.banlance_AllotqueryTv.setBackgroundResource(0);
                viewHodler.banlance_AllotqueryTv.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                viewHodler.banlance_AllotqueryTv.setText("" + mDatas.get(position).getFETCH_BALANCE());
                viewHodler.banlance_AllotqueryTv.setClickable(false);
                viewHodler.banlance_AllotqueryTv.setFocusable(false);
            }
        } else {
            viewHodler.banlance_AllotqueryTv.setText("查询余额");

            if (!TextUtils.isEmpty(mDatas.get(position).getQueryType())) {
                if ("2".equals(mDatas.get(position).getQueryType())) {
                    viewHodler.banlance_AllotqueryTv.setBackgroundDrawable(null);
                    viewHodler.banlance_AllotqueryTv.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.lineColor));
                    viewHodler.banlance_AllotqueryTv.setText("不支持余额查询");
                    viewHodler.banlance_AllotqueryTv.setClickable(false);
                    viewHodler.banlance_AllotqueryTv.setFocusable(false);
                }
            }
        }

        final TextView tv = viewHodler.banlance_AllotqueryTv;
        viewHodler.banlance_AllotqueryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v, tv, mDatas.get(position).getBANK_NO(), mDatas.get(position).getFUND_ACCOUNT(), mDatas.get(position).getQueryType());
                }
            }
        });


        return convertView;
    }

    private class ViewHodler {

        ImageView loglIv;
        RelativeLayout background;
        TextView balanceAccountTv;
        TextView balanceBankNameTv;
        TextView balance_currencyTv;
        TextView banlance_AllotqueryTv;
    }

    public interface BalanceQueryListener {
        void onClick(View view, TextView textView, String bankNo, String account, String queryType);
    }
}
