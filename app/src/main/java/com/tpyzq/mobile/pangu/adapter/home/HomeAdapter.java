package com.tpyzq.mobile.pangu.adapter.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeInfomationObsever;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dragindicator.DragIndicatorView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by zhangwenbo on 2016/11/15.
 */
public class HomeAdapter extends BaseAdapter implements HomeInfomationObsever {

    private ArrayList<Map<String, Object>> mDatas;
    private Map<Integer, Object> mTips;
    private boolean mDragIndicatorViewVisible;
    private Context mContext;
    private int mCount;

    public HomeAdapter(boolean dragIndicatorViewVisible, Context context) {
        mDragIndicatorViewVisible = dragIndicatorViewVisible;
        mContext = context;
    }

    public void setDatas(ArrayList<Map<String, Object>> datas, Map<Integer, Object> tips) {
        mDatas = datas;
        mTips = tips;
        notifyDataSetChanged();
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
    public int getCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHodler viewHodler;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_grid_item, null);
            viewHodler = new CustomViewHodler(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (CustomViewHodler) convertView.getTag();
        }

        viewHodler.titleView.setText("");
        viewHodler.imageView.setImageDrawable(null);
        viewHodler.dragIndicatorView.setVisibility(View.GONE);
        viewHodler.mLayout.removeAllViews();


        if (null != mDatas.get(position).get("img")) {
            Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), (int) mDatas.get(position).get("img"));
            if (mTips != null) {

                Set<Integer> keySet = mTips.keySet();
                Iterator<Integer> iterator = keySet.iterator();

                while (iterator.hasNext()) {
                    int _position = iterator.next();

                    if (_position == position) {
                        Drawable tipDrable = ContextCompat.getDrawable(CustomApplication.getContext(), (int) mTips.get(position));

                        if (tipDrable != null) {
                            ImageView imageView = new ImageView(mContext);
                            imageView.setImageDrawable(tipDrable);
//                            setFlickerAnimation(imageView);


                            if (!SpUtils.getBoolean(mContext,"isFirstInToHomeMoreGride", false)) {
                                viewHodler.mLayout.addView(imageView);
                                SpUtils.putBoolean(mContext,"isFirstInToHomeMoreGride", true);
                            } else {
                                viewHodler.mLayout.removeView(imageView);

                            }

                        }
                    }
                }
            }

            viewHodler.imageView.setImageDrawable(drawable);
        }

        if (null != mDatas.get(position).get("title")) {
            viewHodler.titleView.setText((String) mDatas.get(position).get("title"));
        }


        if (mDragIndicatorViewVisible) {
                if ("我的消息".equals(mDatas.get(position).get("title"))) {

                    if ("0".equals("" + mCount)) {
                        viewHodler.dragIndicatorView.setVisibility(View.GONE);
                    } else {
                        viewHodler.dragIndicatorView.setText("" + mCount);
                        viewHodler.dragIndicatorView.setVisibility(View.VISIBLE);
                    }

                } else {
                    viewHodler.dragIndicatorView.setVisibility(View.GONE);
                }

            viewHodler.dragIndicatorView.setOnDismissAction(new DragIndicatorView.OnIndicatorDismiss() {
                @Override
                public void OnDismiss(DragIndicatorView view) {
//                    bean.isIndicatorShow = false;
                }
            });

        }

        return convertView;
    }

    class CustomViewHodler {

        TextView titleView;
        ImageView imageView;
        DragIndicatorView dragIndicatorView;
        private FrameLayout mLayout;

        public CustomViewHodler(View view) {
            titleView = (TextView) view.findViewById(R.id.item_text);
            imageView = (ImageView) view.findViewById(R.id.item_image);
            dragIndicatorView = (DragIndicatorView) view.findViewById(R.id.dragIndicatorView);
            mLayout = (FrameLayout) view.findViewById(R.id.oterItemLayout);
        }
    }

    @Override
    public void update(int count) {
        mCount = count;
        notifyDataSetChanged();
    }

    private void setFlickerAnimation(ImageView iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        iv_chat_head.setAnimation(animation);
    }
}
