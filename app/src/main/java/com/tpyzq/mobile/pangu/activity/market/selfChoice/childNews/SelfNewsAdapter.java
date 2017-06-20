package com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.NewsInofEntity;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/17.
 *
 */
public class SelfNewsAdapter extends BaseAdapter {

    private ArrayList<NewsInofEntity> mDatas;

    public void setDatas(ArrayList<NewsInofEntity> datas) {
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

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_self_news, null);
            viewHolder.newsFromTitle = (TextView) convertView.findViewById(R.id.newsFromTitle);
            viewHolder.newsHotIv = (ImageView) convertView.findViewById(R.id.news_item_iv1);
            viewHolder.newsHoldIv = (ImageView) convertView.findViewById(R.id.news_item_iv2);
            viewHolder.newsFromTime = (TextView) convertView.findViewById(R.id.newsFromTime);
            viewHolder.newsFromSubject = (TextView) convertView.findViewById(R.id.newsFromSubject);
            viewHolder.newsFromContent = (TextView) convertView.findViewById(R.id.newsFromContent);
            viewHolder.newsFromTimeName = (TextView) convertView.findViewById(R.id.newsFromTimeName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.newsFromTitle.setText("");
        viewHolder.newsFromTime.setText("");
        viewHolder.newsFromTimeName.setText("");
        viewHolder.newsFromSubject.setText("");
        viewHolder.newsFromContent.setText("");


        String comp = "";
        if (!TextUtils.isEmpty(mDatas.get(position).getComp())) {
            comp = mDatas.get(position).getComp();
        }

        String tick = "";
        if (!TextUtils.isEmpty(mDatas.get(position).getTick())) {
            tick = mDatas.get(position).getTick();
        }

        viewHolder.newsFromTitle.setText(comp + "(" + tick + ")");
        viewHolder.newsFromTime.setText(Helper.getTimeByTimeC(String.valueOf(mDatas.get(position).getDt())));

        String auth = "";
        if (!TextUtils.isEmpty(mDatas.get(position).getAuth())) {
            auth = mDatas.get(position).getAuth();
        }
        viewHolder.newsFromTimeName.setText(auth);

        String title = "";
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            title = mDatas.get(position).getTitle();
        }
        viewHolder.newsFromSubject.setText(title);

        String sum = "";
        if (!TextUtils.isEmpty(mDatas.get(position).getSum())) {
            sum = mDatas.get(position).getSum();
        }

        viewHolder.newsFromContent.setText(sum);

        String appearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");

        boolean isHold = false;
        if (!TextUtils.isEmpty(mDatas.get(position).getStockCode())) {
            isHold = HOLD_SEQ.getCode(mDatas.get(position).getStockCode());
        }
        if (isHold && "true".equals(appearHold)) {
            viewHolder.newsHoldIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.newsHoldIv.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView newsFromTitle;
        TextView newsFromTime;
        ImageView newsHoldIv;
        ImageView newsHotIv;
        TextView newsFromSubject;
        TextView newsFromContent;
        TextView newsFromTimeName;
    }
}
