package com.tpyzq.mobile.pangu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonree.agent.android.Bonree;
import com.bonree.agent.android.harvest.Statistics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.navigation.NavigationActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.login.QueryAdForLuncherConnect;
import com.tpyzq.mobile.pangu.http.doConnect.login.ToQueryAdForLuncherConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.QuerySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToQuerySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.FileUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfChoiceStockTempData;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;



/**
 * 启动页面
 */
public class LuncherActivity extends BaseActivity implements ICallbackResult {
    private static final String TAG = "LuncherActivity";
    private static final String LuncherActivityTAG = "LuncherActivityTAG";
    private View mView;
    private Handler handler ;
    private Runnable runnable;
    private ImageView isShowImg;
    private SimpleDraweeView simpleDraweeView;
    private LinearLayout titleLinearLayout;
    private TextView timeText;
    private MyTimeCount myTimeCount;
    private boolean startflag = false;
    private String resultImg = "";

    @Override
    public void initView() {
        handler = new Handler();
        initData();
        isShowImg = (ImageView) findViewById(R.id.show_view);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_ad);
        titleLinearLayout = (LinearLayout) findViewById(R.id.ll_title);
        timeText = (TextView) findViewById(R.id.tv_time);
        simpleDraweeView.setImageResource(R.mipmap.luncher);
        CustomApplication.getInstance().addActivity(this);
        File _file = Helper.getExternalDir(this, "ImageView");
        if (_file != null) {
            Helper.delete(_file);
        }
        String apk_abs = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pangu/apk/";
        File apk = new File(apk_abs + "new.apk");
        try {
            FileUtil.deleteFile(apk);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //测试环境
        Bonree.withApplicationToken(ConstantUtil.BORY_APPID).withConfigUrl(ConstantUtil.BORY).withNougatEnable(true).start(this);
        Statistics.onMemberId(KeyEncryptionUtils.getInstance().localDecryptMobile());
        //设置默认持仓股票不显示
//        SpUtils.putString(CustomApplication.getContext(), FileUtil.APPEARHOLD, FileUtil.HOLD_DISAPPEAR);
        SpUtils.putBoolean(CustomApplication.getContext(), ConstantUtil.MARKET_TAG, false);
        String file = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            FileUtil.createFolder(file + "/Pangu/");
            FileUtil.createFolder(file + "/Pangu/headphoto/");
            FileUtil.createFolder(file + "/Pangu/screenshots/");
            FileUtil.createFolder(file + "/Pangu/pdf/");
            FileUtil.createFolder(file + "/Pangu/apk/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mView = findViewById(R.id.luncherLayout);
        UserEntity userEntity = new UserEntity();
        userEntity.setIslogin("false");
        userEntity.setCertification("false");
        Db_PUB_USERS.UpdateCertification(userEntity);
        Db_PUB_USERS.UpdateIslogin(userEntity);
        SpUtils.putString(CustomApplication.getContext(), "mSession", "");
        SpUtils.putString(CustomApplication.getContext(), "mDivnum", "false");
        SpUtils.putString(CustomApplication.getContext(), "mDivnum_InformActivity", "false");
        SpUtils.putString(CustomApplication.getContext(), "mDivnum_NewSharesTips", "false");
        SpUtils.putString(CustomApplication.getContext(), "mDivnum_PricesPrompt", "false");
        initEvent();
    }

    private void initEvent() {
        // 点击跳过按钮
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTimeCount.onFinish();
            }
        });
    }


    private void initData() {
        SimpleRemoteControl mSimpleRemoteControls = new SimpleRemoteControl(LuncherActivity.this);
        mSimpleRemoteControls.setCommand(new ToQueryAdForLuncherConnect(new
                QueryAdForLuncherConnect(LuncherActivityTAG,"","20","","")));
        mSimpleRemoteControls.startConnect();
        //判断当前用户是否注册
        if (Db_PUB_USERS.isRegister()) {
            //从云端下载数据库自选股
            SimpleRemoteControl mSimpleRemoteControl = new SimpleRemoteControl(LuncherActivity.this);
            mSimpleRemoteControl.setCommand(new ToQuerySelfChoiceStockConnect(new QuerySelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, UserUtil.userId)));
            mSimpleRemoteControl.startConnect();
        } else {
            Db_PUB_STOCKLIST.initUnregisterData();
        }
        getWelcomeView(mView);
    }


    private void finishLuncher() {

            boolean isFirstTag = SpUtils.getBoolean(this, NavigationActivity.FIRST_INTO_APP, false);
            Intent intent = new Intent();
            if (isFirstTag) {
                intent.setClass(this, IndexActivity.class);
            } else {
                intent.setClass(LuncherActivity.this, NavigationActivity.class);
            }
            startflag = false;
            startActivity(intent);
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        startflag = false ;
        OkHttpUtil.cancelSingleRequestByTag(TAG);
        OkHttpUtil.cancelSingleRequestByTag(LuncherActivityTAG);
        if (myTimeCount!=null){
            myTimeCount.cancel();
        }
    }

    private void getWelcomeView(View view) {

        //  延时操作
        runnable = new Runnable() {
            @Override
            public void run() {
                if (startflag){
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        handler.removeCallbacks(runnable);    // 移除handle

                    titleLinearLayout.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse(resultImg);
                    simpleDraweeView.setImageURI(uri);
                    simpleDraweeView.setVisibility(View.VISIBLE);

                    isShowImg.setVisibility(View.GONE);
                    myTimeCount = new MyTimeCount(1000*4,1000);
                    myTimeCount.start();
                }else {
                    finishLuncher();
                }

            }
        };
        handler.postDelayed(runnable,1000*3);
    }


    @Override
    public void getResult(Object result, String tag) {


        if (result instanceof String) {
            CentreToast.showText(this,ConstantUtil.NETWORK_ERROR);
        } else {
            if (TAG.equals(tag)){
                deleteData(result);
            }else if (LuncherActivityTAG.equals(tag)){
                cancelFinish((Map<String,String>)result);
            }
        }
    }

    private void cancelFinish(Map<String,String> result){

        if (!TextUtils.isEmpty(result.get("adUrl"))){
            resultImg = result.get("adUrl");
            startflag = true;
        }else {
            startflag = false;
        }
    }


    private void deleteData(Object result){
        //删除数据股所有自选股
        Db_PUB_STOCKLIST.deleteAllStocListkDatas();
        //把查询到的股票添加到数据库

        ArrayList<StockInfoEntity> stockInfoEntities = (ArrayList<StockInfoEntity>) result;

        if (stockInfoEntities != null && stockInfoEntities.size() > 0) {
            for (StockInfoEntity stockInfoEntity : stockInfoEntities) {

                stockInfoEntity.setStock_flag(StockTable.STOCK_OPTIONAL);
                Db_PUB_STOCKLIST.addOneStockListData(stockInfoEntity);
            }
        }
        SelfChoiceStockTempData.getInstance().setAll();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_luncher;
    }


    /**
     * 短信 语音 倒计时
     */
    class MyTimeCount extends CountDownTimer {

        public MyTimeCount(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onTick(long millisUntilFinished) {  //计时过程显示
            timeText.setText(millisUntilFinished / 1000+"  秒");
        }

        @Override
        public void onFinish() {//计时完毕时触发
            finishLuncher();
        }
    }

}
