package com.tpyzq.mobile.pangu.view.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.view.loopswitch.AutoLoopSwitchBaseAdapter;
import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 09/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:
 */

public class HomeSwitchAdapter extends AutoLoopSwitchBaseAdapter {
    private Context mContext;

    private List<Map<String,String>> mDatas;

    public HomeSwitchAdapter() {
        super();
    }

    public HomeSwitchAdapter(Context mContext) {
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
        final Map<String,String> model = mDatas.get(position);
        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(model.get("url"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return imageView;
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
