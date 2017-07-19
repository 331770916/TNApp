package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2017/7/18.
 * 基金信息Adapter
 */

public class FundInfoAdapter extends BaseAdapter {

    private Context mContext;
    private boolean isShowDeatil;
    private ArrayList<FundSubsEntity> mEntitys;
    public FundInfoAdapter (Context context, boolean isShowDeatil) {
        mContext = context;
        this.isShowDeatil = isShowDeatil;
    }

    public void setDatas(ArrayList<FundSubsEntity> entitys) {
        mEntitys = entitys;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mEntitys != null && mEntitys.size() > 0) {
            return mEntitys.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mEntitys != null && mEntitys.size() > 0) {
            return mEntitys.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_fundinfo, null);
            viewHodler.mNameCode = (TextView) convertView.findViewById(R.id.tv_name_code);
            viewHodler.mDetailTv = (TextView) convertView.findViewById(R.id.tv_detail);
            viewHodler.mFundVal = (TextView) convertView.findViewById(R.id.tv_fund_val);
            viewHodler.mOpenShare = (TextView) convertView.findViewById(R.id.tv_open_share);
            viewHodler.mDiscrbLayout = (LinearLayout) convertView.findViewById(R.id.ll_disrib);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        initNameCode(position, viewHodler.mNameCode);

        if (isShowDeatil) {
            viewHodler.mDetailTv.setVisibility(View.VISIBLE);
        } else {
            viewHodler.mDetailTv.setVisibility(View.GONE);
        }

        initVal(position, viewHodler.mFundVal);
        initOpenShare(position, viewHodler.mOpenShare);
        initDiscriblayout(position, viewHodler.mDiscrbLayout);
        return convertView;
    }

    private void initNameCode(int position, TextView textView) {
        String name = mEntitys.get(position).FUND_NAME;

        if (name.length() > 12) {
            name = name.substring(0, 12) + "...";
        }

        String kong = "\u2000";
        String code = mEntitys.get(position).FUND_CODE;

        String strNameCode = name + kong + code;

        SpannableString nameCode = new SpannableString(strNameCode);
        nameCode.setSpan(new ForegroundColorSpan(Color.BLACK), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameCode.setSpan(new AbsoluteSizeSpan(16,true), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameCode.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.newStockTitle)), (name + kong).length(), strNameCode.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameCode.setSpan(new AbsoluteSizeSpan(12, true), (name + kong).length(), strNameCode.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(nameCode);
    }

    private void initVal(int position, TextView textView) {
        String val = mEntitys.get(position).FUND_VAL;
        String valTitle = "净值";
        String strVal = val + "\n" + valTitle;

        SpannableString valss = new SpannableString(strVal);
        valss.setSpan(new ForegroundColorSpan(Color.RED), 0, val.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
        valss.setSpan(styleSpan_B, 0, val.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        valss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.texthint93)), val.length(), strVal.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        valss.setSpan(new AbsoluteSizeSpan(23,true), 0, val.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        valss.setSpan(new AbsoluteSizeSpan(12, true), val.length(), strVal.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(valss);
    }

    private void initOpenShare(int position, TextView textView) {
        String openShare = setString(mEntitys.get(position).OPEN_SHARE);
        String openShareTitle = "最低投资";
        String strOpenShare = openShare + "\n" + openShareTitle;

        SpannableString opensharess = new SpannableString(strOpenShare);
        opensharess.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_4a4a4a)), 0, openShare.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
        opensharess.setSpan(styleSpan_B, 0, openShare.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        opensharess.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.texthint93)), openShare.length(), strOpenShare.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        opensharess.setSpan(new AbsoluteSizeSpan(17,true), 0, openShare.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        opensharess.setSpan(new AbsoluteSizeSpan(12, true), openShare.length(), strOpenShare.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(opensharess);
    }

    private void initDiscriblayout(int position, LinearLayout layout) {
        layout.removeAllViews();
        String company = mEntitys.get(position).FUND_COMPANY_NAME;
        String level = mEntitys.get(position).OFUND_RISKLEVEL_NAME;
        String status = mEntitys.get(position).FUND_STATUS_NAME;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = Helper.dip2px(mContext, 3);
        layoutParams.setMargins(0, 0, margin, 0);

        if (!TextUtils.isEmpty(company)) {
            TextView textView = new TextView(mContext);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.texts));
            textView.setTextSize(10);
            textView.setBackgroundResource(R.drawable.bg_tv_fundinfo);
            textView.setText(company);
            textView.setLayoutParams(layoutParams);
            layout.addView(textView);
        }

        if (!TextUtils.isEmpty(level)) {
            TextView textView = new TextView(mContext);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.texts));
            textView.setBackgroundResource(R.drawable.bg_tv_fundinfo);
            textView.setTextSize(10);
            textView.setText(level);
            textView.setLayoutParams(layoutParams);
            layout.addView(textView);
        }

        if (!TextUtils.isEmpty(status)) {
            TextView textView = new TextView(mContext);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.texts));
            textView.setBackgroundResource(R.drawable.bg_tv_fundinfo);
            textView.setTextSize(10);
            textView.setText(status);
            textView.setLayoutParams(layoutParams);
            layout.addView(textView);
        }

    }

    private String setString(String str) {
        String temStr = "";
        if (TextUtils.isEmpty(str)) {
            return temStr;
        }
        temStr = str;
        if(str.contains("e")||str.contains("E"))
        {
            temStr = getStringOutE(str);
        }
        if (!temStr.contains(".")) {
            return temStr;
        }

        String[] strs = temStr.split("\\.");
        String str1 = getString(strs[0]);
        String str2 = strs[1];
        temStr = str1 + "." + str2;

        return temStr;
    }

    private  String getStringOutE(String str){
        BigDecimal bd = new BigDecimal(str);
        return bd.toPlainString();
    }

    private  String getString(String str){
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }

    private class ViewHodler {

        TextView mNameCode;
        TextView mDetailTv;
        TextView mFundVal;
        TextView mOpenShare;
        LinearLayout mDiscrbLayout;

    }
}
