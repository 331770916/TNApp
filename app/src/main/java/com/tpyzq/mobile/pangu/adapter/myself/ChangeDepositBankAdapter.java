package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.PanguParameters;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwnebo on 2017/7/4.
 * 三存银行变更
 */

public class ChangeDepositBankAdapter extends BaseAdapter {

    private List<Map<String, String>> mBankInfos;
    private ClickListener mClickListener;
    private Context mContext;
    public ChangeDepositBankAdapter(Context context){
        mContext = context;
    }

    public void setDatas(List<Map<String, String>> bankInfos) {
        mBankInfos = bankInfos;
        notifyDataSetChanged();
    }

    public void setClickListener (ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public int getCount() {
        if (mBankInfos != null && mBankInfos.size() > 0) {
            return mBankInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mBankInfos != null && mBankInfos.size() > 0) {
            return mBankInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_change_depositbank, null);
        }

        RelativeLayout layout =(RelativeLayout) convertView.findViewById(R.id.ll_depositbankItem);
        TextView account = (TextView) convertView.findViewById(R.id.account);
        ImageView stautIv = (ImageView) convertView.findViewById(R.id.state);
        TextView stautTv = (TextView) convertView.findViewById(R.id.state_tv);
        convertView.findViewById(R.id.change).setOnClickListener(null);

        account.setText("资金账户 " + "(" + UserUtil.capitalAccount + ")");



        String status_name = mBankInfos.get(position).get("STATUS_NAME");
        if (!TextUtils.isEmpty(status_name)) {
            if ("正常".equals(status_name)) {
                stautIv.setImageResource(R.mipmap.normal);
                stautTv.setText(status_name);
            } else {
                stautIv.setImageResource(R.mipmap.abnormal);
                stautTv.setText(status_name);
            }
        }

        String bank_no = mBankInfos.get(position).get("BANK_NO");
        if (!TextUtils.isEmpty(bank_no)) {
            layout.setBackgroundResource(PanguParameters.getBankBackground2().get(bank_no));
        } else {
            int height = Helper.dip2px(mContext, 120);
            layout.setMinimumHeight(height);
        }

        convertView.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.changeBankClick(position);
                }
            }
        });

        return convertView;
    }

    public interface ClickListener {
        public void changeBankClick(int position);
    }
}
