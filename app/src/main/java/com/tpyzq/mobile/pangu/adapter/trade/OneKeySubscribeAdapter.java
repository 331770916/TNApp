package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeItem;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/8/10.
 */
public class OneKeySubscribeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OneKeySubscribeItem> list;
    private ICallBackPositionListener listener;
    private ICallBackIdListListener idListListener;
    private ArrayList idList = new ArrayList();

    public OneKeySubscribeAdapter(Context context, ICallBackPositionListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void setList(ArrayList<OneKeySubscribeItem> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public void setListener(ICallBackIdListListener idListListener){
        this.idListListener=idListListener;
    }

    @Override
    public int getCount() {
        if( list != null && list.size() > 0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list!=null&&list.size()>0){
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
      final   ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_one_key_subscribe,null);
            viewHolder=new ViewHolder();
            viewHolder.tvSubscribeStockName= (TextView) convertView.findViewById(R.id.tvSubscribeStockName);
            viewHolder.tvSubscribeStockCode= (TextView) convertView.findViewById(R.id.tvSubscribeStockCode);
            viewHolder.tvSubscribeStockPrice= (TextView) convertView.findViewById(R.id.tvSubscribeStockPrice);
            viewHolder.tvSubscribeStockUpperLimit= (TextView) convertView.findViewById(R.id.tvSubscribeStockUpperLimit);
            viewHolder.tvSubscribeStockNumber= (TextView) convertView.findViewById(R.id.tvSubscribeStockNumber);
            viewHolder.isCheckBox= (CheckBox) convertView.findViewById(R.id.cbPitchOn);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        //赋值
        final OneKeySubscribeItem item = list.get(position);
        viewHolder.tvSubscribeStockName.setText(item.getName());
        viewHolder.tvSubscribeStockCode.setText(item.getCode());
        viewHolder.tvSubscribeStockPrice.setText(item.getPrice()+"元");
        viewHolder.tvSubscribeStockUpperLimit.setText(item.getLimit()+"股");
        viewHolder.tvSubscribeStockNumber.setText(item.getNum());

        /**
         * 点击弹出
         */
        viewHolder.tvSubscribeStockNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callBack(position);
            }
        });


        /**
         * 判断是否勾选
         */
        viewHolder.isCheckBox.setChecked(item.isCheck());

        viewHolder.isCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setCheck(isChecked);
                idListListener.callBack();
            }
        });
        idListListener.callBack();
        return convertView;
    }

    private class ViewHolder{
        CheckBox isCheckBox;
        TextView tvSubscribeStockName;      //股票名称
        TextView tvSubscribeStockCode;      //股票代码
        TextView tvSubscribeStockPrice;     //申购价格
        TextView tvSubscribeStockUpperLimit;        //申购上限
        TextView tvSubscribeStockNumber;        //申购股数
    }

    public interface ICallBackPositionListener {
        void callBack(int position);
    }

    public interface ICallBackIdListListener{
//        public void callBack(ArrayList lists);
            void callBack();
    }


}
