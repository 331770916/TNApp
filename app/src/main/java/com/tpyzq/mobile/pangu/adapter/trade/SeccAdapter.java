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
import com.tpyzq.mobile.pangu.data.RevokeEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 33920_000 on 2017/8/14.
 */

public class SeccAdapter extends BaseAdapter {
    Context mContext;
    private String secc_code;
    private String MARKET_NAME;

    public SeccAdapter(Context context,String secc_code, String MARKET_NAME) {
        mContext = context;
        this.secc_code = secc_code;
        this.MARKET_NAME = MARKET_NAME;
    }

    public void setData(String secc_code, String MARKET_NAME) {
        this.secc_code= secc_code;
        this.MARKET_NAME= MARKET_NAME;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (ConstantUtil.stock_account_list != null && ConstantUtil.stock_account_list.size() > 0) {
            return ConstantUtil.stock_account_list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (ConstantUtil.stock_account_list != null && ConstantUtil.stock_account_list.size() > 0) {
            return ConstantUtil.stock_account_list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_secc, null);
            viewHodler = new ViewHodler();
            viewHodler.tv_secc = (TextView) convertView.findViewById(R.id.tv_secc);
            viewHodler.iv_select_status = (ImageView) convertView.findViewById(R.id.iv_select_status);
            viewHodler.ll_secc = (LinearLayout) convertView.findViewById(R.id.ll_secc);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        HashMap<String,String> hashMap = ConstantUtil.stock_account_list.get(position);
        viewHodler.tv_secc.setText(hashMap.get("MARKET_NAME")+" "+hashMap.get("SECU_ACCOUNT"));
        if (secc_code.equalsIgnoreCase(hashMap.get("SECU_ACCOUNT"))&&MARKET_NAME.equalsIgnoreCase(hashMap.get("MARKET_NAME"))) {
            viewHodler.iv_select_status.setVisibility(View.VISIBLE);
            viewHodler.ll_secc.setBackgroundResource(R.drawable.bg_secc_select);
        } else {
            viewHodler.iv_select_status.setVisibility(View.GONE);
            viewHodler.ll_secc.setBackgroundResource(R.drawable.bg_secc);
        }
        return convertView;
    }

    class ViewHodler {
        TextView tv_secc;
        ImageView iv_select_status;
        LinearLayout ll_secc;
    }
}
