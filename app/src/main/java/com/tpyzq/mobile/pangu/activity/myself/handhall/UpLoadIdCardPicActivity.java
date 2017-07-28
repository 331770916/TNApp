package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.UpdateIdCodeValidityEntity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/21.
 * 上传身份证照片
 */

public class UpLoadIdCardPicActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener{

    public static final int FRONT_REQUEST_IMAGE_CAPTURE = 1;
    public static final int BACK_REQUEST_IMAGE_CAPTURE = 2;
    private final String TAG = UpLoadIdCardPicActivity.class.getSimpleName();

    private Dialog mProgressDialog;
    private UpdateIdCodeValidityEntity mEntity;
    private TextView mUploadIdCardBtn;
    private TextView mFrontTvResult;
    private TextView mBackTvResult;

    private ImageView mFrontImg;
    private ImageView mBackImg;

    private String biz_unit_id; // 业务id 如果biz_unit_id 是空 代表是从身份证有效期更改过来的
    private String verify_id ;//当修改资金密码时的双向视频用到的
    private String fund_account;//资金账号
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求

    private String tempVerifyId1;
    private String tempVerifyId2;

    @Override
    public void initView() {
        Intent intent = getIntent();
        biz_unit_id = intent.getStringExtra("biz_unit_id");
        verify_id = intent.getStringExtra("verify_id");
        fund_account = intent.getStringExtra("fund_account");
        mEntity = intent.getParcelableExtra("entity");

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("上传身份证");

        TextView tipView = (TextView) findViewById(R.id.tipTv);

        String str1 = "拍摄上传照片需保证";
        String str2 = "边框完整、字体图像清晰、亮度均匀。";

        SpannableString ss = new SpannableString(str1 + str2);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.hideTextColor)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.calendarBtnColor)), str1.length(), (str1 + str2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tipView.setText(ss);

        mFrontTvResult = (TextView) findViewById(R.id.frontReuslt);
        mBackTvResult = (TextView) findViewById(R.id.blackReuslt);

        mUploadIdCardBtn = (TextView) findViewById(R.id.uploadIdCarBtn);
        mUploadIdCardBtn.setClickable(false);
        mUploadIdCardBtn.setFocusable(false);
        mUploadIdCardBtn.setOnClickListener(this);

        mFrontImg  = (ImageView) findViewById(R.id.pic_front);
        mBackImg = (ImageView) findViewById(R.id.pic_back);

        mFrontImg.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
        initLoadDialog();
        getStup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                CancelDialog.cancleDialog(UpLoadIdCardPicActivity.this);
                break;
            case R.id.uploadIdCarBtn:
                 if (!TextUtils.isEmpty(biz_unit_id)) {
                     toVideoTask();
                 } else {
                     uploadValidityTask();
                 }
                break;
            case R.id.pic_front:
                dispatchTakePictureIntent(FRONT_REQUEST_IMAGE_CAPTURE);
                break;
            case R.id.pic_back:
                dispatchTakePictureIntent(BACK_REQUEST_IMAGE_CAPTURE);
                break;
        }
    }

    private void dispatchTakePictureIntent(int IDCARD_TAG) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IDCARD_TAG);
        }
    }

    /**
     * 修改密码开启视频任务
     */
    private void toVideoTask() {
        Intent intent = new Intent();
        intent.putExtra("verify_id", verify_id);
        intent.putExtra("fund_account", fund_account);
        intent.putExtra("entity", mEntity);
        intent.setClass(UpLoadIdCardPicActivity.this, StartVideoActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 提交身份证有效期任务
     */
    private void uploadValidityTask () {
        if (TextUtils.isEmpty(tempVerifyId2) || TextUtils.isEmpty(tempVerifyId1) || !tempVerifyId1.equals(tempVerifyId2)) {
            showMistackDialog("业务id不一致", null);
            return;
        }

        initLoadDialog();

        validityApplyStauts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = revitionBitmapSize(1000, (Bitmap) extras.get("data"));
            boolean bitmapIsNull = false;
            String imageNo = "";
            if (requestCode == FRONT_REQUEST_IMAGE_CAPTURE) {
                imageNo = "6A";
                bitmapIsNull = imageBitmap == null ? true : setImageBitmap(mFrontImg, imageBitmap);
            } else if (requestCode == BACK_REQUEST_IMAGE_CAPTURE) {
                imageNo = "6B";
                bitmapIsNull = imageBitmap == null ? true : setImageBitmap(mBackImg, imageBitmap);
            }

            if (bitmapIsNull) {
                showMistackDialog("生成图片失败", null);
            } else {
                initLoadDialog();
                String imageBase64 = bitmapToBase64(imageBitmap);
                uploadPic(imageBase64, biz_unit_id, imageNo, requestCode, resultCode);
            }
        }
    }

    /**
     * 展示图片
     * @param imageView
     * @param imageBitmap
     * @return
     */
    private boolean setImageBitmap (ImageView imageView, Bitmap imageBitmap) {
        imageView.setImageBitmap(imageBitmap);
        return false;
    }

    /**
     * 图片处理
     * @param size
     * @param bitmap
     * @return
     */
    private Bitmap revitionBitmapSize(int size, Bitmap bitmap) {
        int maxLength = 1024 * 1024; // 预定的图片最大内存，单位byte
        Bitmap decodePic = null;
        // 压缩大小
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            int quality = 100;

            while (baos.toByteArray().length > maxLength) { // 循环判断，大于继续压缩
                quality -= 10;// 每次都减少10
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);//PNG 压缩options%
            }


            // 压缩尺寸
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            BitmapFactory.Options options = new BitmapFactory.Options();
            //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            options.inJustDecodeBounds = true;
            decodePic = BitmapFactory.decodeStream(bais, null, options);// 加载图片(只得到图片大小)

            int screenWidth = Helper.getScreenWidth(this);
            int screenHeight = Helper.getScreenHeight(this);
            // 获取屏幕大小，按比例压缩

            int scaleX = options.outWidth / screenWidth; // X轴缩放比例(图片宽度/屏幕宽度)
            int scaleY = options.outHeight / screenHeight; // Y轴缩放比例
            int scale = scaleX > scaleY ? scaleX : scaleY; // 图片的缩放比例(X和Y哪个大选哪个)
            options.inJustDecodeBounds = false; // 修改选项, 不只解码边界
            options.inSampleSize = scale > 1 ? scale : 1; // 修改选项, 加载图片时的缩放比例
            bais.reset();
            decodePic = BitmapFactory.decodeStream(bais, null, options);

//            int i = 0;
//            while (true) {
//                if ((options.outWidth >> i <= size)&& (options.outHeight >> i <= size)) {
//                    options.inSampleSize = (int) Math.pow(2.0D, i);
//                    options.inJustDecodeBounds = false;
//                    decodePic = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, options);
//                    break;
//                }
//
//                i += 1;
//            }

            if (decodePic != null) {
                int picWidth = decodePic.getWidth();
                int picHeight = decodePic.getHeight();

                if (picWidth < picHeight) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(1f, 1f);
                    matrix.postRotate(90, (float) picWidth , (float) picHeight );

                    Bitmap decodePic2 = Bitmap.createBitmap(decodePic, 0, 0, picWidth, picHeight, matrix, true);


                    if (decodePic != decodePic2) {
                        decodePic.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
                        decodePic = decodePic2;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodePic;

    }

    //计算图片的缩放值
    private int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * bitmap转base64
     * @param bitmap
     * @return
     */
    private String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                clickBackKey = true;
                mProgressDialog.cancel();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_uploadidcard_pic;
    }


    /**网络连接**/

    /**
     * 进行到哪一步的网络请求，作用是后台获取进行到了哪一步
     */
    private void getStup () {
        Map map = new HashMap();
        map.put("code", "WT6041");

        Map<String,String> params = new HashMap<>();

        if (mEntity != null && !TextUtils.isEmpty(mEntity.getUser_biz_id())) {
            params.put("user_biz_id", mEntity.getUser_biz_id());
        }
        params.put("node_code", "3");
        params.put("node_name", "证件上传");//交易
        params.put("opr_memo", "证件上传");//资金
        params.put("opr_type",  "0");//通信

        if (mEntity != null && !TextUtils.isEmpty(mEntity.getUserId())) {
            params.put("opr_id", mEntity.getUserId());
        }

        params.put("result_comment", "证件上传");
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                showMistackDialog(ConstantUtil.JSON_ERROR, new CancelDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {

                    showMistackDialog(ConstantUtil.SERVICE_NO_DATA, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            finish();
                        }
                    });

                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String error_no = jsonObject.getString("error_no");
                    String errorInfo = jsonObject.getString("error_info");

                    if (!"0".equals(error_no)) {

                        showMistackDialog(errorInfo, new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {
                                finish();
                            }
                        });

                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    showMistackDialog(ConstantUtil.JSON_ERROR, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            finish();
                        }
                    });
                }
            }
        });
    }


    /**
     * 上传照片
     * @param imageBase64
     * @param biz_unit_id  如果biz_unit_id 是空 代表是从身份证有效期更改过来的
     * @param image_no  上传正面是6A 反面是6B
     */
    private void uploadPic(String imageBase64, String biz_unit_id, String image_no, final int requestCode, final int resultCode) {
        if (TextUtils.isEmpty(imageBase64) && mEntity != null) {
            return;
        }

        if (TextUtils.isEmpty(biz_unit_id)) {
            biz_unit_id = "1002";
        }

        Map map = new HashMap();
        map.put("code", "WT6070");

        Map<String,String> params = new HashMap<>();
        params.put("user_id", mEntity.getUserId());//
        params.put("biz_unit_id", biz_unit_id);//业务id
        params.put("image_type", ".jpg");//
        params.put("image_no", image_no);//
        params.put("image_data", imageBase64);//通信
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error_no = jsonObject.getString("error_no");
                    String error_info = jsonObject.getString("error_info");
                    String verify_id = jsonObject.getString("verify_id");

                    if (!"0".equals(error_no)) {
                        showMistackDialog(error_info, null);
                        return ;
                    }

                    if (resultCode == RESULT_OK && requestCode == FRONT_REQUEST_IMAGE_CAPTURE) {
                        tempVerifyId1 = verify_id;
                        mFrontTvResult.setText("上传身份证照片正面成功");
                    }


                    if (resultCode == RESULT_OK && requestCode == BACK_REQUEST_IMAGE_CAPTURE) {
                        tempVerifyId2 = verify_id;
                        mBackTvResult.setText("上传身份证照片反面成功");
                    }

                    if (!TextUtils.isEmpty(tempVerifyId2) && !TextUtils.isEmpty(tempVerifyId1)) {

                        mUploadIdCardBtn.setClickable(true);
                        mUploadIdCardBtn.setFocusable(true);
                        mUploadIdCardBtn.setBackgroundColor(ContextCompat.getColor(UpLoadIdCardPicActivity.this, R.color.calendarBtnColor));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });

    }

    /**
     * 身份证有效期状态修改
     */
    private void validityApplyStauts () {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("code", "WT6018");

        map1.put("user_id", mEntity.getUserId());
        map1.put("apply_id", mEntity.getApply_id());
        map1.put("apply_status", 1);
        map1.put("result_comment", "");
        map.put("params", map1);
        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_no = jsonObject.getString("error_no");
                    String error_info = jsonObject.getString("error_info");
                    if ("0".equals(error_no)) {
                        creatValidityTask();
                    } else {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.cancel();
                        }

                        showMistackDialog(error_info, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });

    }

    /**
     * 身份证有效期生成审核任务
     */
    private void creatValidityTask() {
        Map map = new HashMap();
        Map map1 = new HashMap();
        map.put("code", "WT6020");

        map1.put("userId", mEntity.getUserId());
        map1.put("applyId", mEntity.getUser_biz_id());
        map1.put("taskType", "101");
        map1.put("taskUrl", "/wtIdentityTask/");
        map.put("params", map1);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error_no = jsonObject.getString("error_no");
                    String error_info = jsonObject.getString("error_info");

                    if ("0".equals(error_no)) {


                        showMistackDialog("修改信息已提交审核，请耐心等待。", new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {
                                finish();
                            }
                        });

                    } else {
                        showMistackDialog(error_info, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }

            }
        });
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(UpLoadIdCardPicActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg, CancelDialog.PositiveClickListener listener) {
        CancelDialog.cancleDialog(UpLoadIdCardPicActivity.this, errorMsg, CancelDialog.NOT_BUY, listener, null);
    }

//    private void showMistackDialog(String errorMsg,  DialogInterface.OnClickListener onClickListener) {
//        AlertDialog alertDialog = new AlertDialog.Builder(UpLoadIdCardPicActivity.this).create();
//        alertDialog.setMessage(errorMsg);
//        alertDialog.setCancelable(false);
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", onClickListener);
//        alertDialog.show();
//    }

}
