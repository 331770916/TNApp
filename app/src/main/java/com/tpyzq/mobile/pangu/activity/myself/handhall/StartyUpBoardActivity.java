package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.fragment.CYBBLFragment;
import com.tpyzq.mobile.pangu.activity.myself.handhall.fragment.FXJSFragment;
import com.tpyzq.mobile.pangu.activity.myself.handhall.fragment.KTCGFragment;
import com.tpyzq.mobile.pangu.activity.myself.handhall.fragment.KZQFragment;
import com.tpyzq.mobile.pangu.activity.myself.handhall.fragment.LXRFragment;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.PartnerInfoEntity;
import com.tpyzq.mobile.pangu.data.SecondContactsEntity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.self.StartUpBoardActivityPresenter;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

/**
 * Created by tianchen on 2017/7/6.
 * 创业板转签
 */

public class StartyUpBoardActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener{

    public static final String TAG = StartyUpBoardActivity.class.getSimpleName();
    private CYBBLFragment cybblFragment;
    private KZQFragment kzqFragment;
    private LXRFragment lxrFragment;
    private FXJSFragment fxjsFragment;
    private KTCGFragment ktcgFragment;
    private Fragment[] fragment;
    private PartnerInfoEntity partnerInfo;
    private SecondContactsEntity secondContacts;
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private Dialog loading;
    private boolean endflag;
    private SecondInfo secondInfo;
    StartUpBoardActivityPresenter presenter;

    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("创业板");

        initLoadDialog();
        presenter = new StartUpBoardActivityPresenter(this);
        cybblFragment = CYBBLFragment.newInstance(presenter);
        kzqFragment = KZQFragment.newInstance(presenter);
        lxrFragment = LXRFragment.newInstance(presenter);
        fxjsFragment = FXJSFragment.newInstance(presenter);
        ktcgFragment = KTCGFragment.newInstance(presenter);
        fragment = new Fragment[]{cybblFragment, kzqFragment, lxrFragment, fxjsFragment, ktcgFragment};
        startFragment(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                setBack();
                break;
        }
    }

    public void setLoading(boolean flag) {
        if (flag) {
            loading.show();
        } else {
            loading.dismiss();
        }
    }

    public void setHint(String hint) {
        if (loading.isShowing()){
            loading.dismiss();
        }
        showDialog(hint);
    }



    private void showDialog(String msg){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),StartyUpBoardActivity.class.toString());
        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
            @Override
            public void confirmOnclick() {
                   finish();
                   customCenterDialog.dismiss();
                }
            });
    }

    public void startFragment(int position) {
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.animator_enter, R.anim.animator_exit, R.anim.animator_enter, R.anim.animator_exit)
                .addToBackStack("OtherFragment")
                .replace(R.id.fl_content, fragment[position])
                .commit();
    }

    @Override
    public void onBackPressed() {
        setBack();
    }

    public PartnerInfoEntity getPartnerInfo() {
        return partnerInfo;
    }

    public void setPartnerInfo(PartnerInfoEntity partnerInfo) {
        this.partnerInfo = partnerInfo;
    }

    public SecondContactsEntity getSecondContacts() {
        return secondContacts;
    }

    public void setSecondContacts(SecondContactsEntity secondContacts,SecondInfo secondInfo) {
        this.secondContacts = secondContacts;
        secondInfo.getSecondContacts(secondContacts);
    }

    public void setEndflag(boolean endflag) {
        this.endflag = endflag;
    }

    private void setBack() {
        int top = getSupportFragmentManager().getBackStackEntryCount();
        if (top > 2) {
            if (!endflag) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (loading != null && loading.isShowing()) {
                clickBackKey = true;
                loading.cancel();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initLoadDialog() {
        loading = LoadingDialog.initDialog(StartyUpBoardActivity.this, "正在加载...");
        loading.setCanceledOnTouchOutside(false);
        loading.setOnCancelListener(this);
        loading.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_starty_up_board;
    }

    public interface SecondInfo{
        void getSecondContacts(SecondContactsEntity secondContacts);
    }
}
