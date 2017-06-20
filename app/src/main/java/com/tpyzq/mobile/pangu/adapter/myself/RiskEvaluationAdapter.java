package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.RiskTableEntity;

import java.util.List;


/**
 * Created by wangqi on 2016/9/7.
 * 风险问题
 */
public class RiskEvaluationAdapter extends BaseAdapter {
    private Context context;
    List<RiskTableEntity> riskTableBeans;
    int height;
    RiskChoose riskChoose;
    ViewHolder holder = null;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
        holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.item_risk_text, null);
        ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, height);
        convertView.setLayoutParams(layoutParams);
        holder.tv_question = (TextView) convertView.findViewById(R.id.tv_question);
        holder.rg_choose = (RadioGroup) convertView.findViewById(R.id.rg_choose);
        holder.rb_num1 = (RadioButton) convertView.findViewById(R.id.rb_num1);
        holder.rb_num2 = (RadioButton) convertView.findViewById(R.id.rb_num2);
        holder.rb_num3 = (RadioButton) convertView.findViewById(R.id.rb_num3);
        holder.rb_num4 = (RadioButton) convertView.findViewById(R.id.rb_num4);
        holder.rb_num5 = (RadioButton) convertView.findViewById(R.id.rb_num5);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
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
//
//    /**
//     * 选择 答案 点击事件设置
//     */
//    public void setClickFalse(int position) {
//        if (count >= riskTableBeans.size() -1) {
//            ToastUtils.showShort(context, position + "");
//            holder.rb_num1.setEnabled(false);
//            holder.rb_num2.setEnabled(false);
//            holder.rb_num3.setEnabled(false);
//            holder.rb_num4.setEnabled(false);
//            holder.rb_num5.setEnabled(false);
//        }
//    }


    class ViewHolder {
        public TextView tv_question;
        public RadioGroup rg_choose;
        public RadioButton rb_num1;
        public RadioButton rb_num2;
        public RadioButton rb_num3;
        public RadioButton rb_num4;
        public RadioButton rb_num5;
    }

    public interface RiskChoose {
        void flag(Boolean flag);

        void position(int point, int position);
    }
}

