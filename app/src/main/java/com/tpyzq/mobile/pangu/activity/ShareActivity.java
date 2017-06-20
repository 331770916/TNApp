package com.tpyzq.mobile.pangu.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;



public class ShareActivity extends Activity implements View.OnClickListener{
    private LinearLayout llWeiXin,llPengYQ,llQQ,llWeiBo;
    private TextView tvShareQuXiao;
    private Activity activity;
    private String url;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
//                Toast.makeText(activity, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(activity, platform + " 邀请成功啦", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(activity, platform + " 邀请失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(activity, platform + " 邀请取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setWindowAnimations(R.style.AnimationPreview);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        setContentView(R.layout.share_dialog);
        initView();
    }
    public  void initView(){
        llWeiXin = (LinearLayout) findViewById(R.id.llWeiXin);              //微信
        llPengYQ = (LinearLayout) findViewById(R.id.llPengYQ);              //朋友圈
        llQQ = (LinearLayout) findViewById(R.id.llQQ);                       //QQ
        llWeiBo = (LinearLayout) findViewById(R.id.llWeiBo);                //微博
        tvShareQuXiao = (TextView) findViewById(R.id.tvShareQuXiao);       //取消

        /**
         * 目前  QQ 和 微博  还不能分享  暂时隐藏
         */
        llQQ.setVisibility(View.GONE);
        llWeiBo.setVisibility(View.GONE);

        llWeiXin.setOnClickListener(this);
        llPengYQ.setOnClickListener(this);
        llQQ.setOnClickListener(this);
        llWeiBo.setOnClickListener(this);
        tvShareQuXiao.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        UMImage image = new UMImage(ShareActivity.this, R.mipmap.share_logo);
        switch (v.getId()){
            case R.id.llWeiXin:
                new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withTitle("生活涨升相伴,我的投资太牛")
                        .withText("--分享自太牛")
                        .withMedia(image)
                        .withTargetUrl(url)
                        .share();
                finish();
                break;

            case R.id.llPengYQ:
                new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withTitle("生活涨升相伴,我的投资太牛")
                        .withText("--分享自太牛")
                        .withMedia(image)
                        .withTargetUrl(url)
                        .share();
                finish();
                break;

            case R.id.llQQ:
                /*new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                        .withTitle("生活涨升相伴,我的投资太牛")
                        .withText("--分享自太牛")
                        .withMedia(image)
                        .withTargetUrl(url)
                        .share();*/
                break;
            case R.id.llWeiBo:
                new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withTitle("生活涨升相伴,我的投资太牛")
                        .withText("--分享自太牛")
                        .withMedia(image)
                        .withTargetUrl(url)
                        .share();
                finish();
                break;

            case R.id.tvShareQuXiao:
                finish();          //销毁
                break;

        }
    }
}
