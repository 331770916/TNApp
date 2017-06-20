package com.tpyzq.mobile.pangu.adapter.trade;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.PanguParameters;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/9/5.
 * 资金收集Adapter
 */
public class PriceCollectionAdapter extends BaseAdapter {

    private ArrayList<BankAccountEntity> mDatas ;
    private static final String TAG = "PriceCollectionAdapter";
    private UpdateCollectionData mUpdateCollectionData;
    private AfterTextChangedListener mAfterTextChangedListener;

    public void setData(ArrayList<BankAccountEntity> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void setAfterTextChangedListener(AfterTextChangedListener afterTextChangedListener) {
        mAfterTextChangedListener = afterTextChangedListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.collection_adapter, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.editSelfAllcheckBox);
        TextView bankInfo = (TextView) convertView.findViewById(R.id.bankInfo);
        EditText inputEdit = (EditText) convertView.findViewById(R.id.collection_input);
        TextView inputUnit = (TextView) convertView.findViewById(R.id.collection_unit);
        TextView balance = (TextView) convertView.findViewById(R.id.collection_useful);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mDatas.get(position).setCollected(true);
                } else {
                    mDatas.get(position).setCollected(false);
                }


                if (mUpdateCollectionData != null) {
                    mUpdateCollectionData.updateDatas(mDatas);
                }
            }

        });

        checkBox.setChecked(mDatas.get(position).getCollected());
        String content1 = "";

        int fundAccountLength = 0;
        int bankAccountLength = 0;

        if (!TextUtils.isEmpty(mDatas.get(position).getFUND_ACCOUNT())) {
            content1 = mDatas.get(position).getFUND_ACCOUNT()+ "\u3000\u3000";
            fundAccountLength = content1.length();
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getBANK_NAME())) {
            content1 = content1 + "" + mDatas.get(position).getBANK_NAME();
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getBANK_ACCOUNT())) {
            content1 = content1 + "(" + Helper.getBanksAccountNumber(mDatas.get(position).getBANK_ACCOUNT()) + ")";
            bankAccountLength = content1.length();
        }

        SpannableString ss = new SpannableString(content1);

        ss.setSpan(new TextAppearanceSpan(CustomApplication.getContext(), R.style.collectCollor),0, fundAccountLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int res = PanguParameters.getBankBgLogo().get(mDatas.get(position).getBANK_NO());

        Drawable drawableIv = ContextCompat.getDrawable(CustomApplication.getContext(), res);

        drawableIv.setBounds(0,0,drawableIv.getIntrinsicWidth()/3,drawableIv.getIntrinsicHeight()/3);
        ss.setSpan(new ImageSpan(drawableIv), fundAccountLength - 1, fundAccountLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new TextAppearanceSpan(CustomApplication.getContext(), R.style.collectCollor1),fundAccountLength, bankAccountLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bankInfo.setText(ss);

        inputUnit.setText("元");

        if (!TextUtils.isEmpty(mDatas.get(position).getFETCH_BALANCE())) {
            balance.setText("可用金额: " + mDatas.get(position).getFETCH_BALANCE() + "元");
            inputEdit.addTextChangedListener(new TextChangedListene(inputEdit, mDatas.get(position),
                    mAfterTextChangedListener, position));
        }

        return convertView;
    }

    public void setDatasListener(UpdateCollectionData updateCollectionData) {
        mUpdateCollectionData = updateCollectionData;
    }

    public interface UpdateCollectionData {
        void updateDatas(ArrayList<BankAccountEntity> datas);
    }

    private class TextChangedListene implements TextWatcher {

        private EditText mEditText;
        private AfterTextChangedListener mAfterTextChangedListener;
        private BankAccountEntity mEntity;
        private String mBalance;
        private int mPosition;
        private double MIN_VALUE = 0d;
        private double MAX_VALUE = 0d;

        public TextChangedListene(EditText editText, BankAccountEntity entity,
                                  AfterTextChangedListener afterTextChangedListener, int position) {
            mEditText = editText;
            mEntity = entity;
            mPosition = position;
            mBalance = mEntity.getFETCH_BALANCE();
            mAfterTextChangedListener = afterTextChangedListener;

            if (!TextUtils.isEmpty(mEntity.getOccurBalance())) {
                mEditText.setText(mEntity.getOccurBalance());
            } else {
                mEntity.setOccurBalance(mBalance);
                mEditText.setText(mBalance);
                MAX_VALUE = Double.valueOf(mBalance);
            }



            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mBalance.length())});
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (start > 1) {
                if (!TextUtils.isEmpty(s)) {
                    double _balance = Double.valueOf(s.toString());

                    if (_balance > MAX_VALUE) {
                        s = String.valueOf(MAX_VALUE);
                        mEditText.setText(s);
                    } else if (_balance < MIN_VALUE) {
                        s = String.valueOf(MIN_VALUE);

                        return;
                    }

                }
            }

        }

            @Override
            public void afterTextChanged (Editable s){

                double _banlance = 0;
                if (!TextUtils.isEmpty(mBalance)) {
                    _banlance = Double.valueOf(mBalance);
                } else {
                    _banlance = Double.valueOf(0);
                }

                double inputEditable = 0d;
                if (!TextUtils.isEmpty(s)) {
                    inputEditable = Double.parseDouble(s.toString());
                } else {
                    inputEditable = Double.valueOf(0);
                }

                if (inputEditable > _banlance) {
                    Helper.getInstance().showToast(CustomApplication.getContext(), "调拨资金不能大于可用资金");
                    mEditText.setText(String.valueOf(MAX_VALUE));
                    return;
                }

                if (mAfterTextChangedListener != null) {
                    mAfterTextChangedListener.afterTextChanged(s.toString(), mPosition);
                }
            }

    }

    public interface AfterTextChangedListener{
        public void afterTextChanged(String text, int position);
    }
}
