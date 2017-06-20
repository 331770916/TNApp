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
    private List<CheckBox> checkBoxes;
    private SpeedCallBack speedCallBack;

    public SpeedTestAdapter(Context context, List<SpeedTestEntity> speedTestBeen, SpeedCallBack speedCallBack) {
        this.context = context;
        this.speedCallBack = speedCallBack;
        this.speedTestBeen = speedTestBeen;
        checkBoxes = new ArrayList<CheckBox>();
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
            checkBoxes.add(holder.cb_speed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cb_speed.setChecked(false);

        String ip = "";
        String port = speedTestBeen.get(position).version_port;
        if ("80".equals(port) || TextUtils.isEmpty(port)) {
            ip = speedTestBeen.get(position).version_ip;
        } else {
            ip = speedTestBeen.get(position).version_ip + ":" + port;
        }
        final String url = ip;
        if (ConstantUtil.IP.equals(url) || ConstantUtil.SJYZM.equals(url)) {
            holder.cb_speed.setChecked(true);
        }
        holder.tv_text1.setText(speedTestBeen.get(position).version_name);
        String status = speedTestBeen.get(position).version_status;
        holder.tv_text2.setText(status);
        if ("优".equals(status)) {
            holder.tv_text2.setTextColor(ColorUtils.RED);
        } else {
            holder.tv_text2.setTextColor(ColorUtils.GREEN);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (i != position) {
                        checkBoxes.get(i).setChecked(false);
                    } else {
                        checkBoxes.get(i).setChecked(true);
                        speedCallBack.setUrl(speedTestBeen.get(position).version_name,url);
                    }
                }
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
