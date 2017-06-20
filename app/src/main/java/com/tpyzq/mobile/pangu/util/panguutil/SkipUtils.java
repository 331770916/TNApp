package com.tpyzq.mobile.pangu.util.panguutil;


import android.content.Context;
import android.content.Intent;

import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;

/**
 * Created by wangqi on 2017/4/26.
 * code -6跳转处理
 */

public class SkipUtils {
    private boolean flag = true;

    private SkipUtils() {
    }

    private static SkipUtils skip;

    public static SkipUtils getInstance(){
        if (skip == null){
            synchronized (TransactionLoginActivity.class){
                if (skip == null) {
                    skip =  new SkipUtils();
                }
            }
        }
        return skip;
    }
    public void startLogin(Context context){
        if (flag){
            Intent intent = new Intent();
            intent.setClass(context, TransactionLoginActivity.class);
            context.startActivity(intent);
            flag = false;
        }
    }

    public void setFlag(boolean flag){
        this.flag = flag;
    }
}

