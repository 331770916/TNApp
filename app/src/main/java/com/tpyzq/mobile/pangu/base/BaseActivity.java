package com.tpyzq.mobile.pangu.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.keyboardlibrary.KeyboardTouchListener;
import com.android.keyboardlibrary.KeyboardUtil;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.detail.GetSearchStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.detail.ToGetSearchStockConnect;
import com.tpyzq.mobile.pangu.http.manager.NetworkManager;
import com.tpyzq.mobile.pangu.util.panguutil.SkipUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * activity 基础类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private boolean isNeedCheck = true; //判断是否需要检测，防止不停的弹框
    private static final int PERMISSON_REQUESTCODE = 0;
    protected InterfaceCollection mInterface;
    protected long oneTime = 0;
    protected long towTime = 0;
    protected String oldMsg;
    protected NetWorkUtil net;
    protected SkipUtils skip;
    protected Toast mToast;
    //需要进行检测的权限数组
    protected String[] needPermissions = {
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.BROADCAST_STICKY,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.GET_TASKS};
    public KeyboardUtil mKeyBoardUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager.addToStack(this);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setTheme(R.style.AppTheme);
        View rootView = View.inflate(this, getLayoutId(), null);
        setContentView(rootView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);     //设置键盘不挤压布局
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mInterface = InterfaceCollection.getInstance();
        net = NetWorkUtil.getInstence();
        skip = SkipUtils.getInstance();
        initView();

        if (Build.VERSION.SDK_INT<19){
            View fitView=rootView.findViewById(R.id.rl_top_bar);
            if (fitView!=null)
                ((ViewGroup)rootView).removeView(fitView);
        }
    }
    /**
     * 初始化键盘
     * @param rootLayout
     */
    public void initMoveKeyBoard(LinearLayout rootLayout, ScrollView scrollView, EditText mSearchEdit) {
        mKeyBoardUtil = new KeyboardUtil(this, rootLayout, scrollView);
        mKeyBoardUtil.setOtherEdittext(mSearchEdit);
        // monitor the KeyBarod state
        mKeyBoardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        mKeyBoardUtil.setInputOverListener(new InputOverListener());
        mSearchEdit.setOnTouchListener(new KeyboardTouchListener(mKeyBoardUtil, KeyboardUtil.INPUTTYPE_NUM_ABC, -1));
//        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                dissmissKeyboardUtil();
//            }
//        });
    }

    private void dissmissKeyboardUtil() {
        mKeyBoardUtil.hideAllKeyBoard();
    }

    public class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {
        @Override
        public void KeyBoardStateChange(int state, EditText editText) {
        }
    }
    public class InputOverListener implements KeyboardUtil.InputFinishListener{

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
            inputOver(onclickType, editText);
        }
    }

    public void inputOver(int onclickType, EditText editText){};

    public abstract void initView();

    public abstract int getLayoutId();
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    /**
     * 显示tost
     * @param content 内容
     */
    protected void showToast(String content){
        showToast(content,0,false);
    }

    protected void showToast(String content,int time,boolean hasGravity) {
        if (mToast == null) {
            mToast = Toast.makeText(CustomApplication.getContext(), content,time);
            if(hasGravity)
                mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            oldMsg = content;
            oneTime = System.currentTimeMillis();
        } else {
            towTime = System.currentTimeMillis();
            if (content.equals(oldMsg)) {
                if (towTime - oneTime > time)
                    mToast.show();
            } else {
                oldMsg = content;
                mToast.setText(content);
                mToast.show();
            }
        }
        oneTime = towTime;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
        skip.setFlag(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissonList.toArray(
                    new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }

        return needRequestPermissonList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 检测是否说有的权限都已经授权
     */
    private boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length > 0) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 退出应用
        builder.setNegativeButton(R.string.cancel,  new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     *  启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    public void destroy(){

    }

    @Override
    protected void onDestroy() {
        destroy();
        super.onDestroy();
        NetworkManager.removeFromStack(this);
        OkHttpUtil.cancelSingleRequestByTag(this.getClass().getName());
    }
}
