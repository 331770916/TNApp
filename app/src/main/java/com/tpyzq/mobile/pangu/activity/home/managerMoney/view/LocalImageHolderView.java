package com.tpyzq.mobile.pangu.activity.home.managerMoney.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.tpyzq.mobile.pangu.view.CustomImageView;

/**
 * Created by Administrator on 2016/9/17.
 */
public class LocalImageHolderView implements Holder<Bitmap> {

    private CustomImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new CustomImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, final int position, Bitmap data) {
        imageView.setImageBitmap(data);
    }
}
