package com.tpyzq.mobile.pangu.view.loopswitch;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import java.util.List;

/**
 * Created by ltyhome on 02/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:AutoSwitchAdapter
 */

public class AutoSwitchAdapter extends AutoLoopSwitchBaseAdapter{
    private Context mContext;

    private List<InformationEntity> mDatas;

    public AutoSwitchAdapter() {
        super();
    }

    public AutoSwitchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<InformationEntity> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getDataCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public View getView(int position) {
        final InformationEntity model = (InformationEntity)getItem(position);
        RelativeLayout ll  = new RelativeLayout(mContext);
        TextView tvTitle = new TextView(mContext);
        tvTitle.setSingleLine();
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setId(R.id.autoloopswitch_title_id);
        tvTitle.setText(model.getTitle());
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
        tvTime.setText(model.getDate()+" "+model.getTime());
        tvTime.setPadding(0,2,0,2);
        tvTime.setTextColor(mContext.getResources().getColor(R.color.hideTextColor));
        ll.addView(tvTime,lpbottom);
        RelativeLayout.LayoutParams lpimage = new RelativeLayout.LayoutParams(-1,-1);
        lpimage.addRule(RelativeLayout.BELOW,R.id.autoloopswitch_title_id);
        lpimage.addRule(RelativeLayout.ABOVE,R.id.autoloopswitch_time_id);
        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(model.getImage_url());
        ll.addView(imageView,lpimage);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("requestId",model.getNewsno());
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
