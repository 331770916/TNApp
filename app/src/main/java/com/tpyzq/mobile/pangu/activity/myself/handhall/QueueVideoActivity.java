package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.UpdateIdCodeValidityEntity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.DotsTextView;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/23.
 * 等待服务端接入视频页面
 */

public class QueueVideoActivity extends BaseActivity implements View.OnClickListener, AnyChatBaseEvent {

    private final String TAG = QueueVideoActivity.class.getSimpleName();

    private TextView mQueueTextView;
    private DotsTextView mDotsTextView;
    private AnyChatCoreSDK mAnyChatCoreSDK;
    private UpdateIdCodeValidityEntity mEntity;
    private String verify_id;
    private String fund_account;

    private static final int REQUEST_CODE = 1001;

    private final int LOCALVIDEOAUTOROTATION = 1; // 本地视频自动旋转控制

    private String mRoomId;
    private String mRemoteId;
    private String mUserPassword;


    @Override
    public void initView() {

        Intent intent = getIntent();
        verify_id = intent.getStringExtra("verify_id");
        fund_account = intent.getStringExtra("fund_account");
        mEntity = intent.getParcelableExtra("entity");

        TextView textView1 = (TextView) findViewById(R.id.userIdOutBtn);
        textView1.setText("退出");

        textView1.setOnClickListener(this);
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);


        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("视频验证");

        mQueueTextView = (TextView) findViewById(R.id.queueTextView);
        mDotsTextView = (DotsTextView) findViewById(R.id.dots);

