package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.text.DecimalFormat;

/**
 * Created by zhangwenbo on 2016/10/7.
 * 计算机dialog
 */
public class CounterDialog {


    public static Dialog showDialog(Activity activity, float persent) {

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LayoutInflater inflater = LayoutInflater.from(CustomApplication.getContext());
        final View view = inflater.inflate(R.layout.dialog_counter, null);

        int minWidth = (int) (width * 0.7);
        int minHeight = (int)(height * 0.4);

        view.setMinimumWidth(minWidth);
        view.setMinimumHeight(minHeight);

        EditText price = (EditText) view.findViewById(R.id.counterPrice);
        EditText day =  (EditText) view.findViewById(R.id.counterDay);
        TextView savePrice = (TextView) view.findViewById(R.id.counterSavePrice);

        PriceWatcher priceWatcher = new PriceWatcher(savePrice, day, persent);
        DayWatcher dayWatcher = new DayWatcher(savePrice, price, persent);

        price.addTextChangedListener(priceWatcher);
        day.addTextChangedListener(dayWatcher);


        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        view.findViewById(R.id.counter_dissmisiv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog(dialog);
            }
        });

        dialog.show();

        return  dialog;
    }

    public static void closeDialog(Dialog dialog){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    private static class PriceWatcher implements TextWatcher {

        private TextView mTextView;
        private EditText mDay;
        private float mPersent;
        private DecimalFormat mFormat1;
        public  PriceWatcher (TextView textView, EditText day, float persent) {
            mTextView = textView;
            mDay = day;
            mPersent = persent;
            mFormat1 = new DecimalFormat("#0.00");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            float price = 0f;
            if (!TextUtils.isEmpty(s.toString())) {
                price = Float.valueOf(s.toString());
            }

            int day = 0;
            if (!TextUtils.isEmpty(mDay.getText())) {
                day = Integer.parseInt(mDay.getText().toString());
            }

            if (day > 0) {
                float totalPrice = price * (mPersent * day / 365);
                mTextView.setText(mFormat1.format(totalPrice));
            }
        }
    }

    private static class DayWatcher implements TextWatcher {

        private TextView mTextView;
        private EditText mPrice;
        private float mPersent;
        private DecimalFormat mFormat1;
        public  DayWatcher (TextView textView, EditText price, float persent) {
            mTextView = textView;
            mPrice = price;
            mPersent = persent;
            mFormat1 = new DecimalFormat("#0.00");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int day = 0;
            if (!TextUtils.isEmpty(s.toString())) {
                day = Integer.parseInt(s.toString());
            } else {
                mTextView.setText("");
                return;
            }

            float price = 0f;
            if (!TextUtils.isEmpty(mPrice.getText())) {
                price = Float.valueOf(mPrice.getText().toString());
            } else {
                mTextView.setText("");
                return;
            }

            if (day > 0) {
                float totalPrice = price * (mPersent * day / 365);
                mTextView.setText(mFormat1.format(totalPrice));
            }

        }
    }


}
