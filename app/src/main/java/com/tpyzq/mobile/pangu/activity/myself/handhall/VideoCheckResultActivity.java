package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;

/**
 * Created by zhangwenbo on 2017/6/23.
 */

public class VideoCheckResultActivity extends BaseActivity implements View.OnClickListener{

    private String mUserId;
    private String mFundAccount;
    private String mUser_biz_id;
    private TextView tv_validate_status;

    @Override
    public void initView() {
        tv_validate_status = (TextView) findViewById(R.id.tv_validate_status);
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("视频验证");

        TextView textView1 = (TextView) findViewById(R.id.userIdOutBtn);
        textView1.setText("联系客服");
        textView1.setOnClickListener(this);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");
        mFundAccount = intent.getStringExtra("fundAccount");
        mUser_biz_id = intent.getStringExtra("user_biz_id");

        if (!TextUtils.isEmpty(mUser_biz_id) && !TextUtils.isEmpty(mFundAccount) && !TextUtils.isEmpty(mUserId)) {
            initSuccessTop();
        } else {
            initFailedTop();
        }
    }

    /**
     * 加载验证成功页面布局
     */
    private void initSuccessTop() {
        ImageView imageView = (ImageView) findViewById(R.id.img_title_tag);
        tv_validate_status.setText("审核成功");
        imageView.setImageResource(R.mipmap.video_check_success);
        findViewById(R.id.videoResultSuccess).setVisibility(View.VISIBLE);
        findViewById(R.id.videoCheckSuccess_Next).setOnClickListener(this);
    }

    /**
     * 加载验证失败页面布局
     */
    private void initFailedTop () {
        ImageView imageView = (ImageView) findViewById(R.id.img_title_tag);
        imageView.setImageResource(R.mipmap.video_check_failed);
        tv_validate_status.setText("审核失败");
        findViewById(R.id.videoResultFailed).setVisibility(View.VISIBLE);
        findViewById(R.id.videoCheckFailed_restartCommite).setOnClickListener(this);
        findViewById(R.id.videoCheckFailed_out).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                CancelDialog.cancleDialog(VideoCheckResultActivity.this, new CancelDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        finish();
                    }
                });

                break;
            case R.id.userIdOutBtn:
                break;
            case R.id.videoCheckSuccess_Next:
                Intent intent = new Intent();
                intent.putExtra("userId", mUserId);
                intent.putExtra("fundAccount", mFundAccount);
                intent.putExtra("user_biz_id", mUser_biz_id);
                intent.setClass(VideoCheckResultActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.videoCheckFailed_restartCommite:
                Intent intent1 = new Intent();
                intent1.setClass(VideoCheckResultActivity.this, FrogetTransactionPwdActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.videoCheckFailed_out:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_result;
    }
}
