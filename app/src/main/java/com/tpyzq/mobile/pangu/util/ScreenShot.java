package com.tpyzq.mobile.pangu.util;
/**
 * 获取指定Activity的截屏，保存到png文件
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShot {

    private static Bitmap takeScreenShot(Activity activity) {

        //View是你需要截图的View

        View view = activity.getWindow().getDecorView();

        view.setDrawingCacheEnabled(true);

        view.buildDrawingCache();

        Bitmap b1 = view.getDrawingCache();

        //获取状态栏高度

        Rect frame = new Rect();

        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int statusBarHeight = frame.top;

        System.out.println(statusBarHeight);//获取屏幕长和高

        int width = activity.getWindowManager().getDefaultDisplay().getWidth();

        int height = activity.getWindowManager().getDefaultDisplay().getHeight();//去掉标题栏

        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);

//        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        Bitmap b = ThumbnailUtils.extractThumbnail(b1,width/2,height/2);
        view.destroyDrawingCache();

        return b;

    }
    //保存到sdcard
    private static Bitmap takeScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        //获取状态栏高度
        Rect frame = new Rect();
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, view.getWidth(), view.getHeight());
        view.destroyDrawingCache();
        return b;

    }//保存到sdcard
    private static void savePic(Bitmap b, String strFileName) {

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(strFileName);

            if (null != fos)

            {

                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                fos.flush();

                fos.close();

            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }//程序入口

    public static String shoot(Activity a) {
            // 使用系统当前日期加以调整作为照片的名称
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "'IMG'_yyyyMMdd_HHmmss");
            String file = "sdcard/pangu/screenshots/" + dateFormat.format(date) + ".jpg";
            ScreenShot.savePic(ScreenShot.takeScreenShot(a), file);

            return imgToBase64(file, null, null);
    }
    public static String shoot(View a) {
        // 使用系统当前日期加以调整作为照片的名称
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        String file = "sdcard/pangu/screenshots/" + dateFormat.format(date) + ".jpg";
        ScreenShot.savePic(ScreenShot.takeScreenShot(a), file);

        return imgToBase64(file, null, null);
    }

    public static String shoot(PullToRefreshScrollView scrollView) {
        // 使用系统当前日期加以调整作为照片的名称
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        String file = "sdcard/pangu/screenshots/" + dateFormat.format(date) + ".png";
        ScreenShot.savePic(getScrollViewBitmap(scrollView), file);
//        getScrollViewBitmap(scrollView,file);
        return imgToBase64(file, null, null);
    }

    /**
     * 截取scrollview的屏幕
     * **/
    public static Bitmap getScrollViewBitmap(PullToRefreshScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }
    /**
     * @param imgPath
     * @param bitmap
     * @param imgFormat 图片转换成 Base64
     * @return
     */
    public static String imgToBase64(String imgPath, Bitmap bitmap, String imgFormat) {
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if (bitmap == null) {
            //bitmap not found!!
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param imgPath 根据路径转换成Bitmap
     * @return
     */
    private static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param base64Data
     * @param imgName
     * @param imgFormat  把Base64转换成Bitmap
     */
    public static void base64ToBitmap(String base64Data, String imgName, String imgFormat) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        File myCaptureFile = new File("/sdcard/", imgName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myCaptureFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (isTu) {
            // fos.notifyAll();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}