        mAnyChatCoreSDK =  AnyChatCoreSDK.getInstance(this);
        // 设置基本回调事件接收
        mAnyChatCoreSDK.SetBaseEvent(this);
        mAnyChatCoreSDK.InitSDK(android.os.Build.VERSION.SDK_INT, 0);// 初始化sdk
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION, LOCALVIDEOAUTOROTATION);

        queryUserWaitInfo(mEntity.getUserId());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAnyChatCoreSDK.SetBaseEvent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelSingleRequestByTag(TAG);
        mAnyChatCoreSDK.LeaveRoom(-1);
        mAnyChatCoreSDK.Logout();
        mAnyChatCoreSDK.Release();
        System.gc();
        System.runFinalization();
    }

    private void exitQueue() {
        OutQueue(mEntity.getUserId());
        OkHttpUtil.cancelSingleRequestByTag(TAG);
        mAnyChatCoreSDK.LeaveRoom(-1);
        mAnyChatCoreSDK.Logout();
        mAnyChatCoreSDK.Release();
        System.gc();
        System.runFinalization();
        QueueVideoActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
            case R.id.userIdOutBtn:
                CancelDialog.cancleDialog(QueueVideoActivity.this, new CancelDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        exitQueue();
                    }
                });
                break;
        }
    }

    ////连接服务器触发（connet），“bSuccess==true”连接服务器成功，反之连接服务器失败
    @Override
    public void OnAnyChatConnectMessage(boolean bSuccess) {
        mQueueTextView.setTextColor(ContextCompat.getColor(QueueVideoActivity.this, R.color.hideTextColor));
        mQueueTextView.setText("正在连接，请稍后");
    }

    //用户登录触发(login),dwUserId是服务器为客户端分配的唯一标识userid，dwErrorCode==0表示登录成功，其他值为登录服务器失败的错误代码
    @Override
    public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
        if (dwErrorCode == 0) {
            if (!TextUtils.isEmpty(mRoomId)) {
                mAnyChatCoreSDK.EnterRoom(Integer.valueOf(mRoomId), mUserPassword);
            }
        } else {
            showMistackDialog("登录失败", null);
        }
    }

    ////进入房间触发，dwRoomId为房间号，dwErrorCode==0表示进入房间成功，其他值为进入房间失败的错误代码
    @Override
    public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
        if (dwErrorCode == 0) {
            Intent intent = new Intent();
            intent.setClass(QueueVideoActivity.this, VideoView.class);
            intent.putExtra("remoteId", mRemoteId);
            startActivityForResult(intent, REQUEST_CODE);

        } else {
            showMistackDialog("进入房间失败", null);
        }
    }

    // 当前房间在线用户消息，进入房间成功后调用一次。dwUserNum当前房间总人数（包含自己）
    @Override
    public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {}

    // 当前房间用户离开或者进入房间触发这个回调，dwUserId用户 id," bEnter==true"表示进入房间,反之表示离开房间
    @Override
    public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {}

    //跟服务器网络断触发该消息。收到该消息后可以关闭音视频以及做相关提示工作
    @Override
    public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
        mAnyChatCoreSDK.LeaveRoom(-1);
        mAnyChatCoreSDK.Logout();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            CancelDialog.cancleDialog(QueueVideoActivity.this, new CancelDialog.PositiveClickListener() {
                @Override
                public void onPositiveClick() {
                    exitQueue();
                }
            });

            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String message = data.getStringExtra("message");
            Intent intent = new Intent();


            if ("verified".equals(message)) {
                intent.putExtra("userId", mEntity.getUserId());
                intent.putExtra("fundAccount", fund_account);
                intent.putExtra("user_biz_id", mEntity.getUser_biz_id());
            }

            intent.setClass(QueueVideoActivity.this, VideoCheckResultActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_queue_video;
    }

    /** 网络连接 **/

    private void queryUserWaitInfo(final String userId) {
        Map map = new HashMap();
        map.put("code", "queryUserWaitInfo");

        Map<String,String> params = new HashMap<>();
        params.put("queryFlag", "0");
        params.put("userId", userId);
        params.put("rnd", "" + System.currentTimeMillis());
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_USERINFO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String errorNo = jsonObject.getString("errorNo");
                    String errorInfo = jsonObject.getString("errorInfo");

                    if (!"0".equals(errorNo)) {
                        showMistackDialog(errorInfo, null);
                        return;
                    }

                    if (!"0".equals(errorNo)) {
                        showMistackDialog(errorInfo, null);
                        return;
                    }

                    String status = jsonObject.getString("status");
                    String waitPosition = jsonObject.getString("waitPosition");

                    String str1 = "当前共有";

                    String str2 = "排队,请稍后";

                    waitPosition = waitPosition + "人";

                    SpannableString ss = new SpannableString(str1 + waitPosition + str2);

                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(QueueVideoActivity.this, R.color.hideTextColor)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(QueueVideoActivity.this, R.color.calendarBtnColor)), str1.length(), (str1 + waitPosition).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(QueueVideoActivity.this, R.color.hideTextColor)), (str1 + waitPosition).length(), (str1 + waitPosition + str2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    mQueueTextView.setText(ss);
                    mDotsTextView.start();

                    if ("1".equals(status)) {
                        String anyChatStreamPort = jsonObject.getString("anyChatStreamPort");
                        String emp_id = jsonObject.getString("emp_id");
                        String roomId = jsonObject.getString("roomId");
                        String anyChatStreamIpOut = jsonObject.getString("anyChatStreamIpOut");
                        String userName = jsonObject.getString("userName");
                        mUserPassword = jsonObject.getString("loginPwd");

                        //生成的对方id需要加20000000 固定死的
                        int tempRemoteId = Integer.valueOf(emp_id) + 20000000;
                        mRemoteId = String.valueOf(tempRemoteId);
                        mRoomId = roomId;


                        if (!TextUtils.isEmpty(anyChatStreamPort)) {
                            mAnyChatCoreSDK.Connect(anyChatStreamIpOut, Integer.valueOf(anyChatStreamPort)); // 连接服务器
                            mAnyChatCoreSDK.Login(userName, mUserPassword);// 登录
                        }

                    } else {
                        try {
                            Thread.sleep(1000);
                            queryUserWaitInfo(userId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });

    }

    /**
     * 退出视频轮询
     */
    private void OutQueue(String userId) {
        Map map = new HashMap();
        map.put("code", "outQueue");

        Map<String,String> params = new HashMap<>();
        params.put("userId", userId);
        map.put("params", params);
        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_USERINFO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }


    private void showMistackDialog(String errorMsg,  DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(QueueVideoActivity.this).create();
        alertDialog.setMessage(errorMsg);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", onClickListener);
        alertDialog.show();
    }
}
