package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/17 16:06
 */
public class SopCastAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationBean> list;

    public SopCastAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationBean> list){
        this.list=list;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sopcast_pager,null);
            viewHolder.tvSopCastRiQi = (TextView) convertView.findViewById(R.id.tvSopCastRiQi);
            viewHolder.tvSopCastShiJian = (TextView) convertView.findViewById(R.id.tvSopCastShiJian);
            viewHolder.tvSopCastTitle = (TextView) convertView.findViewById(R.id.tvSopCastTitle);
            viewHolder.tvXiangGuanContent = (TextView) convertView.findViewById(R.id.tvXiangGuanContent);
            viewHolder.tvXiangGuanStock = (TextView) convertView.findViewById(R.id.tvXiangGuanStock);
            viewHolder.ivSopCasType = (ImageView) convertView.findViewById(R.id.ivSopCasType);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InformationBean informationBean = list.get(position);
        String date = informationBean.getDate();                    //日期
        String times = informationBean.getTimes();                  //时间
        String publishTitle = informationBean.getPublishTitle();    //标题
        int pos = informationBean.getPos();                         //判断图片的  表示
        List<String> sopCastList = informationBean.getSopCastList();    //存储 相关股票的 集合


        if(sopCastList != null&& sopCastList.size()>0){
            StringBuilder sb = new StringBuilder();

            if(sopCastList.size()>2){
                for (int i = 0; i < 2; i++) {
                    String relatStock = sopCastList.get(i);
                    sb.append(relatStock).append("  ");
                }
            }else {
                for (int i = 0; i < sopCastList.size(); i++) {
                    String relatStock = sopCastList.get(i);
                    sb.append(relatStock).append("  ");
                }
            }
            viewHolder.tvXiangGuanStock.setText("相关股票:");
            viewHolder.tvXiangGuanContent.setText(sb.toString());
        }else {
            viewHolder.tvXiangGuanStock.setText("");        //如果 没有相关股票  把相关股票这个控件制为空
            viewHolder.tvXiangGuanContent.setText("");
        }

        viewHolder.tvSopCastRiQi.setText(date);
        viewHolder.tvSopCastShiJian.setText(times);
        viewHolder.tvSopCastTitle.setText(publishTitle);

        switch (pos){
            case 0:
                viewHolder.ivSopCasType.setImageResource(R.mipmap.huangse);
                break;
            case 1:
                viewHolder.ivSopCasType.setImageResource(R.mipmap.hao);
                break;
            case 2:
                viewHolder.ivSopCasType.setImageResource(R.mipmap.sopkong);
                break;
        }

        return convertView;
    }

    class ViewHolder{
        TextView tvSopCastRiQi;             //日期
        TextView tvSopCastShiJian;          //时间
        TextView tvSopCastTitle;            //标题
        TextView tvXiangGuanContent;        //相关股票
        ImageView ivSopCasType;             //图片
        TextView tvXiangGuanStock;             //图片
    }
}
