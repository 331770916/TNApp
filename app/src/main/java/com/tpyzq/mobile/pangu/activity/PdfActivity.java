package com.tpyzq.mobile.pangu.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.io.File;

/**
 * Created by zhangwenbo on 2016/12/10.
 */

public class PdfActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void initView() {

        Intent intent = getIntent();
        String filePath = intent.getStringExtra("filePath");
        String fileName = intent.getStringExtra("fileName");
        int flag = intent.getIntExtra("flag", 0);

        TextView titleText = (TextView) findViewById(R.id.pdf_title);
        if (!TextUtils.isEmpty(fileName)) {
            titleText.setText(fileName);
        }

        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        findViewById(R.id.pdf_back).setOnClickListener(this);

        if (!TextUtils.isEmpty(filePath)) {
            switch (flag) {
                case 0:
                    pdfView.fromAsset(filePath).load();
                    break;
                case 1:
                    File file = new File(filePath);
                    if (file.exists()) {
                        pdfView.fromFile(file).load();
                    }
            }

        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pdf;
    }
}
