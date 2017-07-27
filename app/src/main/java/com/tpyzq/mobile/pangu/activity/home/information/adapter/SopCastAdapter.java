package com.tpyzq.mobile.pangu.activity.home.information.adapter;

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
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者：刘泽鹏 on 2016/9/17 16:06
 */
public class SopCastAdapter extends BaseAdapter {
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private ArrayList<InformationEntity> list;
    private String currentDay;

    public SopCastAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationEntity> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
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
            viewHolder.rlTop = (RelativeLayout)convertView.findViewById(R.id.rlTop);
            viewHolder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InformationEntity informationBean = list.get(position);
        String date = informationBean.getDate();                    //日期
        if(TextUtils.isEmpty(currentDay)){
            currentDay = date;
            viewHolder.rlTop.setVisibility(View.VISIBLE);
        }else{
            if(date.equals(currentDay))
                viewHolder.rlTop.setVisibility(View.GONE);
            else {
                viewHolder.rlTop.setVisibility(View.VISIBLE);
                currentDay = date;
            }
        }
        String times = informationBean.getTime();                  //时间
        String title = informationBean.getTitle();    //标题
        String content = informationBean.getDigest();
        viewHolder.tvDay.setText(getWeekOfDate(getDate(date)));
        viewHolder.tvDate.setText(date);
        viewHolder.tvTime.setText(times);
        viewHolder.tvTitle.setText(title);
        viewHolder.tvContent.setText(content);
        return convertView;
    }

    public static Date getDate(String date){
        try {
           return sf.parse(date);
        }catch (Exception e){
         e.printStackTrace();
        }
        return null;
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDaysCode = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysCode[intWeek];
    }

    class ViewHolder{
        RelativeLayout rlTop;
        TextView tvDay;
        TextView tvDate;
        TextView tvTime;
        TextView tvTitle;
        TextView tvContent;
    }
}
