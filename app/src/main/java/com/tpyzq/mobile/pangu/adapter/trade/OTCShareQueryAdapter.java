package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.tpyzq.mobile.pangu.data.OtcShareEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 作者：刘泽鹏 on 2016/8/26 16:09
 * OTC 份额查询也面  listView的适配器
 */
public class OTCShareQueryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OtcShareEntity> list;
    private boolean isClick = false;
    private int tempPosition;
    public OTCShareQueryAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<OtcShareEntity> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list !=null&& list.size()>0){
            return list.size();
        }
        return 0;
    }
    @Override
    public Object getItem(int position) {
        if(list !=null && list.size()>0){
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
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_otcshare,null);
            viewHolder.otcShareStockInfo= (TextView) convertView.findViewById(R.id.otcShareStockInfo);
            viewHolder.otcSharePrice= (TextView) convertView.findViewById(R.id.otcSharePrice);
            viewHolder.otcShareIntoDate= (TextView) convertView.findViewById(R.id.otcShareIntoDate);
            viewHolder.otcShareOutDate= (TextView) convertView.findViewById(R.id.otcShareOutDate);
            viewHolder.otcShareShuHuibtn= (TextView) convertView.findViewById(R.id.otcShareShuHuibtn);
            viewHolder.llYinCang = (LinearLayout) convertView.findViewById(R.id.llYinCang);
            viewHolder.otcUPdownIv = (RelativeLayout) convertView.findViewById(R.id.otcUPdownIv);
            viewHolder.otcUPdownIv1 = (ImageView) convertView.findViewById(R.id.otcUPdownIv1);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OtcShareEntity otcShareIntentBean = list.get(position);
        String prod_name = otcShareIntentBean.getProd_name();//名称
        final String prod_code = otcShareIntentBean.getProd_code();//代码
        String current_amount = otcShareIntentBean.getCurrent_amount();//金额
        String buy_date = otcShareIntentBean.getBuy_date();//购入日期
//        String prod_end_date = otcShareIntentBean.getProd_end_date();//到期日期

        if (list.get(position).isUnFold()) {
            //初始化  显示布局  箭头朝上
            viewHolder.llYinCang.setVisibility(View.VISIBLE);
            viewHolder.otcUPdownIv1.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.otcshare_up));
        } else {
            //初始化  隐藏布局  箭头朝下
            viewHolder.llYinCang.setVisibility(View.GONE);
            viewHolder.otcUPdownIv1.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.otcshare_down));
        }


        //点击箭头
        viewHolder.otcUPdownIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.get(tempPosition).setUnFold(false);

                if (tempPosition != position) {
                    isClick = false;
                }

                if(isClick == false ){         //如果现在是  收起状态

                    viewHolder.llYinCang.setVisibility(View.VISIBLE);
                    viewHolder.otcUPdownIv1.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.otcshare_up));
                    //展开  箭头  向上
                    list.get(position).setUnFold(true);
                    isClick = true;
                }else if(isClick == true){   //如果现在是 展开状态
                    //收起  箭头向下
                    viewHolder.llYinCang.setVisibility(View.GONE);
                    viewHolder.otcUPdownIv1.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.otcshare_down));
                    list.get(position).setUnFold(false);
                    isClick = false;

                }

                tempPosition = position;



                notifyDataSetChanged();

            }
        });
        viewHolder.otcShareShuHuibtn.setBackgroundResource(R.drawable.button_login_unchecked);
        viewHolder.otcShareOutDate.setVisibility(View.GONE);
//        viewHolder.otcShareShuHuibtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(context, OTC_RedeemActivity.class);
//                intent.putExtra("prod_code",prod_code);
//                context.startActivity(intent);
//            }
//        });


        viewHolder.otcShareStockInfo.setText(prod_name+"("+prod_code+")");
        viewHolder.otcSharePrice.setText(current_amount);



        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            if (!TextUtils.isEmpty(buy_date)){
                Date parse = sdf.parse(buy_date);
                String new_buy_date = simpleDateFormat.format(parse);
                viewHolder.otcShareIntoDate.setText("购入日期:"+new_buy_date);
            }else {
                viewHolder.otcShareIntoDate.setText("购入日期:"+"--");
            }

//            if (!TextUtils.isEmpty(prod_end_date)){
//                Date parse1 = sdf.parse(prod_end_date);
//                String new_prod_end_date = simpleDateFormat.format(parse1);
//                viewHolder.otcShareOutDate.setText("到期日期:"+new_prod_end_date);
//            }else {
//                viewHolder.otcShareOutDate.setText("到期日期:"+"--");
//            }

        } catch (ParseException e) {
            e.printStackTrace();
        }




        return convertView;
    }

    class ViewHolder{
        TextView otcShareStockInfo;    //名称 + 代码
        TextView otcSharePrice;        //金额
        TextView otcShareIntoDate;    //购入时间
        TextView otcShareOutDate;     //到期时间
        LinearLayout llYinCang;       //包裹 赎回的按钮的  LinearLayout
        RelativeLayout otcUPdownIv;       // 展开收起的  箭头
        ImageView otcUPdownIv1;       // 展开收起的  箭头
        TextView otcShareShuHuibtn; //赎回 按钮
    }
}
