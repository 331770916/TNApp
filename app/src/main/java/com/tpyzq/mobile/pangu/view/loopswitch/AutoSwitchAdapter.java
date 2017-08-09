package com.tpyzq.mobile.pangu.view.loopswitch;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.OptionalFinancingActivity;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;

import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 02/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:AutoSwitchAdapter
 */

public class AutoSwitchAdapter extends AutoLoopSwitchBaseAdapter{
    private Context mContext;
    private int mType;
    private List<Map<String,Object>> mDatas;

    public AutoSwitchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public AutoSwitchAdapter(Context mContext,int type) {
        this.mContext = mContext;
        this.mType = type;
    }


    public void setDatas(List<Map<String,Object>> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getDataCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public View getView(int position) {
        final Map<String,Object> model = (Map<String,Object>)getItem(position);
        RelativeLayout ll  = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams lpimage = new RelativeLayout.LayoutParams(-1,-1);
        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        switch (mType){
            case 0:
                TextView tvTitle = new TextView(mContext);
                tvTitle.setSingleLine();
                tvTitle.setId(R.id.autoloopswitch_title_id);
                tvTitle.setText(model.get("title").toString());
                tvTitle.setTextSize(18);
                tvTitle.setPadding(0,5,0,5);
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.bkColor));
                ll.addView(tvTitle);
                RelativeLayout.LayoutParams lpbottom = new RelativeLayout.LayoutParams(-2,-2);
                lpbottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lpbottom.addRule(RelativeLayout.ALIGN_LEFT);
                TextView tvTime = new TextView(mContext);
                tvTime.setId(R.id.autoloopswitch_time_id);
                tvTime.setTextSize(12);
                tvTime.setText(model.get("time").toString());
                tvTime.setPadding(0,2,0,2);
                tvTime.setTextColor(mContext.getResources().getColor(R.color.hideTextColor));
                ll.addView(tvTime,lpbottom);
                lpimage.addRule(RelativeLayout.BELOW,R.id.autoloopswitch_title_id);
                lpimage.addRule(RelativeLayout.ABOVE,R.id.autoloopswitch_time_id);
                imageView.setImageURI(model.get("url").toString());
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewsDetailActivity.class);
                        intent.putExtra("requestId",model.get("id").toString());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 1:
                imageView.setImageResource((int)model.get("resource"));
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        BRutil.menuSelect(model.get("br").toString());
                        intent.setClass(mContext, (Class<?>) model.get("clazz"));
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
        ll.addView(imageView,lpimage);
        return ll;
    }


    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < getDataCount())
            return mDatas.get(position);
        return null;
    }


    @Override
    public View getEmptyView() {
        return null;
    }

    @Override
    public void updateView(View view, int position) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
