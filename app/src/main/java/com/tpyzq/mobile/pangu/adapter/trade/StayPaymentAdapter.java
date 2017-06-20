package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * 作者：刘泽鹏 on 2016/8/16 19:15
 */
public class StayPaymentAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<HashMap<String, String>> list;

    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心

    private boolean isVisible;  //用于记录隐藏布局的可见性


    public StayPaymentAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setList(ArrayList<HashMap<String, String>> list) {
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
            list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ViewHolder holder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_subscribe_history, parent, false);
            holder = new ViewHolder();
            holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            holder.textView_num = (TextView) convertView.findViewById(R.id.textView_num);
            holder.textViewS_data = (TextView) convertView.findViewById(R.id.textViewS_data);
            holder.textViewS_price = (TextView) convertView.findViewById(R.id.textViewS_price);
            holder.textViewS_num = (TextView) convertView.findViewById(R.id.textViewS_num);
            holder.textViewS_num2 = (TextView) convertView.findViewById(R.id.textViewS_num2);
            holder.textView_time1 = (TextView) convertView.findViewById(R.id.textView_time1);
            holder.textView_time2 = (TextView) convertView.findViewById(R.id.textView_time2);
            holder.textView_time3 = (TextView) convertView.findViewById(R.id.textView_time3);
            holder.textView_time4 = (TextView) convertView.findViewById(R.id.textView_time4);
            holder.tvZhongQian = (TextView) convertView.findViewById(R.id.tvZhongQian);
            holder.tvZhongQianLv = (TextView) convertView.findViewById(R.id.tvZhongQianLv);
            holder.tvPaiHaoShuLiangNum = (TextView) convertView.findViewById(R.id.tvPaiHaoShuLiangNum);
            holder.tvQiShiHao = (TextView) convertView.findViewById(R.id.tvQiShiHao);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView5);

            holder.hideArea = (RelativeLayout) convertView.findViewById(R.id.layout_hideArea);
            holder.anniu = (RelativeLayout) convertView.findViewById(R.id.anniu);

            holder.Background_1 = (TextView) convertView.findViewById(R.id.textView16);
            holder.Background_2 = (TextView) convertView.findViewById(R.id.textView19);
            holder.Background_3 = (TextView) convertView.findViewById(R.id.textView22);
            holder.Background_4 = (TextView) convertView.findViewById(R.id.textView32);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

