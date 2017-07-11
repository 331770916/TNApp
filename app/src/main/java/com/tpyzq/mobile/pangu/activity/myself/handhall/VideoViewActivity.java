package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.bairuitech.anychat.AnyChatTextMsgEvent;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * Created by Administrator on 2017/6/23.
 */

public class VideoViewActivity extends BaseActivity implements View.OnClickListener, AnyChatBaseEvent, AnyChatTextMsgEvent {


    private final int UPDATEVIDEOBITDELAYMILLIS = 200; //监听音频视频的码率的间隔刷新时间（毫秒）
    private SurfaceView mClientSurfaceView;
    private SurfaceView mServiceSurfaceView;
    private AnyChatCoreSDK mAnyChatCoreSDK;
    private int mRemoteId;

    boolean bOnPaused = false;
    private boolean bSelfVideoOpened = false; // 本地视频是否已打开
    private boolean bOtherVideoOpened = false; // 对方视频是否已打开
    private Boolean mFirstGetVideoBitrate = false; //"第一次"获得视频码率的标致
    private Boolean mFirstGetAudioBitrate = false; //"第一次"获得音频码率的标致

    private String mMessage;

    @Override
    public void initView() {
        InitSDK();

        mClientSurfaceView = (SurfaceView) findViewById(R.id.surface_local);
        mServiceSurfaceView = (SurfaceView) findViewById(R.id.surface_remote);
        findViewById(R.id.btn_endsession).setOnClickListener(this);


        Intent intent = getIntent();

        String remoteId = intent.getStringExtra("remoteId");

        if (!TextUtils.isEmpty(remoteId)) {
            mRemoteId = Integer.valueOf(remoteId);

        }

        // 如果是采用Java视频采集，则需要设置Surface的CallBack
        if (AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
            mClientSurfaceView.getHolder().addCallback(AnyChatCoreSDK.mCameraHelper);
        }

        // 如果是采用Java视频显示，则需要设置Surface的CallBack
        if (AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
            int index = mAnyChatCoreSDK.mVideoHelper.bindVideo(mServiceSurfaceView.getHolder());
            mAnyChatCoreSDK.mVideoHelper.SetVideoUser(index, mRemoteId);
        }

        mClientSurfaceView.setZOrderOnTop(true);
        mAnyChatCoreSDK.UserCameraControl(mRemoteId, 1);
        mAnyChatCoreSDK.UserSpeakControl(mRemoteId, 1);


        // 判断是否显示本地摄像头切换图标
        if (AnyChatCoreSDK
                .GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
            if (AnyChatCoreSDK.mCameraHelper.GetCameraNumber() > 1) {
                // 默认打开前置摄像头
                AnyChatCoreSDK.mCameraHelper
                        .SelectVideoCapture(AnyChatCoreSDK.mCameraHelper.CAMERA_FACING_FRONT);
            }
        } else {
            String[] strVideoCaptures = mAnyChatCoreSDK.EnumVideoCapture();
            if (strVideoCaptures != null && strVideoCaptures.length > 1) {
                // 默认打开前置摄像头
                for (int i = 0; i < strVideoCaptures.length; i++) {
                    String strDevices = strVideoCaptures[i];
                    if (strDevices.indexOf("Front") >= 0) {
                        mAnyChatCoreSDK.SelectVideoCapture(strDevices);
                        break;
                    }
                }
            }
        }

//        // 根据屏幕方向改变本地surfaceview的宽高比
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            adjustLocalVideo(true);
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            adjustLocalVideo(false);
//        }

        mAnyChatCoreSDK.UserCameraControl(-1, 1);// -1表示对本地视频进行控制，打开本地视频
        mAnyChatCoreSDK.UserSpeakControl(-1, 1);// -1表示对本地音频进行控制，打开本地音频

        handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
    }


    private void InitSDK() {
        mAnyChatCoreSDK = AnyChatCoreSDK.getInstance(this);
        mAnyChatCoreSDK.SetBaseEvent(this);
        mAnyChatCoreSDK.SetTextMessageEvent(this);
        mAnyChatCoreSDK.mSensorHelper.InitSensor(this);
        AnyChatCoreSDK.mCameraHelper.SetContext(this);

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        // 如果是采用Java视频显示，则需要设置Surface的CallBack
        if (AnyChatCoreSDK
                .GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
            int index = mAnyChatCoreSDK.mVideoHelper.bindVideo(mServiceSurfaceView.getHolder());
            mAnyChatCoreSDK.mVideoHelper.SetVideoUser(index, mRemoteId);
        }

        refreshAV();
        bOnPaused = false;
    }

