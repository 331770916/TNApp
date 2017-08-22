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
    private DetailClickListener mListener;
    private ArrayList<FundSubsEntity> mEntitys;
    public FundInfoAdapter (Context context, boolean isShowDeatil) {
        mContext = context;
        this.isShowDeatil = isShowDeatil;
    }

    public void setDatas(ArrayList<FundSubsEntity> entitys) {
        mEntitys = entitys;
        notifyDataSetChanged();
    }

    public void setDetailClickListener(DetailClickListener listener) {
        mListener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_fundinfo, null);
            viewHodler.mNameCode = (TextView) convertView.findViewById(R.id.tv_name_code);
            viewHodler.mDetailTv = (TextView) convertView.findViewById(R.id.tv_detail);
            viewHodler.mFundVal = (TextView) convertView.findViewById(R.id.tv_fund_val);
            viewHodler.mOpenShare = (TextView) convertView.findViewById(R.id.tv_open_share);

            viewHodler.mDsiribcompany = (TextView) convertView.findViewById(R.id.tv_dsiribcompany);
            viewHodler.mDsiritype = (TextView) convertView.findViewById(R.id.tv_dsiritype);
            viewHodler.mDsiriblevel = (TextView) convertView.findViewById(R.id.tv_dsiriblevel);
            viewHodler.mDsiribstatus = (TextView) convertView.findViewById(R.id.tv_dsiribstatus);

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

        viewHodler.mDetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.detailClick(mEntitys.get(position));
                }
            }
        });

        initVal(position, viewHodler.mFundVal);
        initOpenShare(position, viewHodler.mOpenShare);

        String company = mEntitys.get(position).FUND_COMPANY_NAME;
        String type = mEntitys.get(position).OFUND_TYPE;
        String level = mEntitys.get(position).OFUND_RISKLEVEL_NAME;
        String status = mEntitys.get(position).FUND_STATUS_NAME;

        if (!TextUtils.isEmpty(company)) {
            viewHodler.mDsiribcompany.setText(company);
        } else {
            viewHodler.mDsiribcompany.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(company)) {
            viewHodler.mDsiritype.setText(type);
        } else {
            viewHodler.mDsiritype.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(company)) {
            viewHodler.mDsiriblevel.setText(level);
        } else {
            viewHodler.mDsiriblevel.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(company)) {
            viewHodler.mDsiribstatus.setText(status);
        } else {
            viewHodler.mDsiribstatus.setVisibility(View.GONE);
        }

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
        TextView mDsiribcompany;
        TextView mDsiritype;
        TextView mDsiriblevel;
        TextView mDsiribstatus;

    }

    public interface DetailClickListener {
        public void detailClick(FundSubsEntity entity);
    }
}
