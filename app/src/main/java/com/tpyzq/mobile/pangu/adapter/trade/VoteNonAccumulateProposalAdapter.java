package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tpyzq.mobile.pangu.R;

/**
 * 非累积投票议案 Adapter
 */

public class VoteNonAccumulateProposalAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public VoteNonAccumulateProposalAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_vote_non_accumulate_proposal, parent, false);
        return convertView;
    }
}
