package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialogCenter;
import com.tpyzq.mobile.pangu.util.FileUtil;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.tpyzq.mobile.pangu.view.WavaBezierProgress;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;


/**
 * 作者：刘泽鹏 on 2016/11/10 19:05
 */
public class VersionDialog extends BaseDialogCenter implements View.OnClickListener {

    private ImageView ivGuanBi;                 //叉子 关闭
    private TextView tvVersionNumber;          //版本号
    private TextView tv_download;        //立即更新  按钮
    private String apkAddress;                 // 下载地址
    private String forceIsupdate;              //是否强制更新
    private String versionNumber;               //版本号
    private WavaBezierProgress wbp_download;

    public VersionDialog(Context context, String apkAddress, String forceIsupdate, String versionNumber) {
        super(context);
        this.apkAddress = apkAddress;
        this.forceIsupdate = forceIsupdate;
        this.versionNumber = versionNumber;
    }

    @Override
    public void setView() {
        ivGuanBi = (ImageView) findViewById(R.id.ivGuanBi);
        tvVersionNumber = (TextView) findViewById(R.id.tvVersionNumber);
        tv_download = (TextView) findViewById(R.id.tv_download);
        wbp_download = (WavaBezierProgress) findViewById(R.id.wbp_download);
    }

    @Override
    public void initData() {

        setCancelable(false);                 //屏蔽返回键

        if ("1".equals(forceIsupdate)) {      //强制更新
            ivGuanBi.setVisibility(View.GONE);
        } else if ("0".equals(forceIsupdate)) {      //非 强制更新
            ivGuanBi.setVisibility(View.VISIBLE);
        }

        tvVersionNumber.setText("最新版本 " + versionNumber);
        tv_download.setOnClickListener(this);         //更新
        ivGuanBi.setOnClickListener(this);              //关闭 按钮
    }

    @Override
    public int getLayoutId() {
        return R.layout.version_dialog;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_download:
                //更新
                String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pangu/apk/";
                File apkfile = new File(file + "new.apk");
                if (apkfile.exists()) {
                    APPInfoUtils.installApk(context, apkfile.getAbsolutePath());
                } else {
                    wbp_download.setProgress(0);
                    wbp_download.setVisibility(View.VISIBLE);
                    tv_download.setClickable(false);
                    tv_download.setVisibility(View.GONE);
                    downloadApk(apkAddress, file);
                }

                break;

            case R.id.ivGuanBi:             //关闭
                dismiss();
                break;
        }
    }

    private void downloadApk(String url, final String files) {
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(files, "new.apk") {

            @Override
            public void onResponse(final File file, int id) {
                APPInfoUtils.installApk(context, file.getAbsolutePath());
                wbp_download.setProgress(0);
                wbp_download.setVisibility(View.GONE);
                tv_download.setClickable(true);
                tv_download.setVisibility(View.VISIBLE);
                tv_download.setText("开始安装");
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                wbp_download.setProgress(0);
                wbp_download.setVisibility(View.GONE);
                tv_download.setClickable(true);
                tv_download.setVisibility(View.VISIBLE);
                tv_download.setText("更新失败");
                File file = new File(files+"new.apk");
                try {
                    FileUtil.deleteFile(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void inProgress(float progress, long l, int id) {
                int pro = (int)(progress*100);
                wbp_download.setProgress(pro);
            }
        });
    }
}
