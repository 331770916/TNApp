package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.PdfActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.self.GetDepositBankAccountInfoConnect;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.NoSoftInputEditText;
import com.tpyzq.mobile.pangu.util.keyboard.UsefulKeyBoard;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.DownloadDocPdfDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.keybody.InputPasswordView;
import com.tpyzq.mobile.pangu.view.keybody.PopKeyBody;
import com.yzd.unikeysdk.OnInputDoneCallBack;
import com.yzd.unikeysdk.PasswordKeyboard;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;


/**
 * Created by zhangwenbo on 2017/7/5.
 * 做三存银行变更
 */

public class DoChangeDepositBankActivity extends BaseActivity implements View.OnClickListener,
        DialogInterface.OnCancelListener, CompoundButton.OnCheckedChangeListener,
        PopKeyBody.ContentListener, OnInputDoneCallBack, GetDepositBankAccountInfoConnect.IDoChangeDepBank, View.OnTouchListener {

    private final String TAG = DoChangeDepositBankActivity.class.getSimpleName();
    private final int REQUESTCODE = 600;
    private final int BANK_PWDSTUP = 2001;                  //银行密码步骤
    private final int OLD_BANK_PWDSTUP = 2002;                   //原银行密码步骤
    private int       mStup = BANK_PWDSTUP;

    private String mBank_no;
    private String mInputPwd;
    private String mNewBankNo;
    private String mCurrency;
    private String mExtAcc;
    private String mPdfFileUrl;
    private String session;

    private TextView            mPdfTv;
    private EditText            mBankNo;
    private PasswordKeyboard    mKeyboard;
    private PopKeyBody          mPopKeyBody;
    private Button              mPositiveBtn;
    private NoSoftInputEditText mPassWordEdit;
    private TextView            mSelectBankTv;
    private Dialog              mProgressDialog;
    private LinearLayout        mRootLayout;

    private GetDepositBankAccountInfoConnect mConnect = new GetDepositBankAccountInfoConnect();

    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private boolean isChecked;

    @Override
    public void initView() {

        Intent intent = getIntent();
        mBank_no = intent.getStringExtra("bank_no");
        mCurrency = intent.getStringExtra("currency");
        mExtAcc = intent.getStringExtra("ext_acc");

        session = SpUtils.getString(DoChangeDepositBankActivity.this, "mSession", "");

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("三存银行变更");

        String html = "<html><body>" +
                "<p><font color=\"#368de7\"><big>请开通新的三方存管银行</big></font></p>\n" +
                "<p  style=\"color:#4c4c4c\">开通三方存管后，您资金可以在银行卡和交易账户中自由调度。</p>\n" +
                "</body></html>";

        TextView htmlTv = (TextView) findViewById(R.id.tv_doChangeDepositHtml);
        htmlTv.setText(Html.fromHtml(html));


        mRootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        mSelectBankTv =(TextView) findViewById(R.id.tv_SelectBank);
        mSelectBankTv.setOnClickListener(this);
        mPdfTv = (TextView) findViewById(R.id.tv_pdf);
        mPdfTv.setOnClickListener(this);

        mPositiveBtn = (Button) findViewById(R.id.btn_depository);
        mPositiveBtn.setOnClickListener(this);
        mPositiveBtn.setClickable(false);
        mPositiveBtn.setFocusable(false);
        mPositiveBtn.setFocusableInTouchMode(false);

        CheckBox checkBox = (CheckBox) findViewById(R.id.doChangeCheckbox);
        checkBox.setOnCheckedChangeListener(this);

        mPassWordEdit = (NoSoftInputEditText) findViewById(R.id.password);
        mPassWordEdit.setOnTouchListener(this);
        mPassWordEdit.setInputType(InputType.TYPE_NULL);
        mBankNo = (EditText) findViewById(R.id.account);

        mPassWordEdit.addTextChangedListener(new PassWordTextWatcher());

        mBankNo.addTextChangedListener(new BankNoTextWatcher());

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.isChecked = isChecked;
        clickableButton(isChecked);
    }

    /**
     * 判断页面的控件中的数据是否添加
     * @return
     */
    private boolean isContentFull() {
        String selectBank = mSelectBankTv.getText().toString();
        if (TextUtils.isEmpty(selectBank)) {
            return false;
        }

        String password = mPassWordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            return false;
        }

        String bankNo = mBankNo.getText().toString();
        if (TextUtils.isEmpty(bankNo)) {
            return false;
        }

        return true;
    }

    /**
     * 设置按钮是否是可点击状态
     */
    private void clickableButton(boolean isChecked) {
        int res1 = isChecked ? R.drawable.button_login_pitchon : R.drawable.button_login_unchecked; //判断checkbox是否点击
        int res2 = isContentFull() ? R.drawable.button_login_pitchon : R.drawable.button_login_unchecked;  //判断页面内容数据是否全部添加
        boolean flag = res1 == res2 ? true : false; //判断上面两返回的资源是否一样

        int num = flag && res1 ==  R.drawable.button_login_pitchon ? 1 : 2;

        boolean isClick = false;
        int res3 =  R.drawable.button_login_unchecked;
        if (num  == 1) {
            isClick = true;
            res3 = R.drawable.button_login_pitchon;
        } else {
            isClick = false;
            res3 =  R.drawable.button_login_unchecked;
        }

        mPositiveBtn.setClickable(isClick);
        mPositiveBtn.setFocusable(isClick);
        mPositiveBtn.setFocusableInTouchMode(isClick);
        mPositiveBtn.setBackgroundResource(res3);



        if (flag) {
            if (res1 ==  R.drawable.button_login_pitchon) {
                mPositiveBtn.setClickable(true);
                mPositiveBtn.setFocusable(true);
                mPositiveBtn.setFocusableInTouchMode(true);
                mPositiveBtn.setBackgroundResource(R.drawable.button_login_pitchon);
            } else {
                mPositiveBtn.setClickable(false);
                mPositiveBtn.setFocusable(false);
                mPositiveBtn.setFocusableInTouchMode(false);
                mPositiveBtn.setBackgroundResource(R.drawable.button_login_unchecked);
            }
        } else {
            mPositiveBtn.setClickable(false);
            mPositiveBtn.setFocusable(false);
            mPositiveBtn.setFocusableInTouchMode(false);
            mPositiveBtn.setBackgroundResource(R.drawable.button_login_unchecked);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
            case R.id.tv_SelectBank:
                Intent intent = new Intent();
                intent.setClass(this, ChangeDepositBankListActivity.class);
                String bankName = mSelectBankTv.getText().toString();
                intent.putExtra("bankName", bankName);
                startActivityForResult(intent, REQUESTCODE);
                break;
            case R.id.tv_pdf:
                if (!TextUtils.isEmpty(mPdfFileUrl) && !TextUtils.isEmpty(mPdfTv.getText().toString())) {
                    DownloadDocPdfDialog.getInstance().showDialog(this, downloadPdfCallback, mPdfFileUrl, mPdfTv.getText().toString());
                } else {
                    Toast.makeText(this, "协议为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_depository:
                initLoadDialog();
                mConnect.getNeedPassword(TAG, session, mBank_no, this);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                InputMethodManager imm = (InputMethodManager) DoChangeDepositBankActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(DoChangeDepositBankActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                mPassWordEdit.setInputType(InputType.TYPE_NULL);
                mPassWordEdit.setText("");
                showKeyBoard("请输入银行卡密码");
                break;
        }

        return true;
    }

    /**
     * 弹出键盘
     */
    private void showKeyBoard(String passwordTitle) {
        Object object = UsefulKeyBoard.getInstance().initUserKeyBoard(true, DoChangeDepositBankActivity.this, true, null);

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
                mKeyboard = UniKey.getInstance().getPasswordKeyboard(DoChangeDepositBankActivity.this, DoChangeDepositBankActivity.this, 6, true, "custom_keyboard_view3");
                mKeyboard.show();

                TextView tv_Title = (TextView) mKeyboard.getView("tvHeadingTitle");
                tv_Title.setText(passwordTitle);

            } catch (UnikeyException e) {
                e.printStackTrace();
                Helper.getInstance().showToast(CustomApplication.getContext(), "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
            }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==  REQUESTCODE && resultCode == RESULT_OK) {
            String BANK_NAME = data.getStringExtra("BANK_NAME");
            String BANK_NO = data.getStringExtra("BANK_NO");
            mSelectBankTv.setText(BANK_NAME);
            mNewBankNo = BANK_NO;

            mBankNo.setText("");
            mPassWordEdit.setText("");

            initLoadDialog();
            mConnect.getPdf(TAG, BANK_NO, this);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dochange_depositbank;
    }


    @Override
    public void loadingClose() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void error(String errorMsg) {
        showMistackDialog(errorMsg, new CancelDialog.PositiveClickListener() {
            @Override
            public void onPositiveClick() {
                mPassWordEdit.setText("");
            }
        });
    }

    @Override
    public void toLogin() {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(DoChangeDepositBankActivity.this, ShouJiZhuCeActivity.class);
            DoChangeDepositBankActivity.this.startActivity(intent);
        } else if (!Db_PUB_USERS.islogin()) {
            intent = new Intent();
            intent.setClass(DoChangeDepositBankActivity.this, TransactionLoginActivity.class);
            DoChangeDepositBankActivity.this.startActivity(intent);
        } else {
            intent = new Intent();
            intent.setClass(DoChangeDepositBankActivity.this, TransactionLoginActivity.class);
            DoChangeDepositBankActivity.this.startActivity(intent);
        }
        finish();
    }

    @Override
    public void getPdfInfo(String pdfName, String pdfUrl) {
        mPdfFileUrl = pdfUrl;
        mPdfTv.setText("《" + pdfName + "》");
        findViewById(R.id.pdfLayout).setVisibility(View.VISIBLE);
    }

    @Override
    public void needInputPassword(String bkcancelPwd_flag) {
        if (bkcancelPwd_flag.equals("1")) {
            mStup = OLD_BANK_PWDSTUP;
            //需要输入原银行卡密码
            showKeyBoard("请输入原银行卡密码");
        } else {
            //不需要输入密码
            initLoadDialog();
            String inputAccount = mBankNo.getText().toString().trim();
            mConnect.changeBank(TAG, session, mBank_no, mNewBankNo, mExtAcc, "", inputAccount, mInputPwd, mCurrency, this);
        }
    }

    @Override
    public void changeBank(String stauts, String errorinfo) {
        if ("2".equals(stauts)) {
            //办理成功
            CancelDialog.cancleDialog(DoChangeDepositBankActivity.this, "办理成功", -1, null, null);
        } else {
            showMistackDialog(errorinfo, null);
        }
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(DoChangeDepositBankActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg, CancelDialog.PositiveClickListener listener) {
        CancelDialog.cancleDialog(DoChangeDepositBankActivity.this, errorMsg, CancelDialog.NOT_BUY, listener, null);
    }

    /**** 普通自定义键盘回调 ****/
    @Override
    public void getContent(String num) {
        if (num.length() == 6) {
            mPopKeyBody.dismiss();

            if (mStup == BANK_PWDSTUP) {//还没有进行点击按钮的步骤
                mInputPwd = num;
                mPassWordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mPassWordEdit.setText(num);
            } else {//需要输入原银行卡密码步骤
                initLoadDialog();
                String inputAccount = mBankNo.getText().toString().trim();
                mConnect.changeBank(TAG, session, mBank_no, mNewBankNo, mExtAcc, num, inputAccount, mInputPwd, mCurrency, this);
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
                mInputPwd = s;
                mPassWordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mPassWordEdit.setText(mInputPwd);
            } else {
                initLoadDialog();
                String inputAccount = mBankNo.getText().toString().trim();
                mConnect.changeBank(TAG, session, mBank_no, mNewBankNo, mExtAcc, s, inputAccount, mInputPwd, mCurrency, this);
            }
        }
    }

    @Override
    public void getCharNum(int num) {
        InputPasswordView inputPasswordView = (InputPasswordView) mKeyboard.getView("inputPasswordView");
        if (inputPasswordView != null) {
            String tmp = "";
            for (int i = 0; i < num; i++) {
                tmp += "*";
            }
            inputPasswordView.setText(tmp);

        }
    }

    /**
     * pdf下载回调
     */
    DownloadDocPdfDialog.DownloadPdfCallback downloadPdfCallback = new DownloadDocPdfDialog.DownloadPdfCallback() {
        @Override
        public void downloadResult(String filePath, String fileName) {
            Intent intent = new Intent();
            intent.setClass(DoChangeDepositBankActivity.this, PdfActivity.class);
            intent.putExtra("flag", 1);
            intent.putExtra("filePath", filePath);
            intent.putExtra("fileName", fileName);
            startActivity(intent);
        }
    };

    /**
     * 银行账户EditText监听
     */
    private class BankNoTextWatcher implements TextWatcher {
        int beforeTextLength = 0;
        int onTextLength = 0;
        boolean isChanged = false;

        int location = 0;// 记录光标的位置
        private char[] tempChar;
        private StringBuffer buffer = new StringBuffer();
        int konggeNumberB = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeTextLength = s.length();
            if (buffer.length() > 0) {
                buffer.delete(0, buffer.length());
            }
            konggeNumberB = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    konggeNumberB++;
                }
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextLength = s.length();
            buffer.append(s.toString());
            if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                clickableButton(isChecked);
                isChanged = false;
                return;
            }
            isChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChanged) {
                location = mBankNo.getSelectionEnd();
                int index = 0;
                while (index < buffer.length()) {
                    if (buffer.charAt(index) == ' ') {
                        buffer.deleteCharAt(index);
                    } else {
                        index++;
                    }
                }

                index = 0;
                int konggeNumberC = 0;
                while (index < buffer.length()) {

                    if (index == 4 || index == 9 || index == 14 || index == 19) {
                        buffer.insert(index, ' ');
                        konggeNumberC++;
                    }
                    index++;
                }

                if (konggeNumberC > konggeNumberB) {
                    location += (konggeNumberC - konggeNumberB);
                }

                tempChar = new char[buffer.length()];
                buffer.getChars(0, buffer.length(), tempChar, 0);
                String str = buffer.toString();
                if (location > str.length()) {
                    location = str.length();
                } else if (location < 0) {
                    location = 0;
                }

                mBankNo.setText(str);
                Editable etable = mBankNo.getText();
                Selection.setSelection(etable, location);
                isChanged = false;
            }
        }
    }


    /**
     * 密码的EditText监听
     */
    private class PassWordTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            clickableButton(isChecked);
        }
    }


}
