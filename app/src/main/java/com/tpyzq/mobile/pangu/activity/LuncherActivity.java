package com.tpyzq.mobile.pangu.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonree.agent.android.Bonree;
import com.bonree.agent.android.harvest.Statistics;
import com.facebook.common.util.ExceptionWithNoStacktrace;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
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
import com.tpyzq.mobile.pangu.util.imageUtil.ImageUtil;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfChoiceStockTempData;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.width;


/**
 * 启动页面
 */
public class LuncherActivity extends BaseActivity implements ICallbackResult {
    private static final String TAG = "LuncherActivity";
    private static final String LuncherActivityTAG = "LuncherActivityTAG";
    private View mView;
    private Handler handler ;
    private Runnable runnable;
    private SimpleDraweeView simpleDraweeView;
    private LinearLayout titleLinearLayout;
    private TextView timeText;
    private boolean startflag = false;
    private String resultImg = "";
    int i = 3;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (i==0){
                timer.cancel();
                finishLuncher();
                return;
            }
            titleLinearLayout.setVisibility(View.VISIBLE);
            timeText.setText(i+"秒");
            i--;
            super.handleMessage(msg);
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            mHandler.sendEmptyMessage(0);
        }
    };
    @Override
    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler();
        initData();
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_ad);
//        simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        titleLinearLayout = (LinearLayout) findViewById(R.id.ll_title);
        timeText = (TextView) findViewById(R.id.tv_time);
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
        String isNewStock = SpUtils.getString(CustomApplication.getContext(),"isNewStock","");
        if(TextUtils.isEmpty(isNewStock)||!isNewStock.contains(Helper.getCurDate()))
            SpUtils.putString(CustomApplication.getContext(), "isNewStock", Helper.getCurDate());
        initEvent();
    }

    private void initEvent() {
        // 点击跳过按钮
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
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
        if (timer!=null){
            timer.cancel();
        }
    }

    private void getWelcomeView(View view) {
        //  延时操作
        runnable = new Runnable() {
            @Override
            public void run() {
                if (startflag){
//                    handler.removeCallbacks(runnable);    // 移除handle
//                    Uri uri = Uri.parse("http://img4.imgtn.bdimg.com/it/u=3552367727,879479145&fm=26&gp=0.jpg");
//                    Log.e("LuncherActivity","resultImg=="+resultImg);
                    Uri uri = Uri.parse(resultImg);
                    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {}

                        @Override
                        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            finishLuncher();
                        }
                    };

                    Postprocessor redMeshPostprocessor = new BasePostprocessor() {
                        @Override
                        public String getName() {
                            return "redMeshPostprocessor";
                        }

                        @Override
                        public void process(Bitmap bitmap) {
                            try {
                                /*for (int x = 0; x < bitmap.getWidth(); x+=2) {
                                    for (int y = 0; y < bitmap.getHeight(); y+=2) {
                                        bitmap.setPixel(x, y, Color.RED);
                                    }
                                }*/
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                int screenWidth = Helper.getScreenWidth(LuncherActivity.this);
                                int screenHeight = Helper.getScreenHeight(LuncherActivity.this);
                                if (screenHeight<height||screenWidth<width) {
                                    //压缩图片大小到满足屏幕
                                    bitmap = ImageUtil.zoomImageTo(bitmap,screenWidth,screenHeight);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            } finally {
                                timer.schedule(task,0,1000);
                            }
                        }
                    };

                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                            .setPostprocessor(redMeshPostprocessor)
                            .setResizeOptions(new ResizeOptions(Helper.getScreenWidth(LuncherActivity.this)
                                    , Helper.getScreenHeight(LuncherActivity.this)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
//                            .setUri(uri)
                            .setImageRequest(request)
                            .setTapToRetryEnabled(true)
                            .setOldController(simpleDraweeView.getController())
                            .setControllerListener(controllerListener)
                            .build();

                    simpleDraweeView.setController(controller);
                }else {
                    finishLuncher();
                }

            }
        };
        handler.postDelayed(runnable,1000*2);
    }


    @Override
    public void getResult(Object result, String tag) {
        if (result instanceof String) {
//            CentreToast.showText(this,ConstantUtil.NETWORK_ERROR);
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
}
