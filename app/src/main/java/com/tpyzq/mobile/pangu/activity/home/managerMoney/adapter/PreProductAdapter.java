package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.NewOptionalFinancingEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.view.RiskControlView;

import java.util.List;

/**
 * 预约产品adapter
 * 稳赢
 */
public class PreProductAdapter extends BaseAdapter {
    Context context;
    //    OptionalFinancingBean 无用的bean对象
    List<NewOptionalFinancingEntity> prods;

    public PreProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<NewOptionalFinancingEntity> prods) {
        this.prods = prods;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (prods != null && prods.size() > 0) {
            return prods.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (prods != null && prods.size() > 0) {
            return prods.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_newoptional_financing, null);
            vh.rcv_progress = (RiskControlView) convertView.findViewById(R.id.rcv_progress);
            vh.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
            vh.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
            vh.tv_text3 = (TextView) convertView.findViewById(R.id.tv_text3);
            vh.tv_text4 = (TextView) convertView.findViewById(R.id.tv_text4);
            vh.tv_text5 = (TextView) convertView.findViewById(R.id.tv_text5);
            vh.tv_text6 = (TextView) convertView.findViewById(R.id.tv_text6);
            vh.tv_text7 = (TextView) convertView.findViewById(R.id.tv_text7);
            vh.tv_text8 = (TextView) convertView.findViewById(R.id.tv_text8);
            vh.tv_text9 = (TextView) convertView.findViewById(R.id.tv_text9);
            vh.tv_text10 = (TextView) convertView.findViewById(R.id.tv_text10);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();

        }
        String prod_wfsy = prods.get(position).getProd_wfsy();
        String prod_term = prods.get(position).getProd_term();

        String prod_nhsy = prods.get(position).getProd_nhsy();
        float risk = Float.valueOf(prod_nhsy);
        vh.rcv_progress.setMaxProgress(20);
        vh.rcv_progress.setProgress(risk);

        String prod_name = prods.get(position).getProd_name();
        if (prod_name.length() > 6) {
            prod_name = prod_name.substring(0, 6) + "\n" + prod_name.substring(6);
        }

        vh.tv_text1.setText(prods.get(position).getProd_type_name());
        vh.tv_text2.setText(prod_nhsy + "%");


        vh.tv_text4.setText(prod_name);
        if (TextUtils.isEmpty(prod_term)) {
            prod_term = "--";
        }
        if ("3".equals(prods.get(position).getType())) {
            vh.tv_text3.setText("年化收益率");
            vh.tv_text9.setText("投资期限");
            vh.tv_text5.setText(prod_term + "天");
        } else {
            vh.tv_text9.setText("万份收益");
            vh.tv_text3.setText("七日年化收益率");
            vh.tv_text5.setText(prod_wfsy + "元");
        }


        String prod_qgje = prods.get(position).getProd_qgje();
        if (TextUtils.isEmpty(prod_qgje)) {
            prod_qgje = "--";
        }

        vh.tv_text6.setText(prod_qgje + "元");
        String prod_status = prods.get(position).getProd_status();
        if (!TextUtils.isEmpty(prod_status)) {
            switch (prod_status) {
                case "0":
                    prod_status = "未发布";
                    vh.rcv_progress.setProgressColor(ColorUtils.BT_GRAY);
                    vh.tv_text2.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text5.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text6.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text7.setBackgroundResource(R.mipmap.bg_bule_item);

                    break;
                case "1":
                    prod_status = "预约中";
                    vh.rcv_progress.setProgressColor(ColorUtils.ORANGE);
                    vh.tv_text2.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text5.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text6.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text7.setBackgroundResource(R.mipmap.bg_bule_item);
                    break;
                case "2":
                    prod_status = "预约已满";
                    vh.rcv_progress.setProgressColor(ColorUtils.BT_GRAY);
                    vh.tv_text2.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text5.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text6.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text7.setBackgroundResource(R.mipmap.bg_bule_item);

                    break;
                case "3":
                    prod_status = "认购中";
                    vh.rcv_progress.setProgressColor(ColorUtils.ORANGE);
                    vh.tv_text2.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text5.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text6.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text7.setBackgroundResource(R.mipmap.bg_red_item);
                    break;
                case "4":
                    prod_status = "已售罄";
                    vh.rcv_progress.setProgressColor(ColorUtils.BT_GRAY);
                    vh.tv_text2.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text5.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text6.setTextColor(context.getResources().getColor(R.color.texts));
                    vh.tv_text7.setBackgroundResource(R.mipmap.bg_red_item);

                    break;
                case "-1":
                    prod_status = "NO.1 热销中";
                    vh.rcv_progress.setProgressColor(ColorUtils.ORANGE);
                    vh.tv_text2.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text5.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text6.setTextColor(context.getResources().getColor(R.color.orange2));
                    vh.tv_text7.setBackgroundResource(R.mipmap.bg_red_item);
                    break;
                default:
                    break;
            }
            vh.tv_text7.setText(prod_status);
        }else {
            vh.tv_text7.setText("NO.1 热销中");
            vh.rcv_progress.setProgressColor(ColorUtils.ORANGE);
            vh.tv_text2.setTextColor(context.getResources().getColor(R.color.orange2));
            vh.tv_text5.setTextColor(context.getResources().getColor(R.color.orange2));
            vh.tv_text6.setTextColor(context.getResources().getColor(R.color.orange2));
            vh.tv_text7.setBackgroundResource(R.mipmap.bg_red_item);
        }

        if ("3".equals(prods.get(position).getType())) {
            vh.tv_text8.setVisibility(View.VISIBLE);
        } else {
            vh.tv_text8.setVisibility(View.INVISIBLE);
        }
        String date = prods.get(position).getIpo_begin_date();
        if (TextUtils.isEmpty(date)) {
            date = "--";
        }
        vh.tv_text8.setText("发行时间:" + date);

        return convertView;
    }

    class ViewHolder {
        public RiskControlView rcv_progress;
        public TextView tv_text1;
        public TextView tv_text2;
        public TextView tv_text3;
        public TextView tv_text4;
        public TextView tv_text5;
        public TextView tv_text6;
        public TextView tv_text7;
        public TextView tv_text8;
        public TextView tv_text9;
        public TextView tv_text10;
    }
}
