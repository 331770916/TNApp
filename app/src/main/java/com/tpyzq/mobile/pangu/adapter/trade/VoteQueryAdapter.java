package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;

import java.util.List;

/**
 * 投票查询Adapter
 */

public class VoteQueryAdapter extends BaseAdapter implements View.OnClickListener {
    private List<NetworkVotingEntity> mList;
    private Context context;
    private int type;
    private ScallCallback callback;
    private NetworkVotingEntity bean;

    public VoteQueryAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void setCallback(ScallCallback callback) {
        this.callback = callback;
    }


    public void setData(List<NetworkVotingEntity> setText) {
        this.mList = setText;
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
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_vote_query, parent, false);
            viewHodler.llContent = (LinearLayout) convertView.findViewById(R.id.fj_content);
            viewHodler.tableSpan = (View) convertView.findViewById(R.id.tableSpan);

            viewHodler.mCodeConference = (TextView) convertView.findViewById(R.id.codeconference);
            viewHodler.mStockCode = (TextView) convertView.findViewById(R.id.stockcode);
            viewHodler.mEncode = (TextView) convertView.findViewById(R.id.encode);
            viewHodler.mStatus = (TextView) convertView.findViewById(R.id.status);
            // 点击显示隐藏部分
            viewHodler.tvGone1 = (TextView) convertView.findViewById(R.id.TextView2);
            viewHodler.tvGone2 = (TextView) convertView.findViewById(R.id.TextView4);
            viewHodler.tvGone3 = (TextView) convertView.findViewById(R.id.TextView6);
            viewHodler.tvGone4 = (TextView) convertView.findViewById(R.id.TextView8);
            viewHodler.rlSpan = (ImageView) convertView.findViewById(R.id.deliveryImageView);

            ((TextView) convertView.findViewById(R.id.TextView1)).setText("委托日期");
            ((TextView) convertView.findViewById(R.id.TextView3)).setText("委托数量");
            ((TextView) convertView.findViewById(R.id.TextView5)).setText("证券名称");
            ((TextView) convertView.findViewById(R.id.TextView7)).setText("委托编号");
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        bean = mList.get(position);
        viewHodler.llContent.setOnClickListener(this);
        viewHodler.llContent.setTag(bean);
        viewHodler.rlSpan.setOnClickListener(this);
        viewHodler.rlSpan.setTag(bean);

        viewHodler.mCodeConference.setText(bean.getMeeting_seq());
        viewHodler.mStockCode.setText(bean.getStock_code());
        viewHodler.mEncode.setText(bean.getVote_motion());
        String statue = bean.getStatus();
        switch (statue){
            case "0":
                statue= "未报";
                break;
            case "1":
                statue= "待报";
                break;
            case "2":
                statue= "已报";
                break;
            case "3":
                statue= "已报待撤";
                break;
            case "4":
                statue= "部成待撤";
                break;
            case "5":
                statue= "部撤";
                break;
            case "6":
                statue= "已撤";
                break;
            case "7":
                statue= "部成";
                break;
            case "8":
                statue= "已成";
                break;
            case "9":
                statue= "废单";
                break;
            case "A":
                statue= "已报待改(港股)";
                break;
            case "B":
                statue= "预埋单撤单(港股)";
                break;
            case "C":
                statue= "正报";
                break;
            case "D":
                statue= "撤废";
                break;
            case "E":
                statue= "部成待改(港股)";
                break;
            case "F":
                statue= "预埋单废单(港股)";
                break;
            case "G":
                statue= "单腿成交";
                break;
            case "H":
                statue= "待审核(港股)";
                break;
            case "J":
                statue= "复核未通过(港股)";
                break;
            case "M":
                statue= "Wait for Confirming";
                break;
            case "U":
                statue= "已确认待撤";
                break;
        }
        viewHodler.mStatus.setText(statue);
        if (bean.isShowRule()) {
            if (viewHodler.llContent.isSelected()) {
                bean.setShowRule(false);
                viewHodler.llContent.setSelected(false);
                viewHodler.tableSpan.setVisibility(View.GONE);
            } else {
                bean.setShowRule(true);
                viewHodler.llContent.setSelected(true);
                viewHodler.tableSpan.setVisibility(View.VISIBLE);
                switch (type) {
                    case 0:
                        viewHodler.tvGone1.setText(bean.getInit_date());
                        viewHodler.tvGone2.setText(bean.getBusiness_amount());
                        viewHodler.tvGone3.setText(bean.getStock_name());
                        viewHodler.tvGone4.setText(bean.getEntrust_no());
                        break;
                }
            }
        } else {
            bean.setShowRule(false);
            viewHodler.llContent.setSelected(false);
            viewHodler.tableSpan.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        bean = (NetworkVotingEntity) v.getTag();
        if (bean.isShowRule())
            bean.setShowRule(false);
        else
            bean.setShowRule(true);
        notifyDataSetChanged();
        callback.callScall();
    }


    public interface ScallCallback {
        void callScall();
    }

    class ViewHodler {
        LinearLayout llContent;
        TextView mCodeConference, mStockCode, mEncode, mStatus, tvGone1, tvGone2, tvGone3, tvGone4;
        View tableSpan;
        ImageView rlSpan;
    }
}
