package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;

import java.util.List;

/**
 * Created by ltyhome on 26/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:  网络投票详情Adapter
 */

public class VoteDetailAdapter extends BaseAdapter implements View.OnClickListener{
    private List<NetworkVotingEntity> mList;
    private LayoutInflater layoutInflater;
    private NetworkVotingEntity bean;
    private Context mContext;
    private int type = 0;

    public VoteDetailAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<NetworkVotingEntity> data) {
        mList = data;
        notifyDataSetChanged();
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
        bean = mList.get(position);
        if(!TextUtils.isEmpty(bean.getVote_type()))
            type = Integer.parseInt(bean.getVote_type());
        if (convertView == null) {
            switch (type){
                case 0: //非累积投票制
                    convertView = layoutInflater.inflate(R.layout.item_vote_non_accumulate_proposal, parent, false);
                    break;
                case 1://累积投票制
                    convertView = layoutInflater.inflate(R.layout.item_vote_accumulate_proposal, parent, false);
                    TextView tv1 = (TextView)convertView.findViewById(R.id.voteMotion);
                    TextView tv2 = (TextView)convertView.findViewById(R.id.voteInfo);
                    TextView tv3 = (TextView)convertView.findViewById(R.id.voteYes);
                    tv3.setSelected(true);
                    tv3.setOnClickListener(this);
                    TextView tv4 = (TextView)convertView.findViewById(R.id.voteNo);
                    tv4.setOnClickListener(this);
                    TextView tv5 = (TextView)convertView.findViewById(R.id.voteAbstention);
                    tv5.setOnClickListener(this);
                    tv1.setText(bean.getVote_motion());
                    tv2.setText(bean.getVote_info());
                    break;
            }
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.voteYes:

                break;
            case R.id.voteNo:

                break;
            case R.id.voteAbstention:

                break;
        }
    }
}
