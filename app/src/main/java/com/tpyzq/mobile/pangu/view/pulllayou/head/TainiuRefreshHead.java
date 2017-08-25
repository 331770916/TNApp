package com.tpyzq.mobile.pangu.view.pulllayou.head;

import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.view.pulllayou.base.AnimationCallback;

import java.text.SimpleDateFormat;

/**
 * Created by Admin on 2017/7/25.
 */

public class TainiuRefreshHead extends SimpleRefreshHead {
    private View mHeadView;
    private ImageView ivHeaderDownArrow;
    private TextView textView;
    private TextView textViewDate;
    private AnimationDrawable animationDrawable;

    @Override
    public View getTargetView(ViewGroup parent) {
        if (null == mHeadView) {
            animationDrawable = (AnimationDrawable) CustomApplication.getContext().getResources().getDrawable(R.drawable.cow);
            mHeadView = LayoutInflater.from(parent.getContext()).inflate(R.layout.refresh_header_tainiu, parent, false);
            initView(mHeadView);
//            ivHeaderDownArrow.setBackgroundDrawable(animationDrawable);
            ivHeaderDownArrow.setImageDrawable(animationDrawable);
        }
        return mHeadView;
    }

    private void initView(View view) {
        ivHeaderDownArrow = (ImageView) view.findViewById(R.id.arrow);
        textView = (TextView) view.findViewById(R.id.description);
        textViewDate = (TextView) view.findViewById(R.id.updated_at);
    }

    @Override
    public void onPull(float scrollY, boolean enable) {
        if (!enable || mReturningToRefresh) return;

        if (mHeadViewHeight == -1) {
            mHeadViewHeight = mHeadView.getHeight();
        }
        animationDrawable.start();
        if (scrollY > mHeadViewHeight && mArrowDown) {
            textView.setText(R.string.release_refresh);
            SimpleDateFormat sDateFormat = new SimpleDateFormat("刷新时间："+"HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            textViewDate.setText(date);
            textViewDate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.texts));
            mArrowDown = false;
        } else if (scrollY <= mHeadViewHeight && !mArrowDown) {
            textView.setText(R.string.pull_refresh);

            mArrowDown = true;
        }
    }

    @Override
    public void onFingerUp(float scrollY) {
        if (mArrowDown) {//回到原位
            iPull.animToStartPosition(new AnimationCallback() {
                @Override
                public void onAnimationEnd() {
                    animationDrawable.start();
                }
            });
        } else {
            mReturningToRefresh = true;
            isRefreshing = true;
            iPull.animToRightPosition(mHeadViewHeight, new AnimationCallback() {
                @Override
                public void onAnimationStart() {
                    animationDrawable.start();
                    textView.setText(R.string.refreshing);
                }

                @Override
                public void onAnimationEnd() {
                    mReturningToRefresh = false;
                    iPull.pullDownCallback();
                }
            });
        }
    }

    @Override
    public void finishPull(boolean isBeingDragged) {
        iPull.animToStartPosition(new AnimationCallback() {
            @Override
            public void onAnimationEnd() {
                animationDrawable.stop();
                reset();
            }
        });
    }

    @Override
    public void finishPull(boolean isBeingDragged, CharSequence msg, boolean result) {
        textView.setText(msg);
        textViewDate.setText("");
        animationDrawable.stop();
        mHeadView.postDelayed(new Runnable() {
            @Override
            public void run() {
                iPull.animToStartPosition(new AnimationCallback() {
                    @Override
                    public void onAnimationStart() {
                        //恢复场景
                        reset();
                    }
                });
            }
        }, 1000);
    }

    private void reset() {
        isRefreshing = false;
        ivHeaderDownArrow.setImageDrawable(animationDrawable);
        textView.setText(R.string.pull_refresh);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("刷新时间："+"HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        textViewDate.setText(date);
    }
}
