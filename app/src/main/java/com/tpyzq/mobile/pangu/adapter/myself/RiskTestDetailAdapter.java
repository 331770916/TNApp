package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/6/27.
 * 风险承受能力测评详情Adapter
 */

public class RiskTestDetailAdapter extends BaseAdapter {

    private List<Map<String, Object>> mDatas;
    private Context mContext;
    private ViewHolder mHolder;
    public RiskTestDetailAdapter (Context context) {
        mHolder = new ViewHolder();
        mContext = context;
    }

    public void setDatas(List<Map<String, Object>> datas) {
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
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_risktest_detail, null);
            mHolder.questionTv = (TextView) convertView.findViewById(R.id.item_answers_question);
            mHolder.answersLayout = (LinearLayout) convertView.findViewById(R.id.item_answers_layout);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.questionTv.setText(null);
        mHolder.answersLayout.removeAllViews();


        String question_kind = (String) mDatas.get(position).get("question_kind");
        String question = (String) mDatas.get(position).get("question");
        String answer_history = (String) mDatas.get(position).get("answer_history");
        List<Map<String,String>> answers = (List<Map<String,String>>) mDatas.get(position).get("subData");


        mHolder.questionTv.setText(question);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = Helper.dip2px(mContext, 10);
        layoutParams.setMargins(margin, 0, 0, 0);

        if ("0".equals(question_kind)) {
            if (answers != null && answers.size() > 0){
                for (int i = 0; i < answers.size(); i++) {
                    Map<String, String> answer = answers.get(i);
                    RadioButton radioButton = new RadioButton(mContext);
//                    Drawable drawableAdd = mContext.getResources().getDrawable(R.drawable.risk_radio_bg);
//                    drawableAdd.setBounds(20, 20, 20, 20);
//                    radioButton.setCompoundDrawables(drawableAdd, null, null, null);
                    radioButton.setButtonDrawable(R.drawable.radio_button);
                    radioButton.setPadding(10, 10, 10, 10);
                    radioButton.setGravity(Gravity.TOP);
                    radioButton.setText(answer.get("answer_content"));
                    radioButton.setFocusable(false);
                    radioButton.setClickable(false);
                    radioButton.setFocusableInTouchMode(false);
                    if (!("" + (i+1)).equals(answer_history)) {
                        radioButton.setChecked(false);
                    } else {
                        radioButton.setChecked(true);

                    }
                    radioButton.setLayoutParams(layoutParams);
                    mHolder.answersLayout.addView(radioButton);
                }
            }

        } else if ("1".equals(question_kind)) {
            if (answers != null && answers.size() > 0){
                for (int i = 0; i < answers.size(); i++) {
                    Map<String, String> answer = answers.get(i);
                    CheckBox checkBox = new CheckBox(mContext);
                    checkBox.setButtonDrawable(R.drawable.update_idcard_checkbox);
                    checkBox.setPadding(10, 10, 10, 10);
                    checkBox.setText(answer.get("answer_content"));
                    checkBox.setFocusable(false);
                    checkBox.setClickable(false);
                    checkBox.setFocusableInTouchMode(false);



                    if (!answer_history.contains("" + (i+1))) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }

                    checkBox.setLayoutParams(layoutParams);
                    mHolder.answersLayout.addView(checkBox);
                }
            }
        }


        return convertView;
    }

    class ViewHolder {
        TextView questionTv;
        LinearLayout answersLayout;
    }

}
