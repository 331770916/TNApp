package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.RiskTableEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wangqi on 2016/9/7.
 * 风险问题
 */
public class RiskEvaluationAdapter extends BaseAdapter {
    private Context context;
    List<RiskTableEntity> riskTableBeans;
    int height;
    RiskChoose riskChoose;
    ViewHolder holder;
    ViewHolder1 holder1;
    int count = 0;
    boolean flag = true;
    public RiskEvaluationAdapter(Context context) {
        this.context = context;
    }

    public void setRiskTableBeans(List<RiskTableEntity> riskTableBeans) {
        this.riskTableBeans = riskTableBeans;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCallBack(RiskChoose riskChoose) {
        this.riskChoose = riskChoose;
    }

    @Override
    public int getCount() {
        if (riskTableBeans != null && riskTableBeans.size() > 0) {
            return riskTableBeans.size();
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
        int flag = 0;
        if ("0".equals(riskTableBeans.get(position).QUESTION_KIND)) {
            flag = 0;
        } else if ("1".equals(riskTableBeans.get(position).QUESTION_KIND)) {
            flag = 1;
        }

        return flag;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == 0) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_risk_text, null);
            holder.tv_question = (TextView) convertView.findViewById(R.id.tv_question);
            holder.rg_choose = (RadioGroup) convertView.findViewById(R.id.rg_choose);
            holder.rb_num1 = (RadioButton) convertView.findViewById(R.id.rb_num1);
            holder.rb_num2 = (RadioButton) convertView.findViewById(R.id.rb_num2);
            holder.rb_num3 = (RadioButton) convertView.findViewById(R.id.rb_num3);
            holder.rb_num4 = (RadioButton) convertView.findViewById(R.id.rb_num4);
            holder.rb_num5 = (RadioButton) convertView.findViewById(R.id.rb_num5);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, height);
            convertView.setLayoutParams(layoutParams);

