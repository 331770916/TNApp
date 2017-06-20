package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.FixSucessEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.view.RiskControlView;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/11/9.
 * 精选产品
 */
public class OptionalFinancingAdapter extends BaseAdapter {
    Context context;
    //    OptionalFinancingBean 无用的bean对象
    List<FixSucessEntity.Prod> prods;
    String schema_id;

    public OptionalFinancingAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<FixSucessEntity.Prod> prods, String schema_id) {
        this.prods = prods;
        this.schema_id = schema_id;
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 2) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int currentType = getItemViewType(position);//当前类型
        String prod_wfsy;
        String prod_term;
        if (currentType == 0) {
            ViewHolder1 vh1;
            if (convertView == null) {
                vh1 = new ViewHolder1();
                convertView = View.inflate(context, R.layout.item_win_title, null);
                vh1.tv_title_name = (TextView) convertView.findViewById(R.id.tv_title_name);
                convertView.setTag(vh1);
            } else {
                vh1 = (ViewHolder1) convertView.getTag();
            }
            if (position == 0) {
                vh1.tv_title_name.setText("今日推荐");
            } else if (position == 2) {
                vh1.tv_title_name.setText("收益排行");
            }
        } else if (currentType == 1) {
            ViewHolder2 vh2;
            if (convertView == null) {
                vh2 = new ViewHolder2();
                convertView = View.inflate(context, R.layout.item_subscribe, null);
                vh2.rcv_progress = (RiskControlView) convertView.findViewById(R.id.rcv_progress);
                vh2.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
                vh2.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
                vh2.tv_text3 = (TextView) convertView.findViewById(R.id.tv_text3);
                vh2.tv_text4 = (TextView) convertView.findViewById(R.id.tv_text4);
                vh2.tv_text5 = (TextView) convertView.findViewById(R.id.tv_text5);
                vh2.tv_text6 = (TextView) convertView.findViewById(R.id.tv_text6);
                vh2.tv_text7 = (TextView) convertView.findViewById(R.id.tv_text7);
                vh2.tv_text8 = (TextView) convertView.findViewById(R.id.tv_text8);
                vh2.tv_text9 = (TextView) convertView.findViewById(R.id.tv_text9);
                convertView.setTag(vh2);
            } else {
                vh2 = (ViewHolder2) convertView.getTag();
            }
            String prod_nhsy = prods.get(0).prod_nhsy;
            float risk = Float.valueOf(prod_nhsy);
            vh2.rcv_progress.setMaxProgress(20);
            vh2.rcv_progress.setProgress(risk);
            String name = prods.get(0).prod_name;
            if (name.length() > 6) {
                name = name.substring(0, 6) + "\n" + name.substring(6);
            }
            vh2.tv_text1.setText(prods.get(0).prod_type_name);
            vh2.tv_text2.setText(prod_nhsy + "%");
            vh2.tv_text4.setText(name);

            String prod_qgje = prods.get(0).prod_qgje;
            if (TextUtils.isEmpty(prod_qgje)) {
                prod_qgje = "--";
            }
            vh2.tv_text6.setText(prod_qgje + "元");
            String prod_status = prods.get(0).prod_status;
            if (!TextUtils.isEmpty(prod_status)) {
                switch (prod_status) {
                    case "0":
                        prod_status = "未发布";
                        vh2.tv_text7.setBackgroundResource(R.mipmap.bg_bule_item);
                        vh2.tv_text2.setTextColor(ColorUtils.TEXT);
                        vh2.tv_text5.setTextColor(ColorUtils.TEXT);
                        vh2.tv_text6.setTextColor(ColorUtils.TEXT);
                        vh2.rcv_progress.setProgressColor(ColorUtils.BT_GRAY);
                        break;
                    case "1":
                        prod_status = "预约中";
                        vh2.tv_text7.setBackgroundResource(R.mipmap.bg_bule_item);
                        vh2.tv_text2.setTextColor(ColorUtils.ORANGE);
                        vh2.tv_text5.setTextColor(ColorUtils.ORANGE);
                        vh2.tv_text6.setTextColor(ColorUtils.ORANGE);
                        vh2.rcv_progress.setProgressColor(ColorUtils.ORANGE);
                        break;
                    case "2":
                        prod_status = "预约已满";
                        vh2.tv_text7.setBackgroundResource(R.mipmap.bg_bule_item);
                        vh2.tv_text2.setTextColor(ColorUtils.TEXT);
                        vh2.tv_text5.setTextColor(ColorUtils.TEXT);
                        vh2.tv_text6.setTextColor(ColorUtils.TEXT);
                        vh2.rcv_progress.setProgressColor(ColorUtils.BT_GRAY);
                        break;
                    case "3":
                        prod_status = "认购中";
                        vh2.tv_text7.setBackgroundResource(R.mipmap.bg_red_item);
                        vh2.tv_text2.setTextColor(ColorUtils.ORANGE);
                        vh2.tv_text5.setTextColor(ColorUtils.ORANGE);
                        vh2.tv_text6.setTextColor(ColorUtils.ORANGE);
                        vh2.rcv_progress.setProgressColor(ColorUtils.ORANGE);
                        break;
                    case "4":
                        prod_status = "已售罄";
                        vh2.tv_text7.setBackgroundResource(R.mipmap.bg_red_item);
                        vh2.tv_text2.setTextColor(ColorUtils.TEXT);
                        vh2.tv_text5.setTextColor(ColorUtils.TEXT);
                        vh2.tv_text6.setTextColor(ColorUtils.TEXT);
                        vh2.rcv_progress.setProgressColor(ColorUtils.BT_GRAY);
                        break;
                    default:
                        break;
                }
                vh2.tv_text7.setText(prod_status);
            }
            prod_term = prods.get(0).prod_term;
            prod_wfsy = prods.get(0).prod_wfsy;
            if ("3".equals(prods.get(0).TYPE)) {
                vh2.tv_text3.setText("年化收益率");
                vh2.tv_text9.setText("投资期限");
                if (TextUtils.isEmpty(prod_term)) {
                    prod_term = "--";
                }
                if ("认购中".equals(prod_status)) {
                    vh2.tv_text7.setText("No.1热销中");
                }
                vh2.tv_text5.setText(prod_term + "天");
                String date = prods.get(0).ipo_begin_date;
                if (TextUtils.isEmpty(date)) {
                    date = "--";
                }
                vh2.tv_text8.setText("发行时间:" + date);
            } else {
                vh2.tv_text3.setText("七日年化收益率");
                vh2.tv_text9.setText("万份收益");
                vh2.tv_text7.setText("No.1热销中");
                if (TextUtils.isEmpty(prod_wfsy)) {
                    prod_wfsy = "--";
                }
                vh2.tv_text5.setText(prod_wfsy + "元");
                vh2.tv_text7.setBackgroundResource(R.mipmap.bg_red_item);
                vh2.tv_text8.setText("");
            }

        } else if (currentType == 2) {
            ViewHolder3 vh3;
            if (convertView == null) {
                vh3 = new ViewHolder3();
                convertView = View.inflate(context, R.layout.item_optional_financing, null);
                vh3.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
                vh3.tv_million_income = (TextView) convertView.findViewById(R.id.tv_million_income);
                vh3.tv_top = (TextView) convertView.findViewById(R.id.iv_top);
                vh3.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
                vh3.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
                vh3.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
                vh3.tv_termofvalidity = (TextView) convertView.findViewById(R.id.tv_termofvalidity);
                convertView.setTag(vh3);
            } else {
                vh3 = (ViewHolder3) convertView.getTag();
            }
            String prod_nhsy = prods.get(position - 2).prod_nhsy;
            switch (prod_nhsy) {
                case "-":
                    vh3.tv_rate.setText("-");
                    break;
                default:
                    if (!TextUtils.isEmpty(prod_nhsy)) {
                        vh3.tv_rate.setText(prod_nhsy + "%");
                    } else {
                        vh3.tv_rate.setText("-");
                    }
                    break;
            }
            String qgje = prods.get(position - 2).prod_qgje;
            vh3.tv_product.setText(prods.get(position - 2).prod_name);
            vh3.tv_text1.setText(qgje + "元");
            prod_term = prods.get(position - 2).prod_term;
            prod_wfsy = prods.get(position - 2).prod_wfsy;
            if ("3".equals(prods.get(position - 2).TYPE)) {
                vh3.tv_million_income.setText(prod_term);
                vh3.tv_text2.setText("期限");
                vh3.tv_termofvalidity.setText("年化收益");
            } else {
                vh3.tv_million_income.setText(prod_wfsy);
                vh3.tv_text2.setText("万份收益");
                vh3.tv_termofvalidity.setText("七日年化收益");
            }
            String prod_status = prods.get(0).prod_status;
            if ("3".equals(prods.get(0).TYPE)) {
                if (!"3".equals(prod_status)) {
                    switch (position - 2) {
                        case 1:
                            vh3.tv_top.setVisibility(View.VISIBLE);
                            vh3.tv_top.setBackgroundResource(R.mipmap.success01);
                            vh3.tv_top.setText("No.1");
                            break;
                        case 2:
                            vh3.tv_top.setVisibility(View.VISIBLE);
                            vh3.tv_top.setBackgroundResource(R.mipmap.success02);
                            vh3.tv_top.setText("No.2");
                            break;
                        case 3:
                            vh3.tv_top.setVisibility(View.VISIBLE);
                            vh3.tv_top.setBackgroundResource(R.mipmap.success03);
                            vh3.tv_top.setText("No.3");
                            break;
                        default:
                            vh3.tv_top.setVisibility(View.INVISIBLE);
                            break;
                    }
                } else {
                    switch (position - 2) {
                        case 1:
                            vh3.tv_top.setVisibility(View.VISIBLE);
                            vh3.tv_top.setBackgroundResource(R.mipmap.success02);
                            vh3.tv_top.setText("No.2");
                            break;
                        case 2:
                            vh3.tv_top.setVisibility(View.VISIBLE);
                            vh3.tv_top.setBackgroundResource(R.mipmap.success03);
                            vh3.tv_top.setText("No.3");
                            break;
                        default:
                            vh3.tv_top.setVisibility(View.INVISIBLE);
                            break;
                    }
                }
            } else {
                switch (position - 2) {
                    case 1:
                        vh3.tv_top.setVisibility(View.VISIBLE);
                        vh3.tv_top.setBackgroundResource(R.mipmap.success02);
                        vh3.tv_top.setText("No.2");
                        break;
                    case 2:
                        vh3.tv_top.setVisibility(View.VISIBLE);
                        vh3.tv_top.setBackgroundResource(R.mipmap.success03);
                        vh3.tv_top.setText("No.3");
                        break;
                    default:
                        vh3.tv_top.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }
        return convertView;
    }

    class ViewHolder1 {
        public TextView tv_title_name;
    }

    class ViewHolder2 {
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
    }

    class ViewHolder3 {
        public TextView tv_product;
        public TextView tv_million_income;
        public TextView tv_top;
        public TextView tv_rate;
        public TextView tv_text1;
        public TextView tv_text2;
        public TextView tv_termofvalidity;
    }
}
