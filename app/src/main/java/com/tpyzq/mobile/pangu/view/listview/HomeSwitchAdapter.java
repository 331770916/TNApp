package com.tpyzq.mobile.pangu.view.listview;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.HomeFragment;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.loopswitch.AutoLoopSwitchBaseAdapter;
import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 09/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:
 */

public class HomeSwitchAdapter extends AutoLoopSwitchBaseAdapter {
    private Activity mContext;

    private List<Map<String,String>> mDatas;
    private HomeFragment.JumpPageListener listener;
    public HomeSwitchAdapter() {
        super();
    }

    public HomeSwitchAdapter(Activity mContext) {
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
        final String jump_type =model.get("jump_type");
        final String jump_position = model.get("jump_position");
        final String jump_url = model.get("jump_url");
        String show_url = model.get("show_url");
        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Uri imgurl= Uri.parse(show_url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        if("default".equals(show_url))
            imageView.setImageResource(R.mipmap.top2);
         else
            imageView.setImageURI(show_url);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(jump_type)){
                    switch (jump_type){
                        case "0":
                            Helper.startActivity(mContext,"TN0023",jump_url,listener);
                            break;
                        case "1":
                            if(!TextUtils.isEmpty(jump_position))
                                Helper.startActivity(mContext,jump_position,null,listener);
                            break;
                    }
                }
            }
        });
        return imageView;
    }

    public void setListener(HomeFragment.JumpPageListener mJumpPageListener){
        this.listener = mJumpPageListener;
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
