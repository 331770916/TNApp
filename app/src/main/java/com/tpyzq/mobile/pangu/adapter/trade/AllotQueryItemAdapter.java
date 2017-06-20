package com.tpyzq.mobile.pangu.adapter.trade;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/9/6.
 */
public class AllotQueryItemAdapter extends BaseAdapter {

    private ArrayList<BankAccountEntity> mDatas;
    private SimpleDateFormat mSimpleDateFormat;
    private SimpleDateFormat sdf;

    public AllotQueryItemAdapter() {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf =  new SimpleDateFormat("yyyyMMdd HHmmss");
    }

    public void setDatas(ArrayList<BankAccountEntity> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodle viewHodle = null;
        if (convertView == null) {
            viewHodle = new ViewHodle();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_allotquery_item, null);

            viewHodle.allotPrice = (TextView) convertView.findViewById(R.id.allotPrice);
            viewHodle.allotTime = (TextView) convertView.findViewById(R.id.allotTime);
            viewHodle.destAccount = (TextView) convertView.findViewById(R.id.destAccount);
            viewHodle.destBank = (TextView) convertView.findViewById(R.id.destBank);
            viewHodle.srcAccount = (TextView) convertView.findViewById(R.id.srcAccount);
            viewHodle.srcBank = (TextView) convertView.findViewById(R.id.srcBank);

            convertView.setTag(viewHodle);
        } else {
            viewHodle = (ViewHodle) convertView.getTag();
        }

        viewHodle.allotPrice.setText(null);
        viewHodle.allotTime.setText(null);
        viewHodle.destAccount.setText(null);
        viewHodle.destBank.setText(null);
        viewHodle.srcAccount.setText(null);
        viewHodle.srcBank.setText(null);

        if (!TextUtils.isEmpty(mDatas.get(position).getOCCUR_BALANCE())) {
            viewHodle.allotPrice.setText("人民币:" + mDatas.get(position).getOCCUR_BALANCE() + "元");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getINIT_DATE()) && !TextUtils.isEmpty(mDatas.get(position).getCURR_TIME())) {
            try {
//                Date date = sdf.parse("" + mDatas.get(position).getINIT_DATE()  + " " + mDatas.get(position).getCURR_TIME());
//                viewHodle.allotTime.setText(mSimpleDateFormat.format(date));
                viewHodle.allotTime.setText(Helper.getMyDateY_M_D(mDatas.get(position).getINIT_DATE()) + " " +  Helper.getMyDateHMS(mDatas.get(position).getCURR_TIME()));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getFUND_ACCOUNT_SRC())) {
            String destAccount = mDatas.get(position).getFUND_ACCOUNT_SRC();
            viewHodle.destAccount.setText("转出账号:" + destAccount);
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getBANK_NAME_SRC())) {

            String account = "(null)";
            if (!TextUtils.isEmpty(mDatas.get(position).getBANK_ACCOUNT_SRC())){
                account = mDatas.get(position).getBANK_ACCOUNT_SRC();

                viewHodle.destBank.setText(mDatas.get(position).getBANK_NAME_SRC() + "(" + "**** " + account.substring(account.length() - 4, account.length()) + ")");
            } else {

                viewHodle.destBank.setText(mDatas.get(position).getBANK_NAME_SRC() +  "(" + "**** " + account + ")");
            }

        }

        if (!TextUtils.isEmpty(mDatas.get(position).getFUND_ACCOUNT_DEST())) {
            String srcAccount = mDatas.get(position).getFUND_ACCOUNT_DEST();
            viewHodle.srcAccount.setText("转入账号:" + srcAccount);
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getBANK_NAME_DEST())) {

            String account = "(null)";
            if (!TextUtils.isEmpty(mDatas.get(position).getBANK_ACCOUNT_DEST())){
                account = mDatas.get(position).getBANK_ACCOUNT_DEST();

                viewHodle.srcBank.setText(mDatas.get(position).getBANK_NAME_DEST() + "(" + "**** " + account.substring(account.length() - 4, account.length()) + ")");
            } else {
                viewHodle.srcBank.setText(mDatas.get(position).getBANK_NAME_DEST() +  "(" + "**** " + account + ")");
            }



        }

        return convertView;
    }

    private class ViewHodle {
        TextView allotPrice;
        TextView allotTime;
        TextView destAccount;
        TextView destBank;
        TextView srcAccount;
        TextView srcBank;
    }
}
