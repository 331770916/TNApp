package com.tpyzq.mobile.pangu.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.HomeFragment;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.activity.myself.MySelfFragment;
import com.tpyzq.mobile.pangu.activity.trade.TradeFragment;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.service.GetConfigService;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.ExitDialog;
import com.tpyzq.mobile.pangu.view.dialog.HintDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.VersionDialog;
import com.tpyzq.mobile.pangu.view.radiobutton.MyRadioButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.realm.annotations.Index;
import okhttp3.Call;



/**
 * Created by zhangwenbo on 2016/6/16.
 */
public class IndexActivity extends BaseActivity {

    private long mnLastBackKeyPressTime = 0;
    private RadioGroup radioGroup;
    private MyRadioButton homeRadioBtn, marketRadioBtn, transactionRaioBtn, mySelfRaioBtn;

    private Fragment[] tab_fragment=new Fragment[4];
    private FragmentManager manager;
    private int tabIds[] = new int[]{R.id.homeRadioBtn, R.id.marketRadioBtn, R.id.transactionRadioBtn, R.id.informationRadioBtn};
    private ExitDialog dialog;
    private Boolean flag = false;

    @Override
    public void initView() {
        //注册广播
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.pangu.showdialog");
        BroadcastReceiver broadcastReceiver=new SitesChangeRecever();
        registerReceiver(broadcastReceiver, filter);
        CustomApplication.getInstance().addActivity(this);
        //开启站点服务
        Intent serviceIntent = new Intent(this, GetConfigService.class);
        startService(serviceIntent);
        for (Fragment frag:tab_fragment) {
            if (frag!=null){
                tab_fragment=new Fragment[4];
                break;
            }
        }
        getVersionData();       //判断是否是 最新版本
        getHintCode();
        SpUtils.putBoolean(this, "burse", false);
        SpUtils.putBoolean(this, "account", true);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        homeRadioBtn = (MyRadioButton) findViewById(R.id.homeRadioBtn);
        marketRadioBtn = (MyRadioButton) findViewById(R.id.marketRadioBtn);
        transactionRaioBtn = (MyRadioButton) findViewById(R.id.transactionRadioBtn);
        mySelfRaioBtn = (MyRadioButton) findViewById(R.id.informationRadioBtn);

        homeRadioBtn.setChecked(true);
        replaceFragment(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < tabIds.length; i++) {
                    if (tabIds[i] == checkedId) {
                        replaceFragment(i);
                        break;
                    }
                }
            }
        });
    }

    /**
     * 提示框弹窗
     */
    private void getHintCode() {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map.put("funcid", "800123");
        map.put("token", "");
        map.put("parms", map1);
        map1.put("ACCOUNT", UserUtil.userId == null ? "" :UserUtil.userId);
        map1.put("OBJECTIVE", map2);
        String warning_push_time = SpUtils.getString(this, "warning_push_time", "");
        String newshare_push_time = SpUtils.getString(this, "newshare_push_time", "");
        String inform_push_time = SpUtils.getString(this, "inform_push_time", "");
        map2.put("1", warning_push_time);
        map2.put("2", newshare_push_time);
        map2.put("3", inform_push_time);

        NetWorkUtil.getInstence().okHttpForPostString(this, ConstantUtil.URL_ZX_GS, map, new StringCallback() {
            private String mInform_push_time;
            private String token_inform;
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("", e.toString());
            }

            @Override

            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONArray list_Inform = data.getJSONArray(2);
                        //通知公告
                        if (list_Inform != null && list_Inform.length() > 0) {
                            for (int j = 0; j < list_Inform.length(); j++) {
                                token_inform = list_Inform.getJSONObject(0).optString("TITLE");
                                String push_time_Inform = list_Inform.getJSONObject(0).optString("PUSH_TIME");
                                mInform_push_time = list_Inform.getJSONObject(j).optString("CONTENE");
                                SpUtils.putString(IndexActivity.this, "inform_push_time", push_time_Inform);
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(token_inform)) {
                        HintDialog hintdialog = new HintDialog(IndexActivity.this, token_inform, mInform_push_time);
                        hintdialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 输入邀请码弹窗
     */
//    private void getInviteCode() {
//        InviteCodeDialog inviteCodeDialog = new InviteCodeDialog(this);
//        inviteCodeDialog.show();
//    }

    @Override
    protected void onDestroy() {
        try {
            if(dialog!=null)
            {
                dialog.dismiss();
            }
            SpUtils.putBoolean(this,"isFirstInToHomeMoreGride", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_index;
    }

    // 切换main的碎片的方法
    public void replaceFragment(int i) {
        // 碎片管理器
        manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        hideAllFragments(ft);
        switch (i) {
            case 0:
                if (tab_fragment[0] == null) {
                    tab_fragment[0] = new HomeFragment();
                    ((HomeFragment) tab_fragment[0]).setJumPageListener(new HomeFragment.JumpPageListener() {
                        @Override
                        public void onCheckedChanged(int position) {

                            if (position == 0) {
                                homeRadioBtn.setChecked(true);
                            } else if (position == 1) {
                                marketRadioBtn.setChecked(true);
                            } else if (position == 2) {
                                transactionRaioBtn.setChecked(true);
                            } else if (position == 3) {
                                mySelfRaioBtn.setChecked(true);
                            }
                        }
                    });
                    ft.add(R.id.fragmentLayout, tab_fragment[0]);
                } else {
                    ft.show(tab_fragment[0]);
                }
                BRutil.menuSelect("z001");
                break;
            case 1:
                if (tab_fragment[1] == null) {
                    tab_fragment[1] = new MarketFragment();
                    ft.add(R.id.fragmentLayout, tab_fragment[1]);
                } else {
                    ft.show(tab_fragment[1]);
                }

                BRutil.menuSelect("z002");
                break;
            case 2:
                if (tab_fragment[2] == null) {
                    tab_fragment[2] = new TradeFragment();
                    ft.add(R.id.fragmentLayout, tab_fragment[2]);
                } else {
                    ft.show(tab_fragment[2]);
                }
                BRutil.menuSelect("z003");
                break;
            case 3:
                if (tab_fragment[3] == null) {
                    tab_fragment[3] = new MySelfFragment();
                    ft.add(R.id.fragmentLayout, tab_fragment[3]);
                } else {
                    ft.show(tab_fragment[3]);
                }
                BRutil.menuSelect("z004");
                break;
            default:
                break;
        }
        ft.commitAllowingStateLoss();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (tab_fragment[0] != null) {
            ft.hide(tab_fragment[0]);
        }
        if (tab_fragment[1] != null) {
            ft.hide(tab_fragment[1]);
        }
        if (tab_fragment[2] != null) {
            ft.hide(tab_fragment[2]);
        }
        if (tab_fragment[3] != null) {
            ft.hide(tab_fragment[3]);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (tab_fragment[0] == null && fragment instanceof HomeFragment) {
            tab_fragment[0] = fragment;
        } else if (tab_fragment[1] == null && fragment instanceof MarketFragment) {
            tab_fragment[1] = fragment;
        } else if (tab_fragment[2] == null && fragment instanceof TradeFragment) {
            tab_fragment[2] = fragment;
        } else if (tab_fragment[3] == null && fragment instanceof MySelfFragment) {
            tab_fragment[3] = fragment;
        }
    }


    @Override
    public void onBackPressed() {
        dialog = new ExitDialog(this);
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 版本更新
     */
    private void getVersionData() {
        HashMap map400101 = new HashMap();
        HashMap map400101_1 = new HashMap();
        map400101_1.put("versionType", "2");
        map400101_1.put("urltpye", "1");
        map400101.put("funcid", "400101");
        map400101.put("token", "");
        map400101.put("parms", map400101_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_SXRZ, map400101, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        String result = jsonObject.getString("result");
                        JSONObject joResult = new JSONObject(result);
                        String forceIsupdate = joResult.getString("forceIsupdate");   //是否强制更新
                        String versionNumber = joResult.getString("versionNumber");   //版本号
                        String apkAddress = joResult.getString("apkAddress");         //下载地址
                        if (TextUtils.isEmpty(versionNumber)) {
                            return;
                        }
                        String[] versionCode = versionNumber.split("\\.");
                        String[] thisVersionCode = APPInfoUtils.getVersionName(IndexActivity.this).split("\\.");
                        //如果 版本号  与  当前版本号 相同 删除安装包
                        if (Double.parseDouble(versionCode[0]) > Double.parseDouble(thisVersionCode[0])) {
                            Dialog mDialog = new VersionDialog(IndexActivity.this, apkAddress, forceIsupdate, versionNumber);
                            mDialog.show();
                        } else if(Double.parseDouble(versionCode[0]) == Double.parseDouble(thisVersionCode[0])){
                            if (Double.parseDouble(versionCode[1]) > Double.parseDouble(thisVersionCode[1])) {
                                Dialog mDialog = new VersionDialog(IndexActivity.this, apkAddress, forceIsupdate, versionNumber);
                                mDialog.show();
                            } else if (Double.parseDouble(versionCode[1]) == Double.parseDouble(thisVersionCode[1])) {
                                if (Double.parseDouble(versionCode[2]) > Double.parseDouble(thisVersionCode[2])) {
                                    Dialog mDialog = new VersionDialog(IndexActivity.this, apkAddress, forceIsupdate, versionNumber);
                                    mDialog.show();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 检测屏幕的方向：纵向或横向
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏， 在此处添加额外的处理代码

        } else if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            //当前为竖屏， 在此处添加额外的处理代码
        }
        //检测实体键盘的状态：推出或者合上
        if (newConfig.hardKeyboardHidden
                == Configuration.HARDKEYBOARDHIDDEN_NO) {
            //实体键盘处于推出状态，在此处添加额外的处理代码
        } else if (newConfig.hardKeyboardHidden
                == Configuration.HARDKEYBOARDHIDDEN_YES) {
            //实体键盘处于合上状态，在此处添加额外的处理代码
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    class SitesChangeRecever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.pangu.showdialog".equalsIgnoreCase(intent.getAction())) {
                MistakeDialog.showDialog("服务器异常", IndexActivity.this,null);
            }
        }
    }
}
