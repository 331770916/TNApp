package com.tpyzq.mobile.pangu.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.view.View;

import com.bonree.agent.android.Bonree;
import com.bonree.agent.android.harvest.Statistics;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.navigation.NavigationActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * 启动页面
 */
public class LuncherActivity extends BaseActivity implements ICallbackResult {
    private static final String TAG = "LuncherActivity";
    private String diviceId;
    //    UnikeyUrls unikeyUrls;
    Dialog dialog;
    private Dialog loadingDialog;
    private View mView;

    @Override
    public void initView() {
//        SpUtils.putString(this,"appIP","http://106.120.112.246");
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
        initData();

    }


    private void initData() {
        //判断当前用户是否注册
        if (Db_PUB_USERS.isRegister()) {
            //从云端下载数据库自选股
            SimpleRemoteControl mSimpleRemoteControl = new SimpleRemoteControl(LuncherActivity.this);
            mSimpleRemoteControl.setCommand(new ToQuerySelfChoiceStockConnect(new QuerySelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, UserUtil.userId)));
            mSimpleRemoteControl.startConnect();
        }
        getWelcomeView(mView);
    }

    private boolean startflag = true;

    private void finishLuncher() {
        if (startflag){
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startflag = false ;
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }

    private void getWelcomeView(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finishLuncher();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Override
    public void getResult(Object result, String tag) {


        if (result instanceof String) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "" + result);

//            MistakeDialog.showDialog("" + result, LuncherActivity.this, new MistakeDialog.MistakeDialgoListener() {
//                @Override
//                public void doPositive() {
//                    getWelcomeView(mView);
//                }
//            });

        } else {
            //删除数据股所有自选股
            Db_PUB_STOCKLIST.deleteAllStocListkDatas();
            //把查询到的股票添加到数据库

            ArrayList<StockInfoEntity> stockInfoEntities = (ArrayList<StockInfoEntity>) result;

            if (stockInfoEntities != null && stockInfoEntities.size() > 0) {
                for (StockInfoEntity stockInfoEntity : stockInfoEntities) {

                    Db_PUB_STOCKLIST.addOneStockListData(stockInfoEntity);
                }
            }
            SelfChoiceStockTempData.getInstance().setAll();
//            getWelcomeView(mView);
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_luncher;
    }
}
