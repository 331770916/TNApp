package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/12/8.
 */

public class DownloadDocPdfDialog {

    private static DownloadDocPdfDialog mDownloadDocPdfDialog;
    private static final String TAG = "DownloadDocPdfDialog";

    private DownloadDocPdfDialog() {
    }

    ;

    public synchronized static DownloadDocPdfDialog getInstance() {
        if (mDownloadDocPdfDialog == null) {
            mDownloadDocPdfDialog = new DownloadDocPdfDialog();
        }
        return mDownloadDocPdfDialog;
    }

    public void showDialog(final Activity activity, final DownloadPdfCallback downloadPdfCallback, String url, final String fileName) {

//        if (!Helper.isActivityRunning(CustomApplication.getContext(), activity.getLocalClassName())) {
//            return ;
//        }

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LayoutInflater inflater = LayoutInflater.from(CustomApplication.getContext());
        final View view = inflater.inflate(R.layout.dialog_download_pdf, null);

        int minWidth = (int) (width * 0.7);
        int minHeight = (int) (height * 0.2);

        view.setMinimumWidth(minWidth);
        view.setMinimumHeight(minHeight);

        final TextView textView = (TextView) view.findViewById(R.id.dialogDownloadTitle);
        textView.setText(fileName + "\u3000已下载" + 0 + "%");

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.dialogDownloadProgress);

        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

//        try {
//            String ss[] = url.split("/");
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < ss.length; i++) {
//                if (i == ss.length - 1) {
//                    sb.append(URLEncoder.encode(ss[i], "GBK"));
//                } else {
//                    sb.append(ss[i]);
//                    sb.append("/");
//                }
//
//            }
//            url = sb.toString();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        NetWorkUtil.okHttpForDownLoadFile(TAG, url, new FileCallBack(Helper.getAppFileDirPath(CustomApplication.getContext()) + "pdf", fileName + ".pdf") {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                LogHelper.e(TAG, e.toString());
                downloadPdfCallback.downloadResult(null, null);
                CentreToast.showText(activity, ConstantUtil.NETWORK_ERROR);
                File file = new File(Helper.getAppFileDirPath(CustomApplication.getContext()) + "pdf/" + fileName + ".pdf");
                if (file.exists()) {
                    file.delete();
                }
                dialog.dismiss();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                int intProgress = (int) (100 * progress);
                progressBar.setProgress(intProgress);

                textView.setText(fileName + "\u3000已下载" + intProgress + "%");
            }

            @Override
            public void onResponse(File response, int id) {
                downloadPdfCallback.downloadResult(response.getAbsolutePath(), fileName);

                if (progressBar.getProgress() == 100) {
                    dialog.dismiss();
                }
            }
        });
    }

    public interface DownloadPdfCallback {
        public void downloadResult(String filePath, String name);
    }


}
