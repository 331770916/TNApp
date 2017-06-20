package com.tpyzq.mobile.pangu.view.pullDownGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;


/**
 * Created by Administrator on 2016/7/4.
 */
public class PullDownElasticImp implements IPullDownElastic {

    private View refreshView;
    private ImageView arrowImageView;
    private int headContentHeight;
    private ProgressBar progressBar;
    private TextView lastUpdatedTextView;

    private Context mContext;
    public PullDownElasticImp(Context context) {
        mContext = context;
        init();
    }


    private void init() {
        // 刷新视图顶端的的view
        refreshView = LayoutInflater.from(mContext).inflate(
                R.layout.refresh_top_item, null);

        // 指示器view
        arrowImageView = (ImageView) refreshView
                .findViewById(R.id.head_arrowImageView);
        // 刷新bar
        progressBar = (ProgressBar) refreshView
                .findViewById(R.id.head_progressBar);
        // 下来显示时间
        lastUpdatedTextView = (TextView) refreshView
                .findViewById(R.id.refresh_time);

        headContentHeight = dip2px(mContext, 50);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * @return
     *
     */
    @Override
    public View getElasticLayout() {
        return refreshView;
    }

    /**
     * @return
     *
     */
    @Override
    public int getElasticHeight() {
        return headContentHeight;
    }

    /**
     * @param visibility
     *
     */
    @Override
    public void showArrow(int visibility) {
        arrowImageView.setVisibility(visibility);
    }

    /**
     * @param animation
     *
     */
    @Override
    public void startAnimation(Animation animation) {
        arrowImageView.startAnimation(animation);
    }

    /**
     *
     *
     */
    @Override
    public void clearAnimation() {
        arrowImageView.clearAnimation();
    }

    /**
     * @param visibility
     */
    @Override
    public void showProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }

    /**
     * @param tips
     *
     */
    @Override
    public void setTips(String tips) {
    }

    /**
     * @param visibility
     */
    @Override
    public void showLastUpdate(int visibility) {
        lastUpdatedTextView.setVisibility(visibility);
    }

    /**
     * @param text
     *
     */
    public void setLastUpdateText(String text) {
        lastUpdatedTextView.setText(text);
    }


    /**
     * @param state
     * @param isBack
     *
     */
    @Override
    public void changeElasticState(int state, boolean isBack) {
        // TODO Auto-generated method stub
    }
}