            settintInstaceChoick(position);
        } else {
            holder1 = new ViewHolder1();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_risk_morchoice, null);
            holder1.tv_more_question = (TextView) convertView.findViewById(R.id.tv_more_question);
            holder1.mLayout = (LinearLayout) convertView.findViewById(R.id.ll_more_layout);
            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, height);
            convertView.setLayoutParams(params);

            holder1.tv_more_question.setText("" + riskTableBeans.get(position).ORDER_NO + "," + riskTableBeans.get(position).QUESTION_CONTENT);
            if (riskTableBeans.get(position) != null && riskTableBeans.get(position).OPTION_ANSWER != null
                    && riskTableBeans.get(position).OPTION_ANSWER.size() > 0) {

                int size = riskTableBeans.get(position).OPTION_ANSWER.size();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.width = layoutParams.WRAP_CONTENT;
                layoutParams.height = 0;
                layoutParams.weight = 1;
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    final String index = riskTableBeans.get(position).OPTION_ANSWER.get(i).ANSWER_NO;
                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setText(riskTableBeans.get(position).OPTION_ANSWER.get(i).ANSWER_CONTENT);
                    checkBox.setLayoutParams(layoutParams);

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                sb.append(index);
                            } else {
                                if (!TextUtils.isEmpty(sb.toString()) && sb.toString().contains(index)) {
                                    sb.deleteCharAt(sb.indexOf(index));
                                }
                            }
                        }
                    });

                    holder1.mLayout.addView(checkBox, i);
                }

                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams1.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                final Button button = new Button(context);
                button.setBackgroundResource(R.color.calendarBtnColor);
                button.setTextColor(context.getResources().getColor(R.color.white));
                button.setGravity(Gravity.CENTER);
                int padding = Helper.dip2px(context, 10);
                button.setPadding(padding, padding, padding, padding);
                button.setText("下一题");
                button.setLayoutParams(layoutParams1);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(sb.toString())) {

                            if (position == riskTableBeans.size() -1) {
                                button.setVisibility(View.GONE);
                            }

                            delayTime(Integer.parseInt(sb.toString()), position);
                        } else {
                            CentreToast.showText(context, "请选择答案再提交");
                        }

                    }
                });
                holder1.mLayout.addView(button);
            }
        }
        return convertView;
    }

    public void delayTime(final int num, final int position) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = true;

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                riskChoose.flag(true);
                riskChoose.position(num, position);
            }
        }.execute();
    }

    private void settintInstaceChoick(final int position) {
        holder.tv_question.setText("" + riskTableBeans.get(position).ORDER_NO + "," + riskTableBeans.get(position).QUESTION_CONTENT);

        holder.rg_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (flag) {
                    flag = false;
                    switch (checkedId) {
                        case R.id.rb_num1:
                            delayTime(0, position);
                            break;
                        case R.id.rb_num2:
                            delayTime(1, position);
                            break;
                        case R.id.rb_num3:
                            delayTime(2, position);
                            break;
                        case R.id.rb_num4:
                            delayTime(3, position);
                            break;
                        case R.id.rb_num5:
                            delayTime(4, position);
                            break;
                    }
                }
            }
        });
        switch (riskTableBeans.get(position).OPTION_ANSWER.size() - 1) {
            case 0:
                holder.rb_num1.setText(riskTableBeans.get(position).OPTION_ANSWER.get(0).ANSWER_CONTENT);
                holder.rb_num2.setVisibility(View.GONE);
                holder.rb_num3.setVisibility(View.GONE);
                holder.rb_num4.setVisibility(View.GONE);
                holder.rb_num5.setVisibility(View.GONE);

                break;
            case 1:
                holder.rb_num1.setText(riskTableBeans.get(position).OPTION_ANSWER.get(0).ANSWER_CONTENT);
                holder.rb_num2.setText(riskTableBeans.get(position).OPTION_ANSWER.get(1).ANSWER_CONTENT);
                holder.rb_num3.setVisibility(View.GONE);
                holder.rb_num4.setVisibility(View.GONE);
                holder.rb_num5.setVisibility(View.GONE);
                break;
            case 2:
                holder.rb_num1.setText(riskTableBeans.get(position).OPTION_ANSWER.get(0).ANSWER_CONTENT);
                holder.rb_num2.setText(riskTableBeans.get(position).OPTION_ANSWER.get(1).ANSWER_CONTENT);
                holder.rb_num3.setText(riskTableBeans.get(position).OPTION_ANSWER.get(2).ANSWER_CONTENT);
                holder.rb_num4.setVisibility(View.GONE);
                holder.rb_num5.setVisibility(View.GONE);
                break;
            case 3:
                holder.rb_num1.setText(riskTableBeans.get(position).OPTION_ANSWER.get(0).ANSWER_CONTENT);
                holder.rb_num2.setText(riskTableBeans.get(position).OPTION_ANSWER.get(1).ANSWER_CONTENT);
                holder.rb_num3.setText(riskTableBeans.get(position).OPTION_ANSWER.get(2).ANSWER_CONTENT);
                holder.rb_num4.setText(riskTableBeans.get(position).OPTION_ANSWER.get(3).ANSWER_CONTENT);
                holder.rb_num5.setVisibility(View.GONE);
                break;
            case 4:
                holder.rb_num1.setText(riskTableBeans.get(position).OPTION_ANSWER.get(0).ANSWER_CONTENT);
                holder.rb_num2.setText(riskTableBeans.get(position).OPTION_ANSWER.get(1).ANSWER_CONTENT);
                holder.rb_num3.setText(riskTableBeans.get(position).OPTION_ANSWER.get(2).ANSWER_CONTENT);
                holder.rb_num4.setText(riskTableBeans.get(position).OPTION_ANSWER.get(3).ANSWER_CONTENT);
                holder.rb_num5.setText(riskTableBeans.get(position).OPTION_ANSWER.get(4).ANSWER_CONTENT);
                break;
        }
    }

    class ViewHolder {
        public TextView tv_question;
        public RadioGroup rg_choose;
        public RadioButton rb_num1;
        public RadioButton rb_num2;
        public RadioButton rb_num3;
        public RadioButton rb_num4;
        public RadioButton rb_num5;
    }

    class ViewHolder1 {
        TextView tv_more_question;
        LinearLayout mLayout;
    }

    public interface RiskChoose {
        void flag(Boolean flag);

        void position(int point, int position);
    }
}

