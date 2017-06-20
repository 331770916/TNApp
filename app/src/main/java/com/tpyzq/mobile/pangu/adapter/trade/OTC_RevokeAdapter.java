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
 * 作者：刘泽鹏 on 2016/8/31 10:08
 */
public class OTC_RevokeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> list;

    public OTC_RevokeAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<HashMap<String,String>> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if( list != null && list.size()>0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if( list != null && list.size()>0 ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_otc_revoke,null);
            viewHolder.tvRevokeName= (TextView) convertView.findViewById(R.id.tvRevokeName);
            viewHolder.tvRevokeCode= (TextView) convertView.findViewById(R.id.tvRevokeCode);
            viewHolder.tvRevokeData= (TextView) convertView.findViewById(R.id.tvRevokeData);
            viewHolder.tvRevokeTime= (TextView) convertView.findViewById(R.id.tvRevokeTime);
            viewHolder.tvRevokeMoney= (TextView) convertView.findViewById(R.id.tvRevokeMoney);
            viewHolder.tvRevokeType= (TextView) convertView.findViewById(R.id.tvRevokeType);
            viewHolder.tvRevokeState= (TextView) convertView.findViewById(R.id.tvRevokeState);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        viewHolder.tvRevokeName.setText(map.get("prod_name"));
        viewHolder.tvRevokeCode.setText(map.get("prod_code"));
        viewHolder.tvRevokeData.setText(map.get("entrust_date"));
        viewHolder.tvRevokeTime.setText(map.get("entrust_time"));
        viewHolder.tvRevokeMoney.setText(map.get("entrust_amount"));
        viewHolder.tvRevokeState.setText(map.get("entrust_status_name"));
        viewHolder.tvRevokeType.setText(map.get("business_flag"));
        return convertView;
    }

    class ViewHolder{
        TextView tvRevokeName;      //产品名称
        TextView tvRevokeCode;      //产品代码
        TextView tvRevokeData;      //日期
        TextView tvRevokeTime;      //时间
        TextView tvRevokeMoney;     //金额/份额
        TextView tvRevokeType;      //类型
        TextView tvRevokeState;     //状态
    }

}
