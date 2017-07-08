package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.myself.ChangeDepositBankAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.self.GetDepositBankAccountInfoConnect;
import com.tpyzq.mobile.pangu.interfac.IChangeDepositBankResult;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.UsefulKeyBoard;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.keybody.InputPasswordView;
import com.tpyzq.mobile.pangu.view.keybody.PopKeyBody;
import com.yzd.unikeysdk.OnInputDoneCallBack;
import com.yzd.unikeysdk.PasswordKeyboard;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/3.
 * 三存银行变更
 */

public class ChangeDepositBankActivity extends BaseActivity implements View.OnClickListener,
        DialogInterface.OnCancelListener, IChangeDepositBankResult,
        ChangeDepositBankAdapter.ClickListener, PopKeyBody.ContentListener, OnInputDoneCallBack {

    private final String TAG =  ChangeDepositBankActivity.class.getSimpleName();
    private final int ACC_PWDSTUP = 1001;                   //账户密码步骤
    private final int BANK_PWDSTUP = 1002;                  //银行密码步骤

    private Dialog mProgressDialog;
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private ChangeDepositBankAdapter  mAdapter;
    private List<Map<String, String>> mBankInfos = new ArrayList<>();
    private LinearLayout              mRootLayout;
    private PasswordKeyboard          mKeyboard;
    private PopKeyBody                mPopKeyBody;
    private String                    session;
    private String                    mBank_no;
    private String                    mCurrency;
    private String                    mExtAcc;

    private String                    mAccPwd;
    private int                       mStup = ACC_PWDSTUP;


    private GetDepositBankAccountInfoConnect mGetDepositBankAccountInfoConnect = new GetDepositBankAccountInfoConnect();

    @Override
    public void initView() {
        mRootLayout = (LinearLayout) findViewById(R.id.changeDepositRootLayout);
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("三存银行变更");

        String html = "<html>\n" +
                "<body>\n" +
                "\n" +
                "<p style=\"color:#999999\"><b>普通账户所支持的银行：</b></p>\n" +
                "\n" +
                "<p  style=\"color:#999999\">中国银行、中国农业银行、兴业银行、中国光大银行、</p>\n" +
                "<p style=\"color:#999999\">广发银行、招商银行、平安银行、中国建设银行、</p>\n" +
                "<p style=\"color:#999999\">浦发银行、中国工商银行、华夏银行、交通银行、</p>\n" +
                "<p style=\"color:#999999\">中信银行、中国民生银行、宁波银行、北京银行、</p>\n" +
                "<p style=\"color:#999999\">上海银行、中国邮政储蓄银行</p>\n" +
                "<p><font color=\"#E84242\"><small>注：融资融券客户办理信用账户存管变更，机构客户办理三方存管变更请到营业部。</small></font></p>\n" +
                "</body>\n" +
                "</html>";

        TextView describtion = (TextView) findViewById(R.id.tv_depositbank);
        describtion.setText(Html.fromHtml(html));

        ListView bankList = (ListView) findViewById(R.id.lv_depositbank);
        mAdapter = new ChangeDepositBankAdapter(this);
        mAdapter.setClickListener(this);
        bankList.setAdapter(mAdapter);

        //当没有三存银行时，要求要和ios的一样， 要有初始化数据
        Map<String, String> data = new HashMap<>();
        data.put("STATUS_NAME", "正常");
        mBankInfos.add(data);
        mAdapter.setDatas(mBankInfos);

        session = SpUtils.getString(ChangeDepositBankActivity.this, "mSession", "");
        initLoadDialog();
        mGetDepositBankAccountInfoConnect.getDepositBankAccountInfo(session, TAG, ChangeDepositBankActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public void changeBankClick(int position) {

        if (mBankInfos == null || mBankInfos.size() <= 0) {
            return;
        }

        mBank_no = mBankInfos.get(position).get("BANK_NO");
        mCurrency = mBankInfos.get(position).get("CURRENCY");
        mExtAcc = mBankInfos.get(position).get("EXT_ACC");

        initLoadDialog();

        mGetDepositBankAccountInfoConnect.checkCloseAccount(TAG, session, mBank_no,  mCurrency, this);
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
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                clickBackKey = true;
                mProgressDialog.cancel();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_depositbank;
    }

    @Override
    public void loadingClose() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void error(String errorMsg) {
        showMistackDialog(errorMsg, null);
    }

    @Override
    public void toLogin() {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(ChangeDepositBankActivity.this, ShouJiZhuCeActivity.class);
            ChangeDepositBankActivity.this.startActivity(intent);
        } else if (!Db_PUB_USERS.islogin()) {
            intent = new Intent();
            intent.setClass(ChangeDepositBankActivity.this, TransactionLoginActivity.class);
            ChangeDepositBankActivity.this.startActivity(intent);
        } else {
            intent = new Intent();
            intent.setClass(ChangeDepositBankActivity.this, TransactionLoginActivity.class);
            ChangeDepositBankActivity.this.startActivity(intent);
        }
        finish();
    }

    @Override
    public void getBankInfos(List<Map<String, String>> bankInfos) {
        if (bankInfos == null || bankInfos.size() <= 0) {
            return;
        }

        mBankInfos.clear();
        mBankInfos = bankInfos;
        mAdapter.setDatas(bankInfos);
    }

    @Override
    public void checkCloseAccount(String stauts, String error_info) {
        switch (stauts) {
            case "0":
                CancelDialog.cancleDialog(ChangeDepositBankActivity.this, "允许存管销户，确认继续吗？", -1, new CancelDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        Intent intent = new Intent();
                        intent.setClass(ChangeDepositBankActivity.this, DoChangeDepositBankActivity.class);
                        intent.putExtra("ext_acc", mExtAcc);
                        intent.putExtra("bank_no", mBank_no);
                        intent.putExtra("currency", mCurrency);
                        startActivity(intent);
                        finish();
                    }
                }, null);
                break;
            case "1":
                CancelDialog.cancleDialog(ChangeDepositBankActivity.this, "因执行账户预销户处理，需待T+1日银行回报后才允许做销户处理，确定继续吗？", -1, new CancelDialog.PositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        //弹资金密码（根据数据库里的存储）
                        showKeyBoard("请输入资金账号密码");
                    }
                }, null);
                break;
            case "2":
            case "3":
                CancelDialog.cancleDialog(ChangeDepositBankActivity.this, error_info, CancelDialog.NOT_BUY, null, null);
                break;
        }
    }

    @Override
    public void needPwd(String bankNo, String bkopenpwdFlag, String bkcancelpwdFlag, String bankName) {
        if ("0".equals(bkcancelpwdFlag)) {//不需要原卡密码
            initLoadDialog();
            mGetDepositBankAccountInfoConnect.pretreatmentCloseUser(TAG, session, bankNo, "", mAccPwd, ChangeDepositBankActivity.this);
        } else {
            mStup = BANK_PWDSTUP;
            showKeyBoard("请输入原银行卡密码");
        }
    }

    @Override
    public void closeAccountSuccess() {
        showMistackDialog("预销户处理成功，您需要隔天再次提交销户申请完成销户！", null);
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(ChangeDepositBankActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg, CancelDialog.PositiveClickListener listener) {
        CancelDialog.cancleDialog(ChangeDepositBankActivity.this, errorMsg, CancelDialog.NOT_BUY, listener, null);
    }

    /**
     * 弹出键盘
     */
    private void showKeyBoard(String passwordTitle) {
        Object object = UsefulKeyBoard.getInstance().initUserKeyBoard(true, ChangeDepositBankActivity.this, true, null);

        if (object != null && object instanceof PopKeyBody) {
            mPopKeyBody = (PopKeyBody) object;
            mPopKeyBody.clearContent();
            mPopKeyBody.setTitleText(passwordTitle);
            mPopKeyBody.setTitleColor(R.color.black);
            Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.inputpasswor);
            mPopKeyBody.setTitleImage(drawable, null, null, null);
            mPopKeyBody.show(mRootLayout);
        } else {

            try {
                mKeyboard = UniKey.getInstance().getPasswordKeyboard(ChangeDepositBankActivity.this, ChangeDepositBankActivity.this, 6, true, "custom_keyboard_view3");
                mKeyboard.show();

                TextView tv_Title = (TextView) mKeyboard.getView("tvHeadingTitle");
                tv_Title.setText(passwordTitle);

            } catch (UnikeyException e) {
                e.printStackTrace();
                Helper.getInstance().showToast(CustomApplication.getContext(), "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
            }

        }
    }

    /**** 普通自定义键盘回调 ****/

    @Override
    public void getContent(String num) {
        if (num.length() == 6) {
            mPopKeyBody.dismiss();

            if (mStup == BANK_PWDSTUP) {
                initLoadDialog();
                mGetDepositBankAccountInfoConnect.pretreatmentCloseUser(TAG, session, mBank_no, num, mAccPwd, ChangeDepositBankActivity.this);
            } else {
                mAccPwd = num;
                initLoadDialog();
                mGetDepositBankAccountInfoConnect.needPwd(TAG, session, mBank_no, ChangeDepositBankActivity.this);
            }
        }
    }

    @Override
    public void doPositive() {

    }

    /**** 加密键盘回调 ****/

    @Override
    public void getInputEncrypted(String s) {
        if (s.length() >= 6) {
            if (mStup == BANK_PWDSTUP) {
                initLoadDialog();
                mGetDepositBankAccountInfoConnect.pretreatmentCloseUser(TAG, session, mBank_no, s, mAccPwd, ChangeDepositBankActivity.this);
            } else {
                mAccPwd = s;
                initLoadDialog();
                mGetDepositBankAccountInfoConnect.needPwd(TAG, session, mBank_no, ChangeDepositBankActivity.this);
            }
        }
    }

    @Override
    public void getCharNum(int num) {
        InputPasswordView inputPasswordView = (InputPasswordView) mKeyboard.getView("inputPasswordView");
        //注意目前一定要判空，如下
        if (inputPasswordView != null) {
            String tmp = "";
            for (int i = 0; i < num; i++) {
                tmp += "*";
            }
            inputPasswordView.setText(tmp);
        }
    }
}
