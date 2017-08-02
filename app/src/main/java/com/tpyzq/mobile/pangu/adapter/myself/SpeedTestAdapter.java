package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.SpeedTestEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 陈新宇 on 2016/12/9.
 */

public class SpeedTestAdapter extends BaseAdapter {
    private Context context;
    private List<SpeedTestEntity> speedTestBeen;
    private SpeedCallBack speedCallBack;

    public SpeedTestAdapter(Context context, List<SpeedTestEntity> speedTestBeen, SpeedCallBack speedCallBack) {
        this.context = context;
        this.speedCallBack = speedCallBack;
        this.speedTestBeen = speedTestBeen;
    }

    @Override
    public int getCount() {
        if (speedTestBeen != null && speedTestBeen.size() > 0) {
            return speedTestBeen.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_speed, null);
            holder.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
            holder.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
            holder.cb_speed = (CheckBox) convertView.findViewById(R.id.cb_speed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cb_speed.setChecked(false);

        String ip = "";
        final SpeedTestEntity speedTestEntity = speedTestBeen.get(position);
        String port = speedTestEntity.version_port;
        if ("80".equals(port) || TextUtils.isEmpty(port)) {
            ip = speedTestEntity.version_ip;
        } else {
            ip = speedTestEntity.version_ip + ":" + port;
        }
        final String url = ip;
        holder.cb_speed.setChecked(speedTestEntity.isChecked);
        holder.tv_text1.setText(speedTestEntity.version_name);
        String status = speedTestEntity.version_status;
        holder.tv_text2.setText(status);
        if ("优".equals(status)) {
            holder.tv_text2.setTextColor(ColorUtils.RED);
        } else {
            holder.tv_text2.setTextColor(ColorUtils.GREEN);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < speedTestBeen.size(); i++) {
                    if (i != position) {
                        speedTestBeen.get(i).isChecked = false;
                    } else {
                        speedTestBeen.get(i).isChecked = true;
                        speedCallBack.setUrl(speedTestEntity.version_name,url);
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    class ViewHolder {
        public TextView tv_text1;
        public TextView tv_text2;
        public CheckBox cb_speed;
    }

    public interface SpeedCallBack {
        void setUrl(String name, String url);
    }
}
