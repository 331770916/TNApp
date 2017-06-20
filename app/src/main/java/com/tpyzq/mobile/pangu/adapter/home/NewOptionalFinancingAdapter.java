package com.tpyzq.mobile.pangu.adapter.home;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.NewOptionalFinancingEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;

/**
 * Created by wangqi on 2016/11/9.
 * 稳赢
 */
public class NewOptionalFinancingAdapter extends BaseAdapter {
    Context context;
    //    OptionalFinancingBean 无用的bean对象
    List<NewOptionalFinancingEntity> prods;

    public NewOptionalFinancingAdapter(Context context) {
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
            convertView = View.inflate(context, R.layout.item_optional_financing, null);
            vh.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
            vh.tv_million_income = (TextView) convertView.findViewById(R.id.tv_million_income);
            vh.tv_termofvalidity = (TextView) convertView.findViewById(R.id.tv_termofvalidity);
            vh.iv_top = (TextView) convertView.findViewById(R.id.iv_top);
            vh.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
            vh.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
            vh.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tv_product.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
        vh.tv_product.setText(prods.get(position).prod_name);

        if (prods.get(position).getType().equals("3")) {

            //OTC
            String prod_term = prods.get(position).prod_term;
            SpannableString sbs_OTC = new SpannableString(prod_term);
            sbs_OTC.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, prod_term.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_million_income.setText(sbs_OTC+"天");
            vh.tv_termofvalidity.setText("年化收益");
            vh.tv_text2.setText("期限");
        } else {
            //        prod_wfsy = TransitionUtils.s2d4(prod_wfsy);
            String prod_wfsy = prods.get(position).prod_wfsy;
            SpannableString sbs = new SpannableString(prod_wfsy);
            sbs.setSpan(new ForegroundColorSpan(ColorUtils.RED), 0, prod_wfsy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_million_income.setText(sbs);
            vh.tv_termofvalidity.setText("七日年化收益");
            vh.tv_text2.setText("万份收益");

        }

        String prod_nhsy = prods.get(position).prod_nhsy;
        switch (prod_nhsy) {
            case "-":
                vh.tv_rate.setText("-");
                break;
            default:
                if (!TextUtils.isEmpty(prod_nhsy)) {
                    vh.tv_rate.setText(prod_nhsy + "%");
                } else {
                    vh.tv_rate.setText("-");
                }
                break;
        }

        if (!TextUtils.isEmpty(prods.get(position).prod_qgje) && Helper.isDecimal(prods.get(position).prod_qgje)) {
//                vh.tv_text1.setText(TransitionUtils.s2million(prods.get(position).prod_qgje) + "元");
            vh.tv_text1.setText(prods.get(position).prod_qgje + "元");
        } else {
            vh.tv_text1.setText(prods.get(position).prod_qgje + "元");
        }


        switch (position) {
            case 0:
//                vh.iv_top.setText("No.1");
                vh.iv_top.setBackgroundResource(R.mipmap.success01);
                break;
            case 1:
//                vh.iv_top.setText("No.2");
                vh.iv_top.setBackgroundResource(R.mipmap.success02);
                break;
            case 2:
//                vh.iv_top.setText("No.3");
                vh.iv_top.setBackgroundResource(R.mipmap.success03);
                break;
        }
//            vh.iv_buy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    BRutil.menuNewSelect("z001-1", "2", "p2", "2", new Date(), "-1", "-1");
//                    String login = Db_PUB_USERS.queryingIslogin();
//                    Intent intent = new Intent();
//                    intent.putExtra("pageindex", TransactionLogin_First.PAGE_INDEX_PRODUCTBUY);
//                    intent.putExtra("productCode", prods.get(position).prod_code);
//                    intent.putExtra("productType", prods.get(position).prod_source);
//
//                    if (!Db_PUB_USERS.isRegister()) {
//                        context.startActivity(new Intent(context, ShouJiZhuCe.class));
//                    } else {
//                        if ("true".equals(login)) {
//                            intent.setClass(context, ProductBuyActivity.class);
//
//                            context.startActivity(intent);
//                        } else {
//                            intent.setClass(context, TransactionLogin_First.class);
//                            context.startActivity(intent);
//                        }
//                    }
//                }
//            });
//

        return convertView;
    }

    class ViewHolder {
        public TextView tv_product;
        public TextView tv_million_income;
        public TextView tv_termofvalidity;
        public TextView iv_top;
        public TextView tv_rate;
        public TextView tv_text1;
        public TextView tv_text2;
    }

}