//        HashMap<String, String> item = list.get(position);
        HashMap<String, String> map = list.get(position);

        // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，
        // 为了记录点击的 position，我们正好用 position 设置 Tag
        holder.showArea.setTag(position);
        holder.anniu.setTag(position);
        // holder.textView_num.setText(item.get("textView_num"));
        //第一个textView_num为viewholder类的属性  第二个为数据源中集合的键名
        holder.textViewName.setText(map.get("stock_name"));                    //股票名称
        holder.textView_num.setText(map.get("stock_code"));                    //股票代码
        holder.textView3.setText(map.get("stock_step"));                   //状态
        holder.textViewS_data.setText(Helper.getMyDateY_M_D(map.get("entrust_date")));               //申购日期
        holder.textViewS_price.setText(map.get("entrust_price"));              //申购价格
        String entrust_amount = map.get("entrust_amount");
        if (entrust_amount != null) {
            int i = entrust_amount.indexOf(".");
            String substring = entrust_amount.substring(0, i);
            holder.textViewS_num.setText(substring);  //申购数量
        }


        String occur_amount = map.get("occur_amount");
        if (!TextUtils.isEmpty(occur_amount) && !"0".equals(occur_amount)) {
            int idx = occur_amount.lastIndexOf(".");//查找小数点的位置
            String strNum = occur_amount.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
            holder.textViewS_num2.setText(strNum);                 //中签数
        } else {
            holder.textViewS_num2.setText("0");
        }


        String entrust_date = map.get("entrust_date");

        String Date_t = map.get("t1");
        String Date_t_1 = map.get("next_trade_date");

        String Date_t2 = map.get("t2");
        String Date_t2_2 = map.get("ISSUERESULTPUBLDATE");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
        if (null != entrust_date) {
            try {
                Date new_entrust_date = sdf1.parse(entrust_date);
                String format = sdf2.format(new_entrust_date);
                holder.textView_time1.setText(format);                         //时间一
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //王奇
        if (!TextUtils.isEmpty(Date_t)) {                           //t1
            try {
                Date new_Date_t1 = sdf1.parse(Date_t);
                String format_t1 = sdf2.format(new_Date_t1);
                holder.textView_time2.setText(format_t1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (!TextUtils.isEmpty(Date_t_1)) {
            try {
                Date new_Date_t1_1 = sdf1.parse(Date_t_1);
                String format_t1 = sdf2.format(new_Date_t1_1);
                holder.textView_time2.setText(format_t1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (!TextUtils.isEmpty(Date_t2)) {                                      //t2  时间
            try {
                Date new_Date_t2 = sdf1.parse(Date_t2);
                String format_t1 = sdf2.format(new_Date_t2);
                holder.textView_time3.setText(format_t1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (!TextUtils.isEmpty(Date_t2_2)) {
            try {
                Date new_Date_t2_2 = sdf1.parse(Date_t2_2);
                String format_t1 = sdf2.format(new_Date_t2_2);
                holder.textView_time3.setText(format_t1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        String lotrateonline = map.get("lotrateonline");             //中签率
        if (!TextUtils.isEmpty(lotrateonline) && !TextUtils.isEmpty(occur_amount)) {
            double aDouble = Double.valueOf(lotrateonline);
            DecimalFormat format2 = new DecimalFormat("#0.00%");
            String format = format2.format(aDouble);

            int idx = occur_amount.lastIndexOf(".");//查找小数点的位置
            String strNum = occur_amount.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
            holder.tvZhongQianLv.setText(strNum + "(中签率" + format + "%" + ")");


        } else if (TextUtils.isEmpty(lotrateonline) && !TextUtils.isEmpty(occur_amount)) {
            if (!"0".equals(occur_amount)) {
                int idx = occur_amount.lastIndexOf(".");//查找小数点的位置
                String strNum = occur_amount.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
                holder.tvZhongQianLv.setText(strNum + "(中签率" + "0.00" + "%" + ")");
            } else {
                holder.tvZhongQianLv.setText("0" + "(中签率" + "0.00" + "%" + ")");
            }


        } else if (!TextUtils.isEmpty(lotrateonline) && TextUtils.isEmpty(occur_amount)) {

            double aDouble = Double.valueOf(lotrateonline);
            DecimalFormat format2 = new DecimalFormat("#0.00%");
            String format = format2.format(aDouble);
            holder.tvZhongQianLv.setText("0" + "(中签率" + format + "%" + ")");

        } else {
            holder.tvZhongQianLv.setText("0" + "(中签率" + "0.00%" + ")");
        }


        String listdate = map.get("listdate");
        if (!TextUtils.isEmpty(listdate)) {
            String replace1 = listdate.replace("-", ".");
            String substring1 = replace1.substring(5, replace1.length());
            holder.textView_time4.setText(substring1);                                        //时间4
        }

        String business_amount = map.get("business_amount");
        if (!TextUtils.isEmpty(business_amount)) {
            holder.tvPaiHaoShuLiangNum.setText(business_amount.substring(0, business_amount.indexOf(".")));  //配号数量
        } else {
            holder.tvPaiHaoShuLiangNum.setText("0");                     //配号数量
        }

        String textView_time1_Date = holder.textView_time1.getText().toString().trim();  //申购
        String textView_time2_Date = holder.textView_time2.getText().toString().trim();  //配号
        String textView_time3_Date = holder.textView_time3.getText().toString().trim();  //中签
        String textView_time4_Date = holder.textView_time4.getText().toString().trim();  //待上市


        if ("今日申购".equals(map.get("stock_step"))) {
            holder.Background_1.setBackgroundResource(R.drawable.shengouxiao);       //黄色 ,申购
            holder.Background_1.setTextColor(context.getResources().getColor(R.color.subscribe));//黄色 文字
        } else if ("配号".equals(map.get("stock_step"))) {
            holder.Background_2.setBackgroundResource(R.drawable.shengouxiao);       //黄色 ,配号
            holder.Background_2.setTextColor(context.getResources().getColor(R.color.subscribe));//黄色 文字
            holder.tvZhongQian.setTextColor(context.getResources().getColor(R.color.subscribe));//黄色 文字
            holder.tvZhongQianLv.setTextColor(context.getResources().getColor(R.color.subscribe));//黄色 文字

        } else if ("中签".equals(map.get("stock_step"))) {
            holder.Background_3.setBackgroundResource(R.drawable.shengouxiao);       //黄色 ,中签
            holder.Background_3.setTextColor(context.getResources().getColor(R.color.subscribe));//黄色 文字
            holder.tvZhongQian.setTextColor(context.getResources().getColor(R.color.subscribe));//黄色 文字
            holder.tvZhongQianLv.setTextColor(context.getResources().getColor(R.color.subscribe));//黄色 文字
        } else if ("--".equals(map.get("stock_step"))){
            holder.Background_1.setBackgroundResource(R.drawable.xingushengou);
            holder.Background_2.setBackgroundResource(R.drawable.xingushengou);
            holder.Background_3.setBackgroundResource(R.drawable.xingushengou);
            holder.Background_4.setBackgroundResource(R.drawable.xingushengou);
            holder.Background_4.setText("- -");

        }




        holder.tvQiShiHao.setText(map.get("remark"));                                           //起始号


        //根据 currentItem 记录的点击位置来设置"对应Item"的可见
        // 性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
            isVisible = true;

        } else {
            holder.hideArea.setVisibility(View.GONE);
            isVisible = false;
        }
        if (isVisible) {
            holder.anniu.setBackgroundColor(Color.parseColor("#f0f0f0"));
            holder.imageView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.imageView.setImageResource(R.mipmap.shang);
        } else {
            holder.anniu.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.imageView.setBackgroundColor(Color.parseColor("#f2f2f2"));
            holder.imageView.setImageResource(R.mipmap.xia);
        }
        holder.anniu.setOnClickListener(new View.OnClickListener() {

            @Override


            public void onClick(View view) {
                //用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次点击
                    currentItem = -1; //给 currentItem 一个无效值

                } else {
                    currentItem = tag;

                }

                //通知adapter数据改变需要重新加载
                notifyDataSetChanged(); //必须有的一步

            }


        });
        return convertView;
    }


    private static class ViewHolder {

        private RelativeLayout anniu;
        private LinearLayout showArea;

        private RelativeLayout hideArea;

        //股票名称
        private TextView textViewName;
        //股票代码
        private TextView textView_num;
        //状态
        private TextView textView3;
        //申购日期
        private TextView textViewS_data;
        //申购价格
        private TextView textViewS_price;
        //申购数量
        private TextView textViewS_num;
        //中签数量
        private TextView textViewS_num2;
        //时间一
        private TextView textView_time1;
        //时间二
        private TextView textView_time2;
        //时间三
        private TextView textView_time3;
        //时间四
        private TextView textView_time4;
        //配号数量
        private TextView tvPaiHaoShuLiangNum;
        //起始号
        private TextView tvQiShiHao;
        //中签数量
        private TextView tvZhongQian;
        //中签率
        private TextView tvZhongQianLv;
        //安钮图片
        private ImageView imageView;
        //审配
        private TextView Background_1;
        //配号
        private TextView Background_2;
        //中签
        private TextView Background_3;
        //待上市
        private TextView Background_4;
    }
}