    private void refreshAV() {
        mAnyChatCoreSDK.UserCameraControl(mRemoteId, 1);
        mAnyChatCoreSDK.UserSpeakControl(mRemoteId, 1);
        mAnyChatCoreSDK.UserCameraControl(-1, 1);
        mAnyChatCoreSDK.UserSpeakControl(-1, 1);
        bOtherVideoOpened = false;
        bSelfVideoOpened = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bOnPaused = true;
                mAnyChatCoreSDK.UserCameraControl(mRemoteId, 0);
                mAnyChatCoreSDK.UserSpeakControl(mRemoteId, 0);
                mAnyChatCoreSDK.UserCameraControl(-1, 0);
                mAnyChatCoreSDK.UserSpeakControl(-1, 0);
            }
        }, 2000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        mAnyChatCoreSDK.mSensorHelper.DestroySensor();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitVideoDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                int videoBitrate = mAnyChatCoreSDK.QueryUserStateInt(mRemoteId, AnyChatDefine.BRAC_USERSTATE_VIDEOBITRATE);
                int audioBitrate = mAnyChatCoreSDK.QueryUserStateInt(mRemoteId, AnyChatDefine.BRAC_USERSTATE_AUDIOBITRATE);
                if (videoBitrate > 0) {
                    //handler.removeCallbacks(runnable);
                    mFirstGetVideoBitrate = true;
                    mServiceSurfaceView.setBackgroundColor(Color.TRANSPARENT);
                }

                if (audioBitrate > 0) {
                    mFirstGetAudioBitrate = true;
                }

                if (mFirstGetVideoBitrate) {
                    if (videoBitrate <= 0) {
                        Toast.makeText(VideoViewActivity.this, "对方视频中断了!", Toast.LENGTH_SHORT).show();
                        // 重置下，如果对方退出了，有进去了的情况
                        mFirstGetVideoBitrate = false;
                    }
                }

                if (mFirstGetAudioBitrate) {
                    if (audioBitrate <= 0) {
                        Toast.makeText(VideoViewActivity.this, "对方音频中断了!", Toast.LENGTH_SHORT).show();
                        // 重置下，如果对方退出了，有进去了的情况
                        mFirstGetAudioBitrate = false;
                    }
                }

                handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void OnAnyChatConnectMessage(boolean bSuccess) {
        if (bSuccess) {
            Log.e("", "SUCCESS");
        }
    }

    @Override
    public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
        Log.e("", "OnAnyChatLoginMessage");
    }

    @Override
    public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
        Log.e("", "OnAnyChatEnterRoomMessage");
    }

    @Override
    public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
        Log.e("", "OnAnyChatOnlineUserMessage");
    }

    @Override
    public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
        if (!bEnter) {
            if (dwUserId == mRemoteId) {
                Toast.makeText(VideoViewActivity.this, "对方已离开！", Toast.LENGTH_SHORT).show();
                mRemoteId = 0;
                mAnyChatCoreSDK.UserCameraControl(dwUserId, 0);
                mAnyChatCoreSDK.UserSpeakControl(dwUserId, 0);
                bOtherVideoOpened = false;


                Intent data = new Intent();
                data.putExtra("message", mMessage);
                setResult(RESULT_OK, data);
                finish();
            }

        } else {
            if (mRemoteId != 0)
                return;
            int index = mAnyChatCoreSDK.mVideoHelper.bindVideo(mServiceSurfaceView.getHolder());
            mAnyChatCoreSDK.mVideoHelper.SetVideoUser(index, dwUserId);
            mAnyChatCoreSDK.UserCameraControl(dwUserId, 1);
            mAnyChatCoreSDK.UserSpeakControl(dwUserId, 1);
            mRemoteId = dwUserId;
        }
    }

    @Override
    public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
// 网络连接断开之后，上层需要主动关闭已经打开的音视频设备
        if (bOtherVideoOpened) {
            mAnyChatCoreSDK.UserCameraControl(mRemoteId, 0);
            mAnyChatCoreSDK.UserSpeakControl(mRemoteId, 0);
            bOtherVideoOpened = false;
        }
        if (bSelfVideoOpened) {
            mAnyChatCoreSDK.UserCameraControl(-1, 0);
            mAnyChatCoreSDK.UserSpeakControl(-1, 0);
            bSelfVideoOpened = false;
        }

        // 销毁当前界面
        Intent data = new Intent();
        data.putExtra("message", mMessage);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void OnAnyChatTextMessage(int dwFromUserid, int dwToUserid, boolean bSecret, String message) {
        if ("verified".equals(message)) {
            mMessage = "verified";
        } else if ("unverified".equals(message)) {
            mMessage = "unverified";
        }
    }

    @Override
    public void onClick(View v) {

        exitVideoDialog();
    }

    private void exitVideoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mAnyChatCoreSDK.UserCameraControl(-1, 0);
                                mAnyChatCoreSDK.UserSpeakControl(-1, 0);
                                bSelfVideoOpened = false;

                                Intent data = new Intent();
                                data.putExtra("message", mMessage);
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

}
