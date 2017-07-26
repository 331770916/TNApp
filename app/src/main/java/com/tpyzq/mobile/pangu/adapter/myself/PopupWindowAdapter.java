package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;

import java.util.List;

/**
 * Created by wangqi on 2016/8/18.
 * PopupWindow  Adapter
 */
public class PopupWindowAdapter extends BaseAdapter {
    Context mContext;
    List<String> mList;

    public PopupWindowAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<String> List) {
        mList = List;
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
        if (mList.get(position) != null && mList.size() > 0) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_item, null);
            viewHolder.ItemAccount = (TextView) convertView.findViewById(R.id.ItemAccount);
            viewHolder.ItemDelete = (ImageView) convertView.findViewById(R.id.ItemDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ItemAccount.setText(mList.get(position));

        if (mList.get(position).equals(UserUtil.capitalAccount)){
            viewHolder.ItemDelete.setVisibility(View.INVISIBLE);
        }
        viewHolder.ItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size() <= position || position < 0)
                    return;
                Db_PUB_USERS.delTradeId(mList.get(position));
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView ItemAccount;
        ImageView ItemDelete;
    }
}
