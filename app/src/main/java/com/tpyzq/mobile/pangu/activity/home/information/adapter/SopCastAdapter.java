package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationEntity;
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
    private TextView tvDay,tvDate;
    private String currentDay="",currentTime="";

    public SopCastAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationEntity> list,TextView tv1,TextView tv2){
        this.list=list;
        this.tvDay = tv1;
        this.tvDate = tv2;
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
            convertView.findViewById(R.id.rlTop).setVisibility(View.GONE);
            convertView.findViewById(R.id.rlLeft).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.rlRight).setVisibility(View.VISIBLE);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==0)
            viewHolder.tvTime.setVisibility(View.GONE);
        else
            viewHolder.tvTime.setVisibility(View.VISIBLE);
        InformationEntity informationBean = list.get(position);
        String date = informationBean.getDate();                    //日期
        String time = informationBean.getTime();
        if(tvDay!=null)
            tvDay.setText(getWeekOfDate(getDate(TextUtils.isEmpty(currentDay)?date:currentDay)));
        if(tvDate!=null)
            tvDate.setText(TextUtils.isEmpty(currentDay)?date:currentDay+" "+(TextUtils.isEmpty(currentTime)?time:currentTime));
        currentDay = date;
        currentTime = time;
        String title = informationBean.getTitle();    //标题
        String content = informationBean.getDigest();
        viewHolder.tvTime.setText(date+" "+time);
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
        TextView tvTime;
        TextView tvTitle;
        TextView tvContent;
    }
}
