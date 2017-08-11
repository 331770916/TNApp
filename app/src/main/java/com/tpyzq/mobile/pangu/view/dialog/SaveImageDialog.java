package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;

import java.io.InputStream;

/**
 * Created by wangqi on 2017/4/12.
 */

public class SaveImageDialog extends BaseDialog implements View.OnClickListener {
    private TextView yse;
    private TextView no;


    public SaveImageDialog(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        yse = (TextView) findViewById(R.id.Yse);
        no = (TextView) findViewById(R.id.No);
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_longclicked_img;
    }

    @Override
    public void initData() {
        yse.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Yse:
                SPDrawable();
                break;
            case R.id.No:
                dismiss();
                break;
        }
    }

    private void SPDrawable() {
        AssetManager asm = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = asm.open("barcode.png");
            Drawable d = Drawable.createFromStream(inputStream, null);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, Helper.getCurDate(), "description");
            CentreToast.showText(context,"保存成功");
            inputStream.close();
            dismiss();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
