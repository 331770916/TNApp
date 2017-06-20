package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * 作者：刘泽鹏 on 2016/10/24 16:40
 * 分享  微信 QQ 微博
 */
public class ShareDialog extends BaseDialog implements View.OnClickListener {

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

    public ShareDialog(Activity activity) {
        super(activity);

        this.activity = activity;
    }

    public void setUrl(String url){
        this.url = url;
    }



    @Override
    public int getLayoutId() {
        return R.layout.share_dialog;
    }

    @Override
    public void setView() {
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
    }

    @Override
    public void initData() {
        llWeiXin.setOnClickListener(this);
        llPengYQ.setOnClickListener(this);
        llQQ.setOnClickListener(this);
        llWeiBo.setOnClickListener(this);
        tvShareQuXiao.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        UMImage image = new UMImage(context, R.mipmap.share_logo);
        switch (v.getId()){
            case R.id.llWeiXin:
                new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withTitle("生活涨升相伴,我的投资太牛")
                        .withText("--分享自太牛")
                        .withMedia(image)
                        .withTargetUrl(url)
                        .share();
                this.dismiss();
                break;

            case R.id.llPengYQ:
                new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withTitle("生活涨升相伴,我的投资太牛")
                        .withText("--分享自太牛")
                        .withMedia(image)
                        .withTargetUrl(url)
                        .share();
                this.dismiss();
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
//                new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
//                        .withTitle("生活涨升相伴,我的投资太牛")
//                        .withText("--分享自太牛")
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                this.dismiss();
                break;

            case R.id.tvShareQuXiao:
                dismiss();          //销毁
                break;

        }
    }
}
