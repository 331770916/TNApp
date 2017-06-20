package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationBean;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/9/16 14:53
 * 热点模块   ListView 适配器
 */
public class HotAnalysisListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationBean> list;
    private ArrayList<InformationBean> pirceList;

    public HotAnalysisListViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setPirceList(ArrayList<InformationBean> pirceList) {
        this.pirceList = pirceList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hot_analysis_listview, null);
//            viewHolder.llTianShu = (LinearLayout) convertView.findViewById(R.id.llTianShu);
//            viewHolder.tvDays = (TextView) convertView.findViewById(R.id.tvDays);
            viewHolder.tvHotStockContent = (TextView) convertView.findViewById(R.id.tvHotStockContent);
            viewHolder.tvHotStockName = (TextView) convertView.findViewById(R.id.tvHotStockName);
            viewHolder.tvHotXianJia = (TextView) convertView.findViewById(R.id.tvHotXianJia);
            viewHolder.tvHotBaiFenBi = (TextView) convertView.findViewById(R.id.tvHotBaiFenBi);
            viewHolder.HotStockNum = (TextView) convertView.findViewById(R.id.HotStockNum);
            viewHolder.v_line = convertView.findViewById(R.id.v_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InformationBean entity = list.get(position);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.v_line.getLayoutParams());
//        lp.setMargins(TransitionUtils.dp2px(200,context), 0, 0, 0);
//        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(viewHolder.v_line.getLayoutParams());
//        lp2.setMargins(0, 0, 0, 0);
//        if (position == list.size()-1){
//            viewHolder.v_line.setLayoutParams(lp2);
//        }else {
//            viewHolder.v_line.setLayoutParams(lp);
//        }
        if (pirceList != null && pirceList.size() > 0 && !pirceList.get(position).getPrice().equals("-")) {
            InformationBean entity1 = pirceList.get(position);
            String price = entity1.getPrice();      //现价
            viewHolder.tvHotXianJia.setText(price);
        } else {
            viewHolder.tvHotXianJia.setText("--");
        }


//        String days = entity.getDays();     //天数
//        Integer integer = Integer.valueOf(days);
//        if(integer == 0){
//            viewHolder.llTianShu.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
//            viewHolder.tvDays.setText("0天");
//        }else {
//            viewHolder.llTianShu.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
//            viewHolder.tvDays.setText(days+"天");
//        }

        String content = entity.getTname();   //状态
        String stockName = entity.getPublishAboutStock();   //股票名称


        viewHolder.tvHotStockName.setText(stockName);
        viewHolder.tvHotStockContent.setText(content);


        if (!TextUtils.isDigitsOnly(entity.getProb())) {    //百分比
            viewHolder.tvHotBaiFenBi.setText(entity.getProb());
        } else {
            viewHolder.tvHotBaiFenBi.setText("--%");
        }
        viewHolder.HotStockNum.setText("共" + entity.getStockslength() + "只股");

        return convertView;
    }

    class ViewHolder {
        //        LinearLayout llTianShu;              //建议持有天数的  LinearLayout  用于设置背景颜色
//        TextView tvDays;                     //持有天数
        TextView tvHotStockContent;         //状态
        TextView tvHotStockName;            //股票名称
        TextView tvHotXianJia;              //现价
        TextView tvHotBaiFenBi;             //百分比
        TextView HotStockNum;               //股票数量
        View v_line;
    }
}
