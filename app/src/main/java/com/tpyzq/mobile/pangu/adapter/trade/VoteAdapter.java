package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.VoteDetailActivity;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;

import java.util.List;


/**
 * 股票   投票界面  Adapter
 */

public class VoteAdapter extends BaseAdapter{
    private List<NetworkVotingEntity> mList;
    private LayoutInflater layoutInflater;
    private NetworkVotingEntity bean;
    private Context mContext;

    public VoteAdapter(Context context) {
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
        VoteAdapter.ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = layoutInflater.inflate(R.layout.item_vote_list, parent, false);
            viewHodler.tv1 = (TextView)convertView.findViewById(R.id.voteNameTv);
            viewHodler.button = (Button)convertView.findViewById(R.id.voteTitleBt);
            viewHodler.tv2 = (TextView)convertView.findViewById(R.id.voteContent1);
            viewHodler.tv3 = (TextView)convertView.findViewById(R.id.voteContent2);
            viewHodler.tv4 = (TextView)convertView.findViewById(R.id.voteContent3);
            viewHodler.tv5 = (TextView)convertView.findViewById(R.id.voteContent4);
            viewHodler.tv6 = (TextView)convertView.findViewById(R.id.voteContent5);
            viewHodler.tv7 = (TextView)convertView.findViewById(R.id.voteContent6);
            viewHodler.tv8 = (TextView)convertView.findViewById(R.id.voteContent7);
            convertView.setTag(viewHodler);
        }else
            viewHodler = (ViewHodler) convertView.getTag();
        bean = mList.get(position);
        viewHodler.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VoteDetailActivity.class);
                intent.putExtra("meeting_seq",bean.getMeeting_seq());//股东大会编码
                intent.putExtra("company_code",bean.getCompany_code());//证卷代码
                intent.putExtra("stock_account",bean.getStock_account());//股东代码
                intent.putExtra("exchange_type",bean.getExchange_type());//市场
                mContext.startActivity(intent);
            }
        });
        viewHodler.tv1.setText(bean.getCompany_name()+"股东大会");
        viewHodler.tv2.setText(bean.getMeeting_name());
        viewHodler.tv3.setText(bean.getMeeting_seq());
        viewHodler.tv4.setText(bean.getCompany_name());
        viewHodler.tv5.setText(bean.getCompany_code());
        viewHodler.tv6.setText(bean.getBegin_date());
        viewHodler.tv7.setText(bean.getEnd_date());
        if(!TextUtils.isEmpty(bean.getInit_date())) {
            viewHodler.tv8.setText(bean.getInit_date());
        }else
            viewHodler.tv8.setText("--");
        return convertView;
    }

    class ViewHodler {
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
        Button button;
    }
}
