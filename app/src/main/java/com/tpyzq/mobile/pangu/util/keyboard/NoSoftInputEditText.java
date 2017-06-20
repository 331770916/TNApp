package com.tpyzq.mobile.pangu.util.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yzd.unikeysdk.PasswordKeyboard;

/**
 * Created by Alex on 16/10/11.
 */
public class NoSoftInputEditText extends EditText {

    private PasswordKeyboard passwordKeyboard = null;
    private Context mContext = null;

    public NoSoftInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void setPasswordKeyboard(PasswordKeyboard passwordKeyboard) {
        this.passwordKeyboard = passwordKeyboard;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (passwordKeyboard == null) {
            return super.onTouchEvent(event);
        } else {
            this.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0); //强制隐藏键盘
            passwordKeyboard.show();
            return true;
        }
    }

}
