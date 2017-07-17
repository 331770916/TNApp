package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import android.widget.LinearLayout.LayoutParams;
import java.util.List;

/**
 * Created by ltyhome on 26/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:  网络投票详情Adapter
 */

public class VoteDetailAdapter extends BaseAdapter {
    private List<NetworkVotingEntity> mList;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public VoteDetailAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<NetworkVotingEntity> data) {
        mList = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NetworkVotingEntity bean = mList.get(position);
        if(!TextUtils.isEmpty(bean.getVote_type())) {
            switch (Integer.parseInt(bean.getVote_type())) {
                case 0: //非累积投票制
                    convertView = layoutInflater.inflate(R.layout.item_vote_accumulate_proposal, parent, false);
                    TextView tv1 = (TextView) convertView.findViewById(R.id.voteMotion);
                    tv1.setText(bean.getVote_motion());
                    TextView tv2 = (TextView) convertView.findViewById(R.id.voteInfo);
                    tv2.setText(bean.getVote_info());
                    TextView tv3 = (TextView) convertView.findViewById(R.id.voteYes);
                    tv3.setTextColor(Color.rgb(255, 255, 255));
                    tv3.setBackgroundColor(Color.rgb(54, 141, 231));
                    TextView tv4 = (TextView) convertView.findViewById(R.id.voteNo);
                    TextView tv5 = (TextView) convertView.findViewById(R.id.voteAbstention);
                    final TextView[] tvs = new TextView[]{tv3, tv4, tv5};
                    tv3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeColorAndBackgroud(tvs, 0);
                            bean.setEntrust_no("1");
                        }
                    });
                    tv4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeColorAndBackgroud(tvs, 1);
                            bean.setEntrust_no("2");
                        }
                    });
                    tv5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeColorAndBackgroud(tvs, 2);
                            bean.setEntrust_no("3");
                        }
                    });
                    break;
                case 1://累积投票制
                    convertView = layoutInflater.inflate(R.layout.item_vote_non_accumulate_proposal, parent, false);
                    TextView tv6 = (TextView) convertView.findViewById(R.id.voteTitle);
                    tv6.setText(bean.getVote_motion());
                    List<NetworkVotingEntity> list = bean.getList();
                    TextView tv7 = (TextView) convertView.findViewById(R.id.voteContent);
                    tv7.setText(bean.getVote_info() + "(当选人数：" + list.size() + ")");
                    LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.voteSubContent);
                    LayoutParams lp = new LayoutParams(-1,-2);
                    lp.topMargin = (int)mContext.getResources().getDimension(R.dimen.size5);
                    lp.leftMargin = (int)mContext.getResources().getDimension(R.dimen.size45);
                    lp.rightMargin = (int)mContext.getResources().getDimension(R.dimen.size45);
                    for (int i = 0; i < list.size(); i++)
                        ll.addView(getSubView(list.get(i)), lp);
                    break;
            }
        }
        return convertView;
    }

    private LinearLayout getSubView(final NetworkVotingEntity subBean){
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView tvTitle = new TextView(mContext);
        tvTitle.setTextColor(mContext.getResources().getColor(R.color.textss));
        tvTitle.setText(subBean.getVote_motion());
        ll.addView(tvTitle);

        TextView tvContent = new TextView(mContext);
        tvContent.setTextColor(mContext.getResources().getColor(R.color.textss));
        LayoutParams lps = new LayoutParams(-2,(int)mContext.getResources().getDimension(R.dimen.size80));
        lps.leftMargin = (int)mContext.getResources().getDimension(R.dimen.size10);
        lps.weight = 2;
        tvContent.setText(subBean.getVote_info());
        ll.addView(tvContent,lps);

        LayoutParams etlps = new LayoutParams((int)mContext.getResources().getDimension(R.dimen.size150),(int)mContext.getResources().getDimension(R.dimen.size40));
        final EditText etNum = new EditText(mContext);
        etNum.setBackgroundResource(R.mipmap.vote_et_style);
        etNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        etlps.leftMargin = (int)mContext.getResources().getDimension(R.dimen.size20);
        etlps.topMargin =  (int)mContext.getResources().getDimension(R.dimen.size15);
        etlps.weight = 1;
        ll.addView(etNum,etlps);
        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String s = etNum.getText().toString();
                if(s.length()>1&&s.charAt(0)=='0'){
                   subBean.setEntrust_no(Integer.valueOf(s.toString()).toString());
                }else
                    subBean.setEntrust_no(s);
            }
        });
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str=s.toString();
                if(s.length()>1&&s.charAt(0)=='0'){
                    Integer integer = Integer.valueOf(s.toString());
                    etNum.setText(integer.toString());
                    etNum.setSelection(integer.toString().length());
                    str = integer.toString();
                }
                subBean.setEntrust_no(str);
            }
        });
        return ll;
    }

    private void changeColorAndBackgroud(TextView[] tv,int index){
        for (int i = 0;i<tv.length;i++){
            if(i==index){//选中
                tv[i].setTextColor(Color.rgb(255,255,255));
                tv[i].setBackgroundColor(Color.rgb(54,141,231));//blue3
            }else{//非选中
                tv[i].setTextColor(Color.rgb(76,76,76));
                tv[i].setBackgroundColor(Color.rgb(229,229,229));//photoBg
            }
        }
    }
}
