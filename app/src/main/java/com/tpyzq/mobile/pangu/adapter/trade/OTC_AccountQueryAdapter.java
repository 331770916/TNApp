package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者：刘泽鹏 on 2016/9/2 16:50
 * OTC 账户查询界面的适配器
 */
public class OTC_AccountQueryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> list;

    public OTC_AccountQueryAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<HashMap<String,String>> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list !=null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list !=null && list.size()>0){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_otc_account_query,null);
            viewHolder.tvOTC_OpenDate = (TextView) convertView.findViewById(R.id.tvOTC_OpenDate);
            viewHolder.tvOTC_Account = (TextView) convertView.findViewById(R.id.tvOTC_Account);
            viewHolder.tvOTC_company = (TextView) convertView.findViewById(R.id.tvOTC_company);
            viewHolder.tvOTC_AccountState = (TextView) convertView.findViewById(R.id.tvOTC_AccountState);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        viewHolder.tvOTC_OpenDate.setText(map.get("open_date"));
        viewHolder.tvOTC_Account.setText(map.get("secum_account"));
        viewHolder.tvOTC_company.setText(map.get("prodta_no"));
        viewHolder.tvOTC_AccountState.setText(map.get("prodholder_status"));

        return convertView;
    }

    class ViewHolder{
        TextView tvOTC_OpenDate;
        TextView tvOTC_Account;
        TextView tvOTC_company;
        TextView tvOTC_AccountState;
    }
}
