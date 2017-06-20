package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ContractFlowWaterActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ContractSignActivity;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;

import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/9/5 15:18
 */
public class OTC_ElectronicContractAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OTC_ElectronicContractEntity> list;

    public OTC_ElectronicContractAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<OTC_ElectronicContractEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list != null && list.size()>0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null && list.size()>0 ){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_electronic_contract,null);
            viewHolder.tvProductContractName = (TextView) convertView.findViewById(R.id.tvProductContractName);
            viewHolder.tvProductContractCode = (TextView) convertView.findViewById(R.id.tvProductContractCode);
            viewHolder.tvDianJi = (TextView) convertView.findViewById(R.id.tvDianJi);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OTC_ElectronicContractEntity intentBean = list.get(position);
        viewHolder.tvProductContractName.setText(intentBean.getProd_name());
        viewHolder.tvProductContractCode.setText(intentBean.getProd_code());

        String is_sign = intentBean.getIs_sign();
        if(is_sign.equals("0")){
            viewHolder.tvDianJi.setText("已签约");
            viewHolder.tvDianJi.setTextColor(context.getResources().getColor(R.color.blue));
        }else if(is_sign.equals("1")){
            viewHolder.tvDianJi.setText("点击签约");
            viewHolder.tvDianJi.setTextColor(context.getResources().getColor(R.color.orange1));
        }
        viewHolder.tvDianJi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTC_ElectronicContractEntity intentBean = list.get(position);
                ArrayList<OTC_ElectronicContractEntity> listBean = new ArrayList<OTC_ElectronicContractEntity>();
                listBean.add(intentBean);
                String is_sign = intentBean.getIs_sign();
                String prod_name = intentBean.getProd_name();
                String prod_code = intentBean.getProd_code();
                Intent intent = new Intent();
                if(is_sign.equals("1")){
                    intent.setClass(context,OTC_ContractSignActivity.class);
                    intent.putExtra("list",listBean);
                    context.startActivity(intent);
                }else if(is_sign.equals("0")){
                    intent.setClass(context,OTC_ContractFlowWaterActivity.class);
                    intent.putExtra("list",listBean);
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }



    class ViewHolder{
        TextView tvProductContractName;         //产品名称
        TextView tvProductContractCode;         //产品代码
        TextView tvDianJi;                       //点击签约
    }
}
