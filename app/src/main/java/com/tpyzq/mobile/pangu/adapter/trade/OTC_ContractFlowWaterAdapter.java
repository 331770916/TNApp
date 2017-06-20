package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/9/5 20:40
 */
public class OTC_ContractFlowWaterAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OTC_ElectronicContractEntity> list;

    public OTC_ContractFlowWaterAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<OTC_ElectronicContractEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contract_flow_water, null);
            viewHolder.tvOTCProductCode = (TextView) convertView.findViewById(R.id.tvOTCProductCode);
            viewHolder.tvOTCProductCompanyCode = (TextView) convertView.findViewById(R.id.tvOTCProductCompanyCode);
            viewHolder.tvOTCFaShengData = (TextView) convertView.findViewById(R.id.tvOTCFaShengData);
            viewHolder.tvHeTongNum = (TextView) convertView.findViewById(R.id.tvHeTongNum);
            viewHolder.tvSTATUS = (TextView) convertView.findViewById(R.id.tvSTATUS);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OTC_ElectronicContractEntity intentBean = list.get(position);
        viewHolder.tvOTCProductCode.setText(intentBean.getProd_code());
        viewHolder.tvOTCProductCompanyCode.setText(intentBean.getProdta_no_name());
        viewHolder.tvOTCFaShengData.setText(Helper.getMyDateY_M_D(intentBean.getInit_date()));
        viewHolder.tvHeTongNum.setText("合同编号："+intentBean.getEcontract_id());
        switch (intentBean.getStatus()) {
            case "0":
                viewHolder.tvSTATUS.setText("未报");
                break;
            case "1":
                viewHolder.tvSTATUS.setText("已报");
                break;
            case "2":
                viewHolder.tvSTATUS.setText("成功");
                break;
            case "3":
                viewHolder.tvSTATUS.setText("补正");
                break;
            case "4":
                viewHolder.tvSTATUS.setText("结束");
                break;
            case "5":
                viewHolder.tvSTATUS.setText("已补正");
                break;
            default:
                viewHolder.tvSTATUS.setText("--");

        }


        return convertView;
    }

    class ViewHolder {
        TextView tvOTCProductCode;
        TextView tvOTCProductCompanyCode;
        TextView tvOTCFaShengData;
        TextView tvHeTongNum;
        TextView tvSTATUS;
    }
}
