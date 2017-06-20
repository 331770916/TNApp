package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OTC_OpenAccountEntity;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/9/1 21:05
 * OTC开户  选择产品公司界面的适配器
 */
public class OTC_OpenAccountProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OTC_OpenAccountEntity> list;
    private int point = -1;

    public OTC_OpenAccountProductAdapter(Context context) {
        this.context = context;
    }

    public void setPoint(int point){
        this.point=point;
    }

    public void setList(ArrayList<OTC_OpenAccountEntity> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list !=null && list.size()>0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list !=null && list.size()>0 ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_otc_open_account_product,null);
            viewHolder.OTC_OpenAccountProductName= (TextView) convertView.findViewById(R.id.OTC_OpenAccountProductName);
            viewHolder.ivOpenAccountDuiGou = (ImageView) convertView.findViewById(R.id.ivOpenAccountDuiGou);
            viewHolder.mXian=convertView.findViewById(R.id.Xian);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OTC_OpenAccountEntity intentBean = list.get(position);
        viewHolder.OTC_OpenAccountProductName.setText(intentBean.getProductName());

        if(intentBean.isFlag()){
            viewHolder.ivOpenAccountDuiGou.setBackgroundResource(R.mipmap.duigou);
        }

        if(position == point){
            viewHolder.ivOpenAccountDuiGou.setBackgroundResource(R.mipmap.duigou);
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.mXian.getLayoutParams());
        lp.setMargins(TransitionUtils.dp2px(20, context), 0, 0, 0);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(viewHolder.mXian.getLayoutParams());
        lp2.setMargins(0, 0, 0, 0);
        if (position == list.size() - 1) {
            viewHolder.mXian.setLayoutParams(lp2);
        } else {
            viewHolder.mXian.setLayoutParams(lp);
        }



        return convertView;
    }

    class ViewHolder{
        TextView OTC_OpenAccountProductName;
        ImageView ivOpenAccountDuiGou;
        View mXian;
    }

}
