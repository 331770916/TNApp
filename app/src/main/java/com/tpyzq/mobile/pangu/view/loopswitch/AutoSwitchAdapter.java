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
import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 02/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:AutoSwitchAdapter
 */

public class AutoSwitchAdapter extends AutoLoopSwitchBaseAdapter{
    private Context mContext;

    private List<Map<String,String>> mDatas;

    public AutoSwitchAdapter() {
        super();
    }

    public AutoSwitchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<Map<String,String>> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getDataCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public View getView(int position) {
        final Map<String,String> model = (Map<String,String>)getItem(position);
        RelativeLayout ll  = new RelativeLayout(mContext);
        TextView tvTitle = new TextView(mContext);
        tvTitle.setSingleLine();
        tvTitle.setId(R.id.autoloopswitch_title_id);
        tvTitle.setText(model.get("title"));
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
        tvTime.setText(model.get("time"));
        tvTime.setPadding(0,2,0,2);
        tvTime.setTextColor(mContext.getResources().getColor(R.color.hideTextColor));
        ll.addView(tvTime,lpbottom);
        RelativeLayout.LayoutParams lpimage = new RelativeLayout.LayoutParams(-1,-1);
        lpimage.addRule(RelativeLayout.BELOW,R.id.autoloopswitch_title_id);
        lpimage.addRule(RelativeLayout.ABOVE,R.id.autoloopswitch_time_id);
        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(model.get("url"));
        ll.addView(imageView,lpimage);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("requestId",model.get("id"));
                mContext.startActivity(intent);
            }
        });
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
