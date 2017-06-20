package com.tpyzq.mobile.pangu.view.pullrefreshrecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.text.SimpleDateFormat;

/**
 * Created by wangqi on 2017/4/1.
 */

public class HeadView extends LinearLayout implements PullRefreshView.OnHeadStateListener {
    ImageView ivHeaderDownArrow;
    TextView textView;
    TextView textViewDate;

    AnimationDrawable animationDrawable;

    private boolean isReach = false;

    public HeadView(Context context) {
        super(context);
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.cow);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.refresh_top_item_new, this, false);
        this.addView(layout);
        initView(layout);
        restore();
    }

    private void initView(View view) {
        ivHeaderDownArrow = (ImageView) view.findViewById(R.id.arrow);
        textView = (TextView) view.findViewById(R.id.description);
        textViewDate = (TextView) view.findViewById(R.id.updated_at);
    }

    @Override
    public void onScrollChange(View head, int scrollOffset, int scrollRatio) {
        if (scrollRatio >= 100 && !isReach) {
            textView.setText("松开刷新");
            SimpleDateFormat sDateFormat = new SimpleDateFormat("最后更新时间"+"yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            textViewDate.setText(date);
            textViewDate.setTextColor(getResources().getColor(R.color.texts));
            animationDrawable.start();
            isReach = true;
        }
        else {
//            textView.setText("下拉刷新");
            animationDrawable.stop();
            isReach = false;
        }
    }

    @Override
    public void onRefreshHead(View head) {
        ivHeaderDownArrow.setVisibility(VISIBLE);
        ivHeaderDownArrow.setImageDrawable(animationDrawable);
        animationDrawable.start();
        textView.setText("正在刷新");
    }


    @Override
    public void onRetractHead(View head) {
        restore();
        animationDrawable.stop();
        isReach = false;
    }

    private void restore() {
//        ivHeaderDownArrow.setVisibility(VISIBLE);
//        ivHeaderDownArrow.setImageResource(R.drawable.cow);
//        ivHeaderDownArrow.setRotation(0);
        ivHeaderDownArrow.setImageDrawable(animationDrawable);
        textView.setText("下拉刷新");
    }
}
