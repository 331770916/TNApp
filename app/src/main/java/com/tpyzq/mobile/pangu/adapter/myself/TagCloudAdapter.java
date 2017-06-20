package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagsAdapter;
import com.tpyzq.mobile.pangu.activity.myself.view.AccountPager;
import com.tpyzq.mobile.pangu.util.ColorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈新宇 on 2016/9/16.
 */
public class TagCloudAdapter extends TagsAdapter {
    private List<String[]> lists;
    private List<TextView> textViews;
    private AccountPager.UserHeaderListen userHeaderListen;
    int point = -1;

    public TagCloudAdapter(List lists, AccountPager.UserHeaderListen userHeaderListen) {
        this.lists = lists;
        this.userHeaderListen = userHeaderListen;
        textViews = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0) {
            return lists.size();
        }
        return 0;
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        final TextView tv = new TextView(context);
        textViews.add(tv);
        tv.setTag(position);
        tv.setText(lists.get(position)[0]);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point = position;
                // 设置文字颜色
                tv.setTextColor(ColorUtils.BLUE);
                userHeaderListen.setTextView(position);
//                setAnimation(tv);
                for (int i = 0; i < textViews.size(); i++) {
                    if (i != position) {
                        textViews.get(i).setTextColor(Color.WHITE);
                    }
                }
            }
        });
        return tv;
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position)[0];
    }

    @Override
    public int getPopularity(int position) {
        return position % 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {
        view.setBackgroundColor(0);
    }
}
